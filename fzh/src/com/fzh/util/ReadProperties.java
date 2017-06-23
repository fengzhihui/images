package com.fzh.util;

import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {
    static private String driver = null;
    static private String url = null;
    static private String username = null;
    static private String password = null;
    static{
        loads();
    }
    synchronized static public void loads(){
        if(driver == null || url == null || username == null || password == null){
            InputStream is = ReadProperties.class.getResourceAsStream("/jdbc.properties");
            Properties dbproperties = new Properties();
            try {
                dbproperties.load(is);
                url = dbproperties.getProperty("url").toString();
                driver = dbproperties.getProperty("driver").toString();
                username = dbproperties.getProperty("username").toString();
                password = dbproperties.getProperty("password").toString();
                System.out.println(url);
                System.out.println(driver);
                System.out.println(username);
                System.out.println(password);
            }
            catch (Exception e) {
                System.err.println("不能读取属性文件. " + "请确保jdbc.properties在CLASSPATH指定的路径中");
            }
        }
    }
      
    public static String getDriver() {
        if(driver == null)
            loads();
        return driver;
    }
     
    public static String getUrl() {
        if(url == null)
            loads();
        return url;
    }
     
    public static String getUsername() {
        if(username == null)
            loads();
        return username;
    }
     
    public static String getPassword() {
        if(password==null)
            loads();
        return password;
    }
  
}
