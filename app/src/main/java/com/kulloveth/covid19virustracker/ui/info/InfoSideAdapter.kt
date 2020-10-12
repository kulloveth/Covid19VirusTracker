package com.kulloveth.covid19virustracker.ui.info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kulloveth.covid19virustracker.R
import kotlinx.android.synthetic.main.item_intro.view.*
import kotlinx.android.synthetic.main.item_onboarding.view.app_nam
import kotlinx.android.synthetic.main.item_onboarding.view.lotty_icon

class InfoSideAdapter(private val infoSliders: List<InfoSlide>,private val passText: PassText) :
    RecyclerView.Adapter<InfoSideAdapter.IntroSliderViewHolder>() {

    inner class IntroSliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val textTitle = view.app_nam
        private val imageIcon = view.lotty_icon
        private val desc = view.desc

        fun bind(infoSlide: InfoSlide) {
            textTitle.text = infoSlide.titleResource
            desc.text = infoSlide.detail
            imageIcon.setAnimation(infoSlide.logoResource)
            imageIcon.imageAssetsFolder = "images";
            passText.onTextPass(desc)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSliderViewHolder {
        return IntroSliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_intro,
                parent, false
            )
        )
    }

    override fun getItemCount() = infoSliders.size

    override fun onBindViewHolder(holder: IntroSliderViewHolder, position: Int) {
        holder.bind(infoSliders[position])
    }

interface PassText{
   fun onTextPass(textView:TextView)
}
}