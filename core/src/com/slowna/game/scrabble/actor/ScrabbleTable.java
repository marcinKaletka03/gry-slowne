package com.slowna.game.scrabble.actor;


import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.slowna.game.App;
import com.slowna.game.config.TableConfiguration;
import com.slowna.game.core.ScrabbleDictionary;
import com.slowna.game.pojo.*;
import com.slowna.game.scrabble.exeption.DirectionException;
import com.slowna.game.scrabble.exeption.ScrabbleGameException;
import com.slowna.game.scrabble.exeption.WordException;
import com.slowna.game.scrabble.extra.ChunkProcess;
import com.slowna.game.utill.CharUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.slowna.game.config.ScrabbleTableConfiguration.DEFAULT_FIELD_SIZE;
import static com.slowna.game.config.ScrableConfig.*;

/**
 * Main class used to create and manipulate game
 * Searching returns only propositions that do not affect neighbour words
 */

public class ScrabbleTable extends Table {

    private final TableConfiguration c;
    private final ScrabbleField[][] scrabbleFields;
    private final LinkedList<ScrabbleChar> lettersPool;
    private final ScrabbleDictionary scrabbleDictionary;
    private final List<ScrabbleField> lastAffectedFields = new LinkedList<>();
    private boolean firstWord = true;

    public ScrabbleTable(ScrabbleDictionary dictionary, TableConfiguration c) {
        setSize(SCREEN_WIDTH, SCREEN_WIDTH);
        setY((SCREEN_HEIGHT - SCREEN_WIDTH) / 2f);
        pad(SCREEN_WIDTH / 16f);
        top().left();
        this.c = c;
        this.scrabbleDictionary = dictionary;
        this.lettersPool = new LinkedList<>(scrabbleDictionary.getScrabbleCharsPoolForNewGame());
        Collections.shuffle(this.lettersPool);
        this.scrabbleFields = new ScrabbleField[this.c.getTableSize()][this.c.getTableSize()];
        for (int y = 0; y < scrabbleFields.length; y++) {
            for (int x = 0; x < scrabbleFields[y].length; x++) {
                ScrabbleField field = new ScrabbleField(x, y, this.c.resolveFieldBonus(x, y));
                this.scrabbleFields[x][y] = field;
                add(field).size(DEFAULT_FIELD_SIZE).expand();
            }
            row();
        }
    }

    public int getLettersLeft() {
        return lettersPool.size();
    }

    public LinkedList<ScrabbleChar> poolChars(int left) {
        int number = c.getLettersByPlayer() - left;
        if (number <= lettersPool.size()) {
            LinkedList<ScrabbleChar> chars = new LinkedList<>();
            for (int i = 0; i < number; i++) {
                chars.add(lettersPool.poll());
            }
            return chars;
        }
        return new LinkedList<>();
    }

    public LinkedList<ScrabbleChar> swapChars(Collection<ScrabbleChar> scrabbleChars) {
        if (scrabbleChars.size() > c.getLettersByPlayer()) {
            throw new ScrabbleGameException("Can't swap more than 7 letters");
        }
        scrabbleChars.forEach(ScrabbleChar::resetProp);
        lettersPool.addAll(scrabbleChars);
        Collections.shuffle(lettersPool);
        LinkedList<ScrabbleChar> result = new LinkedList<>();
        for (int i = 0; i < scrabbleChars.size(); i++) {
            result.add(lettersPool.poll());
        }
        return result;
    }

    public void returnChars(Collection<ScrabbleChar> scrabbleChars) {
        if (scrabbleChars.size() > c.getLettersByPlayer()) {
            throw new ScrabbleGameException("Can't swap more than 7 letters");
        }
        lettersPool.addAll(scrabbleChars);
        Collections.shuffle(lettersPool);
    }

