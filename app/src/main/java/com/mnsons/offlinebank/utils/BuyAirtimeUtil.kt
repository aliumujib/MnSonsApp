package com.mnsons.offlinebank.utils

object BuyAirtimeUtil {

    fun getActionIdByBankId(id: Int): String = hashMapOfActions[id]!!

    private val hashMapOfActions = hashMapOf(
        1 to "58f263f5",
        2 to "1fb3b56e"
    )

}

