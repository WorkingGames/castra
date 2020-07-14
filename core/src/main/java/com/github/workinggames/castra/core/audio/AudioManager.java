package com.github.workinggames.castra.core.audio;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

@RequiredArgsConstructor
public class AudioManager
{
    private final boolean humbleAssetsPresent;

    @Getter
    private float musicVolume = 0.3f;

    @Getter
    private float soundVolume = 0.3f;

    private Music mainMenuMusic;
    private Music gameMusic;
    private Sound victory;
    private Sound defeat;
    private Sound charge;
    private Sound click;
    private Sound battleStarted;
    private Sound settlementCaptured;
    private Sound settlementLost;

    public void initializeSounds()
    {
        if (humbleAssetsPresent)
        {
            victory = Gdx.audio.newSound(Gdx.files.internal("humble-assets/sounds/Victory.mp3"));
            defeat = Gdx.audio.newSound(Gdx.files.internal("humble-assets/sounds/Defeat.mp3"));
            charge = Gdx.audio.newSound(Gdx.files.internal("humble-assets/sounds/Charge.mp3"));
            click = Gdx.audio.newSound(Gdx.files.internal("humble-assets/sounds/Click.mp3"));
            battleStarted = Gdx.audio.newSound(Gdx.files.internal("humble-assets/sounds/BattleStart.mp3"));
            settlementCaptured = Gdx.audio.newSound(Gdx.files.internal("humble-assets/sounds/SettlementCaptured.mp3"));
            settlementLost = Gdx.audio.newSound(Gdx.files.internal("humble-assets/sounds/SettlementLost.mp3"));
        }
    }

    public void updateMusicVolume(float volume)
    {
        musicVolume = volume;
        mainMenuMusic.setVolume(musicVolume);
    }

    public void updateSoundVolume(float volume)
    {
        soundVolume = volume;
    }

    public void playMainMenuMusic()
    {
        if (humbleAssetsPresent)
        {
            mainMenuMusic = Gdx.audio.newMusic(Gdx.files.internal("humble-assets/music/MainMenu.mp3"));
            mainMenuMusic.setLooping(true);
            mainMenuMusic.setVolume(musicVolume);
            mainMenuMusic.play();
        }
    }

    public void playGameMusic()
    {
        if (humbleAssetsPresent)
        {
            gameMusic = Gdx.audio.newMusic(Gdx.files.internal("humble-assets/music/Game.mp3"));
            gameMusic.setLooping(true);
            gameMusic.setVolume(musicVolume);
            gameMusic.play();
        }
    }

    public void stopMainMenuMusic()
    {
        if (humbleAssetsPresent)
        {
            mainMenuMusic.dispose();
        }
    }

    public void stopGameMusic()
    {
        if (humbleAssetsPresent)
        {
            gameMusic.dispose();
        }
    }

    public void playVictorySound()
    {
        if (humbleAssetsPresent)
        {
            victory.play(soundVolume);
        }
    }

    public void playDefeatSound()
    {
        if (humbleAssetsPresent)
        {
            defeat.play(soundVolume);
        }
    }

    public void playChargeSound()
    {
        if (humbleAssetsPresent)
        {
            charge.play(soundVolume);
        }
    }

    public void playClickSound()
    {
        if (humbleAssetsPresent)
        {
            click.play(soundVolume);
        }
    }

    public void playBattleStartedSound()
    {
        if (humbleAssetsPresent)
        {
            battleStarted.play(soundVolume);
        }
    }

    public void playSettlementCapturedSound()
    {
        if (humbleAssetsPresent)
        {
            settlementCaptured.play(soundVolume);
        }
    }

    public void playSettlementLostSound()
    {
        if (humbleAssetsPresent)
        {
            settlementLost.play(soundVolume);
        }
    }

    public void disposeSounds()
    {
        if (humbleAssetsPresent)
        {
            victory.dispose();
            defeat.dispose();
            charge.dispose();
            click.dispose();
            battleStarted.dispose();
            settlementCaptured.dispose();
            settlementLost.dispose();
        }
    }
}