package ga.devkit.ui;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class LayerView extends Interface implements Initializable {
    
    public HashMap<String, Integer> layers;
    
    @FXML
    public TreeView<String> tree;
    
    private TreeItem<String> rootLayer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        layers = new HashMap<>();
        rootLayer = new TreeItem<>("root");
        tree.setRoot(rootLayer);
        tree.showRootProperty().setValue(false);
        addLayer("Background");
        addLayer("Foreground");
    }
    
    public void addLayer(String name) {
        if (layers.containsKey(name)) {
            name = nameFix(name);
        }
        layers.put(name, 0);
        CheckBoxTreeItem<String> layer = new CheckBoxTreeItem<>(name);
        layer.setGraphic(new CheckBox());
        rootLayer.getChildren().add(layer);
    }
    
    public void removeLayer(String name) {
        TreeItem<String> item = getItem(name);
        if (item != null)
            item.getParent().getChildren().remove(item);
    }
    
    private String nameFix(String name) {
        int i = 1;
        String result = name;
        do {
            result = name + "_" + i;
            i++;
        } 
        while (layers.containsKey(result));
        return result;
    }
    
    private TreeItem<String> getItem(String name) {
        return searchItem(tree.getRoot(), name);
    }
    
    private TreeItem<String> searchItem(TreeItem<String> directory, String name) {
        if (directory.getValue().equals(name)) {
            return directory;
        }
        for (TreeItem<String> item: directory.getChildren()) {
            TreeItem<String> result = searchItem(item, name);
            if (result != null) {
                return result;
            }
        }
        return null;
    }
    
    @FXML
    public void btnAddLayer() {
        
    }
   
    @FXML
    public void btnRemoveLayer() {
        
    }
}
