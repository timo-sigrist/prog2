package ch.zhaw.prog2.io.picturedb;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Logger;

/**
 * Implements the PictureDatasource Interface storing the data in
 * Character Separated Values (CSV) format, where each line consists of a record
 * whose fields are separated by the DELIMITER value ";"
 * See example file: db/picture-data.csv
 */
public class FilePictureDatasource implements PictureDatasource {
    // Charset to use for file encoding.
    protected static final Charset CHARSET = StandardCharsets.UTF_8;
    // Delimiter to separate record fields on a line
    protected static final String DELIMITER = ";";
    // Date format to use for date specific record fields
    protected static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    private final String databasePath;
    private final String tempDatabase;

    private final Logger logger = Logger.getLogger(LogConfiguration.class.getCanonicalName());

    /**
     * Creates the FilePictureDatasource object with the given file path as datafile.
     * Creates the file if it does not exist.
     * Also creates an empty temp file for write operations.
     *
     * @param filepath of the file to use as database file.
     * @throws IOException if accessing or creating the file fails
     */
    public FilePictureDatasource(String filepath) throws IOException {
        if (new File(filepath).isFile()) {
            databasePath = filepath;
            tempDatabase = filepath.substring(0, filepath.length() - 4) + "_temp.csv";
        } else {
            logger.severe("Databasefile was not found");
            throw new IOException("Databasefile was not found");
        }
    }

