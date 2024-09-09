package ch.zhaw.prog2.io;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DirList {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        String pathName = args.length >= 1 ? args[0] : ".";
        File file = new File(pathName);
        StringBuilder sb = new StringBuilder();

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File dirFile : files) {
                WriteFileAttributes(sb, dirFile);

            }
        } else if(file.isFile()) {
            WriteFileAttributes(sb, file);
        } else {
            System.err.println("Given Path isn't a file or a directory");

        }

        // Write metadata of given file, resp. all of its files if it is a directory
        // Whith each file on one line in the following format.
        // - type of file ('d'=directory, 'f'=file)
        // - readable   'r', '-' otherwise
        // - writable   'w', '-' otherwise
        // - executable 'x', '-' otherwise
        // - hidden     'h', '-' otherwise
        // - modified date in format 'yyyy-MM-dd HH:mm:ss'
        // - length in bytes
        // - name of the file

        System.out.println(sb);
    }

    private static void WriteFileAttributes(StringBuilder sb, File dirFile) {
        sb.append(dirFile.isFile() ? "f" : "d").append(dirFile.canRead() ? "w" : "-").append(dirFile.canWrite() ? "r" : "-").append(dirFile.canExecute() ? "r" : "-")
        .append(dirFile.isHidden() ? "h" : "-").append(" " + dateFormat.format(dirFile.lastModified())).append(" "  + dirFile.getTotalSpace()).append(" " + dirFile.getName()).append(System.lineSeparator());
    }

}
