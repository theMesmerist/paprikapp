package com.example.paprikapp_new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_screen extends AppCompatActivity {
    private FirebaseDatabase myDB;
    Button login, forgot_pass;
    ImageButton main;
    EditText id_edit, pass_edit;
    private TextView gor;
    String password_db;
    boolean is_manager;
    static int i = 222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        myDB = FirebaseDatabase.getInstance();

        main = (ImageButton) findViewById(R.id.main4);
        id_edit = (EditText) findViewById(R.id.id);
        pass_edit = (EditText) findViewById(R.id.pass);
        gor = (TextView) findViewById(R.id.gor);

        login = (Button) findViewById(R.id.login);
        forgot_pass = (Button) findViewById(R.id.forgot_pass);

        password_db = "";


        main.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);

            }
        });


        login.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                login();

            }
        });


        forgot_pass.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), forgot_password.class);

                startActivity(intent);

            }
        });


    }


    public void isManager(String id, FireBaseListener fireBaseListener) {



        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("employee").child(id).child("isManager");
        zonesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                is_manager = dataSnapshot.getValue(boolean.class);
                fireBaseListener.success(is_manager);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fireBaseListener.error(databaseError.toException().toString());

            }
        });
    }


/*

    public void add_hash(String add_hash){
        DatabaseReference  dbRef = myDB.getReference("deneme_tablo2");
        dbRef.setValue(add_hash.hashCode());
    }

    public void isRegistered(){
    }
*/


    private void read(String id, FireBaseListener fireBaseListener) {

        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("employee").child(id).child("password");
        zonesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    password_db = dataSnapshot.getValue(String.class);
                    fireBaseListener.success(password_db);
                }
                else if (!dataSnapshot.exists()) {
                    Toast.makeText(login_screen.this, "Id not found!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fireBaseListener.error(databaseError.toException().toString());
            }
        });


    }


    public void login() {
        String id = id_edit.getText().toString();
        String pass = pass_edit.getText().toString();


        if (TextUtils.isEmpty(id) && TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "You should enter your id and password!", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(id)) {
            Toast.makeText(this, "You should enter your id!", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "You should enter your password!", Toast.LENGTH_LONG).show();
        } else if (!pass.equals(pass)) {
            Toast.makeText(this, "Better check your id or password again :( ", Toast.LENGTH_LONG).show();
        } else {
            read(id, new FireBaseListener() {
                @Override
                public void success(Object value) {
                    String db_pass = (String) value;
                    if (!db_pass.equals(pass)){
                        Toast.makeText(login_screen.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    isManager(id, new FireBaseListener() {
                        @Override
                        public void success(Object value) {
                            if (!is_manager) {
                                Intent intent = new Intent(getApplicationContext(), employee_menu.class);
                                intent.putExtra("id", id);
                                intent.putExtra("pass", db_pass);
                                startActivity(intent);

                            } else if (is_manager) {
                          //      Toast.makeText(login_screen.this, "manager page", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), manager_menu.class);
                                intent.putExtra("id", id);
                                intent.putExtra("pass", db_pass);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void error(String errorMsg) {
                            Toast.makeText(login_screen.this, "Error:"+errorMsg, Toast.LENGTH_SHORT).show();
                        }
                    });


                }

                @Override
                public void error(String errorMsg) {
                    Toast.makeText(login_screen.this, "Error:" + errorMsg, Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

    private void createTable(int capacity, boolean isOccupied) {

        DatabaseReference dbRef = myDB.getReference("table");
        Table table = new Table(capacity, isOccupied);

        dbRef.child(String.valueOf(i)).setValue(table);
        i += 1;
    }


    public class Table {
        public int capacity;
        public boolean isOccupied;

        public Table() {

        }

        public Table(int capacity, boolean isOccupied) {
            this.capacity = capacity;
            this.isOccupied = isOccupied;
        }


    }


}