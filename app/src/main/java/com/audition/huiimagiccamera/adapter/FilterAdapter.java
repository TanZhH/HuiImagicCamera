package com.audition.huiimagiccamera.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.audition.huiimagiccamera.R;
import com.audition.huiimagiccamera.utils.FilterTypeHelper;
import com.seu.magicfilter.filter.helper.MagicFilterType;

/**
 * 创建者：   TANHUIHUI
 * 项  目：   HuiImagicCamera
 * 包  名：   com.audition.huiimagiccamera.adapter
 * 创建日期： 2018/5/8
 * 描  述：
 * @author TANHUIHUI
 */

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterHolder> {

    private MagicFilterType[] filterTypes;
    private Context context;
    private int selected = 0;

    public FilterAdapter(MagicFilterType[] filterTypes, Context context) {
        this.filterTypes = filterTypes;
        this.context = context;
    }

    @Override
    public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.filter_item_layout ,
                parent , false);
        FilterHolder viewHolder = new FilterHolder(view);
        viewHolder.thumbImage = (ImageView) view.findViewById(R.id.filter_thumb_image);
        viewHolder.filterName = (TextView) view.findViewById(R.id.filter_thumb_name);
        viewHolder.filterRoot = (FrameLayout) view.findViewById(R.id.filter_root);
        viewHolder.thumbSelected = (FrameLayout) view.findViewById(R.id.filter_thumb_selected);
        viewHolder.thumbSelected_bg = view.findViewById(R.id.filter_thumb_selected_bg);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterHolder holder, final int position) {
        holder.thumbImage.setImageResource(FilterTypeHelper.FilterType2Thumb(filterTypes[position]));
        holder.filterName.setText(FilterTypeHelper.FilterType2Name(filterTypes[position]));
        holder.filterName.setBackgroundColor(context.getResources().getColor(
                FilterTypeHelper.FilterType2Color(filterTypes[position])));
        if(position == selected){
            holder.thumbSelected.setVisibility(View.VISIBLE);
            holder.thumbSelected_bg.setBackgroundColor(context.getResources()
            .getColor(FilterTypeHelper.FilterType2Color(filterTypes[position])));
            holder.thumbSelected_bg.setAlpha(0.7f);
        }else {
            holder.thumbSelected.setVisibility(View.GONE);
        }
        holder.filterRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected == position){
                    return;
                }
                int lastSelected = selected;
                selected = position;
                notifyItemChanged(lastSelected);
                notifyItemChanged(position);
                onFilterChangListener.onFilterChanged(filterTypes[position]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filterTypes == null? 0 : filterTypes.length;
    }

    class FilterHolder extends RecyclerView.ViewHolder{
        public ImageView thumbImage;
        public TextView filterName;
        public FrameLayout thumbSelected;
        public FrameLayout filterRoot;
        public View thumbSelected_bg;

        public FilterHolder(View itemView){
            super(itemView);
        }
    }

    public interface onFilterChangListener{
        void onFilterChanged(MagicFilterType filterType);
    }

    private onFilterChangListener onFilterChangListener;

    public void setOnFilterChangListener(FilterAdapter.onFilterChangListener onFilterChangListener) {
        this.onFilterChangListener = onFilterChangListener;
    }
}
