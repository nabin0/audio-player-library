package com.example.audioplayerlibaray.audioplayer

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.audioplayerlibaray.R
import com.nabin0.audioplayer.view.AudioPlayer
import com.vl.vlplayer.demo.audioplayer.Utils

/**
 * A simple [Fragment] subclass.
 * Use the [PlayerDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PlayerDetailFragment : Fragment() {
    private var selectedIndex: Int = 0
    var audioPlayer: AudioPlayer? = null
    lateinit var containerAudioView: FrameLayout
    private var spinner: Spinner? = null
    private val customLayoutSampleList =
        listOf("Default Layout", "Sample Layout 1", "Sample Layout 2", "Sample Layout 3")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val receivedData = arguments?.getLong("key_data", 0)
        selectedIndex = receivedData?.toInt() ?: 0
        val view = inflater.inflate(R.layout.fragment_full_player, container, false)
        containerAudioView = view.findViewById(R.id.containerAudioView)

        // containerAudioView.removeAllViews()

        // audioPlayer = AudioPlayerUtil.getAudioPlayer(requireActivity())

        audioPlayer = view.findViewById(R.id.audioPlayer)
        audioPlayer?.setChromecastEnabled(true)

// ------ Testing ------

//        audioPlayer?.setActivityInstance(requireActivity())
//        containerAudioView.addView(audioPlayer)
//        audioPlayer = view.findViewById(R.id.audioPlayer)
//        val audioPlayer1: AudioPlayer = view.findViewById(R.id.audioPlayer)
//
//        val par = audioPlayer1.parent as ViewGroup
//        par.removeAllViews()
//        audioPlayer = AudioPlayerUTil.getAudioPlayer(requireActivity())
//        audioPlayer.layoutParams = ViewGroup.LayoutParams
//        par.addView(audioPlayer)
//        audioPlayer?.setChromecastEnabled(true)7
        // audioPlayer?.initializePlayer()
        // audioPlayer?.setPlaylist(Utils.sampleAudioList)
        // audioPlayer?.prepare()


        audioPlayer?.setAutoPlayEnabled(true)
        audioPlayer?.playMediaItemByIndex(selectedIndex)
        audioPlayer?.startForegroundService()

        Utils.SHOW_MINI_PLAYER = true

        spinner = view.findViewById(R.id.customLayoutsSpinner)
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
                Log.d("TAG", "onItemSelected: $position")
                val layoutId: Int = when (position) {
                    0 -> com.nabin0.audio_player.R.layout.audio_player_control // This is default layout no need to apply manually
                    1 -> R.layout.audio_player_sample_layout_1
                    2 -> R.layout.audio_player_sample_layout_2
                    3 -> R.layout.audio_player_sample_layout_3
                    else -> com.nabin0.audio_player.R.layout.audio_player_control
                }
                audioPlayer?.setCustomLayout(layoutId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        return view
    }

    override fun onDestroy() {
        // audioPlayer?.destroy()
        super.onDestroy()
    }


}