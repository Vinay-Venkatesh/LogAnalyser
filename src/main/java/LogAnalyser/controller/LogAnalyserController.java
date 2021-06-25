package LogAnalyser.controller;

import LogAnalyser.model.Logs;
import LogAnalyser.service.LogService;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.GZIPInputStream;

import static java.nio.file.Files.createDirectory;

@Controller
public class LogAnalyserController {

    static String folderName = "/home/app-user/uncompressed_files"; // This path is configured in the environment dockerfile

    @Autowired
    LogService logService;

    /*
        @param       - Model Object
        @return      - Status
     */
    @RequestMapping("/insertLogs")
    public ResponseEntity<String> addLogData(Model model) {
        createFolder();
        File gzip_folderPath = new File("/home/app-user/logs"); // This path is configured in the environment dockerfile
        unZip(gzip_folderPath);
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        // Parsing the log file
                        String[] attributes = line.replaceAll("\\s+"," ").trim().split(" ");
                        String date = attributes[0].split(":")[1];
                        String time = attributes[1];
                        String id = attributes[2].split(":")[1];
                        String level = attributes[3].split(":")[1];
                        String timeStamp = date+"-"+time;
                        String method = attributes[4].split(":")[1];
                        String uri = attributes[5].split(":")[1];
                        String reqTime = attributes[6].split(":")[1];
                        Logs logs = new Logs();
                        logs.setTime(timeStamp);
                        logs.setLog_id(id);
                        logs.setLevel(level);
                        logs.setUri(uri);
                        logs.setMethod(method);
                        logs.setReqtime(reqTime);
                        logService.addData(logs);
                        // Remove the folder that was created to store uncompressed file
                        removeFolder();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return new ResponseEntity<String>("{\"status\" : \"Completed\"}", HttpStatus.OK);
    }

    /*
        @param       - Model Object
        @return      - count of uri
    */
    @RequestMapping("/getURICount")
    public ResponseEntity<String> getURICount(Model model) {
        int count = logService.getUriCount();
        return new ResponseEntity<String>("{\"count\" :"+ count+"}", HttpStatus.OK);
    }

    public static void unZip(File gzip_folderPath) {
        byte[] buffer = new byte[1024];

        try {
            for (final File fileEntry : gzip_folderPath.listFiles()) {
                if (fileEntry.isDirectory()) {
                    unZip(fileEntry);
                } else {
                    FileInputStream fileIn = new FileInputStream(fileEntry);
                    GZIPInputStream gZIPInputStream = new GZIPInputStream(fileIn);
                    File filesOut = new File(folderName + "/" + fileEntry.toString().replace("/", "").replace("logs", "").replace(".gz", ""));
                    FileOutputStream fileOutputStream = new FileOutputStream(filesOut);

                    int bytes_read;

                    while ((bytes_read = gZIPInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, bytes_read);
                    }
                    gZIPInputStream.close();
                    fileOutputStream.close();
                }
            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Method to create a seperate folder to store unzipped file
    public static void createFolder(){
        File dir = new File(folderName);
        Path path = null;
        try {
            if (!dir.exists()) {
                path = createDirectory(Paths.get(folderName));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //Method to remove folder created to store unzipped file
    public static void removeFolder() {
        try{
            FileUtils.deleteDirectory(new File(folderName));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
