
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

`implementation 'com.example:youraudioplayer:1.0.0'`


