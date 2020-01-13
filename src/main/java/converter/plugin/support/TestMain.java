package converter.plugin.support;

public class TestMain {

	public static void main(String args[]) {

		String s = "\\value1\\";
		System.out.println(s.replace("\"", "\\\""));

		System.out.println(String.valueOf((char) 0x005C));

		String output = new ReplaceHelper().replaceValueAndroid("Test Data with\"");
		System.out.println(output);
	}
}
