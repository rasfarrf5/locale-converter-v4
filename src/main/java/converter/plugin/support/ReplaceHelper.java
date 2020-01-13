package converter.plugin.support;

public class ReplaceHelper {

	String backSlash = String.valueOf((char) 0x005C);

	public String replaceValueAndroid(String value) {
		String formattedString = value
				.replace("\n", "\\n")
				.replace("&", "&amp;")
				.replace("'", "\\'")
				.replace("\\", backSlash)
				.replace("<power>, <screen>, <bed>, <seat value> seat.", "%1$s")
				.replace("<power>、<screen>、<bed>、<seat value> 座位。", "%1$s")
				.replace("<US customs website link", "")
				.replace("<hh:mm>, <D(D) Mmm YYYY>", "%1$s")
				.replace("•", "\\u2022")
				.replace("<number>", "%d")
				.replace("<number 2>", "%2$d")
				.replace("<number 3>", "%3$d")
				.replace("<number_decimal_2>", "%.2f")
				.replace("<value>", "%s")
				.replace("<value 1>", "%1$s")
				.replace("<value 2>", "%2$s")
				.replace("<value 3>", "%3$s")
				.replace("<value 4>", "%4$s")
				.replace("<number 1>", "%1$d")
				.replace("<value 1> à <value 2>", "%1$s à %2$s")
				.replace("<value int>", "%s").trim();
		return formattedString.trim();
	}

	public String replaceValueForIos(String value) {
		return value
				.replace("\n", "\\n")
				.replace("'", "\\'")
				.replace("<power>, <screen>, <bed>, <seat value> seat.", "%@")
				.replace("<power>、<screen>、<bed>、<seat value> 座位。", "%@")
				.replace("<US customs website link", "")
				.replace("<hh:mm>, <D(D) Mmm YYYY>", "%@")
				.replace("•", "\\u2022")
				.replace("<number>", "%@")
				.replace("<number 2>", "%@")
				.replace("<number 3>", "%@")
				.replace("<number_decimal_2>", "%@")
				.replace("<value>", "%@")
				.replace("<value 1>", "%@")
				.replace("<value 2>", "%@")
				.replace("<value 3>", "%@")
				.replace("<value 4>", "%@")
				.replace("<number 1>", "%@")
				.replace("<value 1> à <value 2>", "%@ à %@")
				.replace("<value int>", "%@").trim();
	}
}
