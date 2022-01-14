package com.waldemlav.converterapp.data.network.model

data class ExchangeDto(
    val motd: Motd,
    val success: Boolean,
    val query: Query,
    val info: Info,
    val historical: Boolean,
    val date: String,
    val result: Double
) {
    data class Motd(
        val msg: String,
        val url: String
    )

    data class Query(
        val from: String,
        val to: String,
        val amount: Double
    )

    data class Info(
        val rate: Double
    )
}