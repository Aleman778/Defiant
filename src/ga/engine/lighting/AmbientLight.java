package ga.engine.lighting;

import ga.engine.core.Application;

public class AmbientLight extends Light {

    @Override
    public void refresh() {
        lightMap.setWidth(Application.getWidth());
        lightMap.setHeight(Application.getHeight());
        g.setFill(color);
        g.fillRect(0, 0, lightMap.getWidth(), lightMap.getHeight());
    }
}
