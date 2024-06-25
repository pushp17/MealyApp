package com.eat_healthy.tiffin.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.databinding.ReferalLayoutBinding
import com.eat_healthy.tiffin.models.MRefrer
import com.eat_healthy.tiffin.models.ReferalResponse
import com.eat_healthy.tiffin.models.User
import com.eat_healthy.tiffin.utils.AppUtils.getStringValueInNextLine
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.viewmodels.ReferalViewModel
import com.eat_healthy.tiffin.viewmodels.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@AndroidEntryPoint
class ReferalFragment : BaseFragment() {
    lateinit var binding: ReferalLayoutBinding
    private val referalViewModel: ReferalViewModel by viewModels()
    private var local_image_uri: Uri? = null
    private var image_file: File? = null
    var referalData: MRefrer? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()
    val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val PERMISSION_REQUEST_CODE = 1000
    override fun onAttach(context: Context) {
        super.onAttach(context)
        checkPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ReferalLayoutBinding.inflate(inflater, container, false)
        requireActivity().window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.referalBackgroundColour)
        referalData =
            sharedPrefManager.getModelClass<MRefrer>(Constants.REFERAL_USER_DATA)
        if (referalData != null) {
            setDataInViews(referalData)
        } else {
            sharedViewModel.referalResponseLiveData.observe(viewLifecycleOwner, observer)
            if (sharedViewModel.referalApiCalled != true) {
                sharedViewModel.fetchReferalData(
                    User(
                        sharedViewModel.userDetail?.username,
                        sharedViewModel.userDetail?.mobileno
                    ), sharedPrefManager
                )
            }
        }
        //  saveBitmapToFile()
        binding.referalCodeShareButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                checkIfFileExists()
                shareImageWithTextAndLink()
            }
        }
        return binding.root
    }

    override fun receivedResponse(item: Any?) {
        item?.let { response ->
            when (response) {
                is ReferalResponse -> {
                    response.mRefrer?.let {
                        referalData = it
                        setDataInViews(it)
                    }
                }

                else -> {

                }
            }
        }
    }

    private fun shareImageWithTextAndLink() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        if (image_file != null) {
            shareIntent.type = "image/*"
            shareIntent.putExtra(
                Intent.EXTRA_STREAM, FileProvider.getUriForFile(
                    requireContext(), requireActivity().getPackageName() + ".fileprovider",
                    image_file!!
                )
            )
            // Add text and a link
          //  val textToShare = referalData?.referalSharedMsg
            val textToShare = "Homely North Indian Mealy only @59 \n\n Hey! I Just invited you to checkout this app to get the homely north indian meal at electronic city. Use the below link"

            val linkToShare =
                "https://play.google.com/store/apps/details?id=com.eat_healthy.tiffin&referrer=utm_source%3" + referalData?.referalCode
            shareIntent.putExtra(Intent.EXTRA_TEXT, "$textToShare $linkToShare")
        } else {
            shareIntent.type = "text/plain"
            val textToShare = referalData?.referalSharedMsg
            val linkToShare =
                "https://play.google.com/store/apps/details?id=com.eat_healthy.tiffin&referrer=utm_source%3" + referalData?.referalCode
            shareIntent.putExtra(Intent.EXTRA_TEXT, "$textToShare $linkToShare")
        }
        // Launch the intent chooser
        startActivity(Intent.createChooser(shareIntent, "Share image with"))
    }

    private fun setDataInViews(mRefrer: MRefrer?) {
        binding.referalAmount.text = "Earn upto ".plus("₹").plus(mRefrer?.rewardAmount).plus(" per friend")


        binding.referalDescription.text = getStringValueInNextLine(mRefrer?.referalMsg)
        Glide.with(requireActivity()).load(com.eat_healthy.tiffin.R.drawable.refer_earn)
            .into(binding.referalImage)
    }

    @Throws(IOException::class)
    suspend fun saveBitmapToFile() {
            binding.progressBar.visibility = View.VISIBLE
            binding.referalCodeShareButton.isEnabled = false
            withContext(Dispatchers.IO) {
                val referal_bitmap = Glide.with(requireActivity())
                    .asBitmap()
                    .load(R.drawable.mealy_referal_share_img)
                    .submit()
                    .get()
                try {
                    val cachePath = File(requireContext().getCacheDir(), "mealy_images")
                    cachePath.mkdirs()
                    val stream = FileOutputStream("$cachePath/referal_image.jpg")
                    referal_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    stream.close()
                    image_file = File("$cachePath/referal_image.jpg")
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            binding.progressBar.visibility = View.GONE
            binding.referalCodeShareButton.isEnabled = true
    }

    suspend fun checkIfFileExists() {
        val cachePath = File(requireContext().getCacheDir(), "mealy_images")
        cachePath.mkdirs()
        val file = File("$cachePath/referal_image.jpg")
        if (file.exists()) {
            image_file = file
        } else {
            saveBitmapToFile()
        }
    }

    private fun checkPermission() {
        when {
            (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED)
                    && (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED) -> {
                // You can use the API that requires the permission.
                CoroutineScope(Dispatchers.Main).launch {
                    checkIfFileExists()
                }
            }

            else -> {
                requestPermissions(
                    permissions,
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                Log.d("sxcnsdcds", "permission granted")
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    CoroutineScope(Dispatchers.Main).launch {
                        checkIfFileExists()
                    }
                }
                return
            }

            else -> {
                // Ignore all other requests.
            }
        }
    }

    override fun onPause() {
        requireActivity().window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.colorPrimary)
        super.onPause()
    }

    override fun onResume() {
        requireActivity().window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.referalBackgroundColour)
        super.onResume()
    }
}