package org.firstinspires.ftc.teamcode.pedroPathing;

import com.bylazar.configurables.annotations.Configurable;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="Test Pedro OP ANONYMOUS!!!")
@Configurable
public class FirstPedroAuto_anon extends OpMode {

    // variables
    private String alliance;
    private Follower follower;
    private Servo helicopterBoy;
    Timer pathTimer, opModeTimer;



    // ‘Pose’ is a Pedro data type / Class (object)
    // **1) define the Pose (a resting state)
    private Pose startPose = new Pose(20, 120, Math.toRadians(140));
    private Pose shootPose = new Pose(33, 108, Math.toRadians(140));
    private Pose pickupPose = new Pose(24, 94, Math.toRadians(180));
    private Pose stopPose = new Pose(45, 113, Math.toRadians(90));



    // ‘enum’ is a data type in Java, somewhat similar to a list
    // 'PathState' is the name of the enumeration
    // 'path_state' is the name of the variable that can be assigned any item in the enumeration
    // **2) add to the enumeration (the different states)
    PathState path_state;
    public enum PathState{
        drive_start_to_shoot, shoot, drive_shoot_to_pickup, drive_pickup_to_stop
    }



    /*
        Changing hardware here runs EVERY FRAME in loop()
        Behavior: Sends the command repeatedly every loop cycle.
        Best used for: Continuous updates, active monitoring,
            or dynamic logic (e.g., PID target adjustments, sensor checks,
            or setting state transitions).
     */
    // **3) the State Machine (set to next state)
    public void statePathUpdate() {
        switch(path_state) {
            case drive_start_to_shoot:
                // Transition when driving completes
                if (!follower.isBusy()) {
                    setPathState(PathState.shoot);
                }
                break;

            case shoot:
                telemetry.addLine("SHOOOTING!!!!!!!!!!!!!! RAGGHHHHH!!!!!!!!");
                // Wait 5 seconds after reaching shoot pose before moving on
                if (pathTimer.getElapsedTimeSeconds() > 5.0) {
                    setPathState(PathState.drive_shoot_to_pickup);
                }
                break;

            case drive_shoot_to_pickup:
                // Transition when driving completes
                if (!follower.isBusy()) {
                    setPathState(PathState.drive_pickup_to_stop);
                }
                break;

            case drive_pickup_to_stop:
                if (!follower.isBusy()) {
                    // Autonomous finished!
                }
                break;

            default:
                telemetry.addLine("No State Command. Ruh roh");
                break;
        }
    }


    /*
        Changing other hardware here runs ONCE on transistion
        Behavior: Sends one command to the hardware upon entry,
            then lets the device run independently while loop() continues.
        Best used for: One-time actions or triggering state initialization
            (e.g., setting a motor power, launching a single movement, or starting a trajectory).
     */
    // **4) write code to build the PathChain (PathChain is a Pedro data type)
    //      and control other hardware
    public void setPathState(PathState newState) {
        path_state = newState;
        pathTimer.resetTimer();

        // Trigger path movement ONCE upon entering the state
        switch(path_state) {
            case drive_start_to_shoot:
                follower.followPath(follower.pathBuilder()
                        .addPath(new BezierLine(startPose, shootPose))
                        .setLinearHeadingInterpolation(startPose.getHeading(), shootPose.getHeading())
                        .build(), true);
                break;
            case drive_shoot_to_pickup:
                follower.followPath(follower.pathBuilder()
                        .addPath(new BezierLine(shootPose, pickupPose))
                        .setLinearHeadingInterpolation(shootPose.getHeading(), pickupPose.getHeading())
                        .build(), true);
                break;
            case drive_pickup_to_stop:
                follower.followPath(follower.pathBuilder()
                        .addPath(new BezierLine(pickupPose, stopPose))
                        .setLinearHeadingInterpolation(pickupPose.getHeading(), stopPose.getHeading())
                        .build(), true);
                helicopterBoy.setPosition(0.0);
                break;
            case shoot:
                helicopterBoy.setPosition(0.5);
                break;
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
            // **5) flip Pose for red alliance
            startPose = flipPose(startPose);
            shootPose = flipPose(shootPose);
            pickupPose = flipPose(pickupPose);
            stopPose = flipPose(stopPose);
        }

        pathTimer = new Timer();
        opModeTimer = new Timer();

        // initialize other hardware
        helicopterBoy = hardwareMap.get(Servo.class, "helicopterBoy");
        helicopterBoy.setDirection(Servo.Direction.FORWARD);
        helicopterBoy.setPosition(0.0);


        // set the PathState and setup the Follower
        path_state = PathState.drive_start_to_shoot;
        follower = Constants.createFollower(hardwareMap);

        // set the initial Pose
        follower.setPose(startPose);
    }



    @Override
    public void start() {
        opModeTimer.resetTimer();
        // this triggers the first motion
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
        telemetry.addData("path timer", pathTimer.toString());
    }


}





