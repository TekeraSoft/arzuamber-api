package com.tekerasoft.arzuamber.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class EditUserRequest(
    @JsonProperty("email")
    val email: String,
    @JsonProperty("token")
    val token: String,
    @JsonProperty("phoneNumber")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val phoneNumber: String? = null,
    @JsonProperty("address")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val address: String? = null,
)
