package com.example.routetrackerapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_view_pager.view.*

class ViewPager : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        //fragment list
        val fragmentList = arrayListOf<Fragment>(
            ScreenA(),
            ScreenB()

        )


        val Adapter = PagerAdapter(fragmentList,requireActivity().supportFragmentManager,lifecycle)

       view.page_view.adapter = Adapter

        return view

    }


}