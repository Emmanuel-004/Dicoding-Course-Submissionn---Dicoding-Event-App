package com.event.dicodingeventapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.event.dicodingeventapp.EventAdapter
import com.event.dicodingeventapp.databinding.FragmentHomeBinding
import com.event.dicodingeventapp.ui.dashboard.DashboardViewModel
import com.event.dicodingeventapp.ui.notifications.NotificationsViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var upcomingEventAdapter: EventAdapter
    private lateinit var finishedEventAdapter: EventAdapter
    private val upcomingViewModel: DashboardViewModel by viewModels()
    private val finishedViewModel: NotificationsViewModel by viewModels()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upcomingEventAdapter = EventAdapter()
        finishedEventAdapter = EventAdapter()


        binding.rvHomeUpcoming.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvHomeUpcoming.adapter = upcomingEventAdapter

        binding.rvHomeFinished.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHomeFinished.adapter = finishedEventAdapter

        upcomingViewModel.events.observe(viewLifecycleOwner) { eventList ->
            upcomingEventAdapter.submitList(eventList)
        }
        finishedViewModel.events.observe(viewLifecycleOwner) { eventList ->
            finishedEventAdapter.submitList(eventList)
        }

        upcomingViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }
        finishedViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        upcomingViewModel.getEvents("")
        finishedViewModel.getFinishedEvents("")
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvHomeUpcoming.visibility = View.GONE
            binding.rvHomeUpcoming.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvHomeFinished.visibility = View.VISIBLE
            binding.rvHomeUpcoming.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}