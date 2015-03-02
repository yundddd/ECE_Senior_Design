using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO.Ports;//read and write from serial ports
using System.IO;//export data to a file
using System.Threading;//sepreate threads for different sensors
using System.Diagnostics;
using ZedGraph;
using System.Drawing.Imaging;
using System.Drawing.Drawing2D;

namespace GUI_for_new_lazer_accelerameter_encoder
{
    public partial class Form1 : Form
    {

        public static StringBuilder AccData = new StringBuilder();
        public static StringBuilder EncData = new StringBuilder();
        public static StringBuilder LazData = new StringBuilder();
        public static StringBuilder IncData = new StringBuilder();
        Bitmap indicatorPitch, indicatorRoll;
        LineItem accxcurve, accycurve, acczcurve, gyroxcurve, gyroycurve, gyrozcurve, magxcurve, magycurve, magzcurve, Postpitchcurve, Postyawcurve, Postrollcurve, Arduinopitchcurve, Arduinorollcurve, Compasscurve;
        Scale accScale, gryoScale, magScale, pitchScale, yawScale, rollScale, compassScale;
        IPointListEdit listAccx, listAccy, listAccz, listGryox, listGryoy, listGryoz, listMagx, listMagy, listMagz, listPostpitch, listPostroll, listPostyaw, listArduinopitch, listAruduinoroll, listCompass;

        Matrix xpredict = new Matrix(6, 1);
        Matrix xprevious = new Matrix(6, 1);
        Matrix xnow = new Matrix(6, 1);
        Matrix A = new Matrix(6, 6);
        Matrix Q = new Matrix(6, 6);
        Matrix Ppredicted = new Matrix(6, 6);
        Matrix Pprevious = new Matrix(6, 6);
        Matrix Pnow = new Matrix(6, 6);
        Matrix zn = new Matrix(4, 1);//accPitch,accPitch',gyroPitch, GyroPitch',
        //accRoll,accRoll',gyroRoll, GyroRoll'

        Matrix H = new Matrix(4, 6);
        Matrix S = new Matrix(6, 6);
        Matrix R = new Matrix(4, 4);
        Matrix K = new Matrix(6, 6);
        Matrix y = new Matrix(4, 1);
        Matrix I = Matrix.IdentityMatrix(6, 6);
        double accelPitchProcessNoise = 0.000022;
        double accelPitchMeasurementNoise = 0.234;
        double accelRollProcessNoise = 0.000022;
        double accelRollMeasurementNoise = 0.234;
        double gyroPitchSpeedProcessNoise = .001538;
        double gyroPitchSpeedMeasurementNoise = 0.00114;
        double gyroRollSpeedProcessNoise = .001538;
        double gyroRollSpeedMeasurementNoise = 0.00114;
        double gyroPitchProcessNoise = 0;
        double gyroPitchMeasurementNoise = 0;
        double gyroRollProcessNoise = 0;
        double gyroRollMeasurementNoise = 0;

        double KalmanPitch = 0;
        double KalmanRoll = 0;
        double KalmanYaw = 0;
        double accPitch = 0;
        double accRoll = 0;
        double gyroPitch = 0;
        double gyroRoll = 0;
        double lpAccPitch = 0;
        double lpAccRoll = 0;
        double lpaccx = 0;
        double lpaccy = 0;
        double lpaccz = 0;

        double accelPitchError = 0;
        double accelRollError = 0;
        double gyroPitchError = 0;
        double gyroRollError = 0;
        double Qvalue = 0;

        double dt = 0.0014;
        double sigmaAcc = 0;
        double accCenter = 0.98;
        int stable = 0;
        /// <summary>
        /// ///////////////variables for aduino kalman
        /// </summary>

        public double aPitch;
        public double aRoll;
        kalman kalmanX = new kalman();
        kalman kalmanY = new kalman();
        //MadgwickAHRS alg
        MadgwickAHRS madgwickAHRS;

        byte[] askForOneMeasurementCommand = { 0x31 };
        byte[] Calibrate_gyro_Command = { 0x33 };
        byte[] stopStreamCommand = { 0x38 };


        public Form1()
        {
            InitializeComponent();
            initializeZedGraph();
            refreshPorts();
            indicatorPitch = (Bitmap)pictureBoxPitch.Image;
            indicatorRoll = (Bitmap)pictureBoxRoll.Image;
            initMatrix();
            initTrackBars();

        }
        private void initTrackBars()
        {
            trackBarAccPErr.Minimum = 0;
            trackBarAccPErr.Maximum = 1000;
            trackBarAccRErr.Minimum = 0;
            trackBarAccRErr.Maximum = 1000;
            trackBarGyroPErr.Minimum = 0;
            trackBarGyroPErr.Maximum = 1000;
            trackBarGyroRErr.Minimum = 0;
            trackBarGyroRErr.Maximum = 1000;
            trackBarQ.Minimum = 0;
            trackBarQ.Maximum = 1000;
            trackBarAccPErr.Value = (int)accelPitchError * 10;
            trackBarAccRErr.Value = (int)accelRollError * 10;
            trackBarGyroPErr.Value = (int)gyroPitchError * 10;
            trackBarGyroRErr.Value = (int)gyroRollError * 10;
            labelAccRErr.Text = accelRollError.ToString(); labelAccPErr.Text = accelPitchError.ToString(); labelGyroPErr.Text = gyroPitchError.ToString(); labelGyroRErr.Text = gyroRollError.ToString(); labelQ.Text = Qvalue.ToString();
        }

