package com.scanner.demo.ui.info

import androidx.lifecycle.ViewModel
import com.scanner.demo.ui.info.model.AccountInfo

class AccountViewModel: ViewModel() {

    var accountInfo = AccountInfo()

    @JvmName("setAccountInfo1")
    fun setAccountInfo(info: AccountInfo?) {
        info?.let {
            accountInfo = it
        }
    }
}