package com.example.saicharan.whatsapp;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private ViewPager mViewpager;
    private  SectionPagerAdapter mSectionpagerAdapter;
    private TabLayout mtablayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        toolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        toolbar.setTitleTextColor(Color.argb(255,255,255,255));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Whatsapp");


        mViewpager = (ViewPager) findViewById(R.id.main_tabPager);
        mSectionpagerAdapter = new SectionPagerAdapter(getSupportFragmentManager());

        mViewpager.setAdapter(mSectionpagerAdapter);

        mtablayout = (TabLayout) findViewById(R.id.main_tabs);
        mtablayout.setupWithViewPager(mViewpager);
//        mtablayout.setTabTextColors(ColorStateList.valueOf(Color.argb(225,225,225,225)));
//        mtablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

    //checks wheether the user is logged in or not
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentuser = mAuth.getCurrentUser();

        if(currentuser == null){
          sendtostart();
        }

    }

    //used for log out purpose
    private void sendtostart() {
        Intent startIntent = new Intent(MainActivity.this,StartActivity.class);
        //startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(startIntent);
        finish();
    }

    //for creating setting option on toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    //after selecting
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.main_logout){
            mAuth.signOut();
            sendtostart();
        }else if(item.getItemId() == R.id.main_settings){
            Intent i = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(i);
        }else if(item.getItemId() == R.id.main_allusers){
            Intent i = new Intent(MainActivity.this,UsersActivity.class);
            startActivity(i);

        }

        return true;
    }
}
