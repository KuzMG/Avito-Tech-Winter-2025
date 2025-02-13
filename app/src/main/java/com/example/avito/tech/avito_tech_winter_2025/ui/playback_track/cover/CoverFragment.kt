package com.example.avito.tech.avito_tech_winter_2025.ui.playback_track.cover

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.avito.tech.avito_tech_winter_2025.R
import com.example.avito.tech.avito_tech_winter_2025.databinding.FragmentCoverBinding
import com.squareup.picasso.Picasso

private const val ARG_COVER = "cover"
class CoverFragment : Fragment() {

    private lateinit var binding: FragmentCoverBinding
    private var cover = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            cover = it.getString(ARG_COVER) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCoverBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Picasso.get().load(cover).into(binding.imageView)
    }
    companion object {
        @JvmStatic
        fun newInstance(cover: String) =
            CoverFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_COVER, cover)
                }
            }
    }
}