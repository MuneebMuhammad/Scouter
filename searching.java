package project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.layout.Pane;
import javafx.scene.control.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import java.util.*;

public class Project extends Application{

    public void start(Stage primaryStage){
        Button btSearch = new Button("Search");
        Label nm = new Label("Name:");
        Label pb = new Label("Prepared by:");
        Label yr = new Label("Year");
        Label ft = new Label("File Type:");
        Label space = new Label("       ");
        TextField tfnm = new TextField();
        TextField tfpb = new TextField();
        ComboBox comboYear = new ComboBox();
        comboYear.getItems().addAll(2015, 2016, 2017, 2018, 2019, 2020, 2021);
        ComboBox comboPrep = new ComboBox();
        comboPrep.getItems().addAll("Pdf", "Word", "PowerPoint", "mp3", "text");
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(5));
        HBox titlePane = new HBox(5);
        titlePane.getChildren().addAll(nm, tfnm, yr, comboYear, ft, comboPrep, pb, tfpb, btSearch);
        mainPane.setTop(titlePane);
        Scene scene = new Scene(mainPane, 1000, 600);
        primaryStage.setTitle("Request Document");
        primaryStage.setScene(scene);
        primaryStage.show();
    } 
    
    public static void main(String[] args) {
        Application.launch(args);
    }       
}


















