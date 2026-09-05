package org.firstinspires.ftc.teamcode.pedroPathing.Subsystems;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.ImuOrientationOnRobot;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class DriveTrain {
    private DcMotor flm;
    private DcMotor frm;
    private DcMotor blm;
    private DcMotor brm;
    private IMU imu;

    static double speedLimit = 0.0;
    static double drive = 0;
    static double rotate = 0;
    static double strafe = 0;
    static double yawAngle = 0;


    public DriveTrain(HardwareMap hardwareMap){

        flm = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        frm = hardwareMap.get(DcMotor.class, "frontRightMotor");
        blm = hardwareMap.get(DcMotor.class, "backLeftMotor");
        brm = hardwareMap.get(DcMotor.class, "backRightMotor");
        flm.setDirection(DcMotorSimple.Direction.REVERSE);
        blm.setDirection(DcMotorSimple.Direction.REVERSE);
        flm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        blm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        brm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        flm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        blm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        brm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // IMU wth wilted rose emoji
        imu = hardwareMap.get(IMU.class, "REVimu");
        RevHubOrientationOnRobot.LogoFacingDirection logoDirection =
                RevHubOrientationOnRobot.LogoFacingDirection.UP;
        RevHubOrientationOnRobot.UsbFacingDirection usbDirection =
                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD;
        RevHubOrientationOnRobot orientationOnRobot =
                new RevHubOrientationOnRobot(logoDirection, usbDirection);
        IMU.Parameters parameters = new IMU.Parameters(orientationOnRobot);
        imu.initialize(parameters);
        imu.resetYaw();

    }


    public void drive(Gamepad gamepad1){
        // Press Y to set ZERO ANGLE!
        if (gamepad1.yWasPressed()) imu.resetYaw();

        // turbo/slow button, slowwwwwwwwwww fasttttttt
        // max
        if (gamepad1.left_bumper) speedLimit = 1;
        // min
        else if (gamepad1.right_bumper) speedLimit = 0.2;
        // mid
        else speedLimit = 0.6;

        // get the values from the controller
        if (Math.abs(gamepad1.left_stick_y) > .05) drive = -1 * gamepad1.left_stick_y;
        else drive = 0;

        if (Math.abs(gamepad1.right_stick_x) > .05) rotate = gamepad1.right_stick_x;
        else rotate = 0;

        if (Math.abs(gamepad1.left_stick_x) > .05) strafe = gamepad1.left_stick_x;
        else strafe = 0;



        // field oriented control!
        yawAngle = -1 * imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        double sinTemp = Math.sin(yawAngle);
        double cosTemp = Math.cos(yawAngle);
        double fieldX = strafe * cosTemp - drive * sinTemp;
        double fieldY = strafe * sinTemp + drive * cosTemp;
        double flp = fieldY + fieldX + rotate;
        double frp = fieldY - fieldX - rotate;
        double blp = fieldY - fieldX + rotate;
        double brp = fieldY + fieldX - rotate;

        // find total. divide if over 1. normalizeeeee
        double tempMax = Math.max(1, Math.abs(flp));
        tempMax = Math.max(tempMax, Math.abs(frp));
        tempMax = Math.max(tempMax, Math.abs(blp));
        tempMax = Math.max(tempMax, Math.abs(brp));

        // adjust according to speed limit, SPINNNNNNN!!!!!!!!!!
        flm.setPower(flp / tempMax * speedLimit);
        frm.setPower(frp / tempMax * speedLimit);
        blm.setPower(blp / tempMax * speedLimit);
        brm.setPower(brp / tempMax * speedLimit);


    }



}
