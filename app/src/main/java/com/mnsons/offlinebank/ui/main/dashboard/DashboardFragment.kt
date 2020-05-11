package com.mnsons.offlinebank.ui.main.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.mnsons.offlinebank.databinding.FragmentDashboardBinding
import com.mnsons.offlinebank.model.transaction.TransactionType
import com.mnsons.offlinebank.ui.commons.adapters.transaction.SectionedTransactionsAdapter
import com.mnsons.offlinebank.ui.commons.adapters.transaction.TransactionsAdapter
import com.mnsons.offlinebank.ui.main.MainActivity
import com.mnsons.offlinebank.utils.ext.nonNullObserve
import javax.inject.Inject

class DashboardFragment : Fragment() {

    @Inject
    lateinit var dashboardViewModel: DashboardViewModel

    private val transactionsAdapter by lazy { TransactionsAdapter() }

    private val sectionedTransactionsAdapter by lazy {
        SectionedTransactionsAdapter(transactionsAdapter)
    }

    private lateinit var _binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding.rvSectionedTransactions.adapter = sectionedTransactionsAdapter

        nonNullObserve(dashboardViewModel.state, ::handleStates)
    }

    private fun handleStates(dashboardState: DashboardState) {
        when (dashboardState) {
            is DashboardState.Idle, is DashboardState.Editing -> {
                dashboardState.transactions?.let {
                    sectionedTransactionsAdapter.setTransactions(it)

                    val allTransactions = it.flatMap { it.transactions }

                    _binding.tvBankTransferAmount.text = allTransactions.filter {
                        it.type == TransactionType.BANK_TRANSFER
                    }.sumByDouble { it.amount }.toString()

                    _binding.tvAirtimePurchaseAmount.text = allTransactions.filter {
                        it.type == TransactionType.AIRTIME_PURCHASE
                    }.sumByDouble { it.amount }.toString()
                }
            }
            is DashboardState.Error -> {
                Snackbar.make(
                    _binding.root,
                    dashboardState.error?.message.toString(),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        injectDependencies()
    }

    private fun injectDependencies() {
        (requireActivity() as MainActivity).mainComponent.inject(this)
    }

}