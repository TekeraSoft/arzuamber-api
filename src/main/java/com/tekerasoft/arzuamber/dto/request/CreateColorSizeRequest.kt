package com.tekerasoft.arzuamber.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.multipart.MultipartFile


data class CreateColorSizeRequest(
    @JsonProperty("color")
    val color: String,
    @JsonProperty("sizeStock")
    val sizeStock: List<SizeStockRequest>
)
