module ru.shilkin {
    requires javafx.controls;
    requires javafx.fxml;

    requires postgresql;
    requires java.sql;
    requires mail;
    requires poi;
    requires cryptacular;

    requires org.bouncycastle.provider;
    opens ru.shilkin to javafx.fxml;
    exports ru.shilkin;
}