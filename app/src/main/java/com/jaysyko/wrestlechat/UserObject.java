package com.jaysyko.wrestlechat;


public class UserObject {
    private static UserObject ourInstance;
    private String name;
    private String userId;
    private String joinDate;
    private boolean isLoggedIn;

    private UserObject(String name, String userId, String joinDate, boolean isLoggedIn) {
        this.userId = userId;
        this.joinDate = joinDate;
        this.isLoggedIn = isLoggedIn;
    }

    public static void setOurInstance(String name, String userId, String joinDate, boolean isLoggedIn) {
        if (ourInstance == null) {
            ourInstance = new UserObject(name, userId, joinDate, isLoggedIn);
        }
    }

    public static UserObject getInstance() {
        return ourInstance;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public String getName() {
        return name;
    }


    public String getUserId() {
        return userId;
    }

    public String getJoinDate() {
        return joinDate;
    }

}
