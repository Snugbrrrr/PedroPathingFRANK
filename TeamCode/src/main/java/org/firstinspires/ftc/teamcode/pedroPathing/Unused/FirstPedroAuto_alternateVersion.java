// NOTE: the State machine is not quite correct in this version
// we were experimenting with a different way to 'flip' for red

package org.firstinspires.ftc.teamcode.pedroPathing.Unused;

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
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name="Test Pedro OP")
@Configurable
@Disabled
public class FirstPedroAuto_alternateVersion extends OpMode {

    // variables
    private String alliance;
    private Follower follower;
    private Servo helicopterBoy;
    Timer pathTimer, opModeTimer;


    // ‘Pose’ is a Pedro data type / Class (object)
    // 1) define the Pose
    private Pose startPose;
    private Pose shootPose;
    private Pose pickupPose;
    private Pose stopPose;

    // ‘PathChain’ is a Pedro data type that will "connect" the different Poses
    // 2) declare a PathChain variable
    private PathChain driveStartToShoot, driveShootToPickup, drivePickupToStop;


    // ‘enum’ is a data type in Java, somewhat similar to a list
    // PathState is the name of the enumeration
    // path_state is the name of the variable that can be assigned any item in the enumeration
    // 3) add to the enumeration (the variable name is 'path_state')
    PathState path_state;
    public enum PathState{
        drive_start_to_shoot, shoot, drive_shoot_to_pickup, drive_pickup_to_stop
    }


    // helper function to build the PathChain variables
    // 4) define the PathChain variable ("connect" the poses)
    public void buildPaths(){
        driveStartToShoot = follower.pathBuilder()
                .addPath(new BezierLine(startPose, shootPose))
                .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                .build();
        driveShootToPickup = follower.pathBuilder()
                .addPath(new BezierLine(shootPose, pickupPose))
                .setLinearHeadingInterpolation(shootPose.getHeading(), pickupPose.getHeading())
                .build();
        drivePickupToStop = follower.pathBuilder()
                .addPath(new BezierLine(pickupPose, stopPose))
                .setLinearHeadingInterpolation(pickupPose.getHeading(), stopPose.getHeading())
                .build();
    }


    // 5) create code for the State Machine
    public void statePathUpdate(){
        switch(path_state){
            case drive_start_to_shoot:
                follower.followPath(driveStartToShoot, true);
                setPathState(path_state.shoot);
                break;
            case shoot:
                telemetry.addLine("SHOOOTING!!!!!!!!!!!!!! RAGGHHHHH!!!!!!!!");
//                sleep(1000);
                if(!follower.isBusy()){
                    helicopterBoy.setPosition(0.5);
                }
                if(!follower.isBusy() && pathTimer.getElapsedTimeSeconds() > 5){
                    setPathState(PathState.drive_shoot_to_pickup);
                }
                break;
            case drive_shoot_to_pickup:
                if(!follower.isBusy()){
                    follower.followPath(driveShootToPickup);
                    setPathState(path_state.drive_pickup_to_stop);
                }
                break;
            case drive_pickup_to_stop:
                if(!follower.isBusy()){
                    follower.followPath(drivePickupToStop);
                }
                break;
            default:
                telemetry.addLine("No State Command. Ruh roh");
                break;
        }
    }



    // helper function to set next path_state AND reset pathTimer
    public void setPathState(PathState newState){
        path_state = newState;
        pathTimer.resetTimer();
    }

    // helper function to flip alliance
    private Pose flipPose(Pose orig){
        double tempx = 144-orig.getX();
        double tempHeading = Math.PI - orig.getHeading();
        return new Pose(tempx, orig.getY(), tempHeading);
    }





    @Override
    public void init() {
        // get pot value, if something, set alliance to red
        alliance = "blue"; // TODO change this to a conditional with pot value

        // 6) flip for red alliance
        startPose = new Pose((alliance.equals("blue")) ? 20 : 144-20, 120, Math.toRadians((alliance.equals("blue")) ? 140 : 180-140));
        shootPose = new Pose((alliance.equals("blue")) ? 33 : 144-33, 108, Math.toRadians((alliance.equals("blue")) ? 140 : 180-140));
        pickupPose = new Pose((alliance.equals("blue")) ? 24 : 144-24, 94, Math.toRadians((alliance.equals("blue")) ? 180 : 180-180));
        stopPose = new Pose((alliance.equals("blue")) ? 45 : 144-45, 113, Math.toRadians((alliance.equals("blue")) ? 90 : 180-90));

        helicopterBoy = hardwareMap.get(Servo.class, "helicopterBoy");
        helicopterBoy.setDirection(Servo.Direction.FORWARD);
        helicopterBoy.setPosition(0.0);
        path_state = PathState.drive_start_to_shoot;
        pathTimer = new Timer();
        opModeTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);

        buildPaths();
        follower.setPose(startPose);
    }



    @Override
    public void start() {
        opModeTimer.resetTimer();
        setPathState(path_state);
    }



    @Override
    public void loop() {
        follower.update();
        statePathUpdate();

        telemetry.addData("path state", path_state.toString());
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", Math.toDegrees(follower.getPose().getHeading()));
    }


}





