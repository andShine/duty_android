package com.jujinziben.duty.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.jujinziben.duty.R;
import com.jujinziben.duty.common.CommonDelegateAdapter;
import com.jujinziben.duty.common.Constants;
import com.jujinziben.duty.common.HomeEvent;
import com.jujinziben.duty.common.RxBus;
import com.jujinziben.duty.common.ViewHolder;
import com.jujinziben.duty.util.ClipboardUtils;
import com.jujinziben.duty.util.DisplayUtils;
import com.jujinziben.duty.util.GroupUtils;
import com.jujinziben.duty.util.InputMethodUtils;
import com.jujinziben.duty.util.PrefUtils;
import com.jujinziben.duty.util.Toastor;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRcv;
    private TextView mTvMemo;
    private TextView mTvNextGroup;
    private RelativeLayout mRlEditGroup;
    private TextView mTvDutyMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle(getResources().getString(R.string.app_name));
        mRcv = (RecyclerView) findViewById(R.id.rcv);
        mTvMemo = (TextView) findViewById(R.id.tvMemo);
        mTvNextGroup = (TextView) findViewById(R.id.tvNextGroup);
        mRlEditGroup = (RelativeLayout) findViewById(R.id.rlEditGroup);
        mTvDutyMemo = (TextView) findViewById(R.id.tvDutyMemo);
    }

    private void initEvent() {
        mTvMemo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipboardUtils.copyToClipboard(MainActivity.this, ((TextView) v).getText().toString());
                Toastor.showToast("复制值日信息成功");
                return true;
            }
        });
        mTvNextGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextGroup();
            }
        });
        mRlEditGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RoomSetActivity.createIntent(MainActivity.this));
            }
        });
        RxBus.getDefault().toObservable(HomeEvent.class)
                .subscribe(new Action1<HomeEvent>() {
                    @Override
                    public void call(HomeEvent homeEvent) {
                        if (homeEvent.isNeedRefresh()) {
                            initData();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void setNextGroup() {
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(this);
        builder.setTitle("请先输入日期或标记")
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
                            mTvDutyMemo.setText(text.toString());
                            PrefUtils.putString(Constants.DUTY_TAG, text.toString());
                            // 获取下组数据
                            List<String> rooms = GroupUtils.getListFromStr(PrefUtils.getString(Constants.SAVED_ROOMS));
                            for (int i = 0; i < rooms.size(); i++) {
                                String roomName = rooms.get(i);
                                int dutyNum = PrefUtils.getInt(Constants.ROOM_DUTY_NUM + roomName);
                                String oldGroup = PrefUtils.getString(Constants.ROOM_PRE + roomName);
                                List<String> nowGroupList = GroupUtils.getListFromStr(oldGroup);
                                List<String> dutyList = GroupUtils.getGroup(nowGroupList, dutyNum);
                                String newGroup = GroupUtils.resetGroup(oldGroup, GroupUtils.getStrFromList(dutyList));
                                // 存入重置后的分组
                                PrefUtils.putString(Constants.ROOM_PRE + roomName, newGroup);
                            }
                            // 重新载入数据
                            initData();
                            dialog.dismiss();
                        } else {
                            Toast.makeText(MainActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();
    }

    private void initData() {
        initDefalutData();

        mTvDutyMemo.setText(PrefUtils.getString(Constants.DUTY_TAG));

        List<String> rooms = GroupUtils.getListFromStr(PrefUtils.getString(Constants.SAVED_ROOMS));
        // 值日信息
        String dutyInfo = "";
        for (int i = 0; i < rooms.size(); i++) {
            // 当前房间名
            String roomName = rooms.get(i);
            // 当前房间所有的人数
            List<String> allEmployees = GroupUtils.getListFromStr(PrefUtils.getString(Constants.ROOM_PRE + roomName));
            // 当前值日人数
            int dutyNum = PrefUtils.getInt(Constants.ROOM_DUTY_NUM + roomName);
            System.out.println("allEmp:" + allEmployees);
            System.out.println("dutyNum:" + dutyNum);
            // 当前值日名单
            String dutyNames = GroupUtils.getStrFromList(GroupUtils.getGroup(allEmployees, dutyNum));
            if (TextUtils.isEmpty(dutyNames)) {
                dutyNames = "暂无名单";
            }
            if (i == 0) {
                dutyInfo = roomName + ": " + dutyNames;
            } else {
                dutyInfo = dutyInfo + "\n" + roomName + ": " + dutyNames;
            }
        }
        mTvMemo.setText(dutyInfo);

        // 值班室信息
        List<Content> groups = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            groups.add(new Content(rooms.get(i), GroupUtils.getListFromStr(PrefUtils.getString(Constants.ROOM_PRE + rooms.get(i))).size()));
        }

        VirtualLayoutManager mLayoutManager = new VirtualLayoutManager(this);
        mRcv.setLayoutManager(mLayoutManager);
        DelegateAdapter mDelAdapter = new DelegateAdapter(mLayoutManager, true);
        final CommonDelegateAdapter adapter = new CommonDelegateAdapter<Content>(this, R.layout.item_group, groups) {

            @Override
            public LayoutHelper onCreateLayoutHelper() {
                GridLayoutHelper mHelper = new GridLayoutHelper(2);
                mHelper.setAutoExpand(false);
                int margin = DisplayUtils.dp2Px(10);
                mHelper.setMargin(margin, 0, 0, margin);
                return mHelper;
            }

            @Override
            protected void convert(ViewHolder holder, Content content, int position) {
                if (null != content) {
                    View itemView = holder.itemView;
                    TextView memo = (TextView) itemView.findViewById(R.id.tvName);
                    memo.setText(content.name);
                    TextView num = (TextView) itemView.findViewById(R.id.tvNum);
                    num.setText("(" + content.num + "人)");
                }
            }
        };
        adapter.setOnItemClickListener(new CommonDelegateAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                List<Content> datas = adapter.getDatas();
                startActivity(EmployeeSetActivity.createIntent(MainActivity.this, datas.get(position).name));
            }
        });
        mDelAdapter.addAdapter(adapter);
        mRcv.setAdapter(mDelAdapter);
    }

    private void initDefalutData() {
        if (TextUtils.isEmpty(PrefUtils.getString(Constants.SAVED_ROOMS))) {
            // 初始化房间
            PrefUtils.putString(Constants.SAVED_ROOMS, "170,165,164");
            // 初始化人员名单
            PrefUtils.putString(Constants.ROOM_PRE + "170", "杨倩,连振国,夏开放,王庆华,边振亚,祝丹丹,孙晓红,娄爽皙,王霞琴,闻浩,李艳珂,姚广磊,骆杰,吴聪辉,陈金宇,袁新军,张国旗");
            PrefUtils.putString(Constants.ROOM_PRE + "165", "鲁克,杨阳,姬彤辉,刘文静,李和,王攀,张英,杨阁阁,吴旭航,袁慧慧,马江芳,倪行桂");
            PrefUtils.putString(Constants.ROOM_PRE + "164", "王桦伟,翟冬琴,段浩,丁艳祥,周鹏飞,闫刚,邓国忠,刘耀耀,李婷,张博达,王旭,韩福特,杜承祯");

            PrefUtils.putInt(Constants.ROOM_DUTY_NUM + "170", 4);
            PrefUtils.putInt(Constants.ROOM_DUTY_NUM + "165", 3);
            PrefUtils.putInt(Constants.ROOM_DUTY_NUM + "164", 2);

            PrefUtils.putString(Constants.DUTY_TAG, "周一");
        }
    }

    private class Content {
        String name;
        int num;

        Content(String name, int num) {
            this.name = name;
            this.num = num;
        }
    }
}
