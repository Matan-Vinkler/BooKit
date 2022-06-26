package com.example.BookitApp;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class HomePageActivity extends AppCompatActivity {

    boolean isPlay;
    Switch btnPlay;
    String strlink;
    Intent serviceIntent;
    Button btn_LogOut;
    FirebaseAuth mAuth;
    SeekBar seekBar;

    BroadcastBattery battery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        FirebaseApp.initializeApp(this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance());
        isPlay=false;
        serviceIntent= new Intent(this, PlayService.class);
        btnPlay = findViewById(R.id.btn_switch);

        battery = new BroadcastBattery();
        registerReceiver(battery, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        seekBar = findViewById(R.id.seek_bar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        btnPlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    playAudio();
                }
                else {
                    stopAudio();
                }
            }
        });

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("books");


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Fantasy"));
        tabLayout.addTab(tabLayout.newTab().setText("Action"));
        tabLayout.addTab(tabLayout.newTab().setText("Cooking & baking"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager =(ViewPager)findViewById(R.id.view_pager);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Book> actionList = new ArrayList<>();
                List<Book> cookList = new ArrayList<>();
                List<Book> fantasyList = new ArrayList<>();

                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()) {
                        Book b = dataSnapshot1.getValue(Book.class);

                        switch (dataSnapshot.getKey()) {
                            case "action":
                                actionList.add(b);
                                break;
                            case "cooking and baking":
                                cookList.add(b);
                                break;
                            case "fantasy":
                                fantasyList.add(b);
                                break;
                            default:
                                break;
                        }
                    }
                }

                TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), actionList, cookList, fantasyList);
                viewPager.setAdapter(tabsAdapter);
                viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_LogOut= findViewById(R.id.btn_LogOut);
        mAuth= FirebaseAuth.getInstance();

        btn_LogOut.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(HomePageActivity.this,RegisterActivity.class));
        });
    }

    private void playAudio() {
        try
        {
            startService(serviceIntent);
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    private void stopAudio() {
        try {
            stopService(serviceIntent);
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if (id==R.id.logOut) {
            FirebaseAuth.getInstance().signOut();
            Intent i=new Intent(HomePageActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }
        if (id==R.id.userInfo) {
            Intent i=new Intent(HomePageActivity.this, UserInfoActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
 class TabsAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;
    private List<Book> actionList, cookList, fantasyList;

    public TabsAdapter(FragmentManager fm, int NoofTabs, List<Book> list1, List<Book> list2, List<Book> list3){
        super(fm);
        this.mNumOfTabs = NoofTabs;
        this.actionList = list1;
        this.cookList = list2;
        this.fantasyList = list3;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                FantasyGenreFragment fantasy = new FantasyGenreFragment(fantasyList);
                return fantasy;
            case 1:
                ActionGenreFragment action = new ActionGenreFragment(actionList);
                return action;
            case 2:
                DetectiveGenreFragment detective = new DetectiveGenreFragment(cookList);
                return detective;
            default:
                return null;
        }
    }
}