package ga.engine.scene;

import com.sun.javafx.geom.Rectangle;
import ga.engine.blueprint.Blueprint;
import ga.engine.physics.Vector2D;
import ga.engine.rendering.Tile;
import ga.engine.rendering.TilemapRenderer;
import ga.engine.resource.ResourceManager;
import ga.engine.xml.XMLReader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;

public class SceneParser extends XMLReader {

    private int width, height, tilesize;
    private List<GameObject> tilemaps;
    private TilemapRenderer tilemapRenderer;
    private GameObject tilemap;
    private GameObject root;
    private GameObject parent;
    private GameScene scene;
    
    private SceneParser(String xmlfile) {
        tilemaps = new ArrayList<>();
        tilemap = null;
        parent = null;
        width = 0;
        height = 0;
        tilesize = 32;
        root = new GameObject();
        scene = new GameScene(root);
        parse(xmlfile);
        root.addAllChildren(tilemaps);
    }
    
    public static GameScene execute(String xmlfile) {
        SceneParser parser = new SceneParser(xmlfile);
        return parser.scene;
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
                Transform2D tileTransform = new Transform2D(null);
                tileTransform.setDepth(Integer.parseInt(attri.getValue("depth")));
                tilemap = new GameObject("tilemap", tileTransform);
                tilemapRenderer = new TilemapRenderer();
                tilemap.addComponent(tilemapRenderer);
                tilemaps.add(tilemap);
                break;
            case "tile":
                if (tilemap != null) {
                    Tile tile = new Tile();
                    tile.x = Integer.parseInt(attri.getValue("tx"));
                    tile.y = Integer.parseInt(attri.getValue("ty"));
                    tile.offsetX = Integer.parseInt(attri.getValue("tilex"));
                    tile.offsetY = Integer.parseInt(attri.getValue("tiley"));
                    tile.width = Integer.parseInt(attri.getValue("width"));
                    tile.height = Integer.parseInt(attri.getValue("height"));
                    tile.setImage(attri.getValue("image"));
                    tilemapRenderer.addTile(tile);
                }
                break;
            case "object":
                String tag = attri.getValue("tag");
                Vector2D translation = new Vector2D();
                translation.x = Float.parseFloat(attri.getValue("tx"));
                translation.y = Float.parseFloat(attri.getValue("ty"));
                Vector2D scale = new Vector2D();
                scale.x = Float.parseFloat(attri.getValue("sx"));
                scale.y = Float.parseFloat(attri.getValue("sy"));
                float rotation = Float.parseFloat(attri.getValue("rot"));
                int depth = Integer.parseInt(attri.getValue("depth"));
                Rectangle bounds = new Rectangle();
                bounds.x = Integer.parseInt(attri.getValue("bx"));
                bounds.y = Integer.parseInt(attri.getValue("by"));
                bounds.width = Integer.parseInt(attri.getValue("bw"));
                bounds.height = Integer.parseInt(attri.getValue("bh"));
                
                Transform2D objectTransform = new Transform2D(
                        null, translation, new Vector2D(), rotation, scale, depth);
                Blueprint blueprint = ResourceManager.getBlueprint(attri.getValue("blueprint"));
                
                if (blueprint != null) {
                    GameObject object = blueprint.instantiate(tag,objectTransform);
                    object.setAABB(bounds);
                    if (parent == null) {
                        root.addChild(object);
                    } else {
                        parent.addChild(object);
                    }
                    parent = object;
                }
                break;
        }
    }

    @Override
    public void nodeEnd(String element, Attributes attri, String value) {
        switch (element) {
            case "tilemap":
                tilemapRenderer = null;
                tilemap = null;
                break;
            case "object":
                if (parent != null) {
                    parent = parent.getParent();
                }
                break;
        }
    }

    @Override
    public void parse(File xmlfile) {
        super.parse(xmlfile);
    }
}
