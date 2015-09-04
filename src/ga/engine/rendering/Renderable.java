package ga.engine.rendering;

import ga.engine.scene.Camera;
import javafx.scene.Node;

public interface Renderable {
    
    public void render(Camera camera);
    
    public Node getNode();
}
