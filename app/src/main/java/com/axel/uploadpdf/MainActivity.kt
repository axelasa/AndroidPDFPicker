package com.axel.uploadpdf

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.TextView
import com.axel.uploadpdf.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    private lateinit var pdfUri: Uri
    lateinit var pdfFile: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pdfFile = binding.selectedPdf
        pdfFile.setOnClickListener {
            selectPDF()
        }
    }
// Intent for opening files
    private fun selectPDF(){
        val pdfIntent = Intent(Intent.ACTION_GET_CONTENT)
        pdfIntent.type = "application/pdf"
        pdfIntent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(pdfIntent,12)
    }
    //After implementing the intents, you will need the override the onActivityResult method as follows:
    @SuppressLint("Range")
    override fun onActivityResult(requestCode: Int, resultCode: Int,data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            12 -> if(resultCode == Activity.RESULT_OK){
                pdfUri = data?.data!!
                val uri:Uri = data?.data!!
                val uriString: String = uri.toString()
                var pdfName:String? = null
                if (uriString.startsWith("content://")){
                    var myCursor:Cursor? = null
                    try {
                        myCursor = applicationContext!!.contentResolver.query(uri,null,null,null,null,)
                        if(myCursor != null && myCursor.moveToFirst()){
                            pdfName = myCursor.getString(myCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                            pdfFile.text = pdfName

                        }
                    }catch(e:Exception){
                        print("an exception occurred")
                    }finally {
                        myCursor?.close()
                    }
                }
            }
        }
    }
}