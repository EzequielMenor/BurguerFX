package ezequiel.burgerfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtDisponible;
    @FXML
    private TextField txtPrecioBurguer;
    @FXML
    private TextField txtNClientes;
    @FXML
    private Button btnListaEspera;

    @FXML
    private TextArea txtResumen;

    private Burguer burguer;
    private int clientesTerminados = 0;
    private List<String> historialCola = new ArrayList<>();
    private List<String> clienteSinBurguer = new ArrayList<>();

    @FXML
    public void initialize() {
        if (btnListaEspera != null) {
            btnListaEspera.setDisable(true);
        }
    }

    @FXML
    public void guardarReserva(ActionEvent event) {
        try {
            String nombre = txtNombre.getText();
            int disponiblidad = Integer.parseInt(txtDisponible.getText());
            double precioBurguer = Double.parseDouble(txtPrecioBurguer.getText());
            int nClientes = Integer.parseInt(txtNClientes.getText());

            this.burguer = new Burguer(nombre, disponiblidad, precioBurguer, nClientes);
            this.clientesTerminados = 0;
            historialCola.clear();
            clienteSinBurguer.clear();

            txtResumen.setText("Burger del Mes guardada correctamente: \n" + burguer.toString() + "\n");
            if (btnListaEspera != null)
                btnListaEspera.setDisable(true);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            txtResumen.setText("Error al guardar los datos. Revisa que todos los campos son correctos.");
        }
    }

    @FXML
    public void iniciarSimulacion(ActionEvent event) {
        if (burguer == null) {
            txtResumen.setText("Primero guarda la Burger del Mes.");
            return;
        }

        txtResumen.clear();
        historialCola.clear();
        clienteSinBurguer.clear();
        if (btnListaEspera != null)
            btnListaEspera.setDisable(true);
        txtResumen.appendText("Iniciando simulación para " + burguer.getNombre() + "\n");
        txtResumen.appendText("Hamburguesas disponibles: " + burguer.getDisponible() + "\n");
        txtResumen.appendText("Clientes intentando comprar: " + burguer.getnClientes() + "\n\n");

        for (int i = 1; i <= burguer.getnClientes(); i++) {
            Cliente cliente = new Cliente(i, burguer, this);
            Thread hiloCliente = new Thread(cliente);
            hiloCliente.start();
        }

    }

    public void agregarMensaje(String mensaje) {
        txtResumen.appendText(mensaje + "\n");
        historialCola.add(mensaje);
    }

    public synchronized void clienteHaTerminados() {
        clientesTerminados++;

        if (clientesTerminados == burguer.getnClientes()) {
            Platform.runLater(() -> {
                txtResumen.appendText("\n--- Simulación Finalizada ---\n");
                txtResumen.appendText("Todos los clientes han terminado.\n");

                String dineroFormateado = String.format("%.2f", burguer.getRecaudacionTotal());
                txtResumen.appendText("RECAUDACIÓN TOTAL: " + dineroFormateado + " €\n");

                if (btnListaEspera != null) {
                    btnListaEspera.setDisable(false);
                }
            });
        }
    }

    public void verListaEspera(ActionEvent actionEvent) {
        try {
            Stage stageLista = new Stage();
            stageLista.setTitle("INFORME DE INCIDENCIAS - Clientes sin Hamburguesa (" + clienteSinBurguer.size() + ")");

            ListView<String> listView = new ListView<>();
            listView.getItems().addAll(clienteSinBurguer);

            VBox layout = new VBox(10);
            layout.getChildren().add(listView);

            Scene scene = new Scene(layout, 400, 500);
            stageLista.setScene(scene);
            stageLista.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void agregarClienteSinServicio(String s) {
        clienteSinBurguer.add(s);
        Platform.runLater(() -> {
            if (btnListaEspera != null)
                btnListaEspera.setDisable(false);
        });
    }
}
