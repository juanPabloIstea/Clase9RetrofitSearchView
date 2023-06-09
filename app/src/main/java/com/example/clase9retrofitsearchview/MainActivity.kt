package com.example.clase9retrofitsearchview

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener  {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DogsAdapter
    private lateinit var searchView: SearchView
    private var listaImagenes = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerviewlista)
        searchView = findViewById(R.id.searchviewrecycler)

        searchView.setOnQueryTextListener(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DogsAdapter(listaImagenes)
        recyclerView.adapter = adapter
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL_DOGS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun searchBy(breed: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = getRetrofit().create(APIService::class.java).getListaImagenes("breed/$breed/images")
            val respuesta: RazasResponse? = call.body()

            runOnUiThread {
                if (call.isSuccessful) {
                    val imagenes = (respuesta?.imagenes ?: emptyList())
                    listaImagenes.clear()
                    listaImagenes.addAll(imagenes)
                    adapter.notifyDataSetChanged()
                } else {
                    onError()
                }
                hideKeyBoard()
            }
        }
    }

    private fun onError() {
        Toast.makeText(this, "Ocurrio un error", Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyBoard() {
        val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var view: View? = this.currentFocus
        if (view == null) {
            view = View(this);
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0);
    }

    companion object {
        const val URL_DOGS = "https://dog.ceo/api/"
    }

    override fun onQueryTextSubmit(breedQuery: String?): Boolean {
        if(!breedQuery.isNullOrEmpty()) {
            searchBy(breedQuery.lowercase())
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }
}