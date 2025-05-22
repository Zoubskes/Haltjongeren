package com.example.deelproduct21.view;

import com.example.deelproduct21.App;
import com.example.deelproduct21.controller.DataSource;
import com.example.deelproduct21.controller.DatasetController;
import com.example.deelproduct21.model.DatasetEntry;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Hoeveelheidhaltjongere {

    private App app;
    private ComboBox<String> jaartalCB, aantalhaltjongerenCB, leeftijdCB;
    private ListView<String> hoeveelheidhaltjongereListView;
    private Label jaartalLabel;
    private Button toevoegButton, verwijderButton, updateButton, backButton;
    private GridPane grid;
    private Connection con;


    public Hoeveelheidhaltjongere(App app){
        this.app = app;
        jaartalLabel = new Label("Jaartal");
        toevoegButton = new Button("Toevoegen");
        updateButton = new Button("Update");
        verwijderButton = new Button("Verwijder");
        backButton = new Button("Terug naar Home");
        hoeveelheidhaltjongereListView = new ListView<>();
        jaartalCB = new ComboBox<>();
        aantalhaltjongerenCB = new ComboBox<>();
        leeftijdCB = new ComboBox<>();

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
        grid.add(jaartalLabel, 0, 1);
        grid.add(jaartalCB, 1, 1);
        grid.add(new Label("Aantalhaltjongere"), 0, 2);
        grid.add(aantalhaltjongerenCB, 1, 2);
        grid.add(new Label("Leeftijd"), 0, 3);
        grid.add(leeftijdCB, 1, 3);
        grid.add(toevoegButton, 1, 4);
        grid.add(hoeveelheidhaltjongereListView, 0, 5, 2, 1);
        grid.add(updateButton, 0, 6);
        grid.add(verwijderButton, 1, 6);

    }

    private void create() {
        String jaartal = jaartalCB.getValue();
        String aantalhaltjongeren = aantalhaltjongerenCB.getValue();
        String leeftijd = leeftijdCB.getValue();

        if (jaartal != null && aantalhaltjongeren != null && leeftijd != null) {
            try (Connection con = DataSource.getConnection()) {
                String sql = "INSERT INTO hoeveelheidhaltjongeren (jaartalID, aantalhaltjongerenID, leeftijdID) VALUES (?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, jaartal);
                pstmt.setString(2, aantalhaltjongeren);
                pstmt.setString(3, leeftijd);
                pstmt.executeUpdate();

                String newItem = jaartal + " - " + aantalhaltjongeren + " - " + leeftijd;
                hoeveelheidhaltjongereListView.getItems().add(newItem);

            } catch (SQLException se) {
                System.out.println(se.getMessage());
            }
        }
    }

    private void delete() {
        String selectedItem = hoeveelheidhaltjongereListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String[] parts = selectedItem.split(" - ");
            String jaartal = parts[0];
            String aantalhaltjongeren = parts[1];
            String leeftijd = parts[2];

            try (Connection con = DataSource.getConnection()) {
                String sql = "DELETE FROM hoeveelheidhaltjongeren WHERE jaartalID = ? AND aantalhaltjongerenID = ? AND leeftijdID = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, jaartal);
                pstmt.setString(2, aantalhaltjongeren);
                pstmt.setString(3, leeftijd);
                pstmt.executeUpdate();

                hoeveelheidhaltjongereListView.getItems().remove(selectedItem);
            } catch (SQLException se) {
                System.out.println(se.getMessage());
            }
        }
    }

    private void update() {
        String selectedItem = hoeveelheidhaltjongereListView.getSelectionModel().getSelectedItem();
        String newJaartal = jaartalCB.getValue();
        String newAantalhaltjongeren = aantalhaltjongerenCB.getValue();
        String newLeeftijd = leeftijdCB.getValue();

        if (selectedItem != null && newJaartal != null && newAantalhaltjongeren != null && newLeeftijd != null) {
            String[] parts = selectedItem.split(" - ");
            String oldJaartal = parts[0];
            String oldAantalhaltjongeren = parts[1];
            String oldLeeftijd = parts[2];

            try (Connection con = DataSource.getConnection()) {
                String sql = "UPDATE hoeveelheidhaltjongeren SET jaartalID = ?, aantalhaltjongerenID = ?, leeftijdID = ? WHERE jaartalID = ? AND aantalhaltjongerenID = ? AND leeftijdID = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, newJaartal);
                pstmt.setString(2, newAantalhaltjongeren);
                pstmt.setString(3, newLeeftijd);
                pstmt.setString(4, oldJaartal);
                pstmt.setString(5, oldAantalhaltjongeren);
                pstmt.setString(6, oldLeeftijd);
                pstmt.executeUpdate();

                int selectedIndex = hoeveelheidhaltjongereListView.getSelectionModel().getSelectedIndex();
                String newItem = newJaartal + " - " + newAantalhaltjongeren + " - " + newLeeftijd;
                hoeveelheidhaltjongereListView.getItems().set(selectedIndex, newItem);
            } catch (SQLException se) {
                System.out.println(se.getMessage());
            }
        }
    }

    private void vulCombobox() {
        try (Connection con = DataSource.getConnection()) {
            String sqlJaartal = "SELECT jaartalID FROM jaartal";
            PreparedStatement pstmtJaartal = con.prepareStatement(sqlJaartal);
            ResultSet rsJaartal = pstmtJaartal.executeQuery();
            while (rsJaartal.next()) {
                jaartalCB.getItems().add(rsJaartal.getString("jaartalID"));
            }

            String sqlAantalhaltjongeren = "SELECT aantalhaltjongerenID FROM aantalhaltjongeren";
            PreparedStatement pstmtAantalhaltjongeren = con.prepareStatement(sqlAantalhaltjongeren);
            ResultSet rsAantalhaltjongeren = pstmtAantalhaltjongeren.executeQuery();
            while (rsAantalhaltjongeren.next()) {
                aantalhaltjongerenCB.getItems().add(rsAantalhaltjongeren.getString("aantalhaltjongerenID"));
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }
    private void vulListView() {
        try (Connection con = DataSource.getConnection()) {
            String sql = "SELECT jaartalID, aantalhaltjongerenID, leeftijdID FROM hoeveelheidhaltjongeren";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            hoeveelheidhaltjongereListView.getItems().clear();
            while (rs.next()) {
                String item = rs.getString("jaartalID") + " - " + rs.getString("aantalhaltjongerenID") + " - " + rs.getString("leeftijdID");
                hoeveelheidhaltjongereListView.getItems().add(item);
            }
        } catch (SQLException se) {
            System.out.println(se.getMessage());
        }
    }

    public GridPane getGrid() {
        return grid;
    }
}

