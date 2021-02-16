package com.example.paprikapp_new;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {


    private FirebaseDatabase myDB;
    Button goToLogin;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
       /* getSupportActionBar().hide();             fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        myDB = FirebaseDatabase.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goToLogin = (Button) findViewById(R.id.goToLogin);

        //    add("emre");
        //     add_hash("berk");

        goToLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), login_screen.class);

                startActivity(intent);
            }
        });


    }


    public void add(String add){
        DatabaseReference dbRef = myDB.getReference("deneme_tablo");
        dbRef.setValue(add);
    }

    public void add_hash(String add_hash){
        DatabaseReference  dbRef = myDB.getReference("deneme_tablo2");
        dbRef.setValue(add_hash.hashCode());
    }
}