package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.opencv.core.Scalar;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.openftc.easyopencv.OpenCvInternalCamera2;

@Config //Disable if not using FTC Dashboard https://github.com/PinkToTheFuture/OpenCV_FreightFrenzy_2021-2022#opencv_freightfrenzy_2021-2022
@Autonomous(name="OpenCV Basic Blue", group="Tutorials")

public class OpenCV_Basic_Blue extends LinearOpMode {
    private OpenCvCamera webcam;

    StandartConfig robot = new StandartConfig();

    private static final String VUFORIA_KEY =
            "AYL2JCv/////AAABmZtbvIR1OE4EqwDEQrfdLGEVkaFVdZIeCT0/sj+w0GU45fXBMPeDexxY5oTxkgBTlBnW/L8pr4FRst8Nzvlv8WesJ6LkyXmChZFQTksvvJ75aNUx3+JBzuLL17ROiklPQGJNter6Hn6gOqmtJ45r4E/68D1DLL7dQThGhRVp/ity8XPM1h++Vl444xTo/P7QaXYKcbYqk2GoHDGnA8/uPqUKAhCo7oVAoD/74qRCYrCC6IeupYBF7SAiZ+RMWOH3UbShX14jbN+QNWgUGVm/8G21FYpFtjJbrnXm75SCd/mrhsXIg8DE1SInUR7VpZKQBTqZx5PTXiLP+T+i3+NdSqGdcwXeUKx74ap4eYwiQHSs";


    private static final int CAMERA_WIDTH  = 1280; // width  of wanted camera resolution
    private static final int CAMERA_HEIGHT = 720; // height of wanted camera resolution

    private double CrLowerUpdate = 160;
    private double CbLowerUpdate = 100;
    private double CrUpperUpdate = 255;
    private double CbUpperUpdate = 255;

    public boolean done = false;

    public static double borderLeftX    = 0.0;   //fraction of pixels from the left side of the cam to skip
    public static double borderRightX   = 0.0;   //fraction of pixels from the right of the cam to skip
    public static double borderTopY     = 0.0;   //fraction of pixels from the top of the cam to skip
    public static double borderBottomY  = 0.0;   //fraction of pixels from the bottom of the cam to skip

    private double lowerruntime = 0;
    private double upperruntime = 0;

    // Green Range                                      Y      Cr     Cb
    public static Scalar scalarLowerYCrCb = new Scalar(  0.0, 0.0, 0.0);
    public static Scalar scalarUpperYCrCb = new Scalar(255.0, 120.0, 120.0);

    // Yellow Range
//    public static Scalar scalarLowerYCrCb = new Scalar(0.0, 100.0, 0.0);
////    public static Scalar scalarUpperYCrCb = new Scalar(255.0, 170.0, 120.0);

    @Override
    public void runOpMode() throws InterruptedException
    {
        robot.initAuto(hardwareMap);

        robot.motorBackLeft.setPower(1.);
        robot.motorBackRight.setPower(1.);
        robot.motorFrontRight.setPower(1.);
        robot.motorFrontLeft.setPower(1.);

        // OpenCV webcam
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createInternalCamera(OpenCvInternalCamera.CameraDirection.BACK,cameraMonitorViewId);
        //OpenCV Pipeline
        ContourPipeline myPipeline;
        webcam.setPipeline(myPipeline = new ContourPipeline(borderLeftX,borderRightX,borderTopY,borderBottomY));
        // Configuration of Pipeline
        myPipeline.configureScalarLower(scalarLowerYCrCb.val[0],scalarLowerYCrCb.val[1],scalarLowerYCrCb.val[2]);
        myPipeline.configureScalarUpper(scalarUpperYCrCb.val[0],scalarUpperYCrCb.val[1],scalarUpperYCrCb.val[2]);
        // Webcam Streaming
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()

        {
            @Override
            public void onOpened()
            {
                webcam.startStreaming(CAMERA_WIDTH, CAMERA_HEIGHT, OpenCvCameraRotation.SIDEWAYS_LEFT);
            }

            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });

        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
        telemetry.update();
        waitForStart();

        robot.start_reset();

