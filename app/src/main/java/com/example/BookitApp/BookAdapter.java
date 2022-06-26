package com.example.BookitApp;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookViewHolder> {

    //we are storing all the products in a list
    private List<Book> productList;
    private Activity mActivity;

    //getting the context and product list with constructor
    public BookAdapter(List<Book> productList, FragmentActivity activity)
    {
        this.productList = productList;
        mActivity = activity;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new BookViewHolder(view, mActivity);
    }


    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        //getting the product of the specified position
        Book product = productList.get(position);
        //binding the data with the viewholder views
        holder.tvTitle.setText(product.getTitle());
        holder.tvSubTitle.setText(product.getSubTitle());
        holder.tvPrice.setText(String.valueOf(product.getPrice()));
        holder.coverImgId = product.getImgId2();

        byte[] decodedString = Base64.decode(product.getImgId(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        holder.ivProduct.setImageBitmap(decodedByte);
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.custom_layout;
    }

    public int getItemCount() {
        return productList.size();
    }
}
