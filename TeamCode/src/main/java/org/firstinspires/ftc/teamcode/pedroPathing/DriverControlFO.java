package org.firstinspires.ftc.teamcode.pedroPathing;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pedroPathing.Subsystems.DriveTrain;

@TeleOp(name = "Driver Control Field Oriented")
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
