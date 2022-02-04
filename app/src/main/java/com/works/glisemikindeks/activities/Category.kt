package com.works.glisemikindeks.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.works.glisemikindeks.R
import com.works.glisemikindeks.adapters.CategoryAdapter
import com.works.glisemikindeks.databinding.ActivityCategoryBinding
import com.works.glisemikindeks.databinding.CategoryAddBinding
import com.works.glisemikindeks.models.CategoryDbModel
import com.works.glisemikindeks.models.FoodsDbModel

class Category : AppCompatActivity() {
    private lateinit var bind : ActivityCategoryBinding

    var db = DB(this)
    private lateinit var adapter:CategoryAdapter
    var arrFoods = ArrayList<FoodsDbModel>()
    var arrCategories = ArrayList<CategoryDbModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(bind.root)


        val actionbar = supportActionBar
        actionbar!!.setDisplayHomeAsUpEnabled(true)

        bind.rV.setHasFixedSize(true)
        bind.rV.layoutManager = LinearLayoutManager(this)

        getAllCategory()

        bind.fab.setOnClickListener {

            val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
            val li = LayoutInflater.from(this)
            val dialogNameBinding = CategoryAddBinding.inflate(li)


            builder.setView(dialogNameBinding.root)

            builder.setPositiveButton("Ekle"){
                    dialogInterface, i ->

                val categoryName = dialogNameBinding.txtAddCategoryName.text.toString().trim()

                if(!TextUtils.isEmpty(categoryName)){
                    db = DB(this)
                    db.addCategory(categoryName)
                    getAllCategory()
                    val snack = Snackbar.make(it,"Kategori ekleme başarılı!", Snackbar.LENGTH_SHORT)
                    snack.show()

                }
                else{
                    val snack = Snackbar.make(it,"Kategori adı boş olamaz !", Snackbar.LENGTH_SHORT)
                    snack.show()
                }


            }.setNegativeButton("İptal"){
                    dialogInterface, i ->

            }.show()

        }
    }

    fun getAllCategory(){

        db = DB(this)
        arrFoods = db.getAllFoods()
        arrCategories = db.getAllCategories()
        bind.rV.adapter  = CategoryAdapter(arrCategories,arrFoods)
    }

    override fun onStart() {

        db = DB(this)
        var arrCategories = db.getAllCategories()
        var arrFoods = db.getAllFoods()

        bind.rV.layoutManager = LinearLayoutManager(this@Category, LinearLayoutManager.VERTICAL,false)
        bind.rV.adapter  = CategoryAdapter(arrCategories,arrFoods)
        super.onStart()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}