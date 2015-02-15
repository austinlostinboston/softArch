
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

public class MergeSourceFilter extends FilterFramework
{
	public MergeSourceFilter(int inputSourceNum) {
		super(inputSourceNum, 1);
	}

	//final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	
	public void run()
    {
		int bytesread = 0;					// Number of bytes read from the input file.
		int byteswritten = 0;				// Number of bytes written to the stream.
		byte databyte0 = 0;					// The byte of data read from the file A
		byte databyte1 = 0;					// The byte of data read from the file B

		// Next we write a message to the terminal to let the world know we are alive...

		System.out.print( "\n" + this.getName() + "::Merge Reading ");
		byte b0[] = new byte[72];
			/*************************************************************
			*	Here we read a byte and write a byte (SubsetA)
			*************************************************************/
			
			
				for (int i=0;i<InputPortNumbers;i++){
					
					while(true){
						try{
							
							for (int j=0;j<72;j++){
								b0[j] = ReadSingleFilterInputPort(i);
								bytesread++;
		
							}
							for (int j=0;j<72;j++){
								WriteFilterOutputPort(b0[j], 0);
							
								byteswritten++;
							}
							
						}catch (EndOfStreamException e)
						{
							if (i==InputPortNumbers-1)ClosePorts();
							System.out.print( "\n" + this.getName() + "::Merge get SubsetA; bytes read: " + bytesread + " bytes written: " + byteswritten );
							break;
						} 
					}
				}
			

		

		
		
		
		
		//System.out.println(bytesToHex(b0));

   } // run

} // MiddleFilter