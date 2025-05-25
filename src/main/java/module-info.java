module com.blueprinthell {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.google.gson;
    requires javafx.media;
    requires com.gluonhq.attach.util;

    opens com.blueprinthell to javafx.fxml;
    opens com.blueprinthell.controller to javafx.fxml;
    exports com.blueprinthell.controller;

    opens com.blueprinthell.model to com.google.gson;


}