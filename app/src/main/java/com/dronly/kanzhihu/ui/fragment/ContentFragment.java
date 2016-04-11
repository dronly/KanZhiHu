package com.dronly.kanzhihu.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dronly.kanzhihu.callBack.LoadResultCallBack;
import com.dronly.kanzhihu.R;
import com.dronly.kanzhihu.adapter.AnswersListAdapter;
import com.dronly.kanzhihu.base.BaseFragment;
import com.dronly.kanzhihu.model.Answer;
import com.orhanobut.logger.Logger;
import com.victor.loading.rotate.RotateLoading;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by gejiahui on 2016/3/11.
 */
public class ContentFragment extends BaseFragment implements LoadResultCallBack {

    private int type = 0;
    private String dateTime;

    @Bind(R.id.swipe_refreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.rotateloading)
    RotateLoading rotateloading;

    public ContentFragment() {
        dateTime = getTodayTime(); //获取今天的日期
    }

    public static ContentFragment getInstance(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ContentFragment getInstance(int type, String time) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("datetime", time);
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    AnswersListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        ButterKnife.bind(this, view);
        rotateloading.start();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt("type", 0);
            dateTime = getArguments().getString("datetime", getTodayTime());
        }
        adapter = new AnswersListAdapter(getActivity(), this);
        adapter.loadDatas(dateTime, type);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.loadDatas(dateTime, type);
            }
        });

    }

    private String getTodayTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 成功响应
     *
     * @param result
     */
    @Override
    public void onSuccess(ArrayList<Answer> result) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setDatas(result);
        mRecyclerView.setAdapter(adapter);
        rotateloading.stop();//停止转动
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);  //刷新成功，停止刷新动画
        }
    }

    /**
     * 失败响应
     *
     * @param failReason
     */
    @Override
    public void onFail(String failReason) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);  //刷新失败，停止刷新动画
        }
        Logger.d(failReason);
        if (failReason.equals("no result")) {
            Logger.d((Integer.parseInt(dateTime) - 1) + "");
            adapter.loadDatas((Integer.parseInt(dateTime) - 1) + "", type);
        } else {
            rotateloading.stop();//停止转动
        }

    }
}
