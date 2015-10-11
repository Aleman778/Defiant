package ga.engine.audio;

import ga.engine.scene.GameComponent;
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
}