        private void initMatrix()
        {



            Pprevious[0, 0] = 1000;
            Pprevious[1, 1] = 2;
            Pprevious[2, 2] = 0.7;
            Pprevious[3, 3] = 1000;
            Pprevious[4, 4] = 2;
            Pprevious[5, 5] = 0.7;

            A[0, 0] = 1; A[0, 1] = dt; A[0, 2] = -dt;

            A[1, 1] = 1; A[1, 2] = -1;
            A[2, 2] = 1;
            A[3, 3] = 1; A[3, 4] = dt; A[3, 5] = -dt;
            A[4, 4] = 1; A[4, 5] = -1;
            A[5, 5] = 1;

            //Q[0, 0] = Qvalue; Q[1, 1] = Qvalue; Q[2, 2] = Qvalue; Q[3, 3] = Qvalue;
            Q[0, 0] = accelPitchProcessNoise;
            // Q[1, 1] = gyroPitchProcessNoise;
            Q[1, 1] = gyroPitchSpeedProcessNoise;
            Q[2, 2] = accelRollProcessNoise;
            // Q[4, 4] = gyroRollProcessNoise;
            Q[3, 3] = gyroRollSpeedProcessNoise;
            //  H[0, 0] = 1; H[1, 2] = 1; H[2, 0] = 1; H[3, 1] = 1; H[4, 2] = 1; H[5, 3] = 1;
            //  R[0, 0] = accelPitchError; R[1, 1] = accelRollError; R[2, 2] = gyroPitchError; R[3, 3] = gyroRollError; R[4, 4] = gyroPitchOmegaError; R[5, 5] = gyroRollOmegaError;
            R[0, 0] = accelPitchMeasurementNoise;
            //R[1, 1] = gyroPitchMeasurementNoise;
            R[1, 1] = gyroPitchSpeedMeasurementNoise;
            R[2, 2] = accelRollMeasurementNoise;
            //R[4, 4] = gyroRollMeasurementNoise;
            R[3, 3] = gyroRollSpeedMeasurementNoise;

            H[0, 0] = 1; H[0, 1] = dt; H[2, 0] = dt;
            //H[1, 0] = 1; H[1, 1] = dt; H[2, 1] = dt;
            H[1, 1] = 1; H[1, 2] = 1;
            H[2, 3] = 1; H[2, 4] = dt; H[2, 5] = dt;
            H[3, 4] = 1; H[3, 5] = 1;

            xprevious[2, 0] = 0.03;

            Pprevious[0, 0] = 0.1; Pprevious[1, 1] = 0.1; Pprevious[2, 2] = 0.1; Pprevious[3, 3] = 0.1; Pprevious[4, 4] = 0.1; Pprevious[5, 5] = 0.1;
            // Console.WriteLine(H);
            //kalmanUpdate(0.01,0.01,0.99,0.02,0.01);
        }
        private void setR(double Rvalue)
        {
            R[0, 0] = Rvalue; R[1, 1] = Rvalue; R[2, 2] = Rvalue; R[3, 3] = Rvalue; R[4, 4] = Rvalue; R[5, 5] = Rvalue;
        }

        public void kalmanUpdate(double accx, double accy, double accz, double gyrox, double gyroy)
        {
            accPitch = Math.Atan2(-accx, Math.Sqrt(accy * accy + accz * accz)) / Math.PI * 180;
            accRoll = Math.Atan2(accy, accz) / Math.PI * 180;
            sigmaAcc = Math.Sqrt(accx * accx + accy * accy + accz * accz);
            lpAccPitch = 0.95 * lpAccPitch + 0.05 * accPitch;
            lpAccRoll = 0.95 * lpAccRoll + 0.05 * accRoll;
            //gyroPitch = (gyroPitch + gyroy * dt) * 0.99 + 0.01 * accPitch;
            //gyroRoll = (gyroRoll + (gyrox) * dt);
            Q[0, 0] = accelPitchProcessNoise + dt * dt / 2;
            R[0, 0] = accelPitchMeasurementNoise + 300 * Math.Pow(Math.Abs(accCenter - sigmaAcc), 2);
            R[2, 2] = accelRollMeasurementNoise + 300 * Math.Pow(Math.Abs(accCenter - sigmaAcc), 2);
            Q[1, 1] = (dt * dt) / 2 + gyroPitchSpeedMeasurementNoise;
            Q[2, 2] = accelRollProcessNoise + dt * dt / 2;
            Q[3, 3] = (dt * dt) / 2 + gyroRollSpeedMeasurementNoise;

            if (sigmaAcc > 1.02 || sigmaAcc < 0.98)
            {
                accPitch = 0.9 * lpAccPitch + 0.1 * accPitch;
                accRoll = 0.85 * lpAccRoll + 0.15 * accRoll;
            }

            // Console.WriteLine(accPd - Math.Abs(gyrox));
            // gyroPitch = KalmanPitch + dt * gyroy;//calculate pitch and roll from gyro
            //  gyroRoll = KalmanRoll + dt * gyrox;
            // Console.WriteLine(gyroPitch + "    " + KalmanPitch);
            /*zn[0, 0] = accPitch; zn[1, 0] = gyroPitch; 
            zn[2, 0] = gyroy; zn[3, 0] = accRoll;
            zn[4, 0] = gyroRoll; zn[5,0] = gyrox;
             */
            zn[0, 0] = accPitch; zn[1, 0] = gyroy;
            zn[2, 0] = accRoll;
            zn[3, 0] = gyrox;

            xpredict = A * xprevious;


            Ppredicted = A * Pprevious * Matrix.Transpose(A) + Q;
            y = zn - H * xpredict;


            S = H * Ppredicted * Matrix.Transpose(H) + R;



            K = (Ppredicted * Matrix.Transpose(H)) * S.Invert();

            xnow = xpredict + K * y;

            Pnow = (I - K * H) * Ppredicted;

            xprevious = xnow;
            Pprevious = Pnow;
            KalmanRoll = xnow[0, 0];
            //KalmanRoll = KalmanPitch;
            //KalmanPitch = accPitch;
            KalmanPitch = xnow[3, 0];
            KalmanYaw = xnow[3, 0];
            //KalmanPitchError = xnow[2, 0];
            //  KalmanRollError = Pnow[3,3];
            // KalmanRoll = Pnow[2, 2];
            //Console.WriteLine(Pnow);
        }

