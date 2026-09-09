package com.walhalla.ttvloader.utils

import android.util.Log
import com.walhalla.ui.DLog
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.StatusLine
import org.apache.http.client.ClientProtocolException
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

class JSONParser {
    constructor() {
    }
    fun getJSONFromUrl(url: String): JSONArray? {
        var builder: StringBuilder = StringBuilder()
        var client: HttpClient = DefaultHttpClient()
        var httpGet: HttpGet = HttpGet(url)
        try {
            var response: HttpResponse = client.execute(httpGet)
            var statusLine: StatusLine = response.getStatusLine()
            var statusCode: Int = statusLine.getStatusCode()
            if (statusCode == 200) {
                var entity: HttpEntity = response.getEntity()
                var content: InputStream = entity.getContent()
                var reader: BufferedReader = BufferedReader(InputStreamReader(content))
                var line: String? = null
                line = reader.readLine()
                while (line != null) {
                    builder.append(line)
                    line = reader.readLine()
                }
            } else {
                Log.e("==>", "Failed to download file")
            }
        } catch (e: Exception) {
            DLog.handleException(e)
        } catch (e: Exception) {
            DLog.handleException(e)
        }
        // Parse String to JSON object
        try {
            jarray = JSONArray(builder.toString())
        } catch (e: Exception) {
            Log.e("JSON Parser", "Error parsing data " + e.toString())
        }
        // return JSON Object
        return jarray
    }
    fun getOJSONFromUrl(url: String): JSONObject? {
        var builder: StringBuilder = StringBuilder()
        var client: HttpClient = DefaultHttpClient()
        var httpGet: HttpGet = HttpGet(url)
        try {
            var response: HttpResponse = client.execute(httpGet)
            var statusLine: StatusLine = response.getStatusLine()
            var statusCode: Int = statusLine.getStatusCode()
            if (statusCode == 200) {
                var entity: HttpEntity = response.getEntity()
                var content: InputStream = entity.getContent()
                var reader: BufferedReader = BufferedReader(InputStreamReader(content))
                var line: String? = null
                line = reader.readLine()
                while (line != null) {
                    builder.append(line)
                    line = reader.readLine()
                }
            } else {
                Log.e("==>", "Failed to download file")
            }
        } catch (e: Exception) {
            DLog.handleException(e)
        } catch (e: Exception) {
            DLog.handleException(e)
        }
        // Parse String to JSON object
        try {
            jarrayo = JSONObject(builder.toString())
        } catch (e: Exception) {
            Log.e("JSON Parser", "Error parsing data " + e.toString())
        }
        // return JSON Object
        return jarrayo
    }
    fun getSJSONFromUrl(url: String): String? {
        var builder: StringBuilder = StringBuilder()
        var client: HttpClient = DefaultHttpClient()
        var httpGet: HttpGet = HttpGet(url)
        httpGet.addHeader("header-name", "header-value")
        try {
            var response: HttpResponse = client.execute(httpGet)
            var statusLine: StatusLine = response.getStatusLine()
            var statusCode: Int = statusLine.getStatusCode()
            if (statusCode == 200) {
                var entity: HttpEntity = response.getEntity()
                var content: InputStream = entity.getContent()
                var reader: BufferedReader = BufferedReader(InputStreamReader(content))
                var line: String? = null
                line = reader.readLine()
                while (line != null) {
                    builder.append(line)
                    line = reader.readLine()
                }
            } else {
                Log.e("==>", "Failed to download file")
            }
        } catch (e: Exception) {
            DLog.handleException(e)
        } catch (e: Exception) {
            DLog.handleException(e)
        }
        jarrayq = builder.toString()
        // return String
        return jarrayq
    }
    companion object {
        @JvmField var iStream: InputStream? = null
        @JvmField var jarray: JSONArray? = null
        @JvmField var jarrayq: String? = null
        @JvmField var jarrayo: JSONObject? = null
        @JvmField var json: String = ""
    }
}
