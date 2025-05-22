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

public class Jaartal {
    private int jaartal;
    private TextField textJaartal;
    private ListView<String> jaartalListView;
    private App app;
    private Label jaartalLabel;
    private Button backButton;
    private Button updateButton;
    private Button verwijderButton;
    private Button feedbackLabel;
    private GridPane grid;
    private Button toevoegButton;
    private Connection con;

    public void setJaartal(int jaartal) {
        this.jaartal = jaartal;
    }

    public int getJaartal() {
        return jaartal;
    }

    public Jaartal(App app) {
        this.app = app;
        jaartalLabel = new Label("Jaartal");
        textJaartal = new TextField();
        toevoegButton = new Button("Toevoegen");
        updateButton = new Button("Update");
        verwijderButton = new Button("Verwijder");
        backButton = new Button("Terug naar Home");
        jaartalListView = new ListView<>();

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
        grid.add(jaartalLabel, 0, 1);
        grid.add(textJaartal, 1, 1);
        grid.add(toevoegButton, 1, 2);
        grid.add(jaartalListView, 0, 3, 2, 1);
        grid.add(updateButton, 0, 5);
        grid.add(verwijderButton, 1, 5);
    }

    private void create() {
        String tekst = textJaartal.getText();
        if (!tekst.isEmpty()) {
            try {
                con = DataSource.getConnection();
                String sql = "INSERT INTO jaartal (jaartalID) VALUES (?)";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, tekst);
                pstmt.executeUpdate();

                this.setJaartal(Integer.parseInt(tekst));
                textJaartal.clear();
                jaartalListView.getItems().add(String.valueOf(this.getJaartal()));

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
        String selectedItem = jaartalListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                con = DataSource.getConnection();
                String sql = "DELETE FROM jaartal WHERE jaartalID = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, selectedItem);
                pstmt.executeUpdate();

                jaartalListView.getItems().remove(selectedItem);

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
        String selectedItem = jaartalListView.getSelectionModel().getSelectedItem();
        String newValue = textJaartal.getText();
        if (selectedItem != null && !newValue.isEmpty()) {
            try {
                con = DataSource.getConnection();
                String sql = "UPDATE jaartal SET jaartalID = ? WHERE jaartalID = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, newValue);
                pstmt.setString(2, selectedItem);
                pstmt.executeUpdate();

                int selectedIndex = jaartalListView.getSelectionModel().getSelectedIndex();
                jaartalListView.getItems().set(selectedIndex, newValue);
                textJaartal.clear();

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
            String sql = "SELECT jaartalID FROM jaartal";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            jaartalListView.getItems().clear();
            while (rs.next()) {
                jaartalListView.getItems().add(rs.getString("jaartalID"));
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