package com.dpardo.strike.ui.read_only;

import com.dpardo.strike.domain.Pais;
import com.dpardo.strike.repository.PaisRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class HomeController {

    // --- TUS BOTONES DEL MENÚ SUPERIOR (YA ESTABAN) ---
    @FXML
    private Button jugadoresButton;
    @FXML
    private Button estadisticasButton;
    @FXML
    private Button equiposButton;
    @FXML
    private Button partidosButton;
    @FXML
    private Button clasificacionButton;
    @FXML
    private Button resumenButton;
    @FXML
    private Button ligaButton;

    // --- VARIABLES AÑADIDAS PARA LA NUEVA LÓGICA ---
    @FXML
    private BorderPane mainBorderPane; // Asegúrate que tu BorderPane raíz tenga este fx:id

    @FXML
    private VBox paisContenedor; // El VBox dentro del ScrollPane con su fx:id

    // Instancia del Repository para acceder a la base de datos
    private final PaisRepository paisRepository = new PaisRepository();


    /**
     * Este método se ejecuta automáticamente cuando la vista se carga.
     */
    @FXML
    public void initialize() {
        // Llamamos al método para que cargue los países en cuanto la app inicie
        cargarPaises();
    }

    /**
     * Carga los países desde la base de datos y los muestra en la barra lateral.
     */
    public void cargarPaises() {
        // Limpiamos el contenedor por si había algo antes
        if (paisContenedor != null) {
            paisContenedor.getChildren().clear();
        }

        // Obtenemos la lista de países usando nuestro Repository
        List<Pais> listaDePaises = paisRepository.obtenerTodosLosPaises();
        System.out.println("Países obtenidos de la BD: " + listaDePaises.size());

        // Recorremos la lista y creamos un item gráfico por cada país
        for (Pais pais : listaDePaises) {
            try {
                // Cargamos la plantilla FXML para una fila
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/dpardo/strike/ui/read_only/Pais-view.fxml"));
                Node nodoPaisItem = loader.load();

                // Obtenemos el controlador de esa fila específica
                PaisItemController paisController = loader.getController();

                // Construimos la ruta a la imagen de la bandera
                String rutaImagen = "/images/flags/" + pais.codigo() + ".png";
                Image bandera = new Image(getClass().getResourceAsStream(rutaImagen));

                // Enviamos los datos (nombre y bandera) a la plantilla para que se muestren
                paisController.setData(pais.nombre(), bandera);

                // Añadimos la fila ya lista a nuestro contenedor VBox en la GUI
                paisContenedor.getChildren().add(nodoPaisItem);

            } catch (IOException | NullPointerException e) {
                System.err.println("Error al cargar item para " + pais.nombre() + ". ¿Falta la imagen " + pais.codigo() + ".png en resources?");
                e.printStackTrace();
            }
        }
    }
}