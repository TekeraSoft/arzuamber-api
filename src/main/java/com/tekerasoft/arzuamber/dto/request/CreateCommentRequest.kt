package com.tekerasoft.arzuamber.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.arzuamber.dto.ContentDto

data class CreateCommentRequest(
    @JsonProperty("content")
    val content: ContentDto,
    @JsonProperty("rate")
    val rate: CreateRateRequest
)
