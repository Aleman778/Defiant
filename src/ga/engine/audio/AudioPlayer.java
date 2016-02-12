package ga.engine.audio;

import ga.engine.scene.GameComponent;
import java.util.List;
import java.util.Map;
import javafx.scene.media.AudioClip;

public class AudioPlayer extends GameComponent {
    
    private boolean looping;
    private final AudioClip player;

    public AudioPlayer() {
        this.player = null;
        this.looping = false;
    }
    
    public AudioPlayer(String filepath) {
        player = new AudioClip(AudioPlayer.class.getResource("/" + filepath).toExternalForm());
        looping = false;
    }
    
    public void play() {
        if (isPlaying())
            return;
        setLooping(looping);
        player.play();
    }
    
    public void stop() {
        if (!isPlaying())
            return;
        player.stop();
    }
    
    public boolean isPlaying() {
        return player.isPlaying();
    }
    
    public void setLooping(boolean loop) {
        this.looping = loop;
        if (loop)
            player.setCycleCount(AudioClip.INDEFINITE);
        else
            player.setCycleCount(player.getCycleCount());
        System.out.println(player.getCycleCount());
    }
    
    public boolean isLooping() {
        return looping;
    }

    @Override
    public List<String> getAttributes() {
        return ATTRIBUTES_NONE;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
    }
    
    @Override
    public GameComponent instantiate() {
        return new AudioPlayer();
    }
}
