package com.tekerasoft.arzuamber.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class CompleteThreedsRequest (
    @JsonProperty("conversationId")
    val conversationId: String,
    @JsonProperty("paymentId")
    val paymentId: String
)