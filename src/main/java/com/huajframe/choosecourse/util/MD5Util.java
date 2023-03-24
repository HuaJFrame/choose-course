package com.huajframe.choosecourse.util;


import org.apache.commons.codec.digest.DigestUtils;

/**
 * md5加密工具类
 *
 * @author Hua JFarmer
 */
public class MD5Util {

    /**
     * 盐
     */
    private static final String SALT = "1a2b3c4d";

    private static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    /**
     * 第一次加密
     * @param inputPass 前端输入的密码
     * @return 加密后的密码
     */
    public static  String inputPassToFormPass(String inputPass){
        //md5加密密码前，先对密码进行处理，按以下salt的规则处理密码
        String str = "" + SALT.charAt(0) + SALT.charAt(3) +
                inputPass + SALT.charAt(2) + SALT.charAt(5);
        return md5(str);
    }

    /**
     * 第二次加密
     * @param formPass 第一次加密后的结果
     * @param salt 盐
     * @return 加密后的存入数据库的密码
     */
    public static String formPassToDBPass(String formPass, String salt){
        String str = "" + salt.charAt(1) + salt.charAt(5) + formPass + salt.charAt(3) + salt.charAt(6);
        return md5(str);
    }

    /**
     * 实际调用的方法，直接包含两次加密
     * @param inputPass 前端输入密码
     * @param salt 盐
     * @return 加密后的存入数据库的密码
     */
    public static String inputPassToDBPass(String inputPass, String salt){
        //第一次加密
        String formPass = inputPassToFormPass(inputPass);
        //第二次加密
        return formPassToDBPass(formPass, salt);
    }

    /**
     * 测试一下
     * @param args
     */
    public static void main(String[] args) {
        //b6f500ed7576bf832f970eb71479667c
        System.out.println(inputPassToFormPass("123456"));
        System.out.println(formPassToDBPass("b6f500ed7576bf832f970eb71479667c", "asdfg1243"));
        System.out.println(inputPassToDBPass("123456", "asdfg1243"));
    }
}
