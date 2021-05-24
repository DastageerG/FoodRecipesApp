package com.example.foodrecipes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.foodrecipes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(ActivityMainBinding.inflate(layoutInflater).root)

    } // onCreate closed

} // Main Activity closed