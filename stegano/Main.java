package stegano;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.beans.Expression;
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.lang.reflect.Type;
import java.util.Scanner; // Import the Scanner class to read text files
import java.io.File;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;



public class Main extends Application {
    public final String EXEC_EXIF_PATH = "E:\\Fichier_Olivier\\Olivier\\Bureau\\exiftool-12.14\\exiftool.exe";
    private static String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Etiam eleifend, lorem id molestie iaculis, odio massa maximus urna, nec semper augue turpis nec nunc. Vestibulum vel dui eget mi hendrerit congue ac eu metus. Morbi eleifend, augue vitae tempor dictum, dolor leo finibus ipsum, eu finibus nisi risus eget metus. Nullam a eleifend metus, eu congue odio. Aliquam pellentesque risus in volutpat posuere. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Donec ut nulla diam. In aliquam lacus in diam tincidunt fringilla quis blandit dolor. Fusce interdum tellus ut dapibus ornare. Suspendisse sed vehicula mi.\n" +
            "\n" +
            "Sed consequat metus eu orci malesuada, id suscipit nunc viverra. In blandit id dolor vel tincidunt. Integer vulputate, elit et ultricies dignissim, nulla eros efficitur mi, nec ultricies justo eros vitae turpis. Ut non tortor id augue tempor suscipit. Donec ac finibus eros. Fusce fringilla tincidunt metus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Sed pellentesque sit amet sapien semper gravida.\n" +
            "\n" +
            "Nulla fermentum turpis libero, vel dictum ante cursus et. Nulla id dictum turpis. Etiam tempor suscipit tellus. Pellentesque efficitur luctus sem, sed pulvinar ligula. Nunc quis nunc nisi. Sed eget aliquet ipsum, eu consequat justo. Cras in interdum risus. Vestibulum vel viverra nulla. Praesent malesuada turpis nec nibh mollis, non convallis elit ultrices. Suspendisse varius nibh et dui pellentesque, eget malesuada lectus dapibus. Maecenas justo leo, blandit in dolor sed, dapibus molestie leo. Phasellus congue consectetur ex sit amet venenatis. Donec commodo ullamcorper tellus ac tempor. Etiam odio eros, blandit a est id, varius pellentesque nulla. Maecenas sollicitudin odio quis lacus interdum faucibus. Donec nec suscipit nibh.";

    double HAUTEUR_TOP = 55;
    double LARGEUR = 800;
    double HAUTEUR_IMAGE = 400;
    double HAUTEUR = HAUTEUR_IMAGE + HAUTEUR_TOP +60;
    Stage stage;
    BorderPane imagePane;
    BorderPane dataPane;
    BorderPane hiddenDataPane;
    BorderPane miniaturePane;
    TextField textField_exec;
    TextField textField_img;
    Button load;
    Button generated;
    Button present;
    Boolean canSave = false;

    DataController dataController;



    TextArea hiddenDataText;


    MainImagePane leftPhoto;

    MainImagePane miniature;

    FileChooser path_file;

    BorderPane top;
    BorderPane root;

    VBox miniatureButtonBox;
    VBox minitatureBoxButtonAndImage;
    public void start(Stage stage) {
        path_file = new FileChooser();
        path_file.getExtensionFilters().addAll(new ExtensionFilter("jpeg file", "*.jpg"));
        root = new BorderPane();
        top = new BorderPane();
        minitatureBoxButtonAndImage = new VBox();

        root.setTop(top);
        this.stage = stage;


        stage.setTitle("Stegano Gridello Hureau");
        stage.setScene(new Scene(root, LARGEUR, HAUTEUR_TOP));
        stage.show();
        //stage.setMinHeight(HAUTEUR);
        //stage.setMaxHeight(HAUTEUR);
        stage.setMinWidth(HAUTEUR_IMAGE*1.2);
        imagePane = new BorderPane();
        root.setCenter(imagePane);


        dataPane = new BorderPane();
        imagePane.setCenter(dataPane);
        imagePane.setMargin(dataPane, new Insets(0,0,10,10));
        hiddenDataPane = new BorderPane();

        hiddenDataText = new TextArea();
        hiddenDataText.setWrapText(true);
        hiddenDataText.textProperty().addListener((obs,old,niu)->{
            textChange();
        });



        hiddenDataPane.setCenter(hiddenDataText);
        hiddenDataPane.setTop(new Text("Hidden data"));


        miniaturePane = new BorderPane();

        dataPane.setCenter(hiddenDataPane);
        dataPane.setTop(miniaturePane);

        VBox pathBox = new VBox();

        // PATH DU PROGRAMME

        BorderPane execBox = new BorderPane();
            Label label_exec = new Label("Executable path : ");
            textField_exec = new TextField (EXEC_EXIF_PATH);


            Button button_exec = new Button("Select Directory");
            button_exec.setOnAction(e -> {
                selectExec();
            });
        execBox.setLeft(label_exec);
        execBox.setCenter(textField_exec);
        execBox.setRight(button_exec);
       // execBox.getChildren().addAll(label_exec,textField_exec, button_exec);



        // PATH DE L'IMAGE

        BorderPane imgBox = new BorderPane();

        Label label_img = new Label("Image Path:");
        textField_img = new TextField ("E:\\Fichier_Olivier\\Olivier\\Bureau\\image.jpg");
        FileChooser path_img = new FileChooser();

        Button button_img = new Button("Select Directory");
        button_img.setOnAction(e -> {
            selectMainImage();
        });


        imgBox.setLeft(label_img);
        imgBox.setCenter(textField_img);
        imgBox.setRight(button_img);

        BorderPane loadBox = new BorderPane();
        Button button_load = new Button("Load");
        button_load.setOnAction(e -> {
            load();
        });
        Button button_save = new Button("Save");
        button_save.setOnAction(e -> {

            save();

        });

        HBox buttonBox = new HBox();
        buttonBox.getChildren().addAll(button_save,button_load);
        loadBox.setRight(buttonBox);


        //pathBox.getChildren().addAll(execBox, imgBox,loadBox);
        pathBox.getChildren().addAll( imgBox,loadBox);
        pathBox.setSpacing(2);

        miniatureButtonBox = new VBox();
        load = new Button("Load jpeg file");
        load.setOnAction(e -> {

            selectThumbImage();

        });
        generated = new Button("Generated from original");
        generated.setOnAction(e -> {

            generated();
        });
        present = new Button("Founded in the original");
        present.setOnAction(e -> {

            retrieveThumbInFile();
        });


        miniatureButtonBox.getChildren().addAll(load, generated, present);
        miniaturePane.setCenter(minitatureBoxButtonAndImage);





        root.setCenter(imagePane);



        top.setTop(pathBox);



    }







