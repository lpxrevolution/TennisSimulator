package com.example.simuladortennis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MatchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)
        //La unica función de este Activity es contener los Fragments por lo que, solo pasará datos del anterior Activity al primer Fragment y lo mostrará
        val bundle = intent.extras
        val categoria : Int? = bundle?.getInt("cat")
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = frgMascFem()
        val bundleToFrag = Bundle()
        if (categoria != null) {
            bundleToFrag.putInt("cat", categoria)
        }
        fragment.arguments = bundleToFrag
        fragmentTransaction.replace(R.id.fragment_Container,fragment)
        fragmentTransaction.disallowAddToBackStack()
        fragmentTransaction.commit()
    }
}