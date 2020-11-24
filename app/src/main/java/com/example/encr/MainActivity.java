package com.example.encr;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;



public class MainActivity extends AppCompatActivity {


    EditText name,room_id,room_key;
    String name1,room_id1,room_key1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name=(EditText)findViewById(R.id.name);
        room_id=(EditText)findViewById(R.id.room_id);
        room_key=(EditText)findViewById(R.id.room_key);


    }
    public void join(View view)
    {
        Intent intent=new Intent(MainActivity.this,Messages.class);
        name1=name.getText().toString();
        room_id1=room_id.getText().toString();
        room_key1=room_key.getText().toString();
        intent.putExtra("name",name1);
        intent.putExtra("room_id",room_id1);
        intent.putExtra("room_key",room_key1);
        startActivity(intent);
    }
}
