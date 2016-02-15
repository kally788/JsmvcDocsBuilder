package com.jsmvc.utils;

/**   
 * @title: MD5加密
 * @author zhujili  
 * @contact 280000280@qq.com  
 * @date 2015-12-30 上午1:04:38 
 *
 */
public class Md5 {
	/**
	 * 对byte进行MD5
	 * @param source
	 * @return
	 */
	public static String getMD5(byte[] source) {
		String s = null;
		char hexDigits[] = {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		try {
			java.security.MessageDigest md = java.security.MessageDigest
					.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest();
			char str[] = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	/**
	 * 对字符串进行MD5
	 * @param str
	 * @return
	 */
	public static String getMD5(String str) {
		return getMD5(str.getBytes());
	}
}
