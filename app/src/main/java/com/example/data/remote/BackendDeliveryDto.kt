package com.example.data.remote

import com.example.data.DeliveryEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class BackendDeliveryDto(
    val trackingId: String,
    val sender: String,
    val customer: String,
    val hubName: String?,
    val status: String,
    val weight: String?,
    val timestamp: String?
)

object BackendDeliveryMapper {

    private const val ISO_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

    private fun isoFormatter(): SimpleDateFormat =
        SimpleDateFormat(ISO_PATTERN, Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

    fun fromBackend(dto: BackendDeliveryDto): DeliveryEntity {
        val timestampMillis = dto.timestamp
            ?.let { runCatching { isoFormatter().parse(it)?.time }.getOrNull() }
            ?: System.currentTimeMillis()

        return DeliveryEntity(
            trackingId = dto.trackingId,
            sender = dto.sender,
            customer = dto.customer,
            hubName = dto.hubName.orEmpty(),
            status = dto.status,
            weight = dto.weight.orEmpty(),
            timestamp = timestampMillis
        )
    }

    fun toBackend(entity: DeliveryEntity): BackendDeliveryDto {
        return BackendDeliveryDto(
            trackingId = entity.trackingId,
            sender = entity.sender,
            customer = entity.customer,
            hubName = entity.hubName.ifBlank { null },
            status = entity.status,
            weight = entity.weight.ifBlank { null },
            timestamp = isoFormatter().format(Date(entity.timestamp))
        )
    }
}
