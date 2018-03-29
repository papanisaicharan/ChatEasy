package com.example.saicharan.whatsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseReference mdatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        toolbar = (Toolbar) findViewById(R.id.user_tool);
        toolbar.setTitleTextColor(Color.argb(255,255,255,255));
        toolbar.setBackgroundColor(Color.rgb(103,59,183));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.users_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mdatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Users , UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UsersViewHolder>
                (Users.class,R.layout.users_list_layout,UsersViewHolder.class,
                        mdatabaseReference) {
            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, Users model, int position) {

                viewHolder.setDisplayName(model.getName());
                viewHolder.setStatus(model.getStatus());
                viewHolder.setUsersImage(model.getImage(),getApplicationContext());
                final String key = getRef(position).getKey();
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i =  new Intent(UsersActivity.this,ProfileActivity.class);
                        i.putExtra("user_id",key);
                        startActivity(i);

                    }
                });


            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);

    }

    public  static class UsersViewHolder extends RecyclerView.ViewHolder{

       View mView;
//        private StorageReference mStorageRef;


        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

        }


        public  void setDisplayName(String name) {

            TextView view = (TextView) mView.findViewById(R.id.Display_nameuser);
            view.setText(name);
        }

        public void setStatus(String status) {
            TextView view = (TextView) mView.findViewById(R.id.Status_name1user);
            view.setText(status);
        }

        public void setUsersImage(String usersImage, Context context) {
            CircleImageView view = (CircleImageView) mView.findViewById(R.id.circleImageView);
//            mStorageRef = FirebaseStorage.getInstance().getReference();
//            final StorageReference thumb_filepath = mStorageRef.child("profileimages").child("thumbs");
            Picasso.with(context).load(usersImage).placeholder(R.drawable.pro2).into(view);

        }


    }


}
