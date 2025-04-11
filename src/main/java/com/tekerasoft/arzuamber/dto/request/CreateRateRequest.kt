package com.tekerasoft.arzuamber.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class CreateRateRequest(
    @JsonProperty("userName")
    val userName: String,
    @JsonProperty("userId")
    val userId: String,
    @JsonProperty("rate")
    val rate: Double
)
