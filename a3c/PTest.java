import java.io.IOException;


public class PTest {
	public static void main(String args[]) throws InterruptedException, IOException
	{
		
		int ttt=Integer.parseInt(args[0]);
		int delay=Integer.parseInt(args[1]);
		/////////////////////////////////////////////////////////////////////////////////
		// Get the IP address of the message manager
		/////////////////////////////////////////////////////////////////////////////////
		String MsgMgrIP;
		if (args.length==0){
			MsgMgrIP=null;
		}else{
			MsgMgrIP=args[0];
		}
		MsgMgrIP="192.168.0.2";
		
		//int n=Integer.parseInt(args[1]);
		
		int threads=ttt;
 		Stat stat=new Stat(delay, threads,"f:/17655/a3/test.txt");
		
		
		int count=0;
		int time=12000;
		for (int i=threads;i<=threads;i+=50){
			
				int j=delay;
				count=time/j;
				for (int ts=0;ts<i;ts++){
					new Thread(new PerformanceTestController(MsgMgrIP,ts,j,7,stat,i)).start();
				}
				System.out.println("count="+count);
				for (int ts=0;ts<i;ts++){
					new Thread(new PerformanceTestSensor(MsgMgrIP,ts,count,j)).start();
				}
				System.out.println("count="+count);
				//Thread.sleep(3000);
				stat.addLimit(i,j,count);
				
				//Thread.sleep(ttt);
				
		}
		
		
		//stat.writeData("f:/17655/a3/test.txt");

	} // main
}
