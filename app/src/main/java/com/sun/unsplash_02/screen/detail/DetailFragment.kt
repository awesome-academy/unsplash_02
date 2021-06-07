package com.sun.unsplash_02.screen.detail

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.sun.unsplash_02.R
import com.sun.unsplash_02.base.BaseFragment
import com.sun.unsplash_02.data.model.Image
import com.sun.unsplash_02.data.model.TypeEdit
import com.sun.unsplash_02.screen.edit.EditActivity
import com.sun.unsplash_02.utils.extension.showAlertDialog
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_detail.view.*

class DetailFragment : BaseFragment(), View.OnClickListener, DetailContract.View {

    private lateinit var detailPresenter: DetailPresenter
    private var permissionLauncher: ActivityResultLauncher<Array<String>>? = null
    private var progressDialog: ProgressDialog? = null
    private var isReadPermissionGranted = false
    private var isWritePermissionGranted = false
    private var imageData: Image? = null

    override fun getLayoutResourceId() = R.layout.fragment_detail

    override fun initView(view: View) {
        view.toolbarDetail.run {
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                activity?.supportFragmentManager?.popBackStack()
            }
        }
        view.run {
            imageCrop.setOnClickListener(this@DetailFragment)
            imageFilter.setOnClickListener(this@DetailFragment)
            imageDraw.setOnClickListener(this@DetailFragment)
            imageBrightness.setOnClickListener(this@DetailFragment)
            imageIcon.setOnClickListener(this@DetailFragment)
        }
    }

    override fun initData() {
        imageData = arguments?.getParcelable(ARGUMENT_IMAGE)
        DetailPresenter().run {
            setView(this@DetailFragment)
            onStart()
            imageData?.let {
                loadImage(imageDetail, it.urls.full)
            }
            detailPresenter = this
        }
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isReadPermissionGranted =
                    permissions[Manifest.permission.READ_EXTERNAL_STORAGE]
                        ?: isReadPermissionGranted
                isWritePermissionGranted = permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE]
                    ?: isWritePermissionGranted
            }
        updateOrRequestPermission()
    }

    override fun onClick(v: View) {
        activity?.showAlertDialog(getString(R.string.want_to_download)) {
            when (v.id) {
                R.id.imageCrop -> {
                }
                R.id.imageFilter -> {
                    imageData?.let {
                        detailPresenter.downloadImage(
                            requireContext(),
                            it,
                            TypeEdit.FILTER.value
                        )
                    }
                }
                R.id.imageDraw -> {
                    imageData?.let {
                        detailPresenter.downloadImage(
                            requireContext(),
                            it,
                            TypeEdit.DRAW.value
                        )
                    }
                }
                R.id.imageBrightness -> {
                    imageData?.let {
                        detailPresenter.downloadImage(
                            requireContext(),
                            it,
                            TypeEdit.BRIGHTNESS.value
                        )
                    }
                }
                R.id.imageIcon -> {
                }
            }
        }
    }

    override fun onStartDownload() =
        ProgressDialog(requireContext()).run {
            setMessage(resources.getString(R.string.downloading_file))
            max = 100
            isIndeterminate = false
            setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            setCancelable(true)
            progressDialog = this
            show()
        }

    override fun onUpdateProgressBar(value: Int) {
        progressDialog?.progress = value
    }

    override fun onComplete(imagePath: String, type: Int) {
        progressDialog?.dismiss()
        startActivity(
            EditActivity.getEditIntent(
                requireContext(),
                imagePath,
                type
            )
        )
    }

    override fun onError(e: Exception?) {
        progressDialog?.dismiss()
        Toast.makeText(context, e?.message, Toast.LENGTH_SHORT).show()
    }

    private fun updateOrRequestPermission() {
        val hasReadPermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val hasWritePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        val minSdk29 = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        isReadPermissionGranted = hasReadPermission
        isWritePermissionGranted = hasWritePermission || minSdk29
        val permissionsToRequest = mutableListOf<String>()
        if (!isReadPermissionGranted) {
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (!isWritePermissionGranted) {
            permissionsToRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permissionsToRequest.isNotEmpty()) {
            permissionLauncher?.launch(permissionsToRequest.toTypedArray())
        }
    }

    companion object {
        private const val ARGUMENT_IMAGE = "ARGUMENT_IMAGE"

        fun newInstance(image: Image) = DetailFragment().apply {
            arguments = bundleOf(ARGUMENT_IMAGE to image)
        }
    }
}
