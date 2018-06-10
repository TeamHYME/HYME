package org.androidtown.hyme;

// Save information about conference here
public class ConferenceInfo {
    private String title;
    private String member;

    public ConferenceInfo(String title, String member){

        this.title = title;
        this.member = member;
    }

    public String getTitle(){
        return title;
    }

    public String getMember(){
        return member;
    }
}
