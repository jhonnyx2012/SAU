package uneg.software.sau.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uneg.software.sau.R;
import uneg.software.sau.adapters.FullScreenImageAdapter;
import uneg.software.sau.models.Noticia;

public class DetalleNoticiaActivity extends AppCompatActivity implements OnMapReadyCallback, BaseSliderView.OnSliderClickListener {
    private GoogleMap mMap;
    Noticia item;
    @InjectView(R.id.foto)
    ImageView foto;
    @InjectView(R.id.fecha)
    TextView fecha;
    @InjectView(R.id.card_fotos)
    CardView cardFotos;
    @InjectView(R.id.descripcion)
    TextView descripcion;
    @InjectView(R.id.autor)
    TextView autor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_noticia);
        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createNoticiaByBundle(getIntent());
        ButterKnife.inject(this);
        SpannableString s = new SpannableString(item.getArea());
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/AvenirLight.otf"); // your custom font
        s.setSpan(face, 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        getSupportActionBar().setTitle(s);
        descripcion.setText(item.getDescripcion());
        autor.setText(item.getNombreUser());
        fecha.setText(item.getFecha());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if(item.getFirstFoto().isEmpty())
        {
            foto.setVisibility(View.INVISIBLE);
            cardFotos.setVisibility(View.GONE);
        }else
        {
            Picasso.with(this)
                    .load(getString(R.string.url)+item.getFirstFoto())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(foto);

            SliderLayout mDemoSlider = (SliderLayout) findViewById(R.id.slider);
            mDemoSlider.removeAllSliders();
            for (String url : item.getFotos()) {
                TextSliderView textSliderView = new TextSliderView(this);
                textSliderView.setOnSliderClickListener(this);
                textSliderView
                        .image(getString(R.string.url)+url)
                        .setScaleType(BaseSliderView.ScaleType.Fit);
                mDemoSlider.addSlider(textSliderView);
            }
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Fade);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(2000);
        }
    }

    private void createNoticiaByBundle(Intent i) {
        ArrayList<String> aux=i.getStringArrayListExtra("fotos");
        item=new Noticia(aux,"",i.getStringExtra("area"),i.getStringExtra("descripcion"));
        item.setLat(i.getDoubleExtra("lat",0));
        item.setLon(i.getDoubleExtra("lon", 0));
        item.setNombreUser(i.getStringExtra("autor"));
        item.setFecha(i.getStringExtra("fecha"));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng p = new LatLng(item.getLat(), item.getLon());
        //mMap.addMarker(new MarkerOptions().position(p).title(item.getArea()));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(p));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(p)      // Sets the center of the map to Mountain View
                .zoom(18)                   // Sets the zoom
                .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        MarkerOptions options=new MarkerOptions()
                .title(item.getArea())
                .snippet(item.getDescripcion())
                .position(p)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));
        mMap.addMarker(options);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Intent i=new Intent(this, FullScreenSliderActivity.class);
        i.putStringArrayListExtra("fotos", item.getFotos());
        startActivity(i);
    }
}
