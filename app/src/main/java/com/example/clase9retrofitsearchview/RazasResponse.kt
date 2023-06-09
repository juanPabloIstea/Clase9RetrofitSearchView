package com.example.clase9retrofitsearchview

import com.google.gson.annotations.SerializedName

data class RazasResponse(
    @SerializedName("message") var imagenes: List<String>,
    @SerializedName("status") var estado: String
)