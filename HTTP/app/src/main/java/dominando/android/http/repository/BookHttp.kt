package dominando.android.http.repository

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.Gson
import dominando.android.http.model.Book
import dominando.android.http.model.Publisher
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

/*
* contém 3 exemplos, HttpUrlConnection / OkHttp + Gson / XmlPullParse
* */
object BookHttp {

    val BOOKS_JSON_URL = "https://raw.githubusercontent.com/nglauber/dominando_android3/master/livros_novatec.json"
    val BOOKS_XML_URL = "https://raw.githubusercontent.com/nglauber/dominando_android3/master/livros_novatec.xml"

//  HttpUrlConnection
    @Throws(IOException::class)
    private fun connect(urlAddress: String): HttpURLConnection {
        val second = 1000
        val url = URL(urlAddress)

        val connection = (url.openConnection() as HttpURLConnection).apply {
            readTimeout = 10 * second
            connectTimeout = 15 * second
            requestMethod = "GET"
            doInput = true
            doOutput = false
        }

        connection.connect()
        return connection
    }

    fun hasConnection(ctx: Context?): Boolean {
        val cm = ctx?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        return info != null && info.isConnected
    }

    fun loadBooks(): List<Book>? {
        try {
            val connection = connect(
                BOOKS_JSON_URL
            )
            val responseCode = connection.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val inputStream = connection.inputStream
                val json = JSONObject(
                    streamToString(
                        inputStream
                    )
                )
                return readBooksFromJson(
                    json
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(JSONException::class)
    fun readBooksFromJson(json: JSONObject): List<Book> {
        val booksList = mutableListOf<Book>()
        var currentCategory: String
        val jsonNovatec = json.getJSONArray("novatec")

        for (i in 0 until jsonNovatec.length()) {
            val jsonCategory = jsonNovatec.getJSONObject(i)
            currentCategory = jsonCategory.getString("categoria")
            val jsonBooks = jsonCategory.getJSONArray("livros")

            for (j in 0 until jsonBooks.length()) {
                val jsonBook = jsonBooks.getJSONObject(j)
                val book = Book(
                    jsonBook.getString("titulo"),
                    currentCategory,
                    jsonBook.getString("autor"),
                    jsonBook.getInt("ano"),
                    jsonBook.getInt("paginas"),
                    jsonBook.getString("capa")
                )
                booksList.add(book)
            }
        }
        return booksList
    }

    @Throws(IOException::class)
    private fun streamToString(inputStream: InputStream): String {
        val buffer = ByteArray(1024)
        val bigBuffer = ByteArrayOutputStream() //O bigBuffer vai armazenar todos os bytes lidos
        var bytesRead: Int //quantidade de bytes lidos

        while (true) {
            bytesRead = inputStream.read(buffer)    //lendo 1KB por vez
            if (bytesRead == -1) break

            bigBuffer.write(buffer, 0, bytesRead) //copiando os bytes lidos do buffer -> bigBuffer
        }

        return String(bigBuffer.toByteArray(), Charset.forName("UTF-8"))
    }

//    OkHttp + Gson
    fun loadBooksGson(): List<Book>? {

        val client = OkHttpClient.Builder()
            .readTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url(BOOKS_JSON_URL)
            .build()

        try {
            val response = client.newCall(request).execute()
            val json = response.body?.string()
            val gson = Gson()
            val publisher = gson.fromJson<Publisher>(json, Publisher::class.java)

            return publisher.categories.flatMap { category ->
                category.books.forEach { book ->
                    book.category = category.name
                }
                category.books
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

//    XmlPullParser
    @Throws(Exception::class)
    private fun readBooksXml(inputStream: InputStream): List<Book> {
        val bookList = mutableListOf<Book>()
        var book: Book? = null
        var currentTag: String? = null
        var currentCategory: String = ""

        val factory = XmlPullParserFactory.newInstance()

        val xpp = factory.newPullParser()
        xpp.setInput(inputStream, "UTF-8")
        var eventType = xpp.eventType

        while (eventType != XmlPullParser.END_DOCUMENT) {

            when (eventType) {
                XmlPullParser.START_TAG -> {
                    currentTag = xpp.name
                    if ("livro" == currentTag) {
                        book = Book()
                        book.category = currentCategory
                    }
                }

                XmlPullParser.END_TAG -> {
                    if ("livro" == xpp.name) {
                        bookList.add(book as Book)
                    }
                }

                XmlPullParser.TEXT -> {
                    if (!xpp.isWhitespace) {
                        val text = xpp.text

                        when (currentTag) {
                            "titulo" -> book?.title = text
                            "paginas" -> book?.pages = text.toInt()
                            "capa" -> book?.coverUrl = text
                            "autor" -> book?.author = text
                            "ano" -> book?.year = text.toInt()
                            "categoria" -> currentCategory = text
                        }
                    }
                }
            }

            eventType = xpp.next()
        }

        return bookList
    }

    fun readBooksXml(): List<Book>? {
        try {
            val connection = connect(BOOKS_XML_URL)
            val responseCode = connection.responseCode

            if (responseCode == HttpURLConnection.HTTP_OK) {
                val `is` = connection.inputStream
                return readBooksXml(`is`)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}