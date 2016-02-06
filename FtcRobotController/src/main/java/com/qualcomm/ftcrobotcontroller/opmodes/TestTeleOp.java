package com.qualcomm.ftcrobotcontroller.opmodes;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
/**
 * Created by 2955733 on 12/10/2015.
 */
public class TestTeleOp extends OpMode
{
    /*
  * Note: the configuration of the servos is such that
    as the servo approaches 1 the door is extending open.
  */
    // TETRIX VALUES.
    final static double door_MIN_RANGE  = 0.00;
    final static double door_MAX_RANGE  = 1.00;

    // position of the door servo.
    double doorPosition;

    // amount to change the arm servo position.
    double doorDelta = 0.1;

    //Drive Train
    DcMotor motorRight1;
    DcMotor motorLeft1;
    DcMotor motorLeft2;
    // DcMotor motorRight2;
    //DcMotor motorLeft2;

    //Arm control
    DcMotor motorArmAngle;
    //DcMotor motorextension;
    //DcMotor motorangle;

    //Beater Bar
    DcMotor beaterbar;

    Servo door;
    Servo knock;
    /**
     * Constructor
     */
    public TestTeleOp()
    {

    }

    /*
     * Code to run when the op mode is first enabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
     */
    @Override
    public void init() {


		/*
		 * Use the hardwareMap to get the dc motors and servos by name. Note
		 * that the names of the devices must match the names used when you
		 * configured your robot and created the configuration file.
		 */
          /*Contingent upon the hardware team wiring the robot in the manner that
           they had specified to the software team.
           .FOWARD= Motor going in the normal direction
           .REVERSE= Motor going opposite the "natural direction"
          */
        //Drivetrain configuration, names must match up in the phone, direction is viable to change
        motorLeft1 = hardwareMap.dcMotor.get("leftDriveA");
        motorLeft2= hardwareMap.dcMotor.get("leftDriveB");
        //motorLeft2 = hardwareMap.dcMotor.get("motorLeft2");
        motorRight1 = hardwareMap.dcMotor.get("rightDrive");
        //motorRight2 = hardwareMap.dcMotor.get("motorRight2");
        motorLeft1.setDirection(DcMotor.Direction.FORWARD);//r
        motorLeft2.setDirection(DcMotor.Direction.REVERSE);//f
        //motorLeft2.setDirection(DcMotor.Direction.REVERSE);//
        motorRight1.setDirection(DcMotor.Direction.REVERSE);//r
        //motorRight2.setDirection(DcMotor.Direction.FORWARD);
        doorPosition=0.5;
        //arm configuration, names must match up in the phone
        //motorextension = hardwareMap.dcMotor.get("motorextension");
        // motorangle = hardwareMap.dcMotor.get("motorangle");
        motorArmAngle = hardwareMap.dcMotor.get("armAngle");
        //motorextension.setDirection(DcMotor.Direction.FORWARD);
        motorArmAngle.setDirection(DcMotor.Direction.FORWARD);
        motorArmAngle.setMode(DcMotorController.RunMode.RUN_TO_POSITION);
        //beaterbar configuration, names must match up in the phone
        beaterbar= hardwareMap.dcMotor.get("beaterBar");
        beaterbar.setDirection(DcMotor.Direction.FORWARD);

        //door servo
        door = hardwareMap.servo.get("Door");
        knock= hardwareMap.servo.get("Knock");

        doorPosition = 0.2;

    }

    /*
     * This method will be called repeatedly in a loop
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
     */
    @Override
    public void loop(){

		/*
		 * Gamepad 1
		 * Gamepad 1 controls the motors via the left stick
		 */

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float power = -gamepad1.left_stick_y; //Foward, Backwards
        float direction = gamepad1.left_stick_x; // Left, Right
        float right = power - direction;
        float left = power + direction;

        // clip the right/left values so that the values never exceed +/-, simplifies the input
        right = Range.clip(right, -1, 1);
        left = Range.clip(left, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        right = (float)scaleInput(right);
        left =  (float)scaleInput(left);

        // write the values to the motors
        motorRight1.setPower(right);
        //motorRight2.setPower(right);
        motorLeft1.setPower(left);
        motorLeft2.setPower(left);
        // motorLeft2.setPower(left);
        //CONTROLLER 1

        //wait(100);


        if(gamepad2.a)
        {
            doorPosition +=doorDelta;
            telemetry.addData("door position", doorPosition);
        }
        if(gamepad2.b)
        {
            doorPosition -=doorDelta;
            telemetry.addData("door position", doorPosition);
        }

        doorPosition = Range.clip(doorPosition , door_MAX_RANGE , door_MIN_RANGE);

        door.setPosition(doorPosition);


        if (gamepad1.a)
        {
            // if the A button is pushed on gamepad2, carwash will intake at full power
            beaterbar.setPower(1);
        }
        if (gamepad1.y)
        {
            beaterbar.setPower(-1);
        }
        if(gamepad1.left_bumper)
        {
            beaterbar.setPower(0);
        }
        //1 to .9

        double x=0.0;

        if(gamepad2.dpad_up)
        {
            motorArmAngle.setPower(.1);
            x++;
            motorArmAngle.setPower(0.0);
            telemetry.addData("Power in arm angle", "arm angle: " + String.format("%.2f", -0.3));
        }

        if(gamepad2.right_bumper){
            motorArmAngle.setPower(-0.30);
            telemetry.addData("Power in arm angle", "arm angle: " + String.format("%.2f", 0.3));
        }

        // clip the right/left values so that the values never exceed +/-, simplifies the input

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.

        // write the values to the motors
        //motorRight2.setPower(right);

        double power1 = 0.0;
        if (gamepad2.x)
        {
            knock.setPosition(.9);
            knock.setPosition(1);
        }
        /*else
        {
            power1=0.0;
            motorArmExtension.setPower(power1);
        }*/
        // clip the position values so that they never exceed their allowed range.

        // write position values to the door servo




		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("door", "door:  " + String.format("%.2f", doorPosition));
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));
        telemetry.addData("Power in arm extension", "arm extension: " + String.format("%.2f", power1));
    }

    @Override
    public void stop() {
    /*
     * Code to run when the op mode is first disabled goes here
     *
     * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
     */

    }


    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

}