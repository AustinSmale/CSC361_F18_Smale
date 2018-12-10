package com.mygdx.game.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioManager 
{
	/**
	 * Singleton reference to the instance of AudioManager
	 */
	public static final AudioManager instance = new AudioManager();
	private Music playingMusic;
	// singleton: prevent instantiation from other classes
	private AudioManager () { }
	/**
	 * A sequence of methods to play audio
	 * and music
	 * @param sound
	 */
	public void play (Sound sound)
	{
		play(sound, 1);
	}
	public void play (Sound sound, float volume) 
	{
		play(sound, volume, 1);
	}
	public void play (Sound sound, float volume, float pitch) 
	{
		play(sound, volume, pitch, 0);
	}
	public void play (Sound sound, float volume, float pitch,
			float pan)
	{
		if (!GamePreferences.instance.sound) return;
		sound.play(GamePreferences.instance.volSound * volume,
				pitch, pan);
	}

	/**
	 * stops the current song and plays the new one
	 * @param music
	 */
	public void play (Music music) 
	{
		stopMusic();
		playingMusic = music;
		if (GamePreferences.instance.music) {
			music.setLooping(true);
			music.setVolume(GamePreferences.instance.volMusic);
			music.play();
		}
	}
	/**
	 * stops the music currently playing
	 */
	public void stopMusic () 
	{
		if (playingMusic != null) playingMusic.stop();
	}
	
	/**
	 * pauses or plays music on setting window call
	 */
	public void onSettingsUpdated () 
	{
		if (playingMusic == null) return;
		playingMusic.setVolume(GamePreferences.instance.volMusic);
		if (GamePreferences.instance.music)
		{
			if (!playingMusic.isPlaying()) playingMusic.play();
		} 
		else 
		{
			playingMusic.pause();
		}
	}
}