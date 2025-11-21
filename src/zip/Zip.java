package zip;

import java.io.*;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

public class Zip {

    String fileName;
    String absolutePath;
    File file;

    public Zip(String fileName, String absolutePath, File file){

        this.fileName=fileName;
        this.absolutePath=absolutePath;
        this.file=file;

    }

    public void ZipTheFile(){

        String zip=this.absolutePath+".zip";
        byte buffer[]=new byte[1024];
        try{
            FileInputStream fileIn=new FileInputStream(this.absolutePath);//obiect de tip FileInput pentru a citi datele din fisierul nearhivat
            FileOutputStream f=new FileOutputStream(zip);//obiect de tip FileOutput pentru a scrie datele in sifierul arhivat
            ZipOutputStream zipOut=new ZipOutputStream(f);//flux de iesire care adauga capablitati de compresie
            zipOut.putNextEntry(new ZipEntry(this.fileName));//specifica numele si calea interne sub care va fi stocat fisierul origina;
            int nBytes;
            while((nBytes = fileIn.read(buffer, 0, 1024)) != -1)//ruleaza atat timp cat se itesc date din fileIn
            {
                zipOut.write(buffer, 0, nBytes);

            }
            zipOut.close();
            fileIn.close();
            f.close();


        }
        catch(ZipException ze){
            System.out.println(ze.toString());
        }
        catch(FileNotFoundException fnfe){
            fnfe.printStackTrace();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
        }

    }
    public void ZipTheFolder(){

    }


}
