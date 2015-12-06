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
