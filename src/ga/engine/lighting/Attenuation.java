package ga.engine.lighting;

public class Attenuation {
    
    private final double contstant;
    private final double linear;
    private final double exponent;

    public Attenuation(double contstant, double linear, double exponent) {
        this.contstant = contstant;
        this.linear = linear;
        this.exponent = exponent;
    }

    public double getContstant() {
        return contstant;
    }

    public double getLinear() {
        return linear;
    }

    public double getExponent() {
        return exponent;
    }
}
