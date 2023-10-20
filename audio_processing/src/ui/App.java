package ui ;

import javafx.application.Application ;
import javafx.scene.Group ;
import javafx.scene.Scene ;
import javafx.scene.control.ComboBox ;
import javafx.scene.control.Separator ;
import javafx.scene.control.ToolBar ;
import javafx.scene.layout.BorderPane ;
import javafx.scene.layout.HBox ;
import javafx.stage.Stage ;
import javafx.scene.Node ;
import javafx.scene.control.Label ;
import javafx.scene.control.TextField ;
import javafx.scene.control.Button ;

import java.net.MalformedURLException ;


public class App extends Application {
    @Override
    public void start(Stage primStage) throws MalformedURLException {
        try{
            BorderPane root = new BorderPane() ;
            root.setTop(createToolbar()) ;
            root.setBottom(createStatusbar()) ;
            root.setCenter(createMaineContent());

            Scene scene = new Scene(root,1500, 800) ;
            primStage.setScene(scene) ;
            primStage.setTitle("The JavaFX audio processor") ;
            primStage.show();
        }
        catch (Exception e){
            e.printStackTrace() ;
        }
    }

    private ToolBar createToolbar() {
        Button button = new Button("Press !");
        ComboBox<String> cb = new ComboBox<>();
        cb.getItems().addAll("Item 1", "Item 2", "Item 3");
        Label label = new Label("This is a label");

        button.setOnAction(event -> System.out.println("Button pressed!"));
        cb.setOnAction(event -> {
            String selected = cb.getSelectionModel().getSelectedItem();
            System.out.println("Selected item: " + selected);
        });

        ToolBar tb = new ToolBar(button, label, new Separator(), cb);
        return tb;
    }

    private Node createStatusbar(){
        HBox statusbar = new HBox() ;
        statusbar.getChildren().addAll(new Label("Name:"), new TextField("     ")) ;
        return statusbar ;
    }

    private Node createMaineContent(){
        Group g = new Group() ;

        return g ;
    }
}
