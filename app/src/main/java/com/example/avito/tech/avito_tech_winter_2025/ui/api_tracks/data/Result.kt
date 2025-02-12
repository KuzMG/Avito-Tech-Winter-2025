package com.example.avito.tech.avito_tech_winter_2025.ui.api_tracks.data

import com.example.avito.tech.avito_tech_winter_2025.service.dto.model.Track

data class Result(
    val status: Status,
    val tracks: List<Track>? = null,
    val error: String? = null,
)
