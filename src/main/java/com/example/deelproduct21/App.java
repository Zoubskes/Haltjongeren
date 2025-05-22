package com.example.deelproduct21;

import com.example.deelproduct21.view.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    private VBox rootLayout = new VBox();

    @Override
    public void start(Stage primaryStage){
        rootLayout = new VBox();  // Gebruik een VBox voor de layout

        // Toon de HomePage bij het opstarten
        showHomePage();

        Scene scene = new Scene(rootLayout, 600, 400);
        primaryStage.setTitle("Deelproduct2 Zoubair Khan"); // Titel van het project
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Methode om de HomePage te tonen
    public void showHomePage() {
        HomePage homePage = new HomePage(this);
        rootLayout.getChildren().clear(); // Leeg de VBox voor nieuwe content
        rootLayout.getChildren().add(homePage.getGrid());
    }
    public void showStrafbaarfeit(){
        Strafbaarfeit strafbaarfeit = new Strafbaarfeit(this);
        rootLayout.getChildren().clear();
        rootLayout.getChildren().add(strafbaarfeit.getGrid());
    }
    public void showDelictjaar(){
        Delictjaar delictjaar = new Delictjaar(this);
        rootLayout.getChildren().clear();
        rootLayout.getChildren().add(delictjaar.getGrid());
    }
    public void showLeeftijd(){
        Leeftijd leeftijd = new Leeftijd(this);
        rootLayout.getChildren().clear();
        rootLayout.getChildren().add(leeftijd.getGrid());
    }
    public void showLand(){
        Land land = new Land(this);
        rootLayout.getChildren().clear();
        rootLayout.getChildren().add(land.getGrid());
    }
    public void showStrafrechtelijkehandeling(){
        Strafrechtelijkehandeling strafrechtelijkehandeling = new Strafrechtelijkehandeling(this);
        rootLayout.getChildren().clear();
        rootLayout.getChildren().add(strafrechtelijkehandeling.getGrid());
    }
    public void showJaartal(){
        Jaartal jaartal = new Jaartal(this);
        rootLayout.getChildren().clear();
        rootLayout.getChildren().add(jaartal.getGrid());
    }
    public void showAantalhaltjongeren(){
        Aantalhaltjongeren aantalhaltjongeren = new Aantalhaltjongeren(this);
        rootLayout.getChildren().clear();
        rootLayout.getChildren().add(aantalhaltjongeren.getGrid());
    }
    public void showStrafbarefeitenhaltjongere(){
        Strafbarefeitenhaltjongere strafbarefeitenhaltjongere = new Strafbarefeitenhaltjongere(this);
        rootLayout.getChildren().clear();
        rootLayout.getChildren().add(strafbarefeitenhaltjongere.getGrid());
    }
    public void showGegevenshaltjongere(){
        Gegevenshaltjongere gegevenshaltjongere = new Gegevenshaltjongere(this);
        rootLayout.getChildren().clear();
        rootLayout.getChildren().add(gegevenshaltjongere.getGrid());
    }
    public void showHoeveelheidhaltjongere(){
        Hoeveelheidhaltjongere hoeveelheidhaltjongere = new Hoeveelheidhaltjongere(this);
        rootLayout.getChildren().clear();
        rootLayout.getChildren().add(hoeveelheidhaltjongere.getGrid());
    }


    public static void main(String[] args) {
        launch(args);
    }
}