package dominando.android.multimidia

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.text.format.DateFormat
import java.io.File
import java.util.*

object MediaUtils {

    private const val PREFERENCE_MEDIA = "midia_prefs"
    private const val MEDIA_FOLDER = "Dominando_Android"

    const val REQUEST_CODE_PHOTO = 1
    const val REQUEST_CODE_VIDEO = 2
    const val REQUEST_CODE_AUDIO = 3
    const val PROVIDER_AUTHORITY = "dominando.android.multimidia.fileprovider"

    enum class MediaType(val extension: String, val preferenceKey: String) {
        MEDIA_PHOTO(".jpg", "last_photo"),
        MEDIA_VIDEO(".mp4", "last_video"),
        MEDIA_AUDIO(".3gp", "last_audio")
    }

    fun newMedia(mediaType: MediaType): File {
        val fileName = DateFormat.format("yyyy-MM-dd_hhmmss", Date()).toString()

        val mediaDir = File(Environment.getExternalStorageDirectory(), MEDIA_FOLDER)

        if (!mediaDir.exists()) {
            if (!mediaDir.mkdirs()) {
                throw IllegalArgumentException("Fail to create directories")
            }
        }

        return File(mediaDir, fileName + mediaType.extension)
    }

    fun saveLastMediaPath(ctx: Context, mediaType: MediaType, mediaPath: String) {

        ctx.getSharedPreferences(PREFERENCE_MEDIA, Context.MODE_PRIVATE)
            .edit()
            .putString(mediaType.preferenceKey, mediaPath)
            .apply()

        ctx.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).apply {
            data = Uri.parse(mediaPath)
        })

    }

    fun getLastMediaPath(ctx: Context, mediaType: MediaType): String? {

        return ctx.getSharedPreferences(PREFERENCE_MEDIA, Context.MODE_PRIVATE)
                .getString(mediaType.preferenceKey, null)
    }

    fun loadImage(imageFile: File, width: Int, height: Int): Bitmap? {

    }


















}