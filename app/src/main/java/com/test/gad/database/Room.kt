package com.test.gad.database

import androidx.room.*
import com.test.gad.network.model.WeatherResponse


@Dao
interface UsersDao {

   /* @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: String?)*/
}

@Database(entities = [DatabaseUserListItem::class], version = 1)
abstract class UsersDatabase : RoomDatabase() {
    abstract val usersDao: UsersDao
}
