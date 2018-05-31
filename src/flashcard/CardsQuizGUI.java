package flashcard;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CardsQuizGUI extends Application {
    private User user = new User();
    private Button btnAddCourse = new Button("Add Course");
    private Button btnDeleteCourse = new Button ("Delete Course");
    private Button btnUpdateCourse = new Button ("Update Course");
    private ObservableList<Course> courseList = FXCollections.observableArrayList();
    private Button btnAddCard = new Button("Add Card");
    private Button btnDeleteCard = new Button ("Delete Card");
    private Button btnUpdateCard = new Button ("Update Card");
    private Button btnQuiz = new Button ("Quiz");
    private ObservableList<FlashCard> cardList = FXCollections.observableArrayList();

    public void start(Stage primaryStage) {
        SplitPane root = new SplitPane();
        Scene scene = new Scene(root, 800, 800);
        createPrimaryStage (primaryStage, scene);
        VBox left = new VBox();
        VBox right = new VBox();
        root.setDividerPosition(0,0.45);
        root.getItems().addAll(left,right);
        TableView<Course> tvCourse = createCourseTableView();
        TableView<FlashCard> tvCard = createCardTableView();
        HBox manageCourse = new HBox();
        HBox manageCard = new HBox();
        manageCourse.getChildren().addAll(btnAddCourse,btnDeleteCourse, btnUpdateCourse);
        manageCard.getChildren().addAll(btnAddCard, btnDeleteCard, btnUpdateCard, btnQuiz);
        left.getChildren().addAll(manageCourse, tvCourse);
        right.getChildren().addAll(manageCard,tvCard);

        user.getPlainCourses().forEach(course -> {
            courseList.add(course);
        });

        tvCourse.setRowFactory( tv -> {
            TableRow<Course> courseRow = new TableRow<>();
            courseRow.setOnMouseClicked(event -> {
                if (!courseRow.isEmpty()) {
                    courseRow.getItem().readCardFile(courseRow.getItem().getCourseName());
                    cardList.clear();
                    courseRow.getItem().getPlainCards().forEach(card -> {
                        cardList.add(card);
                    });
                }
            });
            return courseRow ;
        });

        setOnActionForButtons(tvCard, tvCourse);
    }

    private void setOnActionForButtons(TableView<FlashCard> tvCard, TableView<Course> tvCourse) {
        btnAddCourse.setOnAction(event -> {
            addCourseAction();
        });
        btnDeleteCourse.setOnAction(event -> {
            deleteCourseAction(tvCourse);
        });
        btnUpdateCourse.setOnAction(event -> {
            updateCourseAction(tvCourse);
        });
        btnAddCard.setOnAction(event -> {
            addCardAction(tvCourse);
        });
        btnDeleteCard.setOnAction(event -> {
            deleteCardAction(tvCard,tvCourse);
        });
        btnUpdateCard.setOnAction(event -> {
            updateCardAction(tvCard,tvCourse);
        });

        btnQuiz.setOnAction(event -> {
            createQuizAction(tvCourse);
        });
    }

    private TableView<Course> createCourseTableView() {
        TableView<Course> tvCourse = new TableView<>(courseList);
        TableColumn<Course, String> courseColumn = new TableColumn<>("Course");
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        tvCourse.getColumns().addAll(courseColumn);
        tvCourse.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tvCourse.setPrefWidth(300);
        tvCourse.setPrefHeight(800);
        return tvCourse;
    }

    private TableView<FlashCard> createCardTableView() {
        TableView<FlashCard> tvCard = new TableView<>(cardList);
        TableColumn<FlashCard, String> cardFrontColumn = new TableColumn<>("Front");
        TableColumn<FlashCard, String> cardBackColumn = new TableColumn<>("Back");
        cardFrontColumn.setCellValueFactory(new PropertyValueFactory<>("front"));
        cardBackColumn.setCellValueFactory(new PropertyValueFactory<>("back"));
        tvCard.getColumns().addAll(cardFrontColumn, cardBackColumn);
        tvCard.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tvCard.setPrefWidth(500);
        tvCard.setPrefHeight(800);
        return tvCard;
    }

    private void addCourseAction () {
        Pane addCoursePane = new Pane();
        Stage addCourseStage = createNewWindow(addCoursePane, 400, 200);
        Label addCourseName = new Label("Set course name: ");
        TextField courseTextField = new TextField();
        Button enter = new Button ("Enter");
        enter.setOnAction(event -> {
            Course newCourse = new Course(courseTextField.getText());
            if (user.addCourse(newCourse))
                courseList.add(newCourse);
            else
                createErrorAlert("add","Course name cannot be blank or duplicated!");
            courseTextField.setText("");
        });
        VBox addCourse = new VBox ();
        addCourse.getChildren().addAll(addCourseName,courseTextField,enter);
        addCoursePane.getChildren().addAll(addCourse);
    }

    private void deleteCourseAction (TableView<Course> tvCourse) {
        Course deletedItem = tvCourse.getSelectionModel().getSelectedItem();
        if (deletedItem != null) {
            if (alertMessage("Are you sure you want to delete this item?")) {
                user.deleteCourse(deletedItem.getCourseID());
                tvCourse.getItems().remove(deletedItem);
                cardList.clear();
            }
        } else {
            createErrorAlert("delete", "Please choose a course you want to delete.");
        }
        tvCourse.getSelectionModel().clearSelection();
    }

    private void updateCourseAction(TableView<Course> tvCourse) {
        Course updatedCourse = tvCourse.getSelectionModel().getSelectedItem();
        if (updatedCourse != null) {
            Pane updateCoursePane = new Pane();
            Stage updateCourseStage = createNewWindow(updateCoursePane,400,200);
            Label updateCourseName = new Label("Update course name: ");
            TextField courseTextField = new TextField();
            Button enter = new Button ("Enter");
            enter.setOnAction(event -> {
                Course newCourse = new Course(courseTextField.getText());
                if (user.updateCourse(updatedCourse.getCourseID(),newCourse)) {
                    tvCourse.getItems().set(courseList.indexOf(updatedCourse), newCourse);
                    tvCourse.getSelectionModel().clearSelection();
                    updateCourseStage.close();
                } else
                    createErrorAlert("update", "The new course is either blank or duplicated");
                courseTextField.setText("");
            });
            VBox updateCourse = new VBox ();
            updateCourse.getChildren().addAll(updateCourseName,courseTextField,enter);
            updateCoursePane.getChildren().addAll(updateCourse);
        }
        else
            createErrorAlert("modify", "Please choose a course you want to modify.");
    }

    private Stage createNewWindow(Pane pane, double width, double height) {
        Stage newStage = new Stage();
        newStage.setScene(new Scene(pane, width,height));
        newStage.show();
        return newStage;
    }

    private void addCardAction (TableView<Course> tvCourse) {
        Course chosenCourse = tvCourse.getSelectionModel().getSelectedItem();
        if (chosenCourse != null) {
            chosenCourse.readCardFile(chosenCourse.getCourseName());
            Pane addCardPane = new Pane();
            Stage addCardStage = createNewWindow(addCardPane, 400, 200);
            Label addCardFront = new Label("Set card front: ");
            TextField cardFrontTextField = new TextField();
            Label addCardBack = new Label("Set card back: ");
            TextField cardBackTextField = new TextField();
            Button enter = new Button("Enter");
            enter.setOnAction(event -> {
                FlashCard newCard = new FlashCard(cardFrontTextField.getText(), cardBackTextField.getText());
                System.out.println(newCard.toString());
                if (chosenCourse.addCard(newCard))
                    cardList.add(newCard);
                else
                    createErrorAlert("add", "Please set both front and back!");
                cardBackTextField.setText("");
                cardFrontTextField.setText("");
            });
            VBox addCard = new VBox();
            addCard.getChildren().addAll(addCardFront, cardFrontTextField, addCardBack, cardBackTextField, enter);
            addCardPane.getChildren().addAll(addCard);
        }
        else
            createErrorAlert("add", "Please choose a course you want to add flashcard to.");
    }

    private void deleteCardAction (TableView<FlashCard> tvCard, TableView<Course> tvCourse) {
        FlashCard deletedItem = tvCard.getSelectionModel().getSelectedItem();
        Course chosenCourse = tvCourse.getSelectionModel().getSelectedItem();
        if (deletedItem != null) {
            chosenCourse.readCardFile(chosenCourse.getCourseName());
            if (alertMessage("Are you sure you want to delete this item?")) {
                chosenCourse.deleteCard(deletedItem.getCardID());
                tvCard.getItems().remove(deletedItem);
            }
        } else {
            createErrorAlert("delete", "Please choose a card you want to delete.");
        }
        tvCard.getSelectionModel().clearSelection();
    }

    private void updateCardAction(TableView<FlashCard> tvCard, TableView<Course> tvCourse) {
        FlashCard updatedCard = tvCard.getSelectionModel().getSelectedItem();
        Course chosenCourse = tvCourse.getSelectionModel().getSelectedItem();
        if (updatedCard != null) {
            chosenCourse.readCardFile(chosenCourse.getCourseName());
            Pane updateCardPane = new Pane();
            Stage updateCardStage = createNewWindow(updateCardPane, 400, 200);
            Label updateCardFront = new Label("Update card front: ");
            TextField cardFrontTextField = new TextField();
            Label updateCardBack = new Label("Update card back: ");
            TextField cardBackTextField = new TextField();
            Button enter = new Button ("Enter");
            enter.setOnAction(event -> {
                FlashCard newCard = new FlashCard(cardFrontTextField.getText(), cardBackTextField.getText());
                if (chosenCourse.updateCard(updatedCard.getCardID(),newCard)) {
                    tvCard.getItems().set(cardList.indexOf(updatedCard), newCard);
                    tvCard.getSelectionModel().clearSelection();
                    updateCardStage.close();
                } else
                    createErrorAlert("update", "Please do not leave either front or back blank");
                cardFrontTextField.setText("");
                cardBackTextField.setText("");
            });
            VBox updateCard = new VBox ();
            updateCard.getChildren().addAll(updateCardFront,cardFrontTextField, updateCardBack, cardBackTextField, enter);
            updateCardPane.getChildren().addAll(updateCard);
        }
        else
            createErrorAlert("modify", "Please choose a card you want to modify.");
    }

    private void createQuizAction(TableView<Course> tvCourse) {
        Course chosenCourse = tvCourse.getSelectionModel().getSelectedItem();
        if (chosenCourse != null) {
            chosenCourse.readCardFile(chosenCourse.getCourseName());
            Pane quizNumPane = new Pane();
            Stage quizNumStage = createNewWindow(quizNumPane, 500, 500);
            Label quizNum = new Label("Number of questions you want: ");
            TextField quizNumTextField = new TextField();
            quizNumTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue,
                                                         String newValue) -> {
                if (!newValue.matches("\\d*")) {
                    quizNumTextField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            });
            Button enter = new Button("Enter");
            enter.setOnAction(event -> {
                int quizLength = 0;
                String quizLengthInput = quizNumTextField.getText();
                try {
                    quizLength = Integer.parseInt(quizLengthInput);
                } catch (NumberFormatException e) {
                    Logger.getLogger(CardsQuizGUI.class.getName()).log(Level.SEVERE, "Error parsing number", e);
                }
                quizUserFirstReaction(chosenCourse, quizLength);
                quizNumStage.close();
            });
            VBox quiz = new VBox ();
            quiz.getChildren().addAll(quizNum, quizNumTextField ,enter);
            quizNumPane.getChildren().addAll(quiz);
        }
        else
            createErrorAlert("create quiz", "Please choose a course you want to test.");
    }

    private void quizUserFirstReaction(Course chosenCourse, int quizLength) {
        Map<String, List<String>> quiz = user.createQuiz(chosenCourse, quizLength);
        if (quiz != null) {
            RadioButton[] btnAnswer = new RadioButton[4];
            Pane quizPane = new Pane();
            createNewWindow(quizPane, 500, 500);
            Label question = new Label();
            Label check = new Label();
            Button submit = new Button("Submit");
            VBox quizBox = new VBox();
            ToggleGroup group = new ToggleGroup();
            Iterator<Map.Entry<String, List<String>>> quizEntry = quiz.entrySet().iterator();
            Map.Entry<String, List<String>> nextEntry = quizEntry.next();
            question.setText(chosenCourse.getCard(nextEntry.getKey()).getFront());
            for (int i = 0; i < nextEntry.getValue().size() - 1; i++) {

                RadioButton button = new RadioButton(chosenCourse.getCard(nextEntry.getValue().get(i)).getBack());
                btnAnswer[i] = button;
                button.setToggleGroup(group);
                button.setOnAction(answerClicked -> {
                    if (button.getText().equals(correctAnswer(nextEntry,chosenCourse)))
                        check.setText("Correct!");
                    else
                        check.setText("Wrong!");
                });
            }
            submit.setOnAction (event -> {
                check.setText("");
                if (quizEntry.hasNext()) {
                    Map.Entry<String, List<String>> entry = quizEntry.next();
                    question.setText(chosenCourse.getCard(entry.getKey()).getFront());
                    for (int j = 0; j < entry.getValue().size()-1; j++) {
                        RadioButton button = new RadioButton(chosenCourse.getCard(entry.getValue().get(j)).getBack());
                        quizBox.getChildren().set(quizBox.getChildren().indexOf(btnAnswer[j]), button);
                        btnAnswer[j] = button;
                        button.setToggleGroup(group);
                        button.setOnAction(answerClicked -> {
                            if (button.getText().equals(correctAnswer(entry,chosenCourse)))
                                check.setText("Correct!");
                            else
                                check.setText("Wrong!");
                        });
                    }
                }
                else {
                    alertMessage("That's the end of the quiz!");
                }
            });
            quizBox.getChildren().add(question);
            for (int i = 0; i < 4; i++) {
                if (btnAnswer[i] != null)
                    quizBox.getChildren().add(btnAnswer[i]);
            }
            quizBox.getChildren().addAll(check, submit);
            quizPane.getChildren().addAll(quizBox);
        } else {
            createErrorAlert("create a quiz!", "The number of questions you want is too small or too big");
        }
    }

    private String correctAnswer(Map.Entry<String, List<String>> quizEntry, Course course) {
        String key = quizEntry.getValue().get(quizEntry.getValue().size() - 1);
        String answer = course.getCard(key).getBack();
        return answer;
    }

    private boolean alertMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        Optional<ButtonType> action = alert.showAndWait();
        return action.isPresent() && action.get() == ButtonType.OK;
    }

    private void createErrorAlert(String message, String cause) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Fail to " + message + "! " + cause);
        alert.showAndWait();
    }

    private void createPrimaryStage(Stage primaryStage, Scene scene) {
        primaryStage.setTitle("Flashcard");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
