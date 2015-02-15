import java.util.ArrayList;
import java.util.Arrays;

/******************************************************************************************************************
* File:MiddleFilter.java
* Course: 17655
* Project: Assignment 1
* Copyright: Copyright (c) 2003 Carnegie Mellon University
* Versions:
*   1.0 November 2008 - Sample Pipe and Filter code (ajl).
*
* Description:
*
* This class serves as an example for how to use the FilterRemplate to create a standard filter. This particular
* example is a simple "pass-through" filter that reads data from the filter's input port and writes data out the
* filter's output port.
*
* Parameters:       None
*
* Internal Methods: None
*
******************************************************************************************************************/

public class WildPointFilter extends FilterFramework
{
    WildPointFilter() {
        super(1, 2);
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
    
    public void run()
    {


        int bytesread = 0;                  // Number of bytes read from the input file.
        int byteswritten = 0;               // Number of bytes written to the stream.
        byte databyte = 0;                  // The byte of data read from the file

        // Next we write a message to the terminal to let the world know we are alive...

        System.out.print( "\n" + this.getName() + "::Middle Reading ");

        double lastValidPress=-1;
        double nextValidPress=-1;
        ArrayList<TempData> curErrorData=new ArrayList<TempData>();
        //ArrayList<Byte> data=new ArrayList<Byte>();
        byte[] data=new byte[8];
        byte[] fullData=new byte[72];
        boolean allowToPass=false;
        double pressure=-1;
        
        while (true)
        {
            /*************************************************************
            *   Here we read a byte and write a byte
            *************************************************************/
            try
            {
                databyte = ReadFilterInputPort(0);
                bytesread++;
                
                fullData[(bytesread-1)%72]=databyte;
                
                if (bytesread % 72 == 48){
                	pressure=byte2Double(Arrays.copyOfRange(fullData, 40, 48));
                    
                    if (pressure < 0) {
                    	System.out.println(pressure);
                    	allowToPass=false;
                    } else {
                    	allowToPass=true;
                    }
                 
                }
                
                if (bytesread%72==0){
                	if (!allowToPass){
                		curErrorData.add(new TempData(pressure,Arrays.copyOf(fullData,72)));
                	}else{
                		outPutCorrectPressure(curErrorData,lastValidPress,pressure);
                		curErrorData.clear();
                		lastValidPress=pressure;
                		for (int i=0;i<72;i++){
                			WriteFilterOutputPort(fullData[i], 0);
                		}
                		
                	}
                }
                
                
                
                byteswritten++;

            } // try

            catch (EndOfStreamException e)
            {
            	outPutCorrectPressure(curErrorData,lastValidPress,-1);
            	
                ClosePorts();
                System.out.print( "\n" + this.getName() + "::Middle Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten );
                break;

            } // catch

        } // while

   } // run

	private void outPutCorrectPressure(ArrayList<TempData> curErrorData,
			double lastValidPress, double pressure) {
		if (!curErrorData.isEmpty()){
    		double correctPress=0;
    		if (lastValidPress<0){
    			correctPress=pressure;
    		}else if (pressure<0){
    			correctPress=lastValidPress;
    		}else{
    			correctPress=(pressure+lastValidPress)/2;
    		}
		
    		byte[] correctByte=double2Byte(correctPress);
    		for (int i=0;i<curErrorData.size();i++){
    			byte[] t=curErrorData.get(i).value;
    			for (int j=0;j<72;j++){
    				if (j>=40 && j<48){
    					WriteFilterOutputPort(correctByte[j-40], 0);
    					WriteFilterOutputPort(t[j], 1);
    				}else{
    					WriteFilterOutputPort(t[j], 0);
    					WriteFilterOutputPort(t[j], 1);
    				}
    			}
    		}
		}
		
	}

} // MiddleFilter
class TempData{
	double key;
	byte[] value;
	public TempData(double k, byte[] v) {
		value=v;
		key=k;
	}
}
