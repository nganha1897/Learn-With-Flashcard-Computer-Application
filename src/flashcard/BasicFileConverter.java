package flashcard;

import java.io.*;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BasicFileConverter implements FileConverter {

    private String fileName;
    private File file;
    private boolean fileExisted = false;

    public BasicFileConverter(String fileName) {
        this.fileName = fileName;
        this.file = new File(fileName);
        try {
            fileExisted = !this.file.createNewFile();
        } catch (IOException e) {
            Logger.getLogger(BasicFileConverter.class.getName()).log(Level.SEVERE, "Error initializing course file converter", e);
        }
    }

    public boolean doesFileExist() {
        return fileExisted;
    }

    public void serializeCourse(Map<String, Course> courseMap) {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName, false);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(courseMap);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            Logger.getLogger(BasicFileConverter.class.getName()).log(Level.SEVERE, "Error serializing the course map", e);
        }
    }

    public Map<String, Course> deserializeCourse() {
        Map<String, Course> courses = null;
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            courses = (Map<String, Course>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(BasicFileConverter.class.getName()).log(Level.SEVERE, "Error deserializing the course map", e);
        }
        return courses;
    }

    public void serializeCard(Map<String, FlashCard> cardMap) {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName, false);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(cardMap);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            Logger.getLogger(BasicFileConverter.class.getName()).log(Level.SEVERE, "Error serializing the card map", e);
        }
    }

    public Map<String, FlashCard> deserializeCard() {
        Map<String, FlashCard> cards = null;
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            cards = (Map<String, FlashCard>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            Logger.getLogger(BasicFileConverter.class.getName()).log(Level.SEVERE, "Error deserializing the card map", e);
        }
        return cards;
    }

    public void fileClose() {
        this.file.delete();
    }
}
