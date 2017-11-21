package sample.controladores;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import sample.Main;

import javax.swing.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Controller {

    public TextField txtBusqueda;
    public ListView<String> listPais;
    public ListView<String> listCiudades;
    private ObservableList<String> paises = FXCollections.observableArrayList();
    private ObservableList<String> ciudades = FXCollections.observableArrayList();
    private String paisesSleccionados;
    private String ciudadSeleccionada;
    public Label txtId;
    public Label txtName;
    public Label txtCodigo;
    public Label txtDistrito;
    public Label txtPoblacion;
    public VBox vbox;

    @FXML
    public void initialize() {
        listPais.setItems(paises);
        listCiudades.setItems(ciudades);

        listPais.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                paisesSleccionados = newValue;
                ciudades.clear();
                borrarLabel();
                try {
                    Connection con = Main.getConexion();
                    Statement stmt = con.createStatement();
                    String sql = "SELECT Name FROM city WHERE countrycode=(SELECT Code FROM country WHERE Name='" + paisesSleccionados + "')";
                    ResultSet resultSet = stmt.executeQuery(sql);
                    while (resultSet.next()) {
                        ciudades.add(resultSet.getString("Name"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        listCiudades.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                vbox.setVisible(true);
                ciudadSeleccionada = newValue;
                try {
                    Connection con = Main.getConexion();
                    Statement stmt = con.createStatement();
                    String sql = "SELECT * FROM city WHERE Name='" + ciudadSeleccionada + "'";
                    ResultSet resultSet = stmt.executeQuery(sql);
                    if (resultSet.next()){
                        txtId.setText(String.valueOf(resultSet.getInt("ID")));
                        txtName.setText(resultSet.getString("Name"));
                        txtCodigo.setText(resultSet.getString("CountryCode"));
                        txtDistrito.setText(resultSet.getString("District"));
                        txtPoblacion.setText(String.valueOf(resultSet.getInt("Population")));
                    }
                    resultSet.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void buscarPais(KeyEvent keyEvent) {

        paises.clear();
        vbox.setVisible(false);
        String nombreBusqueda = txtBusqueda.getText().trim();

        if (nombreBusqueda.length() >= 1) {
            Connection con = Main.getConexion();

            try {
                Statement statement = con.createStatement();
                String sql = "SELECT Name FROM country WHERE Name LIKE '" + nombreBusqueda + "%' ";
                ResultSet resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    paises.add(resultSet.getString("Name"));
                }
                resultSet.close();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }

        }
    }

    public  void borrarLabel(){
        txtId.setText("");
        txtDistrito.setText("");
        txtName.setText("");
        txtPoblacion.setText("");
        txtCodigo.setText("");
    }
}

