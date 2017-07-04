package org.md2k.mindfulnessstrategy;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.md2k.datakitapi.exception.DataKitException;
import org.md2k.datakitapi.messagehandler.OnConnectionListener;
import org.md2k.mindfulnessstrategy.data.DataManager;
import org.md2k.mindfulnessstrategy.datakit.DataKitManager;
import org.md2k.mindfulnessstrategy.strategy.Category;
import org.md2k.mindfulnessstrategy.strategy.CategoryManager;
import org.md2k.mindfulnessstrategy.strategy.Strategy;
import org.md2k.utilities.dialog.Dialog;
import org.md2k.utilities.dialog.DialogCallback;

import java.io.IOException;

public class ActivityStrategy extends Activity {
    private static final long WAIT_LIMIT = 10 * 1000L;//5*60*1000L;
    private static final long QUIT_LIMIT = 10 * 1000L;//5*60*1000L;
    CategoryManager categoryManager;
    Category category;
    Strategy strategy;
    boolean isPreQuit = true, isLapse = false;
    String trigger_type = null;
    double rating = -1;
    Handler handler;
    MaterialDialog materialDialog;
    DataKitManager dataKitManager;
    DataManager dataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_strategy);
            materialDialog= new Dialog().ProgressIndeterminate(this, null,"Loading...").build();
            materialDialog.show();
            trigger_type = getIntent().getStringExtra("trigger_type");
            categoryManager = new CategoryManager(this);
            handler = new Handler();
            dataKitManager=new DataKitManager(this);
            dataKitManager.connect(new OnConnectionListener() {
                @Override
                public void onConnected() {
                    try {
                        prepareValue();
                        prepareUI();
                        materialDialog.dismiss();
                    } catch (DataKitException e) {
                        quit();
                    }
                }
            });
        } catch (IOException e) {
            Toast.makeText(this, "!!! ERROR !!!. Can't access file", Toast.LENGTH_LONG).show();
            finish();
        } catch (DataKitException e) {
            Toast.makeText(this, "!!! ERROR !!!. Can't access datakit", Toast.LENGTH_LONG).show();
            finish();
        }
    }
    private void prepareValue() throws DataKitException {
        isPreQuit=dataKitManager.isPreQuit();
        isLapse=dataKitManager.isLapse();
        dataManager=new DataManager(trigger_type, isPreQuit, isLapse);
    }

    private void setTextMessage(String message) {
        ((TextView) findViewById(R.id.text_view_message)).setText(message);
    }

    private void setButtonYes() {
        Button buttonYes = (Button) findViewById(R.id.button_yes);
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataManager.add("USER_RESPONSE","Would you like a different strategy than the one you see here? - Response: YES");
                setTextMessage(getStrategyDiff());
                findViewById(R.id.layout_different).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_same).setVisibility(View.GONE);
                findViewById(R.id.layout_likert).setVisibility(View.GONE);
                waitForResponse();
            }
        });
    }

    private void setButtonNo() {
        Button buttonNo = (Button) findViewById(R.id.button_no);
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataManager.add("USER_RESPONSE","Would you like a different strategy than the one you see here? - Response: NO");
                findViewById(R.id.layout_different).setVisibility(View.GONE);
                findViewById(R.id.layout_same).setVisibility(View.VISIBLE);
                findViewById(R.id.layout_likert).setVisibility(View.GONE);
                waitForResponse();
            }
        });
    }

    private void setButtonClose() {
        Button buttonClose = (Button) findViewById(R.id.button_close);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float ratings, boolean fromUser) {
                rating = ratings;
            }
        });
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rating!=-1){
                    dataManager.add("RATING",category.getId(), strategy.getId(), rating);
                    try {
                        strategy.setRating(rating);
                        categoryManager.write();
                    } catch (IOException ignored) {

                    }
                }
                quit();
            }
        });

    }

    private void setButtonComplete() {
        Button buttonComplete = (Button) findViewById(R.id.button_complete);
        buttonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataManager.add("BUTTON_CLICK_RESPONSE","I completed the strategy");
                complete();
            }
        });
    }
    void complete(){
        findViewById(R.id.layout_different).setVisibility(View.GONE);
        findViewById(R.id.layout_same).setVisibility(View.GONE);
        findViewById(R.id.layout_likert).setVisibility(View.VISIBLE);
        findViewById(R.id.ratingBar).setVisibility(View.VISIBLE);
        setTextMessage("Great job! Try to use the strategy you just completed throughout your day. Please rate the strategy you just completed.");
        quitForResponse();

    }
    void notComplete(){
        findViewById(R.id.layout_different).setVisibility(View.GONE);
        findViewById(R.id.layout_same).setVisibility(View.GONE);
        findViewById(R.id.layout_likert).setVisibility(View.VISIBLE);
        findViewById(R.id.ratingBar).setVisibility(View.GONE);
        setTextMessage("That's ok! You'll receive another strategy to try later!");
        quitForResponse();
    }

    private void setButtonNotComplete() {
        Button buttonNotComplete = (Button) findViewById(R.id.button_not_complete);
        buttonNotComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataManager.add("BUTTON_CLICK_RESPONSE","I did not complete the strategy");
                notComplete();
            }
        });
    }

    private void prepareUI() {
        setTextMessage(getStrategy());
        handler.postDelayed(runnableWait, WAIT_LIMIT);
        setButtonYes();
        setButtonNo();
        setButtonComplete();
        setButtonNotComplete();
        setButtonClose();

        findViewById(R.id.layout_different).setVisibility(View.VISIBLE);
        findViewById(R.id.layout_same).setVisibility(View.GONE);
        findViewById(R.id.layout_likert).setVisibility(View.GONE);
    }

    private String getStrategy() {
        category = categoryManager.getCategoryRandom(trigger_type, isPreQuit, isLapse);
        strategy = category.getStrategyRandom();
        dataManager.add("SELECTED_STRATEGY", category.getId(), strategy.getId());
        return strategy.getId();
    }

    private String getStrategyDiff() {
        if(trigger_type.equals("USER"))
            category = categoryManager.getCategoryRandom(trigger_type, isPreQuit, isLapse, category);
        else
            category = categoryManager.getCategoryRandom(null, isPreQuit, isLapse, category);
        strategy = category.getStrategyRandom();
        dataManager.add(category.getId(), strategy.getId());
        return strategy.getId();
    }

    Runnable runnableWait = new Runnable() {
        @Override
        public void run() {
            Dialog dialog = new Dialog();
            dataManager.add("NOTIFY_DIALOG_SHOW", "No response in last 5 minutes. Ask whether the strategy is completed with YES/No option");
            materialDialog = dialog.Question(ActivityStrategy.this, "Complete Strategy?", "Did you complete the strategy just sent to you?", new String[]{"Yes", "No"}, new DialogCallback() {
                @Override
                public void onDialogCallback(Dialog.DialogResponse which, String[] result) {
                    if(which== Dialog.DialogResponse.POSITIVE) {
                        dataManager.add("NOTIFY_DIALOG_RESPONSE","I completed the strategy");
                        complete();
                    }
                    else {
                        dataManager.add("NOTIFY_DIALOG_RESPONSE","I did not complete the strategy");
                        notComplete();
                    }
                }
            }).build();
            materialDialog.show();
            handler.postDelayed(runnableQuit, QUIT_LIMIT);
        }
    };
    Runnable runnableQuit = new Runnable() {
        @Override
        public void run() {
            if (materialDialog != null) materialDialog.show();
            quit();
        }
    };
    void waitForResponse(){
        handler.removeCallbacks(runnableQuit);
        handler.removeCallbacks(runnableWait);
        handler.postDelayed(runnableWait, WAIT_LIMIT);
    }
    void quitForResponse(){
        handler.removeCallbacks(runnableQuit);
        handler.removeCallbacks(runnableWait);
        handler.postDelayed(runnableQuit, QUIT_LIMIT);
    }

    void quit(){
        try {
            handler.removeCallbacks(runnableQuit);
            handler.removeCallbacks(runnableWait);
            dataKitManager.save(trigger_type, dataManager);
            dataKitManager.disconnect();
        } catch (DataKitException ignored) {

        }
        finish();
    }
}
