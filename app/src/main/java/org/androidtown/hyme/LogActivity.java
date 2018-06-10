package org.androidtown.hyme;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import org.androidtown.hyme.database.DbOpenHelper;

/**
 * Created by Master on 2017-12-11.
 */

public class LogActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    LogList logList;
    Button bt_share;
    CheckBox cb_type_opinion;
    CheckBox cb_type_additional;
    CheckBox cb_type_ask;
    CheckBox cb_type_answer;
    CheckBox cb_type_refute;
    CheckBox cb_type_etc;
    ListView lv_participant_log;
    Button bt_go_previous;

    ConferenceInfo mConferInfo;
    int countTable = 0;

    DbOpenHelper mDBOpenHelper;
    Cursor mCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        initDatabase();
        initView();
    }

    public void initDatabase(){
        mDBOpenHelper = new DbOpenHelper(this);
        mDBOpenHelper.openDB();
    }

    public void initView() {
        Bundle getBundle = new Bundle();
        getBundle = getIntent().getExtras();
        mConferInfo = new ConferenceInfo(getBundle.getString("title"), getBundle.getString("members"));

        bt_share = (Button) findViewById(R.id.bt_share);
        bt_go_previous = (Button) findViewById(R.id.bt_go_previous);
        lv_participant_log = (ListView) findViewById(R.id.lv_participant_log);

        cb_type_opinion = (CheckBox) findViewById(R.id.cb_type_opinion);
        cb_type_additional = (CheckBox) findViewById(R.id.cb_type_additional);
        cb_type_ask = (CheckBox) findViewById(R.id.cb_type_ask);
        cb_type_answer = (CheckBox) findViewById(R.id.cb_type_answer);
        cb_type_etc = (CheckBox) findViewById(R.id.cb_type_etc);
        cb_type_refute = (CheckBox) findViewById(R.id.cb_type_refute);

        logList = new LogList(this);

        /* Get Data */
        getData();

        /* Temporal setting */
        //logList.addItem(countTable, "김혜미", "추가", "그와 관련된 보도 자료가 있습니다. 다음을 참고하시면...");
        //logList.addItem(countTable+1, "김단우", "질문", "외국에서는 우리나라의 효도법과 비슷한 제도가...");
        //logList.addItem(countTable+2, "박동민", "의견", "홍길동씨와 다르게, 저는 부양의무에 대해서 이렇게 생각하는데...");

        lv_participant_log.setAdapter(logList);

        cb_type_opinion.setOnCheckedChangeListener(this);
        cb_type_additional.setOnCheckedChangeListener(this);
        cb_type_ask.setOnCheckedChangeListener(this);
        cb_type_answer.setOnCheckedChangeListener(this);
        cb_type_etc.setOnCheckedChangeListener(this);
        cb_type_refute.setOnCheckedChangeListener(this);

        /*
        bt_share.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LogActivity.this, DownloadActivity.class);
                startActivity(intent);
            }
        });
        */

        bt_go_previous.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        // Get the specific speech information
        lv_participant_log.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView temp_name = (TextView) view.findViewById(R.id.tv_log_name);
                TextView temp_type = (TextView) view.findViewById(R.id.tv_log_type);
                TextView temp_content = (TextView) view.findViewById(R.id.tv_log_content);

                // Get original content
                String originalContent = getRealContent(temp_name.getText().toString(), temp_type.getText().toString(), temp_content.getText().toString());

                Bundle myBundle = new Bundle();
                myBundle.putString("title", mConferInfo.getTitle());
                myBundle.putString("speaker", temp_name.getText().toString());
                myBundle.putString("type", temp_type.getText().toString());
                myBundle.putString("content", originalContent);
                Intent intent = new Intent(LogActivity.this, SpeechDetailActivity.class);
                intent.putExtras(myBundle);
                finish();
                startActivity(intent);
            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (cb_type_opinion.isChecked()) {
            checkView(1, true);
        } else {
            checkView(1, false);
        }

        if (cb_type_ask.isChecked()) {
            checkView(2, true);
        } else {
            checkView(2, false);
        }

        if (cb_type_answer.isChecked()) {
            checkView(3, true);
        } else {
            checkView(3, false);
        }

        if(cb_type_refute.isChecked()){
            checkView(4, true);
        } else {
            checkView(4, false);
        }


        if (cb_type_additional.isChecked()) {
            checkView(5, true);
        } else {
            checkView(5, false);
        }

        if(cb_type_etc.isChecked()){
            checkView(6, true);
        } else {
            checkView(6, false);
        }

    }

    private void checkView(int check, boolean isVisible) {

        for (int i = 0; i < lv_participant_log.getCount(); i++) {
            Log.i("aaa", String.valueOf(lv_participant_log.getCount()));

            if (isVisible) {
                switch (check) {
                    case 1:
                        if(logList.printType(i).equals(cb_type_opinion.getText().toString())) {
                            Log.i("aaa", "right");
                            lv_participant_log.getChildAt(i).setVisibility(View.VISIBLE);
                        }
                        break;
                    case 2:
                        if(logList.printType(i).equals(cb_type_ask.getText().toString())) {
                            lv_participant_log.getChildAt(i).setVisibility(View.VISIBLE);
                        }
                        break;
                    case 3:
                        if(logList.printType(i).equals(cb_type_answer.getText().toString())) {
                            lv_participant_log.getChildAt(i).setVisibility(View.VISIBLE);
                        }
                        break;
                    case 4:
                        if(logList.printType(i).equals(cb_type_refute.getText().toString())) {
                            lv_participant_log.getChildAt(i).setVisibility(View.VISIBLE);
                        }
                        break;
                    case 5:
                        if(logList.printType(i).equals(cb_type_additional.getText().toString())){
                            lv_participant_log.getChildAt(i).setVisibility(View.VISIBLE);
                        }
                        break;
                    case 6:
                        if(logList.printType(i).equals(cb_type_etc.getText().toString())){
                            lv_participant_log.getChildAt(i).setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }

            else{
                switch (check) {
                    case 1:
                        if(logList.printType(i).equals(cb_type_opinion.getText().toString())) {
                            Log.i("aaa", "wrong");
                            lv_participant_log.getChildAt(i).setVisibility(View.GONE);
                        }
                        break;
                    case 2:
                        if(logList.printType(i).equals(cb_type_ask.getText().toString())) {
                            lv_participant_log.getChildAt(i).setVisibility(View.GONE);
                        }
                        break;
                    case 3:
                        if(logList.printType(i).equals(cb_type_answer.getText().toString())) {
                            lv_participant_log.getChildAt(i).setVisibility(View.GONE);
                        }
                        break;
                    case 4:
                        if(logList.printType(i).equals(cb_type_refute.getText().toString())) {
                            lv_participant_log.getChildAt(i).setVisibility(View.GONE);
                        }
                        break;
                    case 5:
                        if(logList.printType(i).equals(cb_type_additional.getText().toString())){
                            lv_participant_log.getChildAt(i).setVisibility(View.GONE);
                        }
                        break;
                    case 6:
                        if(logList.printType(i).equals(cb_type_etc.getText().toString())){
                            lv_participant_log.getChildAt(i).setVisibility(View.GONE);
                        }
                        break;
                }
            }
        }

    }

    // get conference data
    public void getData(){
        int i=0;

        while(i<=mDBOpenHelper.countSpeech()){
            mCursor=mDBOpenHelper.getColumn_Speech(i);

            if(mCursor.moveToFirst() && mCursor.getCount() >=1 ){
                if(mCursor.getString(1).equals(mConferInfo.getTitle())) {
                    String contentPreview = mCursor.getString(4);
                    if(contentPreview.length() >25){
                        contentPreview = contentPreview.substring(0, 25) + "...";
                    }

                    logList.addItem(countTable, mCursor.getString(2), mCursor.getString(3), contentPreview);
                }
            }

            i++;
            countTable++;
        }
    }

    // get original content from database
    private String getRealContent(String name, String type, String con){
        String go="";
        int i=0;

        while(i<=mDBOpenHelper.countSpeech()){
            mCursor=mDBOpenHelper.getColumn_Speech(i);

            if(mCursor.moveToFirst() && mCursor.getCount() >=1 ){
                if(mCursor.getString(1).equals(mConferInfo.getTitle())) {
                    if(mCursor.getString(2).equals(name)){
                        if(mCursor.getString(3).equals(type)){
                            if(mCursor.getString(4).startsWith(con.substring(0,20))){
                                go = mCursor.getString(4);
                                break;
                            }
                        }
                    }
                }
            }

            i++;
        }
        return go;
    }

}
