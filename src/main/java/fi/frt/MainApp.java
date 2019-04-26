package fi.frt;

import fi.frt.ui.FXMLController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class MainApp extends Application {
    private ConfigurableApplicationContext springContext;

    @Override
    public void start(Stage stage) throws Exception {
        springContext = SpringApplication.run(MainApp.class);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("scene.fxml"));
        fxmlLoader.setControllerFactory(springContext::getBean);
        Parent root = fxmlLoader.load();
        FXMLController fxmlController = fxmlLoader.getController();
        fxmlController.init(stage, springContext.getBean("jdbcTemplate", JdbcTemplate.class));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        springContext.close();
        Platform.exit();
        System.exit(0);
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
