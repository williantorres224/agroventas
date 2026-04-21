public class Venta {
    private int id; //esto esta pendiente (puede quedar sin usar por ahora)
    private String dni;
    private int edad;
    private int productoId;
    private int cantidad;
    private double total;
    private String fecha; // yyyy-MM-dd

    public Venta(String dni, int edad, int productoId, int cantidad, double total, String fecha) {
        this.dni = dni;
        this.edad = edad;
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.total = total;
        this.fecha = fecha;
    }

    public String getDni() { return dni; }
    public int getEdad() { return edad; }
    public int getProductoId() { return productoId; }
    public int getCantidad() { return cantidad; }
    public double getTotal() { return total; }
    public String getFecha() { return fecha; }
}