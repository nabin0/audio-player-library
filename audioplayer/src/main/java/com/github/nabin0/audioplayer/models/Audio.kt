package com.github.nabin0.audioplayer.models

import android.net.Uri

/**
 * A Data class to represent audio object for demo will be changed later
 */
data class Audio(
    val id: Long,
    val audioUri: Uri,
    val displayName: String,
    val albumArtUrl: String?,
    val artist: String,
    val title: String,
    val data: String,
    val duration: Int,
)