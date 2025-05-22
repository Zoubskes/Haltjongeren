package com.example.deelproduct21.view;

import com.example.deelproduct21.App;
import com.example.deelproduct21.controller.DataSource;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Land {
    private String land;
    private TextField textLand;
    private ListView<String> landListView;
    private App app;
    private Label landLabel;
    private Button backButton;
    private Button updateButton;
    private Button verwijderButton;
    private Button feedbackLabel;
    private GridPane grid;
    private Button toevoegButton;
    private Connection con;

    public void setLand(String land) {
        this.land = land;
    }

    public String getLand() {
        return land;
    }

    public Land(App app) {
        this.app = app;
        landLabel = new Label("Land");
        textLand = new TextField();
        toevoegButton = new Button("Toevoegen");
        updateButton = new Button("Update");
        verwijderButton = new Button("Verwijder");
        backButton = new Button("Terug naar Home");
        landListView = new ListView<>();

        grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setMinSize(100, 50);
        createLayout();
        vulListView();
    }

    private void createLayout() {
        backButton.setOnAction(e -> app.showHomePage());
        toevoegButton.setOnAction(e -> create());
        verwijderButton.setOnAction(e -> delete());
        updateButton.setOnAction(e -> update());

        grid.add(backButton, 0, 0);
        grid.add(landLabel, 0, 1);
        grid.add(textLand, 1, 1);
        grid.add(toevoegButton, 1, 2);
        grid.add(landListView, 0, 3, 2, 1);
        grid.add(updateButton, 0, 5);
        grid.add(verwijderButton, 1, 5);
    }

    private void create() {
        String tekst = textLand.getText();
        if (!tekst.isEmpty()) {
            try {
                con = DataSource.getConnection();
                String sql = "INSERT INTO land (landID) VALUES (?)";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, tekst);
                pstmt.executeUpdate();

                this.setLand(tekst);
                textLand.clear();
                landListView.getItems().add(this.getLand());

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
        String selectedItem = landListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                con = DataSource.getConnection();
                String sql = "DELETE FROM land WHERE landID = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, selectedItem);
                pstmt.executeUpdate();

                landListView.getItems().remove(selectedItem);

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
        String selectedItem = landListView.getSelectionModel().getSelectedItem();
        String newValue = textLand.getText();
        if (selectedItem != null && !newValue.isEmpty()) {
            try {
                con = DataSource.getConnection();
                String sql = "UPDATE land SET landID = ? WHERE landID = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, newValue);
                pstmt.setString(2, selectedItem);
                pstmt.executeUpdate();

                int selectedIndex = landListView.getSelectionModel().getSelectedIndex();
                landListView.getItems().set(selectedIndex, newValue);
                textLand.clear();

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

    private void vulListView() {
        try {
            con = DataSource.getConnection();
            String sql = "SELECT landID FROM land";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            landListView.getItems().clear();
            while (rs.next()) {
                landListView.getItems().add(rs.getString("landID"));
            }

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

    public GridPane getGrid() {
        return grid;
    }
}