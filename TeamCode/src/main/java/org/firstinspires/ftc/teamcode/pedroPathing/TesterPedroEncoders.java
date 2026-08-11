package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Disabled
@TeleOp
public class TesterPedroEncoders extends LinearOpMode {


    private Follower follower;

    @Override
    public void runOpMode() {
        follower = Constants.createFollower(hardwareMap);
//        follower = new Follower(hardwareMap);

        waitForStart();

        while (opModeIsActive()){
            follower.update();

            Pose currentPose = follower.getPose();
            telemetry.addData("x: ",currentPose.getX());
            telemetry.addData("y: ",currentPose.getY());




            telemetry.update();
        }


    }
}
