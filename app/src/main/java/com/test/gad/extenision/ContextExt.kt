package com.test.gad.extenision

import android.content.Context
import android.location.LocationManager
import android.view.Gravity
import android.widget.Toast

fun Context.showToast(text: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
    if (text == null) return
    val toast = Toast.makeText(this, text, duration)
    toast.setGravity(Gravity.CENTER, 0, 0)
    toast.show()
}


fun Context.isGpsEnable(): Boolean {
    val locationManager =
        applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}


