package com.fluidsimulation.base

import android.os.*
import android.view.*
import androidx.fragment.app.*
import androidx.viewbinding.*

abstract class BaseFragment<B : ViewBinding>(val bindingFactory: (LayoutInflater) -> B) :
        Fragment() {

    var binding: B? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        create()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = bindingFactory(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.viewCreated()
        binding?.initListeners()
        binding?.initView()
    }

    abstract fun create()
    abstract fun B.initView()
    abstract fun B.initListeners()
    abstract fun B.viewCreated()
}