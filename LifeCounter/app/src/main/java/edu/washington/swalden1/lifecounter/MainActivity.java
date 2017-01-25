package edu.washington.swalden1.lifecounter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final int MAX_LIFE = 20;
    public static final int DEFAULT_NUMBER_OF_PLAYERS = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: have a button add another player, probably in top right of toolbar along top
        for (int i = 1; i <= DEFAULT_NUMBER_OF_PLAYERS; i++) {
            addPlayer(i);
        }
    }

    private void addPlayer(int id) {
        final int player_number = id;
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = vi.inflate(R.layout.player_card, (ViewGroup)findViewById(R.id.activity_main), false);

        // fill in any details dynamically here
        TextView textView = (TextView) v.findViewById(R.id.player_name);
        textView.setText("Player " + player_number);

        LinearLayout bp = (LinearLayout) v.findViewById(R.id.buttonPanel);
        for (int i = 0; i < bp.getChildCount(); i++) {
            bp.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v(TAG, view.toString() + " clicked!");
                    Button b = (Button) view;
                    String buttonText = ((String)b.getText());
                    if (buttonText.charAt(0) == '+') {
                        buttonText = buttonText.substring(1);
                    }
                    TextView hf = (TextView) v.findViewById(R.id.health_fraction);
                    RelativeLayout br = (RelativeLayout) v.findViewById(R.id.blood_remaining);
                    RelativeLayout bl = (RelativeLayout) v.findViewById(R.id.blood_lost);

                    String[] healthFraction = ((String)hf.getText()).split(" / ");
                    int result = (Integer.parseInt(healthFraction[0], 10) + Integer.parseInt(buttonText, 10));
                    if (result <= 0) {
                        result = 0;

                        // Player has lost the game
                        CharSequence text = "Player " + player_number + " LOSES!";
                        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
                        toast.show();
                    } else if (result > MAX_LIFE) {
                        result = MAX_LIFE;
                    }

                    float remaining = result / (float)MAX_LIFE;
                    float lost = 1.0f - remaining;
                    br.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, remaining));
                    bl.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, lost));
                    Log.i(TAG, "1: " + (result / (float)MAX_LIFE));
                    hf.setText("" + result + " / " + MAX_LIFE);

                }
            });
        }

        // insert into main view
        ViewGroup insertPoint = (ViewGroup) findViewById(R.id.activity_main);
        insertPoint.addView(v);
    }
}
