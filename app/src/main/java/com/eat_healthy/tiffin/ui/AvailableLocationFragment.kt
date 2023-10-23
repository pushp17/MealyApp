package com.eat_healthy.tiffin.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.databinding.YoursLocalityBinding
import com.eat_healthy.tiffin.models.AvailableLocalityResponse
import com.eat_healthy.tiffin.models.GpsLocation
import com.eat_healthy.tiffin.utils.Constants
import com.eat_healthy.tiffin.viewmodels.AvailableLocalityListViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AvailableLocationFragment: BaseFragment()  {
    lateinit var navigationController: NavController
    lateinit var binding : YoursLocalityBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val availabLocalityViewModel:AvailableLocalityListViewModel by viewModels()
    lateinit var spinner: Spinner
    var selecdLocality:String?=null
    var subLocality:String?=null
    var locality:String?=null
    var adminArea:String?=null
    var fullGpsLocation:String?=null
    var gpsLocalityMatched=false
    var gpsLocation:GpsLocation?=null

    // We will UnComment this function when we will start taking current location

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        fetchCurrentLocation()
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=YoursLocalityBinding.inflate(inflater,container,false)
        spinner = binding.spinlocality
        navigationController = findNavController()
        availabLocalityViewModel.availablelocalistList()
        availabLocalityViewModel.availableLocalityLivedata.observe(viewLifecycleOwner, observer)
        binding.btSelectedLocationProceed.setOnClickListener {
            navigationController.navigate(
                R.id.action_currentLocationMapFragment_to_foodSelectionFragment
            )
        }
        return binding.root
    }



    private fun fetchCurrentLocation() {
        checkGpsEnabledOrNot()
    }

    // Check GPS enabled or not
    private fun checkGpsEnabledOrNot() {
        if (isGPSEnabled()) {
            checkLocationPermission()
        } else {
            showToast("GPS is not turned OFF , couldn't fetch current location")
            availabLocalityViewModel.availablelocalistList()
        }
    }
    private fun checkLocationPermission() {
        when {
            ((ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) || ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) -> {
                // You can use the API that requires the permission.
                Toast.makeText(
                    requireActivity(),
                    "Already permission given.",
                    Toast.LENGTH_LONG
                ).show()
                availabLocalityViewModel.availablelocalistList()
            }
            else -> {
                val locationPermissionRequest = registerForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { permissions ->
                    when {
                        permissions.getOrDefault(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            false
                        ) -> {
                            // Precise location access granted.
                            getLastKnownLocation()
                        }
                        permissions.getOrDefault(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            false
                        ) -> {
                            // Only approximate location access granted.
                            Toast.makeText(
                                requireActivity(),
                                "approximate location access granted.",
                                Toast.LENGTH_LONG
                            ).show()
                            getLastKnownLocation()
                        }
                        else -> {
                            // No location access granted.
                            Toast.makeText(
                                requireActivity(),
                                "No location access granted.",
                                Toast.LENGTH_LONG
                            ).show()
                            availabLocalityViewModel.availablelocalistList()
                        }
                    }
                }
                locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }
    @SuppressLint("MissingPermission")// since the permission is supressed because this is called after taking permission
    private fun getLastKnownLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                val geocoder = Geocoder(requireActivity(), Locale.getDefault())
                var addresses: List<Address?>? = null
                try {
                    addresses = geocoder.getFromLocation(
                        location!!.latitude,
                        location.longitude,
                        1
                    )
                } catch (ioException: Exception) {
                }
                val address = addresses?.get(0)
                val locality=address?.locality
                gpsLocation=GpsLocation(subLocality=address?.subLocality,locality=address?.locality,address?.subAdminArea)
                Toast.makeText(
                    requireActivity(),
                    locality,
                    Toast.LENGTH_LONG
                ).show()
            }
        availabLocalityViewModel.availablelocalistList()
    }
    private fun isGPSEnabled() =
        (requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager).isProviderEnabled(
            LocationManager.GPS_PROVIDER
        )

    override fun receivedResponse(item: Any?) {
        item?.let { response ->
            when (response) {
                is AvailableLocalityResponse-> {
                    if(response.availableLocalityLIst.isNullOrEmpty()) return

                    sharedPrefManager.addStringListToPreference(Constants.AVAILABLE_LOCALITY_LIST,response.availableLocalityLIst)

                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        response.availableLocalityLIst
                    ).also { adapter ->
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        spinner.adapter = adapter
                    }
                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parentView: AdapterView<*>?,
                            selectedItemView: View?,
                            position: Int,
                            id: Long
                        ) {
                            // your code here
                            selecdLocality= response.availableLocalityLIst.get(position)
                        }

                        override fun onNothingSelected(parentView: AdapterView<*>?) {
                        }
                    }
                    binding.btSelectedLocationProceed.setOnClickListener {
                        sharedPrefManager.addStringToPreference(Constants.USER_CITY_LOCALITY,
                            selecdLocality!!
                        )
                        navigationController.navigate(
                            R.id.action_currentLocationMapFragment_to_foodSelectionFragment
                        )
                    }
                    // Uncomment when we start taking current location
                   // setGpsLocation()
                }
                else -> {}
            }
        }
    }

    private fun setGpsLocation() {
        if (gpsLocation == null) {
            binding.tvLocality.visibility = View.GONE
            binding.btGpsLocationNotFound.text = "Sorry, You Have Not Given Current Localtion."
            return
        }

        subLocality = gpsLocation?.subLocality
        locality = gpsLocation?.locality
        if (subLocality != null) {
            fullGpsLocation = subLocality.plus(locality).plus(adminArea)
        } else {
            fullGpsLocation = locality.plus(adminArea)
        }

        if (subLocality != null) {
            if (selecdLocality!!.contains(subLocality!!) || selecdLocality!!.contains(locality!!)) {
                gpsLocalityMatched = true
            }
        } else if (selecdLocality != null && selecdLocality!!.contains(locality!!)) {
            gpsLocalityMatched = true
        }
        if (gpsLocalityMatched) {
            binding.btGpsLocationProceed.visibility = View.VISIBLE
            binding.btGpsLocationNotFound.visibility = View.GONE
            binding.btGpsLocationProceed.setOnClickListener {
                sharedPrefManager.addStringToPreference(Constants.USER_CITY_LOCALITY,
                    fullGpsLocation!!
                )
                navigationController.navigate(
                    R.id.action_currentLocationMapFragment_to_foodSelectionFragment
                )
            }
        } else {
            binding.btGpsLocationProceed.visibility = View.GONE
            binding.btGpsLocationNotFound.visibility = View.VISIBLE
        }

    }

}