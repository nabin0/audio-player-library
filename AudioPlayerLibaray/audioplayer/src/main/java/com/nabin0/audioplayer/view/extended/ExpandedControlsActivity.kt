package com.nabin0.audioplayer.view.extended

import android.view.Menu
import com.google.android.gms.cast.framework.media.widget.ExpandedControllerActivity

class ExpandedControlsActivity : ExpandedControllerActivity() {
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        return true
    }
}