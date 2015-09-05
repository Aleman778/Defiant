package ga.engine.rendering;

import javafx.scene.Group;
import javafx.scene.Node;

public interface Renderable {
    
    public void render(Group group);
    
    public Node getNode();
}
