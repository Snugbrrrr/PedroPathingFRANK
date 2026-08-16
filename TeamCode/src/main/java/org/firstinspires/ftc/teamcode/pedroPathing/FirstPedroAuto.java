package org.firstinspires.ftc.teamcode.pedroPathing;

import static java.lang.Thread.sleep;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="Test Pedro OP")
@Configurable
public class FirstPedroAuto extends OpMode {

    // variables
    private String alliance;
    private Follower follower;
    private Servo helicopterBoy;
    Timer pathTimer, opModeTimer;

    // ‘Pose’ is a Pedro data type / Class (object)
    // 1) define the Pose
    private Pose startPose = new Pose(20, 120, Math.toRadians(140));
    private Pose shootPose = new Pose(33, 108, Math.toRadians(140));
    private Pose pickupPose = new Pose(24, 94, Math.toRadians(180));
    private Pose stopPose = new Pose(45, 113, Math.toRadians(90));

//    private Pose startPose;
//    private Pose shootPose;
//    private Pose pickupPose;
//    private Pose stopPose;


    // ‘enum’ is a data type in Java, somewhat similar to a list
    // 2) declare a path_state
    public enum PathState{
        drive_start_to_shoot, shoot, drive_shoot_to_pickup, drive_pickup_to_stop
    }


    // declare an instance of a 'PathState' variable
    PathState pathState;

    // helper function to set next pathState AND reset pathTimer
    public void setPathState(PathState newState){
        pathState = newState;
        pathTimer.resetTimer();
    }

    // ‘PathChain’ is a Pedro data type that will use the different Poses
    // 3) declare a pathChain
    private PathChain driveStartToShoot, driveShootToPickup, drivePickupToStop;

    // helper function to build the PathChain variables
    // 4) define the pathChain
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


    // helper function to set the pathState variable
    // 5) create code for the State Machine
    public void statePathUpdate(){
        switch(pathState){
            case drive_start_to_shoot:
                follower.followPath(driveStartToShoot, true);
                setPathState(pathState.shoot);
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
                    setPathState(pathState.drive_pickup_to_stop);
                }
            case drive_pickup_to_stop:
                if(!follower.isBusy()){
                    follower.followPath(drivePickupToStop);
                }
            default:
                telemetry.addLine("No State Command. Ruh roh");
        }
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
        if(alliance.equals("red")){
            // 6) flip for red alliance
            startPose = flipPose(startPose);
            shootPose = flipPose(shootPose);
            pickupPose = flipPose(pickupPose);
            stopPose = flipPose(stopPose);
        }

//        startPose = new Pose((alliance.equals("blue")) ? 20 : 144-20, 120, Math.toRadians((alliance.equals("blue")) ? 140 : 180-140));
//        shootPose = new Pose((alliance.equals("blue")) ? 33 : 144-33, 108, Math.toRadians((alliance.equals("blue")) ? 140 : 180-140));
//        pickupPose = new Pose((alliance.equals("blue")) ? 24 : 144-24, 94, Math.toRadians((alliance.equals("blue")) ? 180 : 180-180));
//        stopPose = new Pose((alliance.equals("blue")) ? 45 : 144-45, 113, Math.toRadians((alliance.equals("blue")) ? 90 : 180-90));

        helicopterBoy = hardwareMap.get(Servo.class, "helicopterBoy");
        helicopterBoy.setDirection(Servo.Direction.FORWARD);
        helicopterBoy.setPosition(0.0);
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
        telemetry.addData("heading", Math.toDegrees(follower.getPose().getHeading()));
    }


}





