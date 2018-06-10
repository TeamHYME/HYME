package org.androidtown.hyme;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.hyme.database.DbOpenHelper;

public class SpeechDetailActivity extends AppCompatActivity {

    TextView tv_detail_speecher;
    TextView tv_detail_type;
    EditText ed_detail_content;
    Button bt_detail_save;
    Button bt_detail_cancel;

    String title = "";
    String speaker = "";
    String type = "";
    String content = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech_detail);

        Bundle getBundle = new Bundle();
        getBundle = getIntent().getExtras();

        title = getBundle.getString("title");
        speaker = getBundle.getString("speaker");
        type = getBundle.getString("type");
        content = getBundle.getString("content");

        initView();
    }

    public void initView() {
        tv_detail_speecher = (TextView) findViewById(R.id.tv_detail_speecher);
        tv_detail_type = (TextView) findViewById(R.id.tv_detail_type);
        ed_detail_content = (EditText) findViewById(R.id.ed_detail_content);
        bt_detail_save = (Button) findViewById(R.id.bt_detail_save);
        bt_detail_cancel = (Button) findViewById(R.id.bt_detail_cancel);

        // make content uneditable, and buttons invisible at first time
        makeCondition(true);
        ed_detail_content.setFocusable(false);
        bt_detail_save.setVisibility(View.INVISIBLE);
        bt_detail_cancel.setVisibility(View.INVISIBLE);

        tv_detail_speecher.setText(speaker);
        tv_detail_type.setText(type);
        ed_detail_content.setText(content);

        ed_detail_content.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                getDialog();

                return true;
            }
        });

        bt_detail_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCondition(true);
                // update edited content in database;
                updateData();
                Toast.makeText(getApplicationContext(), "수정을 완료했습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        bt_detail_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCondition(true);
            }
        });

    }

    private void getDialog() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("발언 내용");
        alert.setPositiveButton("수정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // enable visibility
                makeCondition(false);
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alert.show();
    }

    private void makeCondition(Boolean ok) {

        if (ok) {
            ed_detail_content.setFocusable(false);
            ed_detail_content.setLongClickable(true);
            bt_detail_save.setVisibility(View.INVISIBLE);
            bt_detail_cancel.setVisibility(View.INVISIBLE);
        } else {
            ed_detail_content.setFocusable(true);
            ed_detail_content.setEnabled(true);
            ed_detail_content.setFocusableInTouchMode(true);
            ed_detail_content.setLongClickable(false);
            bt_detail_save.setVisibility(View.VISIBLE);
            bt_detail_cancel.setVisibility(View.VISIBLE);
        }
    }

    private void updateData() {

        DbOpenHelper mDBOpenHelper = new DbOpenHelper(this);
        mDBOpenHelper.openDB();
        Cursor mCursor;

        int i = 0;

        while (i <= mDBOpenHelper.countSpeech()) {
            mCursor = mDBOpenHelper.getColumn_Speech(i);

            if (mCursor.moveToFirst() && mCursor.getCount() >= 1) {
                if (mCursor.getString(1).equals(title)) {
                    if (mCursor.getString(2).equals(speaker)) {
                        if (mCursor.getString(3).equals(type)) {
                            if (mCursor.getString(4).equals(content)) {
                                content = ed_detail_content.getText().toString();
                                mDBOpenHelper.updateColumn_Speech(i, title, speaker, type, content);
                                break;
                            }
                        }
                    }
                }
            }
            i++;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            Intent intent = new Intent(SpeechDetailActivity.this, LogActivity.class);
            Bundle myBundle = new Bundle();
            myBundle.putString("title", title);
            myBundle.putString("members", speaker);
            intent.putExtras(myBundle);
            finish();
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
