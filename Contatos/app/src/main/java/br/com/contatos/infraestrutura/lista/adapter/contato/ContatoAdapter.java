package br.com.contatos.infraestrutura.lista.adapter.contato;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.contatos.R;
import br.com.contatos.modelo.entidade.contato.Contato;

/**
 * Created by JEAN on 23/05/2017.
 */

public class ContatoAdapter extends BaseAdapter {

    private Context contexto;
    private List<Contato> contatos;

    public ContatoAdapter(Context context, List<Contato> pessoas){
        this.contexto = context;
        this.contatos = pessoas;
    }

    @Override
    public int getCount() {
        return contatos.size();
    }

    @Override
    public Object getItem(int position) {
        return contatos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Contato contato = contatos.get(position);


        ViewHolder holder = null;
        if (view == null) {

            view = LayoutInflater.from(contexto).inflate(R.layout.layout_item_contato, null);

            holder = new ViewHolder();
            holder.txtNome = (TextView) view.findViewById(R.id.txtNome);
            holder.txtTelefone = (TextView) view.findViewById(R.id.txtTelefone);

            view.setTag(holder);
        }else{
            holder = (ViewHolder)view.getTag();
        }

        holder.txtNome.setText(contato.getNome());
        holder.txtTelefone.setText(contato.getTelefone());

        return view;
    }

    static class  ViewHolder{
        TextView txtNome;
        TextView txtTelefone;
    }
}
