package com.event.dicodingeventapp.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.event.dicodingeventapp.EventAdapter
import com.event.dicodingeventapp.data.response.ListEventsItem
import com.event.dicodingeventapp.databinding.FragmentNotificationsBinding
//import com.google.android.material.search.SearchView
import androidx.appcompat.widget.SearchView


class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var eventAdapter: EventAdapter
    private val eventViewModel: NotificationsViewModel by viewModels()

    private var eventList: List<ListEventsItem> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        eventAdapter = EventAdapter()
        binding.rvFinished.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinished.adapter = eventAdapter

        eventViewModel.events.observe(viewLifecycleOwner) { eventList ->
            eventAdapter.submitList(eventList)
        }

        eventViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                filterEvents(newText.orEmpty())
                return true
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
        })

        eventViewModel.getFinishedEvents("")
    }

    private fun filterEvents(query: String) {
        val filteredList = if (query.isEmpty()) {
            eventList
        } else {
            eventList.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }
        eventViewModel.getFinishedEvents(query)
        eventAdapter.submitList(filteredList)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
            binding.rvFinished.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.rvFinished.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}