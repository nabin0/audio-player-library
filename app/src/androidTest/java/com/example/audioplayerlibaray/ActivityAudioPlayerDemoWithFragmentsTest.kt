package com.example.audioplayerlibaray

import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.audioplayerlibaray.audioplayer.ActivityAudioPlayerDemo
import org.hamcrest.Matchers.anything
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActivityAudioPlayerDemoWithFragmentsTest {
    @get:Rule
    var activityScenarioRule = activityScenarioRule<ActivityAudioPlayerDemo>()

    @Before
    fun setUp() {
        activityScenarioRule.scenario.moveToState(Lifecycle.State.RESUMED)
    }

    @After
    fun cleanup() {
        activityScenarioRule.scenario.close()
    }

    @Test
    fun runToCheckTestClassWorking() {
    }

    @Test
    fun isFragmentContainerDisplayed() {
        onView(withId(R.id.fragmentContainer))
            .check(matches(isDisplayed()))
    }

    @Test
    fun isListCurrentlyVisible() {
        onView(withId(R.id.audioListView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun isBottomMiniPlayerVisibilityGone() {
        onView(withId(R.id.audioMiniPlayer)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    @Test
    fun isBottomMiniPlayerClickable() {
        onView(withId(R.id.audioMiniPlayer)).check(matches(ViewMatchers.isClickable()))
    }

    @Test
    fun inListItemClickReplaceWithPlayerDetailFragment() {
        onData(anything()).inAdapterView(withId(R.id.audioListView)).atPosition(0).perform(click())
        onView(withId(R.id.containerAudioView)).check(matches(isDisplayed()))
    }

    @Test
    fun isAudioPlayerItemVisibleOnFragmentDetailActivity() {
        onData(anything()).inAdapterView(withId(R.id.audioListView)).atPosition(0).perform(click())
        onView(withId(R.id.textAudioTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.albumImage)).check(matches(isDisplayed()))
        onView(withId(R.id.textArtist)).check(matches(isDisplayed()))
    }

    @Test
    fun isOnClickOnFirstItemPlayingSameItemInAudioDetailFragment() {
        val firstItemTitle = "Ringtone 1"
        onData(anything()).inAdapterView(withId(R.id.audioListView)).atPosition(0)
            .check(matches(withText(firstItemTitle))).perform(click())
        onView(withId(R.id.textAudioTitle)).check(matches(withText(firstItemTitle)))
    }

    @Test
    fun checkBottomMiniPlayerVisibleAfterPlayingAudio() {
        onData(anything()).inAdapterView(withId(R.id.audioListView)).atPosition(0).perform(click())
        pressBack()
        onView(withId(R.id.audioMiniPlayer)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun onSetLayoutOfAudioPlayerInAudioPlayerFullScreenChangesTheView() {
        onData(anything()).inAdapterView(withId(R.id.audioListView)).atPosition(0).perform(click())
        onData(anything()).inAdapterView(withId(R.id.customLayoutsSpinner)).atPosition(3)
            .perform(click())
        try {
            onView(withId(R.id.audioSeekBar)).check(matches(not(isDisplayed())))
        } catch (_: NoMatchingViewException) {
        }
    }

    @Test
    fun onSetLayoutOfMiniPlayerSetsNewLayout() {
        onData(anything()).inAdapterView(withId(R.id.audioListView)).atPosition(0).perform(click())
        pressBack()
        onData(anything()).inAdapterView(withId(R.id.audioListView)).atPosition(1).perform(click())
        onView(withId(R.id.audioSeekBar)).check(matches(isDisplayed()))
    }

    @Test
    fun onClickBottomMiniPlayerOpeningDetailPlayerScreen(){
        onData(anything()).inAdapterView(withId(R.id.audioListView)).atPosition(0).perform(click())
        pressBack()
        onView(withId(R.id.audioMiniPlayer)).check(matches(isDisplayed()))
            .check(matches(isClickable()))
            .perform(click())
        onView(withId(R.id.audioPlayer)).check(matches(isDisplayed()))

    }

    @Test
    fun isPlayPauseButtonToggleWorking(){

    }
}