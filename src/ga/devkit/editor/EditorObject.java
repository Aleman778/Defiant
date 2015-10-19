package ga.devkit.editor;

import ga.engine.scene.GameObject;
import ga.engine.scene.Transform2D;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class EditorObject extends GameObject {
    
    private static Image NONE_RENDERABLE_IMAGE = new Image("ga/devkit/editor/nonrenderable.png");
    private Node node = null;

    public EditorObject(Transform2D transform) {
        super(transform);
        setDefaultNode();
    }

    public EditorObject(double x, double y, double z) {
        super(x, y);
        setDefaultNode();
    }

    public EditorObject() {
        super();
        setDefaultNode();
    }
    
    private void setNode(Node node) {
        this.node = node;
        this.node.setOnMousePressed((MouseEvent event) -> {
            System.out.println("Pressed on node!");
        });
    }
    
    private void setDefaultNode() {
        setNode(new ImageView(NONE_RENDERABLE_IMAGE));
    }
    
    public Node getNode() {
        return node;
    }

    @Override
    public void render(GraphicsContext group) {
//        group.getChildren().add(node);
//        node.getTransforms().clear();
//        Vector3D rotation = transform.localRotation();
//        Vector3D position = transform.localPosition();
//        Rotate rx = new Rotate(rotation.x, 0, 0, 0, Rotate.X_AXIS);
//        Rotate ry = new Rotate(rotation.y, 0, 0, 0, Rotate.Y_AXIS);
//        Rotate rz = new Rotate(rotation.z, 0, 0, 0, Rotate.Z_AXIS);
//        node.getTransforms().addAll(rx, ry, rz);
//        node.setTranslateX((int) position.x);
//        node.setTranslateY((int) position.y);
//        node.setTranslateZ((int) position.z);
//        node.setScaleX(transform.scale.x);
//        node.setScaleY(transform.scale.y);
//        node.setScaleZ(transform.scale.z);
//        
//        for (GameObject child : getChildren()) {
//            ((EditorObject) child).render(group);
//        }
    }
}