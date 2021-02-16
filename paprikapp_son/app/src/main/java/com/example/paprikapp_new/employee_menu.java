package com.example.paprikapp_new;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


import android.os.Handler;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class employee_menu extends AppCompatActivity {

    TextView welcome;
    ImageButton main2;
    String error, id, name, emp_name, db_p;
    Button change, logout2, change_now, cancel2,order,reservation;
    ImageView back2;
    EditText old_pass, new_pass, new_pass_again;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_menu);

        welcome = (TextView) findViewById(R.id.welcome2);
        main2 = (ImageButton) findViewById(R.id.main2);
        back2 = (ImageView) findViewById(R.id.back2);

        change = (Button)  findViewById(R.id.change);
        logout2 = (Button)  findViewById(R.id.logout2);
        change_now = (Button)  findViewById(R.id.payment);
        cancel2 = (Button)  findViewById(R.id.cancel2);
        order = (Button)  findViewById(R.id.order);
        reservation = (Button)  findViewById(R.id.reservation);

        old_pass = (EditText)  findViewById(R.id.old_pass);
        new_pass = (EditText)  findViewById(R.id.new_pass);
        new_pass_again = (EditText)  findViewById(R.id.new_pass_again);

        inv_change_pass();

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        db_p = intent.getStringExtra("pass");
        name = getName(id);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                welcome.setText(" Welcome! \n " + getName(id));
            }
        }, 300);



        main2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(employee_menu.this);
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




        change.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (back2.getVisibility()==View.VISIBLE){
                   inv_change_pass();
                } else if(back2.getVisibility() == View.INVISIBLE) {
                   vis_change_pass();
                }
            }
        });


        change_now.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changePassword();

            }
        });


        order.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Order.class);
                startActivity(intent);

            }
        });



        reservation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), reservation.class);
                startActivity(intent);

            }
        });


        logout2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), login_screen.class);
                startActivity(intent);

            }
        });


        cancel2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clear();
                inv_change_pass();

            }
        });
    }

    private String getName(String id) {
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("employee").child(id).child("name");


        zonesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                emp_name = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                error = databaseError.toException().toString();
            }
        });

        return emp_name;
    }

    public void changePassword(){
        String np, na, op;
        np = new_pass.getText().toString();
        na = new_pass_again.getText().toString();
        op = old_pass.getText().toString();

        if (np.isEmpty() || na.isEmpty() || op.isEmpty()){
            Toast.makeText(this,"fields cannot be empty",Toast.LENGTH_LONG).show();
        } else if (!na.equals(np)){
            Toast.makeText(this,"passwords does not match",Toast.LENGTH_LONG).show();
            clear();
        }else if (!db_p.equals(op)){
            Toast.makeText(this,"old password is wrong",Toast.LENGTH_LONG).show();
            clear();
        } else if (db_p.equals(np)){
            Toast.makeText(this,"try another password! different from the old one",Toast.LENGTH_LONG).show();
            clear();
        } else{
            FirebaseDatabase  database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabaseRef = database.getReference();
            mDatabaseRef.child("employee").child(id).child("password").setValue(np);
            Toast.makeText(this,"password changed successfully",Toast.LENGTH_LONG).show();
            clear();
            inv_change_pass();
        }


    }

    public void clear(){
        old_pass.getText().clear();
        new_pass.getText().clear();
        new_pass_again.getText().clear();
    }


    public  void inv_change_pass(){

        back2.setVisibility(View.INVISIBLE);
        change_now.setVisibility(View.INVISIBLE);
        cancel2.setVisibility(View.INVISIBLE);
        old_pass.setVisibility(View.INVISIBLE);
        new_pass.setVisibility(View.INVISIBLE);
        new_pass_again.setVisibility(View.INVISIBLE);
    }


    public  void vis_change_pass(){

        back2.setVisibility(View.VISIBLE);
        change_now.setVisibility(View.VISIBLE);
        cancel2.setVisibility(View.VISIBLE);
        old_pass.setVisibility(View.VISIBLE);
        new_pass.setVisibility(View.VISIBLE);
        new_pass_again.setVisibility(View.VISIBLE);
    }
}