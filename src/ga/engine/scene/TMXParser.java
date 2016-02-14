package ga.engine.scene;

import ga.engine.rendering.Tile;
import ga.engine.rendering.TilemapRenderer;
import ga.engine.resource.ResourceManager;
import ga.engine.xml.XMLReader;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import org.xml.sax.Attributes;

public class TMXParser extends XMLReader {

    private int tileW, tileH;
    private int width, height, tilesize;
    private TilemapRenderer tilemapRenderer;
    private List<GameObject> tilemaps;
    private List<TilesetData> tilesets;
    private TilesetData tileset;
    private GameObject tileobj;
    private GameObject root;
    private GameScene scene;
    
    private TMXParser(String xmlfile) {
        tilemaps = new ArrayList<>();
        tilesets = new ArrayList<>();
        tilemapRenderer = null;
        width = 0;
        height = 0;
        tilesize = 32;
        tileobj = null;
        root = new GameObject();
        scene = new GameScene(root);
        parse(xmlfile);
        root.addAllChildren(tilemaps);
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
            case "map":
                tilesize = Integer.parseInt(attri.getValue("tilewidth"));
                width = tilesize * Integer.parseInt(attri.getValue("width"));
                height = tilesize * Integer.parseInt(attri.getValue("height"));
                break;
            case "tileset":
                String source = attri.getValue("source");
                if (source == null) {
                    tileset = new TilesetData();
                    tileset.tilewidth = Integer.parseInt(attri.getValue("tilewidth"));
                    tileset.firstgid = Integer.parseInt(attri.getValue("firstgid"));
                    tileset.tileheight = Integer.parseInt(attri.getValue("tileheight"));
                    tileset.tilecount = Integer.parseInt(attri.getValue("tilecount"));
                    tileset.columns = Integer.parseInt(attri.getValue("columns"));
                } else {
                    ExternalTileset external = new ExternalTileset(fixPath(source));
                    tileset = external.getData();
                    tileset.firstgid = Integer.parseInt(attri.getValue("firstgid"));
                }
                tilesets.add(tileset);
                break;
            case "image":
                if (tileset != null) {
                    tileset.tilemap = ResourceManager.getImage(fixPath(attri.getValue("source")));
                }
                break;
            case "layer":
                tileW = Integer.parseInt(attri.getValue("width"));
                tileH = Integer.parseInt(attri.getValue("height"));
                tileobj = new GameObject("tilemap");
                tilemapRenderer = new TilemapRenderer();
                tileobj.addComponent(tilemapRenderer);
                tilemaps.add(tileobj);
                break;
            case "property":
                if (tileobj != null) {
                    switch (attri.getValue("name")) {
                        case "depth":
                        tileobj.getTransform().depth = Integer.parseInt(attri.getValue("value"));
                            break;
                    }
                }
                break;
        }
    }

    @Override
    public void nodeEnd(String element, Attributes attri, String value) {
        switch (element) {
            case "tileset":
                tileset = null;
                break;
            case "data":
                if (tilemapRenderer != null) {
                    String[] map = value.split(",");
                    
                    for (int x = 0; x < tileW; x++) {
                        for (int y = 0; y < tileH; y++) {
                            int index = Integer.parseInt(map[x + y * tileW].trim());
                            if (index > 0) {
                                TilesetData data = getTileset(index);
                                if (data != null) {
                                    index = index - data.firstgid;
                                    Tile tile = new Tile();
                                    tile.x = x * tilesize;
                                    tile.y = y * tilesize;
                                    tile.offsetX = (index % data.columns) * data.tilewidth;
                                    tile.offsetY = (index / data.columns) * data.tileheight;
                                    tile.width = data.tileheight;
                                    tile.height = data.tilewidth;
                                    tile.image = data.tilemap;
                                    tilemapRenderer.addTile(tile);
                                }
                            }
                        }
                    }
                }
                break;
            case "layer":
                tileobj = null;
                tilemapRenderer = null;
                break;
        }
    }
    
    private TilesetData getTileset(int index) {
        for (TilesetData data: tilesets) {
            if (index >= data.firstgid && index < data.firstgid + data.tilecount) {
                return data;
            }
            System.out.println(data.firstgid);
        }
        return null;
    }
    
    class TilesetData {
        
        public int firstgid = 1;
        public int tilecount = 0;
        public int columns = 0;
        public int tilewidth = 32;
        public int tileheight = 32;
        public Image tilemap;
        
    }
    
    class ExternalTileset extends XMLReader {

        private TilesetData data;

        public TilesetData getData() {
            return data;
        }
        
        private ExternalTileset(String xmlfile) {
            data = new TilesetData();
            System.out.println(xmlfile);
            parse(xmlfile);
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
                    data.tilewidth = Integer.parseInt(attri.getValue("tilewidth"));
                    data.tileheight = Integer.parseInt(attri.getValue("tileheight"));
                    data.tilecount = Integer.parseInt(attri.getValue("tilecount"));
                    data.columns = Integer.parseInt(attri.getValue("columns"));
                    break;
                case "image":
                    data.tilemap = ResourceManager.getImage(fixPath(attri.getValue("source")));
                    break;
            }
        }

        @Override
        public void nodeEnd(String element, Attributes attri, String value) {
        }
    }
}
