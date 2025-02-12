package com.example.avito.tech.avito_tech_winter_2025.repository

import com.example.avito.tech.avito_tech_winter_2025.service.api.AvitoApi
import com.example.avito.tech.avito_tech_winter_2025.ui.api_tracks.data.Result
import com.example.avito.tech.avito_tech_winter_2025.ui.api_tracks.data.Status
import javax.inject.Inject

class AvitoRepository @Inject constructor(private val avitoApi: AvitoApi) {

    suspend fun getTracks(): Result = try {
        val response = avitoApi.getTracks()
        if (response.code() == 200) {
            val tracks = response.body()!!.tracks.data
            Result(Status.OK, tracks = tracks)
        } else {
            val code = response.code().toString()
            val error = response.errorBody()?.string() ?: ""
            Result(Status.ERROR, error = "$code $error")
        }
    } catch (e: Exception) {
        Result(Status.ERROR, error = e.toString())
    }

    suspend fun getTracksQuery(query: String): Result = try {
        val response = avitoApi.getTracksQuery(query)
        if (response.code() == 200) {
            val tracks = response.body()!!.data!!
            Result(Status.OK, tracks = tracks)
        } else {
            val code = response.code().toString()
            val error = response.errorBody()?.string() ?: ""
            Result(Status.ERROR, error = "$code $error")
        }
    } catch (e: Exception) {
        Result(Status.ERROR, error = e.toString())
    }
}