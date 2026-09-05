package org.firstinspires.ftc.teamcode.pedroPathing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Subsystems.DriveTrain;

public class DriverControlFO extends OpMode {
    DriveTrain driveTrain;


    @Override
    public void init() {
        driveTrain = new DriveTrain(hardwareMap);

    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        driveTrain.drive(gamepad1);
    }
}
