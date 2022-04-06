module com.garageisep.wschess {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.garageisep.wschess to javafx.fxml;
    exports com.garageisep.wschess;
    exports com.garageisep.wschess.systems;
    exports com.garageisep.wschess.entities;
    exports com.garageisep.wschess.components;
}