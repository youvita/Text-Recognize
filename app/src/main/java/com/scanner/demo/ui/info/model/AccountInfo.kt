package com.scanner.demo.ui.info.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AccountInfo(

    var id_type: String? = null,

    var id_number: String? = null,

    var expiry_date: String? = null

): Parcelable
