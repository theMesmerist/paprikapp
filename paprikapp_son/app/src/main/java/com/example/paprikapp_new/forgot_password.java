package com.example.paprikapp_new;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Properties;


public class forgot_password extends AppCompatActivity {

    Button send, cancel;
    EditText given_email, given_id;
    String email;
    int id;
ImageButton main10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        send = (Button) findViewById(R.id.send);
        cancel = (Button) findViewById(R.id.cancel3);
        given_email = (EditText) findViewById(R.id.given_email);
        given_id = (EditText) findViewById(R.id.given_id);

        main10 = (ImageButton) findViewById(R.id.main10);

        main10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        ActivityCompat.requestPermissions(forgot_password.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                if (given_id.getText().equals(null) && given_email.getText().equals(null)){
                    Log.d("boş","boşşş");

                } else if (!given_id.getText().equals(null) && !given_email.getText().equals(null)){
                    control(Integer.valueOf(given_id.getText().toString()),given_email.getText().toString());
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), login_screen.class);

                startActivity(intent);
            }
        });



    }


    public void control(int id, String email) {
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("employee");

        zonesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Iterable<DataSnapshot> keys = dataSnapshot.getChildren();
                for (DataSnapshot key : keys) {

             if (id == Integer.valueOf(key.getKey())){
            String db_email =  key.child("email").getValue().toString();
            if(email.equals(db_email)){


                AlertDialog.Builder builder = new AlertDialog.Builder(forgot_password.this);
                builder.setCancelable(true);
                builder.setTitle("Don't forget to change your password");
                builder.setMessage("Your password is: " + key.child("password").getValue().toString());
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });


                AlertDialog dialog = builder.create();
                dialog.show();

                break;
            } else if(!email.equals(db_email)){
                Toast.makeText(forgot_password.this,"id and email does not match",Toast.LENGTH_LONG).show();
            }
             }
             else if(id != Integer.valueOf(key.getKey())) {
                 Toast.makeText(forgot_password.this,"id not found",Toast.LENGTH_LONG).show();
             }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }




     }



