package com.example.travelapp.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.travelapp.enums.EnumTravelType
import java.time.LocalDate
import java.util.Date

@Entity
data class Travel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val destination: String = "",
    val travelType: EnumTravelType,
    val startDate: Date = Date(),
    val endDate: Date = Date(),
    val budget: Double = 0.0
)