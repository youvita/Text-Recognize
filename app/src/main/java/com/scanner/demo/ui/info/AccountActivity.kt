package com.scanner.demo.ui.info

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.scanner.demo.R
import com.scanner.demo.app.Constants
import com.scanner.demo.app.parcelable
import com.scanner.demo.base.BaseActivity
import com.scanner.demo.databinding.ActivityAccountBinding
import com.scanner.demo.ui.info.model.AccountInfo

class AccountActivity: BaseActivity<ActivityAccountBinding>() {

    private val accountViewModel: AccountViewModel by viewModels()

    override fun getLayoutId() = R.layout.activity_account

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.account = accountViewModel
        setAccountInformation()
    }

    /**
     * get date from intent and pass to model
     * */
    private fun setAccountInformation() {
        val data = intent.parcelable<AccountInfo>(Constants.ACCOUNT_INFO_KEY)
        accountViewModel.setAccountInfo(data)
    }
}