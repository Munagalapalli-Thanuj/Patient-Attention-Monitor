package com.example.patientattentionmonitor.fragment

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import com.example.patientattentionmonitor.R
import com.example.patientattentionmonitor.adapter.AppRecyclerAdapter
import com.example.patientattentionmonitor.databinding.FragmentHistoryOverviewBinding
import com.example.patientattentionmonitor.klass.navigateSafe
import com.example.patientattentionmonitor.klass.tryIgnore
import com.example.patientattentionmonitor.model.BaseModel
import com.example.patientattentionmonitor.model.HistoryOverallItem
import com.example.patientattentionmonitor.model.Pill
import com.example.patientattentionmonitor.viewmodel.MainViewModel
import com.example.patientattentionmonitor.viewmodel.history.HistoryOverviewViewModel

@AndroidEntryPoint
class HistoryOverviewFragment : Fragment(R.layout.fragment_history_overview) {

    private val model: HistoryOverviewViewModel by viewModels()
    private val mainModel: MainViewModel by activityViewModels()

    private val binding by viewBinding(FragmentHistoryOverviewBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appAdapter = AppRecyclerAdapter(
            null,
            getString(R.string.no_history),
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_hourglass_empty)
        )

        appAdapter.setOnItemClickListener { _, item -> onHistoryClicked(item) }

        binding.recyclerHistory.adapter = appAdapter

        model.historyStats.observe(viewLifecycleOwner) { result ->
            result
                .onSuccess { appAdapter.submitList(it) }
                .onFailure {
                    tryIgnore { (requireParentFragment() as HistoryFragment).disableTabs() }
                    appAdapter.submitList(listOf()) // Submit an empty list to show the adapter
                }
        }

        mainModel.shouldScrollUp.observe(viewLifecycleOwner) {
            if (isVisible) binding.recyclerHistory.smoothScrollToPosition(0)
        }

    }

    private fun onHistoryClicked(item: BaseModel) {
        if (item is Pill) {
            val directions = HistoryFragmentDirections.actionHistoryToFragmentHistoryView(item.id)
            findNavController().navigateSafe(directions, R.id.history)
        } else if (item is HistoryOverallItem) {
            val directions = HistoryFragmentDirections.actionHistoryToFragmentHistoryView(-1, true)
            findNavController().navigateSafe(directions, R.id.history)
        }
    }
}