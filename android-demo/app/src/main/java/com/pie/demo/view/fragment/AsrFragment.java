package com.pie.demo.view.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.acu.pie.model.AsrProduct;
import com.pie.demo.Constants;
import com.pie.demo.R;
import com.pie.demo.base.BaseFragment;
import com.pie.demo.manager.RecoringManaegerInterfaceOne;
import com.pie.demo.manager.RecoringManaegerInterfaceThree;
import com.pie.demo.manager.RecoringManaegerInterfaceTwo;
import com.pie.demo.manager.RecoringManeger;
import com.pie.demo.utils.SpUtils;
import com.pie.demo.view.activity.SettingActivity;
import com.pie.demo.view.weiget.VoiceImgView;

public class AsrFragment extends BaseFragment {

    private Toolbar mToolbar;
    private VoiceImgView vivButton;
    private LinearLayout mRlOne, mRlTwo, mRlThree;
    private TextView mTvOne, mTvTwo, mTvThree;
    private TextView mTvOneError, mTvTwoError, mTvThreeError;
    private TextView ttvText1, ttvText2, ttvText3;

    private RecoringManeger recoringManeger;

    private StringBuilder s1 = null;
    private StringBuilder s2 = null;
    private StringBuilder s3 = null;
    /**
     * 记录关闭了几个client，最多3个
     */
    private int stopThread = 0;
    /**
     * 模型
     */
    private AsrProduct[] values;
    /**
     * 初始化第一个客户端，默认使用 客服模型：金融领域
     */
    private int defaultAsr = 2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(mActivity, R.layout.fragment_asr, null);

        initView(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.recoringManeger = RecoringManeger.getInstance();
        this.values = AsrProduct.values();
        initLisener();
    }

    private void initView(View view) {

        mToolbar = view.findViewById(R.id.mToolbar);
        vivButton = view.findViewById(R.id.vivButton);
        mRlOne = view.findViewById(R.id.mRlOne);
        mTvOne = view.findViewById(R.id.mTvOne);
        mTvOneError = view.findViewById(R.id.mTvOneError);
        ttvText1 = view.findViewById(R.id.ttvText1);

        mRlTwo = view.findViewById(R.id.mRlTwo);
        mTvTwo = view.findViewById(R.id.mTvTwo);
        mTvTwoError = view.findViewById(R.id.mTvTwoError);
        ttvText2 = view.findViewById(R.id.ttvText2);

        mRlThree = view.findViewById(R.id.mRlThree);
        mTvThree = view.findViewById(R.id.mTvThree);
        mTvThreeError = view.findViewById(R.id.mTvThreeError);
        ttvText3 = view.findViewById(R.id.ttvText3);
    }

