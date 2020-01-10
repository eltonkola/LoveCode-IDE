package com.lightteam.modpeide.utils

import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.zeroturnaround.zip.ZipUtil
import java.io.*
import java.lang.Exception

class ZipperFile {

    fun zipAll(directory: String, zipFile: String): Completable {
        return Completable.create { emitter ->
            try{
                ZipUtil.pack(File(directory), File(zipFile))
                emitter.onComplete()
            }catch (e: Exception){
                e.printStackTrace()
                emitter.onError(e)
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

}