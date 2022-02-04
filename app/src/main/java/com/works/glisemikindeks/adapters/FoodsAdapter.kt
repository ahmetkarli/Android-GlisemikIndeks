package com.works.glisemikindeks.adapters

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.works.glisemikindeks.activities.DB
import com.works.glisemikindeks.R
import com.works.glisemikindeks.databinding.ActivityUpdateBinding
import com.works.glisemikindeks.databinding.ItemFoodsRowBinding
import com.works.glisemikindeks.models.CategoryDbModel
import com.works.glisemikindeks.models.FoodsDbModel

class FoodsAdapter(val context : Context,var arrFoods : ArrayList<FoodsDbModel>,var arrCategories : ArrayList<CategoryDbModel>) : RecyclerView.Adapter<FoodsAdapter.ModelViewHolder>(){
    lateinit var db : DB
    class ModelViewHolder(val bind: ItemFoodsRowBinding): RecyclerView.ViewHolder(bind.root){}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val bind = ItemFoodsRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ModelViewHolder(bind)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {

        var gli = arrFoods.get(position).gliIndeks!!.toInt()

       when(gli){
           in 0..55 ->{
               holder.bind.view.setBackgroundColor(Color.GREEN)
           }
           in 56..69->{
               holder.bind.view.setBackgroundColor(Color.parseColor("#FFA500"))
           }

          in 70..999999999999999->{
              holder.bind.view.setBackgroundColor(Color.RED)
           }

           else->{
           holder.bind.view.setBackgroundColor(Color.WHITE)

        }

       }

        holder.bind.txtName.text = arrFoods.get(position).foodName.toString()
        holder.bind.txtGli.text ="Glisemik İndeksi : "+ arrFoods.get(position).gliIndeks.toString()
        holder.bind.txtCarbonAmount.text ="Karbonhidrat miktarı(100 gr'daki) : "+ arrFoods.get(position).carbonAmount.toString()
        holder.bind.txtCalAmount.text ="Kalori(100gr'daki) : "+ arrFoods.get(position).calAmount.toString()

        db = DB(context)
        val categoryName = db.getCategoryName(arrFoods.get(position).categoryID!!)

        holder.bind.txtCategory.text="Kategori : "+categoryName

        holder.bind.btnDelete.setOnClickListener {

            val builder = AlertDialog.Builder(holder.bind.root.context,R.style.AlertDialogTheme)

            builder.setIcon(R.drawable.garbage).setTitle("Besin Silme İşlemi").setMessage("Bu besin silinecektir! Emin misiniz?").setPositiveButton("Sil"){
                    dialogInterface, i ->
                db = DB(context)
                db.deleteItem(arrFoods.get(position).foodID!!.toInt())
                arrFoods=db.getAllFoods()
                arrCategories = db.getAllCategories()
                notifyDataSetChanged()

            }.setNegativeButton("İptal"){
                    dialogInterface, i ->

            }.show()



        }

        holder.bind.btnEdit.setOnClickListener {
            db = DB(context)

            val builder = AlertDialog.Builder(context,R.style.AlertDialogTheme)
            val li = LayoutInflater.from(context)
            val dialogNameBinding = ActivityUpdateBinding.inflate(li)

            db = DB(context)
            arrCategories = db.getAllCategories()

            dialogNameBinding.spinner.adapter = CategorySpinnerAdapter(context,arrCategories)

            for((index,item) in arrCategories.withIndex()){
                if(item.categoryID == arrFoods.get(position).categoryID){
                    dialogNameBinding.spinner.setSelection(index)
                }

            }


            builder.setView(dialogNameBinding.root)
            dialogNameBinding.txtName.setText(arrFoods.get(position).foodName.toString())
            dialogNameBinding.txtGliIndeks.setText(arrFoods.get(position).gliIndeks.toString())
            dialogNameBinding.txtCarbonAmount.setText(arrFoods.get(position).carbonAmount.toString())
            dialogNameBinding.txtCalAmount.setText(arrFoods.get(position).calAmount.toString())

            builder.setPositiveButton("Güncelle"){
                    dialogInterface, i ->
                arrCategories = db.getAllCategories()


                val categoryID = arrCategories.get(dialogNameBinding.spinner.selectedItemId.toInt()).categoryID
                val foodName = dialogNameBinding.txtName.text.toString().trim()
                val gliIndeks= dialogNameBinding.txtGliIndeks.text.toString().trim()
                val carbonAmount= dialogNameBinding.txtCarbonAmount.text.toString().trim()
                val calAmount= dialogNameBinding.txtCalAmount.text.toString().trim()


                if(!TextUtils.isEmpty(foodName) && !TextUtils.isEmpty(gliIndeks) &&
                    !TextUtils.isEmpty(carbonAmount) && !TextUtils.isEmpty(calAmount)){

                        val f = FoodsDbModel(null,categoryID,foodName, gliIndeks, carbonAmount, calAmount)
                    db.updateItem(f,arrFoods.get(position).foodID)

                    arrFoods=db.getAllFoods()
                    arrCategories = db.getAllCategories()

                    gli = arrFoods.get(position).gliIndeks!!.toInt()

                    when(gli){
                        in 0..55 ->{
                            holder.bind.view.setBackgroundColor(Color.GREEN)

                        }
                        in 56..69->{
                            holder.bind.view.setBackgroundColor(Color.parseColor("#FFA500"))

                        }

                        in 70..9999999999999->{
                            holder.bind.view.setBackgroundColor(Color.RED)

                        }

                        else->{
                            holder.bind.view.setBackgroundColor(Color.WHITE)

                        }

                    }

                    notifyDataSetChanged()


                    val snack = Snackbar.make(it,"Besin düzenleme başarılı !", Snackbar.LENGTH_SHORT)
                    snack.show()

                }else{
                    val snack = Snackbar.make(it,"Lütfen boş alan bırakmayınız !", Snackbar.LENGTH_SHORT)
                    snack.show()
                }

                notifyDataSetChanged()

            }.setNegativeButton("İptal"){
                    dialogInterface, i ->

            }.show()

            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return arrFoods.size
    }

}