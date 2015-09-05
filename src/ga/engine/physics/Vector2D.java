package ga.engine.physics;

public class Vector2D {
    
    public double dX = 0, dY = 0;

    public Vector2D(double dX, double dY) {
        this.dX = dX;
        this.dY = dY;
    }
    
    public Vector2D() {}
    
    public double length() {
        return Math.sqrt(dX * dX + dY * dY);
    }
    
    public Vector2D add(Vector2D vector) {
        return new Vector2D(dX + vector.dX, dY + vector.dY);
    }
    
    public Vector2D sub(Vector2D vector) {
        return new Vector2D(dX - vector.dX, dY - vector.dY);
    }
    
    public Vector2D scale(double scale) {
        return new Vector2D(dX * scale, dY * scale);
    }
    
    public double dot(Vector2D vector) {
        return dX * vector.dX + dY * vector.dY;
    }
    
}
