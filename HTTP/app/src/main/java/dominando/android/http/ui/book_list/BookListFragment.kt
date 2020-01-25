package dominando.android.http.ui.book_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import dominando.android.http.repository.BookHttp
import dominando.android.http.R
import dominando.android.http.model.Book
import dominando.android.http.ui.InternetFragment
import kotlinx.android.synthetic.main.fragment_books_list.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class BookListFragment : InternetFragment(), CoroutineScope {

//    private var asyncTask: BooksDownloadTask? = null
    private lateinit var job: Job
    private var downloadJob: Job? = null

    private val booksList = mutableListOf<Book>()
    private var adapter: ArrayAdapter<Book>? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()

        job.cancel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_books_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = BookListAdapter(
            requireContext(),
            booksList
        )
        listView.emptyView = txtMessage
        listView.adapter = adapter

        if (booksList.isNotEmpty()) {
            showProgress(false)
            updateBookList(booksList)

        } else {
            startDownload()
        }
    }

    override fun startDownload() {
        if (downloadJob == null) {
            if (BookHttp.hasConnection(requireContext())) {
                startDownloadJson()

            } else {
                progressBar.visibility = View.GONE
                txtMessage.setText(R.string.error_no_connection)
            }
        } else if (downloadJob?.isActive == true) {
            showProgress(true)
        }
    }

    private fun showProgress(show: Boolean) {
        if (show) {
            txtMessage.setText(R.string.message_progress)
        }
        txtMessage.visibility = if (show) View.VISIBLE else View.GONE
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun startDownloadJson() {
        downloadJob = launch {
            showProgress(true)
            val booksTask = withContext(Dispatchers.IO) {
                BookHttp.loadBooksGson()
            }
            updateBookList(booksTask)
            showProgress(false)
        }
    }

    private fun updateBookList(result: List<Book>?) {
        if (result != null) {
            booksList.clear()
            booksList.addAll(result)

        } else {
            txtMessage.setText(R.string.error_load_books)
        }
        adapter?.notifyDataSetChanged()
        downloadJob = null
    }

    /*inner class BooksDownloadTask : AsyncTask<Void, Void, List<Book>>() {

        override fun onPreExecute() {
            super.onPreExecute()
            showProgress(true)
        }

        override fun doInBackground(vararg strings: Void?): List<Book>? {
//            return BookHttp.readBooksFromJson()
//            return BookHttp.loadBooksGson()
            return BookHttp.readBooksXml()

        }

        override fun onPostExecute(livros: List<Book>?) {
            super.onPostExecute(livros)
            showProgress(false)
            updateBookList(livros)
        }
    }*/
}