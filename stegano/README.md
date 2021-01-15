The code is written in Java and use Javafx library.

This software is programmed for a Windows utilisation.

To properly run the program you need to download software and some variables need to be change

- download Exiftool : https://exiftool.org/
- download http://steghide.sourceforge.net/

Change the variable in Main.java and DataController.java as follow :

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

Then you can compile / run the program

About java Class :

Main.java is handling both UI for the whole programm and the data controller.

DataController.java is handling all the informations about the picture that is loaded in the software

MainImagePane.java is a UI java class for showing images on screen.