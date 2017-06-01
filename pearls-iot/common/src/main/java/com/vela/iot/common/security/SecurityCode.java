package com.vela.iot.common.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.Base64.Encoder;
import java.util.UUID;

import com.vela.iot.common.Exception400;

public class SecurityCode {
	static Random random = new Random();
	static MessageDigest md;
	static MessageDigest sha256;
	static int min = 100000;
	static {
		try {
			md = MessageDigest.getInstance("MD5");
			sha256 = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {

		}
	}

	public enum SecurityCodeLevel {
		Simple, Medium, Hard
	};

	// url safe
	// httpurl = "http://" hostport [ "/" hpath [ "?" search ]]
	// hpath = hsegment *[ "/" hsegment ]
	// hsegment = *[ uchar | ";" | ":" | "@" | "&" | "=" ]
	// uchar = unreserved | escape
	// unreserved = alpha | digit | safe | extra
	// alpha = lowalpha | hialpha
	// lowalpha = "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" | "j" |
	// "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r" | "s" | "t" | "u" | "v" |
	// "w" | "x" | "y" | "z"
	// hialpha = "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | "I" | "J" | "K"
	// | "L" | "M" | "N" | "O" | "P" | "Q" | "R" | "S" | "T" | "U" | "V" | "W" |
	// "X" | "Y" | "Z"
	// digit = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9"
	// safe = "$" | "-" | "_" | "." | "+"
	// extra = "!" | "*" | "'" | "(" | ")" | ","
	// escape = "%" hex hex
	// hex = digit | "A" | "B" | "C" | "D" | "E" | "F" | "a" | "b" | "c" | "d" |
	// "e" | "f"

	private static final char[] CHAR_CODE = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
			'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
			'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
			'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
			'W', 'X', 'Y', 'Z', '-', '_' };

	public static String getSecurityCode() {
		return getSecurityCode(8, SecurityCodeLevel.Medium, true);
	}

	/**
	 * 随机生成6位数.
	 * 
	 * @return
	 */
	public static String randomSixPwd() {
		return Integer.toString(min + random.nextInt(900000));
		// StringBuilder sb = new StringBuilder();
		// //sb.append((int) (Math.random() * 9 + 1));
		// sb.append(random.nextInt(10));
		// //for (int i = 0; i < 5; i++) {
		// //sb.append((int) (Math.random() * 10));
		// sb.append(random.nextInt(900000));
		// //}
		// return sb.toString();
	}

	public static String getSecurityCode(final int len,
			SecurityCodeLevel level, boolean canRepeat) {
		char[] code;
		// 根据不同的难度截取字符数组
		switch (level) {
		case Simple: {
			code = Arrays.copyOfRange(CHAR_CODE, 0, 9);
			break;
		}
		case Medium: {
			code = Arrays.copyOfRange(CHAR_CODE, 0, 36);
			break;
		}
		case Hard: {
			code = Arrays.copyOfRange(CHAR_CODE, 0, CHAR_CODE.length);
			break;
		}
		default: {
			code = Arrays.copyOfRange(CHAR_CODE, 0, CHAR_CODE.length);
		}
		}

		// 字符集合长度
		int n = code.length;
		// 抛出运行时异常
		if (len > n && !canRepeat) {
			throw new Exception400("ALPHA_NOT_ENOUGH", String.format(
					"调用SecurityCode.getSecurityCode(%1$s,%2$s,%3$s)出现异常，"
							+ "当canRepeat为%3$s时，传入参数%1$s不能大于%4$s", len, level,
					canRepeat, n));
		}
		// 存放抽取出来的字符
		char[] result = new char[len];
		// 判断能否出现重复的字符
		if (canRepeat) {
			for (int i = 0; i < result.length; i++) {
				// 索引 0 and n-1
				int r = random.nextInt(n);
				// 将result中的第i个元素设置为codes[r]存放的数值
				result[i] = code[r];
			}
		} else {
			for (int i = 0; i < result.length; i++) {
				// 索引 0 and n-1
				int r = random.nextInt(n);
				// 将result中的第i个元素设置为codes[r]存放的数值
				result[i] = code[r];

				// 必须确保不会再次抽取到那个字符，因为所有抽取的字符必须不相同。
				// 因此，这里用数组中的最后一个字符改写codes[r]，并将n减1
				code[r] = code[n - 1];
				n--;
			}
		}
		return new String(result);
	}

	/**
	 * @param deviceKey
	 * @return
	 */
	public static boolean isValidPwd(String pwd) {
		// 这里只验证了是否有重复字符串,没有重复字符串为有效
		boolean duplicate = false;
		char[] chars = pwd.toCharArray();
		for (int i = 0; i < chars.length - 1; i++) {
			for (int j = i + 1; j < chars.length - 1; j++) {
				if (chars[i] == chars[j]) {
					duplicate = true;
				}
			}
		}
		return !duplicate;
	}

