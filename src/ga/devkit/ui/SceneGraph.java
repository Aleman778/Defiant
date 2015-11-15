package ga.devkit.ui;

import ga.devkit.editor.EditorObject;
import ga.engine.scene.GameObject;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

public class SceneGraph extends Interface implements Initializable {

    @FXML
    public TreeView<String> graph = new TreeView<>();
    
    public HashMap<String, EditorObject> objects;
    
    private final SceneEditor editor;
    
    public SceneGraph(SceneEditor editor) {
        this.objects = new HashMap<>();
        this.editor = editor;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graph.showRootProperty().setValue(false);
        graph.setOnMouseReleased((MouseEvent event) -> {
//            editor.clearSelectedObjects();
//            for (TreeItem<String> item: graph.getSelectionModel().getSelectedItems()) {
//                EditorObject object = objects.get(item.getValue());
//                if (object != null) {
//                    editor.addSelectedObject(object);
//                }
//            }
            refreshObjectEditor();
            editor.render();
        });
    }
    
    private void refreshObjectEditor() {
        if (graph.getSelectionModel().getSelectedItems().size() == 1) {
            editor.object.setObject(objects.get(graph.getSelectionModel().getSelectedItems().get(0).getValue()));
        } else {
            editor.object.setObject(null);
        }
    }
    
    public void clearSelection() {
        graph.getSelectionModel().clearSelection();
    }
    
    public void setSelection(EditorObject object) {
        TreeItem<String> item = getItem(object.getName());
        if (item != null) {
            clearSelection();
            graph.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            graph.getSelectionModel().select(item);
            refreshObjectEditor();
        }
    }
    
    public void addSelection(EditorObject object) {
        TreeItem<String> item = getItem(object.getName());
        if (item != null) {
            graph.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            graph.getSelectionModel().select(item);
            refreshObjectEditor();
        }
    }
    
    public void setAllSelections(Collection<EditorObject> objects) {
        clearSelection();
        graph.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        for (EditorObject object: objects) {
            TreeItem<String> item = getItem(object.getName());
            if (item != null) {
                graph.getSelectionModel().select(item);
            }
        }
        refreshObjectEditor();
    }
    
    public void addAllSelections(Collection<EditorObject> objects) {
        graph.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        for (EditorObject object: objects) {
            TreeItem<String> item = getItem(object.getName());
            if (item != null) {
                graph.getSelectionModel().select(item);
            }
        }
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
            object.setName(name);
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
    
    private TreeItem<String> getItem(String name) {
        return searchItem(graph.getRoot(), name);
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
}
