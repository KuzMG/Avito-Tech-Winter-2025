package com.example.avito.tech.avito_tech_winter_2025.ui.api_tracks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.avito.tech.avito_tech_winter_2025.databinding.FragmentNotificationsBinding
import com.example.avito.tech.avito_tech_winter_2025.ui.downloaded_tracks.data.Track
import com.example.avito.tech.ui.RecyclerViewFragment
import com.example.avito.tech.ui.TrackViewHolder
import com.example.avito.tech.ui.TracksAdapter
import com.squareup.picasso.Picasso

class APITracksFragment : RecyclerViewFragment() {
    override fun searchListener(searchView: SearchView) {

    }

    class APITrackAdapter(private val tracks: List<Track>) :
        TracksAdapter<Track>(tracks) {
        override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.run {
                Picasso.get().load(tracks[position].pic).into(imageView)
                titleTextView.text = tracks[position].title
                authorTextView.text = tracks[position].author
            }
        }
    }
}