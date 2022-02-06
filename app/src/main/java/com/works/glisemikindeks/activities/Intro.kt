package com.works.glisemikindeks.activities

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.github.appintro.AppIntro
import com.github.appintro.AppIntroFragment
import com.github.appintro.AppIntroPageTransformerType
import com.works.glisemikindeks.R

class Intro : AppIntro() {

    lateinit var sha : SharedPreferences
    lateinit var edit : SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        addSlide(AppIntroFragment.newInstance(
            title = "Glisemik İndeks'e Hoşgeldiniz ! ",
            description = "Uygulama ana sayfamızda besinleri görebilirsiniz. Ayrıca kategorilere basarak ta o kateogoriye ait besinleri görüntüleyebilirsiniz. Kartların başında bulunan renkler " +
                    "Glisemik İndeks değerleri ile ilgildir. Yeşil renk düşük,turuncu renk normal, kırmızı renk ise yüksek olarak eşleştirilmiştir.",
            imageDrawable = R.drawable.slide1,
            backgroundDrawable = R.color.orange,
            titleColor = Color.BLACK,
            descriptionColor = Color.BLACK,
            backgroundColor = Color.BLUE,
        ))

        addSlide(AppIntroFragment.newInstance(
            title = "Besin Güncelleme",
            description = "Kartların üzerindeki kalem resmi bulunan butona basarak açılan bu pencere ile besinin bilgilerini değiştirebilirsiniz.",
            imageDrawable = R.drawable.slideedit,
            backgroundDrawable = R.drawable.bg,
            titleColor = Color.BLACK,
            descriptionColor = Color.BLACK,
            backgroundColor = Color.RED,

        ))
        addSlide(AppIntroFragment.newInstance(
            title = "Besin Ekleme",
            description = "Ana sayfada bulunan + butonu ile açılan bu ekran sayesinde gerekli bilgileri doldurarak yeni besin ekleyebilirsiniz.",
            imageDrawable = R.drawable.slide2,
            backgroundDrawable = R.drawable.bg,
            titleColor = Color.BLACK,
            descriptionColor = Color.BLACK,
            backgroundColor = Color.RED,
        ))
        addSlide(AppIntroFragment.newInstance(
            title = "Besin Arama",
            description = "Ana sayfada ekranın sağ üst kısmında yer alan arama butonu ile besin arayabilirsiniz.",
            imageDrawable = R.drawable.slide3,
            backgroundDrawable = R.color.orange,
            titleColor = Color.BLACK,
            descriptionColor = Color.BLACK,
            backgroundColor = Color.RED,

        ))
        addSlide(AppIntroFragment.newInstance(
            title = "Besin Sıralama",
            description = "Ana sayfada ekranın sağ üstünde bulunan sıralama simgeli butona tıklanıldığında seçili kategoriye ait besinler glisemik indeks değerine göre büyükten küçüğe sıralanmış halde listelenir.",
            imageDrawable = R.drawable.slide4,
            backgroundDrawable = R.drawable.bg,
            titleColor = Color.BLACK,
            descriptionColor = Color.BLACK,
            backgroundColor = Color.RED,

        ))
        addSlide(AppIntroFragment.newInstance(
            title = "Kategori Düzenleme",
            description = "Ana sayfada ekranın sağ üstünde bulunan kategori simgeli butona tıklanıldığında bu ekran gelir ve " +
                    "bu ekranda kategori listeleme/ekleme/silme/düzenleme işlemlerini yapabilirsiniz",
            imageDrawable = R.drawable.slide5,
            backgroundDrawable = R.drawable.bg,
            titleColor = Color.BLACK,
            descriptionColor = Color.BLACK,
            backgroundColor = Color.RED,

            ))

        setTransformer(AppIntroPageTransformerType.Flow)
        isSystemBackButtonLocked = true
        showStatusBar(false)

    }

    override fun onSkipPressed(currentFragment: Fragment?) {
        super.onSkipPressed(currentFragment)
        sha = getSharedPreferences("user", MODE_PRIVATE)
        edit = sha.edit()
        edit.putString("status","true")
        edit.commit()

        finish()
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)

        sha = getSharedPreferences("user", MODE_PRIVATE)
        edit = sha.edit()
        edit.putString("status","true")
        edit.commit()

        finish()
    }
}