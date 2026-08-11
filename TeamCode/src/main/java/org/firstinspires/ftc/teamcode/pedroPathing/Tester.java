package org.firstinspires.ftc.teamcode.pedroPathing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
@Disabled
@TeleOp
public class Tester extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException{
        DcMotor motor = hardwareMap.dcMotor.get("backRightMotor");
        motor.setDirection(DcMotorSimple.Direction.FORWARD);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        while(opModeIsActive()){
            telemetry.addData("pos", motor.getCurrentPosition());
            telemetry.update();
        }
    }

}
