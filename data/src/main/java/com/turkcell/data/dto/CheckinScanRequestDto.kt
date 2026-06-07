package com.turkcell.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckinScanRequestDto(
    val qrCode : String
) {
}