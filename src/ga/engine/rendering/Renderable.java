package ga.engine.rendering;

import java.awt.Rectangle;
import javafx.scene.Group;
import javafx.scene.Node;

public interface Renderable {
    
    public void render(Group group);
    
    public Node getNode();
    
    public Rectangle computeAABB();
}
