package com.ramonguimaraes.horacerta.presenter.companiesList.ui

import android.Manifest
import android.R
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.ramonguimaraes.horacerta.databinding.FragmentCompaniesBinding
import com.ramonguimaraes.horacerta.domain.resource.Resource
import com.ramonguimaraes.horacerta.presenter.companiesList.ui.adapter.CompaniesAdapter
import com.ramonguimaraes.horacerta.presenter.companiesList.viewModel.CompaniesViewModel
import com.ramonguimaraes.horacerta.presenter.viewUtils.extensions.gone
import com.ramonguimaraes.horacerta.presenter.viewUtils.extensions.visible
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale


class CompaniesFragment : Fragment() {

    private val binding: FragmentCompaniesBinding by lazy {
        FragmentCompaniesBinding.inflate(layoutInflater)
    }
    private val companiesViewModel: CompaniesViewModel by viewModel()
    private val companiesAdapter = CompaniesAdapter()
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private lateinit var locationPermissionRequest: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureSegmentsSpinner()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        locationPermissionRequest = registerForLocationPermission()
        requestLocationPermissions()
        companiesViewModel.companies.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showLoading()
                    hideError()
                    companiesAdapter.setCompanyList(emptyList())
                }

                is Resource.Success -> {
                    hideLoading()
                    hideError()
                    companiesAdapter.setCompanyList(it.result)
                }

                is Resource.Failure -> {
                    hideLoading()
                    showError(it.exception.message.toString())
                }
            }
        }

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                companiesAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                companiesAdapter.filter.filter(newText)
                return true
            }
        })

        companiesAdapter.setClickListener {
            openScheduleRegistration(it.companyUid)
        }
        binding.rvCompanies.adapter = companiesAdapter
        binding.rvCompanies.layoutManager = LinearLayoutManager(context)
        return binding.root
    }

    private fun openScheduleRegistration(companyUID: String) {
        val action =
            CompaniesFragmentDirections.actionCompaniesToScheduleRegistrationFragment(companyUID = companyUID)
        findNavController().navigate(action)

    }

    private fun showLoading() {
        binding.progressBar.visible()
        binding.spnSegments.isEnabled = false
    }

    private fun hideLoading() {
        binding.progressBar.gone()
        binding.spnSegments.isEnabled = true
    }

    private fun showError(error: String) {
        binding.error.visible()
        binding.error.text = error
    }

    private fun hideError() {
        binding.error.gone()
    }

    private fun configureSegmentsSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_dropdown_item_1line,
            companiesViewModel.segments
        )

        binding.spnSegments.adapter = adapter
        binding.spnSegments.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val segment = binding.spnSegments.selectedItem.toString()

                getLocation {
                    companiesViewModel.loadBySegment(segment, it)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun registerForLocationPermission(): ActivityResultLauncher<Array<String>> {
        return registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    Log.d("PERMISSAO", "permissão: ACCESS_FINE_LOCATION")
                    getLocation {
                        companiesViewModel.load(it)
                    }
                }

                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    Log.d("PERMISSAO", "permissão: ACCESS_COARSE_LOCATION")
                    getLocation {
                        companiesViewModel.load(it)
                    }
                }

                else -> {
                    val spannableString =
                        SpannableString("acesse configurações e conceda permissção de localização para listar as empresas")
                    val clickableSpan = object : ClickableSpan() {
                        override fun onClick(widget: View) {
                            systemSettings()
                        }
                    }
                    spannableString.setSpan(clickableSpan, 7, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    binding.error.visible()
                    binding.error.text = spannableString
                    binding.error.movementMethod = LinkMovementMethod.getInstance()
                }
            }
        }
    }

    private fun systemSettings() {
        val intent = Intent(
            ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireContext().packageName, null)
        )
        startActivity(intent)
        requireActivity().finish()
    }

    private fun requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            getLocation {
                companiesViewModel.load(it)
            }
        }
    }

    private fun getLocation(callback: (Address?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fusedLocationClient?.lastLocation?.addOnCompleteListener { locationTask ->
            locationTask.addOnSuccessListener { location: Location? ->
                if (location == null) return@addOnSuccessListener
                val latLng = LatLng(location.latitude, location.longitude)
                val address = getAddressFromLocation(latLng.latitude, latLng.longitude)
                callback.invoke(address)
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun getAddressFromLocation(latitude: Double, longitude: Double): Address? {
        val geocoder = Geocoder(requireContext(), Locale("pt", "BR"))
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(latitude, longitude, 1)?.get(0)
        } else {
            val addressList = geocoder.getFromLocation(latitude, longitude, 1)
            addressList?.get(0)
        }
    }
}
