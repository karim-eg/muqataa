/*
 * Copyright (c) 2023-2023. Encept Ltd Company, https://encept.co
 * contact: support@encept.co
 */

package co.encept.muqataa.ui.models

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class ValidateBarcodeModel(val app: Application) :  AndroidViewModel(app) {
    private val forbiddenList = mutableListOf<String>()

    fun initData(c: Context) {
        val offlineData = try {
            val inputStream = c.assets.open("forbiddenList.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            String(buffer, Charsets.UTF_8)

        } catch (_: Exception) { "error" }

        if (offlineData == "error") return

        val jsonObject = JSONObject(offlineData)
        val forbiddenArray = jsonObject.getJSONArray("forbidden")

        forbiddenList.clear()
        for (i in 0 until forbiddenArray.length()) {
            val element = forbiddenArray.getString(i)
            forbiddenList.add(element)
        }

        loadForbiddenList()
    }


    fun checkMuqataa(barcode: String): Boolean {
        if (barcode == "error") return false

        return forbiddenList.any {
            barcode.contains(it, true) || barcode.startsWith(it, true)
        }
    }


    private fun loadForbiddenList() {
        val uri = "https://kimoandroid.github.io/muqataa-api/banned.json"
        OkHttpClient().newCall(Request.Builder().url(uri).build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) { }

            override fun onResponse(call: Call, response: Response) {
                val resBody = response.body?.string()?.trim() ?: "error"
                response.close()

                try {
                    val jsObj = JSONObject(resBody)

                    when(jsObj.getString("status")) {
                        "success" -> {
                            val jsonObject = JSONObject(resBody)
                            val forbiddenArray = jsonObject.getJSONArray("forbidden")

                            forbiddenList.clear()
                            for (i in 0 until forbiddenArray.length()) {
                                val element = forbiddenArray.getString(i)
                                forbiddenList.add(element)
                            }
                        }
                    }
                } catch (_: Exception) { }
            }
        })
    }
}