package ga.engine.rendering;

import com.sun.javafx.geom.Rectangle;
import javafx.scene.Group;
import javafx.scene.Node;

public interface Renderable {
    
    public void render(Group group);
    
    public Node getNode();
    
    public Rectangle computeAABB();
}
