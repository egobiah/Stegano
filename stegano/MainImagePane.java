package stegano;
import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.text.TextAlignment;

import javafx.scene.layout.BorderPane;

public class MainImagePane extends BorderPane {

    String filepath = null;
    double largeur;
    double hauteur;
    Text titre;
    BorderPane borderPane;
    BorderPane titlePane;
    Image image;
    public MainImagePane(DataController dataController,double hauteur){

        borderPane = new BorderPane();
        titlePane = new BorderPane();
       /* borderPane.setStyle(
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 1;" +
                "-fx-border-color: black;");
*/
        this.hauteur = hauteur;

        this.setHeight(hauteur+10);
        this.setWidth(largeur+10);
        titre = new Text(dataController.getTitle());
        titre.setTextAlignment(TextAlignment.CENTER);
        titlePane.setCenter(titre);
        this.setTop(titlePane);
        this.setMargin(titre, new Insets(0,0,5,0));
        this.setCenter(borderPane);
        loadNewImage(dataController.getImage());


    }

    public MainImagePane(String path, double hauteur){

        titlePane = new BorderPane();
        borderPane = new BorderPane();
       /* borderPane.setStyle(
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 1;" +
                "-fx-border-color: black;");
*/
        this.hauteur = hauteur;

        this.setHeight(hauteur+10);
        this.setWidth(largeur+10);
        titre = new Text("Image from : " + path);
        titre.setTextAlignment(TextAlignment.CENTER);
        titlePane.setCenter(titre);
        this.setTop(titlePane);
        this.setMargin(titre, new Insets(0,0,5,0));
        this.setCenter(borderPane);
        try {
            File file = new File(path);
            String localUrl = file.toURI().toURL().toString();
            image = new Image(localUrl);
            loadNewImage(image);
        } catch (Exception error){
            System.out.println("Erreur lors de la création du MainImagePane");
        }
    }



    public void loadNewImage(Image image){
       this.image = image;
       if(image == null){
           this.borderPane.setCenter(null);
           return;
       }
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(hauteur);
        imageView.setPreserveRatio(true);
        this.largeur = this.hauteur * image.getWidth() / image.getHeight();
        this.borderPane.setCenter(imageView);
    }

    public Boolean imageIsNull(){
        if(this.image == null){
            return true;
        } else {
            return false;
        }
    }



    public double getLargeurPane(){
        return largeur;
    }

    public double getHauteurPane(){
        return hauteur;
    }

    public void setTitleMinitature(String text){
        this.titre.setText(text);
    }





}
