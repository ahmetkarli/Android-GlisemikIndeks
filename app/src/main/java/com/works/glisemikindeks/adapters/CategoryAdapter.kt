package com.works.glisemikindeks.adapters

import android.text.TextUtils
import com.works.glisemikindeks.databinding.ActivityCategoryRowBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.works.glisemikindeks.activities.DB
import com.works.glisemikindeks.R
import com.works.glisemikindeks.databinding.CategoryUpdateBinding
import com.works.glisemikindeks.models.CategoryDbModel
import com.works.glisemikindeks.models.FoodsDbModel


class CategoryAdapter(var arrCategory : ArrayList<CategoryDbModel>, var arrFoods : ArrayList<FoodsDbModel>) : RecyclerView.Adapter<CategoryAdapter.ModelViewHolder>(){
    lateinit var db : DB

    class ModelViewHolder(val bind: ActivityCategoryRowBinding): RecyclerView.ViewHolder(bind.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val bind = ActivityCategoryRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ModelViewHolder(bind)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {

        holder.bind.txtCategoryName.text = arrCategory.get(position).categoryName.toString()

        holder.bind.btnEditCategory.setOnClickListener {

            db = DB(holder.bind.root.context)

            val builder = AlertDialog.Builder(holder.bind.root.context,R.style.AlertDialogTheme)
            val li = LayoutInflater.from(holder.bind.root.context)
            val dialogNameBinding = CategoryUpdateBinding.inflate(li)
            builder.setView(dialogNameBinding.root)

            dialogNameBinding.txtUpdateCategoryName.setText(arrCategory.get(position).categoryName.toString())

            builder.setPositiveButton("Düzenle"){
                    dialogInterface, i ->
                val categoryName = dialogNameBinding.txtUpdateCategoryName.text.toString().trim()

                if(!TextUtils.isEmpty(categoryName)){
                    db.updateCategory(arrCategory.get(position).categoryID!!,categoryName)
                    arrFoods=db.getAllFoods()
                    arrCategory= db.getAllCategories()
                    notifyDataSetChanged()

                    val snack = Snackbar.make(it,"Kategori düzenleme başarılı !", Snackbar.LENGTH_SHORT)
                    snack.show()

                } else{
                    val snack = Snackbar.make(it,"Kategori adı boş olamaz !", Snackbar.LENGTH_SHORT)
                    snack.show()
                }


                notifyDataSetChanged()
            }.setNegativeButton("İptal"){
                    dialogInterface, i ->

            }.show()


            notifyDataSetChanged()
        }

        holder.bind.btnDeleteCategory.setOnClickListener {

            val builder = AlertDialog.Builder(holder.bind.root.context, R.style.AlertDialogTheme)

            builder.setIcon(R.drawable.garbage).setTitle("Kategori Silme İşlemi").setMessage("Bu kategoriye ait besin/besinler ve kategori silinecektir! Emin misiniz?").setPositiveButton("Sil"){
                    dialogInterface, i ->

                db = DB(holder.bind.root.context)
                db.deleteFoodsbyCategoryName(arrCategory.get(position).categoryID!!.toInt())
                db.deleteCategory(arrCategory.get(position).categoryID!!.toInt())
                arrFoods = db.getAllFoods()
                arrCategory=db.getAllCategories()
                notifyDataSetChanged()


            }.setNegativeButton("İptal"){
                    dialogInterface, i ->

            }.show()




        }


    }

    override fun getItemCount(): Int {
        return arrCategory.size
    }

}