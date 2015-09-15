package ga.engine.physics;

public class Vector2D {
    
    public double x = 0, y = 0;

    public Vector2D(double dX, double dY) {
        this.x = dX;
        this.y = dY;
    }
    
    public Vector2D() {}
    
    public double length() {
        return Math.sqrt(x * x + y * y);
    }
    
    public Vector2D add(Vector2D vector) {
        return new Vector2D(x + vector.x, y + vector.y);
    }
    
    public Vector2D sub(Vector2D vector) {
        return new Vector2D(x - vector.x, y - vector.y);
    }
    
    public Vector2D mul(double scale) {
        return new Vector2D(x * scale, y * scale);
    }
    
    public Vector2D mul(Vector2D vector) {
        return new Vector2D(x * vector.x, y * vector.y);
    }
    
    public double dot(Vector2D vector) {
        return x * vector.x + y * vector.y;
    }
    
    public Vector2D normalize() {
        double length = length();
        if (length == 0) {
            return new Vector2D();
        }
        return new Vector2D(x / length, y / length);
    }
    
    public Vector2D project(Vector2D vector) {
        return new Vector2D((dot(vector) / (vector.x * vector.x + vector.y * vector.y)) * vector.x, (dot(vector) / (vector.x * vector.x + vector.y * vector.y)) * vector.y);
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }

    public boolean equals(Vector2D vector) {
        return (x == vector.x && y == vector.y);
    }
}
