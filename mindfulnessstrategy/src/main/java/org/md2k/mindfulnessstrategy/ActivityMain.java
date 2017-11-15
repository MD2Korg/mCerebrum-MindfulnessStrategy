package org.md2k.mindfulnessstrategy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.md2k.mcerebrum.commons.permission.Permission;
import org.md2k.mcerebrum.commons.permission.PermissionCallback;

import es.dmoral.toasty.Toasty;

public class ActivityMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Permission.requestPermission(this, new PermissionCallback() {
            @Override
            public void OnResponse(boolean isGranted) {
                SharedPreferences sharedpreferences = getSharedPreferences("permission", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putBoolean("permission", isGranted);
                editor.apply();
                if (!isGranted) {
                    Toasty.error(getApplicationContext(), "!PERMISSION DENIED !!! Could not continue...", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    if(getIntent().getStringExtra("trigger_type")!=null){
                        Intent intent=new Intent(ActivityMain.this, ActivityStrategy.class);
                        intent.putExtra("trigger_type",getIntent().getStringExtra("trigger_type"));
                        startActivity(intent);
                        finish();
                    }else {
                        load();
                    }
                }
            }
        });

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
     /*   button = (Button) findViewById(R.id.button_faq);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ActivityMain.this, ActivityFaq.class);
                startActivity(intent);
            }
        });
*/
        button = (Button) findViewById(R.id.button_exit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
