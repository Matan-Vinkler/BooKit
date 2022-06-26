package com.example.BookitApp;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.QuickContactBadge;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BuyDialog extends Dialog {
    private Activity activity;

    private EditText edtCreditNum, edtIdNum, edtCCV;
    private Button btnConfirm;

    private String bookName;

    public BuyDialog(Activity activity, String bookName) {
        super(activity);
        this.activity = activity;
        this.bookName = bookName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.buy_dialog);

        edtCreditNum = findViewById(R.id.edt_cardNum);
        edtIdNum = findViewById(R.id.edt_idNum);
        edtCCV = findViewById(R.id.edt_ccvNum);
        btnConfirm = findViewById(R.id.btn_confirm);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user1 = snapshot.getValue(User.class);

                edtCreditNum.setText(user1.getCreditCardId());
                edtIdNum.setText(user1.getId());
                edtCCV.setText(user1.getCcv());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    int id = Integer.parseInt(edtIdNum.getText().toString().trim());
                    long credit = Long.parseLong(edtCreditNum.getText().toString().trim());
                    int ccv = Integer.parseInt(edtCCV.getText().toString().trim());

                    if(id < 100000000 || credit < 100000000000L || ccv < 100) {
                        throw new Exception("Invalid Data");
                    }

                    reference.child("Users").child(user.getUid()).child("creditCardId").setValue(String.valueOf(credit));
                    reference.child("Users").child(user.getUid()).child("id").setValue(String.valueOf(id));
                    reference.child("Users").child(user.getUid()).child("ccv").setValue(String.valueOf(ccv));

                    reference.child("Buy").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot snapshot1: snapshot.getChildren()) {
                                if(snapshot1.getKey().equals(bookName)) {
                                    Toast.makeText(activity, "You already own this book.", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                    return;
                                }
                            }

                            reference.child("Buy").child(user.getUid()).child(bookName).setValue(true);
                            dismiss();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                catch (Exception e) {
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
