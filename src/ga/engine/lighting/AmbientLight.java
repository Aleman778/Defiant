package ga.engine.lighting;

import ga.engine.core.Application;
import ga.engine.scene.GameComponent;
import java.util.List;
import java.util.Map;

public class AmbientLight extends Light {

    @Override
    public void refresh() {
        lightMap.setWidth(Application.getWidth());
        lightMap.setHeight(Application.getHeight());
        g.setFill(color);
        g.fillRect(0, 0, lightMap.getWidth(), lightMap.getHeight());
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
        return new AmbientLight();
    }
}
