package com.example.smc1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class AddRemove extends AppCompatActivity {

    TextView t1;
    TextInputLayout tl1,tl2;
    EditText e1,e2;
    Button btn1;
    Button b1;
    LinearLayout l2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.setTitle("Backend Portal");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove);


        t1=(TextView)findViewById(R.id.t1);
        final int[] slots = new int[1];
        BackgroundTask bt = new BackgroundTask(this, new response() {
            @Override
            public void onProgressFinish(String res, Context c) {
                t1.setText("Available slots are: "+res);
                if(Integer.parseInt(res)<=0)
                {

                    b1=(Button)findViewById(R.id.add);
                    b1.setClickable(false);

                }
                if(Integer.parseInt(res)>=60)
                {
                    b1=(Button)findViewById(R.id.remove);
                    b1.setClickable(false);
                }

            }
        });

        bt.execute("fetchdata", getSharedPreferences("session",0).getString("area",null));


    }
    public void doADD(View v)
    {
        l2=(LinearLayout)findViewById(R.id.l2);
        l2.setVisibility(View.VISIBLE);
        tl1=(TextInputLayout)findViewById(R.id.tl1);
        tl2=(TextInputLayout)findViewById(R.id.tl2);
        Button b1=(Button)findViewById(R.id.opp);
        tl1.setVisibility(View.VISIBLE);
        tl2.setVisibility(View.VISIBLE);
        b1.setVisibility(View.VISIBLE);
        b1.setText("ADD THE ENTRY");
//        startActivity(new Intent(this,Addentry.class));

    }
    public void doREMOVE(View v)
    {
        l2=(LinearLayout)findViewById(R.id.l2);
        l2.setVisibility(View.VISIBLE);
        tl1=(TextInputLayout)findViewById(R.id.tl1);
        tl2=(TextInputLayout)findViewById(R.id.tl2);
        tl1.setVisibility(View.GONE);
        Button b1=(Button)findViewById(R.id.opp);
        tl2.setVisibility(View.VISIBLE);
        b1.setVisibility(View.VISIBLE);
        b1.setText("REMOVE THE ENTRY");
//        startActivity(new Intent(this,Removeentry.class));
    }
    public void doEntry(View v)
    {
        Button b1=(Button)findViewById(R.id.opp);
        if(b1.getText().toString().equals("ADD THE ENTRY")) {
            e1 = (EditText) findViewById(R.id.addremove_name);
            e2 = (EditText) findViewById(R.id.addremove_regno);
            BackgroundTask bt = new BackgroundTask(this, new response() {
                @Override
                public void onProgressFinish(String res, Context c) {

                    if (res.equals("slots retrived ")) {
                        Toast toast=Toast.makeText(c, "Entry Added", Toast.LENGTH_SHORT);
                        toast.show();
                        finish();
                        startActivity(getIntent());
                    } else {
                        Toast.makeText(c, "Error while Adding Entry", Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }

                }
            });

            bt.execute("adddata", e1.getText().toString(), e2.getText().toString(), getSharedPreferences("session", 0).getString("area", null));
        }
        else
        {
            e1=(EditText) findViewById(R.id.addremove_regno);

            BackgroundTask bt = new BackgroundTask(this, new response() {
                @Override
                public void onProgressFinish(String res, Context c) {
                    if(res.equals("slots retrived ")) {
                        Toast.makeText(c,"Entry Deleted",Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }
                    else
                    {
                        Toast.makeText(c,"Error While Deleting Data",Toast.LENGTH_SHORT).show();
                        finish();
                        startActivity(getIntent());
                    }




                }
            });

            bt.execute("removedata", e1.getText().toString(), getSharedPreferences("session",0).getString("area",null));

        }

    }

    public boolean onCreateOptionsMenu(Menu m) {
        m.add("Logout");
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem mn) {
        getSharedPreferences("session",0).edit().clear().commit();
        finish();
        startActivity(new Intent(this,MainActivity2.class));
        return true;
    }




}