package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

// TODO: 01.03.2022 change values;
// TODO: 01.03.2022 rewrite opmode to be iterative
@Autonomous(name = "Autonomous Parking + LVL3 / Red", group = "Autonomous")
public class AutoNoTFODRed extends LinearOpMode {

    private final ElapsedTime runtime = new ElapsedTime();

    StandartConfig robot = new StandartConfig();

    @Override
    public void runOpMode() {

        robot.init(hardwareMap);

        robot.motorBackLeft.setPower(1.);
        robot.motorBackRight.setPower(1.);
        robot.motorFrontRight.setPower(1.);
        robot.motorFrontLeft.setPower(1.);

        waitForStart();
        runtime.reset();

        robot.motorsSet(2800, 2800, 2800, 2800);
        sleep(1500);

        robot.motorsSet(-800, 800, -800, 800);
        sleep(1500);

        robot.motorHand.setPower(0.25);
        robot.motorHand.setTargetPosition(285);
        sleep(650);

        robot.servoVal.setPower(-0.3);
        sleep(1500);

        robot.servoVal.setPower(0.);

        robot.motorsSet(2400, -2400, 2400, -2400);
        robot.motorHand.setTargetPosition(100);
        sleep(1500);

        robot.motorsSet(7800, 7800, 7800, 7800);

        while (opModeIsActive()) {
            telemetry.addData("runtime", runtime.toString());
        }
    }
}