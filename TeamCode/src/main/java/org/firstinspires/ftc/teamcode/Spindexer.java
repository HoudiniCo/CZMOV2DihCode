package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.JavaUtil;

@Config
public class Spindexer {
    private enum Ball{
        Purple,//210-240
        Green //120 hue
    }
    private Servo spindex;
    private final RevColorSensorV3 colorSensor;
    private ElapsedTime switchTime = new ElapsedTime();
    private ElapsedTime initialTime = new ElapsedTime();
    private int index = 0;
    private boolean isIntaking = true;
    private int ballColorInd = 0;
    public double ServoPos = 0;
    private final int SWITCHINGCOOLDOWN = 1000;
    private final int INTIALCOOLDOWN = 300;
    private double PerBallRotation = 0.276f;
    private double initialOffset = 0.17f;
    private boolean ManualMode = false;

    public double hue;
    public double avgHue;
    public double saturation;
    public double value;
    double oldInitialTime = 0;
    public static Ball[] instorage = new Ball[3];
    private Ball[] pattern = {Ball.Purple, Ball.Green, Ball.Purple};
    public Spindexer(HardwareMap hardwareMap){
        //telemetry.addData("index",index);
        spindex = hardwareMap.get(Servo.class, "spindexer");
        spindex.setPosition(initialOffset);
        ServoPos = initialOffset;
        colorSensor = hardwareMap.get(RevColorSensorV3.class, "colorSensor");
        colorSensor.setGain(10);
        spindex.setPosition(initialOffset);
        hue = 0;
        ballColorInd = 0;
        avgHue = 0;
    }
    public void shoot(){
        if(!isIntaking){
            if(pattern[ballColorInd] == instorage[index]){
                //shoot
            }else if(pattern[ballColorInd] == instorage[(index+1)%3]){
                spinToIndex((index+1)%3);
            }else if(pattern[ballColorInd] == instorage[(index+4)%3]){
                spinToIndex((index+4)%3);
            }
            ballColorInd ++;
        }
        if(ballColorInd == 3){
            isIntaking = true;
        }
    }
    public void ToggleMode(){
        ManualMode = !ManualMode;
        if(!ManualMode){
            isIntaking = true;
            ServoPos = initialOffset;
            initialTime.reset();
            ballColorInd = 0;
            index = 0;
        }else{
            isIntaking = false;
        }
    }
    public void ManualSpinl(){
        if(ManualMode){
            spinl();
        }
    }
    public void ManualSpinR(){
        if(ManualMode){
            spinR();
        }
    }
    private void spinl(){
        if(ServoPos+PerBallRotation <= 1f){
            ServoPos = ServoPos+PerBallRotation;
            index ++;
            index = index% 3;
        }
    }
    private void spinR(){
        if(ServoPos-PerBallRotation >= 0f){
            ServoPos = ServoPos-PerBallRotation;
            index += 4;
            index = index% 3;
        }
    }
    private void spinToIndex(int Newindex){
        index = (int)(((ServoPos-initialOffset)/PerBallRotation)%3);
        if(Math.abs((ServoPos + (Newindex-index)*PerBallRotation)-.5) < Math.abs((ServoPos - (Newindex-index)*PerBallRotation)-.5)) {
            ServoPos = ServoPos + (Newindex-index)*PerBallRotation;
        }else{
            ServoPos = ServoPos - (Newindex-index)*PerBallRotation;
        }
        index = Newindex;
    }
    public void Update(){
        NormalizedRGBA colors = colorSensor.getNormalizedColors();
        hue = JavaUtil.colorToHue(colors.toColor());
        //saturation = JavaUtil.colorToSaturation(colors.toColor());
        //value = JavaUtil.colorToValue(colors.toColor());
        if(!ManualMode && isIntaking) {
            if(initialTime.milliseconds() <= INTIALCOOLDOWN){
                avgHue += hue*(initialTime.milliseconds()-oldInitialTime);
            }
            if (switchTime.milliseconds() >= SWITCHINGCOOLDOWN && initialTime.milliseconds() >= INTIALCOOLDOWN && oldInitialTime <= INTIALCOOLDOWN) {
                hue = avgHue/INTIALCOOLDOWN;
                if (140 <= hue && hue <= 160) {
                    instorage[index] = Ball.Green;
                    spinl();
                    switchTime.reset();
                } else if (210 <= hue && hue <= 240) {
                    instorage[index] = Ball.Purple;
                    spinl();
                    switchTime.reset();
                }
                if(instorage[2] != null){
                    isIntaking = false;
                }
            }
            if(hue>20 && initialTime.milliseconds() >= INTIALCOOLDOWN){
                initialTime.reset();
                avgHue = 0;
            }
            oldInitialTime = initialTime.milliseconds();
        }
        spindex.setPosition(ServoPos);
//
    }
}
