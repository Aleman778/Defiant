package ga.engine.audio;

import ga.engine.scene.GameComponent;
import java.util.List;
import java.util.Map;
import javafx.scene.media.AudioClip;

public class AudioPlayer extends GameComponent implements Runnable {
    
    private boolean looping;
    private final AudioClip player;
    
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
    public void run() {
    }

    @Override
    public GameComponent instantiate() {
        return null;
    }

    @Override
    public Map<String, Integer> getVars() {
        return null;
    }
    
    @Override
    public void xmlVar(String name, String value) {
    }
}
