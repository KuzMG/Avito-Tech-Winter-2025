package com.example.avito.tech.avito_tech_winter_2025.ui.playback_track

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.avito.tech.avito_tech_winter_2025.R
import com.example.avito.tech.avito_tech_winter_2025.databinding.FragmentPlaybackTrackBinding
import com.example.avito.tech.avito_tech_winter_2025.page_transformer.ZoomOutPageTransformer
import com.example.avito.tech.avito_tech_winter_2025.ui.playback_track.cover.CoverFragment
import com.example.avito.tech.internship.utils.appComponent
import com.example.avito.tech.internship.utils.getParcelableArrayCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.squareup.picasso.Picasso


class PlaybackTrackFragment : Fragment() {
    companion object {
        const val ARG_ID_TRACK = "id"
        const val ARG_TRACKS = "tracks"
        const val ARG_POSITION = "position"
    }

    private lateinit var binding: FragmentPlaybackTrackBinding

    private val viewModel by viewModels<PlaybackTracksViewModel> {
        appComponent.playbackTrackFactory.create(
            arguments?.getLong(ARG_ID_TRACK) ?: 0L,
            arguments?.getParcelableArrayCompat(ARG_TRACKS) ?: arrayOf(),
            arguments?.getInt(ARG_POSITION) ?: 0
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlaybackTrackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.GONE

        binding.viewPager?.run {
            offscreenPageLimit = 1
            setPadding(95, 0, 95, 0)
            setPageTransformer(ZoomOutPageTransformer())
            adapter = CoverSlidePagerAdapter(this@PlaybackTrackFragment)
            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    visibilityTrack(position)
                    viewModel.position = position

                }
            })
            setCurrentItem(viewModel.position, false)
        }
        binding.prevButtom.setOnClickListener {
            if (viewModel.position > 0) viewModel.position--
            binding.viewPager?.setCurrentItem(viewModel.position, true)
            visibilityTrack(viewModel.position)
        }
        binding.nextButtom.setOnClickListener {
            if (viewModel.position < viewModel.tracks.size-1) viewModel.position++
            binding.viewPager?.setCurrentItem(viewModel.position, true)
            visibilityTrack(viewModel.position)
        }
        binding.closeButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        visibilityTrack(viewModel.position)
    }

    private fun visibilityTrack(position: Int) {
        viewModel.tracks[position].run {
            binding.titleTextView.text = title
            binding.artistTextView.text = artist.name
            binding.albumTextView.text = getString(R.string.album, album.title)
            binding.imageView?.let {
                Picasso.get().load(album.cover).into(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility =
            View.VISIBLE
    }

    private inner class CoverSlidePagerAdapter(f: Fragment) : FragmentStateAdapter(f) {
        override fun getItemCount(): Int = viewModel.tracks.size

        override fun createFragment(position: Int): Fragment =
            CoverFragment.newInstance(viewModel.tracks[position].album.cover)
    }
}