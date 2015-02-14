/******************************************************************************************************************
 * File:SinkFilter.java
 * Course: 17655
 * Project: Assignment 1
 * Copyright: Copyright (c) 2003 Carnegie Mellon University
 * Versions:
 *	1.0 November 2008 - Sample Pipe and Filter code (ajl).
 *
 * Description:
 *
 * This class serves as an example for using the SinkFilterTemplate for creating a sink filter. This particular
 * filter reads some input from the filter's input port and does the following:
 *
 *	1) It parses the input stream and "decommutates" the measurement ID
 *	2) It parses the input steam for measurments and "decommutates" measurements, storing the bits in a long word.
 *
 * This filter illustrates how to convert the byte stream data from the upstream filterinto useable data found in
 * the stream: namely time (long type) and measurements (double type).
 *
 *
 * Parameters: 	None
 *
 * Internal Methods: None
 *
 ******************************************************************************************************************/
import java.util.*; // This class is used to interpret time words
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat; // This class is used to format and write time in a string format.

public class SinkFilter extends FilterFramework {
	SinkFilter() {
		super(2, 1);
		// TODO Auto-generated constructor stub
	}

	public double byte2Double(byte[] b) {
		long l;
		l = b[7];
		l &= 0xff;
		l |= ((long) b[6] << 8);
		l &= 0xffff;
		l |= ((long) b[5] << 16);
		l &= 0xffffff;
		l |= ((long) b[4] << 24);
		l &= 0xffffffffl;
		l |= ((long) b[3] << 32);
		l &= 0xffffffffffl;
		l |= ((long) b[2] << 40);
		l &= 0xffffffffffffl;
		l |= ((long) b[1] << 48);
		l &= 0xffffffffffffffl;
		l |= ((long) b[0] << 56);
		return Double.longBitsToDouble(l);
	}

	public byte[] double2Byte(double x) {

		long num = Double.doubleToLongBits(x);

		byte[] result = new byte[8];
		result[0] = (byte) (num >>> 56);
		result[1] = (byte) (num >>> 48);
		result[2] = (byte) (num >>> 40);
		result[3] = (byte) (num >>> 32);
		result[4] = (byte) (num >>> 24);
		result[5] = (byte) (num >>> 16);
		result[6] = (byte) (num >>> 8);
		result[7] = (byte) (num);
		return result;

	}

	public void run() {
		/************************************************************************************
		 * TimeStamp is used to compute time using java.util's Calendar class.
		 * TimeStampFormat is used to format the time value so that it can be
		 * easily printed to the terminal.
		 *************************************************************************************/

		Calendar TimeStamp = Calendar.getInstance();
		SimpleDateFormat TimeStampFormat = new SimpleDateFormat(
				"yyyy MM dd::hh:mm:ss:SSS");

		int MeasurementLength = 8; // This is the length of all measurements
									// (including time) in bytes
		int IdLength = 4; // This is the length of IDs in the byte stream

		byte databyte0 = 0; // This is the data byte read from the stream of
							// Lessthan1000
		byte databyte1 = 0; // This is the data byte read from the stream of
							// wildpoints
		byte[] databytes = new byte[72];
		int count = 0;
		int bytesread = 0; // This is the number of bytes read from the stream

		long measurement; // This is the word used to store all measurements -
							// conversions are illustrated.
		int id; // This is the measurement id
		int i; // This is a loop counter

		/*************************************************************
		 * First we announce to the world that we are alive...
		 **************************************************************/

		System.out.print("\n" + this.getName() + "::Sink Reading ");
		BufferedWriter outputWriter = null;
		try{
			outputWriter = new BufferedWriter(new FileWriter("./LessThan10K.dat", true));
		}
		catch (Exception ex){
			System.out.println(ex.toString());
		}
		
		while (true) {
			/*************************************************************
			 * Here we read a byte and write a byte (Lessthan10K)
			 *************************************************************/

			try {
				databyte0 = ReadFilterInputPortC(0);
				databytes[count++] = databyte0;

				bytesread++;

				if (bytesread % 72 == 0) {
					// System.out.println(bytesToHex(databytes));

					byte[] timeArray = Arrays.copyOfRange(databytes, 4, 12);
					byte[] velocityArray = Arrays
							.copyOfRange(databytes, 12, 20);
					byte[] altitudeArray = Arrays
							.copyOfRange(databytes, 20, 28);
					byte[] pressureArray = Arrays
							.copyOfRange(databytes, 28, 36);
					byte[] tempertureArray = Arrays.copyOfRange(databytes, 36,
							44);
					byte[] altitude2Array = Arrays.copyOfRange(databytes, 44,
							52);

					count = 0;
					double time = byte2Double(timeArray);
					double velocity = byte2Double(velocityArray);
					double altitude = byte2Double(altitudeArray);
					double pressure = byte2Double(pressureArray);
					double temperture = byte2Double(tempertureArray);
					double altitude2 = byte2Double(altitude2Array);

					String record = String.valueOf(time) + '\t'
							+ String.valueOf(velocity) + '\t'
							+ String.valueOf(altitude) + '\t'
							+ String.valueOf(pressure) + '\t'
							+ String.valueOf(temperture) + '\t'
							+ String.valueOf(altitude2);
					outputWriter.write(record);
					outputWriter.write("\n");
				}

			} // try

			catch (EndOfStreamException | IOException e) {
				break;
			} // catch

		} // while

		try{
			outputWriter.close();
		}
		catch (Exception ex){
			System.out.println(ex.toString());
		}
		
		
		try{
			outputWriter = new BufferedWriter(new FileWriter("./PressureWildPoints.dat", true));
		}
		catch (Exception ex){
			System.out.println(ex.toString());
		}
		
		while (true) {
			/*************************************************************
			 * Here we read a byte and write a byte (wild points)
			 *************************************************************/
			try {
				databyte1 = ReadFilterInputPortC(1);
				bytesread++;

				if (bytesread % 72 == 0) {

					byte[] timeArray = Arrays.copyOfRange(databytes, 4, 12);
					byte[] velocityArray = Arrays
							.copyOfRange(databytes, 12, 20);
					byte[] altitudeArray = Arrays
							.copyOfRange(databytes, 20, 28);
					byte[] pressureArray = Arrays
							.copyOfRange(databytes, 28, 36);
					byte[] tempertureArray = Arrays.copyOfRange(databytes, 36,
							44);
					byte[] altitude2Array = Arrays.copyOfRange(databytes, 44,
							52);

					count = 0;
					double time = byte2Double(timeArray);
					double velocity = byte2Double(velocityArray);
					double altitude = byte2Double(altitudeArray);
					double pressure = byte2Double(pressureArray);
					double temperture = byte2Double(tempertureArray);
					double altitude2 = byte2Double(altitude2Array);

					String record = String.valueOf(time) + '\t'
							+ String.valueOf(velocity) + '\t'
							+ String.valueOf(altitude) + '\t'
							+ String.valueOf(pressure) + '\t'
							+ String.valueOf(temperture) + '\t'
							+ String.valueOf(altitude2);
					outputWriter.write(record);
					outputWriter.write("\n");
				}
				bytesread++;
				WriteFilterOutputPort(databyte1, 0);

			} // try

			catch (EndOfStreamException | IOException e) {
				ClosePorts();
				break;

			} // catch
		}
		
		try{
			outputWriter.close();
		}
		catch (Exception ex){
			System.out.println(ex.toString());
		}

	} // run

} // SingFilter