        private void refreshPorts()//get names of available ports
        {
            string[] nameArray = null;//store port names
            nameArray = SerialPort.GetPortNames();
            Array.Sort(nameArray);
            StringBuilder mess = new StringBuilder();
            foreach (String a in nameArray) { mess.Append(a + ", "); }
            labelAvailablePorts.Text = "Available Ports: " + mess;
            comboBoxEncoderPortName.DataSource = nameArray;

        }

        private void buttonCheckPorts_Click(object sender, EventArgs e)
        {
            refreshPorts();
        }

        private void buttonEnableSensors_Click(object sender, EventArgs e)
        {
            checkBoxEncoder.Checked = true;

        }

        private void checkBoxEncoder_CheckedChanged(object sender, EventArgs e)
        {
            comboBoxEncoderPortName.Enabled = !comboBoxEncoderPortName.Enabled;
        }


        private void openSelectedPorts()
        {
            try
            {
                if (comboBoxEncoderPortName.Enabled)
                {
                    serialPort.PortName = (String)comboBoxEncoderPortName.SelectedItem;
                    DataCollector Encoder = new DataCollector(serialPort, 1, 77, this, madgwickAHRS, EncData); serialPort.Open(); serialPort.DiscardInBuffer(); Thread.Sleep(100); serialPort.DiscardInBuffer();
                    serialPort.RtsEnable = true;//request to send
                }//set the port name. Baud rate is fixed.}

            }
            catch (UnauthorizedAccessException ex)
            {
                MessageBox.Show("Failed to open some ports");
                checkBoxEncoder.Checked = false;


                if (serialPort.IsOpen) { serialPort.Close(); }

            }
        }


        private void SensorThread(SerialPort mySerialPort, int bytesPerMeasurement, TextBox textBox, StringBuilder data)
        {
            textBox.BeginInvoke(new MethodInvoker(delegate() { textBox.Text = ""; }));

            int bytesRead;
            int t;
            Byte[] dataIn;
            //MessageBox.Show("aaa");           
            while (mySerialPort.IsOpen)
            {
                try
                {
                    if (mySerialPort.BytesToRead != 0)
                    {
                        var startTime = DateTime.Now;
                        var stopwatch = Stopwatch.StartNew();

                        bytesRead = 0;
                        t = 0;
                        dataIn = new Byte[bytesPerMeasurement];
                        t = mySerialPort.Read(dataIn, 0, bytesPerMeasurement);
                        bytesRead += t;
                        while (bytesRead != bytesPerMeasurement)
                        {
                            t = mySerialPort.Read(dataIn, bytesRead, bytesPerMeasurement - bytesRead);
                            bytesRead += t;
                        }
                        //MessageBox.Show("shit received!!");
                        StringBuilder s = new StringBuilder();
                        foreach (Byte b in dataIn) { s.Append(b.ToString("X") + ","); }
                        var line = s.ToString();
                        var timestamp = (startTime + stopwatch.Elapsed);
                        //var timestamp = 0;
                        var lineString = string.Format("{0}  ----{1}          {2}",
                                                          line,
                                                        timestamp.ToString("HH:mm:ss:fff"), mySerialPort.BytesToRead);
                        data.Append(lineString + "\r\n");

                        ////use delegate to change UI thread...
                        textBox.BeginInvoke(new MethodInvoker(delegate() { textBox.Text = line; }));

                        // if (mySerialPort.BytesToRead <= 100) { Thread.Sleep(10); }
                    }
                    else { Thread.Sleep(50); }

                }
                catch (Exception ex)
                {
                    //MessageBox.Show(ex.ToString());
                }
            }

        }

        private void buttonStopStreaming_Click(object sender, EventArgs e)
        {
            serialPort.RtsEnable = false;

            serialPort.Write(stopStreamCommand, 0, 1);

            Thread.Sleep(500);
            try
            {
                closeAllPorts();
                Thread sleepThread = new Thread(sleepForAWhile);
                sleepThread.Start();
            }
            catch (Exception ex) { }
        }
        private void sleepForAWhile()
        {
            Thread.Sleep(100);

        }

        private void buttonSaveEncoderData_Click(object sender, EventArgs e)
        {
            saveFileDialog.ShowDialog();
            TextWriter tw = new StreamWriter(saveFileDialog.FileName);
            tw.Write(EncData.ToString());
            tw.Close();
        }


        private void closeAllPorts()
        {


            if (serialPort.IsOpen) { serialPort.Close(); serialPort.DtrEnable = false; serialPort.DiscardInBuffer(); }


        }
        private void Form1_FormClosed(object sender, FormClosedEventArgs e)
        {
            closeAllPorts();
        }



        private void saveFileDialog_FileOk(object sender, CancelEventArgs e)
        {
        }

        private void buttonSaveLazerData_Click(object sender, EventArgs e)
        {
            saveFileDialog.ShowDialog();
            TextWriter tw = new StreamWriter(saveFileDialog.FileName);
            tw.Write(LazData.ToString());
            tw.Close();
        }

        private void buttonSaveAcc_Click(object sender, EventArgs e)
        {
            saveFileDialog.ShowDialog();
            TextWriter tw = new StreamWriter(saveFileDialog.FileName);
            tw.Write(AccData.ToString());
            tw.Close();
        }

