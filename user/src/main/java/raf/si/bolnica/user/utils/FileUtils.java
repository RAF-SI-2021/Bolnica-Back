package raf.si.bolnica.user.utils;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class FileUtils {

    public static List<String> readUserTitles() {
        Scanner titlesFile = null;
        try {
            titlesFile = new Scanner(new File("Titule.txt")).useDelimiter("\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert titlesFile != null;
        return readFromFile(titlesFile);
    }

    private static List<String> readFromFile(Scanner titlesFile) {
        String title;

        List<String> titleList = new ArrayList<>();

        while (titlesFile.hasNext()) {
            title = titlesFile.next();
            titleList.add(title);
        }
        titlesFile.close();

        return titleList;
    }

    public static List<String> readUserProfessions() {
        Scanner professionFile = null;
        try {
            professionFile = new Scanner(new File("Zanimanja.txt")).useDelimiter("\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert professionFile != null;
        return readFromFile(professionFile);
    }

}
