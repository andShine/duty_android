package com.jujinziben.duty.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.jujinziben.duty.R;
import com.jujinziben.duty.adapter.RoomSetAdapter;
import com.jujinziben.duty.adapter.SimpleAdapter;
import com.jujinziben.duty.common.Constants;
import com.jujinziben.duty.common.HomeEvent;
import com.jujinziben.duty.common.RxBus;
import com.jujinziben.duty.common.ViewHolder;
import com.jujinziben.duty.util.GroupUtils;
import com.jujinziben.duty.util.HandlerUtils;
import com.jujinziben.duty.util.InputMethodUtils;
import com.jujinziben.duty.util.PrefUtils;
import com.jujinziben.duty.util.Toastor;

import java.util.ArrayList;
import java.util.List;

/**
 * 工作区域管理
 * Created by liu on 2017/9/8.
 */

public class RoomSetActivity extends AppCompatActivity {

    private RecyclerView rcv;
    private RoomSetAdapter setAdapter;

    public static Intent createIntent(Context context) {
        return new Intent(context, RoomSetActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_set);
        initView();
    }

    private void initView() {
        initToolbar();
        rcv = (RecyclerView) findViewById(R.id.rcv);

        VirtualLayoutManager mLayoutManager = new VirtualLayoutManager(this);
        rcv.setLayoutManager(mLayoutManager);
        DelegateAdapter mDelAdapter = new DelegateAdapter(mLayoutManager, true);

        List<String> rooms = GroupUtils.getListFromStr(PrefUtils.getString(Constants.SAVED_ROOMS));

        setAdapter = new RoomSetAdapter(this, rooms);
        mDelAdapter.addAdapter(setAdapter);

        mDelAdapter.addAdapter(new SimpleAdapter(this, new LinearLayoutHelper(), 1) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if (viewType == 1)
                    return ViewHolder.createViewHolder(RoomSetActivity.this, parent, R.layout.layout_btn);
                return super.onCreateViewHolder(parent, viewType);
            }

            @Override
            public int getItemViewType(int position) {
                return 1;
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                View itemView = holder.itemView;
                TextView tvBtn = (TextView) itemView.findViewById(R.id.tvBtnName);
                tvBtn.setText("添加");
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setAdapter.addData("");
                    }
                });
            }
        });

        rcv.setAdapter(mDelAdapter);
    }

    private void initToolbar() {
        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("工作区域管理");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolBar.inflateMenu(R.menu.menu_save);
        Menu menu = mToolBar.getMenu();
        MenuItem saveItem = menu.findItem(R.id.action);
        MenuItemCompat.setActionView(saveItem, R.layout.menu_save);
        View saveView = MenuItemCompat.getActionView(saveItem);
        saveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodUtils.hideSoftInput(RoomSetActivity.this);
                if (setAdapter.isNotEmpty()) {
                    Toastor.showToast("保存:" + setAdapter.getDatas());
                    // 获得编辑过的分组
                    List<String> nowGroups = setAdapter.getDatas();
                    // 获得当前分组
                    List<String> oriGroups = GroupUtils.getListFromStr(PrefUtils.getString(Constants.SAVED_ROOMS));
                    List<String> tempList1 = new ArrayList<>();
                    List<String> tempList2 = new ArrayList<>();
                    tempList1.addAll(oriGroups);
                    tempList2.addAll(nowGroups);
                    List<String> delList = new ArrayList<>();// 需要删除的集合
                    List<String> addList = new ArrayList<>();// 需要添加的集合

                    tempList2.removeAll(tempList1);
                    if (tempList2.size() == 0) {
                        tempList1.removeAll(nowGroups);
                        delList = tempList1;
                    } else if (tempList2.size() > 0) {
                        oriGroups.removeAll(nowGroups);
                        delList = oriGroups;
                        nowGroups.removeAll(tempList1);
                        addList = nowGroups;
                    }

                    System.out.println("del:" + delList);
                    System.out.println("add:" + addList);

                    // 存入当前分组
                    PrefUtils.putString(Constants.SAVED_ROOMS, GroupUtils.getStrFromList(setAdapter.getDatas()));

                    for (int i = 0; i < delList.size(); i++) {
                        String delRoom = delList.get(i);
                        PrefUtils.clear(Constants.ROOM_PRE + delRoom);
                        PrefUtils.clear(Constants.ROOM_DUTY_NUM + delRoom);
                    }

                    for (int i = 0; i < addList.size(); i++) {
                        String addRoom = addList.get(i);
                        PrefUtils.putString(Constants.ROOM_PRE + addRoom, "");
                        PrefUtils.putInt(Constants.ROOM_DUTY_NUM + addRoom, 1);
                    }

                    Toastor.showToast("保存成功");
                    RxBus.getDefault().post(new HomeEvent().setNeedRefresh(true));
                    HandlerUtils.runOnUiThreadDelay(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 500);
                } else {
                    Toastor.showToast("不能存在空项，请添加内容");
                }
            }
        });
    }
}
