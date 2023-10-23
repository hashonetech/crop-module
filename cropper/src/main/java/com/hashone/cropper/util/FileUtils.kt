package com.hashone.cropper.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Build
import android.util.Log
import java.io.*
import java.lang.String.format
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.*
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlin.io.path.extension
import kotlin.io.path.name
import kotlin.io.path.nameWithoutExtension


object FileUtils {

    //TODO: Internal Dirs
    const val INTERNAL_DIR_CONTENT = "contents"
    const val INTERNAL_DIR_FONT = "fonts"

    //TODO: Save Project Preview Name
    const val NAME_BLANK_CANVAS = "blnk"
    const val HTTP = "http"
    const val KEY_PACKAGE = "package"
    const val NAME_TEMPLATE_CANVAS = "temp"
    const val PREVIEW_NAME = "preview"
    const val EXTENSION_JPG = "jpg"
    const val EXTENSION_JPEG = "jpeg"
    const val EXTENSION_JPEG_2 = "JPEG"
    const val EXTENSION_PNG = "png"
    const val EXTENSION_WEBP = "webp"
    const val EXTENSION_JSON = "json"
    const val EXTENSION_IMG_JPEG = "image/jpeg"
    const val EXTENSION_IMG_PNG = "image/png"
    const val EXTENSION_IMG_JPG = "image/jpg"
    val File.size get() = if (!exists()) 0.0 else length().toDouble()
    private val File.sizeInKb get() = size / 1024
    val File.sizeInMb get() = sizeInKb / 1024


    fun getPreviewDir(context: Context): File {
        val contextWrapper = ContextWrapper(context)
        val rootDir = contextWrapper.getDir(context.filesDir.name, Context.MODE_PRIVATE)
        val imageDir = File(rootDir.absolutePath, "previews")
        if (!imageDir.exists()) {
            imageDir.mkdirs()
            imageDir.mkdir()
            val gpxfile = File(imageDir, ".nomedia")
            val writer = FileWriter(gpxfile)
            writer.flush()
            writer.close()
        }
        return imageDir
    }



    fun isFileExit(context: Context, templateName: String): Boolean {
        try {
            val filePath = File(getImageDir(context), templateName)
            return filePath.exists()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }


    fun getFontFileFromJson(activity: Activity, templateName: String, fileName: String): String {
        try {
            return when {
                File("${getImageDir(activity).absolutePath}/$templateName/$fileName.ttf").exists() -> "${
                    getImageDir(
                        activity
                    ).absolutePath
                }/$templateName/$fileName.ttf"
                File("${getImageDir(activity).absolutePath}/$templateName/$fileName.ttc").exists() -> "${
                    getImageDir(
                        activity
                    ).absolutePath
                }/$templateName/$fileName.ttc"

                File("${getImageDir(activity).absolutePath}/$templateName/$fileName.otf").exists() -> "${
                    getImageDir(
                        activity
                    ).absolutePath
                }/$templateName/$fileName.otf"

                File("${getImageDir(activity).absolutePath}/$templateName/$fileName.TTF").exists() -> "${
                    getImageDir(
                        activity
                    ).absolutePath
                }/$templateName/$fileName.TTF"

