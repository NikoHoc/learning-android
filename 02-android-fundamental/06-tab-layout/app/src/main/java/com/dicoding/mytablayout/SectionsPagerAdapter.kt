package com.dicoding.mytablayout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var appName: String = "tessss"

    override fun getItemCount(): Int {
        return 3
    }

    // jika fragment lebih dri 1
//    override fun createFragment(position: Int): Fragment {
//        var fragment: Fragment? = null
//        when (position) {
//            0 -> fragment = HomeFragment()
//            1 -> fragment = ProfileFragment()
//        }
//        return fragment as Fragment
//    }

    //jika fragmentnya hanya 1
    override fun createFragment(position: Int): Fragment {
        val fragment = HomeFragment()
        fragment.arguments = Bundle().apply {
            putInt(HomeFragment.ARG_SECTION_NUMBER, position + 1)
            //putstring pakai bundle kirim data
            putString(HomeFragment.ARG_NAME, appName)
        }
        return fragment
    }
}