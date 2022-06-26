package com.example.BookitApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    TextView linkTextView2;
    EditText etEmail2;
    EditText etPassword2;

    FirebaseAuth mAuth;
    FirebaseDatabase mFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth= FirebaseAuth.getInstance();
        mFirebase = FirebaseDatabase.getInstance();


        btn_login= (Button) findViewById(R.id.btn_login);

        etEmail2= (EditText) findViewById(R.id.etEmail2);
        etPassword2= (EditText) findViewById(R.id.etPassword2);

        linkTextView2=(TextView) findViewById(R.id.linktextview2);
        linkTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void loginUser() {
        String Email2= etEmail2.getText().toString().trim();
        String Password2= etPassword2.getText().toString().trim();

        if (TextUtils.isEmpty(Email2)) {
            etEmail2.setError("Email cannot be empty");
            etEmail2.requestFocus();
            shake();
            return;

        }
        else if (TextUtils.isEmpty(Password2)) {
            etPassword2.setError("Password cannot be empty");
            etPassword2.requestFocus();
            shake();
            return;
        }
        else {
            mAuth.signInWithEmailAndPassword(Email2, Password2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "User has logged in successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this,HomePageActivity.class));
                        finish();
                    }else {
                        Toast.makeText(LoginActivity.this, "Login Error:"+task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                        etEmail2.setText("");
                        etPassword2.setText("");
                        shake();
                    }
                }
            });
        }
    }

    private void shake() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }

}