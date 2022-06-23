package com.test.gad.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.test.gad.network.model.WeatherResponse

@Dao
interface UsersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message : DatabaseUserListItem)

    @Query("SELECT * FROM DatabaseUserListItem")
    fun getAllWeather(): List<DatabaseUserListItem>

    @Query("DELETE FROM DatabaseUserListItem")
    suspend fun clearDatabaseUserListItem()
}

@Database(entities = [DatabaseUserListItem::class], version = 4)
abstract class UsersDatabase : RoomDatabase() {
    abstract val usersDao: UsersDao
}
