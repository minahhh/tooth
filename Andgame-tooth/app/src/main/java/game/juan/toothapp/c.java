package game.juan.toothapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import game.juan.andenginegame0.R;

/**
 * Created by GP62 on 2017-10-14.
 */

public class c extends Activity {
    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);


        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_c);
    }
}
