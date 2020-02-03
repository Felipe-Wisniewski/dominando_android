package dominando.android.livros.booklist

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dominando.android.livros.*
import dominando.android.livros.bookdetails.BookDetailsActivity
import dominando.android.livros.bookform.BookFormActivity
import dominando.android.livros.livedata.observeOnce
import dominando.android.livros.model.Book
import kotlinx.android.synthetic.main.activity_book_list.*
import java.lang.Exception

class BookListActivity : BaseActivity() {

    private val viewModel: BookListViewModel by lazy {
        ViewModelProvider(this).get(BookListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        fabAdd.setOnClickListener {
            startActivity(Intent(this, BookFormActivity::class.java))
        }
    }

    override fun init() {
        try {
            viewModel.getBooks().observe(this, Observer { books ->
                updateList(books)
            })
        } catch (e: Exception) {
            Toast.makeText(this,
                R.string.message_error_load_books, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateList(books: List<Book>) {
        rvBooks.layoutManager = LinearLayoutManager(this)

        rvBooks.adapter =
            BookAdapter(books) { book ->
                BookDetailsActivity.start(
                    this,
                    book
                )
            }

        attachSwipeToRecyclerView()
    }

    private fun attachSwipeToRecyclerView() {
        val swipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                removeBookFromPosition(position)
            }

        }

        val itemTouchHelper = ItemTouchHelper(swipe)
        itemTouchHelper.attachToRecyclerView(rvBooks)
    }

    private fun removeBookFromPosition(position: Int) {
        val adapter = rvBooks.adapter as BookAdapter
        val book = adapter.books[position]

        viewModel.removeBook(book).observeOnce(Observer { success ->
            if (!success) {
                Toast.makeText(this, R.string.message_error_delete_book, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
