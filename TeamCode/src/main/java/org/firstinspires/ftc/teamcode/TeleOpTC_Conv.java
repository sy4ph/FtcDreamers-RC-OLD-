package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Conveyor Configuration Mode", group = "Linear Opmode")
public class TeleOpTC_Conv extends OpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    // Init standard configuration from StandardConfig class
    ConvConfig robot = new ConvConfig();
    private boolean changesMade = false;

    public void init() {
        robot.initTele(hardwareMap);
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());
    }

    public void start() {
        runtime.reset();
    }

    public void loop() {
        if (gamepad1.right_trigger != 0) {
            robot.motorConv.setPower(gamepad1.right_trigger);
        }
        if (gamepad1.left_trigger != 0) {
            robot.motorConv.setPower(-gamepad1.left_trigger);
        }
        if ((gamepad1.right_trigger == 0) & (gamepad1.left_trigger == 0)) {
            robot.motorConv.setPower(0.);
        }
        if (gamepad1.a) {
            robot.motorElev.setTargetPosition(0);
            robot.motorElev.setPower(0.1);
        }
        if (gamepad1.b) {
            robot.motorElev.setTargetPosition(77); //-20
            robot.motorElev.setPower(0.15);
        }
        if (gamepad1.y) {
            robot.motorElev.setTargetPosition(150); //-15
            robot.motorElev.setPower(0.11);
        }
        if (gamepad1.right_bumper) {
            robot.motorConv.setPower(0.);
        }
        if ((gamepad1.dpad_up) & (changesMade)) {
            robot.motorElev.setTargetPosition(robot.motorElev.getTargetPosition() + 12);
            robot.motorElev.setPower(0.5);
            changesMade = false;
        }
        if ((gamepad1.dpad_down) & (changesMade)) {
            robot.motorElev.setTargetPosition(robot.motorElev.getTargetPosition() - 12);
            robot.motorElev.setPower(0.5);
            changesMade = false;
        }
        if ((!gamepad1.dpad_down) & (!gamepad1.dpad_up)) {
            changesMade = true;
        }
        if ((gamepad1.left_bumper) & (gamepad1.a)) {
            robot.motorElev.setPower(0.);
            try {
                sleep(900);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            robot.motorElev.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.motorElev.setMode(DcMotor.RunMode.RUN_TO_POSITION);
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


        robot.motorFrontLeft.setPower(leftFrontPower*0.8);
        robot.motorFrontRight.setPower(rightFrontPower*0.8);
        robot.motorBackLeft.setPower(leftBackPower*0.8);
        robot.motorBackRight.setPower(rightBackPower*0.8);

        // Telemetry of hand DCMotor and runtime.
        telemetry.addData("Status", "Run Time: " + runtime);
        telemetry.addData("TargetPos", robot.motorElev.getTargetPosition());
        telemetry.addData("CurrentPos", robot.motorElev.getCurrentPosition());
        telemetry.update();
    }

    public void stop() {
        // Move hand to 0 position
        robot.motorElev.setPower(0.1);
        robot.motorElev.setTargetPosition(0);
    }
}

