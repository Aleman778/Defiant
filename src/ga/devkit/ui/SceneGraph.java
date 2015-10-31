package ga.devkit.ui;

import ga.devkit.editor.EditorObject;
import ga.engine.scene.GameObject;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class SceneGraph extends Interface implements Initializable {

    @FXML
    public TreeView<String> graph = new TreeView<>();
    
    public HashMap<String, EditorObject> objects;
    
    public SceneGraph() {
        objects = new HashMap<>();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
    
    public void refreshGraph(EditorObject object) {
        objects.clear();
        TreeItem<String> root = new TreeItem<>(object.getName());
        objects.put(object.getName(), object);
        root.setExpanded(true);
        for (GameObject o: object.getChildren()) {
            addToGraph(root, (EditorObject) o);
        }
        graph.setRoot(root);
    }
    
    private void addToGraph(TreeItem<String> item, EditorObject object) {
        String name = object.getName();
        if (objects.containsKey(name)) {
            name = nameFix(name);
        }
        objects.put(name, object);
        item.getChildren().add(new TreeItem<>(name));
    }
    
    private String nameFix(String name) {
        int i = 1;
        String result = name;
        do {
            result = name + "_" + i;
            i++;
        } 
        while (objects.containsKey(result));
        return result;
    }
}
