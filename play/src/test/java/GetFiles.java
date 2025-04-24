import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GetFiles {

    @Test
    public void test() throws IOException {
        printAllFilesIn("C:\\Users\\");
        printAllFilesIn("C:\\Program Files (x86)\\");
    }

    private void printAllFilesIn(String directory) throws IOException {
        File curDir = new File(directory);
        
        File f1 = createFileWithFollowNumber(curDir.getName());

        FileWriter fileWriter = new FileWriter(f1,true);
        BufferedWriter bw = new BufferedWriter(fileWriter);

        getAllFiles(curDir, bw);
        bw.flush();
        bw.close();
    }

    private File createFileWithFollowNumber(String filenameBase) throws IOException {
        int i = 1;
        while(true){
            File f1 = new File("D:\\" + filenameBase + "_" + String.format("%03d", i) + ".txt");
            if(!f1.exists()) {
                f1.createNewFile();
                return f1;
            }
            i++;
        }
    }

    private void getAllFiles(File curDir, BufferedWriter bw) throws IOException {
        System.out.println("In dir: " + curDir.getName());
        File[] filesList = curDir.listFiles();
        if(filesList == null){
            return;
        }
        for (File f: filesList) {
            if (f.isDirectory())
                getAllFiles(f, bw);
            if (f.isFile()) {
                bw.append(f.getAbsolutePath());
                bw.newLine();
            }
        }

    }
}