package com.example.pw_78

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.transition.Visibility
import com.example.pw_78.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageView.visibility = View.GONE

        binding.loadImageButton.setOnClickListener {
            val inputUrl = binding.urlOfImage.text.toString()
            var bitmap: Bitmap? = null

            CoroutineScope(Dispatchers.IO).launch {
                bitmap = createBitmapFromURL(inputUrl)
            }

            CoroutineScope(Dispatchers.IO).launch {
                saveImage(bitmap)
            }
        }

        binding.onlyLoadImageButton.setOnClickListener {
            val inputUrl = binding.urlOfImage.text.toString()

            CoroutineScope(Dispatchers.IO).launch {
                createBitmapFromURL(inputUrl)
            }
        }

        binding.clearAllButton.setOnClickListener {
            binding.urlOfImage.text.clear()
            binding.imageView.visibility = View.GONE
        }
    }

    fun createBitmapFromURL(stringURL: String): Bitmap? {
        Log.i("Проверка потоков (Network)", "Первый поток начат")
        var bitmap: Bitmap? = null
        var stream: ByteArrayOutputStream? = null
        if (stringURL.isNotEmpty()) {
            try {
                val url = URL(stringURL)
                bitmap = BitmapFactory.decodeStream(url.openStream())
                stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

                checkForBitmap(bitmap, stream)
            } catch (e: Exception) {
                Log.e("Ошибка загрузки изображения из интернета", "${e.message}")
            }
        }
        checkForBitmap(bitmap, stream)
        Log.i("Проверка потоков (Network)", "Первый поток закончен")
        return bitmap
    }

    fun checkForBitmap(bitmap: Bitmap?, stream: ByteArrayOutputStream?) {
        if (stream != null) {
            if (bitmap != null && stream.toByteArray().isNotEmpty()) {
                runOnUiThread {
                    binding.imageView.visibility = View.VISIBLE
                    binding.imageView.setImageBitmap(bitmap)
                }
            } else {
                binding.imageView.visibility = View.GONE
            }
        } else {
            binding.imageView.visibility = View.GONE
        }
    }

    fun saveImage(bitmap: Bitmap?) {
        Log.i("Проверка потоков (Disk)", "Второй поток начат")
        if (bitmap != null) {
            try {
                val fileDir = getFilesDir()
                val fileName = "new_image.jpg"
                val file = File(fileDir, fileName)

                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()
            } catch (e: Exception) {
                Log.e("Ошибка записи в хранилище", "${e.message}")
            }
        }
        Log.i("Проверка потоков (Disk)", "Второй поток закончен")
    }
}