package uneg.software.sau.activities;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonElement;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Digits;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Min;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uneg.software.sau.R;
import uneg.software.sau.interfaces.ApiRestInterface;
import uneg.software.sau.models.BaseResponse;
import uneg.software.sau.utils.Constants;
import uneg.software.sau.views.CustomFontEditText;
import uneg.software.sau.views.CustomFontTextView;

public class RegistroActivity extends ActionBarActivity implements View.OnClickListener, Validator.ValidationListener {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.iniciar_sesion)
    CustomFontTextView login;

    @InjectView(R.id.registrarme)
    Button registrarme;

    @NotEmpty
    @InjectView(R.id.nombre)
    CustomFontEditText nombre;

    @NotEmpty
    @InjectView(R.id.apellido)
    CustomFontEditText apellido;

    @NotEmpty
    @InjectView(R.id.username)
    CustomFontEditText username;

    @NotEmpty
    @Email
    @InjectView(R.id.correo)
    CustomFontEditText correo;

    @NotEmpty
    @InjectView(R.id.telefono)
    CustomFontEditText telefono;

    @NotEmpty
    @Password(min = 5, scheme = Password.Scheme.ANY)
    @InjectView(R.id.clave)
    CustomFontEditText clave;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    Validator validator;

    ProgressDialog progressDialog;

    private void mostrarEspera(String s)
    {
        progressDialog = ProgressDialog.show(this, "",s, true);
    }

    private void ocultarEspera()
    {
        progressDialog.dismiss();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        login.setOnClickListener(this);
        registrarme.setOnClickListener(this);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        validator = new Validator(this);

        validator.setValidationListener(this);    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iniciar_sesion:
                openLogin();
                break;
            case R.id.registrarme:
               validator.validate();
                break;
        }

    }

    private void registrar() {
        mostrarEspera("Registrando...");

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.url))
                .build();
        restAdapter.create(ApiRestInterface.class).registrarUsuario(
                nombre.getText().toString().trim(),
                apellido.getText().toString().trim(),
                username.getText().toString().trim(),
                correo.getText().toString().trim(),
                telefono.getText().toString().trim(),
                clave.getText().toString().trim(),
                Constants.NIVEL_ESTUDIANTE,
                mCallBack);
    }

    private Callback<BaseResponse> mCallBack = new Callback<BaseResponse>() {
        @Override
        public void success(BaseResponse r, Response response) {

            if (r.isStatus()) {
                openLogin();
            }
            Toast.makeText(RegistroActivity.this,r.getMessage(), Toast.LENGTH_SHORT).show();
            ocultarEspera();

        }

        @Override
        public void failure(RetrofitError error) {
            Log.i("REGISTRO", error.getMessage());
            Toast.makeText(RegistroActivity.this, "Ha habido un problema", Toast.LENGTH_SHORT).show();
            ocultarEspera();
        }
    };

    private void openLogin() {
        finish();
    }


    @Override
    public void onValidationSucceeded() {
        registrar();
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