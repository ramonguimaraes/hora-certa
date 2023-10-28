package com.ramonguimaraes.horacerta.presenter.companyProfile.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdate

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.ramonguimaraes.horacerta.R
import com.ramonguimaraes.horacerta.databinding.FragmentMapsBinding
import com.ramonguimaraes.horacerta.presenter.companyProfile.viewModel.CompanyProfileViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.util.Locale

class MapsFragment : Fragment() {

    private val binding: FragmentMapsBinding by lazy {
        FragmentMapsBinding.inflate(layoutInflater)
    }

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val viewModel: CompanyProfileViewModel by sharedViewModel()
    private var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestLocationPermissions()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.editAddress.setOnClickListener {
            showAddressBottomSheet()
        }

        binding.btnConfirm.setOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { googleMap ->
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {

                fusedLocationClient?.lastLocation?.addOnCompleteListener { locationTask ->
                    locationTask.addOnSuccessListener { location: Location? ->
                        if (location == null) return@addOnSuccessListener
                        val latLng = LatLng(location.latitude, location.longitude)
                        moveCamera(googleMap, latLng)
                        addMarker(googleMap, latLng)
                        getAddressFromLocation(latLng.latitude, latLng.longitude)
                    }
                }
            }

            googleMap.setOnMapClickListener { latLng ->
                moveCamera(googleMap, latLng)
                addMarker(googleMap, latLng)
                getAddressFromLocation(latLng.latitude, latLng.longitude)
            }
        }
    }

    private fun showAddressBottomSheet() {
        AddressBottomSheet()
            .show(childFragmentManager, "")
    }

    private fun addMarker(googleMap: GoogleMap, latLng: LatLng) {
        marker?.remove()
        val markerOptions =
            MarkerOptions().position(latLng).title(viewModel.profileView.value?.companyName)
        marker = googleMap.addMarker(markerOptions)
    }

    private fun moveCamera(googleMap: GoogleMap, latLng: LatLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(19f), 2000, null)
    }

    private fun requestLocationPermissions() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    fusedLocationClient =
                        LocationServices.getFusedLocationProviderClient(requireContext())

                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    fusedLocationClient =
                        LocationServices.getFusedLocationProviderClient(requireContext())
                }

                else -> {

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

    @Suppress("DEPRECATION")
    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(requireContext(), Locale("pt", "BR"))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(latitude, longitude, 1) { addressList ->
                addressList[0]?.let { address: Address -> viewModel.setAddress(address) }
            }
        } else {
            val addressList = geocoder.getFromLocation(latitude, longitude, 1)
            if (!addressList.isNullOrEmpty()) {
                addressList[0]?.let { address: Address -> viewModel.setAddress(address) }
            }
        }
    }
}