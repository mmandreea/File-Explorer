package unzip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipException;
import java.util.zip.ZipInputStream;

public class Unzip {

    String zip;

    public Unzip(String zip)
    {
        this.zip=zip;
    }
    public void UnzipTheFile() {

        String caleCompletaZip = this.zip;
        File zipFile = new File(caleCompletaZip);
        File directorDestinatie = zipFile.getParentFile();///pt locatia fisierului dezarhivat am dat directorul parinte al fisierului arhivat
        if (directorDestinatie == null) {
            System.err.println("Eroare: Nu s-a putut determina directorul parinte al arhivei.");
            return;
        }

        String numeFisierZip = zipFile.getName();
        String numeBazaInitial = numeFisierZip.substring(0, numeFisierZip.lastIndexOf(".zip"));

        String numeFisierCurent = numeBazaInitial;
        File fisierIesire;
        int contor = 1;

        do {
            fisierIesire = new File(directorDestinatie, numeFisierCurent);

            if (fisierIesire.exists()) {

                int indexPunct = numeBazaInitial.lastIndexOf('.');
                String baza = (indexPunct > 0) ? numeBazaInitial.substring(0, indexPunct) : numeBazaInitial;
                String extensie = (indexPunct > 0) ? numeBazaInitial.substring(indexPunct) : "";

                numeFisierCurent = baza + "(" + contor + ")" + extensie;
                contor++;
            }

        } while (fisierIesire.exists());

        byte buffer[] = new byte[1024];

        try (
                FileInputStream fileIn = new FileInputStream(caleCompletaZip);
                ZipInputStream zipIn = new ZipInputStream(fileIn);
                FileOutputStream fileO = new FileOutputStream(fisierIesire);
        )
        {
            zipIn.getNextEntry();

            int nBytes;
            while ( (nBytes = zipIn.read(buffer, 0, 1024)) != -1)
                fileO.write(buffer, 0, nBytes);


        }
        catch (ZipException ze) {
            System.out.println("Eroare ZIP: " + ze.toString());
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }


    public void UnzipTheFolder(){

    }
}
