package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * ###CURRENTLY DISABLED
 *
 * This class is an example of a TFOD classification supported
 * autonomous mode modified for our team's use. It will be used later on
 * upon installing camera to the robot.
 *
 * This class has an algorithm of automatical recognition of a target
 * position for pre-load element installment.
 */

@Autonomous(name = "TFODExample", group = "Autonomous")
@Disabled
public class TFODClassificationExample extends LinearOpMode {

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

    public double FLpos = 0;
    public double FRpos = 0;
    public double BLpos = 0;
    public double BRpos = 0;

    StandartConfig robot = new StandartConfig();

    /**
     * Vuforia Engine initialization function
     */
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

    /**
     * Function that sets target position for motors
     *
     * @param BLplus Value for rear left motor; 1 full round = 1440 ticks
     * @param BRplus Value for rear right motor;
     * @param FLplus Value for front left motor;
     * @param FRplus Value for front right motor;
     */
    public void motorsSet(double BLplus, double BRplus, double FLplus, double FRplus) {
        BRpos = BRpos + BRplus;
        robot.motorBackRight.setTargetPosition((int) (BRpos / 2.678));
        BLpos = BLpos + BLplus;
        robot.motorBackLeft.setTargetPosition((int) (BLpos / 2.678));
        FLpos = FLpos + FLplus;
        robot.motorFrontLeft.setTargetPosition((int) (FLpos / 2.678));
        FRpos = FRpos + FRplus;
        robot.motorFrontRight.setTargetPosition((int) (FRpos / 2.678));
    }

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
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

        waitForStart();
        runtime.reset();

//         Autonomous part setup
        robot.motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorFrontLeft.setTargetPosition(0);
        robot.motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorBackLeft.setTargetPosition(0);
        robot.motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorFrontRight.setTargetPosition(0);
        robot.motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.motorBackRight.setTargetPosition(0);
        robot.motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        robot.motorBackRight.setPower(1.);
        robot.motorBackLeft.setPower(1.);
        robot.motorFrontLeft.setPower(1.);
        robot.motorFrontRight.setPower(1.);



//         The following algorithm uses tfod model for a rubber duck
//         recognition as a team element. It sets the duckPosition parameter
//         to indicate needed Alliance Hub level to drop the pre-load
//
//         The model has 5 seconds to recognize the duck and if doesn't, it sets
//         the default drop level to 3 (the highest)
        if (tfod != null) {
            List<Recognition> recognitions = tfod.getUpdatedRecognitions();
            if (recognitions == null) {
                double timeStamp = runtime.seconds();
                // if recognitions are empty try to getUpdatedRecognitions for 5 seconds
                while (((runtime.seconds() - timeStamp) < 5.) & (recognitions == null)) {
                    recognitions = tfod.getUpdatedRecognitions();
                }
            }

            if (recognitions != null) {
                for (Recognition recognition : recognitions) {
                    if (recognition.getLabel().equals("Duck")) {
                        duckRight = recognition.getBottom();
                        duckLeft = recognition.getLeft();
                    }
                }
            }

            if (recognitions != null) {
                if (recognitions.size() == 3) {
                    for (Recognition recognition : recognitions) {
                        if (!(recognition.getLabel().equals("Duck"))&(recognition.getBottom() < duckLeft)) {
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

        // Movement starts here
        // All the values are low-key totally random nad probably won't work at all
        //BL/BR/FL/FR
        motorsSet(2500,-2500,-2500,2500);
        sleep(1500);


        motorsSet(2800,2800,2800,2800);
        sleep(1500);

        robot.motorHand.setPower(0.25);
        robot.motorHand.setTargetPosition(285);
        sleep(800);
        robot.servoVal.setPower(-1.);
        sleep(2500);
        robot.servoVal.setPower(0.);

        motorsSet(2200,-2200,2200,-2200);
        sleep(1500);
        motorsSet(5800,5800,5800,5800);


        while (opModeIsActive()) {
            telemetry.addData("runtime",runtime.toString());
        }
    }
}
