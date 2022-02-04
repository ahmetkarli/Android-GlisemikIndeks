package com.works.glisemikindeks.activities

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.works.glisemikindeks.models.CategoryDbModel
import com.works.glisemikindeks.models.CategoryModel
import com.works.glisemikindeks.models.FoodsDbModel
import com.works.glisemikindeks.models.FoodsModel

class DB(context: Context?, name: String? = "gli.db", factory: SQLiteDatabase.CursorFactory? = null, version: Int = 1) :
    SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(p0: SQLiteDatabase?) {
        p0!!.execSQL("CREATE TABLE \"category\" (\n" +
                "\t\"categoryID\"\tINTEGER,\n" +
                "\t\"categoryName\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"categoryID\" AUTOINCREMENT )\n" +
                ")\n")

        p0!!.execSQL("CREATE TABLE \"foods\" (\n" +
                "\t\"foodID\"\tINTEGER,\n" +
                "\t\"categoryID\"\tINTEGER,\n" +
                "\t\"foodName\"\tTEXT,\n" +
                "\t\"gliIndex\"\tTEXT,\n" +
                "\t\"carbonAmount\"\tTEXT,\n" +
                "\t\"calAmount\"\tTEXT,\n" +
                "\tPRIMARY KEY(\"foodID\" AUTOINCREMENT),\n" +
                "\tFOREIGN KEY(\"categoryID\") REFERENCES \"category\"(\"categoryID\")\n" +
                ");")

    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0!!.execSQL("DROP TABLE IF EXISTS category")
        onCreate(p0)
    }

    fun getCategoryName(categoryID: Int):String{

        val read = this.readableDatabase
        var name = ""

        val n = read.rawQuery("select * from category where categoryID = "+categoryID,null)

        while(n.moveToNext()){
            name = n.getString(1)

        }
            return name
    }



    fun deleteFoodsbyCategoryName(categoryID : Int){

        val write = this.writableDatabase
        write.delete("foods","categoryID="+categoryID,null)

    }

    fun updateCategory(categoryID:Int,categoryName:String){
        val write = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("categoryName", categoryName)
        write.update("category", contentValues, "categoryID = ?", arrayOf(categoryID.toString()))
    }

    fun searchFood(text:String):ArrayList<FoodsDbModel>{

        val write = this.writableDatabase
        val foodArr = ArrayList<FoodsDbModel>()

        val f = write.rawQuery("select * from foods where foodName like '%$text%' or gliIndex like '%$text%' or carbonAmount like '%$text%' or calAmount like '%$text%' ",null)

        while(f.moveToNext()){
            val foodID = f.getInt(0)
            val categoryID = f.getInt(1)
            val foodName = f.getString(2)
            val gliIndeks = f.getString(3)
            val carbonAmount = f.getString(4)
            val calAmount = f.getString(5)

            val f = FoodsDbModel(foodID, categoryID, foodName, gliIndeks, carbonAmount, calAmount)
            foodArr.add(f)
        }


        return foodArr
    }


    fun deleteItem(foodID:Int){

        val write = this.writableDatabase
        write.delete("foods","foodID="+foodID,null)

    }

    fun deleteCategory(categoryID:Int){
        val write = this.writableDatabase
        write.delete("category","categoryID="+categoryID,null)
    }

    fun addCategory(categoryName : String){

        val write = this.writableDatabase
        val values = ContentValues()

        values.put("categoryName",categoryName)
        write.insert("category",null,values)
    }

    fun addItem(food : FoodsDbModel){

        val write = this.writableDatabase
        val values = ContentValues()

        values.put("categoryID",food.categoryID)
        values.put("foodName",food.foodName)
        values.put("gliIndex",food.gliIndeks)
        values.put("carbonAmount",food.carbonAmount)
        values.put("calAmount",food.calAmount)

        write.insert("foods",null,values)

    }

    fun updateItem(food : FoodsDbModel, foodID : Int?){

        val write = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("categoryID", food.categoryID)
        contentValues.put("foodName", food.foodName)
        contentValues.put("gliIndex", food.gliIndeks)
        contentValues.put("carbonAmount", food.carbonAmount)
        contentValues.put("calAmount", food.calAmount)

        write.update("foods", contentValues, "foodID = ?", arrayOf(foodID.toString()))
    }

    fun addCategories(arr:ArrayList<CategoryModel>){

        val write = this.writableDatabase
        val values = ContentValues()

        arr.forEach {
            values.put("categoryName",it.categoryName)
           write.insert("category",null,values)

        }

    }

    fun addFoods(arr:ArrayList<FoodsModel>, categoryID:Int){

        val write = this.writableDatabase
        val values = ContentValues()

        arr.forEach {
            values.put("categoryID",categoryID)
            values.put("foodName",it.foodName)
            values.put("gliIndex",it.gliInd)
            values.put("carbonAmount",it.carbonAmount)
            values.put("calAmount",it.calAmount)


            write.insert("foods",null,values)

        }
    }

    fun getAllFoods():ArrayList<FoodsDbModel>{

        val arr = ArrayList<FoodsDbModel>()

        val read = this.readableDatabase
        val query = "select * from foods"
        val cursor = read.rawQuery(query,null)


        while(cursor.moveToNext()){

            val foodID = cursor.getInt(0)
            val categoryID = cursor.getInt(1)
            val foodName = cursor.getString(2)
            val gliIndeks = cursor.getString(3)
            val carbonAmount = cursor.getString(4)
            val calAmount = cursor.getString(5)

            val f = FoodsDbModel(foodID, categoryID, foodName, gliIndeks, carbonAmount, calAmount)
            arr.add(f)

        }

        return arr


    }

    fun getAllCategories():ArrayList<CategoryDbModel>{

        val arr = ArrayList<CategoryDbModel>()

        val read = this.readableDatabase
        val query = "select * from category"
        val cursor = read.rawQuery(query,null)


        while(cursor.moveToNext()){

            val categoryID = cursor.getInt(0)
            val categoryName = cursor.getString(1)
            val c = CategoryDbModel(categoryID, categoryName)
            arr.add(c)

        }

        return arr


    }
}