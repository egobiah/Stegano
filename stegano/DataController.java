package stegano;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import javax.xml.bind.annotation.XmlType;
import java.io.File;
import java.util.concurrent.TimeUnit;
import java.io.PrintWriter;
import java.util.Scanner; // Import the Scanner class to read text files

public class DataController extends Throwable {
    // The windows path to exif.exe file
    public final String EXEC_EXIF_PATH = "E:\\Fichier_Olivier\\Olivier\\Bureau\\exiftool-12.14\\exiftool.exe";
    // The windows path to steghide.exe file
    public final String EXEC_STEGHIDE_PATH = "E:\\Fichier_Olivier\\Olivier\\Bureau\\steghide-0.5.1-win32\\steghide\\steghide.exe";
    // A windows path to a tmp file for thumbnail used by exif
    public final String TMP_THUMB_PATH = "E:\\Fichier_Olivier\\Olivier\\Bureau\\tmpFileThumb.jpg";
    // A windows path to a tmp file for .txt file used by steghide
    public final String TMP_TEXT_PATH = "E:\\Fichier_Olivier\\Olivier\\Bureau\\tmpTextStegano.txt";
   // Password of steghide encryption
    public final String PASSWORD_STEGHIDE = "SECU";


    enum TypePicture {
        GENERATED,
        FILE,
        EXIST
    }

    TypePicture imageKind;
    Image image;
    String data = "";
    String filepath;
    String execPath = EXEC_EXIF_PATH;
    String originalString = "";
    public DataController(TypePicture imageKind, String filepath){

        this.imageKind = imageKind;
        this.filepath = filepath;
         if(imageKind == TypePicture.EXIST) {
             retrieveThumb();
             removeThumb();
             return;
        }
        File file = new File(filepath);
        try {
            String localUrl = file.toURI().toURL().toString();
            image = new Image(localUrl);
        } catch (Exception error){
            System.out.println("Erreur lors de la création du DataController");
        }

    }

    private void retrieveThumb(){
        try {
            String cmdErase = removeFileCmd(TMP_THUMB_PATH);
            String cmd = "cmd.exe /c  \"" + execPath + "\" -b -ThumbnailImage  " + filepath + " > " + TMP_THUMB_PATH ;
            // rt.exec(cmdErase);
            Runtime rt = Runtime.getRuntime();
            rt.exec(cmd);
            Thread.sleep(500);
            File myObj = new File(TMP_THUMB_PATH);


            if(myObj.length() == 0){
                System.out.println("No thumb here");
                this.imageKind = TypePicture.GENERATED;
            } else {
                File file = new File(TMP_THUMB_PATH);
                String localUrl = file.toURI().toURL().toString();
                this.image = new Image(localUrl);

                //filepath = TMP_THUMB_PATH;
                TimeUnit.MILLISECONDS.sleep(200);
                this.setData(this.retrieveData(TMP_THUMB_PATH));
                //rt.exec(cmdErase);
                return;
            }


        } catch (Exception e){}
    }

    private void removeThumb(){
        try {
            String cmdErase = removeFileCmd(TMP_THUMB_PATH);
            Runtime rt = Runtime.getRuntime();
            rt.exec(cmdErase);
        } catch (Exception e){}
    }

    public DataController(TypePicture imageKind){

        this.imageKind = imageKind;
        this.filepath = filepath;

        File file = new File(filepath);
        try {
            String localUrl = file.toURI().toURL().toString();
            image = new Image(localUrl);
        } catch (Exception error){
            System.out.println("Erreur lors de la création du DataController");
        }

    }

    public Image getImage(){
        return image;
    }

    public String getData(){
        return data;
    }

    public Boolean setData(String s){
        // Inssert data in the images
        this.data = s;
        return true;
    }
    public TypePicture getType(){
        return imageKind;
    }
    public String getTitle(){
        switch(imageKind){
            case EXIST:
                return "Thumbnail founded in the jpeg";
            case GENERATED:
                return "Thumbnail generated from the original";
            case FILE:
                return "Thumbnail created from " + filepath;
        }
        return "ERREUR SWITCH";
    }

    public Boolean save(String originalPath, String savePos, String textToSave){
        if(this.imageKind == TypePicture.EXIST && this.getData().equals(this.originalString) ){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Original thumbnail");

            // alert.setHeaderText("Results:");
            alert.setContentText("Nothing to do");

            alert.showAndWait();
            return false;

        }

        if(this.imageKind == TypePicture.EXIST){
           retrieveThumb();
        } else {
            try {

                String cmdCopyOrigianl = "cmd.exe /c COPY \""+filepath+"\" \"" + TMP_THUMB_PATH +"\"";
                Runtime rt = Runtime.getRuntime();
                rt.exec(cmdCopyOrigianl);

            } catch (Exception e){}
        }

        try {
            // Embeding data

            String removeFileDest = removeFileCmd(savePos);
            embedeData(textToSave);
            String cmdCopyOrigianl = "cmd.exe /c COPY \""+originalPath+"\" \"" + savePos +"\"";
            String cmdCopyInsert = "cmd.exe /c  " + EXEC_EXIF_PATH +  " \"-ThumbnailImage<="+TMP_THUMB_PATH+"\" " + savePos ;
            System.out.println(" CMD = " + cmdCopyInsert);
            //  ./exiftool "-ThumbnailImage<=test2.jpg" image1.jpg
            Runtime rt = Runtime.getRuntime();
            rt.exec(removeFileDest);
            rt.exec(cmdCopyOrigianl);
            Thread.sleep(300);
            rt.exec(cmdCopyInsert);
            Thread.sleep(1500);
            String removeFileCopyExif = removeFileCmd(savePos + "_original");
            rt.exec(removeFileCopyExif);
            System.out.println(" CMD = " + removeFileCopyExif);
        } catch (Exception e){}

        System.out.println("Saving the thumb : " + filepath + " into : " + originalPath + " at position : " + savePos);


        removeThumb();
        return true;
    }

    public void embedeData(String s){
        if( getData() != null){



        String cmdEmbed = "cmd.exe /c " + EXEC_STEGHIDE_PATH + " embed  -f -cf " + TMP_THUMB_PATH + " -ef " + TMP_TEXT_PATH +" -p " + PASSWORD_STEGHIDE;

            try {
                Runtime rt = Runtime.getRuntime();

                Thread.sleep(300);
                PrintWriter tmpTextFile = new PrintWriter(TMP_TEXT_PATH);
                tmpTextFile.println(s);
                tmpTextFile.close();
                // Embeding data

                rt.exec(cmdEmbed);
                           } catch (Exception e){}
        }



    }

    public String retrieveData(String to_check){
        String cmdEmbed = "cmd.exe /c " + EXEC_STEGHIDE_PATH + " extract -sf " + to_check + " -xf " + TMP_TEXT_PATH +" -p " + PASSWORD_STEGHIDE;
        String res = "";
        try {
            Runtime rt = Runtime.getRuntime();
            rt.exec(removeFileCmd(TMP_TEXT_PATH));
            Thread.sleep(500);
            rt.exec(cmdEmbed);
            Thread.sleep(500);
            File myObj = new File(TMP_TEXT_PATH);


            if(myObj.length() == 0){
                return "";
            }
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                res += data;
            }
            myReader.close();

            rt.exec(removeFileCmd(TMP_TEXT_PATH));



        } catch (Exception e){}
        return res;
    }

    private String removeFileCmd(String filename){
        return "cmd.exe /c if exist " + filename +" del \"" + filename  ;
    }

}
