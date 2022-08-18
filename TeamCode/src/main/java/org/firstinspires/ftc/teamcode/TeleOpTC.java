package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Config
@TeleOp(name = "Standard Configuration OpMode", group = "Linear Opmode")
public class TeleOpTC extends OpMode {
    private static final int CAMERA_WIDTH  = 640; // width  of wanted camera resolution
    private static final int CAMERA_HEIGHT = 360; // height of wanted camera resolution
    public static double MULTIPLIER = 1.0;
    public OpenCvCamera webcam;
    FtcDashboard dashboard = FtcDashboard.getInstance();

    BNO055IMU imu;
    Orientation Angles = new Orientation();

    private final ElapsedTime runtime = new ElapsedTime();
    // Init standard configuration from StandardConfig class
    StandartConfig robot = new StandartConfig();
    private boolean changesMade = false;

    public void init() {
        robot.initTele(hardwareMap);
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled = false;

        imu = hardwareMap.get(BNO055IMU.class, "imu");

        imu.initialize(parameters);
    }

    public void start() {
        runtime.reset();
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(hardwareMap.get(WebcamName.class, "Webcam 1"), cameraMonitorViewId);

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
        FtcDashboard.getInstance().startCameraStream(webcam, 30);

        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
//        robot.motorHand.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        robot.motorHand.setPower(-0.3);
//        try {
//            sleep(1500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        robot.motorHand.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        robot.motorHand.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void loop() {
        if (!gamepad1.left_bumper) {
            if (gamepad1.right_trigger != 0) {
                robot.servoVal.setPower(gamepad1.right_trigger);
            }
            if (gamepad1.left_trigger != 0) {
                robot.servoVal.setPower(-gamepad1.left_trigger);
            }
            if ((gamepad1.right_trigger == 0) & (gamepad1.left_trigger == 0)) {
                robot.servoVal.setPower(0.);
            }
        }
        if (gamepad1.left_bumper) {
            robot.servoVal.setPower(0.);
            if (gamepad1.right_trigger != 0) {
                robot.servoUtki.setPower(gamepad1.right_trigger);
            }
            if (gamepad1.left_trigger != 0) {
                robot.servoUtki.setPower(-gamepad1.left_trigger);
            }
            if ((gamepad1.right_trigger == 0) & (gamepad1.left_trigger == 0)) {
                robot.servoUtki.setPower(0.);
            }
        }
        if (gamepad1.a) {
            robot.motorHand.setTargetPosition(80);
            robot.motorHand.setPower(0.1);
        }
        if (gamepad1.b) {
            robot.motorHand.setTargetPosition(85*4); //-20
            robot.motorHand.setPower(0.15);
        }
        if (gamepad1.y) {
            if (gamepad1.left_bumper) {
                robot.motorHand.setTargetPosition(225*4);
            }
            robot.motorHand.setTargetPosition(158*4); //-15
            robot.motorHand.setPower(0.11);
        }
        if (gamepad1.right_bumper) {
            robot.servoVal.setPower(0.);
        }
        if ((gamepad1.dpad_up) & (changesMade)) {
            robot.motorHand.setTargetPosition(robot.motorHand.getTargetPosition() + 12);
            robot.motorHand.setPower(0.5);
            changesMade = false;
        }
        if ((gamepad1.dpad_down) & (changesMade)) {
            robot.motorHand.setTargetPosition(robot.motorHand.getTargetPosition() - 12);
            robot.motorHand.setPower(0.5);
            changesMade = false;
        }
        if ((!gamepad1.dpad_down) & (!gamepad1.dpad_up)) {
            changesMade = true;
        }
        if ((gamepad1.left_bumper) & (gamepad1.a)) {
            robot.motorHand.setPower(0.);
            try {
                sleep(900);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.motorHand.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.motorHand.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        }

        double max;
/**
 * I have no idea how this stuff works. It just does.
 */
//todo 28/02/22 please rewrite this
        // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
        double axial = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
        double lateral = gamepad1.left_stick_x;
        double yaw = gamepad1.right_stick_x;

        // Combine the joystick requests for each axis-motion to determine each wheel's power.
        // Set up a variable for each drive wheel to save the power level for telemetry.
        double leftFrontPower = axial + lateral + yaw;
        double rightFrontPower = axial - lateral - yaw;
        double leftBackPower = axial - lateral + yaw;
        double rightBackPower = axial + lateral - yaw;

        // Normalize the values so no wheel power exceeds 100%
        // This ensures that the robot maintains the desired motion.
        max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }
        robot.motorFrontLeft.setPower(leftFrontPower*MULTIPLIER);
        robot.motorFrontRight.setPower(rightFrontPower*MULTIPLIER);
        robot.motorBackLeft.setPower(leftBackPower*MULTIPLIER);
        robot.motorBackRight.setPower(rightBackPower*MULTIPLIER);

//        robot.motorFrontLeft.setPower(leftFrontPower*0.5);
//        robot.motorFrontRight.setPower(rightFrontPower*0.5);
//        robot.motorBackLeft.setPower(leftBackPower*0.5);
//        robot.motorBackRight.setPower(rightBackPower*0.5);

        // Telemetry of hand DCMotor and runtime.
        telemetry.addData("Status", "Run Time: " + runtime);
        telemetry.addData("TargetPos", robot.motorHand.getTargetPosition());
        telemetry.addData("CurrentPos", robot.motorHand.getCurrentPosition());
        telemetry.addData("Power (LeftFront)",leftFrontPower);
        telemetry.addData("Power (LeftBack)",leftBackPower);
        telemetry.addData("Power (RightBack)",rightBackPower);
        telemetry.addData("Power (RightFront)",rightFrontPower);
        telemetry.update();

    }

    public void stop() {
        // Move hand to 0 position
        robot.motorHand.setPower(0.1);
        robot.motorHand.setTargetPosition(0);
    }
}