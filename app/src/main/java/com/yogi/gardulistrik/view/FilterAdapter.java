package com.yogi.gardulistrik.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yogi.gardulistrik.R;
import com.yogi.gardulistrik.api.mdl.FilterMdl;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yogi on 13/02/17.
 */
public class FilterAdapter extends
        RecyclerView.Adapter<FilterAdapter.ViewHolder> {

    private static final String TAG = FilterAdapter.class.getSimpleName();

    private Context context;
    private List<FilterMdl> list;
    private OnItemClickListener onItemClickListener;

    public FilterAdapter(Context context, List<FilterMdl> list,
                         OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Todo deklerasiin widget disini
        @BindView(R.id.nama)
        TextView nama;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);


        }

        public void bind(final FilterMdl model,
                         final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getLayoutPosition());

                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_filter, parent, false);


        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FilterMdl item = list.get(position);

        //Todo: Setup viewholder for item
        holder.nama.setText(item.nama);
        holder.bind(item, onItemClickListener);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

}