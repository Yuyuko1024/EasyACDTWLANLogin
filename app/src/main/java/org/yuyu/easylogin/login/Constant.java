package org.yuyu.easylogin.login;

public class Constant {
    //测试用的用户名和密码，现已用undefined移除,要是需要测试连通性请使用自己的账号
    public static final String USERNAME = "undefined";

    public static final String PASSWORD = "undefined";
    //carrier有unicom,cmcc和telecom，可在测试时自行配置
    public static final String CARRIER = "unicom";
    //咱也不知道这些代表什么，但是咱看login.js这些都是默认值QAQ
    public static final String R1 = "0" ;

    public static final String R2 = "0" ;

    public static final String R3 = "0" ;

    public static final String R6 = "0" ;

    public static final String para = "00" ;

    public static final String OMKKey = "123456" ;

    public static final String CAPTIVE_HTTPS_204_URL = "https://connect.rom.miui.com/generate_204";

    public static final int CAPTIVE_SOCKET_TIMEOUT_MS = 5000;
}
