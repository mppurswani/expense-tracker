import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import javafx.concurrent.Worker;
import netscape.javascript.JSObject;
import java.io.File;

public class ExpenseTrackerApp extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Create WebView
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        
        // Load your HTML file
        webEngine.load(new File("src/index.html").toURI().toString());
        
        // ===== BRIDGE CODE GOES HERE - INSIDE start() METHOD =====
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                System.out.println("🌐 WebView loaded - Connecting JavaScript bridge...");
                try {
                    JSObject window = (JSObject) webEngine.executeScript("window");
                    JavaConnector connector = new JavaConnector();
                    window.setMember("javaConnector", connector);
                    System.out.println("✅ JavaScript ↔ Java bridge ACTIVE!");
                } catch (Exception e) {
                    System.err.println("❌ Bridge error: " + e.getMessage());
                }
            }
        });
        // ===== END BRIDGE CODE =====
        
        // Show window
        Scene scene = new Scene(webView, 1200, 800);
        primaryStage.setTitle("Expense Tracker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}