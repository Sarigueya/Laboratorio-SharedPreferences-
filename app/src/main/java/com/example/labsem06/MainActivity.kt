package com.example.labsem06

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences

    private val PREFS_NAME = "user_preferences"
    private val keyUsername = "username"
    private val keyDarkMode = "dark_mode"
    private val keyRemember = "remember_session"
    private val keyLaunchCount = "launch_count"
    private val keyLastSaved = "last_saved"

    private lateinit var etUsername: EditText
    private lateinit var swDarkMode: MaterialSwitch
    private lateinit var swRemember: MaterialSwitch
    private lateinit var tvLaunchCount: TextView
    private lateinit var tvLastSaved: TextView
    private var suppressRememberDialog = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        val targetMode = if (preferences.getBoolean(keyDarkMode, false)) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        if (AppCompatDelegate.getDefaultNightMode() != targetMode) {
            AppCompatDelegate.setDefaultNightMode(targetMode)
        }

        etUsername = findViewById(R.id.et_username)
        swDarkMode = findViewById(R.id.sw_dark_mode)
        swRemember = findViewById(R.id.sw_remember)
        tvLaunchCount = findViewById(R.id.tv_launch_count)
        tvLastSaved = findViewById(R.id.tv_last_saved)
        val btnSave = findViewById<Button>(R.id.btn_save)
        val btnReset = findViewById<Button>(R.id.btn_reset)

        val rememberSession = preferences.getBoolean(keyRemember, false)
        swRemember.isChecked = rememberSession
        etUsername.setText(if (rememberSession) preferences.getString(keyUsername, "") else "")
        swDarkMode.isChecked = preferences.getBoolean(keyDarkMode, false)

        var currentLaunches = preferences.getInt(keyLaunchCount, 0)
        if (savedInstanceState == null) {
            currentLaunches += 1
            preferences.edit().putInt(keyLaunchCount, currentLaunches).apply()
        }
        tvLaunchCount.text = getString(R.string.launch_count_format, currentLaunches)

        val lastSaved = preferences.getString(keyLastSaved, null)
        tvLastSaved.text = if (lastSaved != null) {
            getString(R.string.last_saved_format, lastSaved)
        } else {
            getString(R.string.last_saved_none)
        }

        etUsername.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) etUsername.error = null
        }

        swDarkMode.setOnCheckedChangeListener { _, isChecked ->
            preferences.edit().putBoolean(keyDarkMode, isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        }

        swRemember.setOnCheckedChangeListener { _, isChecked ->
            if (!suppressRememberDialog) {
                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.dialog_remember_title)
                    .setMessage(R.string.dialog_remember_message)
                    .setPositiveButton(android.R.string.ok) { dialog, _ -> dialog.dismiss() }
                    .show()
            }
            preferences.edit().putBoolean(keyRemember, isChecked).apply()
        }

        btnSave.setOnClickListener {
            val username = etUsername.text.toString().trim()
            if (username.isEmpty()) {
                etUsername.error = getString(R.string.err_empty_username)
                return@setOnClickListener
            }
            if (username.length > 24) {
                etUsername.error = getString(R.string.err_too_long)
                return@setOnClickListener
            }
            val now = DateFormat.getTimeFormat(this).format(Date())
            preferences.edit()
                .putString(keyUsername, username)
                .putBoolean(keyDarkMode, swDarkMode.isChecked)
                .putBoolean(keyRemember, swRemember.isChecked)
                .putString(keyLastSaved, now)
                .apply()
            tvLastSaved.text = getString(R.string.last_saved_format, now)
            Toast.makeText(this, R.string.msg_saved, Toast.LENGTH_SHORT).show()
        }

        btnReset.setOnClickListener {
            preferences.edit().clear().apply()
            etUsername.setText("")
            swDarkMode.isChecked = false
            suppressRememberDialog = true
            swRemember.isChecked = false
            suppressRememberDialog = false
            tvLaunchCount.text = getString(R.string.launch_count_default)
            tvLastSaved.text = getString(R.string.last_saved_none)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            Toast.makeText(this, R.string.msg_reset, Toast.LENGTH_SHORT).show()
        }
    }
}