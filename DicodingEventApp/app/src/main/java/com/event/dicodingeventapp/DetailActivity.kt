package com.event.dicodingeventapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.event.dicodingeventapp.data.response.Event
import com.event.dicodingeventapp.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)


        detailViewModel.event.observe(this) { event ->
            setEventDetail(event)
        }

        detailViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        val eventId = intent.getStringExtra("EVENT_ID")
        if (eventId != null) {
            detailViewModel.getDetailEvents(eventId)
        }

        binding.btnRegister.setOnClickListener {
            val event = detailViewModel.event.value
            event?.let {
                register(it.link)
            }
        }
    }
    private fun setEventDetail(event: Event) {
        Glide.with(binding.root.context)
            .load(event.mediaCover)
            .into(binding.ivDetailImage)
        binding.tvEventName.text = event.name
        binding.tvEventOwner.text = event.ownerName
        binding.tvEventDate.text = event.beginTime

        val sisaQuota = event.quota - event.registrants

        binding.tvEventQuota.text = sisaQuota.toString()
        binding.tvEventDescription.text = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_COMPACT)
    }
    private fun register(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }
    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
