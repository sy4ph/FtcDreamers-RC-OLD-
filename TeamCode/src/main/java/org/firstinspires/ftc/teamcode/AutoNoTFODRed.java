package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

//todo change values, they are not final
@Autonomous(name = "Autonomous Parking + LVL3 / Red", group = "Autonomous")
public class AutoNoTFODRed extends LinearOpMode {

    StandartConfig robot = new StandartConfig();
    private ElapsedTime runtime = new ElapsedTime();

    public int FLpos = 0;
    public int FRpos = 0;
    public int BLpos = 0;
    public int BRpos = 0;

    public  void motorsSet(int BLplus, int BRplus, int FLplus,int FRplus) {
        BRpos = BRpos + BRplus;
        robot.motorBackRight.setTargetPosition(BRpos);
        BLpos = BLpos + BLplus;
        robot.motorBackLeft.setTargetPosition(BLpos);
        FLpos = FLpos + FLplus;
        robot.motorFrontLeft.setTargetPosition(FLpos);
        FRpos = FRpos + FRplus;
        robot.motorFrontRight.setTargetPosition(FRpos);
    }
    @Override
    public void runOpMode() {

        robot.init(hardwareMap);

        robot.motorBackLeft.setPower(1.);
        robot.motorBackRight.setPower(1.);
        robot.motorFrontRight.setPower(1.);
        robot.motorFrontLeft.setPower(1.);

        waitForStart();
        runtime.reset();

        motorsSet(2800,2800,2800,2800);
        sleep(1500);

        motorsSet(-800,800,-800,800);
        sleep(1500);

        robot.motorHand.setPower(0.25);
        robot.motorHand.setTargetPosition(285);
        sleep(650);

        robot.servoVal.setPower(-0.3);
        sleep(1500);

        robot.servoVal.setPower(0.);

        motorsSet(2400,-2400,2400,-2400);
        robot.motorHand.setTargetPosition(100);
        sleep(1500);

        motorsSet(7800,7800,7800,7800);

        while (opModeIsActive()) {
            telemetry.addData("runtime",runtime.toString());
        }
    }
}