    private int getNextId(){
        int highestId = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(databasePath)))) {
            String line;
            do {
                line = br.readLine();
                if (line != null) {
                    int foundId = Integer.parseInt(line.substring(0, line.indexOf(DELIMITER)));
                    if (foundId > highestId) {
                        highestId = foundId;
                    }
                }
            } while (line != null);
        } catch (IOException ioException) {
            logger.severe(ioException.getMessage());
        }
        return highestId + 1;
    }

    private Picture mapPictureFromDatabaseString(String databaseString) {
        Picture picture = null;
        try {
            String[] parts = databaseString.split(DELIMITER);
            int id = Integer.parseInt(parts[0]);
            Date date = dateFormat.parse(parts[1]);
            float longitude = Float.parseFloat(parts[2]);
            float latitude = Float.parseFloat(parts[3]);
            String title = parts[4];
            URL url = new URL(parts[5]);
            picture = new Picture(id, url, date,  title, longitude, latitude);
        } catch (ParseException | MalformedURLException parseException) {
            logger.severe(parseException.getMessage());
        }
        return picture;
    }

    private void syncDatabase() {
        if (new File(databasePath).exists() && new File(tempDatabase).exists()) {
            new File(databasePath).delete();
            new File(tempDatabase).renameTo(new File(databasePath));
        }
        logger.info("database synced");
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void insert(Picture picture) {
        try (BufferedReader databaseReader = new BufferedReader(new InputStreamReader(new FileInputStream(databasePath)));
             BufferedWriter databaseWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempDatabase)))) {
            // copy database
            String line;
            do {
                line = databaseReader.readLine();
                if (line != null) {
                    databaseWriter.write(line);
                    databaseWriter.newLine();
                }
            } while (line != null);
            picture.setId(getNextId());
            String newPicture = getNextId() + DELIMITER + dateFormat.format(picture.getDate()) + DELIMITER + picture.getLongitude() + DELIMITER + picture.getLatitude() + DELIMITER +
                picture.getTitle() + DELIMITER + picture.getUrl();
            databaseWriter.write(newPicture);
            databaseWriter.newLine();
            logger.info("Picture inserted");
        } catch (IOException ioException) {
            logger.severe(ioException.getMessage());
        }
        syncDatabase();
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void update(Picture picture) throws RecordNotFoundException {
        boolean recordWasFound = false;
        try (BufferedReader databaseReader = new BufferedReader(new InputStreamReader(new FileInputStream(databasePath)));
             BufferedWriter databaseWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempDatabase)))) {
            String line = null;
            do {
                line = databaseReader.readLine();
                if (line != null) {
                    if (picture.getId() == Integer.parseInt(line.substring(0, line.indexOf(DELIMITER)))) {
                        recordWasFound = true;
                        String newPicture =  picture.getId() + DELIMITER + dateFormat.format(picture.getDate()) + DELIMITER + picture.getLongitude() + DELIMITER + picture.getLatitude() + DELIMITER +
                            picture.getTitle() + DELIMITER + picture.getUrl();
                        databaseWriter.write(newPicture);
                    } else {
                        databaseWriter.write(line);
                    }
                    databaseWriter.newLine();
                }
            } while (line != null);
            logger.info("updated");
        } catch (IOException ioException) {
            logger.severe(ioException.getMessage());
        }
        syncDatabase();
        if (!recordWasFound) {
            throw new RecordNotFoundException("Record with id: " + picture.getId() + " was not found!");

        }
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public void delete(Picture picture) throws RecordNotFoundException {
        boolean recordWasFound = false;
        try (BufferedReader databaseReader = new BufferedReader(new InputStreamReader(new FileInputStream(databasePath)));
             BufferedWriter databaseWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempDatabase)))) {
            String line = null;
            do {
                line = databaseReader.readLine();
                if (line != null) {
                    if (picture.getId() == Integer.parseInt(line.substring(0, line.indexOf(DELIMITER)))) {
                        recordWasFound = true;
                        continue;
                    }
                    databaseWriter.write(line);
                    databaseWriter.newLine();
                }
            } while (line != null);
            logger.info("Deleted");
        } catch (IOException ioException) {
            logger.severe(ioException.getMessage());
        }
        syncDatabase();
        if (!recordWasFound) {
            logger.severe("Record with id: " + picture.getId() + " was not found!");
            throw new RecordNotFoundException("Record with id: " + picture.getId() + " was not found!");
        }
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public long count() {
        int countindex = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(databasePath)))) {
            String line;
            do {
                line = br.readLine();
                if (line != null) {
                    countindex++;
                }
            } while (line != null);
        } catch (IOException ioException) {
            logger.severe(ioException.getMessage());
        }
        return countindex;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Picture findById(long id) {
        Picture picture = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(databasePath)))) {
            String line;
            do {
                line = br.readLine();
                if (line != null) {
                    int foundId = Integer.parseInt(line.substring(0, line.indexOf(DELIMITER)));
                    if (foundId == id) {
                        picture =  mapPictureFromDatabaseString(line);
                        break;
                    }
                }
            } while (line != null);
        } catch (IOException ioException) {
            logger.severe(ioException.getMessage());
        }
        return picture;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Collection<Picture> findAll() {
        Collection<Picture> pictures = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(databasePath)))) {
            String line;
            do {
                line = br.readLine();
                if (line != null) {
                    pictures.add(mapPictureFromDatabaseString(line));
                }
            } while (line != null);
        } catch (IOException ioException) {
            logger.severe(ioException.getMessage());
        }
        return pictures;
    }

    /**
     * {@inheritDoc}
     *
     */
    @Override
    public Collection<Picture> findByPosition(float longitude, float latitude, float deviation) {
        Collection<Picture> pictures = new ArrayList<>();
        Collection<Picture> allPictures = findAll();

        float fromLongitudeValue = longitude - deviation;
        float fromLatitudevalue =  latitude - deviation;
        float toLongitudeValue = longitude + deviation;
        float toLatitudevalue =  latitude + deviation;

        for(Picture picture : allPictures) {
            if(picture.getLongitude() >= fromLongitudeValue && picture.getLongitude() <= toLongitudeValue
                && picture.getLatitude() >= fromLatitudevalue && picture.getLatitude() <= toLatitudevalue) {
                pictures.add(picture);
            }
        }
        return pictures;
    }

}
