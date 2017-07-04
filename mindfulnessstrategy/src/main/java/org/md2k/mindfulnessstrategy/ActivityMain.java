package org.md2k.mindfulnessstrategy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getIntent().getStringExtra("trigger_type")!=null){
            Intent intent=new Intent(this, ActivityStrategy.class);
            intent.putExtra("trigger_type",getIntent().getStringExtra("trigger_type"));
            startActivity(intent);
            finish();
        }else {
            load();
        }
    }

    void load() {
        Button button = (Button) findViewById(R.id.button_practice);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityMain.this, ActivityStrategy.class);
                intent.putExtra("trigger_type","USER");
                startActivity(intent);
                finish();
            }
        });
        button = (Button) findViewById(R.id.button_exit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
