package com.kulloveth.covid19virustracker.ui.info

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import com.kulloveth.covid19virustracker.R
import com.kulloveth.covid19virustracker.ui.MainActivity
import kotlinx.android.synthetic.main.activity_info.*
import kotlinx.android.synthetic.main.activity_info.buttonNext
import kotlinx.android.synthetic.main.fragment_onboarding.indicatorContainer
import java.util.*


class InfoActivity : AppCompatActivity(),InfoSideAdapter.PassText {

    private val TAG = InfoActivity::class.simpleName
    private var tts: TextToSpeech? = null
    private var textView:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)


        buttonNext.setOnClickListener {

            startActivity(Intent(this, MainActivity::class.java))
            this.finish()
            this.overridePendingTransition(
                R.anim.slide_in_right,
                R.anim.slide_out_left
            )
        }
        infoSliderViewPager.adapter = infoSideAdapter
        setupIndicators()
        setCurrentIndicator(0)
        infoSliderViewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentIndicator(position)
                }
            })
    }

    private val infoSideAdapter = InfoSideAdapter(
        listOf(
            InfoSlide(
                "Keep Your Hands Clean",
                "Clean your hands often. Use soap and water, or an alcohol-based hand rub.",
                "wash_hands.json"
            ),
            InfoSlide(
                "Protect your face",
                "Wear a mask when physical distancing is not possible.",
                "wear_mask.json"
            ),
            InfoSlide(
                "Stay at Home",
                "Stay home if you feel unwell.",
                "stay_home.json"
            ),

            InfoSlide(
                "Social Distancing",
                "Maintain a safe distance from anyone who is coughing or sneezing.",
                "distance.json"
            ),
            InfoSlide(
                "Seek Medical Attention",
                "If you have a fever, cough and difficulty breathing, seek medical attention.",
                "medical.json"
            )
        )

    ,this)

    private fun setupIndicators() {

        val indicators = arrayOfNulls<ImageView>(infoSideAdapter.itemCount)
        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indicators.indices) {

            indicators[i] = ImageView(this)
            indicators[i]?.apply {
                this.setImageDrawable(
                    ContextCompat.getDrawable(
                        this@InfoActivity,
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
                        this, R.drawable.indicator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.indicator_inactive
                    )
                )
            }

        }

        if (index == infoSideAdapter.itemCount - 1) {
            val animation = AnimationUtils.loadAnimation(this, R.anim.app_name_animation)
            buttonNext.animation = animation
            buttonNext.visibility = View.VISIBLE

        } else {
            buttonNext.visibility = View.GONE
        }

        when(index){
            0 -> speak()
            1 -> speak()
            2 -> speak()
            3 -> speak()
            4 -> speak()
        }
    }

    fun speak(){
        tts = TextToSpeech(this, TextToSpeech.OnInitListener {status->
            if (status == TextToSpeech.SUCCESS) {
                // set US English as language for tts
                val result = tts?.setLanguage(Locale.US)

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e(TAG,"The Language specified is not supported!")
                } else {
                    val text = textView!!.text.toString()
                    tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null,"")
                }

            } else {
                Log.e(TAG, "Initilization Failed!")
            }
        })
    }

    override fun onTextPass(textView: TextView) {
        this.textView = textView
    }

    override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()

}

}