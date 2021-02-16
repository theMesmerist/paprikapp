package com.example.paprikapp_new;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class drinks extends AppCompatActivity {

    private FirebaseDatabase myDB;
    static int i = 444;
    static int b = 555;
    static int j;
    int quantity;
    boolean a;
    LinearLayout lin_des, lin2_des, lin_ord;
    Button add_des, del_ord;
    ImageButton main4_des;
    TextView tv_des, name_view_des;
    EditText quan;
    boolean f;
    double total;
    int ccc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinks);
        Intent intent = getIntent();
        j = intent.getIntExtra("order_id", 6);

        lin_des = (LinearLayout) findViewById(R.id.lin2_alc);
        lin2_des = (LinearLayout) findViewById(R.id.lin_alc);
        lin_ord = (LinearLayout) findViewById(R.id.lay_ord);
        tv_des = (TextView) findViewById(R.id.tv_des);
        name_view_des = (TextView) findViewById(R.id.name_view_des);
        main4_des = (ImageButton) findViewById(R.id.main10);
        quan = (EditText) findViewById(R.id.quan);
        add_des = (Button) findViewById(R.id.add_order);
        del_ord = (Button) findViewById(R.id.del_ord);

        myDB = FirebaseDatabase.getInstance();

        i = 0;
        ccc = 0;
        quantity = 0;
        main4_des = (ImageButton) findViewById(R.id.main10);

        main4_des.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        listAlcohol();

    }

    public void listAlcohol() {

        Intent intent = getIntent();
        j = intent.getIntExtra("order_id", 6);
        lin_ord.removeAllViews();

        f = false;
        quantity = 0;
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("item").child("drinks");

        zonesRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Iterable<DataSnapshot> keys = dataSnapshot.getChildren();
                for (DataSnapshot key : keys) {
                    getTotal();
                    Button btn = new Button(drinks.this);
                    ImageView iv = new ImageView(drinks.this);
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

                    btn.setText(key.child("itemName").getValue().toString());
                    String itemName = key.child("itemName").getValue().toString();
                    int quantity = Integer.valueOf(quan.getText().toString());
                    double price = key.child("itemPrice").getValue(Double.class);
                    int maxQ = key.child("maxQuantity").getValue(Integer.class);


                    btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ccc = 0;
                            getTotal();
                            add_des.setVisibility(View.VISIBLE);
                            del_ord.setVisibility(View.VISIBLE);
                            getQuantity(itemName);
                            add_des.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {

                                            if (Integer.valueOf(quan.getText().toString()) <= maxQ) {
                                                addToOrder(itemName, Integer.valueOf(quan.getText().toString()) + ccc);
                                                Toast toast = Toast.makeText(drinks.this,"Succesfully added "+Integer.valueOf(quan.getText().toString())+" "+itemName, Toast.LENGTH_LONG);
                                                toast.show();
                                                getTotal();
                                            } else {
                                                Toast toast = Toast.makeText(drinks.this, "We don't have that much, you can only have " + maxQ + " of it", Toast.LENGTH_LONG);
                                                toast.show();

                                            }
                                        }
                                    }, 500);


                                    //       Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {

                                            increaseTotal(price * Integer.valueOf(quan.getText().toString()));
                                            //           setQuantity(itemName,quantity+ccc);             //         aynı adda item varsa çalışmalı
                                        }
                                    }, 500);


                                }
                            });

                            del_ord.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    getTotal();
                                    getQuantity(itemName);
                                    if(total!=0){
                                        decreaseTotal(price*ccc);
                                    }

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        public void run() {


                                            removeNode(itemName);
                                        }
                                    }, 300);


                                }
                            });
                        }
                    });

                    lin2_des.addView(btn);
                    lin2_des.addView(iv);
                    i++;


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }


    public void addToOrder(String item_id, int quantity) {

        DatabaseReference db_ref = myDB.getReference("order").child(String.valueOf(j)).child("orderedItems").child(item_id);
//item itemm = new item(item_id,ite)
        db_ref.child("quantity").setValue(quantity);

    }

    public void getTotal() {

        DatabaseReference db_ref = myDB.getReference("order").child(String.valueOf(j)).child("totalPrice");

        db_ref.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {

                total = snapshot.getValue(Double.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void getQuantity(String itemName) {

        DatabaseReference db_ref = myDB.getReference("order").child(String.valueOf(j)).child("orderedItems").child(itemName);

        db_ref.addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    ccc = snapshot.child("quantity").getValue(Integer.class);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setQuantity(String itemName, int quantity) {

        DatabaseReference db_ref = myDB.getReference("order").child(String.valueOf(j)).child("orderedItems").child(itemName).child("quantity");

        db_ref.setValue(quantity);

    }


    public void increaseTotal(double plus) {

        DatabaseReference db_ref = myDB.getReference("order").child(String.valueOf(j)).child("totalPrice");

        db_ref.setValue(total + plus);

    }

    public void decreaseTotal(double plus) {

        DatabaseReference db_ref = myDB.getReference("order").child(String.valueOf(j)).child("totalPrice");

        db_ref.setValue(total - plus);

    }


    private void createItem(String itemName, double itemPrice, int maxQuantity, String itemType) {


        DatabaseReference dbRef = myDB.getReference("item").child(itemType);
        item Item = new item(itemName, itemPrice, maxQuantity);

        dbRef.child(String.valueOf(j)).setValue(Item);
        j += 1;


    }


    public void removeNode(String item_name) {
        DatabaseReference zonesRef = FirebaseDatabase.getInstance().getReference("order");

        zonesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null && snapshot.getValue() != null) {
                    zonesRef.child(String.valueOf(j)).child("orderedItems").child(item_name).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public class item {
        public String itemName;
        public double itemPrice;
        public int maxQuantity;

        public item() {

        }

        public item(String itemName, double itemPrice, int maxQuantity) {
            this.itemName = itemName;
            this.itemPrice = itemPrice;
            this.maxQuantity = maxQuantity;

        }
    }
}

