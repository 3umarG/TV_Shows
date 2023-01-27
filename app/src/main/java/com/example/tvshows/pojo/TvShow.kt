package com.example.tvshows.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.RawValue
import java.io.Serializable

@Entity(tableName = "tvShows")
data class TvShow(
    @PrimaryKey
    val id: Int?,

    val country: String?,
    val end_date: String?,
    val image_thumbnail_path: String?,
    val name: String?,
    val network: String?,
    val permalink: String?,
    val start_date: String?,
    val status: String?
) : Serializable {
    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TvShow

        if (id != other.id) return false
        if (country != other.country) return false
        if (end_date != other.end_date) return false
        if (image_thumbnail_path != other.image_thumbnail_path) return false
        if (name != other.name) return false
        if (network != other.network) return false
        if (permalink != other.permalink) return false
        if (start_date != other.start_date) return false
        if (status != other.status) return false

        return true
    }
}