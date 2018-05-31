package flashcard;

import java.io.Serializable;

public class FlashCard implements Serializable {
    private String cardID = "0";
    private String front;
    private String back;
    private static final long serialVersionUID = 1L;

    public FlashCard(String front, String back) {
        this.front = front;
        this.back = back;
    }

    public FlashCard(String front, String back, String cardID) {
        this.front = front;
        this.back = back;
        this.cardID = cardID;
    }

    public String getFront() {
        return this.front;
    }

    public String getBack() {
        return this.back;
    }

    public String getCardID() {
        return this.cardID;
    }

    public void setCardID (String cardID) {
        this.cardID = cardID;
    }

    public boolean isInvalid() {
        return isFieldEmpty(cardID) || isFieldEmpty(front) || isFieldEmpty(back);
    }

    public boolean isFieldEmpty (String field) {
        return field == null || field.trim().isEmpty();
    }
    public String toString() {
        return "Id: " + cardID + ", front: " + front + ", back: " + back;
    }
}