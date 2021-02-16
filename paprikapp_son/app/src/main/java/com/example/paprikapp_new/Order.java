package com.example.paprikapp_new;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Order extends AppCompatActivity {

    private FirebaseDatabase myDB;
    static int i = 444;
    int j =  (int)(Math.random()*10000);
    int temp;
    boolean gh,one,two,three,four,five,six ;
    int reed,green;
    String isOccupied, occu;
    ImageButton main10;
  public   ImageButton table1, table2, table3, table4, table5, table6;
    String keyyy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        myDB = FirebaseDatabase.getInstance();
        table1  = (ImageButton) findViewById(R.id.table1);
        table2 = (ImageButton) findViewById(R.id.table2);
        table3 = (ImageButton) findViewById(R.id.table3);
        table4 = (ImageButton) findViewById(R.id.table4);
        table5 = (ImageButton) findViewById(R.id.table5);
        table6 = (ImageButton) findViewById(R.id.table6);

        main10 = (ImageButton) findViewById(R.id.main10);

        main10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

              Intent intent = new Intent(getApplicationContext(), MainActivity.class);
              startActivity(intent);
            }
        });



        lisOccupied("1", new FireBaseListener() {
            @Override
            public void success(Object value) {
                one=Boolean.valueOf(occu);

                if(one==true){
                    Context context = table1.getContext();
                    reed = context.getResources().getIdentifier("red","drawable",context.getPackageName());
                    table1.setImageResource(reed);
                }
                else {
                    Context context2 = table1.getContext();
                    green = context2.getResources().getIdentifier("green","drawable",context2.getPackageName());
                    table1.setImageResource(green);
                }
            }

            @Override
            public void error(String errorMsg) {

            }
        });


        lisOccupied("2", new FireBaseListener() {
            @Override
            public void success(Object value) {
                two=Boolean.valueOf(occu);
                if(two==true){
                    Context context = table2.getContext();
                    reed = context.getResources().getIdentifier("red","drawable",context.getPackageName());
                    table2.setImageResource(reed);
                }
                else {
                    Context context2 = table2.getContext();
                    green = context2.getResources().getIdentifier("green","drawable",context2.getPackageName());
                    table2.setImageResource(green);
                }
            }

            @Override
            public void error(String errorMsg) {

            }
        });


        lisOccupied("3", new FireBaseListener() {
            @Override
            public void success(Object value) {
                three=Boolean.valueOf(occu);
                if(three==true){
                    Context context = table3.getContext();
                    reed = context.getResources().getIdentifier("red","drawable",context.getPackageName());
                    table3.setImageResource(reed);
                }
                else {
                    Context context2 = table3.getContext();
                    green = context2.getResources().getIdentifier("green","drawable",context2.getPackageName());
                    table3.setImageResource(green);
                }
            }

            @Override
            public void error(String errorMsg) {

            }
        });

        lisOccupied("4", new FireBaseListener() {
            @Override
            public void success(Object value) {
                four=Boolean.valueOf(occu);


                if(four==true){
                    Context context = table4.getContext();
                    reed = context.getResources().getIdentifier("red","drawable",context.getPackageName());
                    table4.setImageResource(reed);
                }
                else {
                    Context context2 = table4.getContext();
                    green = context2.getResources().getIdentifier("green","drawable",context2.getPackageName());
                    table4.setImageResource(green);
                }

            }

            @Override
            public void error(String errorMsg) {

            }
        });

        lisOccupied("5", new FireBaseListener() {
            @Override
            public void success(Object value) {
                five=Boolean.valueOf(occu);

                if(five==true){
                    Context context = table5.getContext();
                    reed = context.getResources().getIdentifier("red","drawable",context.getPackageName());
                    table5.setImageResource(reed);
                }
                else {
                    Context context2 = table5.getContext();
                    green = context2.getResources().getIdentifier("green","drawable",context2.getPackageName());
                    table5.setImageResource(green);
                }

            }

            @Override
            public void error(String errorMsg) {

            }
        });

        lisOccupied("6", new FireBaseListener() {
            @Override
            public void success(Object value) {
                six=Boolean.valueOf(occu);


                if(six==true){
                    Context context = table6.getContext();
                    reed = context.getResources().getIdentifier("red","drawable",context.getPackageName());
                    table6.setImageResource(reed);
                }
                else {
                    Context context2 = table6.getContext();
                    green = context2.getResources().getIdentifier("green","drawable",context2.getPackageName());
                    table6.setImageResource(green);
                }
            }
            @Override
            public void error(String errorMsg) {

            }
        });


        table1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                getParentMode("1");
                gh =    isOccupied("1");


                Context context = table1.getContext();
                int full_id = context.getResources().getIdentifier("red","drawable",context.getPackageName());

                if( !gh){
                    updateTable("1","true");
                    temp =j;
                    createOrder("1",0);
                    Intent intent = new Intent(getApplicationContext(), menuItems.class);
                    intent.putExtra("order_id", temp);
                    intent.putExtra("tableName", "1");
                    startActivity(intent);
                    table1.setImageResource(full_id);
                } else if(gh){
                    getParentMode("1");
                    Intent intent = new Intent(getApplicationContext(), menuItems.class);
                    intent.putExtra("tableName", "1");
                    intent.putExtra("order_id",Integer.valueOf(keyyy));
                    startActivity(intent);
                }

            }
        });


        table2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                getParentMode("2");
                gh =    isOccupied("2");

                Context context = table2.getContext();

                int full_id = context.getResources().getIdentifier("red","drawable",context.getPackageName());
                //        table2.setImageResource(full_id);

                if( !gh){
                    updateTable("2","true");
                    temp =j;
                    createOrder("2",0);
                    Intent intent = new Intent(getApplicationContext(), menuItems.class);
                    intent.putExtra("order_id", temp);
                    intent.putExtra("tableName", "2");
                    startActivity(intent);
                    table2.setImageResource(full_id);
                } else if(gh){
                    getParentMode("2");
                    Intent intent = new Intent(getApplicationContext(), menuItems.class);
                    intent.putExtra("tableName", "2");
                    intent.putExtra("order_id",Integer.valueOf(keyyy));
                    startActivity(intent);
                }

            }
        });

        table3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                getParentMode("3");
                gh = isOccupied("3");

                Context context = table3.getContext();

                int full_id = context.getResources().getIdentifier("red","drawable",context.getPackageName());
                //          table3.setImageResource(full_id);

                if(!gh){
                    updateTable("3","true");
                    temp =j;
                    createOrder("3",0);
                    Intent intent = new Intent(getApplicationContext(), menuItems.class);
                    intent.putExtra("order_id", temp);
                    intent.putExtra("tableName", "3");
                    startActivity(intent);
                    table3.setImageResource(full_id);
                } else if(gh){
                    getParentMode("3");
                    Intent intent = new Intent(getApplicationContext(), menuItems.class);
                    intent.putExtra("tableName", "3");
                    intent.putExtra("order_id",Integer.valueOf(keyyy));
                    startActivity(intent);
                }
            }
        });

        table4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                getParentMode("4");
                gh =    isOccupied("4");

                Context context = table4.getContext();

                int full_id = context.getResources().getIdentifier("red","drawable",context.getPackageName());
                //          table4.setImageResource(full_id);

                if(!gh){
                    updateTable("4","true");
                    temp =j;
                    createOrder("4",0);
                    Intent intent = new Intent(getApplicationContext(), menuItems.class);
                    intent.putExtra("order_id", temp);
                    intent.putExtra("tableName", "4");
                    startActivity(intent);
                    table4.setImageResource(full_id);
                } else if(gh){
                    getParentMode("4");
                    Intent intent = new Intent(getApplicationContext(), menuItems.class);
                    intent.putExtra("tableName", "4");
                    intent.putExtra("order_id",Integer.valueOf(keyyy));
                    startActivity(intent);
                }
            }
        });

        table5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                getParentMode("5");
                gh =    isOccupied("5");


                Context context = table5.getContext();
                int full_id = context.getResources().getIdentifier("red","drawable",context.getPackageName());

                if( !gh){
                    updateTable("5","true");
                    temp =j;
                    createOrder("5",0);
                    Intent intent = new Intent(getApplicationContext(), menuItems.class);
                    intent.putExtra("order_id", temp);
                    intent.putExtra("tableName", "5");
                    startActivity(intent);
                    table5.setImageResource(full_id);
                } else if(gh){
                    getParentMode("5");
                    Intent intent = new Intent(getApplicationContext(), menuItems.class);
                    intent.putExtra("tableName", "5");
                    intent.putExtra("order_id",Integer.valueOf(keyyy));
                    startActivity(intent);
                }

            }
        });



        table6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                getParentMode("6");
                gh =    isOccupied("6");


                Context context = table6.getContext();
                int full_id = context.getResources().getIdentifier("red","drawable",context.getPackageName());

                if( !gh){
                    updateTable("6","true");
                    temp =j;
                    createOrder("6",0);
                    Intent intent = new Intent(getApplicationContext(), menuItems.class);
                    intent.putExtra("order_id", temp);
                    intent.putExtra("tableName", "6");
                    startActivity(intent);
                    table6.setImageResource(full_id);
                } else if(gh){
                    getParentMode("6");
                    Intent intent = new Intent(getApplicationContext(), menuItems.class);
                    intent.putExtra("tableName", "6");
                    intent.putExtra("order_id",Integer.valueOf(keyyy));
                    startActivity(intent);
                }

            }
        });


    }


    private void createOrder(String tableID ,double totalPrice) {


        DatabaseReference db_ref = myDB.getReference("order");
        order orderr = new order(tableID,totalPrice);
        db_ref.child(String.valueOf(j)).setValue(orderr);

        j += 1 ;


    }

    public class item{
        public String itemName;
        public double itemPrice;
        public int maxQuantity;




        public item(){

        }

        public item(String itemName, double itemPrice, int maxQuantity){
            this.itemName=itemName;
            this.itemPrice=itemPrice;
            this.maxQuantity=maxQuantity;

        }
    }
    private void createItem(String itemName, double itemPrice, int maxQuantity, String itemType) {


        DatabaseReference dbRef = myDB.getReference("item").child(itemType);
        item Item = new item(itemName,itemPrice,maxQuantity);

        dbRef.child(String.valueOf(j)).setValue(Item);


    }

    public class order {

        public String tableID;
        public double totalPrice;


        public order(){

        }

        public order( String tableID,double totalPrice){
            this.totalPrice=totalPrice;
            this.tableID=tableID;
        }


    }
    private boolean isOccupied(String tableID){

        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("table").child(tableID).child("isOccupied");
        zonesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    isOccupied = dataSnapshot.getValue(String.class);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        return  Boolean.valueOf(isOccupied);
    }


    public void lisOccupied(String tableID, FireBaseListener fireBaseListener){
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("table").child(tableID).child("isOccupied");
        zonesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    occu = dataSnapshot.getValue(String.class);
                    fireBaseListener.success(occu);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                fireBaseListener.error("error");
            }
        });

    }


    public void getParentMode(String tableID){
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        Query query = rootRef.child("order").orderByChild("tableID").equalTo(tableID);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    keyyy = ds.getKey();
                    Log.d("TAG", keyyy);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("TAG", databaseError.getMessage());
            }
        };
        query.addListenerForSingleValueEvent(valueEventListener);

    }

    public void updateTable(String tableID,String isOccupied){
        DatabaseReference db_ref = myDB.getReference("table").child(tableID).child("isOccupied");
        db_ref.setValue(isOccupied);
    }


}