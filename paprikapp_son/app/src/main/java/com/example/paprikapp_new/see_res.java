package com.example.paprikapp_new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class see_res extends AppCompatActivity {
    LinearLayout lin,lin2;
    Button delete_res;
    ArrayList<String> res = new ArrayList<>();
    String str ;
    ImageButton main4;
    TextView tv,name_view;
    int i;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_res);

        lin  = (LinearLayout) findViewById(R.id.lin);
        lin2  = (LinearLayout) findViewById(R.id.lin2);
        str = "";
        tv = (TextView) findViewById(R.id.tv);
        name_view = (TextView) findViewById(R.id.name_view);
        main4 = (ImageButton) findViewById(R.id.main4);

        delete_res = (Button) findViewById(R.id.see_res);

        db = FirebaseDatabase.getInstance();

        i=0;
        //    error();
        listRes();






        main4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(see_res.this);
                builder.setCancelable(true);
                builder.setTitle("You will directed to the Main Page");
                builder.setMessage("Your session will be ended ");
                builder.setPositiveButton("Approve",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


    }



    public void removeNode(String parent_name,String id){
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference(parent_name);

        zonesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                zonesRef.child(id).removeValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void listRes(){
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("reservation");

        zonesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Iterable<DataSnapshot> keys = dataSnapshot.getChildren();
                for (DataSnapshot key: keys) {

                    Button btn = new Button(see_res.this);
                    btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    btn.setId(i);
                    btn.setText("reservation "+key.getKey());
                    btn.setWidth(300);
                    btn.setHeight(200);
                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String res_name = key.child("resName").getValue().toString();
                            String date = key.child("resDate").getValue().toString();
                            String res_phone = key.child("resPhone").getValue().toString();
                            String table_id = key.child("resTableId").getValue().toString();
                            String time = key.child("resTime").getValue().toString();
                            String numOfPeople = key.child("resNumOfPeople").getValue().toString();

                            delete_res.setVisibility(View.VISIBLE);
                            main4.setVisibility(View.VISIBLE);
                            delete_res.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(see_res.this);
                                    builder.setCancelable(true);
                                    builder.setTitle("Are you sure?");
                                    builder.setMessage("Do you want to delete reservation "+key.getKey()+" ?");

                                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            name_view.setText("");
                                            tv.setText("");
                                            delete_res.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                    builder.setPositiveButton("Delete",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    removeNode("reservation",key.getKey());
                                                    lin2.removeAllViews();
                                                    name_view.setText("");
                                                    tv.setText("");
                                                    delete_res.setVisibility(View.INVISIBLE);
                                                }
                                            });

                                    AlertDialog dialog = builder.create();
                                    dialog.show();

                                }
                            });

                            name_view.setText(res_name);
                            tv.setText("date: "+ date+"\n\ntable_id: "+table_id+"\n\nPhone Number: "+res_phone + "\n\ntime: "+ time+ "\n\nNumber of People: "+ numOfPeople);

                        }
                    });

                    lin2.addView(btn);
                    i++;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }






    private void findAllEmployees(){

        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("reservation");

        zonesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot objSnapshot: snapshot.getChildren()) {

                    res.add(objSnapshot.getKey());
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e("Read failed", firebaseError.getMessage());
            }

        });
        tv.setText(res.get(0));
    }




    private void showID(){

        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("reservation");

        zonesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> keys = dataSnapshot.getChildren();
                for (DataSnapshot key: keys) {
                    tv.append(key.getKey()+" ");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
