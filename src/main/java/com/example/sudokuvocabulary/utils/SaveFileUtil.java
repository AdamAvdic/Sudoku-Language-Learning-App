package com.example.sudokuvocabulary.utils;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SaveFileUtil {

    public enum SaveObjects {
        GAME_MODE,
        SUDOKU_MODEL,
        SUDOKU_GRID,
        TIME,
        WORDS,
        TRANSLATIONS,
    }

    /**
     * Writes the given array to the specified save file.
     *
     * @param context The calling activity's context.
     * @param objects An array containing the objects to write to the file.
     * @param fileName The name of the save file.
     */
    public static void writeToSave(Context context, Serializable[] objects, String fileName) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
            outputStream.writeObject(objects);
            outputStream.close();
            fileOutputStream.close();
            Log.d(context.getClass().getSimpleName(), "Object(s) have been saved to: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context The calling activity's context.
     * @param fileName The name of the save file.
     * @return An array containing all the objects in the save file
     */
    public static Object[] readAllFromSave(Context context, String fileName) {
        Object[] objects;
        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
            ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);
            objects = (Object[]) inputStream.readObject();
            inputStream.close();
            fileInputStream.close();
        }catch (ClassNotFoundException e) {
            Log.d(context.getClass().getSimpleName(), "Class Not Found in File!");
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return objects;
    }

    /**
     * @param context The calling activity's context.
     * @param fileName The name of the file.
     * @param object The object type to return.
     * @return The specified object in the save file.
     */
    public static Object readFromSave(Context context, String fileName, SaveObjects object) {
        Object[] objects = readAllFromSave(context, fileName);
        return objects[object.ordinal()];
    }

    public static boolean saveExists(Context context, String fileName) {
        return context.getFileStreamPath(fileName).exists();
    }

    public static void deleteSave(Context context, String fileName) {
        context.deleteFile(fileName);
    }
}
