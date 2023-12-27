package com.ms.smogometr

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ms.smogometr.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bConnectDevice.setOnClickListener{
            val explicitIntent = Intent(applicationContext, ConnectDeviceActivity::class.java)
            startActivity(explicitIntent)
        }

        binding.bMeasurement.setOnClickListener{
            val explicitIntent = Intent(applicationContext, MeasurementActivity::class.java)
            startActivity(explicitIntent)
        }

        binding.bMap.setOnClickListener{
            val explicitIntent = Intent(applicationContext, MapActivity::class.java)
            startActivity(explicitIntent)
        }

        binding.bSettings.setOnClickListener{
            val explicitIntent = Intent(applicationContext, SettingsActivity::class.java)
            startActivity(explicitIntent)
        }

        binding.bAbout.setOnClickListener{
            val explicitIntent = Intent(applicationContext, AboutActivity::class.java)
            startActivity(explicitIntent)
        }


    }
}