package com.example.saicharan.whatsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import static com.example.saicharan.whatsapp.R.id.login_email;
import static com.example.saicharan.whatsapp.R.id.login_password;
import static com.example.saicharan.whatsapp.R.id.reg_Display_name;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout mDisplayname,mEmail,mPassword;
    private Button mCreatebtn;
    private FirebaseAuth mAuth;
    private Toolbar mtoolbar;
    private ProgressDialog mprogress;
    private  DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDisplayname = (TextInputLayout) findViewById(reg_Display_name);
        mEmail = (TextInputLayout) findViewById(login_email);
        mPassword = (TextInputLayout) findViewById(login_password);
        mCreatebtn = (Button) findViewById(R.id.reg_btn);
        mtoolbar = (Toolbar) findViewById(R.id.reg_app_bar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mprogress = new ProgressDialog(this);



        mAuth = FirebaseAuth.getInstance();

        mCreatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Displayname= mDisplayname.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                if(Displayname.isEmpty() || email.isEmpty() || password.isEmpty()){
                    Toast.makeText(RegisterActivity.this,"Registration failed",Toast.LENGTH_LONG).show();
                }else {
                    mprogress.setMessage("Registering.....");
                    mprogress.setTitle("Register");
                    mprogress.setCanceledOnTouchOutside(false);
                    mprogress.show();
                    register_user(Displayname, email, password);
                }
            }
        });
    }

    private void register_user(final String displayname, final String email, String password) {

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    // Write a message to the database
//                    FirebaseDatabase database = FirebaseDatabase.getInstance();
//                    DatabaseReference myRef = database.getReference();
//                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//                    String uid = currentUser.getUid();
//                    User user = new User(displayname,email);
//                    myRef.child("users").child(uid).child("name").setValue(displayname);
//                    myRef.child("usera").child(uid).child("email").setValue(email);
//                    myRef.child("users").child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//
//                            if(task.isSuccessful()) {
//                                mprogress.dismiss();
//                                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
//                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(i);
//                                finish();
//                            }
//                        }
//                    });


                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = currentUser.getUid();

                   users = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                    HashMap<String , String> hashMap = new HashMap<>();
                    hashMap.put("name",displayname);
                    hashMap.put("status","hello i am there");
                    hashMap.put("image","default");
                    hashMap.put("thumb_image","default");
                    users.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            mprogress.dismiss();
                            Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        }
                    });


                }else{
                    mprogress.hide();
                    Toast.makeText(RegisterActivity.this,"sorryregistering failed",Toast.LENGTH_LONG).show();
                }


            }
        });
    }
}


//class User {
//
//public String username;
//public String email;
//
//public User() {
////ault constructor required for calls to DataSnapshot.getValue(User.class)
//}
//
//public User(String username, String email) {
//this.username = username;
//this.email = email;
//}
//
//}