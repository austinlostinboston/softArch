import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;


public class Stat {

	int count[]=null;
	long sum[]=null;
	
	int maxC=0;
	int limit=0;
	
	int delay=0;
	String p=null;
	ArrayList<String> ans=new ArrayList<String>();
	
	public Stat(int d, int maxC, String p) {
		delay=d;
		this.p=p;
		this.maxC=maxC;
		count=new int[maxC+1];
		sum=new long[maxC+1];
	}
	
	
	public synchronized void add(int t, int d, int c, long s) throws IOException{
		count[t]+=c;
		sum[t]+=s;
		maxC--;
		if (maxC==0)writeData(p);
	}
	
	public void writeData(String p) throws IOException{
		
		for (int i=0;i<count.length;i++){
			String s="Metric,"+i+","+count[i]+","+sum[i];
			ans.add(s);
		}
		OutputStreamWriter fr = new OutputStreamWriter(new FileOutputStream(p));
		BufferedWriter br = new BufferedWriter(fr);
		
		try {
			for (String s:ans){
				br.write(s);
				br.newLine();
			}
			  
			//return ans;
		}catch(Exception e){
			
		}finally{
			br.close();
		}
	}


	public void addLimit(int i, int j, int count2) {
		String s="Total,"+i+","+j+","+Integer.toString(count2*i);
		ans.add(s);
	}
	
	
}
