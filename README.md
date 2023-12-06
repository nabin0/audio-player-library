
# Project Title

A brief description of what this project does and who it's for


# Audioplayer Library

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

Audioplayer Library is an Android audio player library built on top of Media3 ExoPlayer. It offers default layouts for fullscreen and mini bottom screen players, as well as comprehensive audio control functionalities. The library allows users to pass a custom layout for a personalized experience.

## Features

- **Media3 ExoPlayer Integration:** Utilizes Media3 ExoPlayer for seamless audio playback.
- **Default Layouts:** Provides default layouts for fullscreen and mini bottom screen players.
- **Custom Layout Support:** Allows users to pass a custom layout for a personalized player interface.
- **Audio Control Functions:** Includes functions for play, pause, stop, seek, and more.

## Installation

To use the Audioplayer Library in your Android project, add the following dependency to your root `build.gradle` file:
```
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}

```
Implement library in your app level build.gradle:

```
dependencies {
	implementation 'com.github.nabin0:audio-player-library:Tag'
}
```

# Implementing the library

1) In your application create an Audioplayer object.(Audioplayer-library creates singleton Media3 instance under the hood so even if you create multiple Audioplayer instance there will be only one ExoPlayer object to create new one you should call `Audioplayer.destroy()`)
	
```
	    audioPlayer = AudioPlayer(context)
        audioPlayer?.initializePlayer()
        audioPlayer?.setPlaylist(your audio list)
        audioPlayer?.prepare()

        audioPlayer?.removeCallbacks() // Needs to be called if you are not creating singleton audioPlayer instance or using it as view in your application
```

2) Use AudioPlayer as view in xml.Also use can use that view to store in variable and call Audioplayer methods

```
<com.github.nabin0.audioplayer.view.AudioPlayer
            android:id="@+id/audioPlayer"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/customLayoutsSpinner" />

```
 
3) If you want to set custom layout you need to follow some rules that id and view type should be same as used in default layout. here are the id viewtype and funciton of that view used in audioplayer library.

| viewId              	| view type            	| function of view                                                         	|
|---------------------	|----------------------	|--------------------------------------------------------------------------	|
| albumImage          	| AppCompatImageView   	| it is used to show album art image                                       	|
| textAudioTitle      	| TextView             	| shows audio title                                                        	|
| textArtist          	| TextView             	| shows audio artist                                                       	|
| audioSeekBar        	| SeekBar              	| shows audio progress                                                     	|
| textCurrentPosition 	| TextView             	| shows current progress value                                             	|
| textAudioDuration   	| TextView             	| shows total audio duration                                               	|
| previousButton      	| AppCompatImageButton 	| control to skip to previous audio                                        	|
| progressBar         	| ProgressBar          	| visible when buffering online audio                                      	|
| playButton          	| AppCompatImageButton 	| play audio                                                               	|
| pauseButton         	| AppCompatImageButton 	| pause the current playing audio                                          	|
| nextButton          	| AppCompatImageButton 	| skip to next audio                                                       	|
| playBackModeButton  	| AppCompatImageButton 	| no need to provide any drawable just set background to transparent color 	|

---

4) To destroy or release audioplayer resource call `audioplayer.destroy()`

## Demo
---
<p align="center">Download the demo app  <a href="https://github.com/nabin0/audio-player-library/releases/download/1.0.1/app-debug.apk"><nobr>here</nobr></a></p>

### Feel Free to use and update or have any suggestion drop an <a href="mailto:nabinbhatt62@gmail.com">email here</a>
 
