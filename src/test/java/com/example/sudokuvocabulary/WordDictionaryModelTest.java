package com.example.sudokuvocabulary;

import static org.junit.Assert.*;

import com.example.sudokuvocabulary.models.WordDictionaryModel;
import com.example.sudokuvocabulary.models.WordSampleModel;

import org.junit.Test;

import java.util.ArrayList;

public class WordDictionaryModelTest {

    String[][] words1 = {{"lamb", "beef", "chicken"}, {"羊肉", "牛肉", "鸡肉"}};
    String[][] words2 = {
            {"Apples", "Oranges", "Watermelon", "Grapes", "Fruits", "Banana",
                    "Dragon Eyes", "Mango", "Plum"},
            {"苹果", "橙子", "西瓜", "葡萄", "水果", "香蕉", "龙眼", "芒果", "李子"}
    };

    String[][][] testCases = {words1, words2};

    @Test
    public void getLength() {
        for (String[][] testCase: testCases) {
            WordDictionaryModel test = new WordDictionaryModel(testCase[0], testCase[1]);
            assertEquals(testCase[0].length, test.getLength());
        }
    }

    @Test
    public void getWords() {
        for (String[][] testCase : testCases) {
            WordDictionaryModel test = new WordDictionaryModel(testCase[0], testCase[1]);
            ArrayList<WordSampleModel> samples = test.getWords();
            for (int index = 0; index < testCase[0].length; index++) {
                assertEquals(testCase[0][index], samples.get(index).getWord());
                assertEquals(testCase[1][index], samples.get(index).getTranslation());
            }
        }
    }

    @Test
    public void setFromArrays() {
        for (String[][] testCase: testCases) {
            WordDictionaryModel test = new WordDictionaryModel();
            test.setFromArrays(testCase[0], testCase[1]);
            assertArrayEquals(testCase[0], test.getWordsAsArray());
            assertArrayEquals(testCase[1], test.getTranslationsAsArray());
        }
    }

    @Test
    public void getWordsAsArray() {
        for (String[][] testCase: testCases) {
            WordDictionaryModel test = new WordDictionaryModel(testCase[0], testCase[1]);
            assertArrayEquals(testCase[0], test.getWordsAsArray());
        }
    }

    @Test
    public void getTranslationsAsArray() {
        for (String[][] testCase: testCases) {
            WordDictionaryModel test = new WordDictionaryModel(testCase[0], testCase[1]);
            assertArrayEquals(testCase[1], test.getTranslationsAsArray());
        }
    }

    @Test
    public void getWord() {
        for (String[][] testCase: testCases) {
            WordDictionaryModel test = new WordDictionaryModel(testCase[0], testCase[1]);
            for (int index = 0; index < testCase[0].length; index++) {
                assertEquals(testCase[0][index], test.getWord(index));
            }
        }
    }

    @Test
    public void getTranslation() {
        for (String[][] testCase: testCases) {
            WordDictionaryModel test = new WordDictionaryModel(testCase[0], testCase[1]);
            for (int index = 0; index < testCase[1].length; index++) {
                assertEquals(testCase[1][index], test.getTranslation(index));
            }
        }
    }

    @Test
    public void add() {
        for (String[][] testCase: testCases) {
            WordDictionaryModel test = new WordDictionaryModel();
            for (int index = 0; index < testCase[0].length; index++) {
                test.add(testCase[0][index], testCase[1][index]);
            }
            assertArrayEquals(testCase[0], test.getWordsAsArray());
            assertArrayEquals(testCase[1], test.getTranslationsAsArray());
        }
    }

    @Test
    public void remove() {
        for (String[][] testCase: testCases) {
            WordDictionaryModel test = new WordDictionaryModel(testCase[0], testCase[1]);
            for (int index = 0; index < testCase[0].length; index++) {
                test.remove(testCase[0][index]);
                assertFalse(test.contains(testCase[0][index]));
            }
        }
    }

    @Test
    public void findSample() {
        for (String[][] testCase: testCases) {
            WordDictionaryModel test = new WordDictionaryModel(testCase[0], testCase[1]);
            for (int index = 0; index < testCase[0].length; index++) {
                WordSampleModel sample = test.findSample(testCase[0][index]);
                assertEquals(testCase[0][index], sample.getWord());
                assertEquals(testCase[1][index], sample.getTranslation());
            }
        }
    }

    @Test
    public void findTranslation() {
        for (String[][] testCase: testCases) {
            WordDictionaryModel test = new WordDictionaryModel(testCase[0], testCase[1]);
            for (int index = 0; index < testCase[0].length; index++) {
                String translation = test.findTranslation(testCase[0][index]);
                assertEquals(testCase[1][index], translation);
            }
        }
    }

    @Test
    public void contains() {
        for (String[][] testCase: testCases) {
            WordDictionaryModel test = new WordDictionaryModel(testCase[0], testCase[1]);
            for (String word: testCase[0]) {
                assertTrue(test.contains(word));
            }
            String notInTest = "bacon";
            assertFalse(test.contains(notInTest));
        }
    }
}