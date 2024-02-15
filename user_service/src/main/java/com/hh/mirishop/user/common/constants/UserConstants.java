package com.hh.mirishop.user.common.constants;

import java.util.regex.Pattern;

public class UserConstants {

    public final static Pattern EMAIL_REGEX = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
    public static final int USER_PASSWORD_LENGTH = 8;
}
