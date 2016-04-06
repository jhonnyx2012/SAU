package uneg.software.sau.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uneg.software.sau.R;
import uneg.software.sau.fragments.NoticiasFragment;
import uneg.software.sau.interfaces.RecyclerItemClickListener;
import uneg.software.sau.models.Alarma;
import uneg.software.sau.models.Noticia;

/**
 * Created by Jhonny on 18/8/2015.
 */
public class NoticiasAdapter extends RecyclerView.Adapter<NoticiasAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final ArrayList<Alarma> lista;
    private final Activity activity;
    private final RecyclerItemClickListener clickListener;

    public NoticiasAdapter(Activity activity, ArrayList<Alarma> lista, NoticiasFragment f) {
        this.clickListener=f;
        this.inflater = LayoutInflater.from(activity);
        this.lista = lista;
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = inflater.inflate(R.layout.item_noticia, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Alarma itm = lista.get(position);
        holder.descripcion.setText(itm.getDescripcion());
        String area=itm.getArea()!=null?itm.getArea().getDescripcion():"";
        holder.area.setText(area);
        holder.autor.setText(itm.getUsuario().getFullName());
        holder.fecha.setText(itm.getFechaCreacion());

        holder.setItem(itm);
        if(itm.getArea()==null)
            holder.area.setVisibility(View.GONE);
        else
            holder.area.setVisibility(View.VISIBLE);

        if(itm.getFirstFotoUrl().isEmpty()){
            holder.foto.setVisibility(View.GONE);}
        else
        {
            holder.foto.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(activity.getString(R.string.url)+itm.getFirstFotoUrl())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.foto);
        }

        holder.itemView.setTag(itm);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @InjectView(R.id.area)
        TextView area;
        @InjectView(R.id.autor)
        TextView autor;
        @InjectView(R.id.fecha)
        TextView fecha;
        @InjectView(R.id.descripcion)
        TextView descripcion;
        @InjectView(R.id.foto)
        ImageView foto;
        private Alarma item;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void setItem(Alarma item) {
            this.item = item;
        }

        public Alarma getItem() {
            return item;
        }


        @Override
        public void onClick(View v) {
            clickListener.onItemClick(item);
        }
    }
}
