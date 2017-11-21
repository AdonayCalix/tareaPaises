package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {

    public String CONEXION_STRING = "jdbc:mysql://127.0.0.1:3306/world";
    public String USUARIO = "root";
    public String PASSWD = "mooseladra";
    private static Connection conexion;

    public static Connection getConexion() {
        return conexion;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        abrirConexion();

        Parent root = FXMLLoader.load(getClass().getResource("vistas/sample.fxml"));
        primaryStage.setTitle("Paises");
        primaryStage.setScene(new Scene(root, 500, 390));
        primaryStage.show();
    }

    public void abrirConexion() {
        try {
            conexion = DriverManager.getConnection(CONEXION_STRING, USUARIO, PASSWD);
        } catch (SQLException e) {
            JOptionPane.showConfirmDialog(null, "No se establecio la conexion: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
