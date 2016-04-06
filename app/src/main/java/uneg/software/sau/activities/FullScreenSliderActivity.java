package uneg.software.sau.activities;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uneg.software.sau.R;
import uneg.software.sau.adapters.FullScreenImageAdapter;

public class FullScreenSliderActivity extends AppCompatActivity {
@InjectView(R.id.pager)
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_slider);
        ButterKnife.inject(this);
        ArrayList<String> aux = getIntent().getStringArrayListExtra("fotos");
        FullScreenImageAdapter adapter=new FullScreenImageAdapter(this,aux);
        pager.setAdapter(adapter);
    }
}