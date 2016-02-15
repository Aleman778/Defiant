package ga.game;

import ga.engine.core.Application;
import ga.engine.resource.ResourceManager;
import ga.engine.scene.GameComponent;
import java.util.List;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class PreloaderComponent extends GameComponent {

    @Override
    public void render(GraphicsContext g) {
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, Application.getWidth(), Application.getHeight());
        g.setFill(Color.WHITE);
        g.setTextAlign(TextAlignment.RIGHT);
        g.setFont(ResourceManager.getFont("fonts/GeosansLight.ttf", 37));
        g.fillText("Loading...", Application.getWidth() - 42, Application.getHeight() - 70);
        g.setTextAlign(TextAlignment.LEFT);
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
        return new PreloaderComponent();
    }
}
