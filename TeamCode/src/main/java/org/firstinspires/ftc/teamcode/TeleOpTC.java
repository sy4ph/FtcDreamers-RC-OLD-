package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Standard Configuration OpMode", group = "Linear Opmode")
public class TeleOpTC extends OpMode {

    private final ElapsedTime runtime = new ElapsedTime();
    // Init standard configuration from StandardConfig class
    StandartConfig robot = new StandartConfig();
    private boolean changesMade = false;

    public void init() {
        robot.init(hardwareMap);
    }

    public void start() {
        runtime.reset();
    }

    public void loop() {
        if (gamepad1.right_trigger != 0) {
            robot.servoVal.setPower(gamepad1.right_trigger);
        }
        if (gamepad1.left_trigger != 0) {
            robot.servoVal.setPower(-gamepad1.left_trigger);
        }
        if ((gamepad1.right_trigger == 0) & (gamepad1.left_trigger == 0)) {
            robot.servoVal.setPower(0.);
        }
        if (gamepad1.a) {
            robot.motorHand.setTargetPosition(0);
            robot.motorHand.setPower(0.1);
        }
        if (gamepad1.b) {
            robot.motorHand.setTargetPosition(155); //-20
            robot.motorHand.setPower(0.15);
        }
        if (gamepad1.y) {
            robot.motorHand.setTargetPosition(300); //-15
            robot.motorHand.setPower(0.11);
        }
        if (gamepad1.right_bumper) {
            robot.servoVal.setPower(0.);
        }
        if ((gamepad1.dpad_up) & (changesMade)) {
            robot.motorHand.setTargetPosition(robot.motorHand.getTargetPosition() + 25);
            robot.motorHand.setPower(0.5);
            changesMade = false;
        }
        if ((gamepad1.dpad_down) & (changesMade)) {
            robot.motorHand.setTargetPosition(robot.motorHand.getTargetPosition() - 25);
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


        robot.motorFrontLeft.setPower(leftFrontPower);
        robot.motorFrontRight.setPower(rightFrontPower);
        robot.motorBackLeft.setPower(leftBackPower);
        robot.motorBackRight.setPower(rightBackPower);

        // Telemetry of hand DCMotor and runtime.
        telemetry.addData("Status", "Run Time: " + runtime);
        telemetry.addData("TargetPos", robot.motorHand.getTargetPosition());
        telemetry.addData("CurrentPos", robot.motorHand.getCurrentPosition());
        telemetry.update();
    }

    public void stop() {
        // Move hand to 0 position
        robot.motorHand.setPower(0.1);
        robot.motorHand.setTargetPosition(0);
    }
}