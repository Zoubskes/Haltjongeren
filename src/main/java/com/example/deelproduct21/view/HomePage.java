package com.example.deelproduct21.view;

import com.example.deelproduct21.App;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class HomePage {

    private GridPane grid;
    private App app;

    public HomePage(App app) {
        this.app = app;
        grid = new GridPane();
        createLayout();

    }
    // Layout van de homepage
    private void createLayout() {
        Label welkomLabel = new Label("Homepagina");
        Label identificatieLabel = new Label("Identificaties");
        Label kwalificatieLabel = new Label("Kwalificaties");
        welkomLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");  // Grotere tekst en vetgedrukt

        // Spaties tussen de objecten
        grid.setVgap(5);
        grid.setHgap(40);
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setMinSize(100, 50);
        grid.setAlignment(Pos.CENTER);

        // Button objecten
        Button strafbaarfeitknop = new Button("Strafbaarfeit");
        Button delictjaarknop = new Button("Delictjaar");
        Button leeftijdsknop = new Button("Leeftijd");
        Button landknop = new Button("Land");
        Button strafrechtelijkehandelingknop = new Button("Strafrechtelijkehandeling");
        Button jaartalknop = new Button("Jaartal");
        Button aantalhaltjongerenknop = new Button("Aantalhaltjongeren");
        Button strafbarefeitenhaltjongereknop = new Button("Strafbarefeitenhaltjongere");
        Button gegevenshaltjongereknop = new Button("Gegevenshaltjongere");
        Button hoeveelheidhaltjongereknop = new Button("Hoeveelheidhaltjongere");

        // Events van de buttons
       strafbaarfeitknop.setOnAction(event -> app.showStrafbaarfeit());
       delictjaarknop.setOnAction(event -> app.showDelictjaar());
       leeftijdsknop.setOnAction(event -> app.showLeeftijd());
       landknop.setOnAction(event -> app.showLand());
       strafrechtelijkehandelingknop.setOnAction(event -> app.showStrafrechtelijkehandeling());
       jaartalknop.setOnAction(event -> app.showJaartal());
       aantalhaltjongerenknop.setOnAction(event -> app.showAantalhaltjongeren());
       strafbarefeitenhaltjongereknop.setOnAction(event -> app.showStrafbarefeitenhaltjongere());
       gegevenshaltjongereknop.setOnAction(event -> app.showGegevenshaltjongere());
       hoeveelheidhaltjongereknop.setOnAction(event -> app.showHoeveelheidhaltjongere());

        // Voeg het welkomslabel en knoppen toe aan het grid
        grid.add(welkomLabel, 0, 0, 2, 1);
        grid.add(identificatieLabel, 0,1);
        grid.add(kwalificatieLabel, 1,1);
        grid.add(strafbaarfeitknop, 0,3);
        grid.add(delictjaarknop, 0,4);
        grid.add(leeftijdsknop, 0,5);
        grid.add(landknop, 0,6);
        grid.add(strafrechtelijkehandelingknop, 0,7);
        grid.add(jaartalknop, 0,8);
        grid.add(aantalhaltjongerenknop, 0,9);
        grid.add(strafbarefeitenhaltjongereknop, 1,3);
        grid.add(gegevenshaltjongereknop, 1,4);
        grid.add(hoeveelheidhaltjongereknop, 1,5);


    }

    public GridPane getGrid() {
        return grid;
    }
}
