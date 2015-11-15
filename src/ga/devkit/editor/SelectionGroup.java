package ga.devkit.editor;

import com.sun.javafx.geom.Rectangle;
import ga.devkit.ui.SceneEditor;
import ga.devkit.ui.UIRenderUtils;
import ga.engine.scene.GameObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class SelectionGroup {
    
    private final List<EditorObject> objects;
    private final List<EditorTile> tiles;
    private SelectionType type;
    private Rectangle selection;
    
    public SelectionGroup(SelectionType type) {
        this.objects = new ArrayList<>();
        this.tiles = new ArrayList<>();
        this.type = type;
        this.selection = new Rectangle(0, 0);
    }

    public SelectionType getSelectionType() {
        return type;
    }

    public void setSelectionType(SelectionType type) {
        this.type = type;
    }

    public void addSelection(Rectangle selection, SceneEditor editor) {
        for (GameObject object: editor.getAllGameObjects()) {
            if (object.localAABB().contains(selection)) {
                objects.add((EditorObject) object);
            }
        }
        for (EditorTile tile: editor.getAllTiles()) {
            if (tile.localAABB().contains(selection)) {
                tiles.add(tile);
            }
        }
        this.selection = getRange();
    }
    
    public void removeSelection(Rectangle selection, SceneEditor editor) {
        for (GameObject object: editor.getAllGameObjects()) {
            if (object.localAABB().contains(selection)) {
                if (objects.contains((EditorObject) object)) {
                    objects.remove((EditorObject) object);
                }
            }
        }
        for (EditorTile tile: editor.getAllTiles()) {
            if (tile.localAABB().contains(selection)) {
                if (tiles.contains(tile)) {
                    tiles.remove(tile);
                }
            }
        }
        this.selection = getRange();
    }

    public List<EditorObject> getObjects() {
        return objects;
    }

    public List<EditorTile> getTiles() {
        return tiles;
    }
    
    public void addObject(EditorObject object) {
        objects.add(object);
        selection = getRange();
    }
    
    public void addObjects(Collection<EditorObject> collection) {
        objects.addAll(collection);
        selection = getRange();
    }
    
    public void addTile(EditorTile tile) {
        tiles.add(tile);
        selection = getRange();
    }
    
    public void addTiles(Collection<EditorTile> collection) {
        tiles.addAll(collection);
        selection = getRange();
    }
    
    public void setObject(EditorObject object) {
        objects.clear();
        objects.add(object);
        selection = getRange();
    }
    
    public void setObjects(Collection<EditorObject> collection) {
        objects.clear();
        objects.addAll(collection);
        selection = getRange();
    }
    
    public void setTile(EditorTile tile) {
        tiles.clear();
        tiles.add(tile);
        selection = getRange();
    }
    
    public void setTiles(Collection<EditorTile> collection) {
        tiles.clear();
        tiles.addAll(collection);
        selection = getRange();
    }
    
    public void translate(int x, int y) {
        selection.x += x;
        selection.y += y;
        for (EditorObject object: objects) {
            object.getTransform().position.x += x;
            object.getTransform().position.y += y;
        }
        for (EditorTile tile: tiles) {
            tile.getPosition().x += x;
            tile.getPosition().y += y;
        }
    }
    
    public void setTranslation(int x, int y) {
        for (EditorObject object: objects) {
            object.getTransform().position.x += x - selection.x;
            object.getTransform().position.y += y - selection.y;
        }
        for (EditorTile tile: tiles) {
            tile.getPosition().x += x - selection.x;
            tile.getPosition().y += y - selection.y;
        }
        selection.x = x;
        selection.y = y;
    }
    
    private Rectangle getRange() {
        int sx = Integer.MAX_VALUE;
        int sy = Integer.MAX_VALUE;
        int ex = Integer.MIN_VALUE;
        int ey = Integer.MIN_VALUE;

        for (EditorObject object: objects) {
            Rectangle bounds = object.localAABB();
            if (bounds.x < sx)
                sx = bounds.x;
            if (bounds.x + bounds.width > ex)
                ex = bounds.x + bounds.width;
            if (bounds.y < sy)
                sy = bounds.y;
            if (bounds.y + bounds.height > ey)
                ey = bounds.y + bounds.height;
        }
        
        for (EditorTile tile: tiles) {
            Rectangle bounds = tile.localAABB();
            if (bounds.x < sx)
                sx = bounds.x;
            if (bounds.x + bounds.width > ex)
                ex = bounds.x + bounds.width;
            if (bounds.y < sy)
                sy = bounds.y;
            if (bounds.y + bounds.height > ey)
                ey = bounds.y + bounds.height;
        }
        
        return new Rectangle(sx, sy, ex - sx, ey - sy);
    }
    
    public void render(GraphicsContext g, boolean showGrid, int tileSize) {
        if (objects.size() + tiles.size() == 0)
            return;
        
        switch (type) {
            case SELECTION:
                UIRenderUtils.renderResizeSelection(g, selection);
                break;
            case TRANSFORMATION:
                if (showGrid) {
                    g.setFill(new Color(0.0, 1.0, 0.0, 0.1));
                    g.fillRect((int) (selection.x / tileSize + 1) * tileSize, 
                            (int) (selection.y / tileSize + 1) * tileSize,
                            selection.width, selection.height);
                }
                UIRenderUtils.renderSelection(g, selection);
                break;
            case PLACEMENT:
                g.setGlobalAlpha(0.4);
                renderObjects(g);
                g.setGlobalAlpha(1.0); 
                break;
            case DRAGBOARD:
                break;
        }
    }
    
    public void mousePressed(MouseEvent event, SceneEditor editor) {
        
    }
    
    public void mouseReleased(MouseEvent event, SceneEditor editor) {
        switch (type) {
            case PLACEMENT:
                
                editor.render();
                break;
        }
    }
    
    public void mouseDragged(MouseEvent event, SceneEditor editor) {
        switch (type) {
            case PLACEMENT:
                setTranslation((int) event.getX() - selection.width / 2, (int) event.getY() - selection.height / 2);
                editor.render();
                break;
        }
    }
    
    public void mouseMoved(MouseEvent event, SceneEditor editor) {
        switch (type) {
            case PLACEMENT:
                setTranslation((int) event.getX() - selection.width / 2, (int) event.getY() - selection.height / 2);
                editor.render();
                break;
        }
    }
    
    private void renderObjects(GraphicsContext g) {
        for (EditorObject object: objects) {
            object.render(g);
        }
        for (EditorTile tile: tiles) {
            tile.render(g);
        }
    }
}