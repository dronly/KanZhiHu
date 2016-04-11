package com.dronly.kanzhihu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dronly.kanzhihu.R;
import com.dronly.kanzhihu.base.EasyRecyclerViewAdapter;
import com.dronly.kanzhihu.model.ThemeColor;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by gejiahui on 2016/3/28.
 */
public class ThemeColorAdapter extends EasyRecyclerViewAdapter<ThemeColor> {


    public ThemeColorAdapter() {

    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme_color, parent, false);
        return new ThemeColorViewHolder(view);
    }

    @Override
    public void onBind(final RecyclerView.ViewHolder viewHolder, final int RealPosition, ThemeColor data) {
        ((ThemeColorViewHolder) viewHolder).them_color.setImageResource(data.getColor());
        if (data.isChosen()) {
            ((ThemeColorViewHolder) viewHolder).chosen.setVisibility(View.VISIBLE);
        } else {
            ((ThemeColorViewHolder) viewHolder).chosen.setVisibility(View.GONE);
        }
    }


    class ThemeColorViewHolder extends EasyViewHolder {
        @Bind(R.id.them_color)
        CircleImageView them_color;
        @Bind(R.id.choose)
        ImageView chosen;

        public ThemeColorViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
