package ga.engine.scene;

import ga.engine.physics.Vector2D;

public class Transform2D {
    
    public GameObject gameobject;
    public Vector2D position;
    public Vector2D pivot;
    public Vector2D scale;
    public double rotation;
    public int depth;

    public Transform2D(GameObject object, Vector2D position, Vector2D pivot, double rotation, Vector2D scale, int depth) {
        this.gameobject = object;
        this.position = position;
        this.pivot = pivot;
        this.rotation = rotation;
        this.scale = scale;
        this.depth = depth;
    }
    
    public Transform2D(GameObject object, double x, double y) {
        this(object, new Vector2D(x, y), new Vector2D(), 0f, new Vector2D(1, 1), 0);
    }
    
    public Transform2D(GameObject object, Transform2D transform) {
        this(object, transform.position, transform.pivot, transform.rotation, transform.scale, transform.depth);
    }
    
    public Transform2D(GameObject object) {
        this(object, new Vector2D(), new Vector2D(), 0f, new Vector2D(1, 1), 0);
    }
    
    public void translate(Vector2D vector2) {
        position = position.add(vector2);
    }
    
    public void translate(double x, double y) {
        translate(new Vector2D(x, y));
    }
    
    public void rotate(double angle) {
        rotation += angle;
    }
    
    public void scale(Vector2D vector2) {
        scale = scale.add(vector2);
    }
    
    public void scale(double x, double y) {
        scale(new Vector2D(x, y));
    }
    
    public Vector2D localPosition() {
        GameObject parent = gameobject.getParent();
        if (parent != null)
            return parent.getTransform().localPosition().add(position);
        return position;
    }
    
    public double localRotation() {
        GameObject parent = gameobject.getParent();
        if (parent != null)
            return parent.getTransform().localRotation() + rotation;
        return rotation;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
