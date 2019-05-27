package com.netfok.parkzone.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.netfok.parkzone.model.History
import com.netfok.parkzone.model.Location
import com.netfok.parkzone.model.ParkingZone

@Database(version = 1, entities = [ParkingZone::class, History::class, Location::class])
@TypeConverters(LocationConverter::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun historyDao(): HistoryDao
}