                File("${getImageDir(activity).absolutePath}/$templateName/$fileName.TTC").exists() -> "${
                    getImageDir(
                        activity
                    ).absolutePath
                }/$templateName/$fileName.TTC"

                File("${getImageDir(activity).absolutePath}/$templateName/$fileName.OTF").exists() -> "${
                    getImageDir(
                        activity
                    ).absolutePath
                }/$templateName/$fileName.OTF"

                else -> ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun getFontFile(activity: Context, fileName: String): String {
        try {
            return when {
                File("${getFontDir(activity).absolutePath}/$fileName.ttf").exists() -> "${
                    getFontDir(
                        activity
                    ).absolutePath
                }/$fileName.ttf"
                File("${getFontDir(activity).absolutePath}/$fileName.ttc").exists() -> "${
                    getFontDir(
                        activity
                    ).absolutePath
                }/$fileName.ttc"
                File("${getFontDir(activity).absolutePath}/$fileName.otf").exists() -> "${
                    getFontDir(
                        activity
                    ).absolutePath
                }/$fileName.otf"

                File("${getFontDir(activity).absolutePath}/$fileName.TTF").exists() -> "${
                    getFontDir(
                        activity
                    ).absolutePath
                }/$fileName.TTF"

                File("${getFontDir(activity).absolutePath}/$fileName.TTC").exists() -> "${
                    getFontDir(
                        activity
                    ).absolutePath
                }/$fileName.TTC"

                File("${getFontDir(activity).absolutePath}/$fileName.OTF").exists() -> "${
                    getFontDir(
                        activity
                    ).absolutePath
                }/$fileName.PTF"

                else -> ""
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }




    fun getFontFromDir(dir: String, fileName: String): String {
        return when {
            File("${dir}/$fileName.ttf").exists() -> "${
                dir
            }/$fileName.ttf"

            File("${dir}/$fileName.ttc").exists() -> "${
                dir
            }/$fileName.ttc"

            File("${dir}/$fileName.otf").exists() -> "${
                dir
            }/$fileName.otf"

            File("${dir}/$fileName.TTF").exists() -> "${
                dir
            }/$fileName.TTF"

            File("${dir}/$fileName.TTC").exists() -> "${
                dir
            }/$fileName.TTC"

            File("${dir}/$fileName.OTF").exists() -> "${
                dir
            }/$fileName.OTF"

            else -> ""
        }
    }

    fun getJson(activity: Activity, templateId: String): String {
        val jsonFile =
            File(getImageDir(activity), "$templateId/$templateId.json")
        var jsonStr: String = ""
        if (!jsonFile.exists()) {
            return ""
        }
        try {
            val stream = FileInputStream(jsonFile)
            try {
                val fc = stream.channel
                val bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size())

                jsonStr = Charset.defaultCharset().decode(bb).toString()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                stream.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return jsonStr
    }



    fun deleteDirectory(fileOrDirectory: File) {
        try {
            if (fileOrDirectory.isDirectory)
                for (element in fileOrDirectory.listFiles()!!) {
                    deleteDirectory(element)
                }
            fileOrDirectory.delete()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun copyFonts(context: Context, fontDirPath: String) {
        try {
            val assetManager = context.assets
            var files: Array<String>? = null
            try {
                files = assetManager.list("fonts")
            } catch (e: IOException) {

            }

            val fontDir = File(fontDirPath)
            if (!fontDir.exists()) {
                fontDir.mkdirs()
                fontDir.mkdir()
            }

            for (i in files!!.indices) {
                var `in`: InputStream? = null
                var out: OutputStream? = null
                try {
                    `in` = assetManager.open("fonts/" + files[i])
                    val outFile = File(fontDir, files[i])
                    out = FileOutputStream(outFile)
                    copyFile(`in`, out)

                    `in`.close()
                    `in` = null
                    out.flush()
                    out.close()
                    out = null
                } catch (e: Exception) {

                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun copyZip(context: Context, fileName: String) {
        try {
            val assetManager = context.assets
            var `in`: InputStream? = null
            var out: OutputStream? = null
            try {
                `in` = assetManager.open(fileName)
                val contextWrapper = ContextWrapper(context)
                val rootDir = contextWrapper.getDir(context.filesDir.name, Context.MODE_PRIVATE)
                val outFile = File(rootDir, fileName)
                if (!outFile.exists()) {
                    out = FileOutputStream(outFile)
                    copyFile(`in`, out)

                    `in`.close()
                    `in` = null
                    out.flush()
                    out.close()
                    out = null
                } else {
                    `in`.close()
                    `in` = null
                }
            } catch (e: Exception) {

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun unZipLogic(context: Context, fileName: String) {
        try {
            val contextWrapper = ContextWrapper(context)
            val rootDir = contextWrapper.getDir(context.filesDir.name, Context.MODE_PRIVATE)
            val filePath = "$rootDir/$fileName"
            val destinationPath = rootDir.absolutePath
            val archive = File(filePath)
            try {
                val zipfile = ZipFile(archive)
                val e = zipfile.entries() as Enumeration<ZipEntry>
                while (e.hasMoreElements()) {
                    val entry = e.nextElement()
                    unzipEntry(zipfile, entry, destinationPath)
                }
                unZipFile(filePath, destinationPath)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun unzipEntry(zipfile: ZipFile, entry: ZipEntry, outputDir: String) {
        try {
            if (!entry.isDirectory && !entry.name.contains("_")) {
                if (entry.isDirectory) {
                    createDir(File(outputDir, entry.name))
                    return
                }

                val outputFile = File(outputDir, entry.name)
                if (!outputFile.parentFile.exists()) {
                    createDir(outputFile.parentFile)
                }

                val inputStream = BufferedInputStream(zipfile.getInputStream(entry))
                val outputStream = BufferedOutputStream(FileOutputStream(outputFile))
                try {
                } finally {
                    outputStream.flush()
                    outputStream.close()
                    inputStream.close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun createDir(dir: File) {
        if (dir.exists()) {
            return
        }
        if (!dir.mkdirs()) {
            throw RuntimeException("Can not create dir \$dir")
        }
    }

    fun unZipFile(zipFile: String, destLocation: String) {
        try {
            val f = File(destLocation)
            val canonicalPath: String = f.canonicalPath
            if (!canonicalPath.startsWith(destLocation)) {
                // SecurityException
            } else {
                if (!f.isDirectory) {
                    f.mkdirs()
                    f.mkdir()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val zin = ZipInputStream(FileInputStream(zipFile))
        try {
            var ze: ZipEntry? = null

            while (run {
                    ze = zin.nextEntry
                    ze
                } != null) {

                val path = destLocation + File.separator + ze!!.name
                val f = File(destLocation, ze!!.name)
                val canonicalPath: String = f.canonicalPath
                if (canonicalPath.startsWith(File(destLocation).canonicalPath)) {
                    if (ze!!.isDirectory) {
                        val unzipFile = File(path)
                        if (!unzipFile.isDirectory) {
                            unzipFile.mkdirs()
                        }
                    } else {
                        val fout = FileOutputStream(path, false)
                        val bufout = BufferedOutputStream(fout)
                        val buffer = ByteArray(8096)
                        var read = 0
                        while (zin.read(buffer).also { read = it } != -1) {
                            bufout.write(buffer, 0, read)
                        }

                        zin.closeEntry()
                        bufout.close()
                        fout.close()

                        fout.use { fout ->
                            var c = zin.read()
                            while (c != -1) {
                                fout.write(c)
                                c = zin.read()
                            }
                            zin.closeEntry()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (zin != null)
                zin.close()
        }
    }

    @Throws(IOException::class)
    private fun copyFile(`in`: InputStream, out: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        while (`in`.read(buffer).also { read = it } != -1) {
            out.write(buffer, 0, read)
        }
    }

    fun scanFile(context: Context, file: File) {
        MediaScannerConnection.scanFile(
            context,
            arrayOf(file.absolutePath),
            null
        ) { path, uri ->
//            Log.i("ExternalStorage", "Scanned $path:")
//            Log.i("ExternalStorage", "-> uri=$uri")
        }
    }

    fun getFontDir(context: Context): File {
        val contextWrapper = ContextWrapper(context)
        val rootDir = contextWrapper.getDir(context.filesDir.name, Context.MODE_PRIVATE)
        val fontDir = File(rootDir.absolutePath, "fonts")
        if (!fontDir.exists()) {
            fontDir.mkdirs()
            fontDir.mkdir()
            val gpxfile = File(fontDir, ".nomedia")
            val writer = FileWriter(gpxfile)
            writer.flush()
            writer.close()
        }
        return fontDir
    }

    fun getInternalMediaDir(context: Context): File {
        val rootDir = context.cacheDir
        val imageDir = File(rootDir.absolutePath, "media")
        imageDir.setReadable(true)
        if (!imageDir.exists()) {
            imageDir.mkdirs()
            imageDir.mkdir()
        }
        return imageDir
    }

    fun getRateDirectoryName(context: Context): File {
        val contextWrapper = ContextWrapper(context)
//        val rootDir = contextWrapper.getDir(context.filesDir.name, Context.MODE_PRIVATE)
        val rootDir = context.cacheDir
        val saveDir = File(rootDir, "Rate")
        if (!saveDir.exists()) {
            saveDir.mkdirs()
            saveDir.mkdir()
        }
        return saveDir
    }

    fun getImageDir(context: Context): File {
        val contextWrapper = ContextWrapper(context)
        val rootDir = contextWrapper.getDir(context.filesDir.name, Context.MODE_PRIVATE)
        val imageDir = File(rootDir.absolutePath, INTERNAL_DIR_CONTENT)
        if (!imageDir.exists()) {
            imageDir.mkdirs()
            imageDir.mkdir()
            val gpxfile = File(imageDir, ".nomedia")
            val writer = FileWriter(gpxfile)
            writer.flush()
            writer.close()
        }
        return imageDir.canonicalFile
    }

    fun moveFile(inputFile: File, destFile: File): String {
        val newFile = File(destFile, inputFile.name)
        var outputChannel: FileChannel? = null
        var inputChannel: FileChannel? = null
        try {

            if (!destFile.exists()) {
                destFile.mkdirs()
                destFile.mkdir()
            }

            if (inputFile.exists()) {
                outputChannel = FileOutputStream(newFile).channel
                inputChannel = FileInputStream(inputFile).channel
                inputChannel.transferTo(0, inputChannel.size(), outputChannel)
                //  inputChannel.close()
                if (inputFile.exists()) {
                    val isDeleted = inputFile.delete()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (inputFile.exists()) {
                val isDeleted = inputFile.delete()
            }
            outputChannel?.close()
            inputChannel?.close()
        }
        return newFile.absolutePath
    }

    fun getStickerDir(context: Context): File {
        val contextWrapper = ContextWrapper(context)
        val rootDir = contextWrapper.getDir(context.filesDir.name, Context.MODE_PRIVATE)
        val imageDir = File(rootDir.absolutePath, "stickers")
        imageDir.setReadable(true)
        imageDir.setWritable(true, false)
        if (!imageDir.exists()) {
            imageDir.mkdirs()
            imageDir.mkdir()
            val gpxfile = File(imageDir, ".nomedia")
            val writer = FileWriter(gpxfile)
            writer.flush()
            writer.close()
        }
        return imageDir
    }

    fun getStickerImage(activity: Activity, fileName: String): File? {
        try {
            val file1 = File("${getStickerDir(activity).absolutePath}/$fileName.png")
            val file2 = File("${getStickerDir(activity).absolutePath}/$fileName.jpg")
            val file3 = File("${getStickerDir(activity).absolutePath}/$fileName.jpeg")
            val file4 = File("${getStickerDir(activity).absolutePath}/$fileName.webp")
            return when {
                file1.exists() -> file1
                file2.exists() -> file2
                file3.exists() -> file3
                file4.exists() -> file4
                else -> null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun readJson(context: Context, fileName: String): String? {
        return try {
            val fis = FileInputStream(File(fileName))
            val isr = InputStreamReader(fis)
            val bufferedReader = BufferedReader(isr)
            val sb = StringBuilder()
            var line: String? = null
            while (run {
                    line = bufferedReader.readLine()
                    line
                } != null) {
                sb.append(line)
            }
            fis.close()
            sb.toString()
        } catch (fileNotFound: FileNotFoundException) {
            null
        } catch (ioException: IOException) {
            null
        }


    }

    fun getLocalStorageSize(context: Context): Double {
        try {
            val contextWrapper = ContextWrapper(context)
            val rootDir = contextWrapper.getDir(context.filesDir.name, Context.MODE_PRIVATE)
            var fileSize = 0.0
            fileSize += getFolderSize(File(rootDir.absolutePath + "/images"))
            fileSize += getFolderSize(File(rootDir.absolutePath + "/json"))

            val mFileSize =
                fileSize / (1024F * 1024F)// (((round(((fileSize / (1024 * 1024L)) * 10L).toDouble()) / 10L).roundToLong()))
            return String.format(Locale.ENGLISH, "%.2f", mFileSize)
                .toDouble()// Formatter.formatFileSize(context, fileSize).split(" ")[0].toDouble()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0.0
    }

    private fun getFolderSize(f: File): Long {
        var size: Long = 0L
        if (f.isDirectory) {
            for (file in f.listFiles()) {
                size += getFolderSize(file)
            }
        } else {
            size = f.length()
        }
        return size
    }


    //-----=================================================================================================================================--------

    fun getCacheDirectoryName(context: Context): File {
        val contextWrapper = ContextWrapper(context)
        val rootDir = contextWrapper.getDir(context.cacheDir.name, Context.MODE_PRIVATE)
        if (!rootDir.exists()) {
            rootDir.mkdirs()
            rootDir.mkdir()
        }
        return context.cacheDir
    }

    fun getInternalFileDir(context: Context): File {
        val contextWrapper = ContextWrapper(context)
        val rootDir = contextWrapper.getDir(context.filesDir.name, Context.MODE_PRIVATE)
        val imageDir = File(rootDir.absolutePath, "")
        imageDir.setReadable(true)
        imageDir.setWritable(true, false)
        if (!imageDir.exists()) {
            imageDir.mkdirs()
            imageDir.mkdir()
            val gpxfile = File(imageDir, ".nomedia")
            val writer = FileWriter(gpxfile)
            writer.flush()
            writer.close()
        }
        return imageDir.canonicalFile
    }

    //TODO: Contents
    fun getInternalContentDir(context: Context): File {
        val imageDir = File(getInternalFileDir(context), INTERNAL_DIR_CONTENT)
        imageDir.setReadable(true)
        imageDir.setWritable(true, false)
        if (!imageDir.exists()) {
            imageDir.mkdirs()
            imageDir.mkdir()
            val gpxfile = File(imageDir, ".nomedia")
            val writer = FileWriter(gpxfile)
            writer.flush()
            writer.close()
        }
        return imageDir.canonicalFile
    }

    //TODO: Fonts
    fun getInternalFontDir(context: Context): File {
        val fontDir = File(getInternalFileDir(context), INTERNAL_DIR_FONT)
        fontDir.setReadable(true)
        fontDir.setWritable(true, false)
        if (!fontDir.exists()) {
            fontDir.mkdirs()
            fontDir.mkdir()
            val gpxfile = File(fontDir, ".nomedia")
            val writer = FileWriter(gpxfile)
            writer.flush()
            writer.close()
        }
        return fontDir.canonicalFile
    }



    fun duplicateTemplates(srcFile: File, dstFile: File) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            copyDir(
                srcFile.toPath(), dstFile.toPath()
            )
        } else {
            duplicateDirectory(srcFile, dstFile)
        }

    }

//    private fun copyDir(src: Path, dest: Path) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            Files.walk(src).forEach {
//                Files.copy(
//                    it, dest.resolve(src.relativize(it)), StandardCopyOption.REPLACE_EXISTING
//                )
//            }
//        }
//    }

    private fun copyDir(src: Path, dest: Path) {
        Files.walk(src).forEach {
            if (it.extension != "zip")
                Files.copy(
                    it, dest.resolve(src.relativize(it)), StandardCopyOption.REPLACE_EXISTING
                )
        }
    }

    private fun duplicateDirectory(src: File, dst: File) {
        try {
            if (src.isDirectory) {
                val files = src.list()
                val filesLength = files.size
                for (i in 0 until filesLength) {
                    val src1 = File(src, files[i]).path
                    val dst1 = dst.path
                    duplicateDirectory(src, dst)
                }
            } else {
                copyFile(src, dst)
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    fun copyFile(sourceFile: File?, destFile: File) {
        if (!destFile.parentFile?.exists()!!) destFile.parentFile?.mkdirs()
        if (!destFile.exists()) {
            destFile.createNewFile()
        }
        var source: FileChannel? = null
        var destination: FileChannel? = null
        try {
            source = FileInputStream(sourceFile).channel
            destination = FileOutputStream(destFile).channel
            destination.transferFrom(source, 0, source.size())
        } finally {
            source?.close()
            destination?.close()
        }
    }

    fun copyFiles(sourceFile: File?, destFile: File): String {
        var outPutFilePath = ""

        if (!destFile.parentFile?.exists()!!) destFile.parentFile?.mkdirs()
        if (!destFile.exists()) {
            destFile.createNewFile()
        }
        var source: FileChannel? = null
        var destination: FileChannel? = null
        try {
            source = FileInputStream(sourceFile).channel
            destination = FileOutputStream(destFile).channel
            destination.transferFrom(source, 0, source.size())
            outPutFilePath = destFile.absolutePath
        } finally {
            source?.close()
            destination?.close()
        }
        return outPutFilePath
    }

    fun copyFile(inputPath: String, inputFile: String, outputPath: String) {
        var `in`: InputStream? = null
        var out: OutputStream? = null
        try {

            //create output directory if it doesn't exist
            val dir = File(outputPath)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            `in` = FileInputStream(inputPath + inputFile)
            out = FileOutputStream(outputPath + inputFile)
            val buffer = ByteArray(1024)
            var read: Int
            while (`in`.read(buffer).also { read = it } != -1) {
                out.write(buffer, 0, read)
            }
            `in`.close()
            `in` = null

            // write the output file (You have now copied the file)
            out.flush()
            out.close()
            out = null
        } catch (fnfe1: FileNotFoundException) {
            Log.e("tag", fnfe1.message!!)
        } catch (e: java.lang.Exception) {
            Log.e("tag", e.message!!)
        }
    }

    @Throws(IOException::class)
    fun copyFontFile(sourceFile: File?, destFile: File) {
        var source: FileChannel? = null
        var destination: FileChannel? = null
        try {
            source = FileInputStream(sourceFile).channel
            destination = FileOutputStream(destFile).channel
            destination.transferFrom(source, 0, source.size())
        } finally {
            source?.close()
            destination?.close()
        }
    }


    const val IMAGE_QUALITY = 100
    const val IMAGE_QUALITY_LOLLIPOP = 80

    fun getBitmapQuality(): Int {
        return when {
            Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1 -> IMAGE_QUALITY
            Build.VERSION.SDK_INT > Build.VERSION_CODES.M -> IMAGE_QUALITY_LOLLIPOP
            else -> IMAGE_QUALITY_LOLLIPOP
        }
    }

    fun saveBitmapToFile(
        bm: Bitmap,
        fileName: String,
        saveFilePath: File,
        fileFormat: String = ""
    ): File {
        val dir = File(saveFilePath.absolutePath)
        dir.setReadable(true)
        dir.setExecutable(true)
        dir.setWritable(true, false)
        if (!dir.exists()) {
            dir.mkdirs()
            dir.mkdir()
        }
        val file = File(dir, fileName)
        if (file.exists()) {
            file.delete()
        }
        try {
            file.createNewFile()
            val fOut = FileOutputStream(file)
            bm.compress(
                if (fileFormat == EXTENSION_JPG) Bitmap.CompressFormat.JPEG else Bitmap.CompressFormat.PNG,
                getBitmapQuality(),
                fOut
            )
            fOut.flush()
            fOut.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return file
    }

    fun zipFolder(toZipFolder: File): File? {
        val ZipFile = File(toZipFolder.parent, format("%s.zip", toZipFolder.name))
        return try {
            val out = ZipOutputStream(FileOutputStream(ZipFile))
            zipSubFolder(out, toZipFolder, toZipFolder.path.length)
            out.close()
            ZipFile
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
            null
        }
    }

    /**
     * Main zip Function
     * @param out Target ZipStream
     * @param folder Folder to be zipped
     * @param basePathLength Length of original Folder Path (for recursion)
     */
    @Throws(IOException::class)
    private fun zipSubFolder(out: ZipOutputStream, folder: File, basePathLength: Int) {
        val BUFFER = 2048
        val fileList = folder.listFiles()
        var origin: BufferedInputStream? = null
        if (fileList != null)
            for (file in fileList) {
                if (file != null) {
                    if (file.isDirectory) {
                        zipSubFolder(out, file, basePathLength)
                    } else {
                        val data = ByteArray(BUFFER)
                        val unmodifiedFilePath = file.path
                        val relativePath = unmodifiedFilePath.substring(basePathLength + 1)
                        val fi = FileInputStream(unmodifiedFilePath)
                        origin = BufferedInputStream(fi, BUFFER)
                        val entry = ZipEntry(relativePath)
                        entry.time =
                            file.lastModified() // to keep modification time after unzipping
                        out.putNextEntry(entry)
                        var count: Int
                        while (origin.read(data, 0, BUFFER).also { count = it } != -1) {
                            out.write(data, 0, count)
                        }
                        origin.close()
                        out.closeEntry()
                    }
                }
            }
    }

    fun getImageHeightWidth(imagePath: File): BitmapFactory.Options? {
        return try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(imagePath.absolutePath, options)
            options
        } catch (e: Exception) {
            null
        }

    }

    fun getPreviewFileFromPath(path: String): File {
        val folder = File(path)
        if (folder.exists()) {
            val fileList = folder.listFiles()?.find { file: File? -> file?.nameWithoutExtension!!.lowercase().startsWith("preview") }
            if (fileList != null && fileList.exists()) {
                return fileList
            }
        }
        return File("$path/${File(path).name}.png")
    }

}
