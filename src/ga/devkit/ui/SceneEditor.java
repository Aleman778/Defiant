package ga.devkit.ui;

import ga.devkit.editor.EditorLayer;
import ga.devkit.editor.EditorObject;
import ga.devkit.editor.EditorTile;
import ga.devkit.editor.SelectionGroup;
import ga.devkit.editor.SelectionType;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.ImageRenderer;
import ga.engine.rendering.JavaFXCanvasRenderer;
import ga.engine.scene.GameObject;
import ga.engine.xml.XMLReader;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.xml.sax.Attributes;

public class SceneEditor extends Interface implements Initializable, Editor {
    
    @FXML
    public Canvas canvas;
    public AnchorPane pane;
    
    public SceneGraph graph;
    public ObjectEditor object;
    public LayerView layer;
    
    private GraphicsContext g;
    public boolean showGrid;
    private int tileSize;
    private int width, height;
    
    private final File file;
    private final EditorObject rootObject;
    private final List<EditorLayer> layers;
    
    public final SelectionGroup selection;
    public static SelectionGroup placement = null;
    
    public SceneEditor(File file) {
        this.file = file;
        this.showGrid = true;
        this.rootObject = new EditorObject("root");
        this.layers = new ArrayList<>();
        this.selection = new SelectionGroup(SelectionType.SELECTION);
        this.tileSize = 32;
        this.width = 0;
        this.height = 0;
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
        sidebar.getItems().add(object.root);
    }
    
    public void render() {
        canvas.setWidth(width);
        canvas.setHeight(height);
        g.clearRect(0, 0, width, height);
        UIRenderUtils.renderCheckerboard(g);
        
        //Render layers
        for (EditorLayer l: layers) {
            l.render(g);
        }
        
        //Render Objects
        List<GameObject> objects = new ArrayList<>();
        rootObject.getGameObjects(objects);
        JavaFXCanvasRenderer.renderAll(canvas, objects);
        
        //Render Selection Groups
        selection.render(g, showGrid, tileSize);
        if (placement != null)
            placement.render(g, showGrid, tileSize);
        
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
    
    public List<GameObject> getAllGameObjects() {
        return rootObject.getGameObjects(new ArrayList<>());
    }
    
    public List<EditorTile> getAllTiles() {
        List<EditorTile> result = new ArrayList<>();
        for (EditorLayer l: layers) {
            result.addAll(l.getTiles());
        }
        return result;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        g = canvas.getGraphicsContext2D();
        
        XMLReader reader = new XMLReader() {

            public EditorLayer layer;
            public EditorObject object;

            @Override
            public void documentStart() {
            }

            @Override
            public void documentEnd() {
            }

            @Override
            public void nodeStart(String element, Attributes attri) {
                switch (element) {
                    case "scene":
                        setSize(Integer.parseInt(attri.getValue("width")),
                                Integer.parseInt(attri.getValue("height"))
                        );
                        break;
                    case "tilemap":
                        layer = new EditorLayer(
                                Integer.parseInt(attri.getValue("depth")),
                                attri.getValue("name")
                        );
                        layers.add(layer);
                        break;
                    case "tile":
                        if (layer != null) {
                            EditorTile tile = new EditorTile(
                                    attri.getValue("image"), layer.getDepth(),
                                    Integer.parseInt(attri.getValue("tilex")),
                                    Integer.parseInt(attri.getValue("tiley")),
                                    Integer.parseInt(attri.getValue("width")),
                                    Integer.parseInt(attri.getValue("height")),
                                    new Vector2D(
                                            Double.parseDouble(attri.getValue("tx")),
                                            Double.parseDouble(attri.getValue("ty"))
                                    )
                            );
                            layer.getTiles().add(tile);
                        }
                        break;
                    case "object":
                        
                        break;
                    case "component":
                        
                        break;
                }
            }

            @Override
            public void nodeEnd(String element, Attributes attri, String value) {
                switch (element) {
                    case "tilemap":
                        layer = null;
                        break;
                    case "object":
                        
                        break;
                }
            }
        };
        reader.parse(file);
        
        pane.setOnMousePressed((MouseEvent event) -> {
            selection.mousePressed(event, this);
            if (placement != null) {
                placement.mousePressed(event, this);
            }
        });
        
        pane.setOnMouseReleased((MouseEvent event) -> {
            selection.mouseReleased(event, this);
            if (placement != null) {
                placement.mouseReleased(event, this);
            }
        });
        
        pane.setOnMouseDragged((MouseEvent event) -> {
            selection.mouseDragged(event, this);
            if (placement != null) {
                placement.mouseDragged(event, this);
            }
        });
        
        pane.setOnMouseMoved((MouseEvent event) -> {
            selection.mouseMoved(event, this);
            if (placement != null) {
                placement.mouseMoved(event, this);
            }
        });
        
        render();
    }

    @Override
    public File getFile() {
        return file;
    }
}
