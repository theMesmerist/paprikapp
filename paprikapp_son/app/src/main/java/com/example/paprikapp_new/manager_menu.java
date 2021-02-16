package com.example.paprikapp_new;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class manager_menu extends AppCompatActivity {
    private FirebaseDatabase myDB;
    int i;
    int temp;
    String error, id, name, emp_name, db_pa;
    TextView welcome;
    ImageButton main3;
    ImageView back;
    Button report, newEmployee, seeEmployee, create, cancel_create, logout, m_change_now, m_cancel_change, m_change, addItem;
    EditText names, email, phone, address, salary, m_old_pass, m_new_pass, m_new_pass_again;
    CheckBox isManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_menu);


        myDB = FirebaseDatabase.getInstance();
        main3 = (ImageButton) findViewById(R.id.main3);
        welcome = (TextView) findViewById(R.id.welcome);

        back = (ImageView) findViewById(R.id.back);

        report = (Button) findViewById(R.id.report);
        newEmployee = (Button) findViewById(R.id.newEmployee);
        seeEmployee = (Button) findViewById(R.id.seeEmployees);
        create = (Button) findViewById(R.id.createNew);
        logout = (Button) findViewById(R.id.logout);
        m_change = (Button) findViewById(R.id.m_change);
        cancel_create = (Button) findViewById(R.id.cancel_create);
        addItem = (Button) findViewById(R.id.addItem);

        m_change_now = (Button) findViewById(R.id.m_change_now);
        m_cancel_change = (Button) findViewById(R.id.m_cancel_change);

        names = (EditText) findViewById(R.id.names);
        email = (EditText) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.phone);
        address = (EditText) findViewById(R.id.address);
        isManager = (CheckBox) findViewById(R.id.isManager);
        salary = (EditText) findViewById(R.id.salary);

        m_old_pass = (EditText) findViewById(R.id.m_old_pass);
        m_new_pass = (EditText) findViewById(R.id.m_new_pass);
        m_new_pass_again = (EditText) findViewById(R.id.m_new_pass_again);


        main3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(manager_menu.this);
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


/////////// getting the users id and the password from previous page

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        db_pa = intent.getStringExtra("pass");
        name = getName(id);
        ////////////////////////////  used handler for db delay
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                welcome.setText(" Welcome! \n " + getName(id));
            }
        }, 300);

////////////////////////////// logout button, user goes previous page

        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), login_screen.class);

                startActivity(intent);
            }
        });

        seeEmployee.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), all_employees.class);

                startActivity(intent);
            }
        });


        report.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), dailyReport.class);

                startActivity(intent);
            }
        });



        addItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), addItem.class);

                startActivity(intent);
            }
        });

/////////////////////// create new employee part

        newEmployee.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (m_old_pass.getVisibility() == View.VISIBLE) {
                    inv_change();
                    vis_create_emp();
                } else if (names.getVisibility() == View.INVISIBLE) {
                    vis_create_emp();
                } else {
                    inv_create_emp();
                }
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                create();
                clear_create();
            }
        });

        cancel_create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clear_create();
                inv_create_emp();
            }
        });


///////////////////////// change password part

        m_change.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (names.getVisibility() == View.VISIBLE) {
                    inv_create_emp();
                    vis_change();
                } else if (m_old_pass.getVisibility() == View.INVISIBLE) {
                    vis_change();
                } else {
                    inv_change();
                }
            }

        });


        m_change_now.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                m_changePassword();
            }
        });


        m_cancel_change.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clear_change();
                inv_change();
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

    public void inv_create_emp() {
        back.setVisibility(View.INVISIBLE);
        names.setVisibility(View.INVISIBLE);
        phone.setVisibility(View.INVISIBLE);
        email.setVisibility(View.INVISIBLE);
        address.setVisibility(View.INVISIBLE);
        isManager.setVisibility(View.INVISIBLE);
        salary.setVisibility(View.INVISIBLE);
        create.setVisibility(View.INVISIBLE);
        cancel_create.setVisibility(View.INVISIBLE);
    }

    public void vis_create_emp() {
        back.setVisibility(View.VISIBLE);
        names.setVisibility(View.VISIBLE);
        phone.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        address.setVisibility(View.VISIBLE);
        isManager.setVisibility(View.VISIBLE);
        salary.setVisibility(View.VISIBLE);
        create.setVisibility(View.VISIBLE);
        cancel_create.setVisibility(View.VISIBLE);
    }


    public void m_changePassword() {
        String np, na, op;
        np = m_new_pass.getText().toString();
        na = m_new_pass_again.getText().toString();
        op = m_old_pass.getText().toString();

        if (np.isEmpty() || na.isEmpty() || op.isEmpty()) {
            Toast.makeText(this, "fields cannot be empty", Toast.LENGTH_LONG).show();
        } else if (!na.equals(np)) {
            Toast.makeText(this, "passwords does not match", Toast.LENGTH_LONG).show();
            clear_change();
        } else if (!db_pa.equals(op)) {
            Toast.makeText(this, "old password is wrong", Toast.LENGTH_LONG).show();
            clear_change();
        } else if (db_pa.equals(np)) {
            Toast.makeText(this, "try another password! different from the old one", Toast.LENGTH_LONG).show();
            clear_change();
        } else {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference mDatabaseRef = database.getReference();
            mDatabaseRef.child("employee").child(id).child("password").setValue(np);
            Toast.makeText(this, "password changed successfully", Toast.LENGTH_LONG).show();
            clear_change();
            inv_change();
        }


    }