        private void buttonStartListening_Click(object sender, EventArgs e)
        {
            madgwickAHRS = new MadgwickAHRS(0.0044f, 0.025f);
            openSelectedPorts();

            Thread.Sleep(100);

            serialPort.Write(askForOneMeasurementCommand, 0, 1);//initiate communication
        }
        private void initializeZedGraph()
        {

            GraphPane accPane = zedGraphControlAcc.GraphPane;
            GraphPane gyroPane = zedGraphControlGyro.GraphPane;
            GraphPane magPane = zedGraphControlMag.GraphPane;

            GraphPane pitchPane = zedGraphControlPitch.GraphPane;
            GraphPane rollPane = zedGraphControlRoll.GraphPane;
            GraphPane yawPane = zedGraphControlYaw.GraphPane;

            GraphPane compassPane = zedGraphControlCompass.GraphPane;

            accPane.Title.Text = "ACC";
            accPane.XAxis.Title.Text = "Time, milliSeconds";
            accPane.YAxis.Title.Text = "g";
            accPane.YAxis.Scale.Min = -1.2;
            accPane.YAxis.Scale.Max = 1.2;

            gyroPane.Title.Text = "Gyro";
            gyroPane.XAxis.Title.Text = "Time, milliSeconds";
            gyroPane.YAxis.Title.Text = "deg/s";
            gyroPane.YAxis.Scale.Min = -200;
            gyroPane.YAxis.Scale.Max = 200;

            magPane.Title.Text = "Mag";
            magPane.XAxis.Title.Text = "Time, milliSeconds";
            magPane.YAxis.Title.Text = "deg";
            magPane.YAxis.Scale.Min = -10;
            magPane.YAxis.Scale.Max = 10;

            pitchPane.Title.Text = "Pitch";
            pitchPane.XAxis.Title.Text = "Time, milliSeconds";
            pitchPane.YAxis.Title.Text = "deg";
            pitchPane.YAxis.Scale.Min = -6;
            pitchPane.YAxis.Scale.Max = 6;

            rollPane.Title.Text = "roll";
            rollPane.XAxis.Title.Text = "Time, milliSeconds";
            rollPane.YAxis.Title.Text = "deg";
            rollPane.YAxis.Scale.Min = -6;
            rollPane.YAxis.Scale.Max = 6;

            yawPane.Title.Text = "Yaw";
            yawPane.XAxis.Title.Text = "Time, milliSeconds";
            yawPane.YAxis.Title.Text = "deg";
            yawPane.YAxis.Scale.Min = -6;
            yawPane.YAxis.Scale.Max = 6;

            compassPane.Title.Text = "Heading";
            compassPane.XAxis.Title.Text = "Time, milliSeconds";
            compassPane.YAxis.Title.Text = "deg";
            compassPane.YAxis.Scale.Min = -360;
            compassPane.YAxis.Scale.Max = 360;

            // Save 1200 points.  At 50 ms sample rate, this is one minute
            // The RollingPointPairList is an efficient storage class that always
            // keeps a rolling set of point data without needing to shift any data values
            RollingPointPairList measurementax = new RollingPointPairList(50000);
            RollingPointPairList measurementay = new RollingPointPairList(50000);
            RollingPointPairList measurementaz = new RollingPointPairList(50000);

            RollingPointPairList measurementgrx = new RollingPointPairList(50000);
            RollingPointPairList measurementgry = new RollingPointPairList(50000);
            RollingPointPairList measurementgrz = new RollingPointPairList(50000);

            RollingPointPairList measurementmx = new RollingPointPairList(50000);
            RollingPointPairList measurementmy = new RollingPointPairList(50000);
            RollingPointPairList measurementmz = new RollingPointPairList(50000);

            RollingPointPairList measurementPostpitch = new RollingPointPairList(50000);
            RollingPointPairList measurementPostroll = new RollingPointPairList(50000);
            RollingPointPairList measurementPostyaw = new RollingPointPairList(50000);

            RollingPointPairList measurementArduinopitch = new RollingPointPairList(50000);
            RollingPointPairList measurementArduinoPostroll = new RollingPointPairList(50000);
            RollingPointPairList measurementArduinoPostyaw = new RollingPointPairList(50000);

            RollingPointPairList measurementCompass = new RollingPointPairList(50000);

            // Initially, a curve is added with no data points (list is empty)
            // Color is blue, and there will be no symbols
            LineItem curveMeasurementax = accPane.AddCurve("Accx(g)", measurementax, Color.Blue, SymbolType.None);
            LineItem curveMeasurementay = accPane.AddCurve("Accy(g)", measurementay, Color.Red, SymbolType.None);
            LineItem curveMeasurementaz = accPane.AddCurve("Accz(g)", measurementaz, Color.Black, SymbolType.None);

            LineItem curveMeasurementgrx = gyroPane.AddCurve("gyrox(inches)", measurementgrx, Color.Blue, SymbolType.None);
            LineItem curveMeasurementgry = gyroPane.AddCurve("gyroy(inches)", measurementgry, Color.Red, SymbolType.None);
            LineItem curveMeasurementgrz = gyroPane.AddCurve("gyroz(inches)", measurementgrz, Color.Black, SymbolType.None);

            LineItem curveMeasurementmx = magPane.AddCurve("Guassx", measurementmx, Color.Blue, SymbolType.None);
            LineItem curveMeasurementmy = magPane.AddCurve("GuassY", measurementmy, Color.Red, SymbolType.None);
            LineItem curveMeasurementmz = magPane.AddCurve("GuassZ", measurementmz, Color.Black, SymbolType.None);

            LineItem curveMeasurementPostpitch = pitchPane.AddCurve("Post", measurementPostpitch, Color.Blue, SymbolType.None);
            LineItem curveMeasurementArduinopitch = pitchPane.AddCurve("Arduino", measurementArduinopitch, Color.Red, SymbolType.None);
            LineItem curveMeasurementPostroll = rollPane.AddCurve("Post", measurementPostroll, Color.Blue, SymbolType.None);
            LineItem curveMeasurementArduinoroll = rollPane.AddCurve("Arduino", measurementArduinoPostroll, Color.Red, SymbolType.None);
            LineItem curveMeasurementPostyaw = yawPane.AddCurve("Post", measurementPostyaw, Color.Blue, SymbolType.None);
            LineItem curveMeasurementArduinoyaw = yawPane.AddCurve("Arduino", measurementArduinoPostyaw, Color.Red, SymbolType.None);

            LineItem curveMeasurementCompass = compassPane.AddCurve("heading", measurementCompass, Color.Red, SymbolType.None);

            accPane.XAxis.Scale.Min = 0;
            accPane.XAxis.Scale.Max = 10;
            accPane.XAxis.Scale.MinorStep = 30;
            accPane.XAxis.Scale.MajorStep = 30;
            zedGraphControlAcc.AxisChange();

            gyroPane.XAxis.Scale.Min = 0;
            gyroPane.XAxis.Scale.Max = 10;
            gyroPane.XAxis.Scale.MinorStep = 30;
            gyroPane.XAxis.Scale.MajorStep = 30;
            zedGraphControlGyro.AxisChange();

            magPane.XAxis.Scale.Min = 0;
            magPane.XAxis.Scale.Max = 10;
            magPane.XAxis.Scale.MinorStep = 30;
            magPane.XAxis.Scale.MajorStep = 30;
            zedGraphControlMag.AxisChange();

            pitchPane.XAxis.Scale.Min = 0;
            pitchPane.XAxis.Scale.Max = 10;
            pitchPane.XAxis.Scale.MinorStep = 30;
            pitchPane.XAxis.Scale.MajorStep = 30;
            zedGraphControlPitch.AxisChange();

            rollPane.XAxis.Scale.Min = 0;
            rollPane.XAxis.Scale.Max = 10;
            rollPane.XAxis.Scale.MinorStep = 30;
            rollPane.XAxis.Scale.MajorStep = 30;
            zedGraphControlRoll.AxisChange();

            yawPane.XAxis.Scale.Min = 0;
            yawPane.XAxis.Scale.Max = 10;
            yawPane.XAxis.Scale.MinorStep = 30;
            yawPane.XAxis.Scale.MajorStep = 30;
            zedGraphControlYaw.AxisChange();

            compassPane.XAxis.Scale.Min = 0;
            compassPane.XAxis.Scale.Max = 10;
            compassPane.XAxis.Scale.MinorStep = 30;
            compassPane.XAxis.Scale.MajorStep = 30;
            zedGraphControlCompass.AxisChange();



            accxcurve = zedGraphControlAcc.GraphPane.CurveList[0] as LineItem;
            accycurve = zedGraphControlAcc.GraphPane.CurveList[1] as LineItem;
            acczcurve = zedGraphControlAcc.GraphPane.CurveList[2] as LineItem;
            gyroxcurve = zedGraphControlGyro.GraphPane.CurveList[0] as LineItem;
            gyroycurve = zedGraphControlGyro.GraphPane.CurveList[1] as LineItem;
            gyrozcurve = zedGraphControlGyro.GraphPane.CurveList[2] as LineItem;
            magxcurve = zedGraphControlMag.GraphPane.CurveList[0] as LineItem;
            magycurve = zedGraphControlMag.GraphPane.CurveList[1] as LineItem;
            magzcurve = zedGraphControlMag.GraphPane.CurveList[2] as LineItem;
            Postpitchcurve = zedGraphControlPitch.GraphPane.CurveList[0] as LineItem;
            Postrollcurve = zedGraphControlRoll.GraphPane.CurveList[0] as LineItem;
            Postyawcurve = zedGraphControlYaw.GraphPane.CurveList[0] as LineItem;
            Arduinopitchcurve = zedGraphControlPitch.GraphPane.CurveList[1] as LineItem;
            Arduinorollcurve = zedGraphControlRoll.GraphPane.CurveList[1] as LineItem;

            Compasscurve = zedGraphControlCompass.GraphPane.CurveList[0] as LineItem;


            accScale = zedGraphControlAcc.GraphPane.XAxis.Scale;
            gryoScale = zedGraphControlGyro.GraphPane.XAxis.Scale;
            magScale = zedGraphControlMag.GraphPane.XAxis.Scale;
            pitchScale = zedGraphControlPitch.GraphPane.XAxis.Scale;
            yawScale = zedGraphControlYaw.GraphPane.XAxis.Scale;
            rollScale = zedGraphControlRoll.GraphPane.XAxis.Scale;
            compassScale = zedGraphControlCompass.GraphPane.XAxis.Scale;


            listAccx = accxcurve.Points as IPointListEdit;
            listAccy = accycurve.Points as IPointListEdit;
            listAccz = acczcurve.Points as IPointListEdit;
            listGryox = gyroxcurve.Points as IPointListEdit;
            listGryoy = gyroycurve.Points as IPointListEdit;
            listGryoz = gyrozcurve.Points as IPointListEdit;
            listMagx = magxcurve.Points as IPointListEdit;
            listMagy = magycurve.Points as IPointListEdit;
            listMagz = magzcurve.Points as IPointListEdit;
            listPostpitch = Postpitchcurve.Points as IPointListEdit;
            listPostroll = Postrollcurve.Points as IPointListEdit;
            listPostyaw = Postyawcurve.Points as IPointListEdit;

            listArduinopitch = Arduinopitchcurve.Points as IPointListEdit;
            listAruduinoroll = Arduinorollcurve.Points as IPointListEdit;

            listCompass = Compasscurve.Points as IPointListEdit;
        }


