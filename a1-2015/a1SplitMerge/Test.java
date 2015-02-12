
public class Test {

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
	
	
	public static void main(String[] args) {
		Test p=new Test();
		double a=12213.222222;
		byte[] b=p.double2Byte(a);
		for (int i=0;i<8;i++){
			System.out.println(b[i]);
		}
		
		System.out.println(p.byte2Double(b));
	}
}
