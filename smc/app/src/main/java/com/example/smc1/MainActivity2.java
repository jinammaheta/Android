package com.example.smc1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    EditText e1,e2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTitle("Login");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        SharedPreferences sd=getSharedPreferences("session",0);
        String user=sd.getString("operatorname",null);
        if(user!=null)
        {
            startActivity(new Intent(this, AddRemove.class));
        }
    }
    public void doLog(View v) {
        e1 = (EditText) findViewById(R.id.user_name);
        e2 = (EditText) findViewById(R.id.login_password);
        final String s1, s2;
        s1 = e1.getText().toString();
        s2 = e2.getText().toString();
        final String finalS = s1;
        BackgroundTask bt = new BackgroundTask(this, new response() {
            @Override
            public void onProgressFinish(String res, Context c) {
                if(!res.equals("NO User Found")) {

                    SharedPreferences sd = getSharedPreferences("session", 0);
                    SharedPreferences.Editor ed = sd.edit();
                    ed.putString("operatorname", s1);
                    ed.putString("area", res);
                    ed.commit();
                    finish();
                    startActivity(new Intent(c, AddRemove.class));
                }
                else
                {
                    Toast.makeText(c, res, Toast.LENGTH_SHORT).show();
                }


            }
        });
        bt.execute("Login", s1, s2);
    }
}
