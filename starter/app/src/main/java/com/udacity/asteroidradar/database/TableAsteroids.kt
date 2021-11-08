package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.Asteroid

@Entity(tableName = "asteroids")
data class TableAsteroids constructor(
    @PrimaryKey
    val id                      : Long,
    val codename                : String,
    val closeApproachDate       : String,
    val absoluteMagnitude       : Double,
    val estimatedDiameter       : Double,
    val relativeVelocity        : Double,
    val distanceFromEarth       : Double,
    val isPotentiallyHazardous  : Boolean
    )

fun List<TableAsteroids>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id                      = it.id,
            codename                = it.codename,
            closeApproachDate       = it.closeApproachDate,
            absoluteMagnitude       = it.absoluteMagnitude,
            estimatedDiameter       = it.estimatedDiameter,
            relativeVelocity        = it.relativeVelocity,
            distanceFromEarth       = it.distanceFromEarth,
            isPotentiallyHazardous  = it.isPotentiallyHazardous
        )
    }
}

fun List<Asteroid>.asDatabaseModel(): List<TableAsteroids> {
    return map {
        TableAsteroids(
            id                      = it.id,
            codename                = it.codename,
            closeApproachDate       = it.closeApproachDate,
            absoluteMagnitude       = it.absoluteMagnitude,
            estimatedDiameter       = it.estimatedDiameter,
            relativeVelocity        = it.relativeVelocity,
            distanceFromEarth       = it.distanceFromEarth,
            isPotentiallyHazardous  = it.isPotentiallyHazardous
        )
    }
}


