package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="MecTestV4", group="Linear Opmode")
public class MecTestV3 extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motorFrontLeft = null;
    private DcMotor motorBackLeft = null;
    private DcMotor motorFrontRight = null;
    private DcMotor motorBackRight = null;
    private DcMotor motorHand = null;
    private CRServo servoVal = null;
    private boolean changesMade = false;
    private double lErr = 0.;
    private int followPos = 0;
    double defU = 0.1;
    double kP = 0.0015;
    double kD = 0.0008;

    public void handToPos(int pos) {

        double err = pos - motorHand.getCurrentPosition();

        double P = err * kP;
        double D = (err - lErr) * kD;

        double dU = P + D;
        lErr = err;

        motorHand.setPower(defU + dU);
    }

    @Override
    public void runOpMode() {

        // Initialize the hardware variables. Note that the strings used here must correspond
        // to the names assigned during the robot configuration step on the DS or RC devices.
        motorFrontLeft = hardwareMap.get(DcMotor.class, "motorFrontLeft");
        motorBackLeft = hardwareMap.get(DcMotor.class, "motorBackLeft");
        motorFrontRight = hardwareMap.get(DcMotor.class, "motorFrontRight");
        motorBackRight = hardwareMap.get(DcMotor.class, "motorBackRight");
        motorHand = hardwareMap.get(DcMotor.class,"motorHand");
        servoVal = hardwareMap.get(CRServo.class, "servoVal");

        // Most robots need the motors on one side to be reversed to drive forward.
        // When you first test your robot, push the left joystick forward
        // and flip the direction ( FORWARD <-> REVERSE ) of any wheel that runs backwards
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.FORWARD);
        motorBackRight.setDirection(DcMotor.Direction.FORWARD);
        motorHand.setDirection(DcMotorSimple.Direction.FORWARD);
        servoVal.setDirection(DcMotorSimple.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        motorHand.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorHand.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        waitForStart();
        runtime.reset();


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            handToPos(followPos);
            if (gamepad1.left_bumper) {
                servoVal.setPower(-1.);
            }
            if (gamepad1.right_bumper) {
                servoVal.setPower(1.);
            }
            if ((gamepad1.a)&(changesMade)) {
                followPos -= 50;
                changesMade = false;
            }

//            if (gamepad1.b) {
//                followPos = 300;
//            }
            if ((gamepad1.y)&(changesMade)) {
                followPos += 50;
                changesMade = false;
            }
            if ((!gamepad1.a)&(!gamepad1.y))
                changesMade = true;

            double max;

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial   = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
            double lateral =  gamepad1.left_stick_x;
            double yaw     =  gamepad1.right_stick_x;

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower  = axial + lateral + yaw;
            double rightFrontPower = axial - lateral - yaw;
            double leftBackPower   = axial - lateral + yaw;
            double rightBackPower  = axial + lateral - yaw;

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            // Send calculated power to wheels
            motorFrontLeft.setPower(leftFrontPower);
            motorFrontRight.setPower(rightFrontPower);
            motorBackLeft.setPower(leftBackPower);
            motorBackRight.setPower(rightBackPower);

            telemetry.addData("Hand Motor Power: ", motorHand.getPower());
            telemetry.addData("Hand Motor Ticks: ", motorHand.getCurrentPosition());
            telemetry.addData("Hand Motor Target Positions: ", followPos);
            telemetry.update();
        }
    }}
