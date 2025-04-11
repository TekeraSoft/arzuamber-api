package com.tekerasoft.arzuamber.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.arzuamber.model.Rate
import java.util.UUID

data class RateDto(
    @JsonProperty("id")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: UUID?,
    @JsonProperty("userName")
    val userName: String,
    @JsonProperty("userId")
    val userId: String,
    @JsonProperty("rate")
    val rate: Double
) {
    companion object {
        @JvmStatic
        fun toDto(from: Rate): RateDto {
            return RateDto(
                from.id,
                from.userName,
                from.userId,
                from.rate
            )
        }
    }
}
