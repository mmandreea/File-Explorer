package fileContent;

import partions.GetPartitions;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileContent
{

    private Path path;
    private String content;

    public String extractContentFromFile(File file)
    {
        try {

            path=file.toPath();
            content = Files.readString(path, StandardCharsets.UTF_8);

        }
        catch (IOException e) {
             String error="Error reading file "+file.getName()+ "/nCauza: "+e.getMessage();
             JOptionPane.showMessageDialog(
                     null,
                     error,
                     "Eroare citire fisier",
                     JOptionPane.ERROR_MESSAGE

             );

        }
        return content;
    }


}
