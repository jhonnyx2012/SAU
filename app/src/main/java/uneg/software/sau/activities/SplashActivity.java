package uneg.software.sau.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.Timer;
import java.util.TimerTask;

import uneg.software.sau.R;
import uneg.software.sau.utils.UserSessionManager;

public class SplashActivity extends AppCompatActivity {

    private static final long SPLASH_SCREEN_DELAY = 1200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Animation hide = AnimationUtils.loadAnimation(this, R.anim.to_top_anim);
        findViewById(R.id.logo).startAnimation(hide);
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
               openLogin();
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, SPLASH_SCREEN_DELAY);
    }

    private void openLogin() {
        UserSessionManager session=new UserSessionManager(this);
        Class activity=session.isUserLoggedIn()?NoticiasActivity.class:LoginActivity.class;
        Intent i = new Intent(this, activity);
        startActivity(i);
        SplashActivity.this.finish();
    }
}
