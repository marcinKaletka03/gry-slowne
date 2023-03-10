package com.slowna.game.core;


import com.slowna.game.config.ScrableConfig;
import com.slowna.game.pojo.*;
import com.slowna.game.scrabble.extra.ChunkProcess;
import com.slowna.game.utill.CharUtils;
import com.slowna.game.utill.PatternUtils;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * Simple Dictionary Implementation based on Patterns to find the best results
 */

public class ScrabbleDictionaryImpl implements ScrabbleDictionary {

    private final WordsProvider provider;
    private final TreeMap<Integer, Map<String, ScrabbleWord>> sortedByPointsWords;
    private final Map<String, Matcher> patternMap = new HashMap<>();

    public ScrabbleDictionaryImpl(WordsProvider provider) {
        this(provider, ScrableConfig.DEFAULT_TABLE_SIZE);
    }

    public ScrabbleDictionaryImpl(WordsProvider provider, int tableSize) {
        this.provider = provider;
        List<ScrabbleWord> words = this.provider.getEnabledWords(tableSize).stream()
                .map(ScrabbleWord::new)
                .collect(Collectors.toList());
        this.sortedByPointsWords = sortWordsByLength(words);
    }


    @Override
    public ChunkProcess<String, ScrabbleWordProposition> getSearchingProcess(ScrabbleField[] availableFieldsInLine, List<ScrabbleChar> playerChars, int minWordLength, int maxWordLength) {
        List<ScrabbleChar> allChars = new LinkedList<>(playerChars);
        addAllCharsToList(allChars, availableFieldsInLine);
        char[] allAvailableChars = CharUtils.convertToCharArray(allChars);

        if (availableFieldsInLine.length == 0) {
            return ChunkProcess.getEmpty();
        }
        Direction direction = Direction.recognizeDirection(availableFieldsInLine);


        List<String> possibleWords = findTheBestWords(allAvailableChars, -1, minWordLength, maxWordLength);
        if (possibleWords.isEmpty()) {
            return ChunkProcess.getEmpty();
        }

        String patternString = PatternUtils.createPattern(availableFieldsInLine);
        Matcher matcher = patternMap.computeIfAbsent(patternString, s -> Pattern.compile(s).matcher(""));
        return new ChunkProcess<>(possibleWords, s -> matcher.reset(s).find(), s -> toProposition(availableFieldsInLine, direction, s));
    }

    @Override
    //todo optimize
    public List<String> findTheBestWords(char[] chars, int maxQuantity, int minLength, int maxLength) {
        int availableBlankLetters = CharUtils.countLettersNumberInArray(chars, ScrableConfig.BLANK_LETTER);
        char[] sortedCharsWithoutBlanks = CharUtils.sortCharsAndRemoveBlanks(chars, availableBlankLetters);

        int lettersNumber = chars.length;
        List<String> result = new LinkedList<>();

        main:
        for (int i = Math.min(lettersNumber, maxLength); i >= minLength; i--) {
            Map<String, ScrabbleWord> words = sortedByPointsWords.get(i);
            if (words == null) {
                continue;
            }
            for (Map.Entry<String, ScrabbleWord> w : words.entrySet()) {
                if (!CharUtils.canBeBuildScrableWordFrom(w.getValue(), sortedCharsWithoutBlanks, availableBlankLetters)) {
                    continue;
                }
                result.add(w.getKey());
                if (result.size() == maxQuantity) {
                    break main;
                }
            }
        }
        return new ArrayList<>(result);
    }

    private ScrabbleWordProposition toProposition(ScrabbleField[] fields, Direction direction, String word) {
        int startIndex = CharUtils.findStartIndex(word, fields);
        if (startIndex == -1) {
            return ScrabbleWordProposition.EMPTY_PROPOSITION;
        }
        ScrabbleField startField = fields[startIndex];
        int startX = startField.getXPos();
        int startY = startField.getYPos();
        int pointsForWord = countPoints(fields, word, startIndex);
        int length = word.length();
        char[] charArray = word.toCharArray();
        ScrabbleCharProposition[] propositions = new ScrabbleCharProposition[length];
        switch (direction) {
            case VERTICALLY:
                for (int i = 0; i < length; i++) {
                    char c = charArray[i];
                    propositions[i] = new ScrabbleCharProposition(startX, startY + i, c);
                }
                break;
            case HORIZONTALLY:
                for (int i = 0; i < length; i++) {
                    char c = charArray[i];
                    propositions[i] = new ScrabbleCharProposition(startX + i, startY, c);
                }
                break;
        }
        return new ScrabbleWordProposition(propositions, pointsForWord);
    }

    private int countPoints(ScrabbleField[] fields, String word, int startIndex) {
        List<ScrabbleFieldBonus> wordBonuses = new LinkedList<>();
        int points = 0;
        int currentLetter = 0;
        for (int i = startIndex; i < startIndex + word.length(); i++) {
            ScrabbleField current = fields[i];
            ScrabbleFieldBonus bonus = current.getScrabbleFieldBonus();
            int charPoints = provider.getBasicPointsForChar(word.charAt(currentLetter));
            if (!current.isBonusCaught()) {
                if (bonus.isWordBonus) {
                    wordBonuses.add(bonus);
                } else {
                    charPoints = bonus.multiplyLetter(charPoints);
                }
            }
            points += charPoints;
            currentLetter++;
        }
        for (ScrabbleFieldBonus bonus : wordBonuses) {
            points = bonus.multiplyWord(points);
        }
        return points;
    }

    @Override
    public List<ScrabbleChar> getScrabbleCharsPoolForNewGame() {
        return provider.getLettersPool();
    }

    @Override
    public boolean containsWord(String word) {
        int length = word.length();
        Map<String, ScrabbleWord> words = sortedByPointsWords.get(length);
        return words != null && words.containsKey(word);
    }

    @Override
    public List<String> getAvailableWords(Pattern pattern, int length) {
        Map<String, ScrabbleWord> words = sortedByPointsWords.get(length);
        if (words == null) {
            return Collections.emptyList();
        }
        return words.keySet().stream()
                .filter(word -> pattern.matcher(word).matches())
                .collect(Collectors.toList());
    }

    @Override
    public void saveNewWord(String word) {
        String result = word.trim();
        if (!containsWord(word)) {
            provider.saveNewWord(result);
//            sortedByPointsWords.get(word.length()).add(new ScrabbleWord(result));
        }
    }

    @Override
    public int getPointsForChar(char c) {
        return provider.getBasicPointsForChar(c);
    }

    private void addAllCharsToList(List<ScrabbleChar> list, ScrabbleField[] fields) {
        for (ScrabbleField field : fields) {
            if (field.getScrabbleCharOn() != null) {
                list.add(field.getScrabbleCharOn());
            }
        }
    }

    /**
     * @param words all enabled words
     * @return sorted map with keys sorted by length related to the word
     */
    private TreeMap<Integer, Map<String, ScrabbleWord>> sortWordsByLength(List<ScrabbleWord> words) {
        return words.stream()
                .collect(Collectors.groupingBy(ScrabbleWord::getLength,
                        () -> new TreeMap<>((o1, o2) -> -Integer.compare(o1, o2)),
                        Collectors.toMap(ScrabbleWord::getWord, Function.identity(), (t1, t2) -> t1)));
    }
}
