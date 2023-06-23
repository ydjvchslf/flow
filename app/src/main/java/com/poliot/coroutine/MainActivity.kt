package com.poliot.coroutine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.forEach
import androidx.lifecycle.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.poliot.coroutine.databinding.ActivityMainBinding
import com.poliot.coroutine.util.DebugLog
import com.poliot.coroutine.util.textChangesToFlow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val logTag = MainActivity::class.simpleName
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 네비게이션
        val bottomNavView = binding.bottomNav
        val navController =
            (supportFragmentManager.findFragmentById(R.id.main_nav_host) as NavHostFragment).findNavController()
        bottomNavView.setupWithNavController(navController)

        // 네비게이션 메뉴 재선택 UI 오류 수정
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            if (destination.id != bottomNavView.selectedItemId) {
                controller.currentBackStack.value.asReversed().drop(1).forEach { entry ->
                    bottomNavView.menu.forEach { item ->
                        if (entry.destination.id == item.itemId) {
                            item.isChecked = true
                            return@addOnDestinationChangedListener
                        }
                    }
                }
            }
        }
    }
}
