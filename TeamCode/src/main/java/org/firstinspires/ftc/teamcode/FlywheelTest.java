package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.ButtonReader;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.Arrays;

@TeleOp
public class FlywheelTest extends LinearOpMode {

    public static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor intake = hardwareMap.dcMotor.get("intake");
        DcMotor RS = hardwareMap.dcMotor.get("rightShooter");
        DcMotor LS = hardwareMap.dcMotor.get("leftShooter");
        Servo servo = hardwareMap.get(Servo.class, "transfer");
        Servo hood = hardwareMap.get(Servo.class, "hood");
        Servo spindex = hardwareMap.get(Servo.class, "spindexer");
        GamepadEx gamepadEx = new GamepadEx(gamepad1);
        ButtonReader AButton = new ButtonReader(gamepadEx, GamepadKeys.Button.A);
        ButtonReader BButton = new ButtonReader(gamepadEx, GamepadKeys.Button.B);
        waitForStart();

        if (isStopRequested()) return;

        final double MAX_SHOOTER_HOOD = 0.21f;
        double shooterPow = .5;
        double hoodPos = 0;
        hood.setPosition(0);
        servo.setPosition(1);
        spindex.setPosition(.66);

        while (opModeIsActive()) {
            shooterPow += gamepad1.left_bumper ? 0.05:0;
            shooterPow -= gamepad1.right_bumper ? 0.05:0;

            hoodPos = gamepad1.left_stick_x;
            if(gamepad1.left_stick_button) {
                hood.setPosition(1 - MAX_SHOOTER_HOOD / 2 + hoodPos * MAX_SHOOTER_HOOD / 2);
            }
            intake.setPower(gamepad1.left_trigger-gamepad1.right_trigger);
            if(AButton.wasJustPressed()){
                if(servo.getPosition() >.5){
                    servo.setPosition(.2);
                }else{
                    servo.setPosition(1);
                }
            }
            if(BButton.wasJustPressed()){
                if(RS.getPower() > 0){
                    RS.setPower(0);
                    LS.setPower(0);
                }else{
                    RS.setPower(shooterPow);
                    LS.setPower(shooterPow);
                }
            }
            AButton.readValue();
            BButton.readValue();
            telemetry.addData("shooter", shooterPow);
            telemetry.update();
        }
    }
}
