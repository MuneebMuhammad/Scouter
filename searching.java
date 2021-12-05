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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.cell.PropertyValueFactory;

public class Project extends Application{
    private Statement stmt;
    private final TableView<FileInfo> table = new TableView<FileInfo>();
    
    private final ObservableList<FileInfo> data = FXCollections.observableArrayList();
    public void start(Stage primaryStage){
        
        initializeDB(); // database initialized
        
        // all the nodes that are needed are declared here
        Button linkbt[] = new Button[10];
        Button btSearch = new Button("Search");
        Button btRe = new Button("Reset");
        Label nm = new Label("File:");
        Label pb = new Label("Prepared by:");
        Label catg = new Label("Category:");
        Label yr = new Label("Year");
        Label ft = new Label("File Extension:");
        Label fileNameOut = new Label();
        Label CtgOut = new Label();
        Label ExtOut = new Label();
        Label dateOut = new Label();
        Label authorOut = new Label();
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
        TableColumn fileNamecol = new TableColumn("File Name");
        TableColumn file_categorycol = new TableColumn("File Category");
        TableColumn authorcol = new TableColumn("Author");
        TableColumn date_createdcol = new TableColumn("Year");
        TableColumn file_extensioncol = new TableColumn("File extension");
        fileNamecol.setCellValueFactory(new PropertyValueFactory<FileInfo,String>("fileName"));
        file_categorycol.setCellValueFactory(new PropertyValueFactory<FileInfo,String>("file_category"));
        authorcol.setCellValueFactory(new PropertyValueFactory<FileInfo,String>("author"));
        date_createdcol.setCellValueFactory(new PropertyValueFactory<FileInfo,String>("date_created"));
        file_extensioncol.setCellValueFactory(new PropertyValueFactory<FileInfo,String>("file_extension"));
        fileNamecol.setMinWidth(100);
        file_categorycol.setMinWidth(100);
        authorcol.setMinWidth(100);
        date_createdcol.setMinWidth(100);
        file_extensioncol.setMinWidth(150);
        table.setItems(data);
        table.getColumns().addAll(fileNamecol, file_categorycol, authorcol, date_createdcol, file_extensioncol);
        
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
        HBox btTable = new HBox(2);        
        GridPane gp = new GridPane();
        gp.setPrefHeight(10);
        btTable.getChildren().addAll(table, gp);
        mainPane.setTop(titlePane);
        mainPane.setCenter(btTable);        
        table.setEditable(true);
        // action when Reset button is clicked
        btRe.setOnAction(e ->{
            int ll = table.getItems().size();
            table.getItems().clear();  // remove table
            for(int i = 0; i < ll; i++){
                // remove buttons
                linkbt[i].setVisible(false);  
                linkbt[i].setManaged(false);
            }
            
            tfnm.clear();
            tfpb.clear();
            comboext.valueProperty().set(null);
            comboYear.valueProperty().set(null);
            comboctg.valueProperty().set(null);
        });
        
        // action when Seach button is clicked
        btSearch.setOnAction(e -> {
            String nameChoose, yearChoose, typeChoose, writerChoose, ctgChoose;
            int ll = table.getItems().size();
            table.getItems().clear(); // remove table
            for(int i = 0; i < ll; i++){
                // remove buttons
                linkbt[i].setVisible(false);  
                linkbt[i].setManaged(false);
            }
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
                ResultSet rs = stmt.executeQuery(q); 
                int i = 0;  
                gp.add(new Button("Links"), 0, 0);
                while(rs.next()){
                    data.add(new FileInfo(rs.getString("fileName"), rs.getString("file_category"), rs.getString("author"),
                            rs.getString("date_created"), rs.getString("file_extension")));
                            String paths = rs.getString("path");
                            linkbt[i] = new Button();
                            linkbt[i].setOnAction(kk->openFile(paths));
                            gp.add(linkbt[i], 0, i+1);
                            i++;
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
    
     // this class is used to store information of files in TableView
    public static class FileInfo{
        private final SimpleStringProperty fileName;
        private final SimpleStringProperty file_category;
        private final SimpleStringProperty author;
        private final SimpleStringProperty date_created;
        private final SimpleStringProperty file_extension;

        public FileInfo(String fileName, String file_category, String author, String date_created, String file_extension) {
            this.fileName = new SimpleStringProperty(fileName);
            this.file_category = new SimpleStringProperty(file_category);
            this.author = new SimpleStringProperty(author);
            this.date_created = new SimpleStringProperty(date_created);
            this.file_extension = new SimpleStringProperty(file_extension);
        }
       
        public String getFileName() {
            return fileName.get();
        }

        public void setFileName(String fileName) {
            this.fileName.set(fileName);
        }

        public String getFile_category() {
            return file_category.get();
        }

        public void setFile_category(String file_category) {
            this.file_category.set(file_category);
        }

        public String getAuthor() {
            return author.get();
        }

        public void setAuthor(String author) {
            this.author.set(author);
        }

        public String getDate_created() {
            return date_created.get();
        }

        public void setDate_created(String date_created) {
            this.date_created.set(date_created);
        }

        public String getFile_extension() {
            return file_extension.get();
        }

        public void setFile_extension(String file_extension) {
            this.file_extension.set(file_extension);
        }
        
    }
    
    public static void main(String[] args) {
        Application.launch(args);
        
    }
    
   
}


















