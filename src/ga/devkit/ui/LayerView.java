package ga.devkit.ui;

import ga.devkit.editor.EditorLayer;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class LayerView extends Interface implements Initializable {
    
    public Map<String, EditorLayer> layers;
    
    @FXML
    public TreeView<String> tree;
    
    private TreeItem<String> rootLayer;
    private SceneEditor editor;

    public LayerView(SceneEditor editor) {
        this.editor = editor;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        layers = new HashMap<>();
        rootLayer = new TreeItem<>("root");
        tree.setRoot(rootLayer);
        tree.showRootProperty().setValue(false);
        addLayer("Background", -10);
        addLayer("Foreground", 10);
        tree.selectionModelProperty().addListener((ObservableValue<? extends MultipleSelectionModel<TreeItem<String>>> observable, MultipleSelectionModel<TreeItem<String>> oldValue, MultipleSelectionModel<TreeItem<String>> newValue) -> {
            editor.setLayer(layers.get(newValue.getSelectedItems().get(0).getValue()));
            editor.render();
        });
    }
    
    public void addLayer(String name, int depth) {
        addLayer(name, new EditorLayer(depth, name));
    }
    
    public void addLayer(String name, EditorLayer layer) {
        if (layers.containsKey(name)) {
            name = nameFix(name);
        }
        
        layers.put(name, layer);
        editor.setLayer(layer);
        CheckBoxTreeItem<String> layerNode = new CheckBoxTreeItem<>(name);
        layerNode.setGraphic(new CheckBox());
        rootLayer.getChildren().add(layerNode);
        tree.getSelectionModel().select(layerNode);
    }
    
    public void addLayers(Collection<EditorLayer> layers) {
        for (EditorLayer layer: layers) {
            addLayer(layer.getName(), layer);
        }
    }
    
    public Collection<EditorLayer> getLayers() {
        return layers.values();
    }
    
    public void removeLayer(String name) {
        TreeItem<String> item = getItem(name);
        if (item != null)
            item.getParent().getChildren().remove(item);
    }
    
    public void clear() {
        rootLayer.getChildren().clear();
        layers.clear();
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
