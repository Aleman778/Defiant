package ga.devkit.editor;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;

public class EditorLayer {
    
    private final List<EditorTile> tiles;
    private int depth;
    private String name;

    public EditorLayer(int depth, String name) {
        this.tiles = new ArrayList<>();
        this.depth = depth;
        this.name = name;
    }

    public void render(GraphicsContext g) {
        for (EditorTile tile: tiles) {
            tile.render(g);
        }
    }
    
    public List<EditorTile> getTiles() {
        return tiles;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
