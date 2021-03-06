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

package com.lightteam.modpeide.data.utils.extensions

import java.io.File
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

fun String.isValidFileName(): Boolean =
        !isEmpty() &&
                !contains("/") &&
                !equals(".") &&
                !equals("..")

fun String.endsWith(suffixes: Array<String>): Boolean {
        for(suffix in suffixes) {
                if(endsWith(suffix)) {
                        return true
                }
        }
        return false
}

fun File.size(): Long {
        if(isDirectory) {
                var length: Long = 0
                for (child in listFiles()) {
                        length += child.size()
                }
                return length
        }
        return length()
}

fun Long.formatAsDate(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy EEE HH:mm", Locale.getDefault())
        return dateFormat.format(this)
}

fun Long.formatAsSize(): String {
        if (this <= 0)
                return "0"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(this.toDouble()) / Math.log10(1024.0)).toInt()
        return (DecimalFormat("#,##0.#").format(this / Math.pow(1024.0, digitGroups.toDouble()))
                + " " + units[digitGroups])
}