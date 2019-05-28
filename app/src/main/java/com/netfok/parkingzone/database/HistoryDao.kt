package com.netfok.parkingzone.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.netfok.parkingzone.model.History

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg history: History)

    @Query("DELETE FROM History")
    fun deleteAll()

    @Query("SELECT * FROM History")
    fun getAll(): LiveData<List<History>>

    @Query("DELETE FROM History WHERE id=:historyId")
    fun delete(historyId: Int)
}