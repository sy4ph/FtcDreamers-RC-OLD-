package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "AutoTest1", group = "Autonomous")
public class AutonomousTest extends LinearOpMode {
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
        while ((motorFrontRight.getCurrentPosition() != motorFrontRight.getTargetPosition()) & (motorBackRight.getCurrentPosition() != motorBackRight.getTargetPosition()) & (motorFrontLeft.getCurrentPosition() != motorFrontLeft.getTargetPosition()) & (motorBackLeft.getCurrentPosition() != motorBackLeft.getTargetPosition())) {
            sleep(10);
        }
        sleep(100);
    }

    public  void motorsSet() {
        motorBackRight.setTargetPosition(BRpos);
        motorBackLeft.setTargetPosition(BLpos);
        motorFrontLeft.setTargetPosition(FLpos);
        motorFrontRight.setTargetPosition(FRpos);
    }
    @Override
    public void runOpMode() {
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
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorHand.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorHand.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorBackRight.setPower(1.);
        motorBackLeft.setPower(1.);
        motorFrontLeft.setPower(1.);
        motorFrontRight.setPower(1.);



// movement starts here
        BRpos = BRpos + 2800;
        FRpos = FRpos + 2800;
        BLpos = BLpos + 2800;
        FLpos = FLpos + 2800;

        motorsSet();



        sleep(1000);

        servoVal.setPower(1.);
        sleep(2500);
        servoVal.setPower(0.);

        motorHand.setTargetPosition(200);
        motorHand.setPower(0.15);
        sleep(2000);

        BRpos = BRpos - 3200;
        FRpos = FRpos - 3200;
        BLpos = BLpos + 3200;
        FLpos = FLpos + 3200;

        motorsSet();

        sleep(1500);


        BRpos = BRpos + 2300;
        FRpos = FRpos + 2300;
        BLpos = BLpos + 2300;
        FLpos = FLpos + 2300;

        motorsSet();

        sleep(1500);

        servoVal.setPower(-1.);
        sleep(2000);
        servoVal.setPower(0.);

        motorHand.setPower(0.1);
        motorHand.setTargetPosition(50);

        sleep(900);
        BRpos = BRpos + 3200;
        FRpos = FRpos + 3200;
        BLpos = BLpos - 3200;
        FLpos = FLpos - 3200;
        motorsSet();

        while (opModeIsActive()) {
            telemetry.addData("runtime",runtime.toString());
        }
    }
}
