package com.example.myspace;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myspace.data.BaseService;
import com.example.myspace.data.entity.Card;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class CardActivity extends AppCompatActivity {

    BaseService baseService;

    LinearLayout frontBackExampleLayout;
    TextView firstTextView;
    TextView secondTextView;

    private boolean front = false;

    private List<Card> cards;
    private int cardListId = 0;

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
            Log.d(TAG, "CardActivity  onServiceConnected");

            cards = baseService.getCardsByStatus(0);
            Log.d(TAG, "unlearned cards: " + cards.size());
            Collections.shuffle(cards);

            if(cards.size() > 0) {
                rotateAndShowCard(cards.get(cardListId).getId());

                frontBackExampleLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rotateAndShowCard(cards.get(cardListId).getId());
                    }
                });

                frontBackExampleLayout.setOnLongClickListener(new View.OnLongClickListener()  {
                    @Override
                    public boolean onLongClick(View view) {
                        Card editedCard = baseService.getCard(cards.get(cardListId).getId());

                        Intent intent = new Intent(CardActivity.this, CardFormActivity.class);
                        intent.putExtra("front", editedCard.getFront());
                        intent.putExtra("back", editedCard.getBack());
                        intent.putExtra("example", editedCard.getExample());

                        intent.putExtra("id", editedCard.getId());

                        startActivityForResult(intent, 1);

                        return false;
                    }
                });
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "CardActivity  onServiceDisconnected");
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if(requestCode == 0) {  // новая карточка
                Card newCard = new Card(LocalDateTime.now(), data.getStringExtra("newFront"), data.getStringExtra("newBack"), data.getStringExtra("newExample"), 0);
                
                baseService.insertCard(newCard);
            } else { // редактирование существующей
                int cardId = data.getIntExtra("id", 0);

                Card updatedCard = baseService.getCard(cardId);

                updatedCard.setEditDateTime(LocalDateTime.now());
                updatedCard.setFront(data.getStringExtra("newFront"));
                updatedCard.setBack(data.getStringExtra("newBack"));
                updatedCard.setExample(data.getStringExtra("newExample"));

                baseService.updateCard(updatedCard);
            }
        }

        front = false;
        rotateAndShowCard(cards.get(cardListId).getId());
    }

    private void rotateAndShowCard(int cardId) {
        Card currentCard = baseService.getCard(cardId);

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
        cards.get(cardListId).setStatus(1);
        baseService.updateCard(cards.get(cardListId));

        showNextCard();
    }

    public void doSkip(View view) {
        showNextCard();
    }

    private void showNextCard() {
//        Log.d(TAG, "showNextCard " + cards.get(cardListId).print());

        if(cardListId < (cards.size() - 1)) {
            cardListId++;

//            int i = cards.indexOf(currentCard);
//            Log.d(TAG, "start " + i);
//
//            currentCard = cards.get(i + 1);
//            int i3 = cards.indexOf(currentCard);
//            Log.d(TAG, "finish " + i3);
//
//            Log.d(TAG, "showNextCard2 " + currentCard.print());

            front = false;
            rotateAndShowCard(cards.get(cardListId).getId());
        }
    }

    // top right menu
    public  boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "add");
        menu.add(0, 2, 0, "delete");
        menu.add(0, 3, 0, "status");
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent(CardActivity.this, CardFormActivity.class);
                intent.putExtra("front", "");
                intent.putExtra("back", "");
                intent.putExtra("example", "");

                intent.putExtra("id", 0);

                startActivityForResult(intent, 0);

                break;
            case 2:
                AlertDialog.Builder alert = new AlertDialog.Builder(CardActivity.this);
                alert.setTitle("Delete card");
                alert.setMessage("Are you sure to delete this card?");
                alert.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        baseService.deleteCard(cards.get(cardListId).getId());
                    }
                });
                alert.show();

                showNextCard();

                break;
            case 3:
                List<Card> unlearnedCards = baseService.getCardsByStatus(0);
                List<Card> learnedCards = baseService.getCardsByStatus(1);

                Toast.makeText(getApplicationContext(), "Cards: " + unlearnedCards.size() + " unlearned and " + learnedCards.size() + " learned", Toast.LENGTH_SHORT).show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
