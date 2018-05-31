package test;

import flashcard.FlashCard;

public class FlashCardTester {
    public static void main (String[] args) {
        System.out.println("Let's test the flashcard.FlashCard class");
        FlashCard fc1 = new FlashCard("Us Goverment", "Donald Trump is the president");
        System.out.println(fc1);
        FlashCard fc2 = new FlashCard("Piano lesson", "Vivaldi");
        System.out.println(fc2);
        fc1.isFieldEmpty("abc");
    }
}