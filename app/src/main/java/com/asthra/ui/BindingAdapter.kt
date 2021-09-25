package com.asthra.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.asthra.ui.dialog.model.DialogModel
import com.bumptech.glide.Glide

class BindingAdapter {

    @BindingAdapter("loadImage")
    fun setLoadImage(imageView: ImageView, imgUrl: String) {
        if (imgUrl != null) {
            Glide.with(imageView.context).load(imgUrl)
                .into(imageView)
        }
    }

    @BindingAdapter("loadImageEmp")
    fun setLoadImageEmployee(imageView: ImageView, imgUrl: String) {
        if (imgUrl != null) {
//            Glide.with(imageView.context).load(imgUrl).placeholder(R.drawable.ic_person_circle)
//                .into(imageView)
        }
    }

  /*  @BindingAdapter("app:contactlist")
    fun setItems(listView: RecyclerView, items: List<DialogModel>?) {
        items?.let {
            (listView.adapter as ContactListAdapter).submitList(items)
        }
    }
*/
}