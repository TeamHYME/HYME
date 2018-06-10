package org.androidtown.hyme;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.androidtown.hyme.database.DbOpenHelper;

/**
 * Created by Master on 2017-12-12.
 */

public class MeetingRoomActivity extends AppCompatActivity {

    ListView lv_meeting_list;
    RoomList roomList;
    Button bt_go_create;

    UserInfo mUserInfo;

    DbOpenHelper mDBOpenHelper;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_room);
        initView();
    }


    public void initView(){
        Bundle getBundle = new Bundle();
        getBundle = getIntent().getExtras();
        mUserInfo = new UserInfo(getBundle.getString("ID"), getBundle.getString("name"));

        mDBOpenHelper = new DbOpenHelper(this);
        mDBOpenHelper.openDB();

        lv_meeting_list = (ListView) findViewById(R.id.lv_meeting_list);
        roomList = new RoomList(this);
        bt_go_create = (Button) findViewById(R.id.bt_go_create);

        lv_meeting_list.setAdapter(roomList);

        //roomList.addItem("졸업 작품 회의방", mUserInfo.getName() + ", 홍길동, 박동민, 김혜미, 김단우, 김전일", "2017/12/10", "05:30 PM");

        getData();

        lv_meeting_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){
                TextView getMember = (TextView) v.findViewById(R.id.tv_room_member);
                TextView getTitle = (TextView) v.findViewById(R.id.tv_room_title);

                Intent intent = new Intent(MeetingRoomActivity.this, SpeechActivity.class);
                Bundle myBundle = new Bundle();
                myBundle.putString("members", getMember.getText().toString());
                myBundle.putString("title", getTitle.getText().toString());
                intent.putExtras(myBundle);
                startActivity(intent);
            }

        });

        bt_go_create.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MeetingRoomActivity.this, CreateRoomActivity.class);
                Bundle myBundle = new Bundle();
                myBundle.putString("ID", mUserInfo.getID());
                myBundle.putString("name", mUserInfo.getName());
                intent.putExtras(myBundle);
                finish();
                startActivity(intent);
            }
        });
    }

    // get meeting rooms from database
    public void getData(){
        int i=0;

        while((i<=mDBOpenHelper.countConf())){
            mCursor=mDBOpenHelper.getColumn_Con(i);

            if(mCursor.moveToFirst() && mCursor.getCount() >=1 ){
                Log.i("test", mCursor.getString(1));
                roomList.addItem(mCursor.getString(1), mUserInfo.getName() + ", " + mCursor.getString(2), mCursor.getString(4), mCursor.getString(3));
            }

            i++;
        }
    }
}
