package com.sun.unsplash_02.screen.home

import android.view.View
import android.widget.Toast
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseFragment
import com.sun.unsplash_02.data.model.Collection
import com.sun.unsplash_02.data.source.ImageRepository
import com.sun.unsplash_02.screen.main.MainActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), HomeContract.View {

    lateinit var collectionAdapter: CollectionAdapter
    lateinit var homePresenter: HomePresenter

    override fun getLayoutResourceId() = R.layout.fragment_home

    override fun onViewCreated(view: View) {
        HomePresenter(ImageRepository.getInstance()).run {
            setView(this@HomeFragment)
            onStart()
            homePresenter = this
        }
    }

    override fun onInit() {
        CollectionAdapter().run {
            setOnItemClickListener {
                Toast.makeText(context, it.id, Toast.LENGTH_SHORT).show()
            }
            collectionAdapter = this
            recyclerCollection.adapter = this
        }
        super.onInit()
    }

    override fun onDestroy() {
        super.onDestroy()
        homePresenter.onStop()
    }

    override fun showLoading() {
        (getBaseActivity() as MainActivity).showDialogLoading()
    }

    override fun hideLoading() {
        (getBaseActivity() as MainActivity).hideDialogLoading()
    }

    override fun onCollectionLoaded(collections: MutableList<Collection>) {
        collectionAdapter.setListCollections(collections)
    }

    override fun onError(e: Exception?) {
        Toast.makeText(context, e?.message, Toast.LENGTH_LONG).show()
    }

    companion object {
        fun newInstance() = HomeFragment()
    }
}
