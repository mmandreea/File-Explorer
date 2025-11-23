package unzip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
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

        String caleCompletaZip = this.zip;
        File zipFile = new File(caleCompletaZip);
        File directorDestinatie = zipFile.getParentFile();

        if (directorDestinatie == null) {
            System.err.println("Eroare: Nu s-a putut determina directorul parinte al arhivei.");
            return;
        }

        byte buffer[] = new byte[1024];

        FileInputStream fileIn = null;
        ZipInputStream zipIn = null;

        try
        {
            fileIn = new FileInputStream(caleCompletaZip);
            zipIn = new ZipInputStream(fileIn);

            ZipEntry entry;
            while ((entry = zipIn.getNextEntry()) != null) {

                File nouaIntrare = new File(directorDestinatie, entry.getName());

                if (entry.isDirectory()) {
                    if (!nouaIntrare.exists()) {
                        nouaIntrare.mkdirs();
                        System.out.println("Director creat: " + nouaIntrare.getAbsolutePath());
                    }
                }
                else {
                    File directorParinte = nouaIntrare.getParentFile();
                    if (directorParinte != null && !directorParinte.exists()) {
                        directorParinte.mkdirs();
                    }

                    FileOutputStream fileO = null;
                    try {
                        fileO = new FileOutputStream(nouaIntrare);
                        int nBytes;
                        while ((nBytes = zipIn.read(buffer)) != -1) {
                            fileO.write(buffer, 0, nBytes);
                        }
                        System.out.println("Fisier dezarhivat: " + nouaIntrare.getAbsolutePath());
                    }
                    finally {
                        if (fileO != null) {
                            fileO.close();
                        }
                    }
                }

                zipIn.closeEntry();
            }

        }
        catch (ZipException ze) {
            System.out.println("Eroare ZIP: " + ze.toString());
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
        finally {
            try {
                if (zipIn != null) zipIn.close();
                if (fileIn != null) fileIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
