package com.mnsons.offlinebank.ui.main.profile.addbank

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mnsons.offlinebank.data.cache.impl.BanksCache
import com.mnsons.offlinebank.data.cache.impl.SettingsCache
import com.mnsons.offlinebank.model.*
import com.mnsons.offlinebank.model.bank.BankModel
import com.mnsons.offlinebank.model.user.UserModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddBankViewModel @ViewModelInject constructor(
    val settingsCache: SettingsCache,
    val banksCache: BanksCache
) : ViewModel() {

    private val _state = MutableLiveData<AddBankState>()
    val state: LiveData<AddBankState> = _state

    init {
        if (settingsCache.userDataExists()) {
            banksCache.getBanks()
                .onEach { banks ->
                    _state.value = AddBankState.Idle(
                        UserModel(
                            settingsCache.fetchUserFirstName()!!,
                            settingsCache.fetchUserLastName()!!,
                            settingsCache.fetchUserPhone()!!,
                            banks.mapInto {
                                it.toBankModel()
                            }
                        )
                    )
                }.launchIn(viewModelScope)
        }
    }

    fun updateUserBanks(banks: List<BankModel>) {
        if (banks.isEmpty()) {
            _state.value = AddBankState.Error(Throwable("Please select at least one bank"))
        } else {
            if (settingsCache.userDataExists()) {
                _state.value = AddBankState.Editing(
                    UserModel(
                        settingsCache.fetchUserFirstName()!!,
                        settingsCache.fetchUserLastName()!!,
                        settingsCache.fetchUserPhone()!!,
                        banks
                    )
                )

                viewModelScope.launch {
                    banksCache.clearBanks()
                    banksCache.saveBanks(
                        banks.mapInto {
                            it.toBankCacheModel()
                        }
                    )
                }
            } else {
                _state.value = AddBankState.Error(Throwable("There was an error adding new banks"))
            }
        }
    }
}