    public ChunkProcess<String, ScrabbleWordProposition> getProcess(Position position, List<ScrabbleChar> letters, Direction direction, int maxLength) {
        int minLength = getLettersLeft() == 0 ? 2 : MIN_BOT_WORDS_LENGTH;
        return scrabbleDictionary.getSearchingProcess(getAllAvailableFieldsInLine(position, direction), letters, minLength, maxLength);
    }

    public Collection<String> getBestWordsToUse(int quantity, List<ScrabbleChar> letters, int maxLength) {
        int minLength = getLettersLeft() == 0 ? 2 : MIN_BOT_WORDS_LENGTH;
        return scrabbleDictionary.findTheBestWords(CharUtils.convertToCharArray(letters), quantity, minLength, maxLength);
    }

    public List<String> getAvailableWords(List<ScrabbleField> sortedFields, Direction direction) {
        if (direction == Direction.BOTH) {
            List<String> result = new LinkedList<>();
            String verticallyWord = toWordInLine(sortedFields, Direction.VERTICALLY);
            if (verticallyWord.length() > 1) {
                String pattern = verticallyWord.replaceAll(" ", ".");
                result.addAll(scrabbleDictionary.getAvailableWords(Pattern.compile(pattern), verticallyWord.length()));
            }
            String horizontallyWord = toWordInLine(sortedFields, Direction.HORIZONTALLY);
            if (horizontallyWord.length() > 1) {
                String pattern = horizontallyWord.replaceAll(" ", ".");
                result.addAll(scrabbleDictionary.getAvailableWords(Pattern.compile(pattern), horizontallyWord.length()));
            }
            return result;
        }
        String word = toWordInLine(sortedFields, direction);
        String pattern = word.replaceAll(" ", ".");
        return scrabbleDictionary.getAvailableWords(Pattern.compile(pattern), word.length());
    }

    public int putWord(List<ScrabbleField> sortedFields, Direction direction, String wordToPut, Runnable onFinish) {
        List<ScrabbleField> affectedFields = toFields(sortedFields, direction);
        char[] chars = wordToPut.toCharArray();
        for (int i = 0; i < affectedFields.size(); i++) {
            ScrabbleField field = affectedFields.get(i);
            ScrabbleChar scrabbleChar = field.getPropOn();
            if (scrabbleChar == null && field.getScrabbleCharOn() != null) {
                continue;
            }
            if (scrabbleChar.getLetter() == BLANK_LETTER) {
                scrabbleChar.setLetter(chars[i]);
            }
        }
        return putWord(sortedFields, direction, onFinish);
    }

    public void setBonusCaught(Iterable<ScrabbleField> fields) {
        for (ScrabbleField field : fields) {
            field.setBonusCaught(true);
        }
    }

    public int putWord(List<ScrabbleField> sortedFields, Direction direction, Runnable onFinish) {
        checkFieldsCorrect(sortedFields);
        LinkedList<WordProposition> props = new LinkedList<>();

        if (direction == Direction.BOTH) {
            List<ScrabbleField> vertically = toFields(sortedFields, Direction.VERTICALLY);
            if (vertically.size() > 1) {
                props.add(new WordProposition(vertically));
            }
            List<ScrabbleField> horizontally = toFields(sortedFields, Direction.HORIZONTALLY);
            if (horizontally.size() > 1) {
                props.add(new WordProposition(horizontally));
            }
        } else {
            props.add(new WordProposition(toFields(sortedFields, direction)));
            props.addAll(checkAffected(sortedFields, direction));
        }

        for (WordProposition wordProposition : props) {
            if (!scrabbleDictionary.containsWord(wordProposition.getWord())) {
                throw new WordException(wordProposition.getWord() + " - " + App.lang().word_not_found);
            }
        }
        int points = props.stream()
                .mapToInt(WordProposition::getPoints)
                .sum();
        refreshLastAffectedFields(sortedFields);
        setBonusCaught(sortedFields);
        handleApplyProps(props, 0, onFinish);
        return points;
    }

