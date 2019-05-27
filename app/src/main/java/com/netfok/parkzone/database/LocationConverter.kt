package com.netfok.parkzone.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.netfok.parkzone.model.Location

class LocationConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun toLocation(json: String): List<Location> = Gson().fromJson(json, object : TypeToken<List<Location>>(){}.type)

        @TypeConverter
        @JvmStatic
        fun toJson(locations: List<Location>): String = Gson().toJson(locations)
    }
}