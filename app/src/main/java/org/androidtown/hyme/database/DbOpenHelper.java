package org.androidtown.hyme.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHelper {
    private Context mContext;

    private static final String DATABASE_NAME = "speechDB.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;

    // tag for debugging
    public static final String TAG = "TAG";

    public class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

    private void dropAndCreateTable(SQLiteDatabase db, String tableName){
            // Drop table if exist and create
            String DROP_SQL = "drop table if exists " + tableName;
            db.execSQL(DROP_SQL);
            db.execSQL(Databases.UserDatabaseUtil._CREATE);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // User DB
            String DROP_SQL = "drop table if exists " + Databases.UserDatabaseUtil._TABLE_NAME;
            db.execSQL(DROP_SQL);
            db.execSQL(Databases.UserDatabaseUtil._CREATE);

            DROP_SQL = "drop table if exists " + Databases.ConferenceDatabaseUtil._TABLE_NAME;
            db.execSQL(DROP_SQL);
            db.execSQL(Databases.ConferenceDatabaseUtil._CREATE);

            DROP_SQL = "drop table if exists " + Databases.SpeechDatabaseUtil._TABLE_NAME;
            db.execSQL(DROP_SQL);
            db.execSQL(Databases.SpeechDatabaseUtil._CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Databases.UserDatabaseUtil._TABLE_NAME);
            onCreate(db);
            Log.w(TAG, "Upgrading database from version" + oldVersion + "to " + newVersion + ".");
        }

        @Override
        public synchronized void close() {
            super.close();
        }
    }

    public DbOpenHelper(Context context) {
        this.mContext = context;
    }

    public DbOpenHelper openDB() throws SQLException {
        mDBHelper = new DatabaseHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDB.close();
    }

    public long countUser(){
        long count = DatabaseUtils.queryNumEntries(mDB, Databases.UserDatabaseUtil._TABLE_NAME);
        return count;
    }

    public long countConf(){
        long count = DatabaseUtils.queryNumEntries(mDB, Databases.ConferenceDatabaseUtil._TABLE_NAME);
        return count;
    }

    public long countSpeech(){
        long count = DatabaseUtils.queryNumEntries(mDB, Databases.SpeechDatabaseUtil._TABLE_NAME);
        return count;
    }

    // Insert DB
    public long insertColumn_User(String _userId, String _userName, String _userPassword, String _userPhoneNumber){
        ContentValues values = new ContentValues();
        values.put(Databases.UserDatabaseUtil.USER_ID, _userId);
        values.put(Databases.UserDatabaseUtil.USER_NAME, _userName);
        values.put(Databases.UserDatabaseUtil.USER_PASSWORD, _userPassword);
        values.put(Databases.UserDatabaseUtil.USER_PHONE_NUMBER, _userPhoneNumber);
        return mDB.insert(Databases.UserDatabaseUtil._TABLE_NAME, null, values);
    }

    public long insertColumn_Con(String _conferenceName, String _participants, String _startTime, String _startDate){
        ContentValues values = new ContentValues();
        values.put(Databases.ConferenceDatabaseUtil.CONFERENCE_NAME, _conferenceName);
        values.put(Databases.ConferenceDatabaseUtil.PARTICIPANTS, _participants);
        values.put(Databases.ConferenceDatabaseUtil.START_TIME, _startTime);
        values.put(Databases.ConferenceDatabaseUtil.START_DATE, _startDate);
        return mDB.insert(Databases.ConferenceDatabaseUtil._TABLE_NAME, null, values);
    }

    public long insertColumn_Speech(String _conferenceName, String _userName, String _speechType, String _speechContent){
        ContentValues values = new ContentValues();
        values.put(Databases.SpeechDatabaseUtil.CONFERENCE_NAME, _conferenceName);
        values.put(Databases.SpeechDatabaseUtil.USER_NAME, _userName);
        values.put(Databases.SpeechDatabaseUtil.SPEECH_TYPE, _speechType);
        values.put(Databases.SpeechDatabaseUtil.SPEECH_CONTENT, _speechContent);
        return mDB.insert(Databases.SpeechDatabaseUtil._TABLE_NAME, null, values);
    }

    // Update DB
    public boolean updateColumn_User(String _userId, String _userName, String _userPassword, String _userPhoneNumber){
        ContentValues values = new ContentValues();
        values.put(Databases.UserDatabaseUtil.USER_ID, _userId);
        values.put(Databases.UserDatabaseUtil.USER_NAME, _userName);
        values.put(Databases.UserDatabaseUtil.USER_PASSWORD, _userPassword);
        values.put(Databases.UserDatabaseUtil.USER_PHONE_NUMBER, _userPhoneNumber);
        return mDB.update(Databases.UserDatabaseUtil._TABLE_NAME, values, "USER_ID="+_userId, null)>0;
    }

    public boolean updateColumn_Con(String _conferenceName, String _participants, String _startTime, String _startDate){
        ContentValues values = new ContentValues();
        values.put(Databases.ConferenceDatabaseUtil.CONFERENCE_NAME, _conferenceName);
        values.put(Databases.ConferenceDatabaseUtil.PARTICIPANTS, _participants);
        values.put(Databases.ConferenceDatabaseUtil.START_TIME, _startTime);
        values.put(Databases.ConferenceDatabaseUtil.START_DATE, _startDate);
        return mDB.update(Databases.ConferenceDatabaseUtil._TABLE_NAME, values, "CONFERENCE_NAME="+_conferenceName, null)>0;
    }

    public boolean updateColumn_Speech(long id, String _conferenceName, String _userName, String _speechType, String _speechContent){
        ContentValues values = new ContentValues();
        values.put(Databases.SpeechDatabaseUtil.CONFERENCE_NAME, _conferenceName);
        values.put(Databases.SpeechDatabaseUtil.USER_NAME, _userName);
        values.put(Databases.SpeechDatabaseUtil.SPEECH_TYPE, _speechType);
        values.put(Databases.SpeechDatabaseUtil.SPEECH_CONTENT, _speechContent);
        return mDB.update(Databases.SpeechDatabaseUtil._TABLE_NAME,values, "_ID="+id,null)>0;
    }

    // ID 컬럼 얻어 오기
    public Cursor getColumn_User(String _userName){
        Cursor c = mDB.query(Databases.UserDatabaseUtil._TABLE_NAME, null,
                "USER_NAME="+_userName, null, null, null, null);
        if(c != null && c.getCount() != 0)
            c.moveToFirst();
        return c;
    }
    public Cursor getColumn_User(long _id){
        Cursor c = mDB.query(Databases.UserDatabaseUtil._TABLE_NAME, null,
                "_ID="+_id, null, null, null, null);
        if(c != null && c.getCount() != 0)
            c.moveToFirst();
        return c;
    }
    public Cursor getColumn_Con(String _conName){
        Cursor c = mDB.query(Databases.ConferenceDatabaseUtil._TABLE_NAME, null,
                "CONFERENCE_NAME="+_conName, null, null, null, null);
        if(c != null && c.getCount() != 0)
            c.moveToFirst();
        return c;
    }
    public Cursor getColumn_Con(long _id){
        Cursor c = mDB.query(Databases.ConferenceDatabaseUtil._TABLE_NAME, null,
                "_ID="+_id, null, null, null, null);
        if(c != null && c.getCount() != 0)
            c.moveToFirst();
        return c;
    }
    public Cursor getSpeech_Con(String _speechContent, String _userName){
        Cursor c = mDB.query(Databases.SpeechDatabaseUtil._TABLE_NAME, null,
                "SPEECH_CONTENT="+_speechContent+" ,USER_NAME="+_userName, null, null, null, null);
        if(c != null && c.getCount() != 0)
            c.moveToFirst();
        return c;
    }
    public Cursor getColumn_Speech(long _id){
        Cursor c = mDB.query(Databases.SpeechDatabaseUtil._TABLE_NAME, null,
                "_id="+_id, null, null, null, null);
        if(c != null && c.getCount() != 0)
            c.moveToFirst();
        return c;
    }
}
