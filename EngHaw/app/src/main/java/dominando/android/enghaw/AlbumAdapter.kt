package dominando.android.enghaw

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dominando.android.enghaw.model.Album
import dominando.android.enghaw.model.AlbumHttp
import kotlinx.android.synthetic.main.item_album.view.*

class AlbumAdapter(private val albums: List<Album>,
                   private val onItemClick: (View, Album, Int) -> Unit) : RecyclerView.Adapter<AlbumAdapter.VH>() {

    override fun getItemCount() = albums.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
        val viewHolder = VH(v)

        v.setOnClickListener { view ->
            val position = viewHolder.adapterPosition
            val album = albums[position]
            onItemClick(view, album, position)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val album = albums[position]

        holder.run {
            Picasso.get()
                .load(AlbumHttp.BASE_URL + album.cover)
                .into(imgCover)

            txtTitle?.text = album.title
            txtYear?.text = album.year.toString()

            imgCover?.transitionName = "cover${album.title}"
            txtTitle?.transitionName = "title${album.title}"
            txtYear?.transitionName = "year${album.title}"
        }
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        var imgCover: ImageView? = view.imgCover
        var txtTitle: TextView? = view.txtTitle
        var txtYear: TextView? = view.txtYear
    }
}