package ch.zhaw.prog2.io;

import java.io.*;
import java.util.*;


public class FileCopy {

	public static void main(String[] args) throws IOException {

		// get the filename from the arguments. By default, use 'files'-directory in current working directory.
        String sourceDirPath = args.length >= 1 ? args[0] : "./files";
        File sourceDir = new File(sourceDirPath);

        // Part a – Verify the directory structure
        // Implement the method 'verifySourceDir()'
        System.out.format("Verifying Source Directory: %s%n", sourceDirPath);
        try {
            verifySourceDir(sourceDir);
        } catch (FileNotFoundException error) {
            System.err.format("Directory %s does not comply with predefined structure: %s%n", sourceDir.getPath(), error.getMessage());
            System.err.println("Terminating programm!");
            System.exit(1);
        }
        System.out.printf("Source Directory verified successfully.");


        // Part b – Copy the files byte resp. char wise.
        // Implement the method 'verifySourceDir()'
        System.out.println("Initiating file copies.");
        try {
            copyFiles(sourceDir);
        } catch (IOException error) {
            System.err.format("Error creating file copies:  %s%n", error.getMessage());
            System.err.println("Terminating programm!");
            System.exit(2);
        }
        System.out.println("Files copied successfully.");
    }

    /**
     * Part a – directory structure
     *
     * Verify the directory structure for correctness.
     * Correct means, that the directory exists and beside the two files rmz450.jpg and rmz450-spec.txt does not contain
     * any other files or directories.
     *
     * @param sourceDir File source directory to verify it contains the correct structure
     * @throws FileNotFoundException if the source directory or required file are missing
     * @throws InvalidObjectException if the source directory contains invalid files or directories.
     */
    private static void verifySourceDir(File sourceDir) throws FileNotFoundException, InvalidObjectException {
        if (sourceDir.isDirectory()) {
            File[] files = sourceDir.listFiles();
            for (File file : files) {
                if (file.isFile() && (file.getName().equals("rmz450-spec.txt") || file.getName().equals("rmz450.jpg"))) {
                    System.out.println(file.getName() + " exists");
                } else {
                    throw new FileNotFoundException("Couldn't found rmz450.jpg or rmz450-spec.txt");
                }
            }
        } else {
            throw new FileNotFoundException("Directory does not exists");
        }
    }


    /**
     * Teilaufgabe b – Kopieren von Dateien
     *
     * Copies each file of the source directory twice, once character-oriented and once byte-oriented.
     * Source and target files should be opened and copied byte by byte respectively char by char.
     * The target files should be named, so the type of copy can be identified.
     *
     * @param sourceDir File representing the source directory containing the files to copy
     * @throws IOException if an error is happening while copying the files
     */
    private static void copyFiles(File sourceDir) throws IOException {
        File[] files = sourceDir.listFiles();
        for (File file : files) {
            String byteOrientedPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 4) + "_byte_oriented.txt";
            String charOrientedPath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 4) + "_char_oriented.txt";

            // byte orientiert
            BufferedInputStream byteInputStream = new BufferedInputStream(new FileInputStream(file));
            BufferedOutputStream byteOutputStream = new BufferedOutputStream(new FileOutputStream(byteOrientedPath));
            try {
                int bytesRead;
                byte[] byteBuffer = new byte[32];
                while ((bytesRead = byteInputStream.read(byteBuffer)) >= 0) {
                    byteOutputStream.write(byteBuffer, 0, bytesRead);
                }
            } finally {
                byteInputStream.close();
                byteOutputStream.close();
            }

            // char orientiert
            BufferedReader charInputReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            BufferedWriter charOutputReader= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(charOrientedPath)));
            try {
                int readChar;
                do {
                    readChar = charInputReader.read();
                    charOutputReader.write(readChar);
                } while (readChar != -1);
            } finally {
                charInputReader.close();
                charOutputReader.close();
            }
        }

    }
}
