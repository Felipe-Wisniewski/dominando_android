package dominando.android.hotel.hotel_details

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import dominando.android.hotel.hotel_form.HotelFormFragment
import dominando.android.hotel.R
import dominando.android.hotel.model.Hotel
import dominando.android.hotel.repository.http.HotelHttp
import kotlinx.android.synthetic.main.fragment_hotel_details.*
import org.koin.android.viewmodel.ext.android.viewModel

class HotelDetailsFragment : Fragment() {

    private val viewModel: HotelDetailsViewModel by viewModel()
    private var hotel: Hotel? = null
    private var shareActionProvider: ShareActionProvider? = null

    companion object{
        const val TAG_DETAILS = "tagDetalhe"
        private const val EXTRA_HOTEL_ID = "hotelId"

        fun newInstance(id: Long) = HotelDetailsFragment().apply {
            arguments = Bundle().apply {
                putLong(EXTRA_HOTEL_ID, id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.hotel_details, menu)

        val shareItem = menu.findItem(R.id.action_share)
        shareActionProvider = MenuItemCompat.getActionProvider(shareItem) as? ShareActionProvider
        setShareIntent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_edit) {
            HotelFormFragment
                .newInstance(hotel?.id ?: 0)
                .open(requireFragmentManager())
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setShareIntent() {
        val text = getString(R.string.share_text, hotel?.name, hotel?.rating)
        shareActionProvider?.setShareIntent(Intent(Intent.ACTION_SEND).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_hotel_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = arguments?.getLong(EXTRA_HOTEL_ID, -1) ?: -1
        viewModel.loadHotelDetails(id).observe(viewLifecycleOwner, Observer { hotel ->
            if (hotel != null) {
                showHotelsDetails(hotel)

            } else {
                activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.remove(this)
                    ?.commit()

                errorHotelNotFound()
            }
        })
    }

    private fun showHotelsDetails(hotel: Hotel) {
        this.hotel = hotel
        txtName.text = hotel.name
        txtAddress.text = hotel.address
        rtbRating.rating = hotel.rating

        var photoUrl = hotel.photoUrl
        if (photoUrl.isNotEmpty()) {
            if (!photoUrl.contains("content://")) {
                photoUrl = HotelHttp.BASE_URL + hotel.photoUrl
            }
            Glide.with(imgPhoto.context).load(photoUrl).into(imgPhoto)
        }
    }

    private fun errorHotelNotFound() {
        txtName.text = getString(R.string.error_hotel_not_found)
        txtAddress.visibility = View.GONE
        rtbRating.visibility = View.GONE
    }
}