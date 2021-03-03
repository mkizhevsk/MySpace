package com.example.myspace;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myspace.data.BaseService;
import com.example.myspace.data.entity.Card;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class CardActivity extends AppCompatActivity {

    BaseService baseService;

    LinearLayout frontBackExampleLayout;
    TextView firstTextView;
    TextView secondTextView;

    private boolean front = false;

    private List<Card> cards;
    private Card currentCard;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        this.setTitle("Cards");

        frontBackExampleLayout = (LinearLayout) findViewById(R.id.frontBackExampleLayout);
        firstTextView = (TextView)findViewById(R.id.frontBackTextView);
        secondTextView = (TextView)findViewById(R.id.exampleTextView);

        Intent intent = new Intent(this, BaseService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BaseService.LocalBinder binder = (BaseService.LocalBinder) service;
            baseService = binder.getService();

            cards = baseService.getCardsByStatus(0);
            Log.d(TAG, "unlearned cards: " + cards.size());
            Collections.shuffle(cards);
//            for(Card tempCard : cards) {
//                Log.d(TAG, tempCard.print());
//            }

            currentCard = cards.get(0);
            rotateAndShowCard();

            frontBackExampleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rotateAndShowCard();
                }
            });

            Log.d(TAG, "CardActivity  onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "CardActivity  onServiceDisconnected");
        }
    };

    private void rotateAndShowCard() {
        if(front) {
//            Log.d(TAG, "show back");
            firstTextView.setText(currentCard.getBack());
            secondTextView.setText(currentCard.getExample());
            front = false;
        } else {
//            Log.d(TAG, "show front");
            firstTextView.setText(currentCard.getFront());
            secondTextView.setText("");
            front = true;
        }
    }

    public void doOk(View view) {
        currentCard.setStatus(1);
        baseService.updateCard(currentCard);

        showNextCard();
    }

    public void doSkip(View view) {
        showNextCard();
    }

    private void showNextCard() {
        if(cards.indexOf(currentCard) < (cards.size() - 1)) {
            currentCard = cards.get((cards.indexOf(currentCard)) + 1);

            front = false;
            rotateAndShowCard();
        }
    }

    // top right menu
    public  boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "insert");
        menu.add(0, 2, 0, "update");
        menu.add(0, 3, 0, "delete");
        menu.add(0, 4, 0, "read");
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Card card = new Card();
                card.setDate(LocalDate.now());
                card.setFront("cat");
                card.setBack("кошка");
                card.setExample("Cats like milk");
                card.setStatus(1);
                baseService.insertCard(card);

                break;
            case 2:
                Card updatedCard = new Card();
                updatedCard.setId(2);
                updatedCard.setDate(LocalDate.now());
                updatedCard.setFront("cat");
                updatedCard.setBack("кот");
                updatedCard.setExample("Cats like milk");
                updatedCard.setStatus(0);

                baseService.updateCard(updatedCard);

                break;
            case 3:
                baseService.deleteCard(1);
//                List<Card> cards = InOut.getInstance().getCards();
//                for(Card tempCard : cards) {
//                    baseService.insertCard(tempCard);
//                }

                break;
            case 4:
                List<Card> cardList = baseService.getCards();
                Log.d(TAG, "cards: " + cardList.size());

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
