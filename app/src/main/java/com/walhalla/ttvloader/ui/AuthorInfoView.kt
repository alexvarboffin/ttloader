package com.walhalla.ttvloader.ui

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.Nullable
import androidx.cardview.widget.CardView
import com.squareup.picasso.Picasso
import com.walhalla.ttvloader.R
import com.walhalla.ttvloader.TTResponse
import com.walhalla.ttvloader.databinding.CustomViewAuthorInfoBinding

class AuthorInfoView : CardView {
    private var binding: CustomViewAuthorInfoBinding? = null
    constructor(context: Context) : super(context) {
        `init`(context)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        `init`(context)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        `init`(context)
    }
    private fun `init`(context: Context) {
        binding = CustomViewAuthorInfoBinding.inflate(LayoutInflater.from(context), this, true)
    }
    fun setAuthor(author: String?) {
        binding!!.author.setText(author)
    }
    fun setFileTitle(fileTitle: String?) {
        binding!!.fileTitle.setText(fileTitle)
    }
    //    public void setThumbImage(int resId) {
    //        binding!!.thumb.setImageResource(resId);
    //    }
    fun setThumbImage(result: TTResponse) {
        if (TextUtils.isEmpty(result.thumb)) {
            Picasso.`get`().load(result.thumb).placeholder(R.drawable.placeholder).error(R.drawable.ic_main_logo).into(binding!!.thumb)
        }
    }
    fun setDescription(resId: String?) {
        binding!!.tvDesc.setText(resId)
    }
    fun setCloseAction(listener: OnClickListener) {
        binding!!.actionClose.setOnClickListener(listener)
    }
    fun bind(result: TTResponse) {
        setAuthor(result.username)
        setFileTitle(result.title)
        if (TextUtils.isEmpty(result.description)) {
            setDescription(result.description)
        } else {
            binding!!.tvDesc.setVisibility(GONE)
        }
        setVisibility(View.VISIBLE)
        setThumbImage(result)
    }
}
