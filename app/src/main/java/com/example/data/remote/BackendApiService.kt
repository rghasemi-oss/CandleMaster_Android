package com.example.data.remote

import retrofit2.Response
import retrofit2.http.*

interface BackendApiService {

    @GET("api/v1/deliveries")
    suspend fun getDeliveries(
        @Header("Authorization") authHeader: String
    ): Response<BackendCollectionResponse<BackendDeliveryDto>>

    @POST("api/v1/deliveries")
    suspend fun insertDelivery(
        @Header("Authorization") authHeader: String,
        @Body delivery: BackendDeliveryDto
    ): Response<BackendDataResponse<BackendDeliveryDto>>

    @DELETE("api/v1/deliveries/{trackingId}")
    suspend fun deleteDelivery(
        @Header("Authorization") authHeader: String,
        @Path("trackingId") trackingId: String
    ): Response<BackendTrackingResponse>

    @GET("api/v1/hubs")
    suspend fun getHubs(
        @Header("Authorization") authHeader: String
    ): Response<BackendCollectionResponse<Map<String, Any?>>>

    @GET("api/v1/health")
    suspend fun health(): Response<BackendHealthResponse>
}

data class BackendCollectionResponse<T>(
    val success: Boolean,
    val data: List<T>?
)

data class BackendDataResponse<T>(
    val success: Boolean,
    val data: T?
)

data class BackendTrackingResponse(
    val success: Boolean,
    val trackingId: String?
)

data class BackendHealthResponse(
    val success: Boolean,
    val service: String?,
    val version: String?,
    val status: String?,
    val timestamp: String?
)
