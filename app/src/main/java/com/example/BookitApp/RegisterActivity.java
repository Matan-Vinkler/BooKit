package com.example.BookitApp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.TextUtils;
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

public class RegisterActivity extends AppCompatActivity {

    TextView linkTextView;
    private Button btn_register;
    EditText etUserName;
    EditText etEmail;
    EditText etPassword;
    EditText etConfirmPassword;
    FirebaseAuth mAuth;
    DatabaseReference reference;
    AlertDialog Dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        etUserName=(EditText) findViewById(R.id.etUserName);
        etEmail=(EditText) findViewById(R.id.etEmail);
        etPassword=(EditText) findViewById(R.id.etPassword);
        etConfirmPassword=(EditText) findViewById(R.id.etConfirmPassword);

        linkTextView= (TextView) findViewById(R.id.linktextview);
        linkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        btn_register=(Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createuser();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    private void createuser() {
        String UserName= etUserName.getText().toString().trim();
        String Email= etEmail.getText().toString().trim();
        String Password= etPassword.getText().toString().trim();
        String ConfirmPassword= etConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(Email)) {
            etEmail.setError("Email cannot be empty");
            etEmail.requestFocus();
            shake();
            return;
        }
        else if (TextUtils.isEmpty(Password)) {
            etPassword.setError("Password cannot be empty");
            etPassword.requestFocus();
            shake();
            return;
        }
        else if (TextUtils.isEmpty(UserName)) {
            etUserName.setError("User name cannot be empty");
            etUserName.requestFocus();
            shake();
            return;
        }
        else if (TextUtils.isEmpty(ConfirmPassword)) {
            etConfirmPassword.setError("Confirm password name cannot be empty");
            etConfirmPassword.requestFocus();
            shake();
            return;
        }

        if(!TextUtils.equals(Password, ConfirmPassword)) {
            etPassword.setError("Password does not match");
            etPassword.requestFocus();
            shake();
            return;
        }

        else {
            mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                        FirebaseUser user = mAuth.getCurrentUser();

                        User newUser = new User();
                        newUser.setUsername(UserName);
                        newUser.setEmail(user.getEmail());

                        reference.child("Users").child(user.getUid()).setValue(newUser);

                        startActivity(new Intent(RegisterActivity.this, HomePageActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Registration error:"+task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                        etUserName.setText("");
                        etEmail.setText("");
                        etPassword.setText("");
                        etConfirmPassword.setText("");
                        shake();
                    }
                }
            });
        }
    }

    public void shake() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            //deprecated in API 26
            v.vibrate(500);
        }
    }
}