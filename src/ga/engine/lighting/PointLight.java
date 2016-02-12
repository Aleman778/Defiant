package ga.engine.lighting;

import ga.engine.scene.GameComponent;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;

public class PointLight extends Light {
    
    private Attenuation attenuation;

    public PointLight() {
        attenuation = new Attenuation(0.6, 0.01, 0);
        color = Color.RED;
    }
    
    public Attenuation getAttenuation() {
        return attenuation;
    }
    
    public double getRange() {
        return Math.min(transform.scale.x, transform.scale.y);
    }
    
    @Override
    public void refresh() {
        double range = getRange();
        gameobject.setAABB((int) -range, (int) -range, (int) range * 2, (int) range * 2);
        lightMap.setWidth(range * 2);
        lightMap.setHeight(range * 2);
        RadialGradient light = new RadialGradient(
                0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE,
                new Stop(1, Color.BLACK),
                new Stop(attenuation.getContstant(), color),
                new Stop(0, color)
        );
        g.setFill(light);
        g.fillRect(0, 0, lightMap.getWidth(), lightMap.getHeight());
        getLitObjects();
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
        return new PointLight();
    }
}
