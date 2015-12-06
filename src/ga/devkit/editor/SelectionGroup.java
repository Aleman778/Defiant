package ga.devkit.editor;

import com.sun.javafx.geom.Rectangle;
import ga.devkit.ui.SceneEditor;
import ga.devkit.ui.UIRenderUtils;
import ga.engine.scene.GameObject;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class SelectionGroup {
    
    private final Set<EditorObject> objects;
    private final Set<EditorTile> tiles;
    private SelectionType type;
    private Rectangle selection;
    private boolean visible;
    private int prevX, prevY;
    
    public SelectionGroup(SelectionType type) {
        this.objects = new HashSet<>();
        this.tiles = new HashSet<>();
        this.type = type;
        this.selection = new Rectangle(0, 0);
        this.visible = true;
        this.prevX = 0;
        this.prevY = 0;
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

    public Set<EditorObject> getObjects() {
        return objects;
    }

    public Set<EditorTile> getTiles() {
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
    
    public void removeObject(EditorObject object) {
        objects.remove(object);
        selection = getRange();
    }
    
    public void removeObjects(Collection<EditorObject> collection) {
        objects.removeAll(collection);
        selection = getRange();
    }
    
    public void removeTile(EditorTile tile) {
        tiles.remove(tile);
        selection = getRange();
    }
    
    public void removeTiles(Collection<EditorTile> collection) {
        tiles.removeAll(collection);
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
    
    public boolean containsObject(EditorObject object) {
        return objects.contains(object);
    }
    
    public boolean containsTile(EditorTile tile) {
        return tiles.contains(tile);
    }
    
    public void clear() {
        objects.clear();
        tiles.clear();
    }
    
    public void clearObjects() {
        objects.clear();
    }
    
    public void clearTiles() {
        tiles.clear();
    }
    
    public int size() {
        return tiles.size() + objects.size();
    }
    
    public boolean isEmpty() {
        return (tiles.isEmpty() && objects.isEmpty());
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    public void snapToGrid(int tileSize) {
        for (EditorObject object: objects) {
            object.getTransform().position.x = (int) (object.getTransform().position.x / (double) tileSize + 0.5) * tileSize;
            object.getTransform().position.y = (int) (object.getTransform().position.y / (double) tileSize + 0.5) * tileSize;
        }
        
        for (EditorTile tile: tiles) {
            tile.getPosition().x = (int) (tile.getPosition().x / (double) tileSize + 0.5) * tileSize;
            tile.getPosition().y = (int) (tile.getPosition().y / (double) tileSize + 0.5) * tileSize;
        }
    }
    
    public void translate(int x, int y) {
        for (EditorObject object: objects) {
            object.getTransform().position.x += x - prevX;
            object.getTransform().position.y += y - prevY;
        }
        for (EditorTile tile: tiles) {
            tile.getPosition().x += x - prevX;
            tile.getPosition().y += y - prevY;
        }
        selection.x += (int) x - prevX;
        selection.y += (int) y - prevY;
        translateBegin(x, y);
    }
    
    public void translateBegin(int x, int y) {
        prevX = x;
        prevY = y;
    }
    
    public void setTranslation(int x, int y) {
        for (EditorObject object: objects) {
            object.getTransform().position.x = x;
            object.getTransform().position.y = y;
        }
        for (EditorTile tile: tiles) {
            tile.getPosition().x = x;
            tile.getPosition().y = y;
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
    
    public Rectangle getSelection() {
        return selection;
    }
    
    public void render(GraphicsContext g, boolean showGrid, int gridSize) {
        if (isEmpty() || !isVisible())
            return;
        
        switch (type) {
            case SELECTION:
                UIRenderUtils.renderSelection(g, selection);
                UIRenderUtils.renderResizeSelection(g, selection);
                break;
            case TRANSLATION: case ROTATION: case SCALE:
                g.setFill(new Color(1.0, 1.0, 0.0, 0.2));
                if (showGrid) {
                    g.fillRect((int) (selection.x / (double) gridSize + 0.5) * gridSize, 
                            (int) (selection.y / (double) gridSize + 0.5) * gridSize,
                            selection.width, selection.height);
                }
                renderSelection(g);
                UIRenderUtils.renderTransformingSelection(g, selection);
                break;
            case PLACEMENT:
                g.setGlobalAlpha(0.4);
                renderSelection(g);
                g.setGlobalAlpha(1.0); 
                break;
            case DRAGGING:
                if (showGrid) {
                    g.setFill(new Color(0.0, 1.0, 0.0, 0.2));
                    g.fillRect((int) (selection.x / (double) gridSize + 0.5) * gridSize, 
                            (int) (selection.y / (double) gridSize + 0.5) * gridSize,
                            selection.width, selection.height);
                }
                renderSelection(g);
                break;
            case REMOVING:
                g.setFill(new Color(1.0, 0.0, 0.0, 0.2));
                if (showGrid) {
                    g.fillRect((int) (selection.x / (double) gridSize + 0.5) * gridSize, 
                            (int) (selection.y / (double) gridSize + 0.5) * gridSize,
                            selection.width, selection.height);
                } else {
                    g.fillRect(selection.x, selection.y, selection.width, selection.height);
                }
                break;
        }
    }
    
    private void renderSelection(GraphicsContext g) {
        for (EditorObject object: objects) {
            object.render(g);
        }
        for (EditorTile tile: tiles) {
            tile.render(g);
        }
    }
    
    public void mousePressed(MouseEvent event, SceneEditor editor) {
        if (isEmpty() || !isVisible())
            return;
        
        switch (type) {
            case PLACEMENT:
                if (event.getButton() == MouseButton.PRIMARY) {
                    setSelectionType(SelectionType.DRAGGING);
                } else {
                    setSelectionType(SelectionType.REMOVING);
                }
                editor.render();
                break;
            case SELECTION:
                if (editor.SELECTION.selection.contains((int) event.getX(), (int) event.getY())) {
                    SceneEditor.PLACEMENT.setVisible(false);
                    setSelectionType(SelectionType.TRANSLATION);
                    translateBegin((int) event.getX(), (int) event.getY());
                }
                break;
        }
    }
    
    public void mouseReleased(MouseEvent event, SceneEditor editor) {
        if (isEmpty() || !isVisible())
            return;
        
        switch (type) {
            case DRAGGING:
                editor.addSelection(this);
                setSelectionType(SelectionType.PLACEMENT);
                editor.render();
                break;
            case REMOVING:
                setSelectionType(SelectionType.PLACEMENT);
                editor.render();
                break;
            case TRANSLATION: case ROTATION: case SCALE:
                if (editor.showGrid) {
                    snapToGrid(editor.getTileSize());
                    selection = getRange();
                }
                setSelectionType(SelectionType.SELECTION);
                SceneEditor.PLACEMENT.setVisible(true);
                SceneEditor.PLACEMENT.setTranslation((int) event.getX() - selection.width / 2, (int) event.getY() - selection.height / 2);
                editor.render();
                break;
        }
    }
    
    public void mouseDragged(MouseEvent event, SceneEditor editor) {
        if (isEmpty() || !isVisible())
            return;
        
        switch (type) {
            case PLACEMENT:
                setTranslation((int) event.getX() - selection.width / 2, (int) event.getY() - selection.height / 2);
                editor.render();
                break;
            case DRAGGING: case REMOVING:
                setTranslation((int) event.getX() - selection.width / 2, (int) event.getY() - selection.height / 2);
                editor.render();
                break;
            case TRANSLATION:
                translate((int) event.getX(), (int) event.getY());
                editor.render();
                break;
        }
    }
    
    public void mouseMoved(MouseEvent event, SceneEditor editor) {
        if (isEmpty() || !isVisible())
            return;
        
        switch (type) {
            case PLACEMENT:
                setTranslation((int) event.getX() - selection.width / 2, (int) event.getY() - selection.height / 2);
                editor.render();
                break;
        }
    }
}