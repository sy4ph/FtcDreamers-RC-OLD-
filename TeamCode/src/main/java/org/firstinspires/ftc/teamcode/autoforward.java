package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
//so this code is an emergency autonomous run; just points for parking.
@Autonomous(name = "Forward",group = "Autonomous")
public class autoforward extends LinearOpMode {
    private DcMotor motorFrontLeft = null;
    private DcMotor motorBackLeft = null;
    private DcMotor motorFrontRight = null;
    private DcMotor motorBackRight = null;
    private DcMotor motorHand = null;

    public int FLpos = 0;
    public int FRpos = 0;
    public int BLpos = 0;
    public int BRpos = 0;

    public  void motorsSet(int BLplus, int BRplus, int FLplus,int FRplus) {
        BRpos = BRpos + BRplus;
        motorBackRight.setTargetPosition(BRpos);
        BLpos = BLpos + BLplus;
        motorBackLeft.setTargetPosition(BLpos);
        FLpos = FLpos + FLplus;
        motorFrontLeft.setTargetPosition(FLpos);
        FRpos = FRpos + FRplus;
        motorFrontRight.setTargetPosition(FRpos);
    }
    @Override
    public void runOpMode() {
        motorFrontLeft = hardwareMap.get(DcMotor.class, "motorFrontLeft");
        motorBackLeft = hardwareMap.get(DcMotor.class, "motorBackLeft");
        motorFrontRight = hardwareMap.get(DcMotor.class, "motorFrontRight");
        motorBackRight = hardwareMap.get(DcMotor.class, "motorBackRight");
        motorHand = hardwareMap.get(DcMotor.class,"motorHand");

        motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        motorFrontRight.setDirection(DcMotor.Direction.FORWARD);
        motorBackRight.setDirection(DcMotor.Direction.FORWARD);
        motorHand.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

//         Autonomous part setup
        motorFrontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontLeft.setTargetPosition(0);
        motorFrontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorBackLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackLeft.setTargetPosition(0);
        motorBackLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorFrontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorFrontRight.setTargetPosition(0);
        motorFrontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorBackRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorBackRight.setTargetPosition(0);
        motorBackRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorHand.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorHand.setTargetPosition(0);
        motorHand.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        motorBackRight.setPower(1.);
        motorBackLeft.setPower(1.);
        motorFrontLeft.setPower(1.);
        motorFrontRight.setPower(1.);

        motorsSet(5600,5600,5600,5600);
        while (opModeIsActive()) {
        }
    }
}
