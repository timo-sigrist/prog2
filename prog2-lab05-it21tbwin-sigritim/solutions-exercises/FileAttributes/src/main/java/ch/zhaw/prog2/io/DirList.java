package ch.zhaw.prog2.io;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DirList {
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        String pathName = args.length >= 1 ? args[0] : ".";
        File file = new File(pathName);
        if (file.isDirectory()) {
            for (File subFile : file.listFiles()) {
                System.out.println(printFileMetadata(subFile));
            }
        } else {
            System.out.println(printFileMetadata(file));
        }
    }

    public static String printFileMetadata(File file) {
        return String.format("%c%c%c%c%c %s %8d %s",
            file.isDirectory()? 'd' : 'f',
            file.canRead()? 'r' : '-',
            file.canWrite()? 'w' : '-',
            file.canExecute()? 'x' : '-',
            file.isHidden()? 'h' : '-',
            dateFormat.format(file.lastModified()),
            file.length(),
            file.getName());
    }
}
