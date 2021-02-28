package com.example.myspace.data;

import android.os.Environment;
import android.util.Log;

import com.example.myspace.data.entity.Card;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InOut {

    private static InOut ourInstance = new InOut();

    public static InOut getInstance() {
        return ourInstance;
    }

    final String DIR_SD = "Download";
    final String FILENAME_SD = "fileSD.txt";

    private static final String TAG = "MainActivity";

    public List<Card> getCards() {
        // проверяем доступность SD
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d(TAG, "SD-карта не доступна: " + Environment.getExternalStorageState());
            //return;
        }

        // получаем путь к SD
        File sdPath = Environment.getExternalStorageDirectory();

        // добавляем свой каталог к пути
        sdPath = new File(sdPath.getAbsolutePath() + "/" + DIR_SD);

        // формируем объект File, который содержит путь к файлу
        File sdFile = new File(sdPath, FILENAME_SD);

        ArrayList<String> cardLines = new ArrayList<String>();
        try {
            // открываем поток для чтения
            BufferedReader br = new BufferedReader(new FileReader(sdFile));
            String str = "";
            // читаем содержимое
            while ((str = br.readLine()) != null) {
                cardLines.add(str);
                //Log.d(LOG_TAG, str);
            }
        } catch (FileNotFoundException e) {
            Log.d(TAG, "file was not found");
            e.printStackTrace();
        } catch (IOException e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            String er = errors.toString();
            Log.d(TAG, er);
        }

        Log.d(TAG, "lines: " + cardLines.size());

        // получаем карточки
        List<Card> cards = new ArrayList<>();
        String[] result;
        for (String thisLine : cardLines) {
            //Log.d("myLogs", thisLine);
            result = thisLine.split("/_");

            String st = (result[4].replace(" ", ""));

            cards.add(new Card(LocalDate.now(), result[1], result[2], result[3], Integer.parseInt(st.replace("\t", ""))));
        }
        for(Card tempCard : cards) {
            Log.d(TAG, tempCard.print());
        }

        return cards;
    }
}
