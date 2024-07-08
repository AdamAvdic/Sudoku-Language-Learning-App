package com.example.sudokuvocabulary.models;

import java.util.ArrayList;

public class WordDictionaryModel {

    private int mId;
    private String mName;
    private ArrayList<WordSampleModel> mWords;

    public WordDictionaryModel(int id, String name) {
        mId = id;
        mName = name;
        mWords = new ArrayList<>();
    }

    public WordDictionaryModel(ArrayList<String> words, ArrayList<String> translations) {
        this(0, null);
        setFromArrays(words, translations);
    }

    public WordDictionaryModel(String[] words, String[] translations) {
        this(0, null);
        setFromArrays(words, translations);
    }

    public WordDictionaryModel() {
        this(0, null);
    }

    public int getLength() {
        return mWords.size();
    }

    public ArrayList<WordSampleModel> getWords() {
        return mWords;
    }

    public void setFromArrays(ArrayList<String> words, ArrayList<String> translations) {
        for (int index = 0; index < words.size(); index++) {
            add(words.get(index), translations.get(index));
        }
    }

    public void setFromArrays(String[] words, String[] translations) {
        for (int index = 0; index < words.length; index++) {
            add(words[index], translations[index]);
        }
    }

    public String[] getWordsAsArray() {
        String[] array = new String[getLength()];
        int index = 0;
        for (WordSampleModel sample: mWords) {
            array[index++] = sample.getWord();
        }
        return array;
    }

    public String[] getTranslationsAsArray() {
        String[] array = new String[getLength()];
        int index = 0;
        for (WordSampleModel sample: mWords) {
            array[index++] = sample.getTranslation();
        }
        return array;
    }

    public String getWord(int index) {
        WordSampleModel sample = mWords.get(index);
        return sample.getWord();
    }

    public String getTranslation(int index) {
        WordSampleModel sample = mWords.get(index);
        return sample.getTranslation();
    }

    public void add(WordSampleModel sample) {
        mWords.add(sample);
    }

    public void add(String word, String translation) {
        WordSampleModel sample = new WordSampleModel(word, translation);
        add(sample);
    }

    public void remove(WordSampleModel sample) {
        mWords.remove(sample);
    }

    public void remove(String word) {
        mWords.remove(findSample(word));
    }

    public WordSampleModel findSample(String word) {
        for (WordSampleModel sample: mWords) {
            if(sample.getWord().equals(word)) {
                return sample;
            }
        }
        return null;
    }

    public String findTranslation(String word) {
        WordSampleModel sample = findSample(word);
        if (sample == null) {
            return null;
        }
        return sample.getTranslation();
    }

    public boolean contains(String word) {
        return findSample(word) != null;
    }
}
