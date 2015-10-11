package ga.devkit.ui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.FlowPane;

public class ProjectView extends Interface implements Initializable {
    
    @FXML
    public TreeView<String> tree = new TreeView<>();
    
    public FolderView folder;
    public AssetPreview preview;
    
    public ProjectView() {
        folder = new FolderView();
        preview = new AssetPreview();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshTree();
    }

    @Override
    public void load() {
        super.load();
        folder.load();
        preview.load();
    }
    
    public void refreshTree() {
        TreeItem<String> root = new TreeItem<>("res");
        List<File> folders = findFolders(new ArrayList<>(), root, new File("res"));
        tree.setRoot(root);
    }
    
    private List<File> findFolders(List<File> result, TreeItem<String> item, File file) {
        File[] files = file.listFiles();
        if (files == null)
            return result;
        
        for (File f: files) {
            if (f.isDirectory()) {
                TreeItem<String> i = new TreeItem<>(f.getName());
                findFolders(result, i, f);
                item.getChildren().add(i);
                result.add(f);
            }
        }
        return result;
    }
}
