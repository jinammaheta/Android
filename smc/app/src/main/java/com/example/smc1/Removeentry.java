package com.example.smc1;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Removeentry extends AppCompatActivity {

    EditText t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_removeentry);
    }
    public void doRemoveEntry(View v)
    {
        t1=(EditText) findViewById(R.id.regno_todelete);

        BackgroundTask bt = new BackgroundTask(this, new response() {
            @Override
            public void onProgressFinish(String res, Context c) {
                if(res.equals("slots retrived ")) {
                    Toast.makeText(c,"Entry Deleted",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(c, AddRemove.class));
                }
                else
                {
                    Toast.makeText(c,"Error While Deleting Data",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(c, Removeentry.class));
                }




            }
        });

        bt.execute("removedata", t1.getText().toString(), getSharedPreferences("session",0).getString("area",null));


    }
}
