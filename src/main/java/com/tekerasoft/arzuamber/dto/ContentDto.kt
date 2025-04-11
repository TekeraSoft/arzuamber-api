package com.tekerasoft.arzuamber.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class ContentDto(
    @JsonProperty("userName")
    val userName: String,
    @JsonProperty("message")
    val message: String,
    @JsonProperty("createdAt")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val createdAt: LocalDateTime?
)
