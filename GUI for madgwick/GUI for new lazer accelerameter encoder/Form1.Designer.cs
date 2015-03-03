namespace GUI_for_new_lazer_accelerameter_encoder
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.buttonCheckPorts = new System.Windows.Forms.Button();
            this.labelAvailablePorts = new System.Windows.Forms.Label();
            this.comboBoxEncoderPortName = new System.Windows.Forms.ComboBox();
            this.checkBoxEncoder = new System.Windows.Forms.CheckBox();
            this.label1 = new System.Windows.Forms.Label();
            this.buttonStopStreaming = new System.Windows.Forms.Button();
            this.saveFileDialog = new System.Windows.Forms.SaveFileDialog();
            this.buttonSaveEncoderData = new System.Windows.Forms.Button();
            this.buttonStartListening = new System.Windows.Forms.Button();
            this.serialPort = new System.IO.Ports.SerialPort(this.components);
            this.zedGraphControlAcc = new ZedGraph.ZedGraphControl();
            this.backgroundWorker1 = new System.ComponentModel.BackgroundWorker();
            this.zedGraphControlGyro = new ZedGraph.ZedGraphControl();
            this.zedGraphControlMag = new ZedGraph.ZedGraphControl();
            this.tabControl1 = new System.Windows.Forms.TabControl();
            this.tabPage1 = new System.Windows.Forms.TabPage();
            this.tabPage2 = new System.Windows.Forms.TabPage();
            this.zedGraphControlYaw = new ZedGraph.ZedGraphControl();
            this.zedGraphControlRoll = new ZedGraph.ZedGraphControl();
            this.zedGraphControlPitch = new ZedGraph.ZedGraphControl();
            this.tabPage3 = new System.Windows.Forms.TabPage();
            this.zedGraphControlCompass = new ZedGraph.ZedGraphControl();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.label4 = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.label6 = new System.Windows.Forms.Label();
            this.label7 = new System.Windows.Forms.Label();
            this.labelAccx = new System.Windows.Forms.Label();
            this.labelAccy = new System.Windows.Forms.Label();
            this.labelAccz = new System.Windows.Forms.Label();
            this.labelGryz = new System.Windows.Forms.Label();
            this.labelGryy = new System.Windows.Forms.Label();
            this.labelGryx = new System.Windows.Forms.Label();
            this.labelMagz = new System.Windows.Forms.Label();
            this.labelMagy = new System.Windows.Forms.Label();
            this.labelMagx = new System.Windows.Forms.Label();
            this.labelPitch = new System.Windows.Forms.Label();
            this.labelRoll = new System.Windows.Forms.Label();
            this.labelYaw = new System.Windows.Forms.Label();
            this.pictureBoxRoll = new System.Windows.Forms.PictureBox();
            this.pictureBoxPitch = new System.Windows.Forms.PictureBox();
            this.trackBarAccPErr = new System.Windows.Forms.TrackBar();
            this.trackBarQ = new System.Windows.Forms.TrackBar();
            this.trackBarAccRErr = new System.Windows.Forms.TrackBar();
            this.trackBarGyroRErr = new System.Windows.Forms.TrackBar();
            this.trackBarGyroPErr = new System.Windows.Forms.TrackBar();
            this.label8 = new System.Windows.Forms.Label();
            this.label9 = new System.Windows.Forms.Label();
            this.label10 = new System.Windows.Forms.Label();
            this.label11 = new System.Windows.Forms.Label();
            this.labelGyroRErr = new System.Windows.Forms.Label();
            this.labelGyroPErr = new System.Windows.Forms.Label();
            this.labelAccRErr = new System.Windows.Forms.Label();
            this.labelAccPErr = new System.Windows.Forms.Label();
            this.label12 = new System.Windows.Forms.Label();
            this.labelQ = new System.Windows.Forms.Label();
            this.button_calibrate_gyro = new System.Windows.Forms.Button();
            this.tabControl1.SuspendLayout();
            this.tabPage1.SuspendLayout();
            this.tabPage2.SuspendLayout();
            this.tabPage3.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBoxRoll)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBoxPitch)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarAccPErr)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarQ)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarAccRErr)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarGyroRErr)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarGyroPErr)).BeginInit();
            this.SuspendLayout();
            // 
            // buttonCheckPorts
            // 
            this.buttonCheckPorts.Location = new System.Drawing.Point(30, 9);
            this.buttonCheckPorts.Name = "buttonCheckPorts";
            this.buttonCheckPorts.Size = new System.Drawing.Size(150, 42);
            this.buttonCheckPorts.TabIndex = 0;
            this.buttonCheckPorts.Text = "Refresh available ports";
            this.buttonCheckPorts.UseVisualStyleBackColor = true;
            this.buttonCheckPorts.Click += new System.EventHandler(this.buttonCheckPorts_Click);
            // 
            // labelAvailablePorts
            // 
            this.labelAvailablePorts.AutoSize = true;
            this.labelAvailablePorts.Location = new System.Drawing.Point(204, 38);
            this.labelAvailablePorts.Name = "labelAvailablePorts";
            this.labelAvailablePorts.Size = new System.Drawing.Size(0, 13);
            this.labelAvailablePorts.TabIndex = 1;
            // 
            // comboBoxEncoderPortName
            // 
            this.comboBoxEncoderPortName.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.comboBoxEncoderPortName.Enabled = false;
            this.comboBoxEncoderPortName.FormattingEnabled = true;
            this.comboBoxEncoderPortName.Location = new System.Drawing.Point(90, 153);
            this.comboBoxEncoderPortName.Name = "comboBoxEncoderPortName";
            this.comboBoxEncoderPortName.Size = new System.Drawing.Size(121, 21);
            this.comboBoxEncoderPortName.TabIndex = 2;
            // 
            // checkBoxEncoder
            // 
            this.checkBoxEncoder.AutoSize = true;
            this.checkBoxEncoder.Location = new System.Drawing.Point(30, 155);
            this.checkBoxEncoder.Name = "checkBoxEncoder";
            this.checkBoxEncoder.Size = new System.Drawing.Size(54, 17);
            this.checkBoxEncoder.TabIndex = 11;
            this.checkBoxEncoder.Text = "9DOF";
            this.checkBoxEncoder.UseVisualStyleBackColor = true;
            this.checkBoxEncoder.CheckedChanged += new System.EventHandler(this.checkBoxEncoder_CheckedChanged);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(385, 12);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(134, 13);
            this.label1.TabIndex = 16;
            this.label1.Text = "Default baudrate = 115200";
            // 
            // buttonStopStreaming
            // 
            this.buttonStopStreaming.Location = new System.Drawing.Point(187, 63);
            this.buttonStopStreaming.Name = "buttonStopStreaming";
            this.buttonStopStreaming.Size = new System.Drawing.Size(155, 79);
            this.buttonStopStreaming.TabIndex = 22;
            this.buttonStopStreaming.Text = "StopStreaming";
            this.buttonStopStreaming.UseVisualStyleBackColor = true;
            this.buttonStopStreaming.Click += new System.EventHandler(this.buttonStopStreaming_Click);
            // 
            // saveFileDialog
            // 
            this.saveFileDialog.FileOk += new System.ComponentModel.CancelEventHandler(this.saveFileDialog_FileOk);
            // 
            // buttonSaveEncoderData
            // 
            this.buttonSaveEncoderData.Location = new System.Drawing.Point(505, 63);
            this.buttonSaveEncoderData.Name = "buttonSaveEncoderData";
            this.buttonSaveEncoderData.Size = new System.Drawing.Size(114, 46);
            this.buttonSaveEncoderData.TabIndex = 23;
            this.buttonSaveEncoderData.Text = "Export";
            this.buttonSaveEncoderData.UseVisualStyleBackColor = true;
            this.buttonSaveEncoderData.Click += new System.EventHandler(this.buttonSaveEncoderData_Click);
            // 
            // buttonStartListening
            // 
            this.buttonStartListening.Location = new System.Drawing.Point(30, 63);
            this.buttonStartListening.Name = "buttonStartListening";
            this.buttonStartListening.Size = new System.Drawing.Size(127, 79);
            this.buttonStartListening.TabIndex = 28;
            this.buttonStartListening.Text = "Start Listening";
            this.buttonStartListening.UseVisualStyleBackColor = true;
            this.buttonStartListening.Click += new System.EventHandler(this.buttonStartListening_Click);
            // 
            // serialPort
            // 
            this.serialPort.BaudRate = 115200;
            this.serialPort.PortName = "COM3";
            this.serialPort.ReadBufferSize = 409600;
            this.serialPort.RtsEnable = true;
            // 
            // zedGraphControlAcc
            // 
            this.zedGraphControlAcc.Location = new System.Drawing.Point(36, 17);
            this.zedGraphControlAcc.Name = "zedGraphControlAcc";
            this.zedGraphControlAcc.ScrollGrace = 0D;
            this.zedGraphControlAcc.ScrollMaxX = 0D;
            this.zedGraphControlAcc.ScrollMaxY = 0D;
            this.zedGraphControlAcc.ScrollMaxY2 = 0D;
            this.zedGraphControlAcc.ScrollMinX = 0D;
            this.zedGraphControlAcc.ScrollMinY = 0D;
            this.zedGraphControlAcc.ScrollMinY2 = 0D;
            this.zedGraphControlAcc.Size = new System.Drawing.Size(865, 276);
            this.zedGraphControlAcc.TabIndex = 29;
            // 
            // zedGraphControlGyro
            // 
            this.zedGraphControlGyro.Location = new System.Drawing.Point(36, 299);
            this.zedGraphControlGyro.Name = "zedGraphControlGyro";
            this.zedGraphControlGyro.ScrollGrace = 0D;
            this.zedGraphControlGyro.ScrollMaxX = 0D;
            this.zedGraphControlGyro.ScrollMaxY = 0D;
            this.zedGraphControlGyro.ScrollMaxY2 = 0D;
            this.zedGraphControlGyro.ScrollMinX = 0D;
            this.zedGraphControlGyro.ScrollMinY = 0D;
            this.zedGraphControlGyro.ScrollMinY2 = 0D;
            this.zedGraphControlGyro.Size = new System.Drawing.Size(865, 276);
            this.zedGraphControlGyro.TabIndex = 30;
            // 
            // zedGraphControlMag
            // 
            this.zedGraphControlMag.Location = new System.Drawing.Point(36, 581);
            this.zedGraphControlMag.Name = "zedGraphControlMag";
            this.zedGraphControlMag.ScrollGrace = 0D;
            this.zedGraphControlMag.ScrollMaxX = 0D;
            this.zedGraphControlMag.ScrollMaxY = 0D;
            this.zedGraphControlMag.ScrollMaxY2 = 0D;
            this.zedGraphControlMag.ScrollMinX = 0D;
            this.zedGraphControlMag.ScrollMinY = 0D;
            this.zedGraphControlMag.ScrollMinY2 = 0D;
            this.zedGraphControlMag.Size = new System.Drawing.Size(865, 276);
            this.zedGraphControlMag.TabIndex = 31;
            // 
            // tabControl1
            // 
            this.tabControl1.Controls.Add(this.tabPage1);
            this.tabControl1.Controls.Add(this.tabPage2);
            this.tabControl1.Controls.Add(this.tabPage3);
            this.tabControl1.Location = new System.Drawing.Point(652, 12);
            this.tabControl1.Name = "tabControl1";
            this.tabControl1.SelectedIndex = 0;
            this.tabControl1.Size = new System.Drawing.Size(925, 906);
            this.tabControl1.TabIndex = 32;
            // 
            // tabPage1
            // 
            this.tabPage1.Controls.Add(this.zedGraphControlAcc);
            this.tabPage1.Controls.Add(this.zedGraphControlMag);
            this.tabPage1.Controls.Add(this.zedGraphControlGyro);
            this.tabPage1.Location = new System.Drawing.Point(4, 22);
            this.tabPage1.Name = "tabPage1";
            this.tabPage1.Padding = new System.Windows.Forms.Padding(3);
            this.tabPage1.Size = new System.Drawing.Size(917, 880);
            this.tabPage1.TabIndex = 0;
            this.tabPage1.Text = "9DOF";
            this.tabPage1.UseVisualStyleBackColor = true;
            // 
            // tabPage2
            // 
            this.tabPage2.Controls.Add(this.zedGraphControlYaw);
            this.tabPage2.Controls.Add(this.zedGraphControlRoll);
            this.tabPage2.Controls.Add(this.zedGraphControlPitch);
            this.tabPage2.Location = new System.Drawing.Point(4, 22);
            this.tabPage2.Name = "tabPage2";
            this.tabPage2.Padding = new System.Windows.Forms.Padding(3);
            this.tabPage2.Size = new System.Drawing.Size(917, 880);
            this.tabPage2.TabIndex = 1;
            this.tabPage2.Text = "AHRS";
            this.tabPage2.UseVisualStyleBackColor = true;
            // 
            // zedGraphControlYaw
            // 
            this.zedGraphControlYaw.Location = new System.Drawing.Point(36, 581);
            this.zedGraphControlYaw.Name = "zedGraphControlYaw";
            this.zedGraphControlYaw.ScrollGrace = 0D;
            this.zedGraphControlYaw.ScrollMaxX = 0D;
            this.zedGraphControlYaw.ScrollMaxY = 0D;
            this.zedGraphControlYaw.ScrollMaxY2 = 0D;
            this.zedGraphControlYaw.ScrollMinX = 0D;
            this.zedGraphControlYaw.ScrollMinY = 0D;
            this.zedGraphControlYaw.ScrollMinY2 = 0D;
            this.zedGraphControlYaw.Size = new System.Drawing.Size(865, 276);
            this.zedGraphControlYaw.TabIndex = 32;
            // 
            // zedGraphControlRoll
            // 
            this.zedGraphControlRoll.Location = new System.Drawing.Point(36, 299);
            this.zedGraphControlRoll.Name = "zedGraphControlRoll";
            this.zedGraphControlRoll.ScrollGrace = 0D;
            this.zedGraphControlRoll.ScrollMaxX = 0D;
            this.zedGraphControlRoll.ScrollMaxY = 0D;
            this.zedGraphControlRoll.ScrollMaxY2 = 0D;
            this.zedGraphControlRoll.ScrollMinX = 0D;
            this.zedGraphControlRoll.ScrollMinY = 0D;
            this.zedGraphControlRoll.ScrollMinY2 = 0D;
            this.zedGraphControlRoll.Size = new System.Drawing.Size(865, 276);
            this.zedGraphControlRoll.TabIndex = 31;
            // 
            // zedGraphControlPitch
            // 
            this.zedGraphControlPitch.Location = new System.Drawing.Point(36, 17);
            this.zedGraphControlPitch.Name = "zedGraphControlPitch";
            this.zedGraphControlPitch.ScrollGrace = 0D;
            this.zedGraphControlPitch.ScrollMaxX = 0D;
            this.zedGraphControlPitch.ScrollMaxY = 0D;
            this.zedGraphControlPitch.ScrollMaxY2 = 0D;
            this.zedGraphControlPitch.ScrollMinX = 0D;
            this.zedGraphControlPitch.ScrollMinY = 0D;
            this.zedGraphControlPitch.ScrollMinY2 = 0D;
            this.zedGraphControlPitch.Size = new System.Drawing.Size(865, 276);
            this.zedGraphControlPitch.TabIndex = 30;
            // 
            // tabPage3
            // 
            this.tabPage3.Controls.Add(this.zedGraphControlCompass);
            this.tabPage3.Location = new System.Drawing.Point(4, 22);
            this.tabPage3.Name = "tabPage3";
            this.tabPage3.Size = new System.Drawing.Size(917, 880);
            this.tabPage3.TabIndex = 2;
            this.tabPage3.Text = "compass";
            this.tabPage3.UseVisualStyleBackColor = true;
            // 
            // zedGraphControlCompass
            // 
            this.zedGraphControlCompass.Location = new System.Drawing.Point(23, 29);
            this.zedGraphControlCompass.Name = "zedGraphControlCompass";
            this.zedGraphControlCompass.ScrollGrace = 0D;
            this.zedGraphControlCompass.ScrollMaxX = 0D;
            this.zedGraphControlCompass.ScrollMaxY = 0D;
            this.zedGraphControlCompass.ScrollMaxY2 = 0D;
            this.zedGraphControlCompass.ScrollMinX = 0D;
            this.zedGraphControlCompass.ScrollMinY = 0D;
            this.zedGraphControlCompass.ScrollMinY2 = 0D;
            this.zedGraphControlCompass.Size = new System.Drawing.Size(865, 276);
            this.zedGraphControlCompass.TabIndex = 31;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(23, 192);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(38, 13);
            this.label2.TabIndex = 35;
            this.label2.Text = "Acc(g)";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(125, 192);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(63, 13);
            this.label3.TabIndex = 36;
            this.label3.Text = "Gyro(deg/s)";
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(232, 192);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(62, 13);
            this.label4.TabIndex = 37;
            this.label4.Text = "Mag(guass)";
            // 
            // label5
            // 
            this.label5.AutoSize = true;
            this.label5.Location = new System.Drawing.Point(525, 192);
            this.label5.Name = "label5";
            this.label5.Size = new System.Drawing.Size(52, 13);
            this.label5.TabIndex = 40;
            this.label5.Text = "Yaw(deg)";
            // 
            // label6
            // 
            this.label6.AutoSize = true;
            this.label6.Location = new System.Drawing.Point(422, 192);
            this.label6.Name = "label6";
            this.label6.Size = new System.Drawing.Size(49, 13);
            this.label6.TabIndex = 39;
            this.label6.Text = "Roll(deg)";
            // 
            // label7
            // 
            this.label7.AutoSize = true;
            this.label7.Location = new System.Drawing.Point(317, 192);
            this.label7.Name = "label7";
            this.label7.Size = new System.Drawing.Size(55, 13);
            this.label7.TabIndex = 38;
            this.label7.Text = "Pitch(deg)";
            // 
            // labelAccx
            // 
            this.labelAccx.AutoSize = true;
            this.labelAccx.Location = new System.Drawing.Point(24, 221);
            this.labelAccx.Name = "labelAccx";
            this.labelAccx.Size = new System.Drawing.Size(13, 13);
            this.labelAccx.TabIndex = 41;
            this.labelAccx.Text = "..";
            // 
            // labelAccy
            // 
            this.labelAccy.AutoSize = true;
            this.labelAccy.Location = new System.Drawing.Point(24, 249);
            this.labelAccy.Name = "labelAccy";
            this.labelAccy.Size = new System.Drawing.Size(13, 13);
            this.labelAccy.TabIndex = 42;
            this.labelAccy.Text = "..";
            // 
            // labelAccz
            // 
            this.labelAccz.AutoSize = true;
            this.labelAccz.Location = new System.Drawing.Point(24, 277);
            this.labelAccz.Name = "labelAccz";
            this.labelAccz.Size = new System.Drawing.Size(13, 13);
            this.labelAccz.TabIndex = 43;
            this.labelAccz.Text = "..";
            // 
            // labelGryz
            // 
            this.labelGryz.AutoSize = true;
            this.labelGryz.Location = new System.Drawing.Point(125, 277);
            this.labelGryz.Name = "labelGryz";
            this.labelGryz.Size = new System.Drawing.Size(13, 13);
            this.labelGryz.TabIndex = 46;
            this.labelGryz.Text = "..";
            // 
            // labelGryy
            // 
            this.labelGryy.AutoSize = true;
            this.labelGryy.Location = new System.Drawing.Point(125, 249);
            this.labelGryy.Name = "labelGryy";
            this.labelGryy.Size = new System.Drawing.Size(13, 13);
            this.labelGryy.TabIndex = 45;
            this.labelGryy.Text = "..";
            // 
            // labelGryx
            // 
            this.labelGryx.AutoSize = true;
            this.labelGryx.Location = new System.Drawing.Point(125, 221);
            this.labelGryx.Name = "labelGryx";
            this.labelGryx.Size = new System.Drawing.Size(13, 13);
            this.labelGryx.TabIndex = 44;
            this.labelGryx.Text = "..";
            // 
            // labelMagz
            // 
            this.labelMagz.AutoSize = true;
            this.labelMagz.Location = new System.Drawing.Point(232, 277);
            this.labelMagz.Name = "labelMagz";
            this.labelMagz.Size = new System.Drawing.Size(13, 13);
            this.labelMagz.TabIndex = 49;
            this.labelMagz.Text = "..";
            // 
            // labelMagy
            // 
            this.labelMagy.AutoSize = true;
            this.labelMagy.Location = new System.Drawing.Point(232, 249);
            this.labelMagy.Name = "labelMagy";
            this.labelMagy.Size = new System.Drawing.Size(13, 13);
            this.labelMagy.TabIndex = 48;
            this.labelMagy.Text = "..";
            // 
            // labelMagx
            // 
            this.labelMagx.AutoSize = true;
            this.labelMagx.Location = new System.Drawing.Point(232, 221);
            this.labelMagx.Name = "labelMagx";
            this.labelMagx.Size = new System.Drawing.Size(13, 13);
            this.labelMagx.TabIndex = 47;
            this.labelMagx.Text = "..";
            // 
            // labelPitch
            // 
            this.labelPitch.AutoSize = true;
            this.labelPitch.Location = new System.Drawing.Point(317, 221);
            this.labelPitch.Name = "labelPitch";
            this.labelPitch.Size = new System.Drawing.Size(13, 13);
            this.labelPitch.TabIndex = 50;
            this.labelPitch.Text = "..";
            // 
            // labelRoll
            // 
            this.labelRoll.AutoSize = true;
            this.labelRoll.Location = new System.Drawing.Point(422, 221);
            this.labelRoll.Name = "labelRoll";
            this.labelRoll.Size = new System.Drawing.Size(13, 13);
            this.labelRoll.TabIndex = 51;
            this.labelRoll.Text = "..";
            // 
            // labelYaw
            // 
            this.labelYaw.AutoSize = true;
            this.labelYaw.Location = new System.Drawing.Point(525, 221);
            this.labelYaw.Name = "labelYaw";
            this.labelYaw.Size = new System.Drawing.Size(13, 13);
            this.labelYaw.TabIndex = 52;
            this.labelYaw.Text = "..";
            // 
            // pictureBoxRoll
            // 
            this.pictureBoxRoll.BackColor = System.Drawing.SystemColors.ButtonHighlight;
            this.pictureBoxRoll.Image = global::GUI_for_new_lazer_accelerameter_encoder.Properties.Resources.indicator11;
            this.pictureBoxRoll.Location = new System.Drawing.Point(310, 317);
            this.pictureBoxRoll.Name = "pictureBoxRoll";
            this.pictureBoxRoll.Size = new System.Drawing.Size(318, 292);
            this.pictureBoxRoll.SizeMode = System.Windows.Forms.PictureBoxSizeMode.CenterImage;
            this.pictureBoxRoll.TabIndex = 34;
            this.pictureBoxRoll.TabStop = false;
            // 
            // pictureBoxPitch
            // 
            this.pictureBoxPitch.BackColor = System.Drawing.SystemColors.ButtonHighlight;
            this.pictureBoxPitch.Image = global::GUI_for_new_lazer_accelerameter_encoder.Properties.Resources.indicator21;
            this.pictureBoxPitch.Location = new System.Drawing.Point(12, 317);
            this.pictureBoxPitch.Name = "pictureBoxPitch";
            this.pictureBoxPitch.Size = new System.Drawing.Size(302, 292);
            this.pictureBoxPitch.SizeMode = System.Windows.Forms.PictureBoxSizeMode.CenterImage;
            this.pictureBoxPitch.TabIndex = 33;
            this.pictureBoxPitch.TabStop = false;
            // 
            // trackBarAccPErr
            // 
            this.trackBarAccPErr.Location = new System.Drawing.Point(128, 671);
            this.trackBarAccPErr.Maximum = 100;
            this.trackBarAccPErr.Name = "trackBarAccPErr";
            this.trackBarAccPErr.Size = new System.Drawing.Size(186, 45);
            this.trackBarAccPErr.TabIndex = 53;
            this.trackBarAccPErr.Scroll += new System.EventHandler(this.trackBarAccPErr_Scroll);
            // 
            // trackBarQ
            // 
            this.trackBarQ.Location = new System.Drawing.Point(443, 714);
            this.trackBarQ.Name = "trackBarQ";
            this.trackBarQ.Size = new System.Drawing.Size(176, 45);
            this.trackBarQ.TabIndex = 54;
            this.trackBarQ.Scroll += new System.EventHandler(this.trackBarQ_Scroll);
            // 
            // trackBarAccRErr
            // 
            this.trackBarAccRErr.Location = new System.Drawing.Point(128, 738);
            this.trackBarAccRErr.Name = "trackBarAccRErr";
            this.trackBarAccRErr.Size = new System.Drawing.Size(186, 45);
            this.trackBarAccRErr.TabIndex = 55;
            this.trackBarAccRErr.Scroll += new System.EventHandler(this.trackBarAccRErr_Scroll);
            // 
            // trackBarGyroRErr
            // 
            this.trackBarGyroRErr.Location = new System.Drawing.Point(128, 869);
            this.trackBarGyroRErr.Name = "trackBarGyroRErr";
            this.trackBarGyroRErr.Size = new System.Drawing.Size(186, 45);
            this.trackBarGyroRErr.TabIndex = 57;
            this.trackBarGyroRErr.Scroll += new System.EventHandler(this.trackBarGyroRErr_Scroll);
            // 
            // trackBarGyroPErr
            // 
            this.trackBarGyroPErr.Location = new System.Drawing.Point(128, 803);
            this.trackBarGyroPErr.Name = "trackBarGyroPErr";
            this.trackBarGyroPErr.Size = new System.Drawing.Size(186, 45);
            this.trackBarGyroPErr.TabIndex = 56;
            this.trackBarGyroPErr.Scroll += new System.EventHandler(this.trackBarGyroPErr_Scroll);
            // 
            // label8
            // 
            this.label8.AutoSize = true;
            this.label8.Location = new System.Drawing.Point(12, 671);
            this.label8.Name = "label8";
            this.label8.Size = new System.Drawing.Size(46, 13);
            this.label8.TabIndex = 58;
            this.label8.Text = "AccPErr";
            // 
            // label9
            // 
            this.label9.AutoSize = true;
            this.label9.Location = new System.Drawing.Point(12, 738);
            this.label9.Name = "label9";
            this.label9.Size = new System.Drawing.Size(47, 13);
            this.label9.TabIndex = 59;
            this.label9.Text = "AccRErr";
            // 
            // label10
            // 
            this.label10.AutoSize = true;
            this.label10.Location = new System.Drawing.Point(12, 869);
            this.label10.Name = "label10";
            this.label10.Size = new System.Drawing.Size(50, 13);
            this.label10.TabIndex = 61;
            this.label10.Text = "GyroRErr";
            // 
            // label11
            // 
            this.label11.AutoSize = true;
            this.label11.Location = new System.Drawing.Point(15, 803);
            this.label11.Name = "label11";
            this.label11.Size = new System.Drawing.Size(49, 13);
            this.label11.TabIndex = 60;
            this.label11.Text = "GyroPErr";
            // 
            // labelGyroRErr
            // 
            this.labelGyroRErr.AutoSize = true;
            this.labelGyroRErr.Location = new System.Drawing.Point(68, 869);
            this.labelGyroRErr.Name = "labelGyroRErr";
            this.labelGyroRErr.Size = new System.Drawing.Size(13, 13);
            this.labelGyroRErr.TabIndex = 65;
            this.labelGyroRErr.Text = "..";
            // 
            // labelGyroPErr
            // 
            this.labelGyroPErr.AutoSize = true;
            this.labelGyroPErr.Location = new System.Drawing.Point(71, 803);
            this.labelGyroPErr.Name = "labelGyroPErr";
            this.labelGyroPErr.Size = new System.Drawing.Size(13, 13);
            this.labelGyroPErr.TabIndex = 64;
            this.labelGyroPErr.Text = "..";
            // 
            // labelAccRErr
            // 
            this.labelAccRErr.AutoSize = true;
            this.labelAccRErr.Location = new System.Drawing.Point(68, 738);
            this.labelAccRErr.Name = "labelAccRErr";
            this.labelAccRErr.Size = new System.Drawing.Size(13, 13);
            this.labelAccRErr.TabIndex = 63;
            this.labelAccRErr.Text = "..";
            // 
            // labelAccPErr
            // 
            this.labelAccPErr.AutoSize = true;
            this.labelAccPErr.Location = new System.Drawing.Point(68, 671);
            this.labelAccPErr.Name = "labelAccPErr";
            this.labelAccPErr.Size = new System.Drawing.Size(13, 13);
            this.labelAccPErr.TabIndex = 62;
            this.labelAccPErr.Text = "..";
            // 
            // label12
            // 
            this.label12.AutoSize = true;
            this.label12.Location = new System.Drawing.Point(440, 664);
            this.label12.Name = "label12";
            this.label12.Size = new System.Drawing.Size(15, 13);
            this.label12.TabIndex = 74;
            this.label12.Text = "Q";
            // 
            // labelQ
            // 
            this.labelQ.AutoSize = true;
            this.labelQ.Location = new System.Drawing.Point(478, 664);
            this.labelQ.Name = "labelQ";
            this.labelQ.Size = new System.Drawing.Size(13, 13);
            this.labelQ.TabIndex = 75;
            this.labelQ.Text = "..";
            // 
            // button_calibrate_gyro
            // 
            this.button_calibrate_gyro.Location = new System.Drawing.Point(360, 63);
            this.button_calibrate_gyro.Name = "button_calibrate_gyro";
            this.button_calibrate_gyro.Size = new System.Drawing.Size(111, 46);
            this.button_calibrate_gyro.TabIndex = 76;
            this.button_calibrate_gyro.Text = "Calibrate Gyro";
            this.button_calibrate_gyro.UseVisualStyleBackColor = true;
            this.button_calibrate_gyro.Click += new System.EventHandler(this.button_calibrate_gyro_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(96F, 96F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Dpi;
            this.AutoSize = true;
            this.ClientSize = new System.Drawing.Size(1589, 882);
            this.Controls.Add(this.button_calibrate_gyro);
            this.Controls.Add(this.labelQ);
            this.Controls.Add(this.label12);
            this.Controls.Add(this.labelGyroRErr);
            this.Controls.Add(this.labelGyroPErr);
            this.Controls.Add(this.labelAccRErr);
            this.Controls.Add(this.labelAccPErr);
            this.Controls.Add(this.label10);
            this.Controls.Add(this.label11);
            this.Controls.Add(this.label9);
            this.Controls.Add(this.label8);
            this.Controls.Add(this.trackBarGyroRErr);
            this.Controls.Add(this.trackBarGyroPErr);
            this.Controls.Add(this.trackBarAccRErr);
            this.Controls.Add(this.trackBarQ);
            this.Controls.Add(this.trackBarAccPErr);
            this.Controls.Add(this.labelYaw);
            this.Controls.Add(this.labelRoll);
            this.Controls.Add(this.labelPitch);
            this.Controls.Add(this.labelMagz);
            this.Controls.Add(this.labelMagy);
            this.Controls.Add(this.labelMagx);
            this.Controls.Add(this.labelGryz);
            this.Controls.Add(this.labelGryy);
            this.Controls.Add(this.labelGryx);
            this.Controls.Add(this.labelAccz);
            this.Controls.Add(this.labelAccy);
            this.Controls.Add(this.labelAccx);
            this.Controls.Add(this.label5);
            this.Controls.Add(this.label6);
            this.Controls.Add(this.label7);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.pictureBoxRoll);
            this.Controls.Add(this.pictureBoxPitch);
            this.Controls.Add(this.tabControl1);
            this.Controls.Add(this.buttonStartListening);
            this.Controls.Add(this.buttonSaveEncoderData);
            this.Controls.Add(this.buttonStopStreaming);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.checkBoxEncoder);
            this.Controls.Add(this.comboBoxEncoderPortName);
            this.Controls.Add(this.labelAvailablePorts);
            this.Controls.Add(this.buttonCheckPorts);
            this.Name = "Form1";
            this.StartPosition = System.Windows.Forms.FormStartPosition.Manual;
            this.Text = "Form1";
            this.FormClosed += new System.Windows.Forms.FormClosedEventHandler(this.Form1_FormClosed);
            this.tabControl1.ResumeLayout(false);
            this.tabPage1.ResumeLayout(false);
            this.tabPage2.ResumeLayout(false);
            this.tabPage3.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.pictureBoxRoll)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.pictureBoxPitch)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarAccPErr)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarQ)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarAccRErr)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarGyroRErr)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.trackBarGyroPErr)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button buttonCheckPorts;
        private System.Windows.Forms.Label labelAvailablePorts;
        private System.Windows.Forms.ComboBox comboBoxEncoderPortName;
        private System.Windows.Forms.CheckBox checkBoxEncoder;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Button buttonStopStreaming;
        private System.Windows.Forms.SaveFileDialog saveFileDialog;
        private System.Windows.Forms.Button buttonSaveEncoderData;
        private System.Windows.Forms.Button buttonStartListening;
        private System.IO.Ports.SerialPort serialPort;
        private ZedGraph.ZedGraphControl zedGraphControlAcc;
        private System.ComponentModel.BackgroundWorker backgroundWorker1;
        private ZedGraph.ZedGraphControl zedGraphControlGyro;
        private ZedGraph.ZedGraphControl zedGraphControlMag;
        private System.Windows.Forms.TabControl tabControl1;
        private System.Windows.Forms.TabPage tabPage1;
        private System.Windows.Forms.TabPage tabPage2;
        private ZedGraph.ZedGraphControl zedGraphControlYaw;
        private ZedGraph.ZedGraphControl zedGraphControlRoll;
        private ZedGraph.ZedGraphControl zedGraphControlPitch;
        private System.Windows.Forms.PictureBox pictureBoxPitch;
        private System.Windows.Forms.PictureBox pictureBoxRoll;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.Label label7;
        private System.Windows.Forms.Label labelAccx;
        private System.Windows.Forms.Label labelAccy;
        private System.Windows.Forms.Label labelAccz;
        private System.Windows.Forms.Label labelGryz;
        private System.Windows.Forms.Label labelGryy;
        private System.Windows.Forms.Label labelGryx;
        private System.Windows.Forms.Label labelMagz;
        private System.Windows.Forms.Label labelMagy;
        private System.Windows.Forms.Label labelMagx;
        private System.Windows.Forms.Label labelPitch;
        private System.Windows.Forms.Label labelRoll;
        private System.Windows.Forms.Label labelYaw;
        private System.Windows.Forms.TrackBar trackBarAccPErr;
        private System.Windows.Forms.TrackBar trackBarQ;
        private System.Windows.Forms.TrackBar trackBarAccRErr;
        private System.Windows.Forms.TrackBar trackBarGyroRErr;
        private System.Windows.Forms.TrackBar trackBarGyroPErr;
        private System.Windows.Forms.Label label8;
        private System.Windows.Forms.Label label9;
        private System.Windows.Forms.Label label10;
        private System.Windows.Forms.Label label11;
        private System.Windows.Forms.Label labelGyroRErr;
        private System.Windows.Forms.Label labelGyroPErr;
        private System.Windows.Forms.Label labelAccRErr;
        private System.Windows.Forms.Label labelAccPErr;
        private System.Windows.Forms.Label label12;
        private System.Windows.Forms.Label labelQ;
        private System.Windows.Forms.TabPage tabPage3;
        private ZedGraph.ZedGraphControl zedGraphControlCompass;
        private System.Windows.Forms.Button button_calibrate_gyro;
    }
}

