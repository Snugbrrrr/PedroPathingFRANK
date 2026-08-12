package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.control.PredictiveBrakingCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
//import com.pedropathing.ftc.localization.Encoder;
//import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
//import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
            .headingPIDFCoefficients(new PIDFCoefficients(0.7, 0.0, 0.01, 0.01))
            .forwardZeroPowerAcceleration(-43.8)
            .lateralZeroPowerAcceleration(-50.3)
            .translationalPIDFCoefficients(new PIDFCoefficients(0.08, 0.0, 0.01, 0.02))
            .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.1, 0.0, 0.01, 0.1, 0.01))
            .centripetalScaling(0.0)
            .predictiveBrakingCoefficients(new PredictiveBrakingCoefficients(0.1, 0.074, 0.0027))
            .mass(5);
    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static MecanumConstants driveConstants = new MecanumConstants()
            .maxPower(1)
            .rightFrontMotorName("frontRightMotor")
            .rightRearMotorName("backRightMotor")
            .leftFrontMotorName("frontLeftMotor")
            .leftRearMotorName("backLeftMotor")
            .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
            .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
            .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
            .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD)
            .xVelocity(56.7)
            .yVelocity(40.67)
            ;


//    public static DriveEncoderConstants localizerConstants = new DriveEncoderConstants()
//            .rightFrontMotorName("frontRightMotor")
//            .rightRearMotorName("backRightMotor")
//            .leftFrontMotorName("frontLeftMotor")
//            .leftRearMotorName("backLeftMotor")
//            .robotWidth(13.0)
//            .robotLength(11.5)
//            .leftFrontEncoderDirection(Encoder.FORWARD)
//            .leftRearEncoderDirection(Encoder.FORWARD)
//            .rightFrontEncoderDirection(Encoder.FORWARD)
//            .rightRearEncoderDirection(Encoder.FORWARD);

//            .forwardTicksToInches(0.67)
//            .strafeTicksToInches(0.3)
//            .turnTicksToInches(0.8)
    public static PinpointConstants localizerConstants = new PinpointConstants()
        .hardwareMapName("pinpoint")
        .distanceUnit(DistanceUnit.INCH)
        .forwardPodY(2)
        .strafePodX(2.37)
        .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD)
        .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
        .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(driveConstants)
//                .driveEncoderLocalizer(localizerConstants)
                .pinpointLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .build();
    }



}
