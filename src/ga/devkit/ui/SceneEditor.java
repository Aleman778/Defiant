package ga.devkit.ui;

import com.sun.javafx.geom.Rectangle;
import ga.devkit.editor.EditorObject;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ImageRenderer;
import ga.engine.scene.GameObject;
import ga.engine.scene.Transform2D;
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
import javafx.scene.image.Image;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class SceneEditor extends Interface implements Initializable, Editor {

    @FXML
    public Canvas canvas;
    public AnchorPane pane;
    
    public SceneGraph graph;
    public ObjectEditor object;
    public LayerView layer;
    
    private final File file;
    private GraphicsContext g;
    private int x, y, prevX, prevY;
    private int width, height;
    private int tileSize;
    private boolean showGrid;
    private boolean dragging, transforming;
    private Set<EditorObject> placingObjects;
    private Set<EditorObject> selectedObjects;
    private Rectangle selectionRange, placingRange;
    
    private final EditorObject rootObject;
    
    public SceneEditor(File file) {
        this.file = file;
        this.width = 0;
        this.height = 0;
        this.x = 0; this.y = 0;
        this.prevX = 0; this.prevY = 0;
        this.tileSize = 32;
        this.showGrid = true;
        this.rootObject = new EditorObject("root");
        this.selectedObjects = new HashSet<>();
        this.placingObjects = new HashSet<>();
        this.selectionRange = null;
        this.placingRange = null;
        this.graph = new SceneGraph(this);
        this.graph.load();
        this.graph.refreshGraph(rootObject);
        this.object = new ObjectEditor();
        this.object.load();
        this.layer = new LayerView();
        this.layer.load();
        SplitPane.setResizableWithParent(graph.root, false);
        SplitPane.setResizableWithParent(layer.root, true);
        SplitPane.setResizableWithParent(object.root, true);
    }
    
    @Override
    public void save() {
        
    }

    @Override
    public void rightContent(SplitPane sidebar) {
        sidebar.setDividerPositions(0.3, 0.5);
        sidebar.getItems().add(graph.root);
        sidebar.getItems().add(layer.root);
        sidebar.getItems().add(object.root);
    }
    
    public void render() {
        canvas.setWidth(width);
        canvas.setHeight(height);
        g.clearRect(0, 0, width, height);
        
        renderCheckerboard();
        
        if (!placingObjects.isEmpty()) {
            placingRange = getRange(placingObjects);
            if (showGrid) {
                placingRange.x = (int) (placingRange.x / 32) * 32;
                placingRange.y = (int) (placingRange.y / 32) * 32;
            }
            g.setFill(new Color(0.0, 1.0, 0.0, 0.2));
            g.fillRect(placingRange.x, placingRange.y, placingRange.width, placingRange.height);
        }
        
        rootObject.render(g);
        
        for (EditorObject object: selectedObjects) {
            object.renderAABB(g);
        }
        if (!selectedObjects.isEmpty()) {
            selectionRange = getRange(selectedObjects);
            renderSelection(selectionRange);
            renderResizeSelection(selectionRange);
        }
        
        if (!placingObjects.isEmpty()) {
            g.setGlobalAlpha(0.8);
            for (GameObject object: placingObjects) {
                object.render(g);
            }
            g.setGlobalAlpha(1.0);
        }
        
        if (showGrid)
            renderGrid();
    }
    
    private void renderGrid() {
        g.setFill(new Color(1.0, 1.0, 1.0, 0.4));
        for (int i = 1; i < width / tileSize; i++) {
            g.fillRect(i * tileSize, 0, 1, height);
        }
        for (int j = 1; j < height / tileSize; j++) {
            g.fillRect(0, j * tileSize, width, 1);
        }
    }
    
    private void renderSelection(Rectangle selection) {
        g.setLineJoin(StrokeLineJoin.MITER);
        g.setLineCap(StrokeLineCap.SQUARE);
        g.setLineDashes(6.0);
        
        g.setFill(Color.rgb(67, 141, 215, 0.1));
        g.fillRect(selection.x, selection.y, selection.width, selection.height);
        
        g.setStroke(Color.rgb(0, 0, 0, 1));
        g.setLineDashOffset(6.0);
        g.strokeLine(selection.x, selection.y, selection.x, selection.y + selection.height);
        g.strokeLine(selection.x, selection.y, selection.x + selection.width, selection.y);
        g.strokeLine(selection.x + selection.width, selection.y, selection.x + selection.width, selection.y + selection.height);
        g.strokeLine(selection.x, selection.y + selection.height, selection.x + selection.width, selection.y + selection.height);
        
        g.setStroke(Color.rgb(67, 141, 215, 1));
        g.setLineDashOffset(0.0);
        g.strokeLine(selection.x, selection.y, selection.x, selection.y + selection.height);
        g.strokeLine(selection.x, selection.y, selection.x + selection.width, selection.y);
        g.strokeLine(selection.x + selection.width, selection.y, selection.x + selection.width, selection.y + selection.height);
        g.strokeLine(selection.x, selection.y + selection.height, selection.x + selection.width, selection.y + selection.height);
    }
    
    private void renderResizeSelection(Rectangle selection) {
        renderDot(selection.x, selection.y);
        renderDot(selection.x + selection.width / 2, selection.y);
        renderDot(selection.x + selection.width, selection.y);
        renderDot(selection.x, selection.y + selection.height / 2);
        renderDot(selection.x, selection.y + selection.height);
        renderDot(selection.x + selection.width / 2, selection.y + selection.height);
        renderDot(selection.x + selection.width, selection.y + selection.height / 2);
        renderDot(selection.x + selection.width, selection.y + selection.height);
    }
    
    private void renderDot(int x, int y) {
        g.setFill(Color.rgb(67, 141, 215, 1));
        g.fillRect(x - 3, y - 3, 6, 6);
        g.setFill(Color.WHITE);
        g.fillRect(x - 2, y - 2, 4, 4);
    }
    
    private void renderCheckerboard() {
        for (int i = 0; i < canvas.getWidth() / 16; i++) {
            for (int j = 0; j < canvas.getHeight() / 16; j++) {
                if (i % 2 + j % 2 == 1) {
                    g.setFill(new Color(0.25, 0.25, 0.25, 1.0));
                } else {
                    g.setFill(new Color(0.32, 0.32, 0.32, 1.0));
                }
                g.fillRect(i * 16, j * 16, 16, 16);
            }
        }
    }
    
    public void addObject(EditorObject object) {
        rootObject.addChild(object);
        graph.refreshGraph(rootObject);
    }
    
    public void clearSelectedObjects() {
        selectedObjects.clear();
    }
    
    public void addSelectedObject(EditorObject object) {
        selectedObjects.add(object);
    }
    
    public void removeSelectedObject(EditorObject object) {
        try {
            selectedObjects.remove(object);
        } catch (Exception e) {
        }
    }
    
    public void transformObjects(Collection<EditorObject> objects, Transform2D transform) {
        for (EditorObject object: objects) {
            object.getTransform().translate(transform.position);
            object.getTransform().rotate(transform.rotation);
            object.getTransform().scale(transform.scale);
        }
    }
    
    private void gridTransformObjects(Collection<EditorObject> objects) {
        for (EditorObject object: objects) {
            object.getTransform().position.x = (int) (object.getTransform().position.x / 32) * 32;
            object.getTransform().position.y = (int) (object.getTransform().position.y / 32) * 32;
        }
    }
    
    public void removeObject(EditorObject object) {
        removeObject(rootObject, object);
        graph.refreshGraph(rootObject); 
    }
    
    private EditorObject removeObject(EditorObject in, EditorObject object) {
        if (in.equals(object))
            return object;
        
        for (GameObject o: in.getChildren()) {
            EditorObject removed = removeObject((EditorObject) o, object);
            if (removed != null) {
                in.getChildren().remove(removed);
                break;
            }
        }
        
        return null;
    }
    
    private Rectangle getRange(Set<EditorObject> objects) {
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
        
        return new Rectangle(sx, sy, ex - sx, ey - sy);
    }
    
    private EditorObject getObject(int x, int y) {
        EditorObject result = null;
        List<GameObject> objects = rootObject.getGameObjects(new ArrayList<>());
        int depth = Integer.MIN_VALUE;
        for (GameObject object: objects) {
            if (object.localAABB().contains(x, y) && object.getTransform().depth > depth) {
                result = (EditorObject) object;
                depth = object.getTransform().depth;
            }
        }
        return result;
    }
    
    private List<EditorObject> getObjects(Rectangle bounds) {
        List<GameObject> objects = rootObject.getGameObjects(new ArrayList<>());
        List<EditorObject> result = new ArrayList<>();
        for (GameObject object: objects) {
            if (bounds.contains(object.localAABB())) {
                result.add((EditorObject) object);
            }
        }
        return result;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        g = canvas.getGraphicsContext2D();
        setSize(2000, 2000);
        render();
        
        pane.setOnMousePressed((MouseEvent event) -> {
            x = (int) event.getX();
            y = (int) event.getY();
            prevX = (int) event.getX();
            prevY = (int) event.getY();
            if (selectionRange != null && !event.isControlDown() && !event.isShiftDown()) {
                if (selectionRange.contains((int) event.getX(), (int) event.getY())) {
                    transforming = true;
                }
            } else {
                transforming = false;
            }
        });
        pane.setOnMouseDragged((MouseEvent event) -> {
            dragging = true;

            if (transforming) {
                Vector2D translate = new Vector2D(event.getX() - prevX, event.getY() - prevY);
                Vector2D scale = new Vector2D();
                double rotate = 0;
                
                transformObjects(selectedObjects, new Transform2D(null, translate, new Vector2D(), rotate, scale, 0));
                placingObjects.addAll(selectedObjects);
                render();
                
            } else {
                render();
                int sx = Math.min(x, (int) event.getX());
                int sy = Math.min(y, (int) event.getY());
                int w = Math.max(x, (int) event.getX()) - sx;
                int h = Math.max(y, (int) event.getY()) - sy;
                renderSelection(new Rectangle(sx, sy, w, h));
            }
            
            prevX = (int) event.getX();
            prevY = (int) event.getY();
        });
        pane.setOnMouseReleased((MouseEvent event) -> {
            if (!event.isControlDown() && !event.isShiftDown() && (!transforming || !dragging)) {
                selectedObjects.clear();
                selectionRange = null;
            }
            placingObjects.clear();
            placingRange = null;
            
            if (showGrid) {
                gridTransformObjects(selectedObjects);
            }

            if (!transforming || !dragging) {
                if (dragging) {
                    int sx = Math.min(x, (int) event.getX());
                    int sy = Math.min(y, (int) event.getY());
                    int w = Math.max(x, (int) event.getX()) - sx;
                    int h = Math.max(y, (int) event.getY()) - sy;

                    List<EditorObject> objects = getObjects(new Rectangle(sx, sy, w, h));
                    if (event.isControlDown()) {
                        selectedObjects.removeAll(objects);
                    } else {
                        selectedObjects.addAll(objects);
                    }
                } else {
                    EditorObject object = getObject((int) event.getX(), (int) event.getY());
                    if (object != null) {
                        if (event.isControlDown()) {
                            selectedObjects.remove(object);
                        } else {
                            selectedObjects.add(object);
                        }
                    }
                }
            }
            graph.setAllSelections(selectedObjects);
            transforming = false;
            dragging = false;
            render();
        });
        pane.setOnKeyPressed((KeyEvent event) -> {
            
        });
        pane.setOnDragDone((DragEvent event) -> {
            placingObjects.clear();
            placingRange = null;
            render();
        });
        pane.setOnDragExited((DragEvent event) -> {
            placingObjects.clear();
            placingRange = null;
            render();
        });
        pane.setOnDragDropped((DragEvent event) -> {
            placingObjects.clear();
            placingRange = null;
            if (event.getDragboard().hasContent(DataFormat.FILES)) {
                File resource = event.getDragboard().getFiles().get(0);
                if (resource.exists()) {
                    String ext = Core.getExtension(resource.getName());
                    switch (ext) {
                        case "png": case "jpg": case "gif":
                            Image image = new Image("file:"+resource.getPath());
                            Vector2D position = new Vector2D(event.getX(), event.getY());
                            if (showGrid) {
                                position.x = (int) (position.x / tileSize) * tileSize;
                                position.y = (int) (position.y / tileSize) * tileSize;
                            }
                            EditorObject result = new EditorObject(Core.getFilename(resource.getName()), new Transform2D(null, position.x, position.y));
                            result.setAABB(0, 0, (int) image.getWidth(), (int) image.getHeight());
                            result.addComponent(new ImageRenderer(image));
                            addObject(result);
                            selectedObjects.clear();
                            selectedObjects.add(result);
                            graph.setSelection(result);
                            render();
                            break;
                    }
                }
            } else if (event.getDragboard().hasContent(DataFormat.PLAIN_TEXT)) {
                
            }
        });
        pane.setOnDragOver((DragEvent event) -> {
            if (event.getDragboard().hasContent(DataFormat.FILES)) {
                File resource = event.getDragboard().getFiles().get(0);
                if (resource.exists()) {
                    String ext = Core.getExtension(resource.getName());
                    event.acceptTransferModes(TransferMode.MOVE);
                    if (placingObjects.isEmpty()) { 
                        switch (ext) {
                            case "png": case "jpg": case "gif":
                                Image image = new Image("file:"+resource.getPath());
                                EditorObject object = new EditorObject("");
                                object.setAABB(0, 0, (int) image.getWidth(), (int) image.getHeight());
                                object.addComponent(new ImageRenderer(image));
                                placingObjects.add(object);
                                break;
                        }
                    }
                    if (!placingObjects.isEmpty()) {
                        for (EditorObject object: placingObjects) {
                            object.getTransform().position.x = event.getX();
                            object.getTransform().position.y = event.getY();
                        }
                        render();
                    }
                }
            } else if (event.getDragboard().hasContent(DataFormat.PLAIN_TEXT)) {
                
            }
        });
    }
}
