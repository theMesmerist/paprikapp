package com.example.paprikapp_new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class all_employees extends AppCompatActivity {
    LinearLayout lin, lin2;
    Button delete_emp;
    ArrayList<String> emp = new ArrayList<>();
    String str;
    ImageButton main4;
    TextView tv, name_view;
    int i;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_employees);

        lin = (LinearLayout) findViewById(R.id.lin);
        lin2 = (LinearLayout) findViewById(R.id.lin2);
        str = "";
        tv = (TextView) findViewById(R.id.tv);
        name_view = (TextView) findViewById(R.id.name_view);
        main4 = (ImageButton) findViewById(R.id.main4);

        delete_emp = (Button) findViewById(R.id.del_rec);

        db = FirebaseDatabase.getInstance();

        i = 0;


        main4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        listEmployees();


        delete_emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        main4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(all_employees.this);
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


    public void listEmployees() {
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("employee");

        zonesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Iterable<DataSnapshot> keys = dataSnapshot.getChildren();
                for (DataSnapshot key : keys) {


                    if (key.child("isManager").getValue().toString() == "false") {


                        Button btn = new Button(all_employees.this);
                        ImageView iv = new ImageView(all_employees.this);
                        btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));


                        btn.setId(i);
                        iv.setId(i);
                        btn.setBackgroundColor(0xFF262261);
                        btn.setTextColor(0xFFFFFFFF);
                        iv.setMaxWidth(300);
                        iv.setMaxHeight(20);
                        iv.setImageResource(R.drawable.white);
                        btn.setWidth(300);
                        btn.setHeight(150);
                        btn.setTextSize(24);


                        btn.setText(key.getKey() + "\n" + key.child("name").getValue().toString());
                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                delete_emp.setVisibility(View.VISIBLE);
                                String name = key.child("name").getValue().toString();
                                String email = key.child("email").getValue().toString();
                                String salary = key.child("salary").getValue().toString();
                                String phone = key.child("phone").getValue().toString();
                                String address = key.child("address").getValue().toString();
                                String isManager = key.child("isManager").getValue().toString();


                                delete_emp.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(all_employees.this);
                                        builder.setCancelable(true);
                                        builder.setTitle("Are you sure?");
                                        builder.setMessage("Do you want to delete employee " + key.getKey() + " ?");

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
                                                        removeNode("employee", key.getKey());
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

                                name_view.setText(name);
                                tv.setText("ID: " + key.getKey() + "\n\nEmail: " + email + "\n\nSalary: " + salary + "\n\nPhone Number: " + phone + "\n\nEmployee's Address: " + address + "\n\nis Manager: " + isManager);

                            }
                        });


                        lin2.addView(btn);
                        lin2.addView(iv);
                        i++;
                    }
                }
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