package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
// TODO: 01.03.2022 Change PIDF coefficients; no idea which to use; also there is velocity regulators, not sure what to do with them
/**
 * This is not an OpMode
 * This class defines and inits standart 5-motor 1-CRServo configuration.
 * Use object of this class to stop wasting time on writing stupid stuff another ten thousand times.
 */
public class StandartConfig {
    private final ElapsedTime period = new ElapsedTime();
    /* Public OpMode members. */
    public DcMotor motorFrontRight = null;
    public DcMotor motorFrontLeft = null;
    public DcMotor motorBackLeft = null;
    public DcMotor motorBackRight = null;
    public DcMotorEx motorHand = null;
    public CRServo servoVal = null;
    /* local OpMode members. */
    HardwareMap hwMap = null;

    /* Constructor */
    public StandartConfig() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        motorFrontRight = hwMap.get(DcMotor.class, "motorFrontRight");
        motorFrontLeft = hwMap.get(DcMotor.class, "motorFrontLeft");
        motorBackLeft = hwMap.get(DcMotor.class, "motorBackLeft");
        motorBackRight = hwMap.get(DcMotor.class, "motorBackRight");
        motorHand = hwMap.get(DcMotorEx.class, "motorHand");

        motorFrontRight.setDirection(DcMotor.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackRight.setDirection(DcMotor.Direction.FORWARD);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorHand.setDirection(DcMotorEx.Direction.FORWARD);

        // Set all motors to zero power
        motorFrontRight.setPower(0.);
        motorFrontLeft.setPower(0.);
        motorBackLeft.setPower(0.);
        motorBackRight.setPower(0.);
        motorHand.setPower(0.);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        motorFrontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        motorHand.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motorHand.setTargetPosition(0);
        motorHand.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

        // Define and initialize ALL installed servos.
        servoVal = hwMap.get(CRServo.class, "servoVal");

    }
}