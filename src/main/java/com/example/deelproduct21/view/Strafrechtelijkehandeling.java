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

public class Strafrechtelijkehandeling {
    private String strafrechtelijkehandeling;
    private TextField textStrafrechtelijkehandeling;
    private ListView<String> strafrechtelijkehandelingListView;
    private App app;
    private Label strafrechtelijkehandelingLabel;
    private Button backButton;
    private Button updateButton;
    private Button verwijderButton;
    private Button feedbackLabel;
    private GridPane grid;
    private Button toevoegButton;
    private Connection con;

    public void setStrafrechtelijkehandeling(String strafrechtelijkehandeling) {
        this.strafrechtelijkehandeling = strafrechtelijkehandeling;
    }

    public String getStrafrechtelijkehandeling() {
        return strafrechtelijkehandeling;
    }

    public Strafrechtelijkehandeling(App app) {
        this.app = app;
        strafrechtelijkehandelingLabel = new Label("Strafrechtelijke handeling");
        textStrafrechtelijkehandeling = new TextField();
        toevoegButton = new Button("Toevoegen");
        updateButton = new Button("Update");
        verwijderButton = new Button("Verwijder");
        backButton = new Button("Terug naar Home");
        strafrechtelijkehandelingListView = new ListView<>();

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
        grid.add(strafrechtelijkehandelingLabel, 0, 1);
        grid.add(textStrafrechtelijkehandeling, 1, 1);
        grid.add(toevoegButton, 1, 2);
        grid.add(strafrechtelijkehandelingListView, 0, 3, 2, 1);
        grid.add(updateButton, 0, 5);
        grid.add(verwijderButton, 1, 5);
    }

    private void create() {
        String tekst = textStrafrechtelijkehandeling.getText();
        if (!tekst.isEmpty()) {
            try {
                con = DataSource.getConnection();
                String sql = "INSERT INTO strafrechtelijkehandeling (strafrechtelijkehandelingID) VALUES (?)";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, tekst);
                pstmt.executeUpdate();

                this.setStrafrechtelijkehandeling(tekst);
                textStrafrechtelijkehandeling.clear();
                strafrechtelijkehandelingListView.getItems().add(this.getStrafrechtelijkehandeling());

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
        String selectedItem = strafrechtelijkehandelingListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                con = DataSource.getConnection();
                String sql = "DELETE FROM strafrechtelijkehandeling WHERE strafrechtelijkehandelingID = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, selectedItem);
                pstmt.executeUpdate();

                strafrechtelijkehandelingListView.getItems().remove(selectedItem);

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
        String selectedItem = strafrechtelijkehandelingListView.getSelectionModel().getSelectedItem();
        String newValue = textStrafrechtelijkehandeling.getText();
        if (selectedItem != null && !newValue.isEmpty()) {
            try {
                con = DataSource.getConnection();
                String sql = "UPDATE strafrechtelijkehandeling SET strafrechtelijkehandelingID = ? WHERE strafrechtelijkehandelingID = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, newValue);
                pstmt.setString(2, selectedItem);
                pstmt.executeUpdate();

                int selectedIndex = strafrechtelijkehandelingListView.getSelectionModel().getSelectedIndex();
                strafrechtelijkehandelingListView.getItems().set(selectedIndex, newValue);
                textStrafrechtelijkehandeling.clear();

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
            String sql = "SELECT strafrechtelijkehandelingID FROM strafrechtelijkehandeling";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            strafrechtelijkehandelingListView.getItems().clear();
            while (rs.next()) {
                strafrechtelijkehandelingListView.getItems().add(rs.getString("strafrechtelijkehandelingID"));
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