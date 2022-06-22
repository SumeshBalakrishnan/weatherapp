package com.test.gad.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.gad.network.model.Main
import com.test.gad.network.model.WeatherListItem

@Entity
data class DatabaseUserListItem constructor(
    @PrimaryKey(autoGenerate = true)
    val id: Int
)
fun List<DatabaseUserListItem>.asDomainModel(): List<WeatherListItem> {
    return map {
        WeatherListItem(
        )
    }
}

