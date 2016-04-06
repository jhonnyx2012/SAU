package uneg.software.sau.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.UtteranceProgressListener;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uneg.software.sau.R;
import uneg.software.sau.adapters.SectionsPagerAdapter;
import uneg.software.sau.interfaces.ApiRestInterface;
import uneg.software.sau.models.Alarma;
import uneg.software.sau.models.LoginResponse;
import uneg.software.sau.utils.Constants;
import uneg.software.sau.utils.PermissionsDispatcher;
import uneg.software.sau.utils.UserSessionManager;
import uneg.software.sau.utils.Utils;
import uneg.software.sau.views.CustomFontEditText;


public class NoticiasActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    @InjectView(R.id.container)
     ViewPager mViewPager;
    private UserSessionManager sessionManager;
    @InjectView(R.id.username)
    TextView username;
    @InjectView(R.id.nombre)
    TextView nombre;
    @InjectView(R.id.nivel)
    TextView nivel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);
        ButterKnife.inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        sessionManager=new UserSessionManager(this);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),sessionManager.getLastArea());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabLayout.setupWithViewPager(mViewPager);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if(!Utils.canSeeCerca(sessionManager.getNivelUser()))
        {
            navigationView.getMenu().removeItem(R.id.cerca);
        }
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(sessionManager.getLastItemId());
        username.setText(sessionManager.getUsername());
        nombre.setText(sessionManager.getNombreCompleto());
        nivel.setText(sessionManager.getDescripcionNivel());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            UserSessionManager session=new UserSessionManager(this);
            session.logoutUser();
            Intent i=new Intent(this,LoginActivity.class);
            startActivity(i);
            NoticiasActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int area =0;
        switch (item.getItemId())
        {
            case R.id.pasillo:area=Constants.AREA_PASILLO;break;
            case R.id.patio:area=Constants.AREA_PATIO;break;
            case R.id.cerca:area=Constants.AREA_CERCA;break;
            case R.id.estacionamiento:area=Constants.AREA_ESTACIONAMIENTO;break;
            case R.id.cafetin:area=Constants.AREA_CAFETIN;break;
            case R.id.transporte:area=Constants.AREA_TRANSPORTE;break;
            case R.id.todas:area=Constants.AREA_TODAS;break;
        }
        mSectionsPagerAdapter.reloadWithhArea(area);
        sessionManager.setLastArea(area);
        sessionManager.setLastItemId(item.getItemId());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    public void openAlertar() {
        Intent i = new Intent(this, AlertarActivity.class);
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