        public void updateZedGraph(int i, double ax, double ay, double az, double grx, double gry, double grz, double mx, double my, double mz, double Pitch, double Roll, double yaw)
        {
            /*      lpaccx = 0.99 * lpaccx + 0.01 * ax;
               lpaccy = 0.99 * lpaccy + 0.01 * ay;
               lpaccz = 0.99 * lpaccz + 0.01 * az;
               lpaccPitch = Math.Atan2(-lpaccx, Math.Sqrt(lpaccy * lpaccy + lpaccz * lpaccz)) / Math.PI * 180;
               lpaccRoll = Math.Atan2(lpaccy, lpaccz) / Math.PI * 180;

              


             
            

                     kalmanX.updateRmeasure(sigmaAcc, gry);
                     kalmanY.updateRmeasure(sigmaAcc, grx);

                     aPitch = kalmanX.arduinoKalmanUpdate(accPitch, gry, dt, sigmaAcc);
                     aRoll = kalmanY.arduinoKalmanUpdate(accRoll, grx, dt, sigmaAcc);//post C# proceessing...red line has better performance
         */
            //Console.WriteLine(madgwickAHRS.MadgPitch);

            accPitch = Math.Atan2(-ax, Math.Sqrt(ay * ay + az * az)) / Math.PI * 180;
            accRoll = Math.Atan2(ay, az) / Math.PI * 180;
          
            lpAccPitch = 0.95 * lpAccPitch + 0.05 * accPitch;
            lpAccRoll = 0.95 * lpAccRoll + 0.05 * accRoll;

            double heading = 0;


            listAccx.Add(i, ax);
            listAccy.Add(i, ay);
            listAccz.Add(i, az);
            listGryox.Add(i, grx);
            listGryoy.Add(i, gry);
            listGryoz.Add(i, grz);
            listMagx.Add(i, mx);
            listMagy.Add(i, my);
            listMagz.Add(i, mz);
            listPostpitch.Add(i, madgwickAHRS.MadgPitch);//blue
            listPostroll.Add(i, madgwickAHRS.MadgRoll);
            listArduinopitch.Add(i, lpAccPitch);//red
            listAruduinoroll.Add(i, lpAccRoll);
            listPostyaw.Add(i, madgwickAHRS.MadgYaw);

            listCompass.Add(i, heading);

            // Keep the X scale at a rolling 30 second interval, with one
            // major step between the max X value and the end of the axis

            if (i > accScale.Max - accScale.MajorStep)
            {
                accScale.Max = i + accScale.MajorStep;
                accScale.Min = accScale.Max - 100.0;
                gryoScale.Max = i + gryoScale.MajorStep;
                gryoScale.Min = gryoScale.Max - 100.0;
                magScale.Max = i + magScale.MajorStep;
                magScale.Min = magScale.Max - 100.0;
                pitchScale.Max = i + pitchScale.MajorStep;
                pitchScale.Min = pitchScale.Max - 100.0;
                yawScale.Max = i + yawScale.MajorStep;
                yawScale.Min = yawScale.Max - 100.0;
                rollScale.Max = i + rollScale.MajorStep;
                rollScale.Min = rollScale.Max - 100.0;
                compassScale.Max = i + compassScale.MajorStep;
                compassScale.Min = compassScale.Max - 100.0;

                zedGraphControlAcc.AxisChange();
                zedGraphControlGyro.AxisChange();
                zedGraphControlMag.AxisChange();
                zedGraphControlPitch.AxisChange();
                zedGraphControlRoll.AxisChange();
                zedGraphControlYaw.AxisChange();
                zedGraphControlCompass.AxisChange();
            }

            zedGraphControlAcc.Invalidate();
            zedGraphControlGyro.Invalidate();
            zedGraphControlMag.Invalidate();
            zedGraphControlPitch.Invalidate();
            zedGraphControlRoll.Invalidate();
            zedGraphControlYaw.Invalidate();
            zedGraphControlCompass.Validate();

            pictureBoxPitch.BeginInvoke(new MethodInvoker(delegate()
            {
                labelAccx.Text = String.Format("{0,-7:+0.00;-0.00  }", madgwickAHRS.Beta);
                /*    pictureBoxPitch.Image = (Image)(RotateImage(indicatorPitch, (float)KalmanPitch));
                    pictureBoxRoll.Image = (Image)(RotateImage(indicatorRoll, (float)KalmanRoll));
                    trackBarAccPErr.Value = (int)accelPitchError * 10;
                    trackBarAccRErr.Value = (int)accelRollError * 10;
                    trackBarGyroPErr.Value = (int)gyroPitchError * 10;
                    trackBarGyroRErr.Value = (int)gyroRollError * 10;
                    labelAccRErr.Text = accelRollError.ToString(); labelAccPErr.Text = accelPitchError.ToString(); labelGyroPErr.Text = gyroPitchError.ToString(); labelGyroRErr.Text = gyroRollError.ToString(); labelQ.Text = Qvalue.ToString();
   
                labelAccx.Text = String.Format("{0,-7:+0.00;-0.00  }", ax);
                labelAccy.Text = String.Format("{0,-7:+0.00;-0.00  }", ay);
                labelAccz.Text = String.Format("{0,-7:+0.00;-0.00  }", az);
                labelGryx.Text = String.Format("{0,-7:+0.00;-0.00  }", grx);
                labelGryy.Text = String.Format("{0,-7:+0.00;-0.00  }", gry);
                labelGryz.Text = String.Format("{0,-7:+0.00;-0.00  }", grz);
                //       labelMagx.Text = String.Format("{0,-7:+0.00;-0.00  }", mx);
                //       labelMagy.Text = String.Format("{0,-7:+0.00;-0.00  }", my);
                //       labelMagz.Text = String.Format("{0,-7:+0.00;-0.00  }", mz);
                //      labelPitch.Text = String.Format("{0,-7:+0.00;-0.00  }", KalmanPitch);
                //       labelRoll.Text = String.Format("{0,-7:+0.00;-0.00  }", KalmanRoll);*/
                double siga=Math.Sqrt(ax*ax+ay*ay+az*az);
                if (siga > 1.2 || siga < 0.98) { labelYaw.Text = String.Format("{0,-7:+0.00;-0.00  }", siga); }
                

            }));

            //String textlabel = String.Format("{0,-7:+0.00;-0.00  }", ax) + String.Format("{0,-7:+0.00;-0.00  }", ay) + String.Format("{0,-7:+0.00;-0.00  }", az) + String.Format("{0,-7:+0.00;-0.00  }", grx) + String.Format("{0,-7:+0.00;-0.00  }", gry) + String.Format("{0,-7:+0.00;-0.00  }", grz) + String.Format("{0,-7:+0.00;-0.00  }", aPitch) + String.Format("{0,-7:+0.00;-0.00  }", aRoll) + String.Format("{0,-7:+0.00;-0.00  }", kalmanX.aP[0,0]) + String.Format("{0,-7:+0.00;-0.00  }", kalmanX.aP[0,1]) + String.Format("{0,-7:+0.00;-0.00  }", kalmanX.aP[1,0]) + String.Format("{0,-7:+0.00;-0.00  }", kalmanX.aP[1,1]);
            String textlabel = ax + "," + ay + "," + az + "," + grx + "," + gry + "," + grz + "," + aPitch + "," + aRoll;


        }

