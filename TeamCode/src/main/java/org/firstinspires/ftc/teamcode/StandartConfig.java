package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
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
    public DcMotorEx motorFrontRight = null;
    public DcMotorEx motorFrontLeft = null;
    public DcMotorEx motorBackLeft = null;
    public DcMotorEx motorBackRight = null;
    public DcMotorEx motorHand = null;
    public CRServo servoVal = null;
    public CRServo servoUtki = null;
    /* local OpMode members. */
    HardwareMap hwMap = null;

    /* Constructor */
    public StandartConfig() {

    }

    /* Initialize standard Hardware interfaces */
    public void initTele(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        motorFrontRight = hwMap.get(DcMotorEx.class, "motorFrontRight");
        motorFrontLeft = hwMap.get(DcMotorEx.class, "motorFrontLeft");
        motorBackLeft = hwMap.get(DcMotorEx.class, "motorBackLeft");
        motorBackRight = hwMap.get(DcMotorEx.class, "motorBackRight");
        motorHand = hwMap.get(DcMotorEx.class, "motorHand");

        motorFrontRight.setDirection(DcMotorEx.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotorEx.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorEx.Direction.FORWARD);
        motorBackLeft.setDirection(DcMotorEx.Direction.REVERSE);
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
        servoVal.setDirection(CRServo.Direction.REVERSE);
        servoUtki = hwMap.get(CRServo.class, "servoUtki");
        servoUtki.setDirection(CRServo.Direction.REVERSE);
    }

    public void start_reset() {
        motorHand.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motorHand.setPower(-0.3);
        try {
            sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        motorHand.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorHand.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
    public void initAuto(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        motorFrontRight = hwMap.get(DcMotorEx.class, "motorFrontRight");
        motorFrontLeft = hwMap.get(DcMotorEx.class, "motorFrontLeft");
        motorBackLeft = hwMap.get(DcMotorEx.class, "motorBackLeft");
        motorBackRight = hwMap.get(DcMotorEx.class, "motorBackRight");
        motorHand = hwMap.get(DcMotorEx.class, "motorHand");

        motorFrontRight.setDirection(DcMotorEx.Direction.FORWARD);
        motorFrontLeft.setDirection(DcMotorEx.Direction.REVERSE);
        motorBackRight.setDirection(DcMotorEx.Direction.FORWARD);
        motorBackLeft.setDirection(DcMotorEx.Direction.REVERSE);
        motorHand.setDirection(DcMotorEx.Direction.FORWARD);

        // Set all motors to zero power
        motorFrontRight.setPower(0.);
        motorFrontLeft.setPower(0.);
        motorBackLeft.setPower(0.);
        motorBackRight.setPower(0.);
        motorHand.setPower(0.);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        motorBackRight.setTargetPosition(0);
        motorBackLeft.setTargetPosition(0);
        motorFrontRight.setTargetPosition(0);
        motorFrontLeft.setTargetPosition(0);

        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorHand.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        motorHand.setTargetPosition(0);
        motorHand.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

        // Define and initialize ALL installed servos.
        servoVal = hwMap.get(CRServo.class, "servoVal");
        servoVal.setDirection(CRServo.Direction.REVERSE);
        servoUtki = hwMap.get(CRServo.class, "servoUtki");
        servoUtki.setDirection(CRServo.Direction.REVERSE);
    }

    private double FLpos = 0;
    private double FRpos = 0;
    private double BLpos = 0;
    private double BRpos = 0;

    /**
     * This method is used in autonomous movement and works solely with GoBilda motors.
     * TODO 02.03.22 change this method to work using centimeters, not ticks.
     * @param BLplus Value for rear left motor; 1 full round = 1440 ticks
     * @param BRplus Value for rear right motor;
     * @param FLplus Value for front left motor;
     * @param FRplus Value for front right motor;
     */
    public void motorsSet(double BLplus, double BRplus, double FLplus, double FRplus) {
        BRpos = BRpos + BRplus;
        motorBackRight.setTargetPosition((int) (BRpos / 2.678));
        BLpos = BLpos + BLplus;
        motorBackLeft.setTargetPosition((int) (BLpos / 2.678));
        FLpos = FLpos + FLplus;
        motorFrontLeft.setTargetPosition((int) (FLpos / 2.678));
        FRpos = FRpos + FRplus;
        motorFrontRight.setTargetPosition((int) (FRpos / 2.678));
    }

    /**
     * This is strange method, I don't yet know ho to implement and use it properly,
     * but that is better than nothing, honestly.
     */
    private boolean finished = true;
    public void waitTillOver() {
        while ((motorFrontLeft.getVelocity() > 15) & (motorFrontRight.getVelocity() > 15) & (motorBackLeft.getVelocity() > 15) & (motorBackRight.getVelocity() > 15)) {
            finished = false;
        }
        finished = true;
    }
}