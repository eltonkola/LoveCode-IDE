package com.eltonkola.lovedocs

import android.content.Context
import com.google.gson.Gson

class DocLoader {

    fun getDocs(context : Context) : LoveDocs {
        val file = context.assets.open("docs.json")
        val content = file.bufferedReader().use { it.readText() }
        return Gson().fromJson(content, LoveDocs::class.java)
    }

}