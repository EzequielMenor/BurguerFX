package ezequiel.burgerfx;

import java.util.concurrent.Semaphore;

public class Burguer {
    private String nombre;
    private int disponible;
    private double precioBurguer;
    private int nClientes;
    private double recaudacionTotal = 0;

    private Semaphore semaforo;
    private int hamburguesasDisponibles;

    public Burguer(String nombre, int disponible, double precioBurguer, int nClientes) {
        this.nombre = nombre;
        this.disponible = disponible;
        this.precioBurguer = precioBurguer;
        this.nClientes = nClientes;

        this.semaforo = new Semaphore(1, true);

        this.hamburguesasDisponibles = disponible;
    }

    public boolean venderBurguer(){
        if (hamburguesasDisponibles > 0) {
            hamburguesasDisponibles--;
            return true;
        } else {
            return false;
        }
    }

    public synchronized void cobrar(boolean esVip){
        if (esVip) {
            double precioConDescuento = precioBurguer * 0.80;
            recaudacionTotal += precioConDescuento;
        } else {
            recaudacionTotal += precioBurguer;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public int getDisponible() {
        return disponible;
    }

    public double getPrecioBurguer() {return precioBurguer;}

    public int getnClientes() {return nClientes;}

    public double getRecaudacionTotal() {return recaudacionTotal;}

    public int getHamburguesasDisponibles() {return hamburguesasDisponibles;}

    public void setHamburguesasDisponibles(int hamburguesasDisponibles) {
        this.hamburguesasDisponibles = hamburguesasDisponibles;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDisponible(int aforo) {
        this.disponible = aforo;
    }

    public void setPrecioBurguer(double precioBurguer) {
        this.precioBurguer = precioBurguer;
    }

    public void setnClientes(int nClientes) {
        this.nClientes = nClientes;
    }

    public Semaphore getSemaforo() {
        return semaforo;
    }

    public void setSemaforo(Semaphore semaforo) {
        this.semaforo = semaforo;
    }


    @Override
    public String toString() {
        return "Nombre: " +
                nombre +
                "\nHamburguesas disponibles: " +
                disponible +
                "\nPrecio: " +
                precioBurguer +
                "\nNúmero de clientes: " +
                nClientes;
    }
}