	public static String byte2hex(byte[] b) {
		int length = b.length;
		char[] out = new char[length << 1];
		for (int i = 0, j = 0; i < length; i++) {
			out[j++] = CHAR_CODE[b[i] >>> 4 & 0xf];
			out[j++] = CHAR_CODE[b[i] & 0xf];
		}
		String hex = new String(out);
		return hex;
	}

	public static String hexTo64(String hex) {
		StringBuffer r = new StringBuffer();
		int index = 0;
		int[] buff = new int[3];
		int l = hex.length();
		for (int i = 0; i < l; i++) {
			index = i % 3;
			buff[index] = Integer.parseInt("" + hex.charAt(i), 16);
			if (index == 2) {
				r.append(CHAR_CODE[buff[0] << 2 | buff[1] >>> 2]);
				r.append(CHAR_CODE[(buff[1] & 3) << 4 | buff[2]]);
			}
		}
		return r.toString();
	}

	/**
	 * @return
	 * @deprecated by SecurityCode.getUUIDbyLong, getUUIDbyLong is faster than
	 *             getUUIDbyStr by 20 times;
	 */
	@Deprecated
	public static String getUUIDbyStr() {
		/*
		 * UUID是把128个二进制数，转换成32个16进制数的，每4个二进制数转换成一个16进制数。
		 * 如果是64（2的6次方）进制的话，应该是6个二进制数转换一个64进制数。
		 * 我们可以在UUID前面补加一个16进行数，让它成为33位的16进制数，共是132位二进制数。
		 * 这样就可以用22个64进制数表示132（22*6）位的二进制数。
		 * 
		 * 结论是，可以把36位的UUID，去掉“-”变成32位的16进制数。
		 * 在这个数前面补一个16进制数，比如“0”，就变成了33位（132个二进制数，33*4）。
		 * 再把这个33位的16进制数，转换成22位的64进制数。 5.4445178707350154154139937189083e+39
		 * 16^33=2^4^33 5.4445178707350154154139937189083e+39 64^22=2^6^22
		 */
		StringBuilder sb = new StringBuilder("0");
		String uuid = UUID.randomUUID().toString();
		uuid = uuid.replaceAll("-", "").toUpperCase();
		sb.append(uuid);
		uuid = hexTo64(sb.toString());
		// System.out.println(uuid);

		return uuid;
	}

	public static String getUUIDbyLong() {
		UUID uuid = UUID.randomUUID();
		long msb = uuid.getMostSignificantBits();
		long lsb = uuid.getLeastSignificantBits();
		byte[] buffer = new byte[16];
		for (int i = 0; i < 8; i++) {
			buffer[i] = (byte) ((msb >>> 8 * (7 - i)) & 0xFF);
			buffer[i + 8] = (byte) ((lsb >>> 8 * (7 - i)) & 0xFF);
		}
		Encoder ec = Base64.getUrlEncoder();
		String uuidBase64 = ec.encodeToString(buffer);
		// 删除编码结束标志==
		// System.out.println(uuidBase64.substring(0, uuidBase64.length()-2));
		return uuidBase64.substring(0, uuidBase64.length() - 2);
	}

	/**
	 * 判断string2MD5(salt,inStr) 是否等于target
	 * @param target
	 * @param salt
	 * @param pw
	 * @return
	 */
	public static boolean md5Equals(String target, String salt, String pw) {
		return string2MD5(salt, pw).equals(target);
	}

	public static String string2MD5(String salt, String inStr) {
		return string2MD5(salt + inStr);
	}

	/**
	 * 他们的定论：MD5 算法不该再被用于任何软件完整性查看或代码签名的用处！
	 * 那是不是MD5就此没有用处了呢？非也，对于文件来说碰撞可能容易，但是对于限定长度的密码或者密文来说
	 * ，MD5作为一种高性能高安全的数字签名算法来说，还是非常实用的。
	 * 
	 * @param inStr
	 * @return
	 */
	public static String string2MD5(String inStr) {
		byte[] bytes;
		try {
			bytes = md.digest(inStr.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return byte2hex(bytes);
	}
	
	public static boolean sha256Equals(String target, String salt, String pw) {
		return string2SHA256(salt,pw).equals(target);
	}

	public static String string2SHA256(String salt, String inStr) {
		return string2SHA256(salt + inStr);
	}

	public static String string2SHA256(String inStr) {
		byte[] bytes;
		try {
			bytes = sha256.digest(inStr.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return byte2hex(bytes);
	}

	public static void main(String[] args) {
		System.out.println(SecurityCode.string2MD5("111111"));
		System.out.println(SecurityCode.string2SHA256("111111"));
		// System.out.println(SecurityCode.getSecurityCode(8,
		// SecurityCodeLevel.Hard, false)) ;
	}
}
