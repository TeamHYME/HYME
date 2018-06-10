package org.androidtown.hyme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by Master on 2017-12-06.
 */

public class ParticipantActivity extends AppCompatActivity {

    /* Get list */
    ParticipantList participantList;
    LogList logList;
    ListView lv_participant_list;
    ListView lv_participant_log;


    Button bt_temp_go_meeting;
    Button bt_go_previous;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant);
        initView();
    }

    public void initView(){
        Bundle getBundle = new Bundle();
        getBundle = getIntent().getExtras();
        String data = getBundle.getString("data");
        int type = getBundle.getInt("type");
        String getType="";

        lv_participant_list = (ListView) findViewById(R.id.lv_participant_list);
        lv_participant_log = (ListView) findViewById(R.id.lv_participant_log);

        participantList = new ParticipantList(this);
        logList = new LogList(this);

        /* Get data */
        // getData();

        switch(type){
            case 1:
                getType="의견";
                break;
            case 2:
                getType="추가";
                break;
            case 3:
                getType="질문";
                break;
            case 4:
                getType="답변";
                break;
        }


        lv_participant_list.setAdapter(participantList);
        lv_participant_log.setAdapter(logList);

        /* Temporal setting */
        participantList.addItem(1, "홍길동", "★");
        participantList.addItem(2, "박동민", "");
        participantList.addItem(3, "김혜미", "");
        participantList.addItem(4, "김단우", "");
        participantList.addItem(5, "김전일", "");

        /* Temporal setting */
        logList.addItem(1, "홍길동", getType, data);
        logList.addItem(2, "김혜미", "추가", "그와 관련된 보도 자료가 있습니다. 다음을 참고하시면...");
        logList.addItem(3, "김단우", "질문", "외국에서는 우리나라의 효도법과 비슷한 제도가...");
        logList.addItem(4, "박동민", "의견", "홍길동씨와 다르게, 저는 부양의무에 대해서 이렇게 생각하는데...");



        /* Temporal button */
        bt_temp_go_meeting = (Button) findViewById(R.id.bt_temp_go_meeting);
        bt_temp_go_meeting.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(ParticipantActivity.this, SpeechActivity.class);
                startActivity(intent);
            }
        });

        bt_go_previous = (Button) findViewById(R.id.bt_go_previous);
        bt_go_previous.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                finish();
            }
        });

    }
}
