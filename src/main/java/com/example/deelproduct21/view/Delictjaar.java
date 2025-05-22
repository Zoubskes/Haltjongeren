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

public class Delictjaar {
    private int delictjaar;
    private TextField textDelictjaar;
    private ListView<String> delictjaarListView;
    private App app;
    private Label delictjaarLabel;
    private Button backButton;
    private Button updateButton;
    private Button verwijderButton;
    private Button feedbackLabel;
    private GridPane grid;
    private Button toevoegButton;
    private Connection con;

    public void setDelictjaar(int delictjaar) {
        this.delictjaar = delictjaar;
    }

    public int getDelictjaar() {
        return delictjaar;
    }

    public Delictjaar(App app) {
        this.app = app;
        delictjaarLabel = new Label("Delictjaar");
        textDelictjaar = new TextField();
        toevoegButton = new Button("Toevoegen");
        updateButton = new Button("Update");
        verwijderButton = new Button("Verwijder");
        backButton = new Button("Terug naar Home");
        delictjaarListView = new ListView<>();

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
        grid.add(delictjaarLabel, 0, 1);
        grid.add(textDelictjaar, 1, 1);
        grid.add(toevoegButton, 1, 2);
        grid.add(delictjaarListView, 0, 3, 2, 1);
        grid.add(updateButton, 0, 5);
        grid.add(verwijderButton, 1, 5);
    }

    private void create() {
        String tekst = textDelictjaar.getText();
        if (!tekst.isEmpty()) {
            try {
                con = DataSource.getConnection();
                String sql = "INSERT INTO delictjaar (delictjaarID) VALUES (?)";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, tekst);
                pstmt.executeUpdate();

                this.setDelictjaar(Integer.parseInt(tekst));
                textDelictjaar.clear();
                delictjaarListView.getItems().add(String.valueOf(this.getDelictjaar()));

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
        String selectedItem = delictjaarListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                con = DataSource.getConnection();
                String sql = "DELETE FROM delictjaar WHERE delictjaarID = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, selectedItem);
                pstmt.executeUpdate();

                delictjaarListView.getItems().remove(selectedItem);

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
        String selectedItem = delictjaarListView.getSelectionModel().getSelectedItem();
        String newValue = textDelictjaar.getText();
        if (selectedItem != null && !newValue.isEmpty()) {
            try {
                con = DataSource.getConnection();
                String sql = "UPDATE delictjaar SET delictjaarID = ? WHERE delictjaarID = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, newValue);
                pstmt.setString(2, selectedItem);
                pstmt.executeUpdate();

                int selectedIndex = delictjaarListView.getSelectionModel().getSelectedIndex();
                delictjaarListView.getItems().set(selectedIndex, newValue);
                textDelictjaar.clear();

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
            String sql = "SELECT delictjaarID FROM delictjaar";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            delictjaarListView.getItems().clear();
            while (rs.next()) {
                delictjaarListView.getItems().add(rs.getString("delictjaarID"));
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