package com.padicare.ui.result

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.padicare.R
import com.padicare.databinding.ActivityResultBinding
import com.padicare.model.ResultScan
import com.padicare.ui.addPost.AddPostActivity
import java.io.ByteArrayOutputStream

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupData()

        binding.resultImage.setOnClickListener {
            if(binding.resultImage.scaleType == ImageView.ScaleType.FIT_CENTER) {
                binding.resultImage.scaleType = ImageView.ScaleType.CENTER
            } else {
                binding.resultImage.scaleType = ImageView.ScaleType.FIT_CENTER
            }
        }
        binding.backButton.setOnClickListener {
            finish()
        }

        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))

        binding.btnShare.imageTintList = colorStateList

        binding.btnShare.setOnClickListener {
            val intent = Intent(this, AddPostActivity::class.java)
            val drawable = binding.resultImage.drawable
            if(drawable is BitmapDrawable) {
                val bitmapDrawable: BitmapDrawable = drawable
                val bitmap: Bitmap = bitmapDrawable.bitmap
                val imageUri: String = getImageUriFromBitmap(bitmap)
                intent.putExtra("image", imageUri)
                intent.putExtra("title", binding.resultTitle.text)
                startActivity(intent)
            }
        }
    }

    private fun setupData() {
        val result = intent.getParcelableExtra<ResultScan>("result")
        val image = intent.getStringExtra("imageUri")
        val imageUri = Uri.parse(image)
        binding.resultImage.setImageURI(imageUri)

        result?.let {
            binding.resultTitle.text = result.name
            binding.resultDefinisi.text= result.desc
            binding.resultSolusi.text = result.solution
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "Title", null)
        return Uri.parse(path).toString()
    }

}