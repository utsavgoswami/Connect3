package com.myappcompany.utsavgoswami.connect3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;

enum GamePiece {
    RED, YELLOW
}

public class MainActivity extends AppCompatActivity {
    // By default, make the first person to go have the red piece
    GamePiece currentPiece = GamePiece.RED;
    Drawable defaultStyle;

    public static final int BOARD_SIZE = 9;

    int currentCapacity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = (Button) findViewById(R.id.button1);
        int bHeight = b.getHeight();
        int bWidth = b.getWidth();

        Button hiddenBtn = (Button) findViewById(R.id.playAgainBtn);
        hiddenBtn.setClickable(false);


        defaultStyle = b.getBackground();
    }

    public void placePiece(View view) {
        Button btn = (Button) view;

        switch (currentPiece) {
            case RED:
                btn.setBackgroundResource(R.drawable.redpiece30by30);
                btn.setTag("Red");

                // Proceed to next player
                currentPiece = GamePiece.YELLOW;
                break;

            case YELLOW:
                btn.setBackgroundResource(R.drawable.yellowpiece30by30);
                btn.setTag("Yellow");

                // Proceed to next player
                currentPiece = GamePiece.RED;
                break;
        }

        // Player has claimed spot
        btn.setClickable(false);



        // Update capacity
        currentCapacity++;

        // Examine for potential winner
        checkRows();
        checkColumns();
        checkDiagonals();

        // Check if the board is full
        checkCapacity();
    }

    public void checkColumns() {
        // Check all three columns to see if there is a three in a column
        int j = 1;
        boolean winnerFound = false;

        for (int i = 1; i <= 3; i++) {
            Button button = (Button) findViewById(getResources().getIdentifier("button" + j, "id",
                    this.getPackageName()));
            Button button2 = (Button) findViewById(getResources().getIdentifier("button" + (j + 3), "id",
                    this.getPackageName()));
            Button button3 = (Button) findViewById(getResources().getIdentifier("button" + (j + 6), "id",
                    this.getPackageName()));
            String tag = (String) button.getTag();
            String tag2 = (String) button2.getTag();
            String tag3 = (String) button3.getTag();

            // Update position of j for next column
            j++;

            // Skip examining column if any item in the current column is unfilled
            if (tag.equals("unfilled") || tag2.equals("unfilled") || tag3.equals("unfilled")) {
                continue;
            }

            if ((tag.equals(tag2)) && (tag2.equals(tag3))) {
                winnerFound = true;
                break;
            }
        }

        if (winnerFound) {
            announceWinner();
        }

    }

    public void checkDiagonals() {
        // The piece in the middle will always be included in both diagonals in the gameboard
        Button button = (Button) findViewById(getResources().getIdentifier("button" + 5, "id",
                this.getPackageName()));
        String tag = (String) button.getTag();
        boolean winnerFound = false;

        // First, examine the diagonal created by the top left corner, middle, and bottom right
        int head = 1;
        int tail = 9;

        for (int i = 1; i <= 2; i++) {
            Button button2 = (Button) findViewById(getResources().getIdentifier("button" + head, "id",
                    this.getPackageName()));
            Button button3 = (Button) findViewById(getResources().getIdentifier("button" + tail, "id",
                    this.getPackageName()));
            String tag2 = (String) button2.getTag();
            String tag3 = (String) button3.getTag();

            // We will next examine the diagonal formed by the rop right corner, middle,
            // and the bottom left corner
            head += 2;
            tail -= 2;

            // Skip examining diagonal if any tile is unfilled
            if (tag.equals("unfilled") || tag2.equals("unfilled") || tag3.equals("unfilled")) {
                continue;
            }

            if ((tag.equals(tag2)) && (tag2.equals(tag3))) {
                winnerFound = true;
                break;
            }
        }

        if (winnerFound) {
            announceWinner();
        }
    }

    public void announceWinner() {
        TextView results = findViewById(R.id.results);

        switch (currentPiece) {
            case RED:
                results.setText("Yellow has won!");
                break;
            case YELLOW:
                results.setText("Red has won!");
                break;
        }

        Button playAgainBtn = (Button) findViewById(R.id.playAgainBtn);
        playAgainBtn.setClickable(true);
        playAgainBtn.setVisibility(View.VISIBLE);
    }

    public void checkRows() {
        // Check all three rows to see if there is a three in a row
        int j = 1;
        boolean winnerFound = false;

        for (int i = 1; i <= 3; i++) {
            Button button = (Button) findViewById(getResources().getIdentifier("button" + j, "id",
                    this.getPackageName()));
            Button button2 = (Button) findViewById(getResources().getIdentifier("button" + (j + 1), "id",
                    this.getPackageName()));
            Button button3 = (Button) findViewById(getResources().getIdentifier("button" + (j + 2), "id",
                    this.getPackageName()));
            String tag = (String) button.getTag();
            String tag2 = (String) button2.getTag();
            String tag3 = (String) button3.getTag();


            // Update position of j for next row
            j += 3;
            // Skip examining row if any item in the current row is unfilled
            if (tag.equals("unfilled") || tag2.equals("unfilled") || tag3.equals("unfilled")) {
                continue;
            }

            if ((tag.equals(tag2)) && (tag2.equals(tag3))) {
                winnerFound = true;
                break;
            }

        }

        if (winnerFound) {
            announceWinner();
        }

    }

    public void resetBoard(View view) {
        // Hide results board for new game
        TextView results = findViewById(R.id.results);
        results.setText("");

        // Reset currentCapacity
        currentCapacity = 0;

        // Reset every button
        for (int i = 1; i <= 9; i++) {
            Button button = (Button) findViewById(getResources().getIdentifier("button" + i, "id",
                    this.getPackageName()));
            // Revert every tile into a default unfilled tile
            button.setBackgroundDrawable(defaultStyle);
            button.setTag("unfilled");

            // Allow the button to be clickable again for the new game
            button.setClickable(true);
        }

        // Hide "Play Again" button
        Button hiddenBtn = (Button) findViewById(R.id.playAgainBtn);
        hiddenBtn.setVisibility(View.GONE);
        hiddenBtn.setClickable(false);

    }

    public void checkCapacity() {
        if (currentCapacity == BOARD_SIZE) {
            TextView results = findViewById(R.id.results);
            results.setText("It's a draw!");

            Button playAgainBtn = (Button) findViewById(R.id.playAgainBtn);
            playAgainBtn.setVisibility(View.VISIBLE);

            // Reset currentCapacity
            //currentCapacity = 0;

            // Reset every button
            //for (int i = 1; i <= 9; i++) {
            //    Button button = (Button) findViewById(getResources().getIdentifier("button" + i, "id",
            //            this.getPackageName()));
            //    button.setBackgroundDrawable(defaultStyle);
            //    button.setTag("unfilled");

            //    // Allow the button to be clickable again for the new game
            //    button.setClickable(true);
            //}
        }
    }
}
