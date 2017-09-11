package com.jujinziben.duty.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.jujinziben.duty.R;
import com.jujinziben.duty.common.SimpleTextWatcher;
import com.jujinziben.duty.common.ViewHolder;
import com.jujinziben.duty.ui.widget.ClearableEditText;
import com.jujinziben.duty.util.GroupUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 办公区域管理
 * Created by liu on 2017/9/8.
 */

public class RoomSetAdapter extends DelegateAdapter.Adapter<ViewHolder> {

    private Context context;
    private List<String> datas;

    public RoomSetAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (0 == viewType) {
            return new ViewHolder(context, LayoutInflater.from(context).inflate(R.layout.item_room_edit, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (null != holder) {
            String name = datas.get(position);
            View itemView = holder.itemView;
            ClearableEditText etEdit = (ClearableEditText) itemView.findViewById(R.id.etRoom);
            etEdit.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    super.onTextChanged(s, start, before, count);
                    String value = "";
                    if (!TextUtils.isEmpty(s)) {
                        value = s.toString();
                    }
                    datas.set(holder.getAdapterPosition(), value);
                }
            });
            etEdit.setText(name);
            ImageView ivDel = (ImageView) itemView.findViewById(R.id.ivDel);
            ivDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datas.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public void addData(String name) {
        this.datas.add(name);
        notifyDataSetChanged();
    }

    public boolean isNotEmpty() {
        boolean flag = true;
        if (this.datas.size() == 0) {
            flag = false;
        } else {
            for (int i = 0; i < this.datas.size(); i++) {
                if (TextUtils.isEmpty(this.datas.get(i))) {
                    flag = false;
                }
            }
        }
        return flag;
    }

    public List<String> getDatas() {
        List<String> nowDatas = new ArrayList<>();
        nowDatas.addAll(this.datas);
        // 去重复
        GroupUtils.removeDuplicate(nowDatas);
        return nowDatas;
    }
}
