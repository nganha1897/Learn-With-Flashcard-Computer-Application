package flashcard;

import java.util.Map;

public interface FileConverter {
    public void serializeCourse (Map<String, Course> courseMap);

    public void serializeCard (Map<String, FlashCard> cardMap);

    public Map<String, Course> deserializeCourse();

    public Map<String, FlashCard> deserializeCard();

    public boolean doesFileExist();

    public void fileClose();
}

