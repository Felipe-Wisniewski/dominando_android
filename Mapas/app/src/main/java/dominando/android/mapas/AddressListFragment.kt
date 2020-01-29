package dominando.android.mapas

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.location.Address
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.LatLng

class AddressListFragment : DialogFragment() {

    private val viewModel: MapViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MapViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val args = arguments?.getParcelableArray(EXTRA_ADDRESSES) ?: emptyArray()

        val addresses = args as Array<Address>

        val onAddressClick = DialogInterface.OnClickListener { _, which ->
            val selectedAddress = addresses[which]

            viewModel.setDestination(LatLng(selectedAddress.latitude, selectedAddress.longitude))
        }

        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.map_title_address_result_dialog)
            .setItems(addressesToArray(addresses), onAddressClick)
            .setOnDismissListener { viewModel.clearSearchAddressResult() }
            .create()
    }

    private fun addressesToArray(addresses: Array<Address>): Array<String> {
        val addressTexts = mutableListOf<String>()

        for (address in addresses) {

            val street = StringBuffer()

            for (j in 0..address.maxAddressLineIndex) {

                if (street.isNotEmpty()) {
                    street.append('\n')
                }

                street.append(address.getAddressLine(j))
            }

            val country = address.countryName
            addressTexts.add("$street, $country")
        }

        return addressTexts.toTypedArray()
    }

    companion object {
        private const val EXTRA_ADDRESSES = "addresses"

        fun newInstance(addresses: List<Address>): AddressListFragment {
            return AddressListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArray(EXTRA_ADDRESSES, addresses.toTypedArray())
                }
            }
        }
    }
}