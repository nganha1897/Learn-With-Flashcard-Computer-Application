
package flashcard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class User {

    private static final String COURSE_FILE_NAME = "src/resources/course.txt";
    private FileConverter fileConverter;
    private Map<String, Course> courseList = new HashMap<>();

    public User() {
        fileConverter = new BasicFileConverter(COURSE_FILE_NAME);
        try {
            BufferedReader br = new BufferedReader(new FileReader(COURSE_FILE_NAME));
            if (fileConverter.doesFileExist() && br.readLine() != null) {
                Map<String, Course> courseMap = fileConverter.deserializeCourse();
                courseList = courseMap == null ? new HashMap<>() : courseMap;
            }
        } catch (IOException e) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, "Error reading file", e);
        }
    }

    public boolean addCourse(Course course) {
        if  (!isNewCourseValid(course)) {
            course.getFileConverter().fileClose();
            return false;
        }
        String courseID = generateCourseID();
        course.setCourseID(courseID);
        courseList.put(courseID, course);
        fileConverter.serializeCourse(courseList);
        return true;
    }

    public Course deleteCourse(String id) {
        if (!courseList.containsKey(id)) {
            System.out.println("Error in deleting course: This course does not exist");
            return null;
        }
        Course deletedCourse = courseList.remove(id);
        deleteTextFile("src/resources/"+ deletedCourse.getCourseName() + ".txt");
        fileConverter.serializeCourse(courseList);
        return deletedCourse;
    }

    public void deleteTextFile(String fileName) {
        File file = new File(fileName);
        if (file != null)
            file.delete();
    }
    public void deleteAllCourse () {
        for (Map.Entry<String,Course> course : courseList.entrySet()) {
            String fileName = "src/resources/" + course.getValue().getCourseName() + ".txt";
            deleteTextFile(fileName);
        }
        courseList.clear();
        fileConverter.serializeCourse(courseList);
        File fileCourse = new File("src/resources/course.txt");
        fileCourse.delete();
    }

    public boolean updateCourse(String id, Course newCourse) {
        if (courseList.containsKey(id) && isNewCourseValid(newCourse) ) {
            File oldFile = new File ("src/resources/"+courseList.get(id).getCourseName()+".txt");
            File newFile = new File ("src/resources/"+newCourse.getCourseName()+".txt");
            oldFile.renameTo(newFile);
            newCourse.setCourseID(id);
            courseList.put(id, newCourse);
            fileConverter.serializeCourse(courseList);
            return true;
        }
        else {
            newCourse.getFileConverter().fileClose();
            System.out.println("Cannot update this course. ");
        }
        return false;
    }

    private boolean isNewCourseValid(Course newCourse) {
        for (Course course : courseList.values()) {
            if (newCourse == null || newCourse.getCourseName().trim().isEmpty() || course.getCourseName().trim().equals(newCourse.getCourseName().trim())) {
                return false;
            }
        }
        return true;
    }

    public Map<String, List<String>> createQuiz (Course course, int questionNum) {
        Map<String, List<String>> quiz = new HashMap<>();
        int answerNum = 0;
        int defaultChoice = 4;
        int count = 0;
        if (questionNum > course.getCourseCards().size()) {
            System.out.println("The number of questions you want is more than the course size!");
            return null;
        } else if (questionNum > 0) {
            if (course.getCourseCards().size() <4 && course.getCourseCards().size() > 1)
                defaultChoice = course.getCourseCards().size();
            else if (course.getCourseCards().size() <= 1) {
                System.out.println("The number of flashcards has to be at least 2 to create a quiz!");
                return null;
            }
            Random random = new Random();
            while (count < questionNum) {
                List<String> answers = new ArrayList<>();
                List<String> IDs      = new ArrayList<String>(course.getCourseCards().keySet());
                String       randomID = IDs.get(random.nextInt(course.getCourseCards().keySet().size()));
                if (!quiz.containsKey(randomID)) {
                    count++;
                    while (answerNum < defaultChoice-1) {
                        String randomAnsID = IDs.get(random.nextInt(course.getCourseCards().keySet().size()));
                        if (!randomAnsID.equals(randomID) && !answers.contains(randomAnsID)) {
                            answers.add(randomAnsID);
                            answerNum++;
                        }
                    }
                    answers.add(randomID);
                    int index = random.nextInt(answers.size());
                    String temp = answers.get(index);
                    answers.set(answers.indexOf(randomID),temp);
                    answers.set(index, randomID);
                    answers.add(randomID);
                    answerNum = 0;
                    quiz.put(randomID, answers);
                }
            }
        } else {
            System.out.println("The number of questions should be larger than 0");
            return null;
        }
        return quiz;
    }

    private String generateCourseID() {
        Random random = new Random();
        int id = random.nextInt(courseList.size() + 1);
        while (courseList.containsKey(Integer.toString(id))) {
            id++;
        }
        return Integer.toString(id);
    }

    public void displayUserCourses() {
        for (HashMap.Entry<String, Course> course : courseList.entrySet())
            System.out.println("Course Key: " + course.getKey() + ", Course Value: " + course.getValue());
    }

    public void displayQuiz(Map<String, List<String>> quiz) {
        for (HashMap.Entry<String, List<String>> entry : quiz.entrySet())
            System.out.println("Entry key: " + entry.getKey() + ", Entry Value: " + entry.getValue());
    }

    public List<Course> getPlainCourses() {
        List<Course> courses = new ArrayList<>(courseList.size());
        for (Course cs : courseList.values()) {
            courses.add(cs);
        }
        return courses;
    }

}
