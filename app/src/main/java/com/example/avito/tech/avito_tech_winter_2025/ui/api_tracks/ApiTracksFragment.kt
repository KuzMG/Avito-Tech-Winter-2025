package com.example.avito.tech.avito_tech_winter_2025.ui.api_tracks

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.avito.tech.avito_tech_winter_2025.R
import com.example.avito.tech.avito_tech_winter_2025.api.dto.model.Track
import com.example.avito.tech.avito_tech_winter_2025.ui.api_tracks.data.Status
import com.example.avito.tech.avito_tech_winter_2025.ui.playback_track.PlaybackTrackFragment
import com.example.avito.tech.internship.utils.appComponent
import com.example.avito.tech.ui.RecyclerViewFragment
import com.example.avito.tech.ui.TrackViewHolder
import com.example.avito.tech.ui.TracksAdapter
import com.squareup.picasso.Picasso

class ApiTracksFragment : RecyclerViewFragment() {

    private val viewModel by viewModels<ApiTracksViewModel> {
        appComponent.multiViewModelFactory
    }

    override fun searchListener(searchView: SearchView) {
        searchView.setQuery(viewModel.query, false)
        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String): Boolean {
                    return true
                }

                override fun onQueryTextChange(p0: String): Boolean {
                    viewModel.query = p0
                    return true
                }
            })
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.refreshButton.setOnClickListener{
            viewModel.refresh()
        }
        viewModel.tracksLiveData.observe(viewLifecycleOwner) { res ->
            res.run {
                error?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
                tracks?.let {
                    binding.recyclerView.adapter = ApiTrackAdapter(it)
                }
                binding.progressIndicator.visibility = isVisible(status == Status.LOADING)
                binding.recyclerView.visibility  = isVisible(status == Status.OK)
                binding.refreshButton.visibility  = isVisible(status == Status.ERROR)
            }
        }
    }
    private fun isVisible(flag: Boolean) = if (flag) {
        View.VISIBLE
    } else {
        View.GONE
    }
    class ApiTrackAdapter(private val tracks: List<Track>) :
        TracksAdapter<Track>(tracks) {
        override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            holder.binding.run {
                Picasso.get().load(tracks[position].album.cover).placeholder(android.R.color.darker_gray).error(R.drawable.default_track).into(imageView)
                titleTextView.text = tracks[position].title
                artistTextView.text = tracks[position].artist.name
                root.setOnClickListener {
                    val bundle = Bundle().apply {
                        putLong(PlaybackTrackFragment.ARG_ID_TRACK, 122)
                        putParcelableArray(PlaybackTrackFragment.ARG_TRACKS, tracks.toTypedArray())
                        putInt(PlaybackTrackFragment.ARG_POSITION, position)
                        putBoolean(PlaybackTrackFragment.ARG_INTERNET,true)
                    }
                    root.findNavController()
                        .navigate(R.id.playback_track_fragment,bundle)
                }
            }
        }
    }
}