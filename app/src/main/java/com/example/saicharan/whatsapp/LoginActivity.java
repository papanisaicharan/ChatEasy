package com.example.saicharan.whatsapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout mEmai1,mPassword1;
    private Button mLogin;
    private FirebaseAuth mAuth;
    private Toolbar mtoolbar1;
    private ProgressDialog mprogress1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmai1 = (TextInputLayout) findViewById(R.id.login_email);
        mPassword1 = (TextInputLayout) findViewById(R.id.login_password);
        mLogin = (Button) findViewById(R.id.login_btn_1);
        mAuth = FirebaseAuth.getInstance();

        mtoolbar1 = (Toolbar) findViewById(R.id.login_app_bar);
        setSupportActionBar(mtoolbar1);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mprogress1 = new ProgressDialog(this);


        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emaillogin = mEmai1.getEditText().getText().toString();
                String passwordlogin = mPassword1.getEditText().getText().toString();
                if(emaillogin.isEmpty() || passwordlogin.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Fields are empty",Toast.LENGTH_LONG).show();
                }else {
                    mprogress1.setMessage("LoggingIn.....");
                    mprogress1.setTitle("Login");
                    mprogress1.setCanceledOnTouchOutside(false);
                    mprogress1.setCancelable(false);
                    mprogress1.show();
                    loginUser(emaillogin, passwordlogin);
                }
            }
        });

    }

    private void loginUser(String emaillogin, String passwordlogin) {
        mAuth.signInWithEmailAndPassword(emaillogin,passwordlogin).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mprogress1.dismiss();
                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();

                }else{
                    mprogress1.hide();
                    Toast.makeText(LoginActivity.this,"Error in Login",Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}
