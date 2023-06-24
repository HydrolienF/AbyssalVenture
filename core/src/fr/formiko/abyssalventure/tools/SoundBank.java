package fr.formiko.abyssalventure.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundBank {
    private SoundBank() {}

    public static final Sound eat = Gdx.audio.newSound(Gdx.files.internal("sounds/eat.mp3"));
    public static final Sound win = Gdx.audio.newSound(Gdx.files.internal("sounds/win 2.mp3"));
    public static final Sound lose = Gdx.audio.newSound(Gdx.files.internal("sounds/lose.mp3"));
    public static final Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));

}
