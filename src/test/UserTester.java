package test;

import flashcard.Course;
import flashcard.FlashCard;
import flashcard.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserTester {

    public static void main (String[] args) {
        User user = new User();
        //user.deleteAllCourse();
        //user.displayQuiz(testCreateQuiz(user));
        user.displayQuiz(temp(user));
        //testAddCourse(user);
        //user.displayUserCourses();
        //testDeleteCourse(user);
        //user.displayUserCourses();
        //testUpdateCourse(user);
        //user.displayUserCourses();
    }
    private static User testAddCourse (User user){
        System.out.println("\n---TEST ADD COURSE---\n");
        Course Politics = new Course ("Politics");
        Course ComputerScience = new Course ("Computer Science");
        System.out.println("Added 2 new courses");
        user.addCourse(Politics);
        user.addCourse(ComputerScience);
        FlashCard EU = new FlashCard("European Union", "a controversial organization");
        Politics.addCard(EU);
        Politics.displayCourseCards();
        return user;
    }
    private static User testDeleteCourse(User user) {
        System.out.println("\n---TEST DELETE COURSE---\n");
        Course deletedCourse = user.deleteCourse("1");
        System.out.println("Course deleted: " + deletedCourse);
        user.deleteCourse("40");
        return user;
    }

    private static User testUpdateCourse(User user) {
        System.out.println("\n---TEST UPDATE COURSE---\n");
        Course updated = new Course("Philosophy");
        user.updateCourse("0", updated);
        System.out.println("Course updated: " + updated);
        user.updateCourse("500", updated);
        return user;
    }

    private static Map<String,List<String>> testCreateQuiz(User user) {
        System.out.println("\n---TEST CREATE QUIZ---\n");
        Course newCourse = new Course("Political Science");
        FlashCard EU = new FlashCard("European Union", "a controversial organization");
        FlashCard EUCAP = new FlashCard("EU CAP", "Costs a lot of money");
        FlashCard EUFreeMovement = new FlashCard("EU free movement", "Travel-friendly");
        FlashCard EUFreeMovement2 = new FlashCard("EU free movement 2", "Low security");
        FlashCard EUHistory = new FlashCard("EU history", "a new org");
        FlashCard EUCountries = new FlashCard("EU countries", "27");
        FlashCard EUAndTurkey = new FlashCard("EU and Turkey", "Complicated");
        FlashCard EUTurkey = new FlashCard("Turkey", "Not a member");
        FlashCard EUGreeceInDebt = new FlashCard("Greece in debt", "EU has to help");
        FlashCard EUBrexit = new FlashCard("Brexit", "Unexpected");

        newCourse.addCard(EU);
        newCourse.addCard(EUCAP);
        newCourse.addCard(EUFreeMovement);
        newCourse.addCard(EUFreeMovement2);
        newCourse.addCard(EUHistory);
        newCourse.addCard(EUCountries);
        newCourse.addCard(EUAndTurkey);
        newCourse.addCard(EUTurkey);
        newCourse.addCard(EUGreeceInDebt);
        newCourse.addCard(EUBrexit);

        Map<String, List<String>> quiz = user.createQuiz(newCourse, 5);

        return quiz;
    }

    private static Map<String,List<String>> temp(User user) {
        Course newCourse = new Course("a");
        Map<String, List<String>> quiz = user.createQuiz(newCourse, 2);
        return quiz;
    }
}