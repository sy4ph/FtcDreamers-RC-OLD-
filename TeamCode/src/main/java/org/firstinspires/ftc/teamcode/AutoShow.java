package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(name = "Show")
public class AutoShow  extends LinearOpMode {

    private static final int CAMERA_WIDTH  = 1280; // width  of wanted camera resolution
    private static final int CAMERA_HEIGHT = 720; // height of wanted camera resolution
    public OpenCvCamera webcam;
    public boolean fn = false;
    StandartConfig robot = new StandartConfig();
    FtcDashboard dashboard = FtcDashboard.getInstance();
    public void CheckFn(int motor) {
        //1 - FL 2 - FR 3 - BL 4 - BR 5 -H 6 - All
        if (motor == 5) {
            fn = (robot.motorHand.getCurrentPosition() >= robot.motorHand.getCurrentPosition() - 50) & (robot.motorHand.getCurrentPosition() <= robot.motorHand.getTargetPosition() - 50);
        }
        if (motor == 1) {
            fn = (robot.motorFrontLeft.getCurrentPosition() >= robot.motorFrontLeft.getCurrentPosition() - 50) & (robot.motorFrontLeft.getCurrentPosition() <= robot.motorFrontLeft.getTargetPosition() - 50);
        }
        if (motor == 2) {
            fn = (robot.motorFrontRight.getCurrentPosition() >= robot.motorFrontRight.getCurrentPosition() - 50) & (robot.motorFrontRight.getCurrentPosition() <= robot.motorFrontRight.getTargetPosition() - 50);
        }
        if (motor == 3) {
            fn = (robot.motorBackLeft.getCurrentPosition() >= robot.motorBackLeft.getCurrentPosition() - 50) & (robot.motorBackLeft.getCurrentPosition() <= robot.motorBackLeft.getTargetPosition() - 50);
        }
        if (motor == 4) {
            fn = (robot.motorBackRight.getCurrentPosition() >= robot.motorBackRight.getCurrentPosition() - 50) & (robot.motorBackRight.getCurrentPosition() <= robot.motorBackRight.getTargetPosition() - 50);
        }
        if (motor == 6) {
            fn = ((robot.motorHand.getCurrentPosition() >= robot.motorHand.getCurrentPosition() - 50) & (robot.motorHand.getCurrentPosition() <= robot.motorHand.getTargetPosition() - 50)) & ((robot.motorFrontRight.getCurrentPosition() >= robot.motorFrontRight.getCurrentPosition() - 50) & (robot.motorFrontRight.getCurrentPosition() <= robot.motorFrontRight.getTargetPosition() - 50)) & ((robot.motorBackLeft.getCurrentPosition() >= robot.motorBackLeft.getCurrentPosition() - 50) & (robot.motorBackLeft.getCurrentPosition() <= robot.motorBackLeft.getTargetPosition() - 50)) & ((robot.motorBackRight.getCurrentPosition() >= robot.motorBackRight.getCurrentPosition() - 50) & (robot.motorBackRight.getCurrentPosition() <= robot.motorBackRight.getTargetPosition() - 50));
        }
    }
    public void runOpMode() {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);
        robot.initAuto(hardwareMap);

        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

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
        // Only if you are using ftcdashboard
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
        FtcDashboard.getInstance().startCameraStream(webcam, 10);

        robot.motorBackLeft.setPower(1.);
        robot.motorBackRight.setPower(1.);
        robot.motorFrontRight.setPower(1.);
        robot.motorFrontLeft.setPower(1.);

        waitForStart();
        telemetry.addData("Finished?",fn);

        robot.motorHand.setPower(0.25);
        robot.motorHand.setTargetPosition(145*4);
        while (!fn) {
            CheckFn(5);
            telemetry.update();
        }
        fn = false;
        robot.motorsSet(1500,1500,1500,1500);
        while (!fn) {
            CheckFn(6);
            telemetry.update();
        }
        fn = false;
    }
}