        private Bitmap RotateImage(Bitmap bmp, float angle)
        {
            Bitmap rotatedImage = new Bitmap(bmp.Width, bmp.Height);
            using (Graphics g = Graphics.FromImage(rotatedImage))
            {
                g.TranslateTransform(bmp.Width / 2, bmp.Height / 2); //set the rotation point as the center into the matrix
                g.RotateTransform(angle); //rotate
                g.TranslateTransform(-bmp.Width / 2, -bmp.Height / 2); //restore rotation point into the matrix
                g.DrawImage(bmp, new Point(0, 0)); //draw the image on the new bitmap
            }
            //bmp.Dispose();
            return rotatedImage;
        }

        private void trackBarAccPErr_Scroll(object sender, EventArgs e)
        {
            accelPitchError = trackBarAccPErr.Value / 10.0;
            labelAccPErr.Text = accelPitchError.ToString();
            R[0, 0] = accelPitchError;
        }

        private void trackBarAccRErr_Scroll(object sender, EventArgs e)
        {
            accelRollError = trackBarAccRErr.Value / 10.0;
            labelAccRErr.Text = accelRollError.ToString();
            R[1, 1] = accelRollError;
        }

        private void trackBarGyroPErr_Scroll(object sender, EventArgs e)
        {
            gyroPitchError = trackBarGyroPErr.Value / 10.0;
            labelGyroPErr.Text = gyroPitchError.ToString();
            R[2, 2] = gyroPitchError;
        }

