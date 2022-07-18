package dev.davlatov.wallpapers.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.davlatov.wallpapers.models.RoomModel.UserModel

//for room
@Dao
interface DaoClass {
    //put data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllData(model: UserModel?)

    //get All Data
    @Query("select * from  user")
    fun getAllData(): List<UserModel?>?

    //DELETE DATA
    @Query("delete from user where `id`= :id")
    fun deleteData(id: Int)

    //Update Data
    @Query("update user SET smallLink= :smallLink ,bigLink =:bigLink, date =:date where `id`= :id")
    fun updateData(smallLink: String?, bigLink: String?, date: String?, id: Int)
}