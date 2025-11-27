package ezequiel.burgerfx;

import javafx.application.Platform;

public class Cliente implements Runnable {
    private int id;
    private Burguer burguer;
    private HelloController controller;

    public Cliente(int id, Burguer burguer, HelloController controller) {
        this.id = id;
        this.burguer = burguer;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    controller.agregarMensaje("Cliente #" + id + " llega a caja...");
                }
            });

            boolean haPasadoPorCaja = false;
            try {
                burguer.getSemaforo().acquire();
                haPasadoPorCaja = true;

                Platform.runLater(() -> controller.agregarMensaje("Cliente #" + id + " --> Entra a CAJA y pide..."));

                boolean tieneBurguer = burguer.venderBurguer();

                if (tieneBurguer) {
                    boolean esVip = Math.random() < 0.20;
                    burguer.cobrar(esVip);
                    if (esVip) {
                        Platform.runLater(() -> controller
                                .agregarMensaje("Cliente #" + id + " 🌟 ¡ES VIP! Compra con 20% de descuento."));
                    } else {
                        Platform.runLater(() -> controller.agregarMensaje(
                                "Cliente #" + id + " compra su hamburguesa (" + burguer.getPrecioBurguer() + " €)."));
                    }

                    Thread.sleep((int) (Math.random() * 2000) + 1000);
                    Platform.runLater(
                            () -> controller.agregarMensaje("Cliente #" + id + " ha terminado y se va satisfecho."));
                } else {
                    Thread.sleep(1000);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            controller.agregarMensaje("Cliente #" + id + " no pudo comprar: burguers agotadas");
                        }
                    });

                    controller.agregarClienteSinServicio("Cliente #" + id + " se fue insatisfecho (Sin hamburguesa)");

                }
            } finally {
                if (haPasadoPorCaja) {
                    burguer.getSemaforo().release();
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            controller.clienteHaTerminados();
        }
    }
}