//////////////// clears all inputs of edit texts

    public void clear_change() {
        m_old_pass.getText().clear();
        m_new_pass.getText().clear();
        m_new_pass_again.getText().clear();
    }

    //////////////// clears all inputs of edit texts
    public void clear_create() {
        names.getText().clear();
        phone.getText().clear();
        email.getText().clear();
        address.getText().clear();
        salary.getText().clear();
    }


    public void inv_change() {
        back.setVisibility(View.INVISIBLE);
        m_change_now.setVisibility(View.INVISIBLE);
        m_cancel_change.setVisibility(View.INVISIBLE);
        m_old_pass.setVisibility(View.INVISIBLE);
        m_new_pass.setVisibility(View.INVISIBLE);
        m_new_pass_again.setVisibility(View.INVISIBLE);

    }

    public void vis_change() {
        back.setVisibility(View.VISIBLE);
        m_change_now.setVisibility(View.VISIBLE);
        m_cancel_change.setVisibility(View.VISIBLE);
        m_old_pass.setVisibility(View.VISIBLE);
        m_new_pass.setVisibility(View.VISIBLE);
        m_new_pass_again.setVisibility(View.VISIBLE);
    }


    public void writeNewEmployee(String name, String email, String password, long phone, String address, boolean isManager, double salary) {

        DatabaseReference dbRef = myDB.getReference("employee");
        Employee employee = new Employee(name, email, password, phone, address, isManager, salary);
        dbRef.child(String.valueOf(i)).setValue(employee);


    }

    private void create() {
        i = temp = (int) (Math.random() * 10000);
        String v1 = names.getText().toString();
        String v2 = email.getText().toString();
        String v3 = String.valueOf(i);
        Editable v4 = phone.getText();
        String v5 = address.getText().toString();
        Boolean v6 = isManager.isChecked();
        Editable v7 = salary.getText();

        if (v1.isEmpty() || v2.isEmpty() || v3.isEmpty() || v4 == null || v5.isEmpty() || v7 == null) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_LONG).show();
        } else if (!v2.contains("@") || !v2.contains(".com")) {
            Toast.makeText(this, "Please enter an valid email adress", Toast.LENGTH_LONG).show();
        } else if (v4.length() < 10) {
            Toast.makeText(this, "Please enter an valid phone number", Toast.LENGTH_LONG).show();
        } else {

            writeNewEmployee(v1, v2, v3, Long.valueOf(v4.toString()), v5, v6, Double.valueOf(v7.toString()));

            AlertDialog.Builder builder = new AlertDialog.Builder(manager_menu.this);
            builder.setCancelable(true);
            builder.setTitle("New employee has created successfully");
            builder.setMessage("Employee ID : " + i + "\nEmployee Password : " + i);
            builder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });


            AlertDialog dialog = builder.create();
            dialog.show();
            inv_create_emp();

        }


    }

    public class Employee {

        public String name;
        public String email;
        public String password;
        public long phone;
        public String address;
        public boolean isManager;
        public double salary;


        public Employee() {

        }

        public Employee(String name, String email, String password, long phone, String address, boolean isManager, double salary) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.phone = phone;
            this.address = address;
            this.isManager = isManager;
            this.salary = salary;
        }
    }


}

