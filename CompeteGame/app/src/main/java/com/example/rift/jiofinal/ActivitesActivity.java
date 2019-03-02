package com.example.rift.jiofinal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ActivitesActivity extends AppCompatActivity implements View.OnClickListener {

    Button[] buttons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);
        buttons = new Button[5];

        for(int i=0; i<buttons.length; i++) {
            {
                String buttonID = "button" + (i+1);

                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i] = ((Button) findViewById(resID));
                buttons[i].setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
// TODO Auto-generated method stub

        int index = 0;
        for (int i = 0; i < buttons.length; i++)
        {
            if (buttons[i].getId() == v.getId())
            {
                index = i;
                break;
            }
        }

        if(index ==0)
        {
            Intent intent = new Intent(this, EventDetailActivity.class);
            intent.putExtra("eventObject", "Anagrams");
            startActivity(intent);
        }
        else if(index ==1)
        {
            Intent intent = new Intent(this, EventDetailActivity.class);
            intent.putExtra("eventObject", "Analogy");
            startActivity(intent);
        }
        else if(index ==2)
        {
            Intent intent = new Intent(this, EventDetailActivity.class);
            intent.putExtra("eventObject", "OddOne");
            startActivity(intent);
        }
        else if(index ==3)
        {
            Intent intent = new Intent(this, EventDetailActivity.class);
            intent.putExtra("eventObject", "Series");
            startActivity(intent);
        }
        else if(index ==4)
        {
            Intent intent = new Intent(this, ChessaActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }
}

