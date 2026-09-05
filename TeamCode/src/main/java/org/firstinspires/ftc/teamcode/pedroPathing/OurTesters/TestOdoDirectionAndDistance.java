package org.firstinspires.ftc.teamcode.pedroPathing.OurTesters;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

@Disabled
@TeleOp
public class TestOdoDirectionAndDistance extends LinearOpMode {

    private GoBildaPinpointDriver odo;

    @Override
    public void runOpMode() throws InterruptedException{
//        DcMotor motor = hardwareMap.dcMotor.get("backRightMotor");
//        motor.setDirection(DcMotorSimple.Direction.FORWARD);
//        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        odo = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        odo.setOffsets(2.0, 1.5, DistanceUnit.INCH);
        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_SWINGARM_POD);

        odo.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.REVERSED,
                GoBildaPinpointDriver.EncoderDirection.FORWARD
        );

        odo.resetPosAndIMU();


        waitForStart();

        while(opModeIsActive()){
            odo.update();
            Pose2D pos = odo.getPosition();
//            telemetry.addData("pos", motor.getCurrentPosition());
            telemetry.addData("x",pos.getX(DistanceUnit.INCH));
            telemetry.addData("y",pos.getY(DistanceUnit.INCH));
            telemetry.addData("heading",pos.getHeading(AngleUnit.DEGREES));

            telemetry.update();
        }
    }

}
