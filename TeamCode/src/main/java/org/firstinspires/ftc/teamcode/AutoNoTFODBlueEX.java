package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * this opmode is an extension to the standart basic no TFOD opmode.
 * It will try to get at least a couple of cubes besides a pre-loaded cube.
 */


@Autonomous(name = "Autonomous Parking + LVL3 / Blue Extended", group = "Autonomous w/out TFOD")
@Disabled
public class AutoNoTFODBlueEX extends LinearOpMode {
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
        robot.initAuto(hardwareMap);

        robot.motorBackLeft.setPower(1.);
        robot.motorBackRight.setPower(1.);
        robot.motorFrontRight.setPower(1.);
        robot.motorFrontLeft.setPower(1.);

        waitForStart();
        runtime.reset();

        int loop = 0;

        while (opModeIsActive()) {
            //todo write something here huh

        robot.motorBackLeft.setTargetPosition(500);
        robot.motorFrontLeft.setTargetPosition(500);
        robot.motorBackRight.setTargetPosition(-500);
        robot.motorFrontRight.setTargetPosition(-500);
        sleep(600);
        while (loop < 4) {
            robot.motorFrontRight.setTargetPosition(robot.motorFrontRight.getTargetPosition()*-1);
            robot.motorFrontLeft.setTargetPosition(robot.motorFrontLeft.getTargetPosition()*-1);
            robot.motorBackLeft.setTargetPosition(robot.motorBackLeft.getTargetPosition()*-1);
            robot.motorBackRight.setTargetPosition(robot.motorBackRight.getTargetPosition()*-1);
        }
            telemetry.addData("runtime", runtime.toString());
        }

    }
}
