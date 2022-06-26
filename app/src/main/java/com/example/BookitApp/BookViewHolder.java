package com.example.BookitApp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

class BookViewHolder extends RecyclerView.ViewHolder {
    public TextView tvPrice, tvTitle, tvSubTitle;
    public ImageView ivProduct;
    public Activity mActivity;
    public Button btn_summary, btn_buy, btn_borrow;

    public Dialog d;
    public EditText card;
    public Button btnDialog;

    public String coverImgId;


    public BookViewHolder(View itemView, Activity activity) {
        super(itemView);
        mActivity = activity;
        tvPrice = itemView.findViewById(R.id.tvPrice);
        tvTitle = itemView.findViewById(R.id.tvTitle);
        tvSubTitle = itemView.findViewById(R.id.tvSubTitle);
        ivProduct = itemView.findViewById(R.id.ivProduct);

        btn_borrow=itemView.findViewById(R.id.btn_borrow);
        btn_buy= itemView.findViewById(R.id.btn_buy);
        btn_summary= itemView.findViewById(R.id.btn_readMore);

        btn_summary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog builder = new Dialog(mActivity);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable( new ColorDrawable(android.graphics.Color.TRANSPARENT));
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        dialogInterface.dismiss();
                    }
                });

                byte[] decodedString = Base64.decode(coverImgId, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                ImageView imageView = new ImageView(mActivity);
                imageView.setImageBitmap(decodedByte);

                builder.addContentView(imageView, new RelativeLayout.LayoutParams(1000, ViewGroup.LayoutParams.WRAP_CONTENT));
                builder.show();
            }
        });

        btn_borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                String bookName = tvTitle.getText().toString();

                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String date = i2 + "/" + i1 + "/" + i;

                        reference.child("Reg").child(currentUser.getUid()).child(bookName).setValue(date);
                        Toast.makeText(activity, date, Toast.LENGTH_SHORT).show();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONDAY), calendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.show();
            }
        });

        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BuyDialog buyDialog = new BuyDialog(mActivity, tvTitle.getText().toString());
                buyDialog.show();
            }
        });
    }
}