        while (opModeIsActive())
        {
            myPipeline.configureBorders(borderLeftX, borderRightX, borderTopY, borderBottomY);
            if(myPipeline.error){
                telemetry.addData("Exception: ", myPipeline.debug);
            }
            // Only use this line of the code when you want to find the lower and upper values
            //testing(myPipeline);

            telemetry.addData("RectArea: ", myPipeline.getRectArea());
            telemetry.update();

            if(myPipeline.getRectArea() > 2000){
                if(myPipeline.getRectMidpointX() > 900){
                    AUTONOMOUS_C();
                }
                else if(myPipeline.getRectMidpointX() > 400){
                    AUTONOMOUS_B();
                }
                else {
                    AUTONOMOUS_A();
                }
            }
        }
    }
    public void testing(ContourPipeline myPipeline){
        if(lowerruntime + 0.05 < getRuntime()){
            CrLowerUpdate += -gamepad1.left_stick_y;
            CbLowerUpdate += gamepad1.left_stick_x;
            lowerruntime = getRuntime();
        }
        if(upperruntime + 0.05 < getRuntime()){
            CrUpperUpdate += -gamepad1.right_stick_y;
            CbUpperUpdate += gamepad1.right_stick_x;
            upperruntime = getRuntime();
        }

        CrLowerUpdate = inValues(CrLowerUpdate, 0, 255);
        CrUpperUpdate = inValues(CrUpperUpdate, 0, 255);
        CbLowerUpdate = inValues(CbLowerUpdate, 0, 255);
        CbUpperUpdate = inValues(CbUpperUpdate, 0, 255);

        myPipeline.configureScalarLower(0.0, CrLowerUpdate, CbLowerUpdate);
        myPipeline.configureScalarUpper(255.0, CrUpperUpdate, CbUpperUpdate);

        telemetry.addData("lowerCr ", (int)CrLowerUpdate);
        telemetry.addData("lowerCb ", (int)CbLowerUpdate);
        telemetry.addData("UpperCr ", (int)CrUpperUpdate);
        telemetry.addData("UpperCb ", (int)CbUpperUpdate);
    }
    public Double inValues(double value, double min, double max){
        if(value < min){ value = min; }
        if(value > max){ value = max; }
        return value;
    }
    public void AUTONOMOUS_A(){
        telemetry.addLine("Autonomous A");
        if (done = false) {
            robot.motorsSet(1400,-1400,1400,-1400);
            sleep(1000);
            done = true;
            robot.motorsSet(2800, 2800, 2800, 2800);
            sleep(1500);

            robot.motorsSet(800, -800, 800, -800);
            sleep(1500);

            robot.motorHand.setPower(0.25);
            robot.motorHand.setTargetPosition(280*2);
            sleep(650);

            robot.servoVal.setPower(-0.3);
            sleep(1500);

            robot.servoVal.setPower(0.);

            robot.motorsSet(-2400, 2400, -2400, 2400);
            robot.motorHand.setTargetPosition(50*4);
            sleep(1500);

            robot.motorsSet(7800, 7800, 7800, 7800);
        }
    }
    public void AUTONOMOUS_B(){
        if (done = false) {
            telemetry.addLine("Autonomous B");
            done = true;
            robot.motorsSet(1400,-1400,1400,-1400);
            sleep(1000);

            robot.motorsSet(2800, 2800, 2800, 2800);
            sleep(1500);

            robot.motorsSet(800, -800, 800, -800);
            sleep(1500);

            robot.motorHand.setPower(0.25);
            robot.motorHand.setTargetPosition(77 * 4);
            sleep(650);

            robot.servoVal.setPower(-0.3);
            sleep(1500);

            robot.servoVal.setPower(0.);

            robot.motorsSet(-2400, 2400, -2400, 2400);
            robot.motorHand.setTargetPosition(77 * 4);
            sleep(1500);

            robot.motorsSet(7800, 7800, 7800, 7800);

            done = true;
        }
    }
    public void AUTONOMOUS_C(){
        if (done = false) {
            done = true;
            telemetry.addLine("Autonomous C");

            robot.motorsSet(1400,-1400,1400,-1400);
            sleep(1000);

            robot.motorsSet(2800, 2800, 2800, 2800);
            sleep(1500);

            robot.motorsSet(800, -800, 800, -800);
            sleep(1500);

            robot.motorHand.setPower(0.25);
            robot.motorHand.setTargetPosition(35 * 4);
            sleep(650);

            robot.servoVal.setPower(-0.3);
            sleep(1500);

            robot.servoVal.setPower(0.);

            robot.motorsSet(-2400, 2400, -2400, 2400);
            robot.motorHand.setTargetPosition(50 * 4);
            sleep(1500);

            robot.motorsSet(7800, 7800, 7800, 7800);

            done = true;
        }
    }
}
