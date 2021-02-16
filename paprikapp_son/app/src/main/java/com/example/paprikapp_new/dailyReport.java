package com.example.paprikapp_new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class dailyReport extends AppCompatActivity {



    LinearLayout lin, lin2;
    Button delete_emp,list;
    ArrayList<String> emp = new ArrayList<>();
    String str,selectedDate;
    ImageButton main4;
    TextView tv, name_view;
    int i;
    private FirebaseDatabase db;
    Spinner dropdown2;
    ArrayList<String> dates;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);

            lin = (LinearLayout) findViewById(R.id.lin);
            lin2 = (LinearLayout) findViewById(R.id.lin2);
            str = "";
            tv = (TextView) findViewById(R.id.tv);
            name_view = (TextView) findViewById(R.id.name_view);
            main4 = (ImageButton) findViewById(R.id.main4);
            delete_emp = (Button) findViewById(R.id.del_rec);
        list = (Button) findViewById(R.id.list);
            db = FirebaseDatabase.getInstance();
            dropdown2 = findViewById(R.id.spinner2);
            i = 0;
            listReport();
            dates   = new ArrayList<>();
            dates.add("All Dates");


            main4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
            });

        }


        public void listReport() {
            DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("cashRegister");

            zonesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                                Iterable<DataSnapshot> keys = dataSnapshot.getChildren();
                    for (DataSnapshot key : keys) {

                        Button btn = new Button(dailyReport.this);
                        ImageView iv = new ImageView(dailyReport.this);
                        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));


                        if (!dates.contains(key.child("currentTime").getValue().toString())) {
                            dates.add(key.child("currentTime").getValue().toString());
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, dates);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        dropdown2.setAdapter(adapter);




                        btn.setId(i);
                            iv.setId(i);
                            btn.setBackgroundColor(0xFF262261);
                            btn.setTextColor(0xFFFFFFFF);
                            iv.setMaxWidth(300);
                            iv.setMaxHeight(20);
                            iv.setImageResource(R.drawable.white);
                            btn.setWidth(300);
                            btn.setHeight(70);
                            btn.setTextSize(24);
                            btn.setText(key.getKey());


                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    delete_emp.setVisibility(View.VISIBLE);
                                    String currentTime = key.child("currentTime").getValue().toString();
                                    String orderId = key.child("orderId").getValue().toString();
                                    String price = key.child("orderPrice").getValue().toString();


                                    delete_emp.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(dailyReport.this);
                                            builder.setCancelable(true);
                                            builder.setTitle("Are you sure?");
                                            builder.setMessage("Do you want to delete the record " + key.getKey() + " ?");

                                            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    name_view.setText("");
                                                    tv.setText("");
                                                    delete_emp.setVisibility(View.INVISIBLE);
                                                }
                                            });
                                            builder.setPositiveButton("Delete",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            removeNode("cashRegister", key.getKey());
                                                            lin2.removeAllViews();
                                                            name_view.setText("");
                                                            tv.setText("");
                                                            delete_emp.setVisibility(View.INVISIBLE);
                                                        }
                                                    });

                                            AlertDialog dialog = builder.create();
                                            dialog.show();

                                        }
                                    });

                                    name_view.setText(key.getKey());
                                    tv.setText("Order date: " + currentTime + "\n\norderID: " + orderId + "\n\nTotal price: " + price);

                                }
                            });

                            lin2.addView(btn);
                            lin2.addView(iv);
                            i++;

                    } ///// for ends

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }


    public void removeNode(String parent_name, String id) {
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
    }
