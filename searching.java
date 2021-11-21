package project;

import java.awt.Color;
import javafx.scene.paint.*;
import javafx.scene.canvas.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.layout.Pane;
import javafx.scene.control.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import java.util.*;
import java.sql.*;
import java.awt.Desktop;
import java.io.*;

public class Project extends Application{
    private Statement stmt;
    public void start(Stage primaryStage){
        
        initializeDB(); // database initialized
        
        // all the nodes that are needed are declared here
        Button btSearch = new Button("Search");
        Button btRe = new Button("Reset");
        Label nm = new Label("File:");
        Label pb = new Label("Prepared by:");
        Label catg = new Label("Category:");
        Label yr = new Label("Year");
        Label ft = new Label("File Extension:");
        Label space = new Label("       ");
        TextField tfnm = new TextField();
        TextField tfpb = new TextField();
        ComboBox comboctg = new ComboBox();
        ComboBox comboYear = new ComboBox();
        comboYear.getItems().addAll(2015, 2016, 2017, 2018, 2019, 2020, 2021);
        ComboBox comboext = new ComboBox();
        comboext.getItems().addAll("Pdf", "Word", "PowerPoint", ""
                + "mp3", "text");
        comboctg.getItems().addAll("Comic", "Action", "Poetry", "Politics", "Geography", "History");
        tfnm.setPromptText("File Name");
        tfpb.setPromptText("Writer Name");
        
        // add design to the nodes
        btSearch.setPrefSize(100, 60);
        btRe.setPrefSize(100, 60);
        Font f = new Font(21);
        btSearch.setStyle("-fx-text-fill: white");
        btRe.setStyle("-fx-text-fill: white");
        btSearch.setBackground(new Background(new BackgroundFill(Paint.valueOf("green"), new CornerRadii(8), Insets.EMPTY)));
        btRe.setBackground(new Background(new BackgroundFill(Paint.valueOf("red"), new CornerRadii(8), Insets.EMPTY)));
        btSearch.setFont(f.font("verdana", FontWeight.SEMI_BOLD, 18));
        btRe.setFont(f.font("verdana", FontWeight.SEMI_BOLD, 18));
        
        // set panes
        BorderPane mainPane = new BorderPane();
        mainPane.setPadding(new Insets(5));
        HBox titlePane = new HBox(5);
        titlePane.getChildren().addAll(nm, tfnm, yr, comboYear, ft, comboext, catg, comboctg, pb, tfpb, space, btSearch, btRe);
        mainPane.setTop(titlePane);
        
        // action when Reset button is clicked
        btRe.setOnAction(e ->{
            tfnm.clear();
            tfpb.clear();
            comboext.valueProperty().set(null);
            comboYear.valueProperty().set(null);
            comboctg.valueProperty().set(null);
            
        });
        
        // action when Seach button is clicked
        btSearch.setOnAction(e -> {
            String nameChoose, yearChoose, typeChoose, writerChoose, ctgChoose;
            // check which conditions were selected and accordingly choose query 
            if(tfnm.getText().isEmpty()){
                nameChoose = "";
            }
            else {
                nameChoose = tfnm.getText();
            }
            if(tfpb.getText().isEmpty()){
                writerChoose = "";
            }
            else{
                writerChoose = tfpb.getText();
            }
            if(comboYear.getSelectionModel().isEmpty()){
                yearChoose = "";
            }
            else{
                yearChoose = comboYear.getValue().toString();
            }
            if(comboext.getSelectionModel().isEmpty()){
                typeChoose = "";
            }
            else{
                typeChoose = comboext.getValue().toString();
            }
            if(comboctg.getSelectionModel().isEmpty()){
                ctgChoose = "";
            }
            else{
                ctgChoose = comboctg.getValue().toString();
            }
            
            try{
                String q = "Select * from files where fileName LIKE '%" + nameChoose + "%' and file_category like '%" + ctgChoose + "%' and author like '%" + writerChoose 
                        + "%' and date_created like '%" + yearChoose + "%' and file_extension like '%" + typeChoose + "%'";
                System.out.println(q);
                ResultSet rs = stmt.executeQuery(q);  
                while(rs.next()){
                    System.out.println(rs.getString("fileName") + " " + rs.getString("file_category") + " " + rs.getString("file_extension") + " " + 
                            rs.getString("date_created") + " " + rs.getString("author"));
                }
            }
            catch(SQLException ex){
                ex.printStackTrace();
            }
        });
        Scene scene = new Scene(mainPane, 1350, 600);
        primaryStage.setTitle("Request Document");
        primaryStage.setScene(scene);
        primaryStage.show();
    } 
    
    // sets up database connection
    public void initializeDB(){
        try{
            // load jdbc driver
            Class.forName("com.mysql.jdbc.Driver");
            // establish connection with a database
            Connection con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/file_management", "root", "sns1234");
            // create a statement
            stmt = con.createStatement();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    // p is the path of the file and this file is opened by openFile if it exists
    public static void openFile(String p){
        // the path provided should have double '\\' e.g: c:\\Desktop\\Computer\\file.txt
        try{
            File file = new File(p);
            // check if Desktop is supported
            if(!Desktop.isDesktopSupported()){
                System.out.println("Desktop not supported");
                return;
            }
            Desktop desktop = Desktop.getDesktop();
            if(file.exists()){  // check if file exists
                desktop.open(file); // open the file
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        Application.launch(args);
        
    }
}
