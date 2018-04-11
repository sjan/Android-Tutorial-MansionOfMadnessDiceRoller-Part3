package farsight.solutions.tutorial.diceroller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final int ONE_SECOND = 1000;
    private static final int TWO_SECONDS = 2000;
    private static final int FULL_REVOLUTION = 360;
    private static final int THREE_REVOLUTION = 1080;
    private static final float CENTER = 0.5f;

    Random random = new Random();

    MainPresenter presenter;
    DiceAdapter diceAdapter;
    ListView listView;
    TextView totalCount;
    TextView blankCount;
    TextView magCount;
    TextView starCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Connecting activity to layout
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.dice_list);
        totalCount = findViewById(R.id.total_count);
        blankCount = findViewById(R.id.blank_count);
        magCount = findViewById(R.id.mag_count);
        starCount = findViewById(R.id.star_count);

        //Presenter
        presenter = new MainPresenter();
        presenter.setView(this);

        //Setup ListView and Adapter
        diceAdapter = new DiceAdapter(this, R.layout.dice_row, presenter.getDiceList());

        //Initialize Data
        listView.setAdapter(diceAdapter);
    }

    @Override
    public void rollDice(int diceIndex) {
        //roll all dice that are not being held
        View diceRowView = listView.getChildAt(diceIndex);

        if (diceRowView != null) {
            ImageView diceView = diceRowView.findViewById(R.id.dice_icon);
            int rotation = randomRotation();
            int duration = randomDuration();

            RotateAnimation rotate = new RotateAnimation(
                    0, rotation,
                    Animation.RELATIVE_TO_SELF, CENTER,
                    Animation.RELATIVE_TO_SELF, CENTER
            );
            rotate.setFillAfter(true);
            rotate.setFillEnabled(true);
            rotate.setDuration(duration);
            rotate.setInterpolator(new DecelerateInterpolator());

            diceView.startAnimation(rotate);
        }
    }

    @Override
    public void refreshAdapter() {
        this.diceAdapter.notifyDataSetChanged();
    }

    private int randomDuration() {
        return ONE_SECOND + random.nextInt(TWO_SECONDS);
    }

    private int randomRotation() {
        return FULL_REVOLUTION + random.nextInt(THREE_REVOLUTION);
    }

    @Override
    public void updateDiceCount(int totalDice, int blankDice, int magDice, int starDice) {
        totalCount.setText(Integer.toString(totalDice));
        blankCount.setText(Integer.toString(blankDice));
        magCount.setText(Integer.toString(magDice));
        starCount.setText(Integer.toString(starDice));
    }

    public void onClickAddButton(View view) {
        presenter.addButtonClicked();
    }

    public void onClickRemoveButton(View view) {
        presenter.removeButtonClicked();
    }

    public void onClickRollButton(View view) {
        presenter.rollButtonClicked();
    }

    public void onClickChangeButton(int diceIndex) {
        presenter.changeButtonClicked(diceIndex);
    }

    public void onClickHoldButton(int diceIndex) {
        presenter.holdButtonClick(diceIndex);
    }
}
