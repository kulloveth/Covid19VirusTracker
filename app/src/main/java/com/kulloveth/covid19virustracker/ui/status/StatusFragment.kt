package com.kulloveth.covid19virustracker.ui.status

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kulloveth.covid19virustracker.R
import com.kulloveth.covid19virustracker.data.db.StatusEntity
import com.kulloveth.covid19virustracker.ui.info.InfoActivity
import com.kulloveth.covid19virustracker.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_status.*
import kotlinx.coroutines.FlowPreview
import org.koin.androidx.viewmodel.ext.android.viewModel


/**
 * A simple [Fragment] subclass.
 */
class StatusFragment : BaseFragment(), StatusAdapter.StatusITemListener {

    private val TAG = StatusFragment::class.java.simpleName
    private val viewModel: StatusViewModel by viewModel()
    private var statusRv: RecyclerView? = null
    private var progress: ProgressBar? = null


    override fun getLayoutId() = R.layout.fragment_status


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).setSupportActionBar(app_bar)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.covid_status)
        statusRv = list
        progress = progress_bar
        getStatus()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.info_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.info){
            startActivity(Intent(requireActivity(), InfoActivity::class.java))
            requireActivity().overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }

        return super.onOptionsItemSelected(item)
    }

    //observe status from livedata
    @FlowPreview
    private fun getStatus() {
        viewModel.getNewStatus().observe(requireActivity(), Observer {
            Log.d(TAG,"$it")
            val adapter = StatusAdapter(this)
            statusRv?.apply {
                this.adapter = adapter
                postponeEnterTransition()
                viewTreeObserver.addOnPreDrawListener {
                    startPostponedEnterTransition()
                    true
                }
            }
            adapter.submitList(it)
            statusRv?.visibility = View.VISIBLE
            progress?.visibility = View.INVISIBLE
        })
    }


    override fun onStatusListener(statusEntity: StatusEntity) {
        viewModel.setUpStatus(statusEntity)
        requireView().findNavController().navigate(R.id.action_countriesFragment_to_detailsFragment)
    }




}