    private void refreshLastAffectedFields(List<ScrabbleField> sortedFields) {
        this.lastAffectedFields.clear();
        for (ScrabbleField field : sortedFields) {
            if (field.getPropOn() != null)
                this.lastAffectedFields.add(field);
        }
    }

    private void handleApplyProps(Queue<WordProposition> propositions, float delay, Runnable onFinish) {
        if (propositions.isEmpty()) {
            getStage().addAction(Actions.delay(delay + 1f, Actions.run(onFinish)));
            return;
        }
        WordProposition prop = propositions.poll();
        float nextDelay = prop.countDelay() + delay;
        prop.applyProps(() -> this.handleApplyProps(propositions, nextDelay, onFinish), delay);
    }

    private LinkedList<WordProposition> checkAffected(List<ScrabbleField> sortedFields, Direction direction) {
        Direction opposite = direction.getOpposite();
        LinkedList<WordProposition> result = new LinkedList<>();
        for (ScrabbleField field : sortedFields) {
            if (hasFilledNeighbours(field, opposite)) {
                List<ScrabbleField> fields = toFields(Collections.singletonList(field), opposite);
                result.add(new WordProposition(fields));
            }
        }
        return result;
    }

    public WordPropositionWrapper resolveProposition(ScrabbleWordProposition proposition) {
        Direction opositeDirection = proposition.getDirection().getOpposite();
        WordPropositionWrapper result = new WordPropositionWrapper(proposition);

        boolean anyNewChar = false;
        for (ScrabbleCharProposition prop : proposition.getScrabbleCharPropositions()) {
            ScrabbleField checkedField = scrabbleFields[prop.getX()][prop.getY()];
            ScrabbleChar charOn = checkedField.getScrabbleCharOn();
            char c = prop.getC();
            if (charOn == null) {
                anyNewChar = true;
            } else {
                if (charOn.getLetter() != c) {
                    return WordPropositionWrapper.EMPTY;
                }
            }
            if (checkedField.getScrabbleCharOn() == null && hasFilledNeighbours(checkedField, opositeDirection)) {
                ScrabbleWordProposition optional = createPropositionIfPossible(c, checkedField, opositeDirection);
                if (optional == ScrabbleWordProposition.FAIL_PROP) {
                    return WordPropositionWrapper.EMPTY;
                }
                result.addSideProposition(optional);

            }
        }
        if (!anyNewChar) {
            return WordPropositionWrapper.EMPTY;
        }
        return result;
    }

    private ScrabbleWordProposition createPropositionIfPossible(char c, ScrabbleField startField, Direction direction) {

        List<ScrabbleCharProposition> charPropositions = new LinkedList<>();

        int startIndex;
        ScrabbleField[] fields;

        if (direction == Direction.VERTICALLY) {
            fields = Arrays.copyOf(scrabbleFields[startField.getXPos()], scrabbleFields.length);
            ArrayUtils.reverse(fields);
            startIndex = scrabbleFields.length - startField.getYPos() - 1;
        } else {
            fields = getAllByYPos(startField.getYPos());
            startIndex = startField.getXPos();
        }
        int points = 0;
        for (int i = startIndex - 1; i >= 0; i--) {
            ScrabbleField field = fields[i];
            ScrabbleChar scrabbleChar = field.getScrabbleCharOn();
            if (scrabbleChar == null) {
                break;
            }
            points += field.getScrabbleFieldBonus().multiplyLetter(scrabbleChar.getPoints());
            charPropositions.add(new ScrabbleCharProposition(field.getXPos(), field.getYPos(), scrabbleChar.getLetter()));
        }

        points += startField.getScrabbleFieldBonus().multiplyLetter(scrabbleDictionary.getPointsForChar(c));
        charPropositions.add(new ScrabbleCharProposition(startField.getXPos(), startField.getYPos(), c));

        for (int i = startIndex + 1; i < scrabbleFields.length; i++) {
            ScrabbleField field = fields[i];
            ScrabbleChar scrabbleChar = field.getScrabbleCharOn();
            if (scrabbleChar == null) {
                break;
            }
            points += field.getScrabbleFieldBonus().multiplyLetter(scrabbleChar.getPoints());
            charPropositions.add(new ScrabbleCharProposition(field.getXPos(), field.getYPos(), scrabbleChar.getLetter()));
        }
        Collections.sort(charPropositions);
        ScrabbleWordProposition proposition = new ScrabbleWordProposition(charPropositions.toArray(new ScrabbleCharProposition[0]), points);
        String word = proposition.getWord();
        if (!scrabbleDictionary.containsWord(word)) {
            return ScrabbleWordProposition.FAIL_PROP;
        }
        return proposition;
    }


