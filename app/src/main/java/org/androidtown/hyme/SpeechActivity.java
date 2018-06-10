package org.androidtown.hyme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.hyme.database.DbOpenHelper;

/**
 * Created by Master on 2017-12-06.
 */

public class SpeechActivity extends AppCompatActivity {

    Button bt_start_voice_recog;
    Button bt_go_participant;
    Button bt_show_log;
    TextView tv_voice_recog_result;
    TextView tv_speak_type;
    Spinner spinner_speech_speaker;
    ArrayAdapter<String> adapter;
    RadioGroup rg_speech_type_1;
    RadioGroup rg_speech_type_2;
    TextView tv_speech_formal_name;
    TextView tv_speech_formal_content;

    // set member
    String getMember = "";
    String speaker = "";
    String[] member;

    // check if radiobutton is checked
    Boolean checked = false;

    // set speech type
    int typeStatus = 0;
    String saveStatus = "";

    // Save string
    String saveString = "";
    String speechContent = "";

    // count listener time
    int listened = 0;

    //Database
    DbOpenHelper mDBOpenHelper;

    // conference information
    ConferenceInfo mConferInfo;

    // speech analysis
    SpeechAlgorithm mSpeechAlgorithm;

    // voice recognition
    public static final String TAG = "SpeechActivity";
    private static final int RECORD_REQUEST_CODE = 101;
    private SpeechAPI speechAPI;
    private VoiceRecorder mVoiceRecorder;
    private final VoiceRecorder.Callback mVoiceCallback = new VoiceRecorder.Callback() {
        @Override
        public void onVoiceStart() {
            if (speechAPI != null) {
                speechAPI.startRecognizing(mVoiceRecorder.getSampleRate());
            }
        }

        @Override
        public void onVoice(byte[] data, int size) {
            if (speechAPI != null) {
                speechAPI.recognize(data, size);
            }
        }

        @Override
        public void onVoiceEnd() {
            if (speechAPI != null) {
                speechAPI.finishRecognizing();
            }
        }
    };
    private final SpeechAPI.Listener mSpeechServiceListener =
            new SpeechAPI.Listener() {
                @Override
                public void onSpeechRecognized(final String text, final boolean isFinal) {
                    if (isFinal) {
                        mVoiceRecorder.dismiss();
                    }
                    if (tv_voice_recog_result != null && !TextUtils.isEmpty(text)) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isFinal) {
                                    Log.i("aaa", "is got here");
                                    Log.i("aaa", text);
                                    saveString += text;
                                    tv_voice_recog_result.setText(saveString);
                                } else {
                                    Log.i("aaa", "or got here");
                                    Log.i("aaa", text);
                                    tv_voice_recog_result.setText(saveString + text);

                                    // Analyze text if is not answer or question
                                    if((typeStatus != 4) && (typeStatus !=3)) {

                                        // Decide the type in 20 listener times
                                        if (listened <= 20) {
                                            typeStatus = mSpeechAlgorithm.analyzeText(tv_voice_recog_result.getText().toString());
                                            mSpeechAlgorithm.setType(typeStatus);
                                        }
                                    }
                                    setType();
                                    listened++;
                                    Log.i("aaa", listened + "");
                                }
                            }
                        });
                    }
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech);

        Bundle getBundle = new Bundle();
        getBundle = getIntent().getExtras();
        mConferInfo = new ConferenceInfo(getBundle.getString("title"), getBundle.getString("members"));
        getMember = mConferInfo.getMember();
        getMember.trim();
        member = getMember.split(",");
        speaker = member[0];

        mSpeechAlgorithm = new SpeechAlgorithm();

        initDatabase();
        initView();
    }

    public void initDatabase() {
        mDBOpenHelper = new DbOpenHelper(this);
        mDBOpenHelper.openDB();
    }

    public void initView() {
        tv_voice_recog_result = (TextView) findViewById(R.id.tv_voice_recog_result);
        tv_speak_type = (TextView) findViewById(R.id.tv_speak_type);
        spinner_speech_speaker = (Spinner) findViewById(R.id.spinner_speech_speaker);
        tv_speech_formal_name = (TextView) findViewById(R.id.tv_speech_formal_name);
        tv_speech_formal_content = (TextView) findViewById(R.id.tv_speech_formal_content);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, member);
        spinner_speech_speaker.setAdapter(adapter);

        spinner_speech_speaker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                speaker = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        bt_start_voice_recog = (Button) findViewById(R.id.bt_start_voice_recog);
        bt_start_voice_recog.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (bt_start_voice_recog.getText().toString() == getResources().getString(R.string.activity_speech_want)) {
                    Log.i("aaa", "started");

                    speechAPI = new SpeechAPI(SpeechActivity.this);

                    // predict the type if the user didn't push the radio button
                    if(!checked) {
                        tv_speak_type.setText("발언 예측중..");

                        // Analyze formal type
                        typeStatus = mSpeechAlgorithm.analyzeType();
                    }

                    if (isGrantedPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        Log.i("aaa", "where are you");
                        bt_start_voice_recog.setText(getResources().getString(R.string.activity_speech_ongoing));
                        startVoiceRecorder();
                        speechAPI.addListener(mSpeechServiceListener);
                        setType();
                    } else {
                        Log.i("aaa", "are you here");
                        Toast.makeText(getApplicationContext(), "오디오 접근 허용 승인 후 사용해 주시기 바랍니다.", Toast.LENGTH_LONG).show();
                        makeRequest(Manifest.permission.RECORD_AUDIO);
                    }


                } else if (bt_start_voice_recog.getText().toString() == getResources().getString(R.string.activity_speech_ongoing)) {
                    bt_start_voice_recog.setText(getResources().getString(R.string.activity_speech_want));

                    if (typeStatus != 0) {
                        saveStatus = tv_speak_type.getText().toString();
                    }

                    // Save formal information
                    tv_speech_formal_name.setText(speaker + " (" + tv_speak_type.getText().toString() + ")");
                    tv_speech_formal_content.setText(saveString);
                    mSpeechAlgorithm.setType(typeStatus);

                    typeStatus = 0;
                    tv_speak_type.setText(getResources().getString(R.string.activity_speech_ready));
                    tv_speak_type.setBackgroundColor(Color.parseColor("#717171"));

                    speechContent = saveString;
                    saveString = "";
                    listened = 0;
                    tv_voice_recog_result.setText(saveString);

                    for (int i = 0; i < rg_speech_type_1.getChildCount(); i++) {
                        rg_speech_type_1.getChildAt(i).setBackgroundResource(R.color.LightGray);
                    }
                    for (int i = 0; i < rg_speech_type_2.getChildCount(); i++) {
                        rg_speech_type_2.getChildAt(i).setBackgroundResource(R.color.LightGray);
                    }

                    saveData();

                    stopVoiceRecorder();

                    rg_speech_type_1.clearCheck();
                    rg_speech_type_2.clearCheck();
                }

            }
        });

        rg_speech_type_1 = (RadioGroup) findViewById(R.id.rg_speech_type_1);
        rg_speech_type_2 = (RadioGroup) findViewById(R.id.rg_speech_type_2);

        rg_speech_type_1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton one = (RadioButton) findViewById(R.id.rb_speech_type_1);
                RadioButton two = (RadioButton) findViewById(R.id.rb_speech_type_2);
                RadioButton three = (RadioButton) findViewById(R.id.rb_speech_type_3);
                RadioButton four = (RadioButton) findViewById(R.id.rb_speech_type_4);
                RadioButton five = (RadioButton) findViewById(R.id.rb_speech_type_5);
                RadioButton six = (RadioButton) findViewById(R.id.rb_speech_type_6);

                four.setBackgroundResource(R.color.LightGray);
                five.setBackgroundResource(R.color.LightGray);
                six.setBackgroundResource(R.color.LightGray);

                switch (i) {
                    case R.id.rb_speech_type_1:
                        typeStatus = 1;
                        if (one.isChecked()) {
                            one.setBackgroundResource(R.color.gold);
                            checked = true;
                        }
                        else {
                            one.setBackgroundResource(R.color.LightGray);
                            checked = false;
                        }
                        two.setBackgroundResource(R.color.LightGray);
                        three.setBackgroundResource(R.color.LightGray);
                        break;
                    case R.id.rb_speech_type_2:
                        typeStatus = 2;
                        if (two.isChecked()) {
                            two.setBackgroundResource(R.color.limeGreen);
                            checked = true;
                        }
                        else {
                            two.setBackgroundResource(R.color.LightGray);
                            checked = false;
                        }
                        one.setBackgroundResource(R.color.LightGray);
                        three.setBackgroundResource(R.color.LightGray);
                        break;
                    case R.id.rb_speech_type_3:
                        typeStatus = 3;
                        if (three.isChecked()) {
                            three.setBackgroundResource(R.color.red);
                            checked = true;
                        }
                        else {
                            three.setBackgroundResource(R.color.LightGray);
                            checked = false;
                        }
                        one.setBackgroundResource(R.color.LightGray);
                        two.setBackgroundResource(R.color.LightGray);
                        break;
                    default:
                        typeStatus = 0;
                        break;
                }

                setType();
            }
        });

        rg_speech_type_2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton one = (RadioButton) findViewById(R.id.rb_speech_type_1);
                RadioButton two = (RadioButton) findViewById(R.id.rb_speech_type_2);
                RadioButton three = (RadioButton) findViewById(R.id.rb_speech_type_3);
                RadioButton four = (RadioButton) findViewById(R.id.rb_speech_type_4);
                RadioButton five = (RadioButton) findViewById(R.id.rb_speech_type_5);
                RadioButton six = (RadioButton) findViewById(R.id.rb_speech_type_6);

                one.setBackgroundResource(R.color.LightGray);
                two.setBackgroundResource(R.color.LightGray);
                three.setBackgroundResource(R.color.LightGray);

                switch (i) {
                    case R.id.rb_speech_type_4:
                        typeStatus = 4;
                        if (four.isChecked()) {
                            four.setBackgroundResource(R.color.purple);
                            checked = true;
                        }
                        else {
                            four.setBackgroundResource(R.color.LightGray);
                            checked = false;
                        }
                        five.setBackgroundResource(R.color.LightGray);
                        six.setBackgroundResource(R.color.LightGray);
                        break;
                    case R.id.rb_speech_type_5:
                        typeStatus = 5;
                        if (five.isChecked()) {
                            five.setBackgroundResource(R.color.cleanBlue);
                            checked = true;
                        }
                        else {
                            five.setBackgroundResource(R.color.LightGray);
                            checked = false;
                        }
                        four.setBackgroundResource(R.color.LightGray);
                        six.setBackgroundResource(R.color.LightGray);
                        break;
                    case R.id.rb_speech_type_6:
                        typeStatus = 6;
                        if (six.isChecked()) {
                            six.setBackgroundResource(R.color.pink);
                            checked = true;
                        }
                        else {
                            six.setBackgroundResource(R.color.LightGray);
                            checked = false;
                        }
                        four.setBackgroundResource(R.color.LightGray);
                        five.setBackgroundResource(R.color.LightGray);
                        break;
                    default:
                        typeStatus = 0;
                        break;
                }
                setType();
            }
        });

        // when type changed


        // For server mode(update soon?)
        /*
        bt_go_participant = (Button) findViewById(R.id.bt_show_status);
        bt_go_participant.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle myBundle = new Bundle();
                myBundle.putString("title", mConferInfo.getTitle());
                myBundle.putString("members", getMember);
                Intent intent = new Intent(SpeechActivity.this, ParticipantActivity.class);
                intent.putExtras(myBundle);
                startActivity(intent);
            }
        });
        */

        bt_show_log = (Button) findViewById(R.id.bt_show_log);
        bt_show_log.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle myBundle = new Bundle();
                myBundle.putString("title", mConferInfo.getTitle());
                myBundle.putString("members", getMember);
                Intent intent = new Intent(SpeechActivity.this, LogActivity.class);
                intent.putExtras(myBundle);
                startActivity(intent);
            }
        });

    }

    // Save data to database
    private void saveData() {
        Log.i("aaaa", speaker + ", " + saveStatus + ", " + speechContent);
        if(!speechContent.equals("") && !saveStatus.equals("")) {
            Toast.makeText(getApplicationContext(), "발언을 로그에 저장했습니다.", Toast.LENGTH_SHORT).show();
            mDBOpenHelper.insertColumn_Speech(mConferInfo.getTitle(), speaker, saveStatus, speechContent);
        }
    }

    // Set TextView(type)'s background color based on typeStatus
    private void setType(){

        switch (typeStatus) {
            case 1:
                tv_speak_type.setText(getResources().getString(R.string.dialog_speech_opinion));
                tv_speak_type.setBackgroundColor(Color.parseColor("#FFD700"));
                break;
            case 2:
                tv_speak_type.setText(getResources().getString(R.string.dialog_speech_ask));
                tv_speak_type.setBackgroundColor(Color.parseColor("#32CD32"));
                break;
            case 3:
                tv_speak_type.setText(getResources().getString(R.string.dialog_speech_answer));
                tv_speak_type.setBackgroundColor(Color.parseColor("#FF0000"));
                break;
            case 4:
                tv_speak_type.setText(getResources().getString(R.string.dialog_speech_refutation));
                tv_speak_type.setBackgroundColor(Color.parseColor("#800080"));
                break;
            case 5:
                tv_speak_type.setText(getResources().getString(R.string.dialog_speech_additional));
                tv_speak_type.setBackgroundColor(Color.parseColor("#63b2f7"));
                break;
            case 6:
                tv_speak_type.setText(getResources().getString(R.string.dialog_speech_etc));
                tv_speak_type.setBackgroundColor(Color.parseColor("#FFC0CB"));
        }
    }


    /* For voice recognition! */
    @Override
    protected void onStop() {
        //stopVoiceRecorder();

        // Stop Cloud Speech API
        if ((mSpeechServiceListener != null) && (speechAPI != null)) {
            speechAPI.removeListener(mSpeechServiceListener);
            speechAPI.destroy();
            speechAPI = null;
        }

        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isGrantedPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
        } else {
            makeRequest(Manifest.permission.RECORD_AUDIO);
        }
    }

    private int isGrantedPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission);
    }

    private void makeRequest(String permission) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, RECORD_REQUEST_CODE);
    }

    private void startVoiceRecorder() {
        if (mVoiceRecorder != null) {
            Log.i("aaa", "it means mVoiceRecorder is null");
            mVoiceRecorder.stop();
        }
        Log.i("aaa", "Recorder call");
        mVoiceRecorder = new VoiceRecorder(mVoiceCallback);
        mVoiceRecorder.start();
    }

    private void stopVoiceRecorder() {
        if (mVoiceRecorder != null) {
            mVoiceRecorder.stop();
            mVoiceRecorder = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == RECORD_REQUEST_CODE) {
            if (grantResults.length == 0 && grantResults[0] == PackageManager.PERMISSION_DENIED
                    && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                finish();
            } else {
                startVoiceRecorder();
            }
        }
    }
}
