package com.kulloveth.covid19virustracker.ui.news

import android.content.Intent
import android.icu.util.TimeUnit
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.transition.TransitionInflater
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kulloveth.covid19virustracker.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_news_detail.*
import org.koin.android.ext.android.inject


/**
 * A simple [Fragment] subclass.
 */
class NewsDetailFragment : Fragment() {

    private val viewModel: NewsViewModel by inject()
    private var newsImage: AppCompatImageView? = null
    private var descTv: TextView? = null
    private var contentTv: TextView? = null
    private var publishedTv: TextView? = null
    private var titleTv: TextView? = null
    private var toolba: Toolbar? = null
    private var shareFab: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        postponeEnterTransition(250, java.util.concurrent.TimeUnit.MILLISECONDS)
        return inflater.inflate(R.layout.fragment_news_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolba = toolbar
        toolba?.setNavigationOnClickListener {
            requireView().findNavController()
                .navigate(R.id.action_newsDetailFragment_to_newsFragment)
        }
        newsImage = article_image
        descTv = description
        contentTv = content
        publishedTv = published
        shareFab = fab
        titleTv = title
        newsImage?.transitionName = resources.getString(R.string.transition_image)
        viewModel.covidNewsLiveData.observe(requireActivity(), Observer {
            Picasso.get().load(it.urlToImage).error(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.covid)
                .into(newsImage)
            descTv?.text = it.description
            contentTv?.text = it.content
            if (isAdded) {
                publishedTv?.text = String.format(
                    requireActivity().resources.getString(
                        R.string.published,
                        it.publishedAt
                    )
                )
            }
            titleTv?.text = it.title
            shareFab?.setOnClickListener {v->
                startActivity(
                    Intent.createChooser(
                        ShareCompat.IntentBuilder.from(requireActivity())
                    .setType("text/plain")
                    .setText(it.url)
                    .getIntent(), "Covid News"))
            }

        })
    }



}