package com.kulloveth.covid19virustracker.ui.info

import androidx.annotation.RawRes

data class InfoSlide(val titleResource: String,
                     val detail:String,
                     @RawRes val logoResource: Int)
