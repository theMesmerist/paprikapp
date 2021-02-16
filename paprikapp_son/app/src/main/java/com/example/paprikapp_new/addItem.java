package com.example.paprikapp_new;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addItem extends AppCompatActivity {

    private FirebaseDatabase myDB;
    Spinner dropdown;
    Button add ;
    EditText name, price, maxQ;
    String itemName, itemType;
    double itemPrice;
    int maxQuantity;
    int j;
    ImageButton main10;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_add_item);
        myDB = FirebaseDatabase.getInstance();
        add = findViewById(R.id.add);

        name = findViewById(R.id.name);
        price = findViewById(R.id.price);
        maxQ = findViewById(R.id.maxq);


        main10 = (ImageButton) findViewById(R.id.main10);

        main10.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        dropdown = findViewById(R.id.itemType);
        String[] items = new String[]{"appetizer", "mainCourse", "dessert", "salad", "drinks", "alcohol"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



        if (name.getText().toString().isEmpty() || price.getText().length()<0 || maxQ.getText().length()<0 ){
            Toast toast = Toast.makeText(addItem.this, "Please fill all the fields", Toast.LENGTH_LONG);
            toast.show();

        }
              else  if (name.getText()!=null && price.getText()!=null && maxQ.getText()!=null) {
            itemName = name.getText().toString();
            itemPrice = Double.valueOf(price.getText().toString());
            maxQuantity = Integer.valueOf(maxQ.getText().toString());
            itemType = dropdown.getSelectedItem().toString();
            createItem(itemName, itemPrice, maxQuantity, itemType);

                }
                name.setText(null);
                price.setText(null);
                maxQ.setText(null);
        }
        });

        }


    private void createItem(String itemName, double itemPrice, int maxQuantity, String itemType) {

        j = (int) (Math.random() * 10000);
        DatabaseReference dbRef = myDB.getReference("item").child(itemType);

            item Item = new item(itemName, itemPrice, maxQuantity);
            dbRef.child(String.valueOf(j)).setValue(Item);
            Toast toast = Toast.makeText(addItem.this, "Item succesfully added to '" + itemType + "' menu", Toast.LENGTH_LONG);
            toast.show();

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