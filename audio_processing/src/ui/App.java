package ui ;

import javafx.application.Application ;
import javafx.scene.* ;
import javafx.scene.control.* ;
import javafx.scene.layout.* ;
import javafx.stage.Stage ;


public class App extends Application {
    @Override
    public void start(Stage primStage) {
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

        return new ToolBar(button, label, new Separator(), cb);
    }

    private Node createStatusbar(){
        HBox statusbar = new HBox() ;
        statusbar.getChildren().addAll(new Label("Name:"), new TextField("     ")) ;
        return statusbar ;
    }

    private Node createMaineContent(){

        return new Group();
    }
}
