package org.androidtown.hyme;

// Save information about user here
public class UserInfo {

    private String ID;
    private String name;

    public UserInfo(String ID, String name){

        this.ID = ID;
        this.name = name;
    }

    public String getID(){
        return ID;
    }

    public String getName(){
        return name;
    }
}
