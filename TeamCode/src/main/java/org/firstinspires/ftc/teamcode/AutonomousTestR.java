package org.firstinspires.ftc.teamcode;

/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "AutoTestR", group = "Autonomous")
public class AutonomousTestR extends LinearOpMode {

    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }

    public void ResetEncoders() {
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }

    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_DM.tflite";
    private static final String[] LABELS = {
            "Duck",
            "Marker"
    };

    private static final String VUFORIA_KEY =
            "AYL2JCv/////AAABmZtbvIR1OE4EqwDEQrfdLGEVkaFVdZIeCT0/sj+w0GU45fXBMPeDexxY5oTxkgBTlBnW/L8pr4FRst8Nzvlv8WesJ6LkyXmChZFQTksvvJ75aNUx3+JBzuLL17ROiklPQGJNter6Hn6gOqmtJ45r4E/68D1DLL7dQThGhRVp/ity8XPM1h++Vl444xTo/P7QaXYKcbYqk2GoHDGnA8/uPqUKAhCo7oVAoD/74qRCYrCC6IeupYBF7SAiZ+RMWOH3UbShX14jbN+QNWgUGVm/8G21FYpFtjJbrnXm75SCd/mrhsXIg8DE1SInUR7VpZKQBTqZx5PTXiLP+T+i3+NdSqGdcwXeUKx74ap4eYwiQHSs";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;
    private int duckPosition = 1;
    private double duckRight = 0;
    private double duckLeft = 0;

    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor motorFrontLeft = null;
    private DcMotor motorBackLeft = null;
    private DcMotor motorFrontRight = null;
    private DcMotor motorBackRight = null;
    private DcMotor motorHand = null;

    public int FLpos = 0;
    public int FRpos = 0;
    public int BLpos = 0;
    public int BRpos = 0;

    private CRServo servoVal = null;
    public void waitTillOver() {
        while ((motorFrontRight.getCurrentPosition() > motorFrontRight.getTargetPosition()-10) & (motorFrontRight.getCurrentPosition() < motorFrontRight.getTargetPosition()+10) & (motorBackRight.getCurrentPosition() > motorBackRight.getTargetPosition()-10) & (motorBackRight.getCurrentPosition() < motorBackRight.getTargetPosition()+10) & (motorFrontLeft.getCurrentPosition() > motorFrontLeft.getTargetPosition()-10) & (motorFrontLeft.getCurrentPosition() < motorFrontLeft.getTargetPosition()+10) & (motorBackLeft.getCurrentPosition() > motorBackLeft.getTargetPosition()-10) & (motorBackLeft.getCurrentPosition() < motorBackLeft.getTargetPosition()+10)) {
            sleep(10);
        }
        sleep(100);
    }

    public  void motorsSet(int BLplus, int BRplus, int FLplus,int FRplus) {
        BRpos = BRpos + BRplus;
        motorBackRight.setTargetPosition(BRpos);
        BLpos = BLpos + BLplus;
        motorBackLeft.setTargetPosition(BLpos);
        FLpos = FLpos + FLplus;
        motorFrontLeft.setTargetPosition(FLpos);
        FRpos = FRpos + FRplus;
        motorFrontRight.setTargetPosition(FRpos);
    }
    @Override
    public void runOpMode() {
        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            tfod.setZoom(2.5, 16.0/9.0);
        }

        motorFrontLeft = hardwareMap.get(DcMotor.class, "motorFrontLeft");
        motorBackLeft = hardwareMap.get(DcMotor.class, "motorBackLeft");
        motorFrontRight = hardwareMap.get(DcMotor.class, "motorFrontRight");
        motorBackRight = hardwareMap.get(DcMotor.class, "motorBackRight");
        motorHand = hardwareMap.get(DcMotor.class,"motorHand");
        servoVal = hardwareMap.get(CRServo.class, "servoVal");

        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.FORWARD);
        motorBackRight.setDirection(DcMotor.Direction.FORWARD);
        motorHand.setDirection(DcMotorSimple.Direction.REVERSE);
        servoVal.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();
        runtime.reset();

//         Autonomous part setup
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setTargetPosition(0);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setTargetPosition(0);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setTargetPosition(0);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setTargetPosition(0);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorHand.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorHand.setTargetPosition(0);
        motorHand.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorBackRight.setPower(1.);
        motorBackLeft.setPower(1.);
        motorFrontLeft.setPower(1.);
        motorFrontRight.setPower(1.);



        if (tfod != null) {
            List<Recognition> recognitions = tfod.getUpdatedRecognitions();
            if (recognitions == null) {
                double timeStamp = runtime.seconds();
                // if recognitions are empty try to getUpdatedRecognitions for 5 seconds
                while (((runtime.seconds() - timeStamp) < 5.) & (recognitions == null)) {
                    recognitions = tfod.getUpdatedRecognitions();
                }
            }

            for (Recognition recognition : recognitions) {
                if (recognition.getLabel() == "Duck") {
                    duckRight = recognition.getBottom();
                    duckLeft = recognition.getLeft();
                }
            }

            if (recognitions != null) {
                if (recognitions.size() == 3) {
                    for (Recognition recognition : recognitions) {
                        if ((recognition.getLabel() != "Duck")&(recognition.getBottom() < duckLeft)) {
                            duckPosition++;
                        }
                    }
                }
            }
            else {
                duckPosition = 3;
            }
            telemetry.addData("duckPosition", duckPosition);
        }
// movement starts here

        //BL/BR/FL/FR
        motorsSet(2800,2800,2800,2800);
        sleep(1500);


        motorsSet(0,1300,0,1300);
        sleep(1500);
        motorHand.setPower(0.25);
        motorHand.setTargetPosition(285);
        sleep(650);
        servoVal.setPower(-1.);
        sleep(1500);
        servoVal.setPower(0.);

        motorsSet(-2800,2800,-2800,2800);
        sleep(1500);
        motorsSet(5800,5800,5800,5800);

        while (opModeIsActive()) {
            telemetry.addData("runtime",runtime.toString());
        }
    }
}