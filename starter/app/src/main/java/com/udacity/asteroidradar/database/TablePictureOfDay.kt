package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.domain.PictureOfDay

@Entity(tableName = "pic_of_day")
data class TablePictureOfDay(
    @PrimaryKey(autoGenerate = true)
        val id:         Long = 0L,
        val mediaType:  String,
        val title:      String,
        val url:        String
    )

fun TablePictureOfDay?.asDomainModel(): PictureOfDay? {
    return if(this != null) {
        PictureOfDay(
            mediaType   = this.mediaType,
            title       = this.title,
            url         = this.url
        )
    } else null
}

fun PictureOfDay.asDatabaseModel(): TablePictureOfDay {
    return TablePictureOfDay(
        mediaType   = this.url,
        title       = this.title,
        url         = this.url
    )
}