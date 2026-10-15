public final class Complex {
    private static final double EPS = 1e-12;

    private final double re;
    private final double im;

    public Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    public Complex add(Complex o) {
        return new Complex(re + o.re, im + o.im);
    }

    public Complex subtract(Complex o) {
        return new Complex(re - o.re, im - o.im);
    }

    public Complex multiply(Complex o) {
        return new Complex(re * o.re - im * o.im, re * o.im + im * o.re);
    }

    public Complex divide(Complex o) {
        if (o.isZero()) {
            throw new IllegalArgumentException("Деление на ноль");
        }
        double d = o.re * o.re + o.im * o.im;
        return new Complex((re * o.re + im * o.im) / d, (im * o.re - re * o.im) / d);
    }

    public double abs() {
        return Math.sqrt(re * re + im * im);
    }

    public boolean isZero() {
        return abs() < EPS;
    }

    private static String format(double v) {
        v += 0.0;
        if (v == (long) v && Math.abs(v) < 1e15) {
            return String.valueOf((long) v);
        }
        return String.format("%.4f", v).replace(',', '.');
    }

    public String toString() {
        String sign = im < 0 ? "-" : "+";
        return format(re) + sign + format(Math.abs(im)) + "i";
    }
}