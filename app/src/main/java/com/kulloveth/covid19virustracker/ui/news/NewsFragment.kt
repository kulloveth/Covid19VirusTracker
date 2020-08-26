package com.kulloveth.covid19virustracker.ui.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.kulloveth.covid19virustracker.R
import com.kulloveth.covid19virustracker.data.db.NewsEntity
import com.kulloveth.covid19virustracker.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_news.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass.
 */
class NewsFragment : BaseFragment(),NewsAdapter.ArticleITemListener {

    private val TAG = NewsFragment::class.java.simpleName
    private val viewModel: NewsViewModel by viewModel()
    private var newsRv: RecyclerView? = null
    private var progress: ProgressBar? = null
    private val adapter = NewsAdapter(this)
    override fun getLayoutId() = R.layout.fragment_news

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        app_bar.title = getString(R.string.covid_news)
        progress = progress_bar
        newsRv = news_list
        newsRv?.adapter = adapter
        postponeEnterTransition()
        newsRv?.doOnPreDraw {
            startPostponedEnterTransition()
        }
        fetchNews()
    }

    //observe status from livedata
    fun fetchNews() {
        viewModel.getNewCovidNews().observe(requireActivity(), Observer {
            Log.d(TAG,"$it")
            adapter.submitList(it)
            newsRv?.visibility = View.VISIBLE
            progress?.visibility = View.INVISIBLE

        })
    }

    override fun articleListener(imageView: ImageView,newsEntity: NewsEntity) {
        viewModel.passNewsDetail(newsEntity)
        val extras=FragmentNavigatorExtras(imageView to imageView.transitionName)
        requireView().findNavController().navigate(R.id.action_newsFragment_to_newsDetailFragment,null,null,extras)
    }

}
