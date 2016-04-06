package uneg.software.sau.fragments;

/**
 * Created by Jhonny on 25/2/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uneg.software.sau.R;
import uneg.software.sau.activities.AlertarActivity;
import uneg.software.sau.activities.DetalleNoticiaActivity;
import uneg.software.sau.activities.NoticiasActivity;
import uneg.software.sau.adapters.NoticiasAdapter;
import uneg.software.sau.interfaces.ApiRestInterface;
import uneg.software.sau.interfaces.RecyclerItemClickListener;
import uneg.software.sau.models.Alarma;
import uneg.software.sau.models.Noticia;
import uneg.software.sau.utils.Constants;
import uneg.software.sau.utils.PermissionsDispatcher;
import uneg.software.sau.utils.UserSessionManager;
import uneg.software.sau.utils.Utils;

public class NoticiasFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,RecyclerItemClickListener {

    @InjectView(R.id.lista)
    RecyclerView lista;

    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefresh;
    private NoticiasAdapter adapter;

    @InjectView(R.id.no_alertas)
    TextView noAlertas;

    @InjectView(R.id.progressBar)
    ProgressBar progressBar;
    private int area;

    public static NoticiasFragment newInstance(int area)
    {
        NoticiasFragment fragment = new NoticiasFragment();
        fragment.setArea(area);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_noticias, container, false);
        ButterKnife.inject(this, rootView);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        lista.setLayoutManager(mLayoutManager);
        lista.setAdapter(adapter);

        return rootView;
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setColorScheme(
                R.color.colorAccent,
                R.color.colorPrimary,
                R.color.colorAccent,
                R.color.colorPrimaryDark);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionsDispatcher.showDialogPermissions((NoticiasActivity) getActivity());
            }
        });

        getNoticias(area);

    }




    @Override
    public void onRefresh() {

        getNoticias(area);
    }

    @Override
    public void onItemClick(Alarma noticia) {
        Intent i=new Intent(getActivity(), DetalleNoticiaActivity.class);
        i.putExtra("id", noticia.getId());
        i.putExtra("fecha", noticia.getFechaCreacion());

        if(noticia.getArea()==null)
        {
            i.putExtra("area","");

            i.putExtra("lat",0);
            i.putExtra("lon",0);
        }else
        {
            i.putExtra("area",noticia.getArea().getDescripcion());
            i.putExtra("lat",noticia.getArea().getLat());
            i.putExtra("lon",noticia.getArea().getLon());
        }
        i.putStringArrayListExtra("fotos", noticia.getFotosStringArray());
        i.putExtra("descripcion", noticia.getDescripcion());
        i.putExtra("autor", noticia.getUsuario().getFullName());

        startActivity(i);
    }



    private void getNoticias(int area)
    {
        noAlertas.setVisibility(View.GONE);
        swipeRefresh.setRefreshing(true);
        UserSessionManager session=new UserSessionManager(getActivity());
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.url))
                .build();
        restAdapter.create(ApiRestInterface.class).getAlarmas(
                area,
                Utils.canSeeCerca(session.getNivelUser()),
                mCallBack);
    }

    private Callback<ArrayList<Alarma>> mCallBack=new Callback<ArrayList<Alarma>>() {
        @Override
        public void success(ArrayList<Alarma> r, Response response) {
            //Toast.makeText(getActivity(), "Alarmas Obtenidas", Toast.LENGTH_SHORT).show();

            if(r.size()==0)
                noAlertas.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            swipeRefresh.setRefreshing(false);
            Collections.reverse(r);
            if(getActivity()!=null)
            {
                adapter = new NoticiasAdapter(getActivity(), r,NoticiasFragment.this);
                lista.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        }

        @Override
        public void failure(RetrofitError error) {
            Log.i("LOGIN", error.getMessage());
            Toast.makeText(getActivity(),"Ha habido un problema",Toast.LENGTH_SHORT).show();
        }
    };


    public void setArea(int area) {
        this.area = area;
    }

    public int getArea() {
        return area;
    }



}