        private void trackBarGyroRErr_Scroll(object sender, EventArgs e)
        {
            gyroRollError = trackBarGyroRErr.Value / 10.0;
            labelGyroRErr.Text = gyroRollError.ToString();
            R[3, 3] = gyroRollError;
        }

        private void trackBarQ_Scroll(object sender, EventArgs e)
        {
            Qvalue = trackBarQ.Value / 1000.0;
            labelQ.Text = Qvalue.ToString();
            //setQ(Qvalue);
        }

        private void button_calibrate_gyro_Click(object sender, EventArgs e)
        {

            serialPort.Write(Calibrate_gyro_Command, 0, 1);//initiate communication
        }




    }
    public class DataCollector
    {
        private readonly Action _processMeasurement;
        private SerialPort _serialPort;
        private readonly int SizeOfMeasurement;
        List<byte> Data = new List<byte>();
        List<byte> measurementData = new List<byte>();
        //TextBox textBox;
        Form1 form;
        ZedGraphControl graph;
        String serialMsg;
        double ax, ay, az, gx, gy, gz, mx, my, mz, pitch, roll, yaw;
        long timeStamp;
        string[] result;
        static int meaNum = 0;
        Form1 aform;
        String textlabel;
        MadgwickAHRS madgwickAHRS;
        int i = 0;
        StringBuilder PitchLog;
        byte[] bytes = new byte[36];//bytes from 6DOF
        byte[] askForOneMeasurementCommand = { 0x31 };
        public DataCollector(SerialPort port, int sensor, int SizeOfMeasurement, Form1 aform, MadgwickAHRS m, StringBuilder s)
        {
            //this.textBox = textBox;
            this.SizeOfMeasurement = SizeOfMeasurement;
            this.aform = aform;
            switch (sensor)
            {
                case 1: _processMeasurement = new Action(process9DOF);
                    break;

            }
            _serialPort = port;

            _serialPort.DataReceived += SerialPortDataReceived;
            madgwickAHRS = m;
            
            PitchLog = s;
        }
        private void SerialPortDataReceived(object sender, SerialDataReceivedEventArgs e)
        {
            // Console.Write("..");
            try
            {
                if (_serialPort.BytesToRead >= 36)
                {
                    _serialPort.Read(bytes, 0, 36);
                    //  for (int i = 0; i < 17; i++)
                   // {
                    //       Console.Write(bytes[i] + ",");
                   // }

                   // Console.WriteLine(" ");
                    _processMeasurement();
                }
                Thread.Sleep(50);
               //_serialPort.Write(askForOneMeasurementCommand, 0, 1);
               _serialPort.Write(askForOneMeasurementCommand, 0, 1);
            }
            catch (Exception ex) { }
        }

