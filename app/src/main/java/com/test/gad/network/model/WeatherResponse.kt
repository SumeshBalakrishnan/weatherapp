package com.test.gad.network.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherResponse(

	@PrimaryKey(autoGenerate = true)
	val id: Long?=null,

	@field:SerializedName("city")
	val city: City? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("list")
	val list: List<WeatherListItem?>? = null
) : Parcelable

@Parcelize
data class City(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("country")
	val country: String? = null,

) : Parcelable

@Parcelize
data class Main(

	@field:SerializedName("temp")
	val temp: Double? = null,

	@field:SerializedName("temp_min")
	val tempMin: Double? = null,

	@field:SerializedName("temp_max")
	val tempMax: Double? = null,

	@field:SerializedName("humidity")
	val humidity: Double? = null

) : Parcelable

@Parcelize
data class WeatherListItem(
	@field:SerializedName("main")
	val main: Main? = null
) : Parcelable
