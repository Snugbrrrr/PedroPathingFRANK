package org.firstinspires.ftc.teamcode.pedroPathing.OurTesters;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

//@Disabled
@TeleOp
@Disabled
public class TestPedroDirectionAndDistance extends LinearOpMode {


    private Follower follower;
    private Pose currentPose;

    @Override
    public void runOpMode() {
        follower = Constants.createFollower(hardwareMap);
//        follower = new Follower(hardwareMap);

        waitForStart();

        while (opModeIsActive()){
            follower.update();

            currentPose = follower.getPose();
            telemetry.addData("x: ",currentPose.getX());
            telemetry.addData("y: ",currentPose.getY());
            telemetry.addData("heading: ",Math.toDegrees(currentPose.getHeading()));




            telemetry.update();
        }


    }
}
