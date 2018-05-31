package flashcard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Course implements Serializable {
    private String courseID = "0";
    private String courseName;
    private transient FileConverter fileConverter;
    private Map<String, FlashCard> courseCards = new HashMap<>();
    private static final long serialVersionUID = -6552473766035773662L;

    public Course(String courseName, Map<String, FlashCard> courseCards) {
        this.courseName = courseName;
        this.courseCards = courseCards;
    }

    public Course(String courseName) {
        this.courseName = courseName;
        readCardFile(courseName);
    }

    public void readCardFile(String courseName) {
        String CARD_FILE_NAME = "src/resources/" + courseName + ".txt";
        fileConverter = new BasicFileConverter(CARD_FILE_NAME);
        try {
            BufferedReader br = new BufferedReader(new FileReader(CARD_FILE_NAME));
            if (fileConverter.doesFileExist() && br.readLine() != null) {
                Map<String, FlashCard> cardMap = fileConverter.deserializeCard();
                courseCards = cardMap == null ? new HashMap<>() : cardMap;
            }
        } catch (IOException e) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, "Error reading file", e);
        }
    }

    public boolean addCard(FlashCard newCard) {
        if (!isNewCardValid(newCard)) {
            return false;
        }
        String cardID = generateCardID();
        newCard.setCardID(cardID);
        courseCards.put(cardID, newCard);
        fileConverter.serializeCard(courseCards);
        return true;
    }

    private boolean isNewCardValid(FlashCard newCard) {
        if (newCard != null && !newCard.isInvalid())
            return true;
        return false;
    }

    private String generateCardID() {
        Random random = new Random();
        int id = random.nextInt(courseCards.size() + 1);
        while (courseCards.containsKey(Integer.toString(id))) {
            id++;
        }
        return Integer.toString(id);
    }

    public void displayCourseCards() {
        for (HashMap.Entry<String, FlashCard> FlashCard : courseCards.entrySet()) {
            System.out.println("Course: " + courseName + ", Card Key: " + FlashCard.getKey() + ", Card Value: " + FlashCard.getValue());
        }
    }

    public FlashCard deleteCard (String id) {
        if (!courseCards.containsKey(id)) {
            System.out.println("Error in deleting card: This card does not exist");
            return null;
        }
        FlashCard deletedCard = courseCards.remove(id);
        fileConverter.serializeCard(courseCards);
        return deletedCard;
    }

    public void deleteAllCard () {
        courseCards.clear();
        fileConverter.serializeCard(courseCards);
    }

    public boolean updateCard (String id, FlashCard newCard) {
        if (courseCards.containsKey(id) && isNewCardValid(newCard)) {
            newCard.setCardID(id);
            courseCards.put(id, newCard);
            fileConverter.serializeCard(courseCards);
            return true;
        }
        else
            System.out.println("Cannot update this card: This card does not exist");
        return false;
    }

    public FlashCard getCard (String id) {
        if (courseCards.containsKey(id))
            return courseCards.get(id);
        return null;
    }

    public List<FlashCard> getPlainCards() {
        List<FlashCard> cards = new ArrayList<>(courseCards.size());
        for (FlashCard cs : courseCards.values()) {
            cards.add(cs);
        }
        return cards;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public String getCourseID() {
        return this.courseID;
    }

    public FileConverter getFileConverter() {
        return fileConverter;
    }

    public void setCourseID(String id) {
        this.courseID = id;
    }

    public Map<String, FlashCard> getCourseCards() {
        return this.courseCards;
    }

    public String toString() {
        return "ID: " + courseID + ", Course Name: " + courseName;
    }
}