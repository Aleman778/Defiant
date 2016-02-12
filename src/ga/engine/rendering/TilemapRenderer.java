package ga.engine.rendering;

import ga.engine.scene.GameComponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;

public class TilemapRenderer extends GameComponent {
    
    private List<Tile> tiles;

    public TilemapRenderer() {
        tiles = new ArrayList<>();
    }

    public void addTile(Tile tile) {
        tiles.add(tile);
    }
    
    public void removeTile(Tile tile) {
        tiles.remove(tile);
    }

    @Override
    public void render(GraphicsContext g) {
        for (Tile tile: tiles) {
            tile.render(g);
        }
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
        return new TilemapRenderer();
    }
}