    private List<ScrabbleField> toFields(List<ScrabbleField> sortedFields, Direction direction) {
        if (sortedFields.size() < 1) {
            return Collections.emptyList();
        }
        ScrabbleField first = sortedFields.get(0);
        ScrabbleField last = sortedFields.get(sortedFields.size() - 1);
        List<ScrabbleField> result = new LinkedList<>();

        int startIndex;
        int finishIndex;
        ScrabbleField[] fields;
        if (direction == Direction.VERTICALLY) {
            fields = Arrays.copyOf(scrabbleFields[first.getXPos()], scrabbleFields.length);
            startIndex = first.getYPos();
            finishIndex = last.getYPos();
        } else {
            fields = getAllByYPos(first.getYPos());
            startIndex = first.getXPos();
            finishIndex = last.getXPos();
        }

        if (startIndex != 0) {
            for (int i = startIndex - 1; i >= 0; i--) {
                ScrabbleChar scrabbleChar = fields[i].getScrabbleCharOn();
                if (scrabbleChar == null) {
                    break;
                }
                result.add(fields[i]);
            }
        }
        for (int i = startIndex; i < scrabbleFields.length; i++) {
            ScrabbleField field = fields[i];
            ScrabbleChar scrabbleChar = field.getScrabbleCharOn();
            if (scrabbleChar == null) {
                scrabbleChar = field.getPropOn();
            }
            if (scrabbleChar == null) {
                if (i < finishIndex) {
                    throw new WordException(App.lang().breakesInWord);
                } else break;
            }
            result.add(fields[i]);
        }
        Collections.sort(result);
        return result;
    }


    private String toWordInLine(List<ScrabbleField> sortedFields, Direction direction) {
        Collection<ScrabbleField> fields = toFields(sortedFields, direction);
        StringBuilder stringBuilder = new StringBuilder();
        for (ScrabbleField field : fields) {
            ScrabbleChar charOn = field.getPropOn();
            if (charOn == null) {
                charOn = field.getScrabbleCharOn();
            }
            stringBuilder.append(charOn.getLetter());
        }
        return stringBuilder.toString();
    }

    private void checkFieldsCorrect(Iterable<ScrabbleField> affectedFields) {
        if (firstWord) {
            int center = scrabbleFields.length / 2;
            for (ScrabbleField field : affectedFields) {
                if (field.getXPos() == center && field.getYPos() == center) {
                    return;
                }
            }
            throw new WordException(App.lang().firstWord);
        }

        for (ScrabbleField field : affectedFields) {
            if (hasAnyFilledNeighbours(field)) {
                return;
            }
        }
        throw new WordException(App.lang().newWord);
    }

    public long countEmptyFieldsAround(ScrabbleField field) {
        int x = field.getXPos();
        int y = field.getYPos();
        return Stream.of(
                        isFieldEmpty(x - 1, y),
                        isFieldEmpty(x + 1, y),
                        isFieldEmpty(x, y + 1),
                        isFieldEmpty(x, y - 1)
                ).filter(t -> t)
                .count();

    }

    private boolean hasAnyFilledNeighbours(ScrabbleField field) {
        return hasFilledNeighbours(field, Direction.BOTH);
    }

