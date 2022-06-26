package com.example.BookitApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class UserInfoActivity extends AppCompatActivity {

    private LinearLayout borrowedBooksLayout, boughtBooksLayout;
    private ImageButton btnBack, btnImg;
    private TextView txtUsername, txtEmail;

    private FrameLayout frameLayout;
    private FrameLayout.LayoutParams params;
    private FragmentManager ft;

    private DatabaseReference reference;
    private FirebaseUser currentUser;

    private static int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        reference = FirebaseDatabase.getInstance("https://bookit-new-default-rtdb.firebaseio.com/").getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        boughtBooksLayout = findViewById(R.id.bought_books_layout);
        borrowedBooksLayout = findViewById(R.id.borrowed_books_layout);

        btnBack = findViewById(R.id.btn_back);
        btnImg = findViewById(R.id.btn_img);

        txtUsername = findViewById(R.id.txt_username);
        txtEmail = findViewById(R.id.txt_email);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserInfoActivity.this, HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(UserInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        reference.child("Users").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                txtUsername.setText(user.getUsername());
                txtEmail.setText(user.getEmail());

                String imgId = user.getImgId();
                if(!imgId.equals("")) {
                    byte[] decodedString = Base64.decode(imgId, Base64.DEFAULT);
                    Bitmap imageBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    btnImg.setBackground(null);
                    btnImg.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 500, 500, false));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("Buy").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()) {
                    String name = snapshot1.getKey();

                    reference.child("books").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot1: snapshot.getChildren()) {
                                for(DataSnapshot snapshot2: snapshot1.getChildren()) {
                                    Book b = snapshot2.getValue(Book.class);
                                    if(b.getTitle().equals(name)) {
                                        generateFragment(b.getImgId(), "", boughtBooksLayout);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("Reg").child(currentUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1: snapshot.getChildren()) {
                    String name = snapshot1.getKey();
                    String date = snapshot1.getValue(String.class);

                    reference.child("books").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot1: snapshot.getChildren()) {
                                for(DataSnapshot snapshot2: snapshot1.getChildren()) {
                                    Book b = snapshot2.getValue(Book.class);
                                    if(b.getTitle().equals(name)) {
                                        generateFragment(b.getImgId(), date, borrowedBooksLayout);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    public void generateFragment(String imgId, String date, LinearLayout linearLayout) {
        frameLayout = new FrameLayout(UserInfoActivity.this);
        frameLayout.setId(View.generateViewId());

        params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        frameLayout.setLayoutParams(params);

        linearLayout.addView(frameLayout, params);

        ft = getSupportFragmentManager();

        if(!ft.isDestroyed()) {
            ft.beginTransaction().add(frameLayout.getId(), ImageFragment.newInstance(imgId, date)).commit();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            btnImg.setBackground(null);
            btnImg.setImageBitmap(Bitmap.createScaledBitmap(imageBitmap, 500, 500, false));

            reference.child("Users").child(currentUser.getUid()).child("imgId").setValue(BitMapToString(imageBitmap));
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
}