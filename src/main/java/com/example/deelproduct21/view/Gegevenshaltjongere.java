package com.example.deelproduct21.view;

import com.example.deelproduct21.App;
import com.example.deelproduct21.controller.DataSource;
import com.example.deelproduct21.controller.DatasetController;
import com.example.deelproduct21.model.DatasetEntry;
import com.example.deelproduct21.model.Persoon;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Gegevenshaltjongere extends Persoon {
    private App app;
    private TextField textHaltjongere;
    private ComboBox<String> geslachtCB, leeftijdCB, migratieachtergrondCB, landCB, strafrechtelijkehandelingCB;
    private ListView<String> strafbarefeitenListView;
    private Label haltjongereLabel;
    private Button toevoegButton, verwijderButton, updateButton, backButton;
    private GridPane grid;
    private Connection con;


    public Gegevenshaltjongere(App app){
        this.app = app;
        haltjongereLabel = new Label("Haltjongere");
        textHaltjongere = new TextField();
        toevoegButton = new Button("Toevoegen");
        updateButton = new Button("Update");
        verwijderButton = new Button("Verwijder");
        backButton = new Button("Terug naar Home");
        strafbarefeitenListView = new ListView<>();
        geslachtCB = new ComboBox<>();
        geslachtCB.getItems().addAll("man", "vrouw");
        leeftijdCB = new ComboBox<>();
        migratieachtergrondCB = new ComboBox<>();
        migratieachtergrondCB.getItems().addAll("westers", "niet-westers");
        landCB = new ComboBox<>();
        strafrechtelijkehandelingCB = new ComboBox<>();

        DatasetController controller = new DatasetController();
        List<DatasetEntry> datasetEntries = controller.fetchDatasetEntries("Leeftijd");
        if (datasetEntries.size() > 2) {
            datasetEntries = datasetEntries.subList(1, datasetEntries.size() - 1);
        }
        for (DatasetEntry entry : datasetEntries) {
            String title = entry.getTitle().replace(" jaar", "");
            leeftijdCB.getItems().add(title);
        }


        grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setPadding(new Insets(10,10,10,10));
        grid.setMinSize(100, 50);
        createLayout();
        vulCombobox();
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
        grid.add(new Label("Geslacht"), 0, 2);
        grid.add(geslachtCB, 1, 2);
        grid.add(new Label("Leeftijd"), 0, 3);
        grid.add(leeftijdCB, 1, 3);
        grid.add(new Label("Migratieachtergrond"), 0, 4);
        grid.add(migratieachtergrondCB, 1, 4);
        grid.add(new Label("Land"), 0, 5);
        grid.add(landCB, 1, 5);
        grid.add(new Label("Strafrechtelijkehandeling"), 0, 6);
        grid.add(strafrechtelijkehandelingCB, 1, 6);
        grid.add(toevoegButton, 1, 7);
        grid.add(strafbarefeitenListView, 0, 8, 2, 1);
        grid.add(updateButton, 0, 9);
        grid.add(verwijderButton, 1, 9);

    }

    private void create() {
        String haltjongereText = textHaltjongere.getText();
        String geslacht = geslachtCB.getValue();
        String leeftijd = leeftijdCB.getValue();
        String migratieachtergrond = migratieachtergrondCB.getValue();
        String land = landCB.getValue();
        String strafrechtelijkehandeling = strafrechtelijkehandelingCB.getValue();
        if (!haltjongereText.isEmpty() && geslacht != null && leeftijd != null && migratieachtergrond != null && land != null && strafrechtelijkehandeling != null) {
            try {
                con = DataSource.getConnection();
                String sql = "INSERT INTO gegevenshaltjongeren (haltjongereID, geslachtID, leeftijdID, migratieachtergrondID, landID, strafrechtelijkehandelingID) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, haltjongereText);
                pstmt.setString(2, geslacht);
                pstmt.setString(3, leeftijd);
                pstmt.setString(4, migratieachtergrond);
                pstmt.setString(5, land);
                pstmt.setString(6, strafrechtelijkehandeling);
                pstmt.executeUpdate();

                String item = haltjongereText + " - " + geslacht + " - " + leeftijd + " - " + migratieachtergrond + " - " + land + " - " + strafrechtelijkehandeling;
                strafbarefeitenListView.getItems().add(item);
                super.setHaltjongere(haltjongereText);
                textHaltjongere.clear();
                geslachtCB.setValue(null);
                leeftijdCB.setValue(null);
                migratieachtergrondCB.setValue(null);
                landCB.setValue(null);
                strafrechtelijkehandelingCB.setValue(null);

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
            String[] parts = selectedItem.split(" - ");
            String haltjongereID = parts[0];
            try {
                con = DataSource.getConnection();
                String sql = "DELETE FROM gegevenshaltjongeren WHERE haltjongereID = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, haltjongereID);
                pstmt.executeUpdate();

                strafbarefeitenListView.getItems().remove(selectedItem);

                super.setHaltjongere(haltjongereID);
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
        String newHaltjongereText = textHaltjongere.getText();
        String newGeslacht = geslachtCB.getValue();
        String newLeeftijd = leeftijdCB.getValue();
        String newMigratieachtergrond = migratieachtergrondCB.getValue();
        String newLand = landCB.getValue();
        String newStrafrechtelijkehandeling = strafrechtelijkehandelingCB.getValue();
        if (selectedItem != null && !newHaltjongereText.isEmpty() && newGeslacht != null && newLeeftijd != null && newMigratieachtergrond != null && newLand != null && newStrafrechtelijkehandeling != null) {
            String[] parts = selectedItem.split(" - ");
            String haltjongereID = parts[0];
            try {
                con = DataSource.getConnection();
                String sql = "UPDATE gegevenshaltjongeren SET haltjongereID = ?, geslachtID = ?, leeftijdID = ?, migratieachtergrondID = ?, landID = ?, strafrechtelijkehandelingID = ? WHERE haltjongereID = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, newHaltjongereText);
                pstmt.setString(2, newGeslacht);
                pstmt.setString(3, newLeeftijd);
                pstmt.setString(4, newMigratieachtergrond);
                pstmt.setString(5, newLand);
                pstmt.setString(6, newStrafrechtelijkehandeling);
                pstmt.setString(7, haltjongereID);
                pstmt.executeUpdate();

                int selectedIndex = strafbarefeitenListView.getSelectionModel().getSelectedIndex();
                String newItem = newHaltjongereText + " - " + newGeslacht + " - " + newLeeftijd + " - " + newMigratieachtergrond + " - " + newLand + " - " + newStrafrechtelijkehandeling;
                strafbarefeitenListView.getItems().set(selectedIndex, newItem);
                textHaltjongere.clear();
                geslachtCB.setValue(null);
                leeftijdCB.setValue(null);
                migratieachtergrondCB.setValue(null);
                landCB.setValue(null);
                strafrechtelijkehandelingCB.setValue(null);

                super.setHaltjongere(haltjongereID);
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


    private void vulCombobox() {
        try (Connection con = DataSource.getConnection()) {
            String sqlLand = "SELECT landID FROM land";
            PreparedStatement pstmtLand = con.prepareStatement(sqlLand);
            ResultSet rsLand = pstmtLand.executeQuery();
            while (rsLand.next()) {
                landCB.getItems().add(rsLand.getString("landID"));
            }

            String sqlStrafrechtelijkeHandeling = "SELECT strafrechtelijkehandelingID FROM strafrechtelijkehandeling";
            PreparedStatement pstmtStrafrechtelijkeHandeling = con.prepareStatement(sqlStrafrechtelijkeHandeling);
            ResultSet rsStrafrechtelijkeHandeling = pstmtStrafrechtelijkeHandeling.executeQuery();
            while (rsStrafrechtelijkeHandeling.next()) {
                strafrechtelijkehandelingCB.getItems().add(rsStrafrechtelijkeHandeling.getString("strafrechtelijkehandelingID"));
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    private void vulListView() {
        try (Connection con = DataSource.getConnection()) {
            String sql = "SELECT haltjongereID, geslachtID, leeftijdID, migratieachtergrondID, landID, strafrechtelijkehandelingID FROM gegevenshaltjongeren";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            strafbarefeitenListView.getItems().clear();
            while (rs.next()) {
                String item = rs.getString("haltjongereID") + " - " + rs.getString("geslachtID") + " - " + rs.getString("leeftijdID") + " - " + rs.getString("migratieachtergrondID") + " - " + rs.getString("landID") + " - " + rs.getString("strafrechtelijkehandelingID");
                strafbarefeitenListView.getItems().add(item);
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    public GridPane getGrid() {
        return grid;
    }
}
