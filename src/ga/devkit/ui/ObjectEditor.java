package ga.devkit.ui;

import ga.devkit.editor.EditorObject;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.util.Callback;

public class ObjectEditor extends Interface implements Initializable {

    @FXML
    public Label title;
    public TableView<Attribute> tableAttri;
    public TableColumn<Attribute, String> columnAttri;
    public TableColumn<Attribute, String> columnValue;
    
    private ObservableList<Attribute> attributes;
    private EditorObject selectedObject;
    
    public ObjectEditor() {
        
    }
        
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableAttri.setPlaceholder(new Label("There are no attributes for this object"));
        attributes = 
            FXCollections.observableArrayList(
                new Attribute("Test", "Value"),
                new Attribute("Test2", "Value2"),
                new Attribute("Test3", "Value3")
            );
        
        columnAttri.setCellValueFactory((TableColumn.CellDataFeatures<Attribute, String> param) -> param.getValue().attriName);
        columnValue.setCellValueFactory((TableColumn.CellDataFeatures<Attribute, String> param) -> param.getValue().attriValue);
        columnValue.setOnEditCommit((TableColumn.CellEditEvent<Attribute, String> t) -> {
            ((Attribute) t.getTableView().getItems().get(t.getTablePosition().getRow())).setValue(t.getNewValue());
            if (selectedObject != null) {
                selectedObject.setAttribute(t.getRowValue().getName(), t.getNewValue());
            }
        });
        columnValue.setCellFactory(TextFieldTableCell.forTableColumn());
        tableAttri.setItems(attributes);
        setObject(null);
    }
    
    public void setObject(EditorObject selection) {
        selectedObject = selection;
        if (selectedObject == null) {
            title.setText("Object Editor - No object selected");
        } else {
            title.setText("Object Editor - " + selection.getName());
            attributes.clear();
            for (String attri: selectedObject.getAllAttributes()) {
                String value = selectedObject.getAttribute(attri);
                if (value == null) {
                    value = "";
                }
                attributes.add(new Attribute(attri, value));
            }
        }
    }
    
    public static class Attribute {
        
        private final SimpleStringProperty attriName;
        private final SimpleStringProperty attriValue;

        public Attribute(String attriName, String attriValue) {
            this.attriName = new SimpleStringProperty(attriName);
            this.attriValue = new SimpleStringProperty(attriValue);
        }
        
        public void setName(String name) {
            attriName.set(name);
        }
        
        public String getName() {
            return attriName.get();
        }
        
        public void setValue(String value) {
            attriValue.set(value);
        }
        
        public String getValue() {
            return attriValue.get();
        }
    }
}
