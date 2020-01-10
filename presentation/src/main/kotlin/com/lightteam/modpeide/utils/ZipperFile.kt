package com.lightteam.modpeide.utils

import android.util.Log
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.zeroturnaround.zip.ZipUtil
import java.io.*
import java.lang.Exception
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ZipperFile {

    fun zipAll(directory: String, zipFile: String): Completable {
        return Completable.create { emitter ->
            try{
                ZipUtil.pack(File(directory), File(zipFile))
                //doZipAll(directory, zipFile)
                emitter.onComplete()
            }catch (e: Exception){
                e.printStackTrace()
                emitter.onError(e)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    private fun doZipAll(directory: String, zipFile: String) {
        val sourceFile = File(directory)

        ZipOutputStream(BufferedOutputStream(FileOutputStream(zipFile))).use {
            it.use {
                zipFiles(it, sourceFile, "")
            }
        }
    }

    private fun zipFiles(zipOut: ZipOutputStream, sourceFile: File, parentDirPath: String) {

        val data = ByteArray(2048)

        for (f in sourceFile.listFiles()) {

            if (f.isDirectory) {
                val entry = ZipEntry(f.name + File.separator)
                entry.time = f.lastModified()
                entry.isDirectory
                entry.size = f.length()

                Log.i("zip", "Adding Directory: " + f.name)
                zipOut.putNextEntry(entry)

                //Call recursively to add files within this directory
                zipFiles(zipOut, f, f.name)
            } else {

                if (!f.name.contains(".zip")) { //If folder contains a file with extension ".zip", skip it
                    FileInputStream(f).use { fi ->
                        BufferedInputStream(fi).use { origin ->
                            val path = parentDirPath + File.separator + f.name
                            Log.i("zip", "Adding file: $path")
                            val entry = ZipEntry(path)
                            entry.time = f.lastModified()
                            entry.isDirectory
                            entry.size = f.length()
                            zipOut.putNextEntry(entry)
                            while (true) {
                                val readBytes = origin.read(data)
                                if (readBytes == -1) {
                                    break
                                }
                                zipOut.write(data, 0, readBytes)
                            }
                        }
                    }
                } else {
                    zipOut.closeEntry()
                    zipOut.close()
                }
            }
        }
    }

}