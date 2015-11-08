package ga.engine.lighting;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;

public class LightMap extends Group {
    
    private final Set<Light> lights;
    private AmbientLight ambientLight;
    
    public LightMap() {
        this.lights = new HashSet<>();
        this.ambientLight = null;
        setBlendMode(BlendMode.MULTIPLY);
    }
    
    public void setAmbientLight(AmbientLight light) {
        ambientLight = light;
        lights.add(ambientLight);
        getChildren().add(ambientLight.lightMap);
        ambientLight.lightMap.toFront();
    }
    
    public void addLight(Light light) {
        lights.add(light);
        getChildren().add(light.lightMap);
        light.lightMap.toBack();
    }
    
    public void refresh() {
        for (Light light: lights) {
            light.refresh();
        }
    }
}
