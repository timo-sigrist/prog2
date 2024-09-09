package ch.zhaw.prog2.io.picturedb;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;

/* This demo-application reads some picture data from terminal,
 * saves it to the datasource, read it from the DB and prints the result
 */

public class PictureImport {
    private static final String PICTURE_DB = "db/picture-data.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private static final PrintWriter out = new PrintWriter(System.out, true);


    public static void main (String[] args) throws IOException {
        // initialize logger level
        LogConfiguration.setLogLevel(FilePictureDatasource.class, Level.FINE);

        // create datasource
        PictureDatasource dataSource = new FilePictureDatasource(PICTURE_DB);
        // read picture data from the terminal
        Picture picture = createPicture();
        // save the picture to the data source
        dataSource.insert(picture);
        // read the picture back from file
        Picture readPicture = dataSource.findById(picture.getId());
        if (readPicture != null) {
            out.println("The following pictures has been saved: ");
            out.println(readPicture);
        } else {
            out.println("Picture with id=" + picture.getId() + " not found.");
        }

        // read all pictures and list them on the console
        Collection<Picture> pictures = dataSource.findAll();
        out.println("Pictures:");
        for (Picture pict : pictures) {
            out.println(pict.toString());
        }
    }

    static Picture createPicture() {
        // asks the values for the objects
        out.println("** Create a new picture **");
        URL url = null;
        do {
            String urlString = prompt("Picture URL: ");
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e1) {
                out.println("Malformed URL: " + e1.getMessage());
            }
        } while (url == null);

        String title = prompt("Picture title: ");

        Date date = new Date(); // now
        try {
            date = dateFormat.parse(prompt("Picture time ("+DATE_FORMAT+") Default = now: "));
        } catch (ParseException e) {
            out.println("Unknown date format. Using "+ dateFormat.format(date));
        }

        float longitude = 0.0f;
        try {
            longitude = Float.parseFloat(prompt("Picture position longitude: "));
        } catch (NumberFormatException e) {
            out.println("Unknown number format. Using " + longitude);
        }

        float latitude = 0.0f;
        try {
            latitude = Float.parseFloat(prompt("Picture position latitude: "));
        } catch (NumberFormatException e) {
            out.println("Unknown number format. Using " + latitude);
        }

        return new Picture(url, date, title, longitude, latitude);
    }

    // prompt function -- to read input string
    static String prompt(String prompt) {
        try {
            Scanner scanner = new Scanner(System.in);
            out.print(prompt);
            out.flush();
            return scanner.nextLine().strip();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

}

