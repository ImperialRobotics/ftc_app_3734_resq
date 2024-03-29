package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

public class TeleOp3 extends OpMode 
{
	// TETRIX VALUES.
	final static double door_MIN_RANGE  = 0.20;
	final static double door_MAX_RANGE  = 0.90;
	final static double knock_MIN_RANGE  = 0.20;
	final static double knock_MAX_RANGE  = 0.7;

	// position of the arm servo.
	double doorPosition;

	// amount to change the door servo position.
	double doorDelta = 0.1;

	// position of the knock servo
	double knockPosition;

	// amount to change the knock servo position by
	double knockDelta = 0.1;
  double x=0;
  
	DcMotor motorRight1;
	DcMotor motorLeft1;
	DcMotor beaterbar;
	DcMotor armangle;
	Servo door;
	Servo knock;

	/**
	 * Constructor
	 */
	public TeleOp3() 
	{

	}

	/*
	 * Code to run when the op mode is first enabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#start()
	 */
	@Override
	public void init() 
	{
//Drivetrain configuration, names must match up in the phone, direction is viable to change
        motorLeft1 = hardwareMap.dcMotor.get("leftDrive");
        motorRight1 = hardwareMap.dcMotor.get("rightDrive");
        motorLeft1.setDirection(DcMotor.Direction.REVERSE);
        motorRight1.setDirection(DcMotor.Direction.FORWARD);

        //arm configuration, names must match up in the phone
        //motorangle = hardwareMap.dcMotor.get("motorangle");
        //motorangle.setDirection(DcMotor.Direction.FORWARD);

        //beaterbar configuration, names must match up in the phone
        beaterbar= hardwareMap.dcMotor.get("beaterBar");
        beaterbar.setDirection(DcMotor.Direction.FORWARD);

        //door servo
        door = hardwareMap.servo.get("Door");
        knock= hardwareMap.servo.get("knock");
		
		    // assign the starting position of the wrist and claw
		    doorPosition = 0.2;
		    knockPosition = 0.2;
	}

	/*
	 * This method will be called repeatedly in a loop
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#run()
	 */
	@Override
	public void loop() {
/*Gamepad1
*Joystick to control direction
*dpad_down = bring the arm up 
*dpad_up = arm falls down
*/

		// throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
		// 1 is full down
		// direction: left_stick_x ranges from -1 to 1, where -1 is full left
		// and 1 is full right
		float throttle = -gamepad1.left_stick_y;
		float direction = gamepad1.left_stick_x;
		float right = throttle - direction;
		float left = throttle + direction;

		// clip the right/left values so that the values never exceed +/- 1
		right = Range.clip(right, -1, 1);
		left = Range.clip(left, -1, 1);

		// scale the joystick value to make it easier to control
		// the robot more precisely at slower speeds.
		right = (float)scaleInput(right);
		left =  (float)scaleInput(left);
		
		// write the values to the motors
		//motorRight.setPower(right);
		//motorLeft.setPower(left);
		
    /*Gamepad2
     *Right Trigger = car wash forward
     *Left Trigger = car wash backwards
     *d_pad up = car wash off
    */
    if(gamepad2.left_bumper)
    {
    beaterbar.setPower(-1);
    }
    if(gamepad2.right_bumper)
    {
    beaterbar.setPower(1);
    }
    if(gamepad2.dpad_right)
    {
    beaterbar.setPower(0);
    }
    
    if(gamepad2.dpad_up)
        {
            //arm moves down
            //for(x<50000000)
            {
            armangle.setPower(5);
            x++;
            }
        }
        if (gamepad2.dpad_down)
        {
            //arm retracts to pull itself up
            //armextension-=30;
        }
        if(gamepad2.dpad_right)
        {
        beaterbar.setPower(0);
        }
		// update the position of the door.
		if (gamepad2.a) {
			// if the A button is pushed on gamepad2, increment the position of
			// the door servo.
			doorPosition += doorDelta;
		}

		if (gamepad2.y) 
		{
			// if the Y button is pushed on gamepad2, decrease the position of
			// the door servo.
			doorPosition -= doorDelta;
		}

		// update the position of the knock
		if (gamepad2.x) 
		{
			knockPosition += knockDelta;
		}

		if (gamepad2.b) 
		{
			knockPosition -= knockDelta;
		}

        // clip the position values so that they never exceed their allowed range.
        doorPosition = Range.clip(doorPosition, door_MIN_RANGE, door_MAX_RANGE);
        knockPosition = Range.clip(knockPosition, knock_MIN_RANGE, knock_MAX_RANGE);

		// write position values to the wrist and claw servo
		door.setPosition(doorPosition);
		knock.setPosition(knockPosition);



		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("door", "door:  " + String.format("%.2f", doorPosition));
        telemetry.addData("knock", "knock:  " + String.format("%.2f", knockPosition));
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", left));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", right));

	}

	/*
	 * Code to run when the op mode is first disabled goes here
	 * 
	 * @see com.qualcomm.robotcore.eventloop.opmode.OpMode#stop()
	 */
	@Override
	public void stop() {

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
