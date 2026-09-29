package com.cognito.scanner.focus

import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private lateinit var toggleSwitch: Switch
    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toggleSwitch = findViewById(R.id.toggleSwitch)
        statusText = findViewById(R.id.statusText)

        toggleSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                startService(Intent(this, ScannerFocusService::class.java))
                statusText.text = "🟢 ACCESO"
                statusText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_green_dark))
            } else {
                stopService(Intent(this, ScannerFocusService::class.java))
                statusText.text = "🔴 SPENTO"
                statusText.setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
            }
        }

        val settingsButton = findViewById<Button>(R.id.settingsButton)
        settingsButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }
    }
}
