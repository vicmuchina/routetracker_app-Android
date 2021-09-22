package com.example.routetrackerapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_screen_b.view.*


class ScreenB : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_screen_b, container, false)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.page_view)

        view.previous_b.setOnClickListener {

            viewPager?.currentItem = 0

        }
        view.next_b.setOnClickListener {

            findNavController().navigate(R.id.action_viewPager_to_login2)

            onBoardingFinished()
        }

        return view
    }

    private fun onBoardingFinished() {
        val sharedPref = requireActivity().getSharedPreferences("onBoarding" , Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean("Finished",true)

        editor.apply()
    }


}