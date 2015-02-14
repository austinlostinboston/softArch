
import java.util.ArrayList;

/******************************************************************************************************************
* File:MiddleFilter.java
* Course: 17655
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*	1.0 November 2008 - Sample Pipe and Filter code (ajl).
*
* Description:
*
* This class serves as an example for how to use the FilterRemplate to create a standard filter. This particular
* example is a simple "pass-through" filter that reads data from the filter's input port and writes data out the
* filter's output port.
*
* Parameters: 		None
*
* Internal Methods: None
*
******************************************************************************************************************/

public class MergeFilter extends FilterFramework
{
	public MergeFilter(int InputPortNumbers) throws Exception {
		super(InputPortNumbers,1);
		//TODO
		if (InputPortNumbers<3){
			throw new Exception();
		}
		
	}

	public void run()
    {


		int bytesread = 0;					// Number of bytes read from the input file.
		int byteswritten = 0;				// Number of bytes written to the stream.
		//byte databyte = 0;					// The byte of data read from the file

		// Next we write a message to the terminal to let the world know we are alive...

		System.out.print( "\n" + this.getName() + "::Middle Reading ");

		//ArrayList<Byte> data=new ArrayList<Byte>();
		byte[] databyte=new byte[InputPortNumbers];
		while (true)
		{
			/*************************************************************
			*	Here we read a byte and write a byte
			*************************************************************/

			try
			{
				byte outputByte=0;
				for (int i=0;i<InputPortNumbers;i++){
				
					databyte[i] = ReadFilterInputPort(i);
					outputByte=(byte) (outputByte^databyte[i]);
				}
				
				if (InputPortNumbers%2==0){
					
					if ((databyte[0]^databyte[1])==0){
						outputByte=(byte) (outputByte^databyte[0]);
					}else{
						outputByte=(byte) (outputByte^databyte[2]);
					}
					
				}
				WriteFilterOutputPort(outputByte, 0);
				
			} 

			catch (EndOfStreamException e)
			{
				ClosePorts();
				System.out.print( "\n" + this.getName() + "::Middle Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten );
				break;

			} // catch

		} // while

   } // run

} // MiddleFilter