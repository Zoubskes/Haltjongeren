package com.example.deelproduct21.view;

import com.example.deelproduct21.App;
import com.example.deelproduct21.controller.DataSource;
import com.example.deelproduct21.model.Persoon;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Strafbarefeitenhaltjongere extends Persoon {

    private App app;
    private TextField textHaltjongere;
    private ComboBox<String> strafbarefeitenCB;
    private ComboBox<String> delictjarenCB;
    private ListView<String> strafbarefeitenListView;
    private Label haltjongereLabel;
    private Button toevoegButton, verwijderButton, updateButton, backButton;
    private GridPane grid;
    private Connection con;


    public Strafbarefeitenhaltjongere(App app) {
        this.app = app;
        haltjongereLabel = new Label("Haltjongere");
        textHaltjongere = new TextField();
        toevoegButton = new Button("Toevoegen");
        updateButton = new Button("Update");
        verwijderButton = new Button("Verwijder");
        backButton = new Button("Terug naar Home");
        strafbarefeitenListView = new ListView<>();
        strafbarefeitenCB = new ComboBox<>();
        delictjarenCB = new ComboBox<>();


        grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setMinSize(100, 50);
        createLayout();
        vulComboBoxes();
        vulListView();
    }


    private void createLayout() {
        backButton.setOnAction(e -> app.showHomePage());
        toevoegButton.setOnAction(e -> create());
        verwijderButton.setOnAction(e -> delete());
        updateButton.setOnAction(e -> update());

        grid.add(backButton, 0, 0);
        grid.add(haltjongereLabel, 0, 1);
        grid.add(textHaltjongere, 1, 1);
        grid.add(new Label("Strafbarefeiten"), 0, 2);
        grid.add(strafbarefeitenCB, 1, 2);
        grid.add(new Label("Delictjaar"), 0, 3);
        grid.add(delictjarenCB, 1, 3);
        grid.add(toevoegButton, 1, 4);
        grid.add(strafbarefeitenListView, 0, 5, 2, 1);
        grid.add(updateButton, 0, 6);
        grid.add(verwijderButton, 1, 6);

    }

    private void create() {
        String haltjongereText = textHaltjongere.getText();
        String strafbarefeit = strafbarefeitenCB.getValue();
        String delictjaar = delictjarenCB.getValue();
        if (!haltjongereText.isEmpty() && strafbarefeit != null && delictjaar != null) {
            try {
                con = DataSource.getConnection();
                String sql = "INSERT INTO strafbaarfeitenhaltjongeren (haltjongereID, strafbaarfeitID, delictjaarID) VALUES (?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, haltjongereText);
                pstmt.setString(2, strafbarefeit);
                pstmt.setString(3, delictjaar);
                pstmt.executeUpdate();

                String item = haltjongereText + " - " + strafbarefeit + " - " + delictjaar;
                strafbarefeitenListView.getItems().add(item);
                textHaltjongere.clear();
                strafbarefeitenCB.setValue(null);
                delictjarenCB.setValue(null);
                super.setHaltjongere(haltjongereText);
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText(super.toString() + "is toegevoegd aan de database");
                alert.showAndWait();
            } catch (SQLException se) {
                System.out.println(se.getMessage());
            } finally {
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException se) {
                    System.out.println(se.getMessage());
                }
            }
            }
        }

    private void delete() {
        String selectedItem = strafbarefeitenListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                con = DataSource.getConnection();
                String[] parts = selectedItem.split(" - ");
                String haltjongereText = parts[0];
                String sql = "DELETE FROM strafbaarfeitenhaltjongeren WHERE haltjongereID = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, haltjongereText);
                pstmt.executeUpdate();

                strafbarefeitenListView.getItems().remove(selectedItem);

                super.setHaltjongere(haltjongereText);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText(super.toString() + "is verwijderd van de database");
                alert.showAndWait();
            } catch (SQLException se) {
                System.out.println(se.getMessage());
            } finally {
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException se) {
                    System.out.println(se.getMessage());
                }
            }
        }
    }

    private void update() {
        String selectedItem = strafbarefeitenListView.getSelectionModel().getSelectedItem();
        String haltjongereText = textHaltjongere.getText();
        String strafbarefeit = strafbarefeitenCB.getValue();
        String delictjaar = delictjarenCB.getValue();
        if (selectedItem != null && !haltjongereText.isEmpty() && strafbarefeit != null && delictjaar != null) {
            try {
                con = DataSource.getConnection();
                String[] parts = selectedItem.split(" - ");
                String oldHaltjongereText = parts[0];
                String sql = "UPDATE strafbaarfeitenhaltjongeren SET haltjongereID = ?, strafbaarfeitID = ?, delictjaarID = ? WHERE haltjongereID = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, haltjongereText);
                pstmt.setString(2, strafbarefeit);
                pstmt.setString(3, delictjaar);
                pstmt.setString(4, oldHaltjongereText);
                pstmt.executeUpdate();

                int selectedIndex = strafbarefeitenListView.getSelectionModel().getSelectedIndex();
                String newItem = haltjongereText + " - " + strafbarefeit + " - " + delictjaar;
                strafbarefeitenListView.getItems().set(selectedIndex, newItem);
                textHaltjongere.clear();
                strafbarefeitenCB.setValue(null);
                delictjarenCB.setValue(null);

                super.setHaltjongere(haltjongereText);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText(super.toString() + "is ge-update in de database");
                alert.showAndWait();
            } catch (SQLException se) {
                System.out.println(se.getMessage());
            } finally {
                try {
                    if (con != null) {
                        con.close();
                    }
                } catch (SQLException se) {
                    System.out.println(se.getMessage());
                }
            }
        }
    }
        private void vulComboBoxes () {
            try (Connection con = DataSource.getConnection()) {
                String sqlStrafbarefeiten = "SELECT strafbaarfeitID FROM strafbaarfeit";
                PreparedStatement pstmtStrafbarefeiten = con.prepareStatement(sqlStrafbarefeiten);
                ResultSet rsStrafbarefeiten = pstmtStrafbarefeiten.executeQuery();
                while (rsStrafbarefeiten.next()) {
                    strafbarefeitenCB.getItems().add(rsStrafbarefeiten.getString("strafbaarfeitID"));
                }

                String sqlDelictjaren = "SELECT delictjaarID FROM delictjaar";
                PreparedStatement pstmtDelictjaren = con.prepareStatement(sqlDelictjaren);
                ResultSet rsDelictjaren = pstmtDelictjaren.executeQuery();
                while (rsDelictjaren.next()) {
                    delictjarenCB.getItems().add(rsDelictjaren.getString("delictjaarID"));
                }
            } catch (SQLException se) {
                System.out.println(se.getMessage());
            }
        }

    private void vulListView() {
        try (Connection con = DataSource.getConnection()) {
            String sql = "SELECT haltjongereID, strafbaarfeitID, delictjaarID FROM strafbaarfeitenhaltjongeren";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            strafbarefeitenListView.getItems().clear();
            while (rs.next()) {
                String item = rs.getString("haltjongereID") + " - " + rs.getString("strafbaarfeitID") + " - " + rs.getString("delictjaarID");
                strafbarefeitenListView.getItems().add(item);
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }
        public GridPane getGrid () {
            return grid;
        }
    }

