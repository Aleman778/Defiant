package ga.engine.scene;

import ga.engine.blueprint.Blueprint;
import ga.engine.rendering.Tile;
import ga.engine.rendering.TilemapRenderer;
import ga.engine.resource.ResourceManager;
import ga.engine.xml.XMLReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.media.Media;
import org.xml.sax.Attributes;

public class TMXParser extends XMLReader {
    
    private int width, height;
    private int tileWidth, tileHeight;
    private Data data;
    private final GameObject root;
    private final GameScene scene;
    private final List<Tileset> tilesets;
    
    private TMXParser(String tmxfile) {
        width = 0; height = 0;
        tileWidth = 32; tileHeight = 32;
        data = null;
        root = new GameObject("root");
        scene = new GameScene(root);
        tilesets = new ArrayList<>();
        
        parse(tmxfile);
    }
    
    public static GameScene execute(String xmlfile) {
        TMXParser parser = new TMXParser(xmlfile);
        return parser.scene;
    }
    
    private static String fixPath(String path) {
        return path.replace("../", "");
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
            case "property": 
                if (data != null) {
                    data.put(attri.getValue("name"), attri.getValue("value"));
                }
                break;
            case "tileset":
                String src = attri.getValue("source");
                if (src == null) {
                    Tileset tileset = new Tileset();
                    tileset.firstgid = (int) Double.parseDouble(attri.getValue("firstgid"));
                    tileset.tilecount = (int) Double.parseDouble(attri.getValue("tilecount"));
                    tilesets.add(tileset);
                } else {
                    ExternalTileset tileset =new ExternalTileset(fixPath(src),
                            (int) Double.parseDouble(attri.getValue("firstgid")));
                }
                break;
            case "image":
                if (!tilesets.isEmpty()) {
                    String source = attri.getValue("source");
                    tilesets.get(tilesets.size() - 1).image =
                            ResourceManager.getImage(fixPath(attri.getValue("source")));
                }
                break;
            case "layer":
                data = new Layer();
                String w = attri.getValue("width");
                ((Layer) data).width = w == null ? 0 : (int) Double.parseDouble(w);
                String h = attri.getValue("height");
                ((Layer) data).height = h == null ? 0 : (int) Double.parseDouble(h);
                break;
            case "object":
                data = new Object();
                ((Object) data).x = Double.parseDouble(attri.getValue("x"));
                ((Object) data).y = Double.parseDouble(attri.getValue("y"));
                String w2 = attri.getValue("width");
                ((Object) data).width = w2 == null ? 0 : (int) Double.parseDouble(w2);
                String h2 = attri.getValue("height");
                ((Object) data).height = h2 == null ? 0 : (int) Double.parseDouble(h2);
                break;
            case "map":
                width = (int) Double.parseDouble(attri.getValue("width"));
                height = (int) Double.parseDouble(attri.getValue("height"));
                tileWidth = (int) Double.parseDouble(attri.getValue("tilewidth"));
                tileHeight = (int) Double.parseDouble(attri.getValue("tileheight"));
                break;
        }
    }

    @Override
    public void nodeEnd(String element, Attributes attri, String value) {
        switch (element) {
            case "data":
                if (data instanceof Layer) {
                    String[] tilemap = value.split(",");
                    for (int i = 0; i < tilemap.length; i++) {
                        int id = (int) Double.parseDouble(tilemap[i].trim());
                        Tileset tileset = getTileset(id);
                        if (tileset != null && id > 0) {
                            Tile tile = new Tile();
                            tile.x = (i % ((Layer) data).width) * tileWidth;
                            tile.y = (i / ((Layer) data).width) * tileHeight;
                            tile.offsetX = ((id - tileset.firstgid) % (int) (tileset.image.getWidth() / tileWidth)) * tileWidth;
                            tile.offsetY = ((id - tileset.firstgid) / (int) (tileset.image.getWidth() / tileWidth)) * tileHeight;
                            tile.width = tileWidth;
                            tile.height = tileHeight;
                            tile.image = tileset.image;
                            ((Layer) data).tilemap.addTile(tile);
                        }
                    }
                }
                break;
            case "layer":
                String tag = data.get("Tag");
                GameObject object = new GameObject(tag == null ? "untagged" : tag);
                object.addComponent(((Layer) data).tilemap);
                setupGameObject(object, 0, 0, 0, 0);
                if (data.get("Music") != null) {
                    scene.backgroundMusic = ResourceManager.getMedia(data.get("Music"));
                }
                data = null;
                break;
            case "object":
                Object obj = (Object) data;
                String tag2 = obj.get("Tag");
                String source = obj.get("Blueprint");
                if (source == null) {
                    source = "blueprints/physics/Collision.blueprint";
                }
                
                Blueprint blueprint = ResourceManager.getBlueprint(source);
                if (blueprint != null) {
                    GameObject object2 = blueprint.instantiate(tag2 == null ? "untagged" : tag2, new Transform2D(null));
                    setupGameObject(object2, obj.x, obj.y, obj.width, obj.height);
                }
                data = null;
                break;
        }
    }
    
    private void setupGameObject(GameObject object, double x, double y, int width, int height) {
        String depth = data.get("Depth");
        String rot = data.get("Rot");
        String sclx = data.get("Sclx");
        String scly = data.get("Scly");
        
        object.transform.position.x = x;
        object.transform.position.y = y;
        object.transform.rotation   = rot == null ? 0 : Double.parseDouble(rot);
        object.transform.depth      = rot == null ? 0 : (int) Double.parseDouble(depth);
        object.transform.scale.x    = sclx == null ? 1 : Double.parseDouble(sclx);
        object.transform.scale.y    = scly == null ? 1 : Double.parseDouble(scly);
        object.setAABB(0, 0, width, height);
        root.addChild(object);
    }
    
    private Tileset getTileset(int tileid) {
        for (Tileset tileset: tilesets) {
            if ((tileid >= tileset.firstgid && tileid < tileset.firstgid + tileset.tilecount)) {
                return tileset;
            }
        }
        
        return null;
    }
    
    abstract class Data extends HashMap<String, String> {}
    
    class Tileset extends Data {
        public int firstgid = 0;
        public int tilecount = 0;
        public Image image;
    }
    
    class ExternalTileset extends XMLReader {

        public Tileset tileset;

        public ExternalTileset(String tsxfile, int firstgid) {
            tileset = new Tileset();
            tileset.firstgid = firstgid;
            parse(tsxfile);
            tilesets.add(tileset);
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
                case "tileset":
                tileset.tilecount = (int) Double.parseDouble(attri.getValue("tilecount"));
                tilesets.add(tileset);
                break;
            case "image":
                if (!tilesets.isEmpty()) {
                    String source = attri.getValue("source");
                    tilesets.get(tilesets.size() - 1).image =
                            ResourceManager.getImage(fixPath(attri.getValue("source")));
                }
                break;
            }
        }

        @Override
        public void nodeEnd(String element, Attributes attri, String value) {
        }
        
    }
    
    class Layer extends Data {
        public TilemapRenderer tilemap = new TilemapRenderer();
        public int width = 0, height = 0;
    }
    
    class Object extends Data {
        public String blueprint;
        public double x = 0, y = 0;
        public int width = 0, height = 0;
    }
}
