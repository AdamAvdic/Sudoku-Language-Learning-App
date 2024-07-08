package com.example.sudokuvocabulary.models;

public class WordSampleModel {
    private String word;
    private String translation;

    public WordSampleModel(String word, String translation) {
       setWord(word);
       setTranslation(translation);
    }

    public String getWord() {return word;}

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
