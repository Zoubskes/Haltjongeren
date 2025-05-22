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

public class Aantalhaltjongeren {
    private int aantalhaltjongeren;
    private TextField textAantalhaltjongeren;
    private ListView<String> aantalhaltjongerenListView;
    private App app;
    private Label aantalhaltjongerenLabel;
    private Button backButton;
    private Button updateButton;
    private Button verwijderButton;
    private Button feedbackLabel;
    private GridPane grid;
    private Button toevoegButton;
    private Connection con;

    public void setAantalhaltjongeren(int aantalhaltjongeren) {
        this.aantalhaltjongeren = aantalhaltjongeren;
    }

    public int getAantalhaltjongeren() {
        return aantalhaltjongeren;
    }

    public Aantalhaltjongeren(App app) {
        this.app = app;
        aantalhaltjongerenLabel = new Label("Aantal Halt Jongeren");
        textAantalhaltjongeren = new TextField();
        toevoegButton = new Button("Toevoegen");
        updateButton = new Button("Update");
        verwijderButton = new Button("Verwijder");
        backButton = new Button("Terug naar Home");
        aantalhaltjongerenListView = new ListView<>();

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
        grid.add(aantalhaltjongerenLabel, 0, 1);
        grid.add(textAantalhaltjongeren, 1, 1);
        grid.add(toevoegButton, 1, 2);
        grid.add(aantalhaltjongerenListView, 0, 3, 2, 1);
        grid.add(updateButton, 0, 5);
        grid.add(verwijderButton, 1, 5);
    }

    private void create() {
        String tekst = textAantalhaltjongeren.getText();
        if (!tekst.isEmpty()) {
            try {
                con = DataSource.getConnection();
                String sql = "INSERT INTO aantalhaltjongeren (aantalhaltjongerenID) VALUES (?)";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, tekst);
                pstmt.executeUpdate();

                this.setAantalhaltjongeren(Integer.parseInt(tekst));
                textAantalhaltjongeren.clear();
                aantalhaltjongerenListView.getItems().add(String.valueOf(this.getAantalhaltjongeren()));

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
        String selectedItem = aantalhaltjongerenListView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                con = DataSource.getConnection();
                String sql = "DELETE FROM aantalhaltjongeren WHERE aantalhaltjongerenID = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, selectedItem);
                pstmt.executeUpdate();

                aantalhaltjongerenListView.getItems().remove(selectedItem);

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
        String selectedItem = aantalhaltjongerenListView.getSelectionModel().getSelectedItem();
        String newValue = textAantalhaltjongeren.getText();
        if (selectedItem != null && !newValue.isEmpty()) {
            try {
                con = DataSource.getConnection();
                String sql = "UPDATE aantalhaltjongeren SET aantalhaltjongerenID = ? WHERE aantalhaltjongerenID = ?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, newValue);
                pstmt.setString(2, selectedItem);
                pstmt.executeUpdate();

                int selectedIndex = aantalhaltjongerenListView.getSelectionModel().getSelectedIndex();
                aantalhaltjongerenListView.getItems().set(selectedIndex, newValue);
                textAantalhaltjongeren.clear();

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
            String sql = "SELECT aantalhaltjongerenID FROM aantalhaltjongeren";
            PreparedStatement pstmt = con.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            aantalhaltjongerenListView.getItems().clear();
            while (rs.next()) {
                aantalhaltjongerenListView.getItems().add(rs.getString("aantalhaltjongerenID"));
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