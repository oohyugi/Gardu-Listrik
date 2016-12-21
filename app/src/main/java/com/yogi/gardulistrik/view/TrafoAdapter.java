package com.yogi.gardulistrik.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yogi.gardulistrik.R;
import com.yogi.gardulistrik.api.mdl.TrafoMdl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yogi on 21/12/16.
 */
public class TrafoAdapter extends
        RecyclerView.Adapter<TrafoAdapter.ViewHolder> {

    private static final String TAG = TrafoAdapter.class.getSimpleName();

    private Context context;
    private List<TrafoMdl> list;
    private OnItemClickListener onItemClickListener;

    public TrafoAdapter(Context context, List<TrafoMdl> list,
                        OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Todo deklerasiin widget disini

        @BindView(R.id.nama_trafo)
        TextView namaTrafo;
        @BindView(R.id.alamat)
        TextView alamat;
        @BindView(R.id.daya)
        TextView daya;
        @BindView(R.id.penyulang)
        TextView penyulang;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);


        }

        public void bind(final TrafoMdl model,
                         final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getLayoutPosition(),model);

                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_data, parent, false);
        ButterKnife.bind(this,view);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TrafoMdl item = list.get(position);

        //Todo: Setup viewholder for item 
        holder.bind(item, onItemClickListener);
        holder.namaTrafo.setText(item.nama_gardu);
        holder.penyulang.setText("Penyulang: "+item.nama_penyulang);
        holder.alamat.setText("Alamat: "+item.alamat);
        holder.daya.setText("Daya: "+item.daya+" Volt");
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position, TrafoMdl model);
    }

}