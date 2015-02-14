
/******************************************************************************************************************
* File:MergeFilter.java
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
	public MergeFilter() {
		super(2, 1);
	}

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public void run()
    {
		int bytesread = 0;					// Number of bytes read from the input file.
		int byteswritten = 0;				// Number of bytes written to the stream.
		byte databyte0 = 0;					// The byte of data read from the file A
		byte databyte1 = 0;					// The byte of data read from the file B

		// Next we write a message to the terminal to let the world know we are alive...

		System.out.print( "\n" + this.getName() + "::Merge Reading ");
		byte b0[] = new byte[8640];
		
		while (true)
		{
			/*************************************************************
			*	Here we read a byte and write a byte (SubsetA)
			*************************************************************/
			
			try
			{
				databyte0 = ReadFilterInputPortC(0);
				b0[bytesread] = databyte0;

				bytesread++;
				WriteFilterOutputPort(databyte0, 0);
				byteswritten++;

			} // try

			catch (EndOfStreamException e)
			{
				System.out.print( "\n" + this.getName() + "::Merge get SubsetA; bytes read: " + bytesread + " bytes written: " + byteswritten );
				break;
			} // catch

		} // while
		
		while(true)
		{
			/*************************************************************
			*	Here we read a byte and write a byte (SubsetB)
			*************************************************************/
			try
			{				
				databyte1 = ReadFilterInputPortC(1);
				b0[bytesread] = databyte1;

				bytesread++;
				WriteFilterOutputPort(databyte1, 0);

				byteswritten++;

			} // try

			catch (EndOfStreamException e)
			{
				ClosePorts();
				System.out.print( "\n" + this.getName() + "::Merge get SubsetA and SubsetB; bytes read: " + bytesread + " bytes written: " + byteswritten );
				break;

			} // catch
		}
		
		//System.out.println(bytesToHex(b0));

   } // run

} // MiddleFilter