    public static void main(String[] args) {
        launch();
    }


    private void refreshAll(){
        refreshMain();
        refreshMinitature();



    }

    private void refreshMain(){
        leftPhoto = new MainImagePane(textField_img.getText(),HAUTEUR_IMAGE );
        imagePane.setLeft(leftPhoto);
        stage.setWidth(leftPhoto.getLargeurPane()*2.2);
        stage.setMinWidth(leftPhoto.getLargeurPane()*2.2);
        stage.setMinHeight(HAUTEUR);
    }

    private void refreshMinitature(){
        miniature = new MainImagePane(dataController,HAUTEUR_IMAGE/4 );
        minitatureBoxButtonAndImage.getChildren().removeAll();
        minitatureBoxButtonAndImage.getChildren().clear();
        minitatureBoxButtonAndImage.getChildren().add(miniature );
        minitatureBoxButtonAndImage.getChildren().add(miniatureButtonBox );
        hiddenDataText.setText(dataController.getData());
        refreshText(dataController.getData());
        refreshButton();
    }

    public void refreshButton(){
        load.setStyle("");
        generated.setStyle("");
        present.setStyle("");
        switch (dataController.imageKind){
            case FILE:
                load.setStyle("-fx-background-color: green;");
                break;
            case EXIST:
                present.setStyle("-fx-background-color: green");
                break;
            case GENERATED:
                generated.setStyle("-fx-background-color: green");
                break;

        }

    }

    private void refreshText(String text){
        hiddenDataText.setText(text);
    }


    private void save(){
        if(!canSave)
            return;
        File selectedDirectory = path_file.showSaveDialog(stage);
        String res = "";
        try {
            res =  selectedDirectory.getAbsolutePath();
        } catch (Exception ex){
            if (res == ""){
                return;
            }
        }


        root.getChildren().removeAll();
        root.getChildren().clear();
        root.setTop(top);


        System.out.println("Saving at : " + res);
        if(dataController.save(textField_img.getText(), res, hiddenDataText.getText())){
            load();
        }
        root.setCenter(imagePane);
        textField_img.setText(res);




    }

    public void load(){

       dataController = new DataController(DataController.TypePicture.EXIST, textField_img.getText(), EXEC_EXIF_PATH);
       System.out.println("Image is : " + dataController.getImage() + " and type is " + dataController.getType());

        canSave = true;
       refreshAll();






    }

    public void generated(){
        dataController = new DataController(DataController.TypePicture.GENERATED, textField_img.getText() );
        refreshMinitature();
    }
    private void selectMainImage(){
        textField_img.setText( selectFile());
    }


    private void selectThumbImage(){
        dataController = new DataController(DataController.TypePicture.FILE, selectFile());
        hiddenDataText.setText("");
        refreshMinitature();
    }

    private void selectExec(){
        textField_exec.setText( selectFile());
    }

    private String selectFile(){
        String res = "null";
        File selectedDirectory = path_file.showOpenDialog(stage);
        try {
            res =  selectedDirectory.getAbsolutePath();
        } catch (Exception ex){
            //
        }
        return res;
    }
    private void textChange(){

        dataController.setData(hiddenDataText.getText());
        System.out.println("The text is : " + dataController.getData());
    }
    private void retrieveThumbInFile(){
        dataController = new DataController(DataController.TypePicture.EXIST, textField_img.getText(), EXEC_EXIF_PATH);
        if(dataController.getType() != DataController.TypePicture.EXIST){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("No thumbnail in this picture");

            // alert.setHeaderText("Results:");
            alert.setContentText("Generated from the source");

            alert.showAndWait();
        }
        refreshMinitature();
    }





}