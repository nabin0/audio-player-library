package com.example.audioplayerlibaray.audioplayer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.audioplayerlibaray.R
import com.github.nabin0.audioplayer.view.AudioMiniPlayer

class AudioListFragment : Fragment() {
    private lateinit var listView: ListView
    private lateinit var audioMiniPlayer: AudioMiniPlayer
    private var spinner: Spinner? = null

    private val customLayoutSampleList =
        listOf("Default Layout", "Sample Layout 1", "Sample Layout 2")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_audio_list, container, false)
        listView = view.findViewById(R.id.audioListView)
        audioMiniPlayer = view.findViewById(R.id.audioMiniPlayer)
        audioMiniPlayer.setCustomLayout(R.layout.audio_mini_player_sample_layout_1)
        spinner = view.findViewById(R.id.customLayoutsSpinner)

        if (Utils.SHOW_MINI_PLAYER) {
            spinner?.visibility = View.VISIBLE
            audioMiniPlayer.visibility = View.VISIBLE
        } else {
            spinner?.visibility = View.GONE
            audioMiniPlayer.visibility = View.GONE
        }

        audioMiniPlayer.setOnClickListener {
            val bundle = Bundle()
            bundle.putLong("key_data", audioMiniPlayer.currentPlayingAudioIndex().toLong() ?: 0L)
            val playerDetailFragment = PlayerDetailFragment()
            playerDetailFragment.arguments = bundle

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, playerDetailFragment, "player_detail_fragment")
                ?.addToBackStack(null)
                ?.commit()
        }

        // Set spinner for listing layout
        val adapter = ArrayAdapter(
            this.requireActivity(),
            android.R.layout.simple_spinner_item,
            customLayoutSampleList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner?.adapter = adapter

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val layoutId: Int = when (position) {
                    0 -> com.github.nabin0.audioplayer.R.layout.audio_mini_player_layout// This is default layout no need to apply manually
                    1 -> R.layout.audio_mini_player_sample_layout_1
                    2 -> R.layout.audio_mini_player_sample_layout_2
                    else -> com.github.nabin0.audioplayer.R.layout.audio_mini_player_layout
                }
                audioMiniPlayer.setCustomLayout(layoutId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        val arrayAdapter = ArrayAdapter(
            this.requireActivity(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            Utils.sampleAudioList.map {
                it.title
            }
        )
        listView.adapter = arrayAdapter

        listView.setOnItemClickListener { _, view, position, id ->
            Utils.CURRENT_AUDIO_INDEX = position
            val bundle = Bundle()
            bundle.putLong("key_data", position.toLong())
            val playerDetailFragment = PlayerDetailFragment()
            playerDetailFragment.arguments = bundle

            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, playerDetailFragment, "player_detail_fragment")
                ?.addToBackStack(null)
                ?.commit()
        }
        return view
    }
}