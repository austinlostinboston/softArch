import java.util.ArrayList;

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

public class wildPointFilter extends FilterFramework
{
    wildPointFilter() {
        super(1, 1);
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

        //ArrayList<Byte> data=new ArrayList<Byte>();
        byte[] data=new byte[8];
        while (true)
        {
            /*************************************************************
            *   Here we read a byte and write a byte
            *************************************************************/

            try
            {
                databyte = ReadFilterInputPort(0);
                bytesread++;
                
                if (bytesread%72>=29 && bytesread%72<=36){
                    //29-36
                    data[bytesread%72-29]=databyte;
                    
                    if (bytesread%72==36){
                        double temp=byte2Double(data);
                        //System.out.println("height is: "+temp);
                        temp=temp/3.28083989501;
                        data=double2Byte(temp);
                        
                        for (int i=0;i<8;i++){
                            WriteFilterOutputPort(data[i],0);
                        }
                        
                    }

                }else{
                
                    WriteFilterOutputPort(databyte,0);
                }
                byteswritten++;

            } // try

            catch (EndOfStreamException e)
            {
                ClosePorts();
                System.out.print( "\n" + this.getName() + "::Middle Exiting; bytes read: " + bytesread + " bytes written: " + byteswritten );
                break;

            } // catch

        } // while

   } // run

} // MiddleFilter