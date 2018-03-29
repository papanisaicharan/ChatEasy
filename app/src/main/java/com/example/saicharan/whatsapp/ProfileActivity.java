package com.example.saicharan.whatsapp;

import android.app.ProgressDialog;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.sql.Date;

public class ProfileActivity extends AppCompatActivity {


    private ImageView imageView;
    private TextView displayname,status,friends;
    private Button sendrequest;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private String mCurrentState;
    private DatabaseReference mFriendRequest,mFriendsList;
    private FirebaseUser mfirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final String key = getIntent().getStringExtra("user_id");
        //obtained the reference of friend
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(key);
        //obtained the reference of friendRequest
        mFriendRequest = FirebaseDatabase.getInstance().getReference().child("FriendRequest");
        //got the current user
        mfirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mFriendsList = FirebaseDatabase.getInstance().getReference().child("Friends");

        imageView = (ImageView) findViewById(R.id.imageViewpro1);
        displayname = (TextView) findViewById(R.id.Displaynamepro1);
        status = (TextView) findViewById(R.id.Statuspro1);
        friends = (TextView) findViewById(R.id.friendspro1);
        sendrequest = (Button) findViewById(R.id.requestButtonpro1);

       // mFriendRequest.child(mfirebaseUser.getUid()).child("mCurrentState");
        mCurrentState = "not_friends";

        progressDialog= new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Uploading...");
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("image").getValue().toString();
                String name = dataSnapshot.child("name").getValue().toString();
                String Status = dataSnapshot.child("status").getValue().toString();
                String thumbs_image = dataSnapshot.child("thumb_image").getValue().toString();

                displayname.setText(name);
                status.setText(Status);
                Picasso.with(ProfileActivity.this).load(image).placeholder(R.drawable.pro2).into(imageView);

                mFriendRequest.child(mfirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild(key)) {
                            String request_type = dataSnapshot.child(key).child("request_type").getValue().toString();

                            if (request_type.equals("recieved")) {
                                mCurrentState = "req_recieved";
                                sendrequest.setText("Accept Friend Request");
                            } else if (request_type.equals("sent")) {
                                mCurrentState = "request_sent";
                                sendrequest.setText("Cancel Friend Request");
                            }
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        sendrequest.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                //preventing user for multiple clicking ,processing
                sendrequest.setEnabled(false);
                if(mCurrentState.equals("not_friends")){
//setting the currentstate of
                    mFriendRequest.child(mfirebaseUser.getUid()).child(key)
                            .child("request_type").setValue("sent")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){

                                mFriendRequest.child(key).child(mfirebaseUser.getUid())
                                        .child("request_type").setValue("recieved").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
//setting the state as request sent and text as cancel request

                                        mCurrentState = "request_sent";
                                        sendrequest.setText("Cancel Friend Request");

                                        Toast.makeText(ProfileActivity.this,"request sent succesfully",Toast.LENGTH_LONG).show();
                                    }
                                });

                            }else {
                                Toast.makeText(ProfileActivity.this,"failed to sent request",Toast.LENGTH_LONG).show();

                            }
                            sendrequest.setEnabled(true);
                        }
                    });

                }

                if(mCurrentState.equals("request_sent")){
                    mFriendRequest.child(mfirebaseUser.getUid()).child(key)
                            .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mFriendRequest.child(key).child(mfirebaseUser.getUid()).
                                    removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    sendrequest.setEnabled(true);
                                    mCurrentState = "not_friends";
                                    sendrequest.setText("Send Friend Request");
                                    Toast.makeText(ProfileActivity.this,"request Cancelled  succesfully",Toast.LENGTH_LONG).show();

                                }
                            });
                        }
                    });
                }

                if(mCurrentState.equals("req_recieved")){
                    String date = null;
                    Calendar c = Calendar.getInstance();
                   // System.out.println("Current time => " + c.getTime());

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c.getTime());
                    date = DateFormat.getDateTimeInstance().format(formattedDate);

                    final String finalDate = date;
                    mFriendsList.child(mfirebaseUser.getUid()).child(key).setValue(date)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mFriendsList.child(key).child(mfirebaseUser.getUid()).setValue(finalDate)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mFriendRequest.child(mfirebaseUser.getUid()).child(key)
                                            .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mFriendRequest.child(key).child(mfirebaseUser.getUid()).
                                                    removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    sendrequest.setEnabled(true);
                                                    mCurrentState = "friends";
                                                    sendrequest.setText("Unfriend");
                                                   // Toast.makeText(ProfileActivity.this,"request Cancelled  succesfully",Toast.LENGTH_LONG).show();

                                                }
                                            });
                                        }
                                    });
                                }
                            });

                        }
                    });
                }

            }
        });
    }
}
