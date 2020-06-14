package ua.alxmute.migratemusic.activity

import android.content.res.Resources
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_file_processing.*
import ua.alxmute.migratemusic.R
import ua.alxmute.migratemusic.activity.presenter.FileProcessingPresenter
import ua.alxmute.migratemusic.activity.view.FileProcessingView
import ua.alxmute.migratemusic.adapter.TrackRecyclerViewAdapter
import ua.alxmute.migratemusic.data.ProcessedTrackDto
import javax.inject.Inject
import kotlin.concurrent.thread


class FileProcessingActivity : DaggerAppCompatActivity(), FileProcessingView {

    @Inject
    lateinit var fileProcessingPresenter: FileProcessingPresenter

    private val processedTracks = ArrayList<ProcessedTrackDto>()

    private var trackRecyclerViewAdapter: TrackRecyclerViewAdapter = TrackRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_processing)

        trackRecyclerViewAdapter.tracks = processedTracks
        trackRecyclerViewAdapter.context = this

        rcProcessedTracks.adapter = trackRecyclerViewAdapter
        rcProcessedTracks.layoutManager = LinearLayoutManager(this)

        failureSwitch.setOnClickListener {
            failureSwitch()
        }

        thread {
            fileProcessingPresenter.onload()
        }
    }

    override fun refreshList() {
        runOnUiThread {
            trackRecyclerViewAdapter.notifyDataSetChanged()
        }
    }

    override fun setTrackCounter(text: String) {
        runOnUiThread {
            trackCounter.text = text
        }
    }

    override fun getResources(): Resources {
        return super.getResources()
    }

    override fun getListForProcessedTracks(): MutableList<ProcessedTrackDto> {
        return processedTracks
    }

    override fun setFailureTrackCounter(text: String) {
        runOnUiThread {
            failureSwitch.text = text
        }
    }

    fun failureSwitch() {
        val currentTracks = rcProcessedTracks
        val count = currentTracks.childCount
        if (failureSwitch.isChecked) {
            for (i in 0 until count) {
                val view: View = currentTracks.getChildAt(i)
                if (view.tag == "success") {
                    view.visibility = View.GONE

                }
            }
        } else {
            for (i in 0 until count) {
                val view: View = currentTracks.getChildAt(i)
                if (view.tag == "success") {
                    view.visibility = View.VISIBLE
                }
            }

        }
    }


}
