module com.example.deelproduct21 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires org.apache.commons.dbcp2;
    requires com.google.gson;
    requires java.sql;

    exports com.example.deelproduct21;
    exports com.example.deelproduct21.view;
    exports com.example.deelproduct21.model;
    exports com.example.deelproduct21.controller;
}