    private boolean hasFilledNeighbours(ScrabbleField field, Direction direction) {
        int x = field.getXPos();
        int y = field.getYPos();
        switch (direction) {
            case VERTICALLY:
                return isFieldFiled(x, y + 1) ||
                        isFieldFiled(x, y - 1);
            case HORIZONTALLY:
                return isFieldFiled(x - 1, y) ||
                        isFieldFiled(x + 1, y);
            case BOTH:
                return isFieldFiled(x - 1, y) ||
                        isFieldFiled(x + 1, y) ||
                        isFieldFiled(x, y + 1) ||
                        isFieldFiled(x, y - 1);
            default:
                throw new DirectionException(direction + " is not recognized");
        }

    }

    private boolean isFieldEmpty(int x, int y) {
        if (x < 0 || x > scrabbleFields.length - 1) {
            return false;
        }
        if (y < 0 || y > scrabbleFields.length - 1) {
            return false;
        }
        return scrabbleFields[x][y].getScrabbleCharOn() == null;
    }

    private boolean isFieldFiled(int x, int y) {
        if (x < 0 || x > scrabbleFields.length - 1) {
            return false;
        }
        if (y < 0 || y > scrabbleFields.length - 1) {
            return false;
        }
        return scrabbleFields[x][y].getScrabbleCharOn() != null;
    }

    private ScrabbleField[] getAllAvailableFieldsInLine(Position position, Direction direction) {
        if (direction == Direction.HORIZONTALLY) {
            return splitIntoAvailableFields(getAllByYPos(position.y), position.x);
        } else return splitIntoAvailableFields(scrabbleFields[position.x], position.y);
    }


//    private List<ScrabbleField[]> getAllAvailableFieldsInLine(Position position) {
//        List<ScrabbleField[]> allPossibleFieldsArray = new ArrayList<>();
//        allPossibleFieldsArray.add(splitIntoAvailableFields(scrabbleFields[position.getX()], position.getY()));
//        allPossibleFieldsArray.add(splitIntoAvailableFields(getAllByYPos(position.getY()), position.getX()));
//        return allPossibleFieldsArray.stream()
//                .filter(s -> s.length > 0)
//                .collect(Collectors.toList());
//    }


    private ScrabbleField[] splitIntoAvailableFields(ScrabbleField[] line, int startIndex) {
        LinkedList<ScrabbleField> result = new LinkedList<>();

        boolean isBreak = false;
        for (int i = startIndex - 1; i >= 0; i--) {
            ScrabbleField current = line[i];
            if (current.getScrabbleCharOn() != null) {
                if (isBreak) {
                    result.removeLast();
                    break;
                }
                result.add(current);
                continue;
            }
            result.add(current);
            isBreak = true;
        }
        isBreak = false;
        for (int i = startIndex; i < line.length - 1; i++) {
            ScrabbleField current = line[i];
            if (current.getScrabbleCharOn() != null) {
                if (isBreak) {
                    result.removeLast();
                    break;
                }
                result.add(current);
                continue;
            }
            result.add(current);
            isBreak = true;
        }

        // returns only when is any gap to put char
        for (ScrabbleField field : result) {
            if (field.getScrabbleCharOn() == null) {
                Collections.sort(result);
                return result.toArray(new ScrabbleField[0]);
            }
        }
        return new ScrabbleField[0];

    }

    private ScrabbleField[] getAllByYPos(int yPos) {
        ScrabbleField[] fields = new ScrabbleField[scrabbleFields.length];
        for (int x = 0; x < fields.length; x++) {
            fields[x] = scrabbleFields[x][yPos];
        }
        return fields;
    }

    public ScrabbleField[][] getScrabbleFields() {
        return scrabbleFields;
    }

    public boolean isFirstWord() {
        return firstWord;
    }

    public void setFirstWord(boolean firstWord) {
        this.firstWord = firstWord;
    }

    public List<ScrabbleField> getLastAffectedFields() {
        return lastAffectedFields;
    }
}
