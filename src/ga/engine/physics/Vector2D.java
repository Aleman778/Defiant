package ga.engine.physics;

public class Vector2D {

    public double x, y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2D() {
        this(0, 0);
    }

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

    public double cross(Vector2D vector) {
        return x * vector.y - y * vector.x;
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }

    public boolean equals(Vector2D vector) {
        return (x == vector.x && y == vector.y);
    }

    public static Vector2D intersects(Vector2D V1Base, Vector2D V1Size, Vector2D V2Base, Vector2D V2Size) {
        if (V1Size.cross(V2Size) == 0) {
            return null;
        }
        Vector2D a = V2Base.sub(V1Base);
        double b = V1Size.cross(V2Size);
        double V1Scale = a.cross(V2Size) / b,
                V2Scale = a.cross(V1Size) / b;
        if (V1Scale < 0 || V1Scale > 1 || V2Scale < 0 || V2Scale > 1) {
            return null;
        }
        return V1Base.add(V1Size.mul(V1Scale));
    }
    
    public static Vector2D intersects(Vector2D V1Base, Vector2D V1Size, Vector2D V2Base, Vector2D V2Size, boolean V1Infinite) {
        if (V1Size.cross(V2Size) == 0) {
            return null;
        }
        Vector2D a = V2Base.sub(V1Base);
        double b = V1Size.cross(V2Size);
        double V1Scale = a.cross(V2Size) / b,
                V2Scale = a.cross(V1Size) / b;
        if (V1Scale < 0 || (V1Scale > 1 && !V1Infinite) || V2Scale < 0 || V2Scale > 1) {
            return null;
        }
        return V1Base.add(V1Size.mul(V1Scale));
    }
    
    public static double intersectScale(Vector2D V1Base, Vector2D V1Size, Vector2D V2Base, Vector2D V2Size, boolean V1Infinite) {
        if (V1Size.cross(V2Size) == 0) {
            return 0;
        }
        Vector2D a = V2Base.sub(V1Base);
        double b = V1Size.cross(V2Size);
        double V1Scale = a.cross(V2Size) / b,
                V2Scale = a.cross(V1Size) / b;
        if (V1Scale < 0 || (V1Scale > 1 && !V1Infinite) || V2Scale < 0 || V2Scale > 1) {
            return 0;
        }
        return V1Scale;
    }
}
