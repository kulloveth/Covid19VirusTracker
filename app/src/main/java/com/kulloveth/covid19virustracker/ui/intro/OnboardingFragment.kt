package com.kulloveth.covid19virustracker.ui.intro

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.kulloveth.covid19virustracker.R
import com.kulloveth.covid19virustracker.data.PreferenceRepository
import com.kulloveth.covid19virustracker.ui.MainActivity
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.fragment_onboarding.*
import kotlinx.android.synthetic.main.fragment_onboarding.buttonNext
import kotlinx.android.synthetic.main.fragment_onboarding.indicatorContainer


/**
 * A simple [Fragment] subclass.
 */
class OnboardingFragment : Fragment() {

    private val introSideAdapter = IntroSideAdapter(
        listOf(
            IntroSlide(
                "Keep Your Hands Clean",
                "wash_hands.json"
            ),
            IntroSlide(
                "Protect your face", "wear_mask.json"
            ),
            IntroSlide(
                "Stay at Home",
                "stay_home.json")))



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PreferenceRepository.saveOnboarding(true)
        introSliderViewPager.adapter = introSideAdapter
        setupIndicators()
        setCurrentIndicator(0)
        introSliderViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicator(position)
                }
            }
        )

        buttonNext.setOnClickListener {

            if (introSliderViewPager.currentItem + 1 < introSideAdapter.itemCount) {
                introSliderViewPager.currentItem += 1
            } else {
                startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
                requireActivity().overridePendingTransition(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left
                )
            }
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_onboarding, container, false)
    }

    private fun setupIndicators() {

        val indicators = arrayOfNulls<ImageView>(introSideAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {

            indicators[i] = ImageView(requireContext())
            indicators[i]?.apply {
                this.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.indicator_inactive
                    )
                )
                this.layoutParams = layoutParams
            }
            indicatorContainer.addView(indicators[i])

        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = indicatorContainer.childCount

        for (i in 0 until childCount) {

            val imageView = indicatorContainer.get(i) as ImageView
            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(), R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireActivity(),
                        R.drawable.indicator_inactive
                    )
                )
            }

        }

        if (index == introSideAdapter.itemCount - 1) {
            val animation = AnimationUtils.loadAnimation(requireActivity(), R.anim.app_name_animation)
            buttonNext.animation = animation
            buttonNext.visibility = View.VISIBLE

        } else {
            buttonNext.visibility = View.INVISIBLE
        }

    }


}