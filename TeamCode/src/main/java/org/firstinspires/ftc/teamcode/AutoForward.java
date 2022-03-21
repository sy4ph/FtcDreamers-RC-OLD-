package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Autonomous Forward", group = "Autonomous")
public class AutoForward extends LinearOpMode {
    private final ElapsedTime runtime = new ElapsedTime();
    // changing to iterative raises strange exceptions
// i don't know what to do with them, it seems that function start() isn't able to
// handle java.lang.Thread.sleep() function.
// actually i am not sure that we should change to iterative at all, it seems
// that linear works just fine?

    StandartConfig robot = new StandartConfig();

    @Override
    public void runOpMode() {
        /**
         * Initialise standart config
         */
        robot.initTele(hardwareMap);

        runtime.reset();
        waitForStart();

        robot.motorHand.setPower(0.5);
        robot.motorHand.setTargetPosition(300);
        sleep(1200);
        robot.motorFrontRight.setPower(0.6);
        robot.motorFrontLeft.setPower(0.6);
        robot.motorBackLeft.setPower(0.6);
        robot.motorBackRight.setPower(0.6);

        sleep(1000);

        robot.motorFrontRight.setPower(0.);
        robot.motorFrontLeft.setPower(0.);
        robot.motorBackLeft.setPower(0.);
        robot.motorBackRight.setPower(0.);


        robot.motorsSet(7800, 7800, 7800, 7800);

        while (opModeIsActive()) {
            telemetry.addData("runtime", runtime.toString());
        }

    }
}
