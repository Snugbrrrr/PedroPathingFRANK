package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.DriveEncoderConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants().mass(5);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
                .mecanumDrivetrain(driveConstants)
                .driveEncoderLocalizer(localizerConstants)
                .pathConstraints(pathConstraints)
                .build();
    }

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
            ;
    public static DriveEncoderConstants localizerConstants = new DriveEncoderConstants()
            .rightFrontMotorName("frontRightMotor")
            .rightRearMotorName("backRightMotor")
            .leftFrontMotorName("frontLeftMotor")
            .leftRearMotorName("backLeftMotor")
            .robotWidth(13.0)
            .robotLength(11.5);
//            .leftFrontEncoderDirection(Encoder.FORWARD)
//            .leftRearEncoderDirection(Encoder.FORWARD)
//            .rightFrontEncoderDirection(Encoder.FORWARD)
//            .rightRearEncoderDirection(Encoder.FORWARD)

//            .forwardTicksToInches(0.67)
//            .strafeTicksToInches(0.3)
//            .turnTicksToInches(0.8)



}
