package com.works.glisemikindeks.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.works.glisemikindeks.databinding.CategoryRowBinding.inflate
import com.works.glisemikindeks.models.CategoryDbModel


class CategorySpinnerAdapter (val context: Context, val arr:ArrayList<CategoryDbModel>,): BaseAdapter()  {
    override fun getCount(): Int {
        return arr.size
    }

    override fun getItem(p0: Int): Any {
        return p0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val bind = inflate(LayoutInflater.from(context),p2,false)
        bind.textView.text = arr.get(p0).categoryName
        return bind.root
    }
}