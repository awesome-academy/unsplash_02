package com.sun.unsplash_02.base

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

abstract class BaseFragment : Fragment() {

    abstract fun getLayoutResourceId(): Int

    private var weakReference: WeakReference<Activity>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weakReference = WeakReference<Activity>(activity)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(view)
        onViewCreated()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(getLayoutResourceId(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.let {
            it.isClickable = true
        }
    }

    fun getBaseActivity(): Activity? = weakReference?.get()

    open fun onViewCreated() {
        onInit()
        onEvent()
    }

    abstract fun onViewCreated(view: View)

    open fun onInit() {}

    open fun onEvent() {}
}
