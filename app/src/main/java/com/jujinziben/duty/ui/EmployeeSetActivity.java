package com.jujinziben.duty.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.jujinziben.duty.R;
import com.jujinziben.duty.adapter.SimpleAdapter;
import com.jujinziben.duty.common.CommonDelegateAdapter;
import com.jujinziben.duty.common.Constants;
import com.jujinziben.duty.common.HomeEvent;
import com.jujinziben.duty.common.RxBus;
import com.jujinziben.duty.common.ViewHolder;
import com.jujinziben.duty.util.GroupUtils;
import com.jujinziben.duty.util.InputMethodUtils;
import com.jujinziben.duty.util.PrefUtils;
import com.jujinziben.duty.util.Toastor;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.List;

public class EmployeeSetActivity extends AppCompatActivity {

    private String roomName;
    private RecyclerView rcv;
    private CommonDelegateAdapter<String> adapter;
    private List<String> employeeNames;
    private int dutyNum;

    public static Intent createIntent(Context context, String roomName) {
        Intent intent = new Intent(context, EmployeeSetActivity.class);
        intent.putExtra("roomName", roomName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initData();
        initToolbar();
        initView();
    }

    private void initData() {
        roomName = getIntent().getStringExtra("roomName");
        employeeNames = GroupUtils.getListFromStr(PrefUtils.getString(Constants.ROOM_PRE + roomName));
        dutyNum = PrefUtils.getInt(Constants.ROOM_DUTY_NUM + roomName);
    }

    private void initView() {
        rcv = (RecyclerView) findViewById(R.id.rcv);
        VirtualLayoutManager mLayoutManager = new VirtualLayoutManager(this);
        rcv.setLayoutManager(mLayoutManager);
        DelegateAdapter mDelAdapter = new DelegateAdapter(mLayoutManager, true);

        adapter = new CommonDelegateAdapter<String>(this, R.layout.item_employee, employeeNames) {

            @Override
            public LayoutHelper onCreateLayoutHelper() {
                return new LinearLayoutHelper();
            }

            @Override
            protected void convert(final ViewHolder holder, String s, int position) {
                if (!TextUtils.isEmpty(s)) {
                    View itemView = holder.itemView;
                    TextView tvSort = (TextView) itemView.findViewById(R.id.tvSortNum);
                    int posi = position + 1;
                    String showPosi;
                    if (posi < 10) {
                        showPosi = "0" + posi;
                    } else {
                        showPosi = posi + "";
                    }
                    tvSort.setText(showPosi);
                    if (position < dutyNum) {
                        // 值日
                        tvSort.setTextColor(Color.parseColor("#43CD80"));
                    } else if (position < dutyNum * 2 && position >= dutyNum) {
                        // 下次值日
                        tvSort.setTextColor(Color.parseColor("#ffd600"));
                    } else {
                        // 不值日
                        tvSort.setTextColor(Color.parseColor("#9e9e9e"));
                    }
                    TextView tvName = (TextView) itemView.findViewById(R.id.tvName);
                    tvName.setText(s);
                    // 删除
                    ImageView ivDel = (ImageView) itemView.findViewById(R.id.ivArrow);
                    ivDel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new QMUIDialog.MessageDialogBuilder(EmployeeSetActivity.this)
                                    .setTitle("提示")
                                    .setMessage("确定要删除吗？")
                                    .addAction("取消", new QMUIDialogAction.ActionListener() {
                                        @Override
                                        public void onClick(QMUIDialog dialog, int index) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .addAction(0, "删除", QMUIDialogAction.ACTION_PROP_NEGATIVE, new QMUIDialogAction.ActionListener() {
                                        @Override
                                        public void onClick(QMUIDialog dialog, int index) {
                                            employeeNames.remove(holder.getAdapterPosition());
                                            notifyDataSetChanged();
                                            PrefUtils.putString(Constants.ROOM_PRE + roomName, GroupUtils.getStrFromList(employeeNames));
                                            RxBus.getDefault().post(new HomeEvent().setNeedRefresh(true));
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    });
                }
            }
        };
        adapter.setOnItemClickListener(new CommonDelegateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, final int position) {
                if (position < dutyNum) {
                    final String emplName = employeeNames.get(position);
                    final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(EmployeeSetActivity.this);
                    builder.setTitle("请输入外出天数（" + emplName + "）")
                            .setInputType(InputType.TYPE_CLASS_NUMBER)
                            .addAction("取消", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    InputMethodUtils.hideSoftInput(builder.getEditText());
                                    dialog.dismiss();
                                }
                            })
                            .addAction("确定", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    InputMethodUtils.hideSoftInput(builder.getEditText());
                                    CharSequence text = builder.getEditText().getText();
                                    if (text != null && text.length() > 0) {
                                        int toPosi = dutyNum - position + Integer.parseInt(text.toString()) * dutyNum - dutyNum;
                                        if (position + toPosi > employeeNames.size() - 1) {
                                            Toastor.showToast("目前只能放入队尾");
                                            toPosi = employeeNames.size() - position - 1;
                                        }
                                        GroupUtils.displace(employeeNames, emplName, toPosi);
                                        adapter.notifyDataSetChanged();
                                        List<String> datas = adapter.getDatas();
                                        PrefUtils.putString(Constants.ROOM_PRE + roomName, GroupUtils.getStrFromList(datas));
                                        RxBus.getDefault().post(new HomeEvent().setNeedRefresh(true));

                                        dialog.dismiss();
                                    } else {
                                        Toast.makeText(EmployeeSetActivity.this, "请先填入外出天数", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            })
                            .show();
                }
            }
        });
        mDelAdapter.addAdapter(adapter);

        mDelAdapter.addAdapter(new SimpleAdapter(this, new LinearLayoutHelper(), 1) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                if (viewType == 1)
                    return ViewHolder.createViewHolder(EmployeeSetActivity.this, parent, R.layout.layout_btn);
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
                        showEditTextDialog();
                    }
                });
            }
        });

        rcv.setAdapter(mDelAdapter);
    }

    private void showEditTextDialog() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(this);
        builder.setTitle("添加新员工")
                .setPlaceholder("在此输入姓名")
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        InputMethodUtils.hideSoftInput(builder.getEditText());
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        InputMethodUtils.hideSoftInput(builder.getEditText());
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            adapter.addData(text.toString());
                            List<String> datas = adapter.getDatas();
                            PrefUtils.putString(Constants.ROOM_PRE + roomName, GroupUtils.getStrFromList(datas));
                            RxBus.getDefault().post(new HomeEvent().setNeedRefresh(true));
                            dialog.dismiss();
                        } else {
                            Toast.makeText(EmployeeSetActivity.this, "请填入姓名", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }

    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(roomName + "名单");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.inflateMenu(R.menu.menu_save);
        Menu menu = mToolbar.getMenu();
        MenuItem saveItem = menu.findItem(R.id.action);
        MenuItemCompat.setActionView(saveItem, R.layout.menu_duty_num);
        View saveView = MenuItemCompat.getActionView(saveItem);
        saveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditNumDialog();
            }
        });
    }

    private void showEditNumDialog() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(this);
        int curreyNum = PrefUtils.getInt(Constants.ROOM_DUTY_NUM + roomName, 1);
        String curretInfo = "当前设置：" + curreyNum + "人";
        builder.setTitle("请输入每天值日的人数")
                .setPlaceholder(curretInfo)
                .setInputType(InputType.TYPE_CLASS_NUMBER)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        InputMethodUtils.hideSoftInput(builder.getEditText());
                        dialog.dismiss();
                    }
                })
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        InputMethodUtils.hideSoftInput(builder.getEditText());
                        CharSequence text = builder.getEditText().getText();
                        if (text != null && text.length() > 0) {
                            PrefUtils.putInt(Constants.ROOM_DUTY_NUM + roomName, Integer.parseInt(text.toString()));
                            RxBus.getDefault().post(new HomeEvent().setNeedRefresh(true));
                            initData();
                            initView();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(EmployeeSetActivity.this, "请填入值日人数", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }
}
