package com.example.avito.tech.avito_tech_winter_2025.service.api

import com.example.avito.tech.avito_tech_winter_2025.service.dto.model.Data
import com.example.avito.tech.avito_tech_winter_2025.service.dto.model.Track
import com.example.avito.tech.avito_tech_winter_2025.service.dto.response.TracksResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AvitoApi {
    @GET("chart")
    suspend fun getTracks(): Response<TracksResponse>

    @GET("search")
    suspend fun getTracksQuery(@Query("q") query: String): Response<Data>
}