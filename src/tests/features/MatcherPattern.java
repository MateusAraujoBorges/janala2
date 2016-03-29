package tests.features;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import janala.Main;

final class ByteWrapper implements CharSequence {
	
	public byte[] backing;
	public int length;
	
	public ByteWrapper(byte[] backing) {
		this(backing, backing.length);
	}
	
	public ByteWrapper(byte[] backing, int length) {
		this.backing = backing;
		this.length = length;
	}
	
	public int length() {
		return length;
	}
	
	public char charAt(int index) {
		return (char) backing[index];
	}
	
	public CharSequence subSequence(int start, int end) {
		throw new UnsupportedOperationException();
	}
}

public class MatcherPattern {

	private static Pattern codesPat = Pattern.compile("[BDHKMNRSVWY]");


	public static void main(String[] args) {
		final int N = 5;
		byte[] buf = new byte[N];
		for (int i = 0; i < N; i++) {
			int bla = Main.readInt(0);
			Main.MakeSymbolic(bla, -128, 127);
			buf[i] = (byte) bla;
		}

		ByteWrapper wrap = new ByteWrapper(buf);
		Matcher m = codesPat.matcher(wrap);
		
		while(m.find()) {
			System.out.println(m.group());
		}
	}

}