        public void process9DOF()
        {

            float[] resultArray = new float[3];
            resultArray = translateAcc();
            ax = resultArray[0]/1000;
            ay = resultArray[1]/1000;
            az = resultArray[2]/1000;
            resultArray = translateGyro();
            gx = resultArray[0]/1000;
            gy = resultArray[1]/1000;
            gz = resultArray[2]/1000;
            resultArray = translateMag();
            mx = resultArray[0]/1000;
            my = resultArray[1]/1000;
            mz = resultArray[2]/1000;
          //  Console.WriteLine(ax.ToString() + " , " + ay.ToString() + " , " + az.ToString());
            Console.WriteLine(gx.ToString() + " , " + gy.ToString() + " , " + gz.ToString());
          //  Console.WriteLine(mx.ToString() + " , " + my.ToString() + " , " + mz.ToString());



            //  mx= translateTime();


            /*result = serialMsg.Split(',');
             ax = Convert.ToSingle(result[0]);
             ay = Convert.ToSingle(result[1]);
             az = Convert.ToSingle(result[2]);
             gx = Convert.ToSingle(result[3]);
             gy = Convert.ToSingle(result[4]);
             gz = Convert.ToSingle(result[5]);
         */
            //   mx = Convert.ToSingle(result[6]);
            //    my = Convert.ToSingle(result[7]);
            //  mz = Convert.ToSingle(result[8]);
            // pitch = Convert.ToDouble(result[9]);
            // roll = Convert.ToDouble(result[10]);
            // yaw = Convert.ToSingle(result[8]);




            // yaw = 0;
//
      //  madgwickAHRS.Update((float)(gx ), (float)(gy), (float)(gz), (float)ax, (float)ay, (float)az, (float)mx, (float)my, (float)mz);
        madgwickAHRS.SamplePeriod = 0.05f;
         madgwickAHRS.Update((float)(gx), (float)(gy), (float)(gz ), (float)ax, (float)ay, (float)az);
     //       aform.kalmanUpdate(ax, ay, az, gx, gy);
     //       i++;

     //       if (i % 20 == 0)
      //      {
              aform.updateZedGraph(meaNum++, ax, ay, az, gx, gy, gz, mx, my, mz, pitch, roll, 0);
    //        }
            //   PitchLog.Append(textlabel + "\r\n");
           // Thread.Sleep(5);
           // _serialPort.Write(askForOneMeasurementCommand, 0, 1);

        }
        private float[] translateAcc()
        {
            float[] acc = new float[3];
           // int[] accTemp = new int[3];

           // accTemp[0] = ((Int16)bytes[0] << 8) + (Int16)bytes[1];
            //accTemp[1] = ((Int16)bytes[2] << 8) + (Int16)bytes[3];
            //accTemp[2] = ((Int16)bytes[4] << 8) + (Int16)bytes[5];
            acc[0] = ((Int32)bytes[0] << 24) | ((Int32)bytes[1] << 16) | ((Int32)bytes[2] << 8) | ((Int32)bytes[3] );
            acc[1] = ((Int32)bytes[4] << 24) | ((Int32)bytes[5] << 16) | ((Int32)bytes[6] << 8) | ((Int32)bytes[7]);
            acc[2] = ((Int32)bytes[8] << 24) | ((Int32)bytes[9] << 16) | ((Int32)bytes[10] << 8) | ((Int32)bytes[11]);

           // Console.WriteLine(bytes[1] + " " + bytes[0] + "," + bytes[3] + " " + bytes[2] + "," + bytes[5] + " " + bytes[4]);

            return acc;
        }
        private float[] translateGyro()
        {
            float[] gyro = new float[3];
            gyro[0] = ((Int32)bytes[12] << 24) | ((Int32)bytes[13] << 16) | ((Int32)bytes[14] << 8) | ((Int32)bytes[15]);
            gyro[1] = ((Int32)bytes[16] << 24) | ((Int32)bytes[17] << 16) | ((Int32)bytes[18] << 8) | ((Int32)bytes[19]);
            gyro[2] = ((Int32)bytes[20] << 24) | ((Int32)bytes[21] << 16) | ((Int32)bytes[22] << 8) | ((Int32)bytes[23]);
            return gyro;
        }
        private long translateTime()
        {
            long time;
            time = (Int16)bytes[12] << 16 + (Int16)bytes[13] << 8 + (Int16)bytes[14];
            //Console.WriteLine(time);
            return time;
        }

        private float[] translateMag()
        {
            float[] mag = new float[3];
            mag[0] = ((Int32)bytes[24] << 24) | ((Int32)bytes[25] << 16) | ((Int32)bytes[26] << 8) | ((Int32)bytes[27]);
            mag[1] = ((Int32)bytes[28] << 24) | ((Int32)bytes[29] << 16) | ((Int32)bytes[30] << 8) | ((Int32)bytes[31]);
            mag[2] = ((Int32)bytes[32] << 24) | ((Int32)bytes[33] << 16) | ((Int32)bytes[34] << 8) | ((Int32)bytes[35]);
           
            return mag;
        }






    }




}
