package uneg.software.sau.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


import butterknife.ButterKnife;
import butterknife.InjectView;
import fr.ganfra.materialspinner.MaterialSpinner;
import nl.changer.polypicker.Config;
import nl.changer.polypicker.ImagePickerActivity;
import nl.changer.polypicker.utils.ImageInternalFetcher;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uneg.software.sau.R;
import uneg.software.sau.adapters.NoticiasAdapter;
import uneg.software.sau.interfaces.ApiRestInterface;
import uneg.software.sau.models.Alarma;
import uneg.software.sau.models.Area;
import uneg.software.sau.models.BaseResponse;
import uneg.software.sau.utils.UserSessionManager;
import uneg.software.sau.utils.Utils;
import uneg.software.sau.views.CustomFontEditText;

public class AlertarActivity extends AppCompatActivity implements View.OnClickListener, Validator.ValidationListener {
    private static final int INTENT_REQUEST_GET_IMAGES = 2323;
    Toolbar toolbar;

    @InjectView(R.id.pickImages)
    ImageView pickImage;
    ArrayList<Uri> mMedia = new ArrayList<>();
    private ViewGroup mSelectedImagesContainer;
    @InjectView(R.id.selectArea)
    MaterialSpinner selectArea;
    @InjectView(R.id.descripcion)
    @NotEmpty(message = "Ingrese un mensaje para la solicitud")
    CustomFontEditText descripcion;

