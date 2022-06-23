package com.test.gad

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.test.gad.adapter.UsersListAdapter
import com.test.gad.database.DatabaseUserListItem
import com.test.gad.databinding.ActivityMainBinding
import com.test.gad.extenision.isGpsEnable
import com.test.gad.extenision.showToast
import com.test.gad.network.model.Main
import com.test.gad.network.model.WeatherListItem
import com.test.gad.utils.NetworkResult
import com.test.gad.utils.Utils
import com.test.gad.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var adapter: UsersListAdapter

    private val permissionCode = 123
    private val permission = listOf<String>(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private var lat: String? = null
    private var lng: String? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationRequest = LocationRequest.create()
    private var callback: LocationCallback? = null

    private var testlist : ArrayList<DatabaseUserListItem> = arrayListOf()

    var databaseUserListItem: DatabaseUserListItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getFusedLocation()
        initView()
        setObserver()
    }

    private fun getFusedLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        Log.e("getFusedLocation====", "fusedLocationClient : $fusedLocationClient")
        checkPermissions()
    }

    private fun checkPermissions() {
        val requestPermission = ArrayList<String>()
        for (perm in permission) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    perm
                ) != PermissionChecker.PERMISSION_GRANTED
            ) {
                requestPermission.add(perm)
            }
        }
        if (requestPermission.isEmpty()) {
            //launchLogin()
            if (!isGpsEnable()) {
                displayLocationSettingsRequest(this, true)
                showToast("Gps is Offs")
                binding.pbDog.visibility = View.GONE
                getVisibility(false)
            } else {
                getLastKnownLocation()
                displayLocationSettingsRequest(this, false)
            }
        } else {
            ActivityCompat.requestPermissions(this, permission.toTypedArray(), permissionCode)
            binding.pbDog.visibility = View.GONE
        }
    }

    private fun displayLocationSettingsRequest(context: Context, buildershow: Boolean) {
        val googleApiClient = GoogleApiClient.Builder(context)
            .addApi(LocationServices.API).build()
        googleApiClient.connect()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000 / 2.toLong()
        val builder =
            LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        if (buildershow) {
            builder.setAlwaysShow(true)
        } else {
            builder.setAlwaysShow(false)
        }

        val task = LocationServices.getSettingsClient(this)
            .checkLocationSettings(builder.build())


        task.addOnCompleteListener { response ->
            if (response.isComplete) {
                //Do something
                startLocationUpdate()
            }
        }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    // Handle result in onActivityResult()
                    val intentSenderRequest = IntentSenderRequest.Builder(
                        e.resolution
                    ).build()
                    resolutionForResult.launch(intentSenderRequest)
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }
    }


    private val resolutionForResult = registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    ) { activityResult ->
        if (activityResult.resultCode == Activity.RESULT_OK) {
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?

            if (locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true) {
                displayLocationSettingsRequest(this, false)
            }
        }
    }

    private fun startLocationUpdate() {
        locationCallback()
        if (::fusedLocationClient.isInitialized) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            fusedLocationClient.requestLocationUpdates(locationRequest, callback, null)
        }
    }

    private fun locationCallback() {
        callback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                lat = locationResult.locations.get(0).latitude.toString()
                lng = locationResult.locations.get(0).longitude.toString()
                println("===latitude== : $lat")
                println("===longitude== : $lng")
                //triggerAPI(lat.toString(), lng.toString())
            }
        }
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    lat = location.latitude.toString()
                    lng = location.longitude.toString()
                    triggerAPI(lat, lng)
                }
            }
    }

    private fun triggerAPI(lat: String?, lng: String?) {
        if (Utils.hasInternetConnection(this)) {
            binding.pbDog.visibility = View.VISIBLE
            getVisibility(true)
            viewModel.fetchWeatherResponse(lat, lng)
        } else {
            Toast.makeText(
                this, resources.getString(R.string.no_internet),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getVisibility(visible: Boolean) {
        if (visible) {
            binding.pbDog.visibility = View.GONE
            binding.lytItemHead.visibility = View.VISIBLE
            binding.tvCity.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.VISIBLE
            binding.ivNoData.visibility = View.GONE
        } else {
            binding.pbDog.visibility = View.GONE
            binding.lytItemHead.visibility = View.GONE
            binding.tvCity.visibility = View.GONE
            binding.recyclerView.visibility = View.GONE
            binding.ivNoData.visibility = View.VISIBLE
        }
    }

    private fun setObserver() {
        //binding.pbDog.visibility = View.VISIBLE
        viewModel.response.observe(this) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    val city = response.data?.city?.name + ", " + response.data?.city?.country
                    response.data?.let {
                        for(item in response.data.list!!){
                            databaseUserListItem = DatabaseUserListItem(city, item?.main!!.humidity,
                                item?.main?.tempMax, item?.main?.tempMin)
                            testlist.add(databaseUserListItem!!)
                        }
                        adapter.submitList(testlist)
                    }
                    println("==Success====")
                    binding.pbDog.visibility = View.GONE
                    binding.tvCity.text = city
                }

                is NetworkResult.Error -> {
                    // _binding.pbDog.visibility = View.GONE
                    Toast.makeText(
                        this,
                        response.message,
                        Toast.LENGTH_SHORT
                    ).show()

                    println("==Error====${response.message}")
                    binding.pbDog.visibility = View.GONE
                    getVisibility(false)
                }

                is NetworkResult.Loading -> {
                    binding.pbDog.visibility = View.VISIBLE
                }
            }
        }

        viewModel.test?.observe(this){
            binding.tvCity.text = it.get(0).city
            if(it.isNotEmpty()){
                adapter.submitList(it)
                getVisibility(true)
                adapter.notifyDataSetChanged()
            }else{
                getVisibility(false)
            }

        }
    }

    private fun initView() {
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.recyclerView.adapter = adapter

        binding.swipe.setOnRefreshListener {
            getFusedLocation()
            binding.swipe.isRefreshing = false
        }
    }


    private fun showAlert(mesg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Need permission(s)")
        builder.setMessage(mesg)
        builder.setPositiveButton("OK", { dialog, which -> checkPermissions() })
        builder.setNeutralButton("Cancel", null)
        val dialog = builder.create()
        dialog.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionCode && grantResults.isNotEmpty()) {
            var deniedCount: Int = 0
            val permissionResult = HashMap<String, Int>()

            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionResult[permissions[i]] = grantResults[i]
                    deniedCount++
                }
            }
            if (deniedCount == 0) {
                triggerAPI(lat, lng)
            } else {
                for ((permName, permResult) in permissionResult) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permName)) {
                        showAlert("Some permissions are required to do the task.")
                    } else {
                        val builder = AlertDialog.Builder(this)
                        //denied_imp_permission
                        builder.setMessage("You have denied few important permission which are needed for the application. Please allow all permission at Settings -> Permissions")
                        builder.setPositiveButton(
                            "Go to Settings"
                        ) { dialog, which ->
                            dialog.dismiss()
                            val intent = Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", packageName, null)
                            )
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        }
                        builder.setNegativeButton(
                            "No"
                        ) { dialog, which ->
                            dialog.dismiss()
                            finish()
                        }
                        builder.show()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.recyclerView.adapter = null
    }

    // Stop location updates
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(callback)
    }

    // Stop receiving location update when activity not visible/foreground
    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }
}