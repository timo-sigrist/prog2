package ch.zhaw.prog2.io;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;


public class UnderstandingCharsets {
    public static final String FILE_DESTINATION = "C:"+File.separator+"develop"+File.separator+"Semester2"+File.separator+"prog2-lab05-it21tbwin-sigritim"+File.separator+"code"+File.separator+"Charsets";
    public static final String DEFAULT_FILENAME = "CharSetEvaluation_Default.txt";
    public static final String ASCII_FILENAME = "CharSetEvaluation_ASCII.txt";

    public static void main(String[] args) {


        //a
        System.out.println("Default Charset: "+Charset.defaultCharset().name());

        System.out.println("All charsets:");
        for (Charset charset : Charset.availableCharsets().values()) {
            System.out.print(charset + ", ");
        }
        System.out.println();

        // b
        BufferedWriter defaultFile = null;
        BufferedWriter asciiFile = null;
        System.out.print("Userinput: ");
        BufferedInputStream userInput = new BufferedInputStream(System.in);

        try {
            defaultFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_DESTINATION + DEFAULT_FILENAME)));
            asciiFile = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_DESTINATION + ASCII_FILENAME), StandardCharsets.US_ASCII));
            int charValue = 0;
            do {
                charValue = userInput.read();
                if (charValue != -1){
                    defaultFile.write(charValue);
                    asciiFile.write(charValue);
                    defaultFile.flush();
                    asciiFile.flush();
                }
            } while (charValue != -1 && charValue != 'q');

        } catch (IOException exception) {
            exception.printStackTrace();
        } finally {
            try {
                defaultFile.close();
                asciiFile.close();
            } catch (IOException e) {
                System.out.println("File can't be closed");
                e.printStackTrace();
            }
        }
    }
}

