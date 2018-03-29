package com.example.saicharan.whatsapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.SearchEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class SettingsActivity extends AppCompatActivity {

    private static final int GALLERY = 1;
    private Toolbar toolbar;
    private ListView listView;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private CircleImageView mcircleImageView;
    private TextView mstatus,mname;
    private ProgressDialog progressDialog,progressDialog1;
    private ArrayList<String> headings;

    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mcircleImageView = (CircleImageView) findViewById(R.id.profile_image);
        mstatus = (TextView) findViewById(R.id.status_set);
        mname = (TextView) findViewById(R.id.Display_nameset);
        listView = (ListView) findViewById(R.id.setting_list);
        mStorageRef = FirebaseStorage.getInstance().getReference();


        toolbar = (Toolbar) findViewById(R.id.Setting_tool_bar1);
        toolbar.setTitleTextColor(Color.argb(255,255,255,255));
        toolbar.setBackgroundColor(Color.rgb(103,59,183));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        headings = new ArrayList<>();
        headings.add("Account");
        headings.add("status and DisplayName");
        headings.add("Privacy");
        headings.add("Notifications");
        headings.add("Help");

        SettingsAdapter settingsAdapter = new SettingsAdapter(SettingsActivity.this,headings);
        listView.setAdapter(settingsAdapter);





        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                switch((String)parent.getItemAtPosition(position)) {
                    case "Account" :
                        Intent i = new Intent(SettingsActivity.this,AccountActivity.class);
                        startActivity(i);
                        break;
                    case "status and DisplayName":

                        AlertDialog.Builder mBuilder = new  AlertDialog.Builder(SettingsActivity.this);
                        View mView = getLayoutInflater().inflate(R.layout.statusdialog, null);
                        final EditText Status1 = (EditText) mView.findViewById(R.id.Status_dialog);
                        final EditText Display1 = (EditText) mView.findViewById(R.id.name_dialog);

                        Status1.setText("");
                        Display1.setText("");
                        Status1.append(mstatus.getText().toString());
                        Display1.append(mname.getText().toString());
                        mBuilder.setPositiveButton("submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String s = Status1.getText().toString();
                                String s1 = Display1.getText().toString();
                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                String uid = firebaseUser.getUid();

                                databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                                databaseReference.child("name").setValue(s1);
                                databaseReference.child("status").setValue(s);
                                dialog.dismiss();


                            }
                        });
                        mBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });


                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.show();

                        break;
                    case "Privacy":
                        Toast.makeText(SettingsActivity.this,(String)parent.getItemAtPosition(position),Toast.LENGTH_LONG).show();
                        break;
                    case "Notifications":
                        Toast.makeText(SettingsActivity.this,(String)parent.getItemAtPosition(position),Toast.LENGTH_LONG).show();
                        break;
                    case "Help":
                        Toast.makeText(SettingsActivity.this,(String)parent.getItemAtPosition(position),Toast.LENGTH_LONG).show();
                        break;

                    default:

                }

            }
        });


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = firebaseUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {



                String name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

//                progressDialog= new ProgressDialog(SettingsActivity.this);
//                progressDialog.setMessage("Loading...");
//                progressDialog.setTitle("Recieving");
//                progressDialog.setCanceledOnTouchOutside(true);
//                progressDialog.setCancelable(false);
//                progressDialog.show();

                mname.setText(name);
                mstatus.setText(status);
                //Picasso.with(SettingsActivity.this).load(image).into(mcircleImageView);


                    Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.pro2).into(mcircleImageView);
                    //progressDialog.dismiss();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void propic(View view) {

        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(gallery,"Select image"),GALLERY);

//        // start picker to get image for cropping and then use the image in cropping activity
//        CropImage.activity()
//                .setGuidelines(CropImageView.Guidelines.ON)
//                .start(SettingsActivity.this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY && resultCode == RESULT_OK){
            Uri imageUri = data.getData();

            // start cropping activity for pre-acquired image saved on the device
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                progressDialog= new ProgressDialog(SettingsActivity.this);
                progressDialog.setMessage("Uploading...");
                progressDialog.setTitle("Please wait");
                progressDialog.setCanceledOnTouchOutside(true);
                progressDialog.show();

                Uri resultUri = result.getUri();

                File thumb = new File(resultUri.getPath());

                String uid = firebaseUser.getUid();


                Bitmap thumb_nail = null;
                try {
                    thumb_nail = new Compressor(this).setQuality(70).setMaxWidth(250).setMaxHeight(250).compressToBitmap(thumb);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream boos = new ByteArrayOutputStream();
                    boolean compress = thumb_nail.compress(Bitmap.CompressFormat.JPEG, 100, boos);
                    final byte[] bytes = boos.toByteArray();





                final StorageReference profileimages = mStorageRef.child("profileimages").child(uid+".jpg");
                final StorageReference thumb_filepath = mStorageRef.child("profileimages").child("thumbs").child(uid+".jpg");




                profileimages.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()){

                            final String s = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = thumb_filepath.putBytes(bytes);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull final Task<UploadTask.TaskSnapshot> Thumb_task) {

                                    String thumb_path = Thumb_task.getResult().getDownloadUrl().getPath();

                                    if(Thumb_task.isSuccessful()){

                                        Map hashmap = new HashMap();
                                        hashmap.put("image",s);
                                        hashmap.put("thumb_image",thumb_path);


                                        databaseReference.updateChildren(hashmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                if (task.isSuccessful()){
                                                    progressDialog.dismiss();
                                                    Toast.makeText(SettingsActivity.this,"uploaded",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                    }else{
                                        Toast.makeText(SettingsActivity.this,"Error in Uploading",Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }

                                }
                            });

                       }else{
                            progressDialog.dismiss();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();
            }
        }
    }
}
