package com.works.glisemikindeks.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.AdapterView
import android.widget.FrameLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import android.widget.Toast.makeText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.works.glisemikindeks.R
import com.works.glisemikindeks.adapters.CategorySpinnerAdapter
import com.works.glisemikindeks.adapters.FoodsAdapter
import com.works.glisemikindeks.databinding.ActivityAddBinding
import com.works.glisemikindeks.databinding.ActivityMainBinding
import com.works.glisemikindeks.models.CategoryDbModel
import com.works.glisemikindeks.models.CategoryModel
import com.works.glisemikindeks.models.FoodsDbModel
import com.works.glisemikindeks.models.FoodsModel
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var bind :ActivityMainBinding
    var db = DB(this)
    lateinit var sha : SharedPreferences
    lateinit var edit : SharedPreferences.Editor
    private lateinit var adapter:FoodsAdapter
    private var currentToast: Toast? = null


    var arrCategory = ArrayList<CategoryModel>()
    var arrFoods0 = ArrayList<FoodsModel>()
    var arrFoods1 = ArrayList<FoodsModel>()
    var arrFoods2 = ArrayList<FoodsModel>()
    var arrFoods3 = ArrayList<FoodsModel>()
    var arrFoods4 = ArrayList<FoodsModel>()
    var arrFoods5 = ArrayList<FoodsModel>()

    var arrFoods = ArrayList<FoodsDbModel>()
    var arrCategories = ArrayList<CategoryDbModel>()
    lateinit var mSnackbar:Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        sha = getSharedPreferences("user", MODE_PRIVATE)
        edit = sha.edit()

        val status = sha.getString("status","")
        if(status == ""){


        val i = Intent(this, Intro::class.java)
            startActivity(i)
        }

        mSnackbar = Snackbar.make(bind.root,"Uygulamadan çıkmak için 2 kez geri butonuna basın !", Snackbar.LENGTH_SHORT)

        if (Build.VERSION.SDK_INT > 9) {
            val policy = ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        val opened = sha.getString("key","")
        if(opened == "value"){

        }else {

            arrCategory = getCategories()
            arrFoods0 = getFoods0()
            arrFoods1 = getFoods1()
            arrFoods2 = getFoods2()
            arrFoods3 = getFoods3()
            arrFoods4 = getFoods4()
            arrFoods5 = getFoods5()

            db = DB(this)
            db.addCategories(arrCategory)
            db.addFoods(arrFoods0,1)
            db.addFoods(arrFoods1,2)
            db.addFoods(arrFoods2,3)
            db.addFoods(arrFoods3,4)
            db.addFoods(arrFoods4,5)
            db.addFoods(arrFoods5,6)

            edit.putString("key","value")
            edit.commit()

        }

            db = DB(this)
            arrCategories = db.getAllCategories()

            bind.spinnerCategory.adapter = CategorySpinnerAdapter(this,arrCategories)

            bind.spinnerCategory.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    arrCategories = db.getAllCategories()
                    val categoryID =  arrCategories.get(p2).categoryID
                    getAllFoodsbyCategory(categoryID!!)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }


            }

            bind.rV.setHasFixedSize(true)
            bind.rV.layoutManager = LinearLayoutManager(this)
            getAll()

            bind.floatingActionButton2.setOnClickListener {

                val builder = AlertDialog.Builder(this, R.style.AlertDialogTheme)
                val li = LayoutInflater.from(this)
                val dialogNameBinding = ActivityAddBinding.inflate(li)

                dialogNameBinding.spinner.adapter = CategorySpinnerAdapter(this,arrCategories)

                builder.setView(dialogNameBinding.root)

                builder.setPositiveButton("Ekle"){
                        dialogInterface, i ->


                    db = DB(this)
                    arrFoods = db.getAllFoods()
                    var categoryID = 0

                    arrCategories = db.getAllCategories()

                    if(arrCategories.size > 0){
                         categoryID = arrCategories.get(dialogNameBinding.spinner.selectedItemId.toInt()).categoryID!!

                    }


                    val foodName = dialogNameBinding.txtName.text.toString().trim()
                    val gliIndeks= dialogNameBinding.txtGliIndeks.text.toString().trim()
                    val carbonAmount= dialogNameBinding.txtCarbonAmount.text.toString().trim()
                    val calAmount= dialogNameBinding.txtCalAmount.text.toString().trim()

                    if( categoryID != 0 && !TextUtils.isEmpty(foodName) && !TextUtils.isEmpty(gliIndeks) &&
                        !TextUtils.isEmpty(carbonAmount) && !TextUtils.isEmpty(calAmount)){

                        db = DB(this)

                        val f = FoodsDbModel(null,categoryID,foodName, gliIndeks, carbonAmount, calAmount)
                        db.addItem(f)
                        getAll()

                        val snack = Snackbar.make(it,"Besin ekleme başarılı !",Snackbar.LENGTH_SHORT)
                        snack.show()

                    }else{
                        val snack = Snackbar.make(it,"Lütfen boş alan bırakmayınız !",Snackbar.LENGTH_SHORT)
                        snack.show()
                    }


                }.setNegativeButton("İptal"){
                        dialogInterface, i ->

                }.show()
            }
        }

    override fun onStart() {

        db = DB(this)
        arrFoods = db.getAllFoods()
        arrCategories = db.getAllCategories()

        bind.spinnerCategory.adapter = CategorySpinnerAdapter(this,arrCategories)
        bind.rV.adapter  = FoodsAdapter(this,arrFoods,arrCategories)
        super.onStart()
    }

    override fun onBackPressed() {
        if (mSnackbar.isShown()) {

            super.onBackPressed()
        } else {
            mSnackbar.show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)

        val item = menu?.findItem(R.id.action_search)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_categoryEdit ->{
                val intent = Intent(this, Category::class.java)
                startActivity(intent)
            }
            R.id.action_sort->{

                db = DB(this)

                arrCategories = db.getAllCategories()
                val categoryID = arrCategories.get(bind.spinnerCategory.selectedItemId.toInt()).categoryID!!
                arrFoods = db.getSortedFoods(categoryID)


                bind.rV.adapter  = FoodsAdapter(this,arrFoods,arrCategories)

            }

        }

        return super.onOptionsItemSelected(item)
    }
    override fun onQueryTextSubmit(query: String?): Boolean {
        search(query!!)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        search(newText!!)
        return true
    }

  fun getAllFoodsbyCategory(categoryID:Int){
      db = DB(this)
      arrFoods = db.foodsByCategory(categoryID)
      arrCategories = db.getAllCategories()
      bind.rV.adapter  = FoodsAdapter(this,arrFoods,arrCategories)
  }

  fun getAll(){
        db = DB(this)
        arrFoods = db.getAllFoods()
        arrCategories = db.getAllCategories()
        bind.rV.adapter  = FoodsAdapter(this,arrFoods,arrCategories)

  }

  fun search(text:String){

      db = DB(this)
      arrFoods = db.searchFood(text)
      bind.rV.adapter  = FoodsAdapter(this,arrFoods,arrCategories)

    }


  fun getCategories():ArrayList<CategoryModel>{
      var arrC = ArrayList<CategoryModel>()
        try {

            val doc: Document = Jsoup.connect("http://kolaydoktor.com/saglik-icin-yasam/diyet-ve-beslenme/besinlerin-glisemik-indeks-tablosu/0503/1").get()
            val elements: Elements = doc.select("#ctl00_icerik_UpdatePanel1 > table > tbody >tr > td >p>font>font >b ")
            for ((index,item) in elements.withIndex()) {

                val st = item.toString()
                var stN = st.substring(3,st.length-4 )
                if(index == 0 || index == 193 || index == 240  || index == 345 || index == 398 || index == 419 ){
                    if(index == 240){
                        stN = stN.substring(20,stN.length-7)
                        stN = "N"+ stN
                    }
                    stN.trim()
                    val c = CategoryModel(stN)
                    arrC.add(c)

                }
            }

        }catch (ex:Exception){
            Log.e("TAG", "onCreate error: $ex")
        }

        return arrC
    }

  fun getFoods0():ArrayList<FoodsModel>{
        var arrF = ArrayList<FoodsModel>()
        try {

            val doc: Document = Jsoup.connect("http://kolaydoktor.com/saglik-icin-yasam/diyet-ve-beslenme/besinlerin-glisemik-indeks-tablosu/0503/1").get()
            val elements: Elements = doc.select("div#ctl00_icerik_UpdatePanel1 > table > tbody >tr >td>p>font>font>b ")

            val arrName: MutableList<String> = ArrayList()
            val arrGli: MutableList<String> = ArrayList()
            val arrCarbon: MutableList<String> = ArrayList()
            val arrCal: MutableList<String> = ArrayList()

            for ((index,item) in elements.withIndex()){
                val i = item.text()
                Log.d("TAG", "getFoods0: $index - $i ")

            }

            for ((index,item) in elements.withIndex()) {

                val i = item.text()
                if(index>4 && index<193){

                    if(index%4 ==1){
                        arrName.add(i)
                    }
                    if(index%4 ==2){
                        arrGli.add(i)
                    }
                    if(index%4 ==3){
                        arrCarbon.add(i)
                    }
                    if(index%4 ==0){
                        arrCal.add(i)

                    }


                }

            }

           for (i in 0..arrName.size){
               val f = FoodsModel(arrName.get(i),arrGli.get(i),arrCarbon.get(i),arrCal.get(i))
               arrF.add(f)
           }

        }catch (ex:Exception){
            Log.e("TAG", "onCreate error: $ex")
        }

        return arrF
    }

  fun getFoods1() :ArrayList<FoodsModel>{
      var arrF = ArrayList<FoodsModel>()
      try {

          val doc: Document = Jsoup.connect("http://kolaydoktor.com/saglik-icin-yasam/diyet-ve-beslenme/besinlerin-glisemik-indeks-tablosu/0503/1").get()
          val elements: Elements = doc.select("div#ctl00_icerik_UpdatePanel1 > table > tbody >tr >td>p>font>font>b ")

          val arrName: MutableList<String> = ArrayList()
          val arrGli: MutableList<String> = ArrayList()
          val arrCarbon: MutableList<String> = ArrayList()
          val arrCal: MutableList<String> = ArrayList()


          for ((index,item) in elements.withIndex()) {


              val i = item.text()
              if(index>197 && index<230) {
                  if (index % 4 == 2) {
                      arrName.add(i)
                  }
                  if (index % 4 == 3) {
                      arrGli.add(i)
                  }
                  if (index % 4 == 0) {
                      arrCarbon.add(i)

                  }
                  if (index % 4 == 1) {
                      arrCal.add(i)

                  }

              }

              if(index == 230){
                  arrName.add(i)

              }
              if(index == 231){
                  arrGli.add(i)
                  arrCal.add("Empty")
                  arrCarbon.add("Empty")


              }

              if(index > 231 && index < 240){
                  if (index % 4 == 0) {
                      arrName.add(i)
                  }
                  if (index % 4 == 1) {
                      arrGli.add(i)
                  }
                  if (index % 4 == 2) {
                      arrCarbon.add(i)

                  }
                  if (index % 4 == 3) {
                      arrCal.add(i)

                  }


              }




          }

          for (i in 0..arrName.size){
              val f = FoodsModel(arrName.get(i),arrGli.get(i),arrCarbon.get(i),arrCal.get(i))
              arrF.add(f)
          }

      }catch (ex:Exception){
          Log.e("TAG", "onCreate error: $ex")
      }

      return arrF


  }

  fun getFoods2() :ArrayList<FoodsModel>{
        var arrF = ArrayList<FoodsModel>()
        try {

            val doc: Document = Jsoup.connect("http://kolaydoktor.com/saglik-icin-yasam/diyet-ve-beslenme/besinlerin-glisemik-indeks-tablosu/0503/1").get()
            val elements: Elements = doc.select("div#ctl00_icerik_UpdatePanel1 > table > tbody >tr >td>p>font>font>b ")

            val arrName: MutableList<String> = ArrayList()
            val arrGli: MutableList<String> = ArrayList()
            val arrCarbon: MutableList<String> = ArrayList()
            val arrCal: MutableList<String> = ArrayList()


            for ((index,item) in elements.withIndex()) {

                val i = item.text()
                if(index>244 && index<345){

                    if(index%4 ==1){
                        arrName.add(i)
                    }
                    if(index%4 ==2){
                        arrGli.add(i)
                    }
                    if(index%4 ==3){
                        arrCarbon.add(i)
                    }
                    if(index%4 ==0){
                        arrCal.add(i)

                    }


                }

            }

            for (i in 0..arrName.size){
                val f = FoodsModel(arrName.get(i),arrGli.get(i),arrCarbon.get(i),arrCal.get(i))
                arrF.add(f)
            }

        }catch (ex:Exception){
            Log.e("TAG", "onCreate error: $ex")
        }

        return arrF


    }

  fun getFoods3() :ArrayList<FoodsModel>{
        var arrF = ArrayList<FoodsModel>()
        try {

            val doc: Document = Jsoup.connect("http://kolaydoktor.com/saglik-icin-yasam/diyet-ve-beslenme/besinlerin-glisemik-indeks-tablosu/0503/1").get()
            val elements: Elements = doc.select("div#ctl00_icerik_UpdatePanel1 > table > tbody >tr >td>p>font>font>b ")

            val arrName: MutableList<String> = ArrayList()
            val arrGli: MutableList<String> = ArrayList()
            val arrCarbon: MutableList<String> = ArrayList()
            val arrCal: MutableList<String> = ArrayList()


            for ((index,item) in elements.withIndex()) {

                val i = item.text()
                if(index>349 && index<398){

                    if(index%4 ==2){
                        arrName.add(i)
                    }
                    if(index%4 ==3){
                        arrGli.add(i)
                    }
                    if(index%4 ==0){
                        arrCarbon.add(i)
                    }
                    if(index%4 ==1){
                        arrCal.add(i)

                    }


                }

            }

            for (i in 0..arrName.size){
                val f = FoodsModel(arrName.get(i),arrGli.get(i),arrCarbon.get(i),arrCal.get(i))
                arrF.add(f)
            }

        }catch (ex:Exception){
            Log.e("TAG", "onCreate error: $ex")
        }

        return arrF


    }

  fun getFoods4() :ArrayList<FoodsModel>{
        var arrF = ArrayList<FoodsModel>()
        try {

            val doc: Document = Jsoup.connect("http://kolaydoktor.com/saglik-icin-yasam/diyet-ve-beslenme/besinlerin-glisemik-indeks-tablosu/0503/1").get()
            val elements: Elements = doc.select("div#ctl00_icerik_UpdatePanel1 > table > tbody >tr >td>p>font>font>b ")

            val arrName: MutableList<String> = ArrayList()
            val arrGli: MutableList<String> = ArrayList()
            val arrCarbon: MutableList<String> = ArrayList()
            val arrCal: MutableList<String> = ArrayList()


            for ((index,item) in elements.withIndex()) {

                val i = item.text()
                if(index>402 && index<419){

                    if(index%4 ==3){
                        arrName.add(i)
                    }
                    if(index%4 ==0){
                        arrGli.add(i)
                    }
                    if(index%4 ==1){
                        arrCarbon.add(i)
                    }
                    if(index%4 ==2){
                        arrCal.add(i)

                    }


                }

            }

            for (i in 0..arrName.size){
                val f = FoodsModel(arrName.get(i),arrGli.get(i),arrCarbon.get(i),arrCal.get(i))
                arrF.add(f)
            }

        }catch (ex:Exception){
            Log.e("TAG", "onCreate error: $ex")
        }

        return arrF


    }

  fun getFoods5() :ArrayList<FoodsModel>{
        var arrF = ArrayList<FoodsModel>()
        try {

            val doc: Document = Jsoup.connect("http://kolaydoktor.com/saglik-icin-yasam/diyet-ve-beslenme/besinlerin-glisemik-indeks-tablosu/0503/1").get()
            val elements: Elements = doc.select("div#ctl00_icerik_UpdatePanel1 > table > tbody >tr >td>p>font>font>b ")

            val arrName: MutableList<String> = ArrayList()
            val arrGli: MutableList<String> = ArrayList()
            val arrCarbon: MutableList<String> = ArrayList()
            val arrCal: MutableList<String> = ArrayList()


            for ((index,item) in elements.withIndex()) {

                val i = item.text()
                if(index>423 && index<452){

                    if(index%4 ==0){
                        arrName.add(i)
                    }
                    if(index%4 ==1){
                        arrGli.add(i)
                    }
                    if(index%4 ==2){
                        arrCarbon.add(i)
                    }
                    if(index%4 ==3){
                        arrCal.add(i)

                    }


                }

            }

            for (i in 0..arrName.size){
                val f = FoodsModel(arrName.get(i),arrGli.get(i),arrCarbon.get(i),arrCal.get(i))
                arrF.add(f)
            }

        }catch (ex:Exception){
            Log.e("TAG", "onCreate error: $ex")
        }

        return arrF
    }

}