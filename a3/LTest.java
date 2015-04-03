
public class LTest {

	public static void main(String[] args) {
		long t=System.currentTimeMillis();
		String s=String.valueOf(t);
		System.out.println(s);
		t=Long.parseLong(s);
		System.out.print(t);
	}
}
