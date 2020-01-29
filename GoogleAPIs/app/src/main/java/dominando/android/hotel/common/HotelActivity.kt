package dominando.android.hotel.common

import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import dominando.android.hotel.R
import dominando.android.hotel.model.Hotel
import dominando.android.hotel.hotel_details.HotelDetailsActivity
import dominando.android.hotel.hotel_details.HotelDetailsFragment
import dominando.android.hotel.hotel_form.HotelFormFragment
import dominando.android.hotel.hotel_list.HotelListFragment
import dominando.android.hotel.hotel_list.HotelListViewModel
import dominando.android.hotel.hotel_login.UserProfileFragment
import kotlinx.android.synthetic.main.activity_hotel.*
import org.koin.android.viewmodel.ext.android.viewModel

class HotelActivity : BaseActivity(), HotelListFragment.OnHotelClickListener,
    SearchView.OnQueryTextListener, MenuItem.OnActionExpandListener {

    private val viewModel: HotelListViewModel by viewModel()
    private var searchView: SearchView? = null

    private val listFragment: HotelListFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragmentList) as HotelListFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hotel)

        fabAdd.setOnClickListener {
            listFragment.hideDeleteMode()
            HotelFormFragment.newInstance().open(supportFragmentManager)
        }

        initAdMob()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.hotel, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        searchItem?.setOnActionExpandListener(this)

        searchView = searchItem?.actionView as SearchView
        searchView?.queryHint = getString(R.string.hint_search)
        searchView?.setOnQueryTextListener(this)

        if (viewModel.getSearchTerm()?.value?.isNotEmpty() == true) {
            Handler().post {
                val query = viewModel.getSearchTerm()?.value
                searchItem.expandActionView()
                searchView?.setQuery(query, true)
                searchView?.clearFocus()
            }
        }

        return true
    }

    override fun onHotelClick(hotel: Hotel) {
        if (isTablet()) {
            viewModel.hotelIdSelected = hotel.id
            showDetailsFragment(hotel.id)
        } else {
            showDetailsActivity(hotel.id)
        }
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        listFragment.search(newText ?: "")
        return true
    }

    override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
        listFragment.search()
        return true
    }

    private fun isTablet() = resources.getBoolean(R.bool.tablet)

    private fun showDetailsFragment(hotelId: Long) {
        searchView?.setOnQueryTextListener(null)

        val fragment = HotelDetailsFragment.newInstance(hotelId)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.details, fragment, HotelDetailsFragment.TAG_DETAILS)
            .commit()
    }

    private fun showDetailsActivity(hotelId: Long) {
        HotelDetailsActivity.open(this, hotelId)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_info -> AboutDialogFragment().show(supportFragmentManager, "sobre")
            R.id.action_user_profile -> UserProfileFragment().open(supportFragmentManager)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?) = true

    override fun onMenuItemActionExpand(item: MenuItem?): Boolean = true

    private fun initAdMob() {
        MobileAds.initialize(this, getString(R.string.admob_app_id))

        val adRequest = AdRequest.Builder()
            .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
            .addTestDevice("0025575238")
            .build()

        adView.loadAd(adRequest)
    }

    override fun onResume() {
        super.onResume()
        adView.resume()
    }

    override fun onPause() {
        super.onPause()
        adView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        adView.destroy()
    }
}
