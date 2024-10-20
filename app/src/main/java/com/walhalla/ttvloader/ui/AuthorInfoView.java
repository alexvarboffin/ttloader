package com.walhalla.ttvloader.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;
import com.walhalla.ttvloader.R;
import com.walhalla.ttvloader.TTResponse;
import com.walhalla.ttvloader.databinding.CustomViewAuthorInfoBinding;


public class AuthorInfoView extends CardView {

    private CustomViewAuthorInfoBinding binding;

    public AuthorInfoView(Context context) {
        super(context);
        init(context);
    }

    public AuthorInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AuthorInfoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        binding = CustomViewAuthorInfoBinding.inflate(LayoutInflater.from(context), this, true);
    }

    public void setAuthor(String author) {
        binding.author.setText(author);
    }

    public void setFileTitle(String fileTitle) {
        binding.fileTitle.setText(fileTitle);
    }

//    public void setThumbImage(int resId) {
//        binding.thumb.setImageResource(resId);
//    }
    public void setThumbImage(TTResponse result) {
        if (!TextUtils.isEmpty(result.thumb)) {
            Picasso.get().load(result.thumb)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.ic_main_logo)
                    .into(binding.thumb);
        }
    }

    public void setDescription(String resId) {
        binding.tvDesc.setText(resId);
    }

    public void setCloseAction(OnClickListener listener) {
        binding.actionClose.setOnClickListener(listener);
    }

    public void bind(TTResponse result) {
        setAuthor(result.username);
        setFileTitle(result.title);
        if (!TextUtils.isEmpty(result.description)) {
            setDescription(result.description);
        } else {
            binding.tvDesc.setVisibility(GONE);
        }
        setVisibility(View.VISIBLE);
        setThumbImage(result);
    }
}

