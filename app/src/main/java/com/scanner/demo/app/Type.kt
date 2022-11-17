package com.scanner.demo.app

sealed class Type {

    companion object {
        const val KHMER_ID_TYPE = "Khmer ID"
        const val PASSPORT_TYPE = "Passport"
    }
}
