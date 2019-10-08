package com.example.smc1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class HomeActivity  extends AppCompatActivity {
    public CardView open1;
    public CardView open2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        open1=(CardView)findViewById(R.id.useropen);
        open1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        open2=(CardView)findViewById(R.id.adminopen);
        open2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,MainActivity2.class);
                startActivity(intent);
            }
        });
    }
//    open.OnClickListener(new View.OnClickListener)
public void onBackPressed() {

    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
    builder.setTitle("SMC Parking System");
    builder.setIcon(R.drawable.parking_icon1);
    builder.setMessage("Do you want to exit?")
            .setCancelable(false)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
    AlertDialog alert = builder.create();
    alert.show();

}
}
