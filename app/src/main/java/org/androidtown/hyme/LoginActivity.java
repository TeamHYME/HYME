package org.androidtown.hyme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by HAMHAM on 2017-11-26.
 */

public class LoginActivity extends AppCompatActivity {

    EditText ed_user_id;
    EditText ed_password;
    Button bt_register;
    Button bt_go_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }


    public void initView(){

        ed_user_id = (EditText) findViewById(R.id.ed_user_id);
        ed_password = (EditText) findViewById(R.id.ed_password);
        bt_register = (Button) findViewById(R.id.bt_register);
        bt_go_login = (Button) findViewById(R.id.bt_go_login);

        ed_user_id.setFocusable(true);
        ed_user_id.setFocusableInTouchMode(true);


        bt_register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "현재 준비중입니다.", Toast.LENGTH_LONG).show();
            }
        });

        bt_go_login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                //checkId();

                /* For testing!*/
                justForTest();

            }
        });

    }

    // For testing!!
    public void justForTest(){

        if(ed_user_id.getText().toString().equals("user001")){

            if(ed_password.getText().toString().equals("0000")){
                Intent intent = new Intent(LoginActivity.this, MeetingRoomActivity.class);
                startActivity(intent);
            }

            else{
                Toast.makeText(getApplicationContext(), "비밀번호가 틀립니다. 다시 입력해주세요", Toast.LENGTH_LONG).show();
            }
        }

        else{
            Toast.makeText(getApplicationContext(), "없는 ID입니다. 다시 입력해주세요", Toast.LENGTH_LONG).show();
        }

    }

}
