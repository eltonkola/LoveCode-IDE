/*
 * Licensed to the Light Team Software (Light Team) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The Light Team licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lightteam.modpeide.data.repository

import android.content.SharedPreferences
import android.os.Environment
import com.lightteam.modpeide.data.converter.DocumentConverter
import com.lightteam.modpeide.data.converter.FileConverter
import com.lightteam.modpeide.data.storage.database.AppDatabase
import com.lightteam.modpeide.data.utils.extensions.formatAsDate
import com.lightteam.modpeide.data.utils.extensions.formatAsSize
import com.lightteam.modpeide.data.utils.extensions.size
import com.lightteam.modpeide.domain.model.DocumentModel
import com.lightteam.modpeide.domain.model.FileModel
import com.lightteam.modpeide.domain.model.PropertiesModel
import com.lightteam.modpeide.domain.repository.FileRepository
import io.reactivex.Completable
import io.reactivex.Single
import java.io.BufferedReader
import java.io.File
import java.io.FileNotFoundException

class LocalFileRepository(
    private val database: AppDatabase,
    private val sharedPreferences: SharedPreferences
) : FileRepository {

    private val FOLDER_PATH = "FOLDER_PATH"

    private val defaultLocation: File = Environment.getExternalStorageDirectory().absoluteFile

    // region EXPLORER


    override fun getLastPath(): String {
        val sdcard = Environment.getExternalStorageDirectory().absolutePath
        return sharedPreferences.getString(FOLDER_PATH, sdcard) ?: sdcard
    }

    private fun updateLastBrowsedFolder(path: String){
        sharedPreferences.edit().putString(FOLDER_PATH, path).apply()
    }

    override fun defaultLocation(): FileModel {
        return FileConverter.toModel(defaultLocation)
    }

    override fun provideDirectory(parent: FileModel): Single<List<FileModel>> {
        if(parent.path != defaultLocation.path){
            updateLastBrowsedFolder(parent.path)
        }

        return Single.create { emitter ->
            val files = FileConverter.toFile(parent)
                .listFiles()
                .map(FileConverter::toModel)
                .toList()
            emitter.onSuccess(files)
        }
    }

    override fun createFile(fileModel: FileModel): Single<FileModel> {
        return Single.create { emitter ->
            val realFile: File = FileConverter.toFile(fileModel)
            if(!realFile.exists()) {
                if(fileModel.isFolder) {
                    realFile.mkdirs()
                } else {
                    realFile.createNewFile()
                }
            }
            val modelFile = FileConverter.toModel(realFile)
            emitter.onSuccess(modelFile)
        }
    }

    override fun deleteFile(fileModel: FileModel): Single<FileModel> {
        return Single.create { emitter ->
            val realFile: File = FileConverter.toFile(fileModel)
            if(realFile.exists()) {
                realFile.deleteRecursively()
            }
            val modelFile = FileConverter.toModel(realFile.parentFile)
            emitter.onSuccess(modelFile)
        }
    }

    override fun renameFile(fileModel: FileModel, fileName: String): Single<FileModel> {
        return Single.create { emitter ->
            val originalFile: File = FileConverter.toFile(fileModel)
            val renamedFile = File(originalFile.parentFile.absolutePath + "/$fileName")
            if(originalFile.exists()) {
                originalFile.renameTo(renamedFile)
            }
            val modelFile = FileConverter.toModel(renamedFile.parentFile)
            emitter.onSuccess(modelFile)
        }
    }

    override fun propertiesOf(fileModel: FileModel): Single<PropertiesModel> {
        return Single.create { emitter ->
            val realFile = File(fileModel.path)
            if(realFile.exists()) {
                val result = PropertiesModel(
                    fileModel.name,
                    fileModel.path,
                    fileModel.lastModified.formatAsDate(),
                    realFile.size().formatAsSize(),
                    getLineCount(realFile),
                    getWordCount(realFile),
                    getCharCount(realFile),
                    realFile.canRead(),
                    realFile.canWrite(),
                    realFile.canExecute()
                )
                emitter.onSuccess(result)
            } else {
                emitter.onError(FileNotFoundException())
            }
        }
    }

    // endregion EXPLORER

    // region EDITOR

    override fun loadFile(documentModel: DocumentModel): Single<String> {
        return Single.create { emitter ->
            database.documentDao().insert(DocumentConverter.toCache(documentModel)) // Save to Database

            // Load from Storage
            val file = File(documentModel.path)
            val text = if(file.exists()) {
                file.inputStream().bufferedReader().use(BufferedReader::readText)
            } else {
                emitter.onError(FileNotFoundException())
                String() //empty
            }
            emitter.onSuccess(text)
        }
    }

    override fun saveFile(documentModel: DocumentModel, text: String): Completable {
        return Completable.create { emitter ->
            database.documentDao().update(DocumentConverter.toCache(documentModel)) // Save to Database

            // Save to Storage
            val file = File(documentModel.path)
            if(!file.exists()) {
                file.createNewFile()
            }
            val writer = file.outputStream().bufferedWriter()
            writer.write(text)
            writer.close()

            emitter.onComplete()
        }
    }

    // endregion EDITOR

    // region PROPERTIES

    private fun getLineCount(file: File): String {
        if(file.isFile) {
            var lines = 0
            file.forEachLine {
                lines++
            }
            return lines.toString()
        }
        return "…"
    }

    private fun getWordCount(file: File): String {
        if(file.isFile) {
            var words = 0
            file.forEachLine {
                words += it.split(' ').size
            }
            return words.toString()
        }
        return "…"
    }

    private fun getCharCount(file: File): String {
        if(file.isFile) {
            return file.length().toString()
        }
        return "…"
    }

    // endregion PROPERTIES
}