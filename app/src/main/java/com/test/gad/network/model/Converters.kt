package com.test.gad.network.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromGroupTaskMemberList(value: List<Main>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Main>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toGroupTaskMemberList(value: String): List<Main> {
        val gson = Gson()
        val type = object : TypeToken<List<Main>>() {}.type
        return gson.fromJson(value, type)
    }

}