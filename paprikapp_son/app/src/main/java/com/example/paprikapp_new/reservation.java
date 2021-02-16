package com.example.paprikapp_new;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class reservation extends AppCompatActivity {
    private FirebaseDatabase myDB;
    Button crt_reservation;
    Button see_res;
    static int i;
    TextView view_tableid,view_resname,view_resPhone,peopleNumV,abc;
    EditText text_resname,text_resPhone,peopleNum;
    private      DatabaseReference dbRefRes;
    TimePicker picker;
    Button btnGet;
    TextView tvw;

    Spinner dropdown;
    DatePicker pickerr;
    Button btnGett;
    TextView tvww;
    int table_id;
    int capacity;
ImageButton main10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);
        myDB = FirebaseDatabase.getInstance();

        dbRefRes =  myDB.getReference("reservation");
        crt_reservation = (Button) findViewById(R.id.crt_reservation);
        see_res = (Button) findViewById(R.id.see_res);
        view_tableid = (TextView) findViewById(R.id.view_tableid);
        view_resname = (TextView) findViewById(R.id.view_resname);
        view_resPhone = (TextView) findViewById(R.id.view_resPhone);

        peopleNumV = (TextView) findViewById(R.id.peopleNumV);

        abc = (TextView) findViewById(R.id.abc);


        tvw=(TextView)findViewById(R.id.textView1);
        picker=(TimePicker)findViewById(R.id.timePicker1);
        picker.setIs24HourView(true);
        picker.setMinute(00);
        btnGet=(Button)findViewById(R.id.button1);

        dropdown = findViewById(R.id.spinnerTable);
        String[] items = new String[]{"1","2","3","4","5","6"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        tvww=(TextView)findViewById(R.id.textView2);
        pickerr=(DatePicker)findViewById(R.id.datePicker1);
        btnGett=(Button)findViewById(R.id.button2);
        pickerr.setMinDate(System.currentTimeMillis() + 10000);


        text_resname = (EditText) findViewById(R.id.text_resname);
        text_resPhone = (EditText) findViewById(R.id.text_resPhone);
        peopleNum = (EditText) findViewById(R.id.peopleNum);

        main10 = (ImageButton) findViewById(R.id.main10);

        main10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        see_res.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { Handler handler = new Handler();
                Intent intent = new Intent(getApplicationContext(), see_res.class);
                startActivity(intent);
            }
        });


        picker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener()
        {

            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                //rezervasyon saatleri 12:00-22:00 arası
                if(hourOfDay<12){
                    picker.setHour(12);
                }else if(hourOfDay>22){
                    picker.setHour(22);
                }
                //rezarvasyon sadece tam saatlerde yapılabilir örnk: 13:00, 13:30'a rezervasyon kabul edilmez
                if(minute>=00) {
                    picker.setMinute(00);
                }
            }

        });

        crt_reservation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createRes(new FireBaseListener() {
                    @Override
                    public void success(Object value) {
                        Toast.makeText(reservation.this, "Yeey!: "+value, Toast.LENGTH_SHORT).show();
                        abc.setText("emre");
                    }

                    @Override
                    public void error(String errorMsg) {
                        Toast.makeText(reservation.this, "Oops!:\n"+errorMsg, Toast.LENGTH_SHORT).show();
                    }
                }


                );
            }
        });


        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int hour, minute;


                hour = picker.getHour();
                minute = picker.getMinute();


                tvw.setText(hour +":"+ minute+"0");
            }
        });




        btnGett.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int day,month,year;
                day = pickerr.getDayOfMonth();
                month= pickerr.getMonth();
                year = pickerr.getYear();


                tvww.setText(day+"/"+ (month + 1)+"/"+year);

            }
        });


    }


    private void createRes(FireBaseListener listener) {




        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("reservation");
        String res_Name= text_resname.getText().toString();
        String res_Phone= text_resPhone.getText().toString();
        String date = tvww.getText().toString();
        String table_id = dropdown.getSelectedItem().toString();
        String time = tvw.getText().toString();
        String numOfPeople = peopleNum.getText().toString();

        ref.orderByChild("resTableId");


        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("table").child(table_id);

        ref2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                capacity = snapshot.child("capacity").getValue(Integer.class);
                //Log.d("num: ", String.valueOf(capacity));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                i =  (int)(Math.random()*10000);

                int a = Integer.parseInt(numOfPeople);

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    Reservation res= dsp.getValue(Reservation.class);
                    String currentDate = res.getResDate();
                    String currentTime  = res.getResTime();
                    String tableIdNow = res.getResTableId();

                    if (currentDate.equals(date) && currentTime.equals(time) && tableIdNow.equals(table_id)){
                        listener.error("Table is occupied!");
                        abc.setText("nope1");
                        return;
                    }

                }
                if(res_Name.isEmpty() || res_Phone.isEmpty()||date.isEmpty() || time.isEmpty()|| numOfPeople.isEmpty()){
                    listener.error("Please fill all the blanks!");

                    abc.setText("nope2");
                }
                else if(a>capacity){
                    listener.error("Sorry, not enough capacity for this table only "+capacity+" people.");
                    abc.setText("nope3");
                    return;
                }else if(a<=0){
                    listener.error("Sorry, capacity must be positive!");
                    abc.setText("nope4");
                    return;
                }

                else {
                    reservation.Reservation reser = new Reservation(res_Name, res_Phone, date, table_id, time, numOfPeople);
                    dbRefRes.child(String.valueOf(i)).setValue(reser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            i += 1;
                            abc.setText("reserved");
                            listener.success("Reservation created! ;)");
                        }
                    });
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }






/*
    public void createResf(){
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("reservation");

        String res_Name= text_resname.getText().toString();
        String res_Phone= text_resPhone.getText().toString();
        String date = tvww.getText().toString();
        String table_id = dropdown.getSelectedItem().toString();
        String time = tvw.getText().toString();

        zonesRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Iterable<DataSnapshot> keys = dataSnapshot.getChildren();
                for (DataSnapshot key : keys) {

                    String db_date = key.child("resDate").getValue().toString();
                    String db_table_id = key.child("resTableId").getValue().toString();
                    String db_time = key.child("resTime").getValue().toString();



                    if (TextUtils.isEmpty(date) && TextUtils.isEmpty(time)){
                        tvv.setText("empty date time");

                    }  else if (TextUtils.isEmpty(date)){
                        tvv.setText("empty date ");

                    }   else if (TextUtils.isEmpty(time)){
                        tvv.setText("empty time");

                    }   else if (table_id.equals(db_table_id) && date.equals(db_date) && time.equals(db_time)){
                        tvv.setText("full");

                    }   else {
                    reservation.Reservation res = new reservation.Reservation(res_Name, res_Phone, date, table_id, time);
                    zonesRef.child(String.valueOf(i)).setValue(res);
                    i += 1;
                    tvv.setText(":)");

                }

            }


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

*/

    public static class Reservation {
        public String resName;
        public String resPhone;
        public String resDate;
        public String resTableId;
        public String resTime;
        public String resNumOfPeople;

        public String getResTableId() {
            return resTableId;
        }

        public String getResDate() {
            return resDate;
        }
        public String getResTime() {
            return resTime;
        }


        public Reservation() {
        }

        public Reservation(String resName, String resPhone, String resDate, String resTableId, String resTime,String resNumOfPeople) {
            this.resName = resName;
            this.resPhone = resPhone;
            this.resDate = resDate;
            this.resTableId = resTableId;
            this.resTime = resTime;
            this.resNumOfPeople = resNumOfPeople;
        }
    }

}

