package com.example.paprikapp_new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class menuItems extends AppCompatActivity {
    private FirebaseDatabase myDB;
    int j;
    ImageButton appt, mainCo, sal, des, dri, alc;
    int i=0;
    LinearLayout see_ord,see_ord2;
    TextView totalAmount;
    Button payment;
    String tableID;
    double value;
    ImageButton main10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_items);


        Intent intent = getIntent();
        j = intent.getIntExtra("order_id",0);
        tableID = intent.getStringExtra("tableName");

        myDB = FirebaseDatabase.getInstance();
        see_ord = (LinearLayout) findViewById(R.id.see_ord);
        see_ord2 = (LinearLayout) findViewById(R.id.see_ord2);
        totalAmount = (TextView) findViewById(R.id.totalAmount);
        payment = (Button) findViewById(R.id.payment);


        appt = (ImageButton) findViewById(R.id.appt);
        mainCo = (ImageButton) findViewById(R.id.mainCo);
        sal = (ImageButton) findViewById(R.id.sal);
        des = (ImageButton) findViewById(R.id.des);
        dri = (ImageButton) findViewById(R.id.dri);
        alc = (ImageButton) findViewById(R.id.alc);


        appt.setImageResource(R.drawable.appetizer);
       mainCo.setImageResource(R.drawable.maincourse);
        sal.setImageResource(R.drawable.salad);
        dri.setImageResource(R.drawable.drink);
        des.setImageResource(R.drawable.dessert);
        alc.setImageResource(R.drawable.alcohol);
        payment.setVisibility(View.INVISIBLE);
        allOrder();

        main10 = (ImageButton) findViewById(R.id.main10);

        main10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        appt.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), appetizer.class);
                intent.putExtra("order_id", j);
                startActivity(intent);

            }

        });

        mainCo.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), mainCourse.class);
                intent.putExtra("order_id", j);
                startActivity(intent);

            }

        });

        sal.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), salad.class);
                intent.putExtra("order_id", j);
                startActivity(intent);

            }

        });

        des.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), dessert.class);
                intent.putExtra("order_id", j);
                startActivity(intent);

            }

        });

        dri.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), drinks.class);
                intent.putExtra("order_id", j);
                startActivity(intent);

            }

        });

        alc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), alcohol.class);
                intent.putExtra("order_id", j);
                startActivity(intent);

            }
        });



        payment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

             addRegister(Double.valueOf(totalAmount.getText().toString()),j);



                see_ord2.removeAllViews();
                payment.setVisibility(View.INVISIBLE);
                updateTable(tableID,"false");
                totalAmount.setText("");

                Intent intent = new Intent(getApplicationContext(), Order.class);

                startActivity(intent);

            }
        });
    }


    public void allOrder(){

        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("order").child(String.valueOf(j)).child("orderedItems");

        zonesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                see_ord2.removeAllViews();
                Iterable<DataSnapshot> keys = dataSnapshot.getChildren();
                for (DataSnapshot key: keys) {

                    Button btn = new Button(menuItems.this);
                    btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    btn.setId(i);
                    btn.setText(key.getKey()+" "+key.child("quantity").getValue());
                    btn.setWidth(150);
                    btn.setHeight(100);
                    btn.setClickable(false);
                    payment.setVisibility(View.VISIBLE);
                    see_ord2.addView(btn);
                    DatabaseReference zonesRef2 = FirebaseDatabase.getInstance().getReference("order").child(String.valueOf(j));
                    zonesRef2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

if(snapshot.exists()){

    value = snapshot.child("totalPrice").getValue(Double.class);
    value =Double.parseDouble(new DecimalFormat("##.###").format(value));
    totalAmount.setText(String.valueOf(value));

}  else {
    totalAmount.setText("");
}

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    i++;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private void addRegister(double orderPrice, int orderId) {

        j =  (int)(Math.random()*10000);
        DatabaseReference dbRef = myDB.getReference("cashRegister");
       long  now = System.currentTimeMillis() - 1000;


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH)+1;
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        String date = mDay+"/"+mMonth+"/"+mYear;
        cashRegister cash = new cashRegister(orderPrice,orderId, date);
        dbRef.child(String.valueOf(j)).setValue(cash);
        Toast toast = Toast.makeText(menuItems.this,"PAYMENT SUCCESSFULL", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP|Gravity.LEFT, 0, 0);
        toast.show();
    }

    public void updateTable(String tableID,String isOccupied){
        DatabaseReference db_ref = myDB.getReference("table").child(tableID).child("isOccupied");
        db_ref.setValue(isOccupied);
    }

    public class cashRegister{

        public double orderPrice;
        public int orderId;
        public  String currentTime;

        public cashRegister(){

        }

        public cashRegister(double orderPrice, int orderId,String currentTime ){

            this.orderPrice=orderPrice;
            this.orderId=orderId;
            this.currentTime=currentTime;
        }
    }
}