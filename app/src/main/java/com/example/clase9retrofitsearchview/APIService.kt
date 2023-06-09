package com.example.clase9retrofitsearchview

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APIService {
    @GET
    suspend fun getListaImagenes(@Url url: String): Response<RazasResponse>
}
