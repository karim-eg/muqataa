/*
 * Copyright (c) 2023-2024. Kotect Company, https://kotect.com
 * Main Programmer: Karim Abdallah
 * contact: support@kotect.com
 */

package co.encept.muqataa.ui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import co.encept.muqataa.R
import co.encept.muqataa.databinding.ActivityMainBinding
import com.google.android.gms.ads.MobileAds


class MainActivity : AppCompatActivity() {
    private lateinit var bind: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        bind = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        MobileAds.initialize(this) {}

        bind.apply {
            setupViewPager(viewPager)
            viewPager.isUserInputEnabled = false

            bind.apply {
                bottomNavigation.setOnItemSelectedListener { item ->
                    when(item.itemId) {
                        R.id.item_scan -> {
                            viewPager.setCurrentItem(0, false)
                        }

                        R.id.item_manual -> {
                            viewPager.setCurrentItem(1, false)
                        }

                        R.id.item_blacklist -> {
                            viewPager.setCurrentItem(2, false)
                        }

                        R.id.item_about -> {
                            viewPager.setCurrentItem(3, false)
                        }
                    }
                    true
                }
            }

            viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }
    }


    private fun setupViewPager(viewPager: ViewPager2) {
        val adapter2 = ViewPagerAdapter(this)
        adapter2.addFragment(ScannerFragment(), "")
        adapter2.addFragment(ManualBarCodeFragment(), "")
        adapter2.addFragment(BlacklistFragment(), "")
        adapter2.addFragment(AboutFragment(), "")
        viewPager.adapter = adapter2
    }


    internal class ViewPagerAdapter(fmanager: FragmentActivity) : FragmentStateAdapter(fmanager) {
        private val mFragmentList: MutableList<Fragment> = ArrayList()
        private val mFragmentTitleList: MutableList<String> = ArrayList()

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getItemCount(): Int = mFragmentList.size

        override fun createFragment(position: Int): Fragment = mFragmentList[position]
    }
}