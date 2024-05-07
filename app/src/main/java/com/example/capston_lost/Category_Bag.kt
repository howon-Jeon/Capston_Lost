package com.example.capston_lost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * A simple [Fragment] subclass.
 * Use the [Category_Bag.newInstance] factory method to
 * create an instance of this fragment.
 */
class Category_Bag : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category__bag, container, false)
    }

    private fun newInstant() : Category_Bag {
        val args = Bundle()
        val frag = Category_Bag()
        frag.arguments = args
        return frag
    }
}