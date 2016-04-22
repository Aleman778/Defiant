package ga.engine.audio;

import ga.engine.resource.ResourceManager;
import ga.engine.scene.GameComponent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.media.AudioClip;

public class AudioController extends GameComponent {

    private static final List<String> ATTRIBUTES = new ArrayList<>();
    private Map<String, AudioClip[]> effects = new HashMap<>();

    static {
        ATTRIBUTES.add("Audio Effects");
    }
    
    public void play(String effect) {
        AudioClip[] clips = effects.get(effect);
        clips[(int) (Math.random() * clips.length)].play();
    }

    @Override
    public List<String> getAttributes() {
        return ATTRIBUTES;
    }

    @Override
    public void setAttributes(Map<String, String> attributes) {
        String[] effects = attributes.get("Audio Effects").split(";");
        for (String effect : effects) {
            String key = effect.split(":")[0];
            AudioClip[] clips = new AudioClip[(effect.split(":")[1]).split(",").length];
            int index = 0;
            for (String value : (effect.split(":")[1]).split(",")) {
                clips[index] = ResourceManager.getAudio(value);
                index++;
            }
            this.effects.put(key, clips);
        }
        System.out.println("");
    }

    @Override
    public GameComponent instantiate() {
        return new AudioController();
    }

}
