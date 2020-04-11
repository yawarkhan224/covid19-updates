package com.aryk.covid.ui.legal

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aryk.covid.BuildConfig
import com.aryk.covid.MainActivity
import com.aryk.covid.R
import com.aryk.covid.helper.ItemClickSupport
import com.aryk.covid.ui.legal.adapters.LicenseAdapter
import kotlinx.android.synthetic.main.fragment_legal_info.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * A simple [Fragment] subclass.
 */
@ExperimentalCoroutinesApi
class LegalInfoFragment : Fragment() {
    companion object {
        private const val FRAGMENT_TAG = "legal_info_fragment"
    }

    private lateinit var libraryName: Array<String>
    private lateinit var libraryLinks: Array<String>
    private lateinit var recyclerAdapter: LicenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_legal_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        libraryName = resources.getStringArray(R.array.library_name)
        libraryLinks = resources.getStringArray(R.array.library_web_link)

        licenseRecyclerView.layoutManager = LinearLayoutManager(context)
        licenseRecyclerView.adapter = LicenseAdapter()
        recyclerAdapter = licenseRecyclerView.adapter as LicenseAdapter

        versionValue.text = BuildConfig.VERSION_NAME

        val dividerItemDecoration = DividerItemDecoration(
            licenseRecyclerView.context,
            (licenseRecyclerView.layoutManager as LinearLayoutManager).orientation
        )

        licenseRecyclerView.addItemDecoration(dividerItemDecoration)

        disclaimerValue.movementMethod = LinkMovementMethod.getInstance()

        recyclerAdapter.submitList(libraryName.toList())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ItemClickSupport.addTo(licenseRecyclerView).onItemClickListener =
            object : ItemClickSupport.OnItemClickListener {
                override fun onItemClicked(
                    recyclerView: RecyclerView,
                    position: Int,
                    v: View
                ) {
                    if (position == RecyclerView.NO_POSITION) {
                        return
                    } else {
                        val browserIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(libraryLinks[position])
                        )

                        startActivity(browserIntent)
                    }
                }
            }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.sortBy).isVisible = false
        menu.findItem(R.id.aboutApp).isVisible = false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()

        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
    }
}
