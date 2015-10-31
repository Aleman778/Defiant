package ga.devkit.ui;

import ga.devkit.editor.EditorObject;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ImageRenderer;
import ga.engine.scene.GameObject;
import ga.engine.scene.Transform2D;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class SceneEditor extends Interface implements Initializable, Editor {

    @FXML
    public Canvas canvas;
    public AnchorPane pane;
    
    public SceneGraph graph;
    
    private final File file;
    private GraphicsContext g;
    private int width, height;
    private int tileSize;
    private boolean showGrid;
    private EditorObject selectedObject;
    private EditorObject placingObject;
    
    private final EditorObject rootObject;
    
    public SceneEditor(File file) {
        this.width = 0;
        this.height = 0;
        this.tileSize = 32;
        this.showGrid = true;
        this.rootObject = new EditorObject("root");
        this.selectedObject = null;
        this.placingObject = null;
        this.file = file;
        this.graph = new SceneGraph();
        this.graph.load();
        graph.refreshGraph(rootObject);
    }
    
    @Override
    public void save() {
        
    }

    @Override
    public void rightContent(SplitPane sidebar) {
        sidebar.getItems().add(graph.root);
    }
    
    public void render() {
        canvas.setWidth(width);
        canvas.setHeight(height);
        g.clearRect(0, 0, width, height);
        
        rootObject.render(g);
        rootObject.renderAABB(g);
        
        if (placingObject != null) {
            g.setGlobalAlpha(0.3);
            placingObject.render(g);
            g.setGlobalAlpha(1.0);
        }
        
        if (showGrid)
            drawGrid();
    }
    
    private void drawGrid() {
        g.setFill(new Color(1.0, 1.0, 1.0, 0.05));
        for (int i = 1; i < width / tileSize; i++) {
            g.fillRect(i * tileSize, 0, 1, height);
        }
        for (int j = 1; j < height / tileSize; j++) {
            g.fillRect(0, j * tileSize, width, 1);
        }
    }
    
    public void addObject(EditorObject object) {
        rootObject.addChild(object);
        graph.refreshGraph(rootObject);
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
            
        });
        pane.setOnMouseDragged((MouseEvent event) -> {
            
        });
        pane.setOnMouseReleased((MouseEvent event) -> {
            placingObject = null;
            render();
        });
        pane.setOnDragDone((DragEvent event) -> {
            placingObject = null;
            render();
        });
        pane.setOnDragExited((DragEvent event) -> {
            placingObject = null;
            render();
        });
        pane.setOnDragDropped((DragEvent event) -> {
            placingObject = null;
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
                    if (placingObject == null) { 
                        switch (ext) {
                            case "png": case "jpg": case "gif":
                                Image image = new Image("file:"+resource.getPath());
                                placingObject = new EditorObject("");
                                placingObject.setAABB(0, 0, (int) image.getWidth(), (int) image.getHeight());
                                placingObject.addComponent(new ImageRenderer(image));
                                render();
                                break;
                        }
                    }
                    if (placingObject != null) {
                        placingObject.getTransform().position.x = event.getX();
                        placingObject.getTransform().position.y = event.getY();
                        render();
                    }
                }
            } else if (event.getDragboard().hasContent(DataFormat.PLAIN_TEXT)) {
                
            }
        });
    }
}
