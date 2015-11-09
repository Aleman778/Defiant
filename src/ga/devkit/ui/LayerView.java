package ga.devkit.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class LayerView extends Interface implements Initializable {
    
    @FXML
    public TreeView<String> layers;
    
    private TreeItem<String> rootLayer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootLayer = new TreeItem<>("root");
        layers.setRoot(rootLayer);
        layers.showRootProperty().setValue(false);
        CheckBoxTreeItem<String> layer = new CheckBoxTreeItem<>("Layer 1");
        layer.setGraphic(new CheckBox());
        rootLayer.getChildren().add(layer);
    }
}
