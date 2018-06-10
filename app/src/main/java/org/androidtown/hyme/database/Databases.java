package org.androidtown.hyme.database;

import android.provider.BaseColumns;

// DataBase Table
public final class Databases {
    public static final class UserDatabaseUtil implements BaseColumns {
        public static final String USER_ID = "user_id";
        public static final String USER_NAME = "user_name";
        public static final String USER_PASSWORD = "user_password";
        public static final String USER_PHONE_NUMBER = "user_phone_number";
        public static final String _TABLE_NAME = "UserDatabase";
        public static final String _CREATE =
                "create table "+_TABLE_NAME+"("
                        +_ID+" integer primary key autoincrement, "
                        +USER_ID+" text not null , "
                        +USER_NAME+" text not null , "
                        +USER_PASSWORD+" text not null , "
                        +USER_PHONE_NUMBER +" text not null  );";
    }

    public static final class ConferenceDatabaseUtil implements BaseColumns {
        public static final String CONFERENCE_NAME = "conference_name";
        public static final String PARTICIPANTS = "participants";
        public static final String START_TIME = "start_time";
        public static final String START_DATE = "start_date";
        public static final String _TABLE_NAME = "ConferenceDatabase";
        public static final String _CREATE =
                "create table "+_TABLE_NAME+"("
                        +_ID+" integer primary key autoincrement, "
                        +CONFERENCE_NAME+" text not null , "
                        +PARTICIPANTS+" text not null , "
                        +START_TIME+" text not null  ,"
                        +START_DATE+" text not null  ); ";
    }

    public static final class SpeechDatabaseUtil implements BaseColumns {
        public static final String CONFERENCE_NAME = "conference_name";
        public static final String USER_NAME = "user_name";
        public static final String SPEECH_TYPE = "speech_type";
        public static final String SPEECH_CONTENT = "speech_content";
        public static final String _TABLE_NAME = "SpeechDatabase";
        public static final String _CREATE =
                "create table "+_TABLE_NAME+"("
                        +_ID+" integer primary key autoincrement, "
                        +CONFERENCE_NAME+" text not null , "
                        +USER_NAME+" text not null , "
                        +SPEECH_TYPE+" text not null , "
                        +SPEECH_CONTENT+" text not null  ); ";

    }
}
