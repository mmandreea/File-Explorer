import partions.GetPartitions;
import fileContent.FileContent;
import zip.Zip;
import unzip.Unzip;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

class App extends JFrame{


    private JTable fileTable;
    private DefaultTableModel model;
    private File currentDir;
    private JButton renameButton;
    private JButton deleteButton;
    private JButton previousButton;///for the folder that is above the current folder
    private JButton zipButton;
    private JButton unzipButton;

    public App(){
        setTitle("My File Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,400);
        setLocationRelativeTo(null);
        String[] columnNames =new String[3];
        columnNames[0]="Name";
        columnNames[1]="Type";
        columnNames[2]="Size";
        model=new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {return false;}
        };
        fileTable=new JTable(model);
        fileTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(fileTable);
        add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel=new JPanel();
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        deleteButton=new JButton("Delete");
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        renameButton=new JButton("Rename");
        renameButton.setBackground(Color.BLUE);
        renameButton.setForeground(Color.WHITE);
        previousButton=new JButton("Go Back");
        previousButton.setForeground(Color.BLACK);
        zipButton=new JButton("ZIP");
        zipButton.setForeground(Color.WHITE);
        zipButton.setBackground(Color.ORANGE);
        unzipButton=new JButton("UNZIP");
        unzipButton.setForeground(Color.WHITE);
        unzipButton.setBackground(Color.ORANGE);
        buttonPanel.add(deleteButton);
        buttonPanel.add(renameButton);
        buttonPanel.add(previousButton);
        buttonPanel.add(zipButton);
        buttonPanel.add(unzipButton);
        add(buttonPanel,BorderLayout.SOUTH);

        previousButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e){

                if (currentDir != null) {
                    File parent = currentDir.getParentFile();

                    if (parent != null) {
                        refreshTable(parent);
                    } else {

                        model.setRowCount(0);
                        currentDir = null;
                    }
                }
            }

        });

        deleteButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                int row=fileTable.getSelectedRow();
                String fileName =fileTable.getValueAt(row,0).toString();
                String filePath=currentDir.getAbsolutePath()+File.separator+ fileName;
                boolean delete=confirm(filePath);
                File f=new File(filePath);
                if(delete==true){
                   f.delete();
                    ((DefaultTableModel)fileTable.getModel()).removeRow(row);

                }


            }
        });

        ///  zip button
        zipButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e){
                int row=fileTable.getSelectedRow();
                String fileName=fileTable.getValueAt(row, 0).toString();
                String filePath=currentDir.getAbsolutePath()+File.separator + fileName;
                File file=new File(filePath);
                Zip zipObj=new Zip(fileName, filePath, file);
                if(file.isFile()){
                    zipObj.ZipTheFile();
                }
                else
                {
                    zipObj.ZipTheFolder();
                }
                refreshTable(currentDir);

            }

        });

        ///unzip button
        unzipButton.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e){

                int row=fileTable.getSelectedRow();
                String fileName=fileTable.getValueAt(row, 0).toString();
                String filePath=currentDir.getAbsolutePath()+File.separator+ fileName;
                File file=new File(filePath);
                Unzip unzipObj=new Unzip(filePath);
                if(file.isFile())
                    unzipObj.UnzipTheFile();
                else
                    unzipObj.UnzipTheFolder();
                refreshTable(currentDir);


            }

        } );

        renameButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                int row=fileTable.getSelectedRow();
                String fileName =fileTable.getValueAt(row,0).toString();
                String filePath=currentDir.getAbsolutePath()+File.separator+ fileName;
                File f=new File(filePath);
                JTextField textField=new JTextField(20);
                JPanel p=new JPanel(new FlowLayout(FlowLayout.CENTER));
                JLabel label=new JLabel("New name :");
                p.setBackground(Color.LIGHT_GRAY);
                p.add(label);
                p.add(textField);

                JOptionPane.showMessageDialog(null,p,"Rename",JOptionPane.PLAIN_MESSAGE);
                String newFileName=textField.getText();
                String newFilePath=currentDir.getAbsolutePath()+File.separator+ newFileName;
                File f1=new File(newFilePath);
                f.renameTo(f1);
                refreshTable(currentDir);
            }
        });

        /// Partitions Buttons

        JToolBar partitionTool=new JToolBar();
        add(partitionTool,BorderLayout.NORTH);
        partitionTool.setLayout(new FlowLayout(FlowLayout.CENTER));
        partitionTool.setBackground(Color.LIGHT_GRAY);
        GetPartitions manager=new GetPartitions();
        File[] partitions=manager.getPartitions();
        for(File currentPartition:partitions){
            JButton button=new JButton(Arrays.toString(manager.getPartitionName(currentPartition)));
            button.setBackground(Color.GREEN);
            partitionTool.add(button);
            button.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e){
                    currentDir=currentPartition;
                    refreshTable(currentPartition);
                }
            });

        }

        fileTable.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount()==2){
                    int row=fileTable.getSelectedRow();
                    if(row!=-1){
                        String filename=fileTable.getValueAt(row,0).toString();
                        String filePath=currentDir.getAbsolutePath()+File.separator+filename;
                        File file=new File(filePath);
                        if(file.isDirectory()){
                            refreshTable(file);
                        }
                        else
                            if(file.isFile())
                            {

                                try{
                                    FileContent contentExtractor=new FileContent();
                                    String content=contentExtractor.extractContentFromFile(file);
                                    JTextArea textArea=new JTextArea(content);
                                    textArea.setEditable(false);
                                    textArea.setDisabledTextColor(Color.BLACK);
                                    JScrollPane scrollPaneText=new JScrollPane(textArea);
                                    add(scrollPaneText,BorderLayout.CENTER);
                                    scrollPaneText.setPreferredSize(new Dimension(500,400));
                                    JOptionPane.showMessageDialog(
                                            null,
                                            scrollPaneText,
                                            "Continut: "+file.getName(),
                                            JOptionPane.PLAIN_MESSAGE

                                    );

                                    }
                                    catch(Exception ex){
                                        String msg=ex.getMessage();
                                        JOptionPane.showMessageDialog(
                                                null,
                                                msg,
                                                "Eroare afisare!",
                                                JOptionPane.ERROR_MESSAGE

                                        );

                                    }
                        }
                    }
                }
            }
        });


    }

    public boolean confirm(String fileName){

        JPanel panel=new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBackground(Color.LIGHT_GRAY);
        JLabel messageLabel=new JLabel("Sunteti siguri ca doriti sa stergeti?");
        panel.add(messageLabel, BorderLayout.NORTH);

        String[] options={"Delete", "Cancel"};
        int result = JOptionPane.showOptionDialog(
                null,
                panel,
                "Confirmare Stergere",
                JOptionPane.YES_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[1]
        );
        if(result == JOptionPane.YES_OPTION){
            return true;
        }
        return false;
    }

    private void refreshTable(File directory){

        if(directory.isDirectory()){
            model.setRowCount(0);
            currentDir=directory;
            File[] files=directory.listFiles();
            if(files!=null){
                for(int i=0;i<files.length;i++){
                    String name=files[i].getName();
                    String type=files[i].isDirectory()?"Folder":"File";
                    String size=files[i].isFile()?String.valueOf(files[i].length()):"-";
                    model.addRow(new Object[]{name,type,size});
                }
            }
        }

    }


    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{new App().setVisible(true);});


            
    }


}