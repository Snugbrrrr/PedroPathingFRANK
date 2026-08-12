package org.firstinspires.ftc.teamcode.pedroPathing;

import static java.lang.Math.toRadians;
import static java.lang.Thread.sleep;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

@Autonomous(name="Test Pedro OP")
@Configurable
public class TestPedroOP extends OpMode {

    private Follower follower;
    Timer pathTimer, opModeTimer;

    private final Pose testPose = new Pose();
    private final Pose startPose = new Pose(20, 120, Math.toRadians((140)));
    private final Pose shootPose = new Pose(50, 90);
    private final Pose stopPose = new Pose(70, 100, Math.toRadians((90)));

    public enum PathState{
        drive_start_to_shoot, shoot, drive_shoot_to_stop
    }

    PathState pathState;

    public void setPathState(PathState newState){
        pathState = newState;
        pathTimer.resetTimer();
    }

    private PathChain driveStartToShoot, driveShootToStop;

    public void buildPaths(){
        driveStartToShoot = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();
        driveShootToStop = follower.pathBuilder()
                .addPath(new BezierLine(shootPose, stopPose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), stopPose.getHeading())
                .build();
    }

    public void statePathUpdate(){
        switch(pathState){
            case drive_start_to_shoot:
                follower.followPath(driveStartToShoot, true);
                setPathState(pathState.shoot);
                break;
            case shoot:
                telemetry.addLine("SHOOOTING!!!!!!!!!!!!!! RAGGHHHHH!!!!!!!!");
//                sleep(1000);
                if(!follower.isBusy() && pathTimer.getElapsedTimeSeconds() > 5){
                    setPathState(PathState.drive_shoot_to_stop);
                }
                break;
            case drive_shoot_to_stop:
                if(!follower.isBusy()){
                    follower.followPath(driveShootToStop);
                }
            default:
                telemetry.addLine("No State Command. Ruh roh");
        }
    }


    @Override
    public void init() {
        pathState = PathState.drive_start_to_shoot;
        pathTimer = new Timer();
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);

        buildPaths();
        follower.setPose(startPose);
    }

    @Override
    public void start() {
        opModeTimer.resetTimer();
        setPathState(pathState);
    }

    @Override
    public void loop() {
        follower.update();
        statePathUpdate();

        telemetry.addData("path state", pathState.toString());
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
    }





}





