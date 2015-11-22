package ga.devkit.editor;

import ga.engine.physics.Vector2D;
import ga.engine.xml.XMLReader;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;

public class SceneEditorParser extends XMLReader {
    
    private int width, height;
    private int tilesize;
    private EditorObject root;
    private List<EditorLayer> layers;
    private EditorLayer layer;

    public SceneEditorParser() {
        layers = new ArrayList<>();
    }

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
                width = Integer.parseInt(attri.getValue("width"));
                height = Integer.parseInt(attri.getValue("height"));
                tilesize = Integer.parseInt(attri.getValue("tilesize"));
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
    
    public int getTilesize() {
        return tilesize;
    }

    public EditorObject getRoot() {
        return root;
    }

    public List<EditorLayer> getLayers() {
        return layers;
    }
}
