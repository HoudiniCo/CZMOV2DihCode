package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.Servo;
import com.seattlesolvers.solverslib.gamepad.GamepadKeys;
import com.seattlesolvers.solverslib.gamepad.ButtonReader;
import com.seattlesolvers.solverslib.gamepad.GamepadEx;
import org.firstinspires.ftc.teamcode.Spindexer;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.limelightvision.LLResult;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import java.util.Arrays;

@TeleOp
public class FlywheelTest extends OpMode {
    private DcMotor intake;
    private Spindexer Spindexer;
    private ButtonReader AButton;
    private ButtonReader BButton;
    private ButtonReader XButton;
    private ButtonReader YButton;
    @Override
    public void init(){
        Spindexer = new Spindexer(hardwareMap);
        intake = hardwareMap.dcMotor.get("intake");
        DcMotor RS = hardwareMap.dcMotor.get("rightShooter");
        DcMotor LS = hardwareMap.dcMotor.get("leftShooter");
        Servo servo = hardwareMap.get(Servo.class, "transfer");
        Servo hood = hardwareMap.get(Servo.class, "hood");
        GamepadEx gamepadEx = new GamepadEx(gamepad1);
        AButton = new ButtonReader(gamepadEx, GamepadKeys.Button.A);
        BButton = new ButtonReader(gamepadEx, GamepadKeys.Button.B);
        XButton = new ButtonReader(gamepadEx, GamepadKeys.Button.X);
        YButton = new ButtonReader(gamepadEx, GamepadKeys.Button.Y);
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
    }
    @Override
    public void loop(){
        AButton.readValue();
        XButton.readValue();
        BButton.readValue();
        YButton.readValue();
        Spindexer.Update();
        if(AButton.wasJustPressed()){
            Spindexer.ManualSpinl();
        }
        if(BButton.wasJustPressed()){
            Spindexer.ManualSpinR();
        }
        if(XButton.wasJustPressed()){
            Spindexer.ToggleMode();
        }
        if(YButton.wasJustPressed()){
            Spindexer.shoot();
        }
        intake.setPower(gamepad1.left_trigger-gamepad1.right_trigger);
        telemetry.addData("hue Val", Spindexer.hue);
        telemetry.addData("sat Val", Spindexer.saturation);
        telemetry.addData("Color Val", Spindexer.value);
        telemetry.addData("servo Val", Spindexer.ServoPos);
        telemetry.update();
    }
}
