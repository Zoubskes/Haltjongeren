package com.example.deelproduct21.view;

import com.example.deelproduct21.App;
import com.example.deelproduct21.controller.DatasetController;
import com.example.deelproduct21.model.DatasetEntry;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;

import java.util.List;

public class Leeftijd {
    private int leeftijd;
    private App app;
    private Label leeftijdLabel;
    private Button backButton;
    private GridPane grid;
    private ListView<DatasetEntry> listViewLeeftijd;

    public void setLeeftijd(int leeftijd) {
        this.leeftijd = leeftijd;
    }

    public int getLeeftijd() {
        return leeftijd;
    }

    public Leeftijd(App app) {
        this.app = app;
        leeftijdLabel = new Label("Leeftijd");
        backButton = new Button("Terug naar Home");
        DatasetController controller = new DatasetController();
        listViewLeeftijd = new ListView<>();

        List<DatasetEntry> datasetEntries = controller.fetchDatasetEntries("Leeftijd");
        if (!datasetEntries.isEmpty()) {
            datasetEntries.remove(0); // Remove the first item
            if (!datasetEntries.isEmpty()) {
                datasetEntries.remove(datasetEntries.size() - 1); // Remove the last item
            }
        }
        listViewLeeftijd.getItems().setAll(datasetEntries);

        grid = new GridPane();
        grid.setVgap(5);
        grid.setHgap(5);
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setMinSize(100, 50);
        createLayout();
    }

    private void createLayout() {
        backButton.setOnAction(e -> app.showHomePage());

        grid.add(backButton, 0, 0);
        grid.add(leeftijdLabel, 0, 1);
        grid.add(listViewLeeftijd, 0, 3, 2, 1); // Corrected variable name
    }




    public GridPane getGrid() {
        return grid;
    }
}