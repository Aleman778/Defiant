package ga.engine.rendering;

import java.util.HashMap;

public class ParticleConfiguration {

    private HashMap<String, String> config = new HashMap<String, String>() {
        {
            put("direction", "0");
            put("spread", "360");
            put("size", "2");
            put("sizeEnd", "2");
            put("sizeStep", "0");
            put("mode", "MODE_CONTINUOUS");
            put("life", "100");
            put("color", "#0000FFFF");
            put("gravity", "0.6");
            put("velocity", "1");
            put("velocityStep", "0");
        }
    };
    
    public String getValue(String key) {
        return config.get(key);
    }
    
    public void setValue(String key, String value) {
        config.put(key, value);
    }
}
