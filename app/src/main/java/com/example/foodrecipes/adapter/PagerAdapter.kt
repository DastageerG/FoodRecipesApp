package com.example.foodrecipes.adapter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter

class PagerAdapter(
    private val resultBundle: Bundle,
    private val fragmentList:ArrayList<Fragment>,
    private val fragmentTitle:ArrayList<String>,
    fm:FragmentManager
    ) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
{
    override fun getCount(): Int
    {
        return fragmentList.size
    } // getCount closed

    override fun getItem(position: Int): Fragment
    {
       fragmentList[position].arguments = resultBundle
        return fragmentList[position]
    } // getItem closed

    override fun getPageTitle(position: Int): CharSequence?
    {
        return fragmentTitle[position]
    }

} // PagerAdapter closed