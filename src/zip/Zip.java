package zip;

import java.io.*;
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
    /// Creez o singura data un fisier de tip zip si apelez metoda recursica addFileToZip
    /// In metoda addFileToZip se adauga fiecare fisier copil in fiesierul Zip si daca fisierul este de fapt folder se reapeleaza metoda addFileToZip

    public void ZipTheFolder(){

        if (!this.file.exists() || !this.file.isDirectory()){
            System.out.println("Calea specificata nu este un director valid: " + this.absolutePath);
            return;
        }

        String zipFilePath = this.absolutePath + ".zip";

        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        try {
            fos = new FileOutputStream(zipFilePath);
            zos = new ZipOutputStream(fos);

            String rootDirName = this.file.getName();
            addFileToZip(this.file, rootDirName, zos);

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (zos != null) zos.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addFileToZip(File fileToZip, String entryName, ZipOutputStream zos) throws IOException {

        if (!fileToZip.exists() || fileToZip.isHidden()) {
            return;
        }

        if (fileToZip.isDirectory()) {

            String dirEntryName = entryName;
            if (!dirEntryName.endsWith(File.separator)) {
                dirEntryName = dirEntryName + "/";
            }

            ZipEntry zipEntry = new ZipEntry(dirEntryName);
            zos.putNextEntry(zipEntry);
            zos.closeEntry();

            File[] children = fileToZip.listFiles();
            if (children != null) {
                for (File childFile : children) {
                    addFileToZip(childFile, dirEntryName + childFile.getName(), zos);
                }
            }
            return;
        }

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileToZip);

            ZipEntry zipEntry = new ZipEntry(entryName);
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int nBytes;
            while ((nBytes = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, nBytes);
            }
            zos.closeEntry();

        }
        finally {
            if (fis != null) fis.close();
        }
    }
}