    Validator validator;
    private ArrayList<Area> areas;
    private ArrayList<String> fotos;
    private ProgressDialog progressDialog;
    private int indexSubiendo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertar);
        ButterKnife.inject(this);
        indexSubiendo=0;
        fotos=new ArrayList<>();
        mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        validator = new Validator(this);
        validator.setValidationListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pickImage.setOnClickListener(this);
        getAreas();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alertar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.send) {
            if(selectArea.getSelectedItemPosition() == 0){
                Snackbar.make(selectArea,"Seleccione un area para la solicitud", Snackbar.LENGTH_SHORT).show();
            }
            else
            validator.validate();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getImages() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        Config config = new Config.Builder()
                .setTabBackgroundColor(R.color.white)    // set tab background color. Default white.
                .setTabSelectionIndicatorColor(R.color.colorAccent)
                .setCameraButtonColor(R.color.colorAccent)
                .setSelectionLimit(3)// set photo selection limit. Default unlimited selection.
                .build();
        ImagePickerActivity.setConfig(config);
        startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.pickImages:
                getImages();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resuleCode, Intent intent) {
        super.onActivityResult(requestCode, resuleCode, intent);

        if (resuleCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {
                Parcelable[] parcelableUris = intent.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                if (parcelableUris == null) {
                    return;
                }

                // Java doesn't allow array casting, this is a little hack
                Uri[] uris = new Uri[parcelableUris.length];
                System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);

                mMedia.clear();
                if (uris != null) {
                    for (Uri uri : uris) {
                        Log.i("jhonny", " uri: " + uri);
                        mMedia.add(uri);
                    }

                    showMedia();
                }
            }
        }
    }

    private void showMedia() {
        mSelectedImagesContainer.removeAllViews();
        ImageInternalFetcher imageFetcher = new ImageInternalFetcher(this, 500);
        for (Uri uri:mMedia) {
            Log.i("jhonny", " uri: " + uri);
            if (mMedia.size() >= 1) {
                mSelectedImagesContainer.setVisibility(View.VISIBLE);
            }

            View imageHolder = LayoutInflater.from(this).inflate(R.layout.media_layout, null);

            ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);

            if (!uri.toString().contains("content://")) {
                uri = Uri.fromFile(new File(uri.toString()));
            }

            imageFetcher.loadImage(uri, thumbnail);

            mSelectedImagesContainer.addView(imageHolder);

            int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());
            int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
            thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));
        }
    }

    private void getAreas()
    {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.url))
                .build();
        restAdapter.create(ApiRestInterface.class).getAreas(areasCallback);
    }

    private Callback<ArrayList<Area>> areasCallback=new Callback<ArrayList<Area>>() {
        @Override
        public void success(ArrayList<Area> r, Response response) {

            ArrayAdapter<String> adapter = new ArrayAdapter<>(AlertarActivity.this, android.R.layout.simple_spinner_item, getArrayByList(r));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectArea.setAdapter(adapter);
            areas=r;
        }

        @Override
        public void failure(RetrofitError error) {
            Log.i("GET ALERTAS", error.getMessage());
            Toast.makeText(AlertarActivity.this, "Ha habido un problema", Toast.LENGTH_SHORT).show();
        }
    };

    private String[] getArrayByList(ArrayList<Area> r) {
        ArrayList<String> result=new ArrayList<>();
        for(Area area:r)
        {
            if((!area.getDescripcion().toLowerCase().contains("cerca"))&&((!area.getDescripcion().toLowerCase().contains("transporte"))))
            {
                result.add(area.getDescripcion());
            }
        }
        return result.toArray(new String[0]);
    }


    private void uploadPhoto(int i) {
        mostrarEspera("Subiendo imagen "+(i+1));
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(getResources().getString(R.string.url)).build();
        restAdapter.create(ApiRestInterface.class).subirFoto(Utils.fileToBase64(Utils.getFileByN(i, mMedia)), mUploadCallBack);
    }


    private Callback<BaseResponse> mUploadCallBack=new Callback<BaseResponse>() {
        @Override
        public void success(final BaseResponse baseResponse, Response response) {
            ocultarEspera();
            if(baseResponse.isStatus())
            {
                fotos.add(baseResponse.getMessage());
                Toast.makeText(AlertarActivity.this,"SUBIDA "+baseResponse.getMessage(),Toast.LENGTH_SHORT).show();
                indexSubiendo++;
                if(indexSubiendo<mMedia.size())
                {
                    uploadPhoto(indexSubiendo);
                }else
                {
                    enviarAlerta();
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            Log.i("AlertarActivity",error.getMessage());
            Toast.makeText(AlertarActivity.this,"PROBLEMA AL SUBIR IMAGEN",Toast.LENGTH_SHORT).show();
            ocultarEspera();
            reinitAll();
        }
    };

    private void reinitAll() {
        mMedia.clear();
        showMedia();
        descripcion.setText("");
        indexSubiendo=0;
        fotos.clear();
        selectArea.setSelection(0);
    }


    private void mostrarEspera(String s)
    {
        progressDialog = ProgressDialog.show(this, "", s, true);
    }

    private void ocultarEspera()
    {
        progressDialog.dismiss();
    }

    private void enviarAlerta()
    {
        mostrarEspera("Enviando alerta...");
        Area area=getAreaByDescripcion(((String)selectArea.getSelectedItem()));
        UserSessionManager session=new UserSessionManager(this);
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(getResources().getString(R.string.url)).build();
        restAdapter.create(ApiRestInterface.class)
                .sendAlarma(
                        session.getIdUser(),
                        descripcion.getText().toString().trim(),
                        area.getLat().toString(),
                        area.getLon().toString(),
                        area.getId(),
                        fotos.toArray(new String[0]),
                        mAlarmaCallBack);
    }

    private Area getAreaByDescripcion(String selectedItem) {
        for(Area area:areas)
        {
            if(area.getDescripcion().equals(selectedItem))
                return area;
        }
        return null;
    }

    private Callback<Alarma> mAlarmaCallBack=new Callback<Alarma>() {
        @Override
        public void success(Alarma r, Response response) {
            Toast.makeText(AlertarActivity.this,"ALARMA ENVIADA",Toast.LENGTH_SHORT).show();
            ocultarEspera();
            reinitAll();
        }

        @Override
        public void failure(RetrofitError error) {
            Log.i("AlertarActivity","enviando alerta "+error.getMessage());
            Toast.makeText(AlertarActivity.this,"PROBLEMA AL ENVIAR ALARMA",Toast.LENGTH_SHORT).show();
            ocultarEspera();
            reinitAll();
        }
    };

    @Override
    public void onValidationSucceeded() {
        if(mMedia.isEmpty()){

            enviarAlerta();
        }
        else
            uploadPhoto(indexSubiendo);
        //Snackbar.make(toolbar, "Enviando alerta...", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}