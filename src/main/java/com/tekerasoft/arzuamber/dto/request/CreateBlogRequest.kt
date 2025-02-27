package com.tekerasoft.arzuamber.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateBlogRequest(
    @JsonProperty("title")
    val title: String,
    @JsonProperty("category")
    val category: String,
    @JsonProperty("content")
    val content: String
)
