package com.dronly.kanzhihu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dronly.kanzhihu.R;
import com.dronly.kanzhihu.base.EasyRecyclerViewAdapter;
import com.dronly.kanzhihu.model.MenuItem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gejiahui on 2016/3/13.
 */
public class MenuAdapter extends EasyRecyclerViewAdapter<MenuItem> {

    public MenuAdapter(List<MenuItem> mDatas) {
        super(mDatas);
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);

        return new MenuViewHolder(view);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, MenuItem data) {
        ((MenuViewHolder) viewHolder).title.setText(data.getTitle());
        ((MenuViewHolder) viewHolder).img.setImageResource(data.getResId());
        if (data.isSelected()) {
            ((MenuViewHolder) viewHolder).menuItem.setBackgroundResource(R.color.menu_selected);
        } else {
            ((MenuViewHolder) viewHolder).menuItem.setBackgroundResource(R.color.white);
        }
    }

    class MenuViewHolder extends EasyViewHolder {
        @Bind(R.id.menu_item_title)
        TextView title;
        @Bind(R.id.menu_item_img)
        ImageView img;
        @Bind(R.id.men_item)
        LinearLayout menuItem;

        public MenuViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