    private void initLisener() {
        /**
         * button监听
         */
        vivButton.setOnVoiceButtonInterface(new VoiceImgView.onVoiceButtonInterface() {
            @Override
            public void onStartVoice() {
                s1 = new StringBuilder();
                s2 = new StringBuilder();
                s3 = new StringBuilder();

                mTvOneError.setVisibility(View.GONE);
                mTvTwoError.setVisibility(View.GONE);
                mTvThreeError.setVisibility(View.GONE);

                ttvText1.setText("");
                ttvText2.setText("");
                ttvText3.setText("");

                recoringManeger.startRecord();
            }

            @Override
            public void onStopVoice() {
                recoringManeger.stopRecord();

                stopThread = 0;
            }
        });

        mTvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecord();
                Intent intent = new Intent(mActivity, SettingActivity.class);
                intent.putExtra("flag", "one");
                startActivity(intent);
            }
        });
        mTvTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecord();
                Intent intent = new Intent(mActivity, SettingActivity.class);
                intent.putExtra("flag", "two");
                startActivity(intent);
            }
        });
        mTvThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecord();
                Intent intent = new Intent(mActivity, SettingActivity.class);
                intent.putExtra("flag", "three");
                startActivity(intent);
            }
        });

        RecoringManeger.getInstance().setRecoringManaegerInterfaceOne(new RecoringManaegerInterfaceOne() {
            @Override
            public void onNext(final String result, final Boolean completed) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(result)) {
                            if (completed) {
                                s1.append(result);
                                ttvText1.setText(s1);
                            } else {
                                ttvText1.setText(s1.toString() + result);
                            }
                        }
                    }
                });
            }

            @Override
            public void onError(final String message) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvOneError.setVisibility(View.VISIBLE);
                        mTvOneError.setText(message);

                        errorStopVoice();
                    }
                });
            }
        });

        RecoringManeger.getInstance().setRecoringManaegerInterfaceTwo(new RecoringManaegerInterfaceTwo() {
            @Override
            public void onNext(final String result, final Boolean completed) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(result)) {
                            if (completed) {
                                s2.append(result);
                                ttvText2.setText(s2);
                            } else {
                                ttvText2.setText(s2.toString() + result);
                            }
                        }
                    }
                });
            }

            @Override
            public void onError(final String message) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvTwoError.setVisibility(View.VISIBLE);
                        mTvTwoError.setText(message);

                        errorStopVoice();
                    }
                });
            }
        });

        RecoringManeger.getInstance().setRecoringManaegerInterfaceThree(new RecoringManaegerInterfaceThree() {
            @Override
            public void onNext(final String result, final Boolean completed) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!TextUtils.isEmpty(result)) {
                            if (completed) {
                                s3.append(result);
                                ttvText3.setText(s3);
                            } else {
                                ttvText3.setText(s3.toString() + result);
                            }
                        }
                    }
                });
            }

            @Override
            public void onError(final String message) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTvThreeError.setVisibility(View.VISIBLE);
                        mTvThreeError.setText(message);

                        errorStopVoice();
                    }
                });
            }
        });
    }

    public void isRecord() {
        boolean record = RecoringManeger.getInstance().isRecord();
        if (record) {
            vivButton.stopAnim();
            RecoringManeger.getInstance().stopRecord();
            stopThread = 0;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                if (mRlOne.getVisibility() == View.GONE) {
                    mRlOne.setVisibility(View.VISIBLE);
                    mTvOne.setText("请去设置ip和post");
                } else if (mRlTwo.getVisibility() == View.GONE) {
                    mRlTwo.setVisibility(View.VISIBLE);
                    mTvTwo.setText("请去设置ip和post");
                } else if (mRlThree.getVisibility() == View.GONE) {
                    mRlThree.setVisibility(View.VISIBLE);
                    mTvThree.setText("请去设置ip和post");
                }
                break;
            case R.id.action_delete:
                if (mRlThree.getVisibility() == View.VISIBLE) {
                    mRlThree.setVisibility(View.GONE);
                    SpUtils.getInstance().putString(Constants.THREEADDRESS, null);
                    SpUtils.getInstance().putString(Constants.THREEPORT, null);
                    ttvText3.setText("");
                } else if (mRlTwo.getVisibility() == View.VISIBLE) {
                    mRlTwo.setVisibility(View.GONE);
                    SpUtils.getInstance().putString(Constants.TWOADDRESS, null);
                    SpUtils.getInstance().putString(Constants.TWOPORT, null);
                    ttvText2.setText("");
                }
                break;
            case R.id.action_set:
                String hz = SpUtils.getInstance().getString(Constants.SAMPLERATEINHZ);
                if (TextUtils.isEmpty(hz)) {
                    checkItem = 0;
                } else {
                    switch (hz) {
                        case "8000":
                            checkItem = 0;
                            break;
                        case "16000":
                            checkItem = 1;
                            break;

                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                builder.setTitle("请选择采样率")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SpUtils.getInstance().putString(Constants.SAMPLERATEINHZ, items[checkItem]);
                            }
                        })
                        .setSingleChoiceItems(items, checkItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                checkItem = which;
                            }
                        })
                        .show();


                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private int checkItem = -1;
    private String items[] = new String[]{"8000", "16000"};

    @Override
    public void onStart() {
        super.onStart();
        /**
         * 初始化AsrClient配置
         */
        String oneAddress = SpUtils.getInstance().getString(Constants.ONEADDRESS);
        String onePort = SpUtils.getInstance().getString(Constants.ONEPORT);
        int oneAsr = SpUtils.getInstance().getInt(Constants.ONEASRPRODUCT);
        String twoAddress = SpUtils.getInstance().getString(Constants.TWOADDRESS);
        String twoPort = SpUtils.getInstance().getString(Constants.TWOPORT);
        int twoAsr = SpUtils.getInstance().getInt(Constants.TWOASRPRODUCT);
        String threeAddress = SpUtils.getInstance().getString(Constants.THREEADDRESS);
        String threePort = SpUtils.getInstance().getString(Constants.THREEPORT);
        int threeAsr = SpUtils.getInstance().getInt(Constants.THREEASRPRODUCT);

        Log.e("tag", oneAddress + "::" + onePort);

        if (TextUtils.isEmpty(oneAddress) && TextUtils.isEmpty(onePort) && oneAsr == -1) {

            defaultAsr = AsrProduct.CUSTOMER_SERVICE_FINANCE.ordinal();

            Log.e("tag", "def:" + defaultAsr);

            SpUtils.getInstance().putString(Constants.ONEADDRESS, Constants.SERVER_IP_ADDR);
            SpUtils.getInstance().putString(Constants.ONEPORT, Constants.SERVER_IP_PORT + "");
            SpUtils.getInstance().putInt(Constants.ONEASRPRODUCT, defaultAsr);
            mRlOne.setVisibility(View.VISIBLE);
            mTvOne.setText("address:" + Constants.SERVER_IP_ADDR + "   port:" + Constants.SERVER_IP_PORT + "  " + values[defaultAsr].getName());
        } else {
            mRlOne.setVisibility(View.VISIBLE);
            mTvOne.setText("address:" + oneAddress + "   port:" + onePort + "  " + values[oneAsr].getName());
        }

        if (!TextUtils.isEmpty(twoAddress) && !TextUtils.isEmpty(twoPort)) {
            mRlTwo.setVisibility(View.VISIBLE);
            mTvTwo.setText("address:" + twoAddress + "   port:" + twoPort + "  " + values[twoAsr].getName());
        }
        if (!TextUtils.isEmpty(threeAddress) && !TextUtils.isEmpty(threePort)) {
            mRlThree.setVisibility(View.VISIBLE);
            mTvThree.setText("address:" + threeAddress + "   port:" + threePort + "  " + values[threeAsr].getName());
        }
    }

    private void errorStopVoice() {
        stopThread++;
        if (stopThread == RecoringManeger.getInstance().getHavaThread()) {
            vivButton.stopAnim();
            RecoringManeger.getInstance().stopRecord();
            stopThread = 0;
        }
    }

}
