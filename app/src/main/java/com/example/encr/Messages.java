package com.example.encr;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import androidx.appcompat.app.AppCompatActivity;
import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class Messages extends MainActivity {


    public static SecretKeySpec secretKey;
    public static byte[] key;

    DatabaseReference ref;
    private FirebaseRecyclerOptions<List_Data> options;
    private FirebaseRecyclerAdapter<List_Data, MyViewHolder>adapter;
    RecyclerView recyclerView;

    EditText msg;
    String mname,mroom_id,mroom_key;
    Button send;

    public static void setKey(String myKey)
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String strToEncrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
            }
        }
        catch (Exception e)
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
    public static String decrypt(String strToDecrypt, String secret)
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
            }
        }
        catch (Exception e)
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }




   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        recyclerView=findViewById(R.id.myRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        Intent intent = getIntent();

        mname = intent.getStringExtra("name");
        mroom_id = intent.getStringExtra("room_id");
        mroom_key = intent.getStringExtra("room_key");

        final String secretKey = mroom_key+mroom_id;


      /*  recyclerView=(RecyclerView)findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<model>();*/

       ref=FirebaseDatabase.getInstance().getReference("SecuredChatDemo").child(mroom_id);/*
        nm.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()){
                        List_Data l=npsnapshot.getValue(List_Data.class);
                        listData.add(l);
                    }
                    adapter=new MyAdapter(listData);
                    rv.setAdapter(adapter);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
       /* reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    model p=dataSnapshot1.getValue(model.class);
                    list.add(p);
                }
                adapter=new MyAdapter(Messages.this,list);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(Messages.this,"Oops Somrthing went wrong",Toast.LENGTH_LONG);
            }
        });*/

        msg=(EditText)findViewById(R.id.msg);
        send=(Button)findViewById(R.id.btnSend);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String key=ref.push().getKey();
               /* String encname=encrypt(mname.toString(),secretKey);
                String name=encname;*/
               String name=mname.toString();
                String encmsg=encrypt(msg.getText().toString(),secretKey);
                String message=encmsg;

                ref.child(key).child("Name").setValue(name);
                ref.child(key).child("Message").setValue(message);
                msg.setText("");
            }
        });

        options=new FirebaseRecyclerOptions.Builder<List_Data>().setQuery(ref, List_Data.class).build();
        adapter=new FirebaseRecyclerAdapter<List_Data, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int i, @NonNull List_Data list_data) {
                String decmsg=decrypt(list_data.getMessage(),secretKey);
                holder.textName.setText(""+list_data.getName());
                holder.textMsg.setText(decmsg);

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view_layout,parent,false);
                return new MyViewHolder(v);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
}
