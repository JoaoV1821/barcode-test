package com.pessoal

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private val productList = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configuração do RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProductAdapter(productList)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.button).setOnClickListener {
            startBarcodeScanner()
        }
    }

    private val barcodeLauncher = registerForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            Toast.makeText(this, "Código: ${result.contents}", Toast.LENGTH_SHORT).show()
            fetchProductData(result.contents)
        }
    }

    private fun startBarcodeScanner() {
        barcodeLauncher.launch(ScanOptions().apply {
            setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
            setPrompt("Escaneie um código")
            setCameraId(0)
            setBeepEnabled(false)
            setBarcodeImageEnabled(true)
        })
    }

    private fun fetchProductData(barcode: String) {
        val api = RetrofitClient.instanceUPC

        val call = api.getProduct(barcode)
        call.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(call: Call<ProductResponse>, response: Response<ProductResponse>) {
                if (response.isSuccessful) {
                    val results = response.body()?.items

                    results?.let {
                        productList.clear()
                        productList.addAll(it)
                        println(it)
                        adapter.notifyDataSetChanged()

                        // Enviar produto para FastAPI após exibir na lista
                        sendProductToFastAPI(it.firstOrNull())
                    }

                } else {
                    Toast.makeText(applicationContext, "Produto não encontrado!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Log.e("Erro", "Falha ao buscar produto", t)
                Toast.makeText(applicationContext, "Erro ao buscar o produto!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Envio do produto para o FastAPI
    private fun sendProductToFastAPI(product: Product?) {
        if (product == null) return

        val api = RetrofitClient.instanceFastAPI

        // Chama o método createProduct para enviar o produto
        val call = api.createProduct(product)

        call.enqueue(object : Callback<Product> {
            override fun onResponse(call: Call<Product>, response: Response<Product>) {
                if (response.isSuccessful) {
                    Toast.makeText(applicationContext, "Produto enviado com sucesso!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "Falha ao enviar produto!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Product>, t: Throwable) {
                Log.e("Erro", "Falha ao enviar produto", t)

            }
        })
    }
}

