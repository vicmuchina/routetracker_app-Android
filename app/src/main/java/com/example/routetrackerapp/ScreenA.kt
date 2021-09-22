package com.example.routetrackerapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_screen_a.view.*


class ScreenA : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_screen_a, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.page_view)

        view.next.setOnClickListener {
            viewPager?.currentItem = 1

        }
        return view
    }


}