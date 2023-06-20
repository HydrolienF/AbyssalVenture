package fr.formiko.abyssalventure.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.Map;

public class SoundBank {
    private SoundBank() {}
    
    public static final Sound eat = Gdx.audio.newSound(Gdx.files.internal("sounds/eat.mp3"));
}
