package com.example.smc1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Addentry extends AppCompatActivity {

    EditText t1,t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addentry);

    }
    public void doAddEntry(View v)
    {
        t1=(EditText) findViewById(R.id.name);
        t2=(EditText) findViewById(R.id.regno);
        BackgroundTask bt = new BackgroundTask(this, new response() {
            @Override
            public void onProgressFinish(String res, Context c) {

                    if(res.equals("slots retrived ")) {
                        Toast.makeText(c,"Entry Added",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(c, AddRemove.class));
                    }
                    else
                    {
                        Toast.makeText(c,"Error while Adding Entry",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(c, Addentry.class));
                    }

            }
        });

        bt.execute("adddata", t1.getText().toString(),t2.getText().toString(), getSharedPreferences("session",0).getString("area",null));


    }
}
