package cn.com.larunda.monitor.gson;

/**
 * Created by sddt on 18-1-11.
 */

public class UserToken {
    public String token;
    public String message;
    public UnitInfo unit;
    public ConnectInfo connect_info;

    public class ConnectInfo {
        public String user;
        public String phone;
    }
}
