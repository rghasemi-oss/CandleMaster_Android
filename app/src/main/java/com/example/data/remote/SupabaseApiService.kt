package com.example.data.remote

import com.example.data.DeliveryEntity
import retrofit2.Response
import retrofit2.http.*

interface SupabaseApiService {
    @GET("rest/v1/deliveries")
    suspend fun getDeliveries(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String
    ): Response<List<DeliveryEntity>>

    @POST("rest/v1/deliveries")
    suspend fun insertDelivery(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Header("Prefer") prefer: String = "return=representation",
        @Body delivery: DeliveryEntity
    ): Response<Unit>

    @PATCH("rest/v1/deliveries")
    suspend fun updateDelivery(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Query("trackingId") query: String, // e.g. "eq.IR-2025-4419"
        @Body delivery: DeliveryEntity
    ): Response<Unit>

    @DELETE("rest/v1/deliveries")
    suspend fun deleteDelivery(
        @Header("apikey") apiKey: String,
        @Header("Authorization") authHeader: String,
        @Query("trackingId") query: String // e.g. "eq.IR-2025-4419"
    ): Response<Unit>
}
