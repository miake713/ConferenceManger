package com.android.FileBrowser;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * 类说明： 字符串操作类
 * 
 * @author zhaguitao
 * @date 2012-02-20
 * @version 1.0
 */
public class StringUtils {

    public static String GetNameOrderCode(String content) {
        Pattern p = Pattern.compile("\\([0-9]+\\)$");

        Matcher m = p.matcher(content);

        if (m.matches()) {
            return m.group(0).replace("(", "").replace(", newChar)", "");
        } else {
            return "";
        }
    }

    public static boolean IsStartWithLetter(String content) {
        Pattern p = Pattern.compile("^[a-zA-Z]");

        Matcher m = p.matcher(content);

        return m.matches();
    }
    
    
    /**
	 * 对象转整数
	 * 
	 * @param obj
	 * @return 转换异常返回 0
	 */
	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}
	
	/**
	 * 字符串转整数
	 * 
	 * @param str
	 * @param defValue
	 * @return
	 */
	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
		}
		return defValue;
	}
    

    /**
     * 去除左边�?0"
     * 
     * @param input
     * @return String
     */
    public static String trimLeftZero(String input) {
        if (TextUtils.isEmpty(input)) {
            return input;
        }
        while (input.startsWith("0")) {
            if (input.length() > 1) {
                input = input.substring(1);
                continue;
            } else {
                input = "";
                break;
            }
        }
        return input;
    }

    /**
     * 去除右边边的"0"
     * 
     * @param input
     * @return String
     */
    public static String trimRightZero(String input) {
        if (TextUtils.isEmpty(input)) {
            return input;
        }
        while (input.endsWith("0")) {
            if (input.length() > 1) {
                input = input.substring(0, input.length() - 1);
                continue;
            } else {
                input = "";
                break;
            }
        }
        return input;
    }

    /**
     * 将double型数据转换成字符�?
     * 
     * @param value
     * @param scale
     * @return
     */
    public static String parseDouble2Str(Double value, int scale) {
        if (value == null) {
            return "";
        }
        // 格式化成小数点后几位
        String pattern = "##0";
        String endPattern = "";
        if (scale > 0) {
            endPattern += ".";
            for (int i = 0; i < scale; i++) {
                endPattern += "#";
            }
        }

        DecimalFormat fnum = new DecimalFormat(pattern + endPattern);
        return fnum.format(value);
    }

    /**
     * 格式化小数字符串
     * 
     * @param value
     * @param scale
     * @return
     */
    public static String formatNumericString(String value, int scale) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        // 格式化成小数点后几位
        String pattern = "##0";
        String endPattern = "";
        if (scale > 0) {
            endPattern += ".";
            for (int i = 0; i < scale; i++) {
                endPattern += "#";
            }
        }

        DecimalFormat fnum = new DecimalFormat(pattern + endPattern);
        return fnum.format(new BigDecimal(value));
    }

    /**
     * 格式化小数字符串
     * 
     * @param value
     * @param scale
     * @return
     */
    public static String formatDecimalString(BigDecimal value, int scale) {
        if (value == null) {
            return "";
        }
        // 格式化成小数点后几位
        String pattern = "##0";
        String endPattern = "";
        if (scale > 0) {
            endPattern += ".";
            for (int i = 0; i < scale; i++) {
                endPattern += "#";
            }
        }

        DecimalFormat fnum = new DecimalFormat(pattern + endPattern);
        return fnum.format(value);
    }

    /**
     *  * 金额格式�? * @param s 金额  * @param len 小数位数  * @return 格式后的金额  
     */
    public static String consumeFormat(String s, int len) {
        if (s == null || s.length() < 1) {
            return "";
        }
        DecimalFormat formater = null;
        if (len == 0) {
            formater = new DecimalFormat("###,##0");
        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("###,##0.");
            for (int i = 0; i < len; i++) {
                buff.append("0");

                formater = new DecimalFormat(buff.toString());
            }
        }
        return formater.format(new BigDecimal(s));
    }

    /**
     * 格式化数据库特殊字符（转义单引号'�?
     * 
     * @param value
     * @return
     */
    public static String escapeDBChar(String value) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }

        return value.replaceAll("'", "''");
    }
    
	/**
	 * 判断给定字符串是否空白串，空白串是指由空格�?制表符�?回车符�?换行符组成的字符�?br>
	 * 若输入字符串为null或空字符串，返回true
	 * 
	 * @param input
	 * @return boolean
	 */
	public static boolean isBlank(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断字符串是否为empty
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str))
			return true;
		return false;
	}

    /**
     * 调用前先判断字符串是否为�?
     * */
    public static String[] split(String original, String separator) {
        Vector<String> nodes = new Vector<String>();

        // Parse nodes into vector
        int index = original.indexOf(separator);
        while (index >= 0) {
            nodes.addElement(original.substring(0, index));
            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }
        // Get the last node
        nodes.addElement(original);

        // Create splitted string array
        String[] result = new String[nodes.size()];
        if (nodes.size() > 0) {
            for (int loop = 0; loop < nodes.size(); loop++)
                result[loop] = nodes.elementAt(loop);
        }
        return result;
    }
    
    /**
	 * 判断字符串是否超过指定的byte�?
	 * 
	 * @param oriStr
	 *            源字符串
	 * @param maxBytes
	 *            指定byte�?
	 * @return
	 */
	public static boolean isOutofMaxBytes(String oriStr, int maxBytes) {
		if (oriStr == null || oriStr.length() == 0) {
			return false;
		}

		try {
			return oriStr.getBytes("GBK").length > maxBytes;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}



    
    public static boolean isPhoneNumber(String phoneNum) {
    	Pattern p = Pattern.compile("1\\d{10}");
    	Matcher m = p.matcher(phoneNum);
    	return m.matches();
    }
    
    
    public static String TrimLastStr(String str) {

    	return  str.substring(0,str.length()-1);
    }
    
    

    
    
    
}