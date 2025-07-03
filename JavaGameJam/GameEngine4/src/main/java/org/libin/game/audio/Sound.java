package org.libin.game.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    private Clip musicClip;
    private Clip effectClip;
    URL soundURL[] = new URL[50];

    public Sound(){
        soundURL[0] = getClass().getResource("/Sounds/Level1/level1.wav");
        soundURL[1] = getClass().getResource("/Sounds/SoundEffects/walk.wav");
        soundURL[2] = getClass().getResource("/Sounds/Menu/EndOfTheDemo/demo_end.wav"); //Todo
        soundURL[3] = getClass().getResource("/Sounds/Menu/MainMenu/echo_of_the_husk.wav"); //Todo
    }

    public void setMusic(int i) {
        stopMusic();
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i])) {
            musicClip = AudioSystem.getClip();
            musicClip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playMusicLoop() {
        if (musicClip != null) {
            musicClip.start();
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopMusic() {
        if (musicClip == null) return;
        musicClip.stop();
        musicClip.close();
        musicClip = null;
    }
    public void setEffect(int i) {
        stopEffect();
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i])) {
            effectClip = AudioSystem.getClip();
            effectClip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playEffectLoop() {
        if (effectClip != null) {
            effectClip.start();
            effectClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopEffect() {
        if (effectClip == null) return;
        effectClip.stop();
        effectClip.close();
        effectClip = null;
    }
}
