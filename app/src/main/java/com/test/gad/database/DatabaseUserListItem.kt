package com.test.gad.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.test.gad.network.model.Main
import com.test.gad.network.model.WeatherListItem

@Entity
data class DatabaseUserListItem constructor(
    val city: String?,
    val humidity: Double?,
    val tempMax: Double?,
    val tempMin: Double?,
){
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id: Int? = null
}
/*fun List<DatabaseUserListItem>.asDomainModel(): List<WeatherListItem> {
    return map {
        WeatherListItem(

        )
    }
}*/

