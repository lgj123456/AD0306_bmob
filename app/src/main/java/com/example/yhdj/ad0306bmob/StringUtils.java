package com.example.yhdj.ad0306bmob;

/**
 * Created by yhdj on 2017/3/7.
 */

public class StringUtils {
    private static final String USER_NAME_REGEX = "^[a-zA-Z]\\w{2,19}$";
   public static final String STRING_REGZX_PASSWORD = "^[0-9]{3,20}$";
    public static boolean isValidUserName(String username){
        return username.matches(USER_NAME_REGEX);
    }
    public static boolean isValidPassword(String password){
        return password.matches(STRING_REGZX_PASSWORD);
    }
}
