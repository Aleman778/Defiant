package ga.devkit.ui;

import com.sun.javafx.geom.Rectangle;
import ga.devkit.editor.EditorLayer;
import ga.devkit.editor.EditorObject;
import ga.devkit.editor.EditorTile;
import ga.devkit.editor.SceneEditorParser;
import ga.devkit.editor.SelectionGroup;
import ga.devkit.editor.SelectionType;
import ga.engine.rendering.JavaFXCanvasRenderer;
import ga.engine.scene.GameObject;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class SceneEditor extends Interface implements Initializable, Editor {
    
    @FXML
    public Canvas canvas;
    public AnchorPane pane;
    
    public SceneGraph graph;
    public ObjectEditor objeditor;
    public LayerView layer;
    
    private GraphicsContext g;
    public boolean showGrid;
    private int tileSize;
    private int width, height;
    
    private final File file;
    private final EditorObject rootobject;
    private EditorLayer currentLayer = null;
    
    public final SelectionGroup SELECTION;
    public static final SelectionGroup PLACEMENT = new SelectionGroup(SelectionType.PLACEMENT);
    
    public SceneEditor(File file) {
        this.file = file;
        this.showGrid = true;
        this.rootobject = new EditorObject("root");
        this.SELECTION = new SelectionGroup(SelectionType.SELECTION);
        this.tileSize = 32;
        this.width = 0;
        this.height = 0;
        this.graph = new SceneGraph(this);
        this.graph.load();
        this.graph.refreshGraph(rootobject);
        this.objeditor = new ObjectEditor();
        this.objeditor.load();
        this.layer = new LayerView(this);
        this.layer.load();
        SplitPane.setResizableWithParent(graph.root, false);
        SplitPane.setResizableWithParent(layer.root, true);
        SplitPane.setResizableWithParent(objeditor.root, true);
    }

    @Override
    public void load() {
        super.load();
    }
    
    @Override
    public void save() {
        
    }

    @Override
    public void rightContent(SplitPane sidebar) {
        sidebar.setDividerPositions(0.3, 0.5);
        sidebar.getItems().add(graph.root);
        sidebar.getItems().add(layer.root);
        sidebar.getItems().add(objeditor.root);
    }
    
    public void render() {
        canvas.setWidth(width);
        canvas.setHeight(height);
        g.clearRect(0, 0, width, height);
        UIRenderUtils.renderCheckerboard(g);
        
        //Render layers
        for (EditorLayer l: layer.getLayers()) {
            if (l != getLayer()) {
                g.setGlobalAlpha(0.5);
            }
            l.render(g);
            g.setGlobalAlpha(1.0);
        }
        
        //Render Objects
        List<GameObject> objects = new ArrayList<>();
        rootobject.getGameObjects(objects);
        JavaFXCanvasRenderer.renderAll(canvas, objects);
        
        //Render Selection Groups
        SELECTION.render(g, showGrid, tileSize);
        if (SELECTION.isEmpty()) {
            PLACEMENT.render(g, showGrid, tileSize);
        }
        
        if (showGrid) {
            UIRenderUtils.renderGrid(g, width, height, tileSize);
        }
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width / tileSize * tileSize;
        render();
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height / tileSize * tileSize;
        render();
    }
    
    public void setSize(int width, int height) {
        this.width = width / tileSize * tileSize;
        this.height = height / tileSize * tileSize;
        render();
    }

    public int getTileSize() {
        return tileSize;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
        width = width / this.tileSize * this.tileSize;
        height = height / this.tileSize * this.tileSize;
        render();
    }
    
    public void setLayer(EditorLayer layer) {
        currentLayer = layer;
    }
    
    public EditorLayer getLayer() {
        return currentLayer;
    }
    
    public void addObject(EditorObject object) {
        rootobject.addChild(object);
        graph.refreshGraph(rootobject);
    }
    
    public void addTile(EditorTile tile) {
        if (getLayer() != null) {
            getLayer().getTiles().add(tile);
        }
    }
    
    public void removeObject(EditorObject object) {
        rootobject.removeChild(object);
        graph.refreshGraph(rootobject);
    }
    
    public void removeObjects(Collection<EditorObject> objects) {
        for (EditorObject object: objects) {
            rootobject.removeChild(object);
        }
        graph.refreshGraph(rootobject);
    }
    
    public void removeTile(EditorTile tile) {
        if (getLayer() != null) {
            getLayer().getTiles().remove(tile);
        }
    }
    
    public void removeTiles(Collection<EditorTile> tiles) {
        if (getLayer() != null) {
            for (EditorTile tile: tiles) {
                getLayer().getTiles().remove(tile);
            }
        }
    }
    
    public void addObjects(Collection<EditorObject> objects) {
        for (EditorObject object: objects) {
            addObject(object);
        }
    }
    
    public void addTiles(Collection<EditorTile> tiles) {
        if (getLayer() == null)
            return;
        
        for (EditorTile tile: tiles) {
            addTile(tile);
        }
    }
    
    public Set<EditorObject> getObjects(int x, int y) {
        Set<EditorObject> result = new HashSet<>();
        for (GameObject o: getAllGameObjects()) {
            if (o.localAABB().contains(x, y)) {
                result.add((EditorObject) o);
            }
        }
        return result;
    }
    
    public EditorObject getObject(int x, int y) {
        EditorObject result = null;
        int depth = Integer.MIN_VALUE;
        for (GameObject object: getAllGameObjects()) {
            if (object.localAABB().contains(x, y)) {
                if (object.getTransform().getDepth() >= depth) { //TODO: Replace with the rendering order
                    result = (EditorObject) object;
                    depth = object.getTransform().getDepth();
                }
            }
        }
        return result;
    }
    
    public Set<EditorTile> getTiles(int x, int y) {
        Set<EditorTile> result = new HashSet<>();
        for (EditorLayer l: layer.getLayers()) {
            for (EditorTile tile: l.getTiles()) {
                if (tile.localAABB().contains(x, y)) {
                    result.add(tile);
                }
            }
        }
        return result;
    }
    
    public EditorTile getTile(int x, int y) {
        EditorTile result = null;
        int depth = Integer.MIN_VALUE;
        for (EditorLayer l: layer.getLayers()) {
            for (EditorTile tile: l.getTiles()) {
                if (tile.localAABB().contains(x, y)) {
                    if (tile.getDepth() >= depth) { //TODO: Replace with the rendering order
                        result = tile;
                        depth = tile.getDepth();
                    }
                }
            }
        }
        return result;
    }
    
    public Set<EditorObject> getObjects(Rectangle rectangle) {
        Set<EditorObject> result = new HashSet<>();
        for (GameObject object: getAllGameObjects()) {
            if (Core.rectIntersection(rectangle, object.localAABB())) {
                result.add((EditorObject) object);
            }
        }
        return result;
    }
    
    public Set<EditorTile> getTiles(Rectangle rectangle) {
        Set<EditorTile> result = new HashSet<>();
        for (EditorLayer l: layer.getLayers()) {
            for (EditorTile tile: l.getTiles()) {
                if (Core.rectIntersection(rectangle, tile.localAABB())) {
                    result.add(tile);
                }
            }
        }
        return result;
    }
    
    public void addSelection(SelectionGroup group) {
        SELECTION.clear();
        for (EditorObject o: group.getObjects()) {
            EditorObject result = o.instantiate();
            if (showGrid) {
                result.getTransform().position.x = (int) (result.getTransform().position.x / (double) tileSize + 0.5) * tileSize;
                result.getTransform().position.y = (int) (result.getTransform().position.y / (double) tileSize + 0.5) * tileSize;
            }
            addObject(result);
        }
        
        if (getLayer() != null) {
            for (EditorTile tile: group.getTiles()) {
                EditorTile result = tile.instantiate();
                if (showGrid) {
                    result.getPosition().x = (int) (result.getPosition().x / (double) tileSize + 0.5) * tileSize;
                    result.getPosition().y = (int) (result.getPosition().y / (double) tileSize + 0.5) * tileSize;
                }
                addTile(result);
            }
        }
    }
    
    public void removeSelection(int x, int y) {
        if (PLACEMENT.isEmpty()) {
            EditorObject object = getObject(x, y);
            EditorTile tile = getTile(x, y);
            if (object != null)
                removeObject(object);
            if (tile != null)
                removeTile(tile);
        } else {
            Rectangle selection = PLACEMENT.getSelection();
            if (showGrid) {
                selection.x = (int) (selection.x / (double) tileSize + 0.5) * tileSize;
                selection.y = (int) (selection.y / (double) tileSize + 0.5) * tileSize;
            }
            
            removeObjects(getObjects(selection));
            removeTiles(getTiles(selection));
        }
        render();
    }
    
    public List<GameObject> getAllGameObjects() {
        return rootobject.getGameObjects(new ArrayList<>());
    }
    
    public List<EditorTile> getAllTiles() {
        List<EditorTile> result = new ArrayList<>();
        for (EditorLayer l: layer.getLayers()) {
            result.addAll(l.getTiles());
        }
        return result;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        g = canvas.getGraphicsContext2D();
        if (file.exists()) {
            SceneEditorParser parser = new SceneEditorParser();
            parser.parse(file);
            layer.clear();
            layer.addLayers(parser.getLayers());
            setTileSize(parser.getTilesize());
            setWidth(parser.getWidth());
            setHeight(parser.getHeight());
        }
        
        pane.setOnMousePressed((MouseEvent event) -> {
            EditorObject object = getObject((int) event.getX(), (int) event.getY());
            EditorTile tile = getTile((int) event.getX(), (int) event.getY());
            
            if (event.getButton() == MouseButton.PRIMARY) {
                if (event.isShiftDown()) {
                    if (object != null)
                        SELECTION.addObject(object);
                    if (tile != null)
                        SELECTION.addTile(tile);
                } else {
                    if (SELECTION.isEmpty() || !SELECTION.getSelection().contains((int) event.getX(), (int) event.getY())) {
                        if (object != null || tile != null)
                            SELECTION.clear();
                        
                        if (object != null)
                            SELECTION.setObject(object);
                        if (tile != null)
                            SELECTION.setTile(tile);
                    }
                }
                render();
            } else if (event.getButton() == MouseButton.SECONDARY) {
                PLACEMENT.mousePressed(event, this);
                removeSelection((int) event.getX(), (int) event.getY());
            }
            
            SELECTION.mousePressed(event, this);
            
            if (SELECTION.isEmpty()) {
                PLACEMENT.mousePressed(event, this);
            } else {
                if (event.isControlDown()) {
                    if (object != null)
                        SELECTION.removeObject(object);
                    if (tile != null)
                        SELECTION.removeTile(tile);
                } else {
                    if (!SELECTION.getSelection().contains((int) event.getX(), (int) event.getY())) {
                        SELECTION.clear();
                        PLACEMENT.mouseMoved(event, this);
                    }
                }
            }
        });
        
        pane.setOnMouseReleased((MouseEvent event) -> {
            SELECTION.mouseReleased(event, this);
            
            if (SELECTION.isEmpty()) {
                PLACEMENT.mouseReleased(event, this);
            }
        });
        
        pane.setOnMouseDragged((MouseEvent event) -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                removeSelection((int) event.getX(), (int) event.getY());
            }
            
            SELECTION.mouseDragged(event, this);
            
            if (SELECTION.isEmpty()) {
                PLACEMENT.mouseDragged(event, this);
            }
        });
        
        pane.setOnMouseMoved((MouseEvent event) -> {
            SELECTION.mouseMoved(event, this);
            
            if (SELECTION.isEmpty()) {
                PLACEMENT.mouseMoved(event, this);
            }
        });
        
        render();
    }

    @Override
    public File getFile() {
        return file;
    }
}
