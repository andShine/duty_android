package com.jujinziben.duty.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.jujinziben.duty.R;
import com.jujinziben.duty.common.ViewHolder;

/**
 * Created by liu on 2017/9/8.
 */

public class SimpleAdapter extends DelegateAdapter.Adapter<ViewHolder>{

    private Context context;
    private LayoutHelper helper;
    private int count;

    public SimpleAdapter(Context context, LayoutHelper helper, int count) {
        this.context = context;
        this.helper = helper;
        this.count = count;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return helper;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(context, LayoutInflater.from(context).inflate(R.layout.item_base, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return count;
    }
}
