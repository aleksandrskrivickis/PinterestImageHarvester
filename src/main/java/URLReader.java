//package main.java;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class URLReader {

    int stat_countries = 0;
    int stat_images = 0;

    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame("InputDialog Example #1");
        String dest_dir = (String)JOptionPane.showInputDialog(frame, "Please enter destination directory!");

        URLReader rdr = new URLReader();
        Keywords keyw = new Keywords();


    for (int a = 0; a < keyw.countries.length; a++) {
        System.out.println(keyw.countries[a]);
        rdr.getCountry(keyw.countries[a]);
        rdr.stat_countries++;
    }

        System.out.println("Total amount of pictures from " + rdr.stat_countries + " countries: " + rdr.stat_images);

    }

    public void getCountry(String lang) throws Exception {
        String targetDirectory = "C:\\Users\\aleks\\Desktop\\FacesFromAllOverTheWorld\\" + lang + "\\";

        ArrayList<String> links = getAllImages(lang);
        download(links, targetDirectory);
    }

    public ArrayList<String> getAllImages(String lang) {
        ArrayList<String> lnks = new ArrayList<String>();
        try {
            //TODO add more search queries like: boy, girl, villager, worker,  old, student, face, portrait, head
            //DONE national, kid, child, face
            URL oracle = new URL("https://www.pinterest.co.uk/search/pins/?q=" + lang + "%20face%20"+ 11 + "&rs=typed");
            URL oracle1 = new URL("https://www.pinterest.co.uk/search/pins/?q=" + lang + "%20male%20face%20" + 11 +"&rs=typed");
            URL oracle2 = new URL("https://www.pinterest.co.uk/search/pins/?q=" + lang + "%20female%20face%20" + 11 +"&rs=typed");
            Pattern pattern = Pattern.compile("(https://i.pinimg.com/736x/).+?(jpg)");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null){
                Matcher matcher = pattern.matcher(inputLine);
                while (matcher.find()) {
                    if (!lnks.contains(matcher.group())){
                        lnks.add(matcher.group());
                    }
                }
            }

            BufferedReader in1 = new BufferedReader(
                    new InputStreamReader(oracle1.openStream()));

            String inputLine1;
            while ((inputLine = in1.readLine()) != null){
                Matcher matcher = pattern.matcher(inputLine);
                while (matcher.find()) {
                    if (!lnks.contains(matcher.group())){
                        lnks.add(matcher.group());
                    }
                }
            }

            BufferedReader in2 = new BufferedReader(
                    new InputStreamReader(oracle2.openStream()));

            String inputLine2;
            while ((inputLine = in2.readLine()) != null){
                Matcher matcher = pattern.matcher(inputLine);
                while (matcher.find()) {
                    if (!lnks.contains(matcher.group())){
                        lnks.add(matcher.group());
                    }
                }
            }
            System.out.println("Finished picture gathering from: " + oracle +
                    "\nTotal pictures collected: " + lnks.size());
            in.close();
            in1.close();
            in2.close();


        } catch (java.io.IOException e) {
            System.out.println(e);
        }
        return lnks;
    }

    public void download(ArrayList<String> x, String targetDirectory) throws IOException
    {
        //Create directory if does not exist
        Path path = Paths.get(targetDirectory);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                //fail to create directory
                e.printStackTrace();
            }
        }

        String sourceURL = "";
        for (int i = 0; i < x.size(); i++){
            try {
            sourceURL = x.get(i);
            URL url = new URL(sourceURL);
            String fileName = sourceURL.substring(sourceURL.lastIndexOf('/') + 1, sourceURL.length());
            Path targetPath = new File(targetDirectory + File.separator + fileName).toPath();
            Files.copy(url.openStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

            //Detect face in image and delete if more or less than one face
            FaceDetector fd = new FaceDetector();
            fd.DetectOneFaceAndOverWriteCropOrDelete(targetPath.toString());
            ////////////////////////////////////////////////////////////////

                //Implement deletion of files smaller than 6kB
            stat_images++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}