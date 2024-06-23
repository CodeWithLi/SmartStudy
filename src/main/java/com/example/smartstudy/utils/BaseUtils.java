package com.example.smartstudy.utils;


/**
 * 线程用于存放手机号或身份证
 */
public class BaseUtils {


    public static ThreadLocal<String> threadLocal = new ThreadLocal<>();


    public static void setCurrentAccount(String account) {threadLocal.set(account);}


    public static String getCurrentAccount() {
        return threadLocal.get();
    }


    public static void removeCurrentAccount() {
        threadLocal.remove();
    }


}
