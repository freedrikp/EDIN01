package project2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
import java.util.HashSet;

public class PIN {
	private static byte out2;
	private static byte out5;

	private static void click2(byte[] fsr) {
		// byte toZero = (byte) (fsr[2] ^ 1 & fsr[1] ^ 1 & fsr[0] ^ 1 & fsr[3]);
		// byte fromZero = (byte) (fsr[2] ^ 1 & fsr[1] ^ 1 & fsr[0] ^ 1 & fsr[3]
		// ^ 1);
		byte extraFeedback = (byte) ((fsr[2] == 0 && fsr[1] == 0 && fsr[0] == 0) ? 1
				: 0);
		byte in = (byte) ((fsr[3] + fsr[0] + extraFeedback) % 2);
		out2 = fsr[3];
		for (int i = fsr.length - 1; i > 0; i--) {
			fsr[i] = fsr[i - 1];
		}
		fsr[0] = in;
	}

	private static void click5(byte[] fsr) {
		byte toZero = (byte) ((fsr[2] == 0 && fsr[1] == 0 && fsr[0] == 0 && fsr[3] == 1) ? 2
				: 0);
		byte fromZero = (byte) ((fsr[2] == 0 && fsr[1] == 0 && fsr[0] == 0 && fsr[3] == 0) ? 3
				: 0);
		byte in = (byte) ((3 * fsr[3] + 3 * fsr[1] + 4 * fsr[0] + toZero + fromZero) % 5);
		out5 = fsr[3];
		for (int i = fsr.length - 1; i > 0; i--) {
			fsr[i] = fsr[i - 1];
		}
		fsr[0] = in;
	}

	private static void print(byte[] fsr) {
		for (int i = fsr.length - 1; i >= 0; i--) {
			System.out.print(fsr[i]);
		}
		System.out.println();
	}

	private static String toString(byte[] fsr) {
		String s = "";
		for (int i = fsr.length - 1; i >= 0; i--) {
			s += fsr[i];
		}
		return s;
	}

	public static void main(String[] args) {
		byte[] fsr2 = { 0, 0, 0, 0 };
		byte[] fsr5 = { 0, 0, 0, 0 };
		out2 = 0;
		out5 = 0;
		// print(fsr);
		HashSet<String> set = new HashSet<String>();
		for (int i = 0; i < 625; i++) {
			click5(fsr5);

			String s = toString(fsr5);
			if (set.contains(s)) {
				System.out.println("Already visited: " + s);
			} else {
				set.add(s);
			}
		}
		System.out.println("Number of stated visited: " + set.size());

		set = new HashSet<String>();
		for (int i = 0; i < 16; i++) {
			click2(fsr2);

			String s = toString(fsr2);
			if (set.contains(s)) {
				System.out.println("Already visited: " + s);
			} else {
				set.add(s);
			}
		}
		System.out.println("Number of stated visited: " + set.size());
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream("out.txt"));

			for (int i = 0; i < 10003; i++) {
				click2(fsr2);
				click5(fsr5);
				if (out2 == 1) {
					pw.print(out5 + 5);
				} else {
					pw.print(out5);
				}

			}
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println(new File(".").getAbsolutePath());
		Process p;
		try {
			ProcessBuilder pb = new ProcessBuilder("gcc", "-o", "verify" ,"Check_LE4.c");
//			ProcessBuilder pb = new ProcessBuilder("ls");
			pb.inheritIO();
			p = pb.start();
			p.waitFor();

			pb = new ProcessBuilder("./verify");
			pb.redirectOutput(Redirect.INHERIT);

			p = pb.start();
			PrintWriter pw = new PrintWriter(p.getOutputStream());
			pw.println("out.txt");
			pw.flush();
			pw.close();
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}
