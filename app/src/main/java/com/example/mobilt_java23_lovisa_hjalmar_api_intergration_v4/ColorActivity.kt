package com.example.mobilt_java23_lovisa_hjalmar_api_intergration_v4

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ColorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_color)

        val codeBtn = findViewById<Button>(R.id.colorCodeButton)
        val schemeBtn = findViewById<Button>(R.id.colorSchemeButton)

        val frag1 = CodeFragment()
        val frag2 = SchemeFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragmentContainerView, frag1)
            commit()
        }

        codeBtn.setOnClickListener{
            supportFragmentManager.beginTransaction().apply {
                //replaces fragment
                replace(R.id.fragmentContainerView, frag1)
                //adds to backstack with name
                addToBackStack("code_fragment")
                setReorderingAllowed(true)
                commit()
            }
        }

        schemeBtn.setOnClickListener{
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView, frag2)
                addToBackStack("scheme_fragment")
                setReorderingAllowed(true)

                commit()
            }
        }
        

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}

