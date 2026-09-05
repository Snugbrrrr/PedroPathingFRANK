package org.firstinspires.ftc.teamcode.pedroPathing.Subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ArmSubsystem {
    private Servo arm;

    public ArmSubsystem(HardwareMap hardwareMap){
        arm = hardwareMap.get(Servo.class, "arm");
        arm.setDirection(Servo.Direction.FORWARD);
        arm.setPosition(0.0);
    }

    public void lift(double amt){
        arm.setPosition(amt);
    }


}
