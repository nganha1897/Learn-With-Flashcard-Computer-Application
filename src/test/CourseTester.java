package test;

import flashcard.Course;
import flashcard.FlashCard;

public class CourseTester {
    public static void main(String[] args) {
        Course art = new Course("a");
        //art.deleteAllCard();
        //testAddCard(art);
        art.displayCourseCards();
        //testDeleteCard(art);
        //art.displayCourseCards();
        //testUpdateCard(art);
        //art.displayCourseCards();
        //testGetCard(art);
    }

    private static Course testAddCard(Course art) {
        System.out.println("\n---TEST ADD CARD---\n");
        FlashCard VanGogh = new FlashCard("Van Gogh","The Starry Night");
        FlashCard Caravaggio = new FlashCard("Caravaggio", "Judith Beheading Holofernes");
        FlashCard Monet = new FlashCard("Monet", "Water Lilies");
        FlashCard LeonardoDaVinci = new FlashCard("Leonardo da Vinci", "Mona Lisa's smile");
        FlashCard Duplicate = new FlashCard("Leonardo da Vinci","Mona Lisa's smile");
        System.out.println("Added 4 new flash cards");
        art.addCard(VanGogh);
        art.addCard(Caravaggio);
        art.addCard(Monet);
        art.addCard(LeonardoDaVinci);
        art.addCard(Duplicate);
        return art;
    }

    private static Course testDeleteCard(Course art) {
        System.out.println("\n---TEST DELETE CARD---\n");
        FlashCard deletedCard = art.deleteCard("1");
        System.out.println("Card deleted: " + deletedCard);
        art.deleteCard("50");
        return art;
    }

    private static Course testUpdateCard(Course art) {
        System.out.println("\n---TEST UPDATE CARD---\n");
        FlashCard modified = new FlashCard("Raphael", "Vatican Stanze");
        art.updateCard("2", modified);
        System.out.println("Card updated: " + modified);
        art.updateCard("50", modified);
        return art;
    }

    private static void testGetCard(Course art) {
        System.out.println("\n---TEST GET CARD---\n");
        FlashCard card = art.getCard("3");
        if (card != null)
            System.out.println("Retrieved card: " + card);
        else
            System.out.println("This card does not exist");
    }
}
