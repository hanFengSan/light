package com.yakami.light.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.trello.rxlifecycle.FragmentEvent;
import com.yakami.light.R;
import com.yakami.light.event.Event;
import com.yakami.light.event.RxBus;
import com.yakami.light.view.fragment.base.BaseFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author Yakami, Created on 2016/4/23
 */
public class LoadFragment extends BaseFragment {

    @Bind(R.id.load_layout) RelativeLayout mLoadLayout;
    @Bind(R.id.view_content) FrameLayout mContentLayout;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.loading) TextView mTextLoading;
    @Bind(R.id.load_failed) TextView mTextFailed;

    private BaseFragment mFragment;
    private OnLoadingListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle) {
        View view = inflater.inflate(R.layout.fragment_load, container, false);
        ButterKnife.bind(this, view);

        //接收加载完成/失败事件
        RxBus.with(this)
                .setEvent(Event.EventType.LOAD_ERROR)
                .setEndEvent(FragmentEvent.DESTROY)
                .onNext(event -> {
                    switchLoadState(false);
                }).create();
        RxBus.with(this)
                .setEvent(Event.EventType.LOADED_SUCCESSFUL)
                .setEndEvent(FragmentEvent.DESTROY)
                .onNext(event -> {
                    showFragment();
                }).create();
        //加载内容fragment
        mContentLayout.post(() -> {
            getChildTransaction().replace(R.id.view_content, mFragment, "VIEW_CONTENT").commit();
        });

        return view;
    }

    private void switchLoadState(boolean open) {
        mProgressBar.setVisibility(open ? View.VISIBLE : View.GONE);
        mTextLoading.setVisibility(open ? View.VISIBLE : View.GONE);
        mTextFailed.setVisibility(open ? View.GONE : View.VISIBLE);
    }

    private void showFragment() {
        mLoadLayout.setVisibility(View.GONE);
        mContentLayout.setVisibility(View.VISIBLE);
//        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.view_content, mFragment, "view_content").commit();
    }

    public void setFragment(BaseFragment fragment) {
        mFragment = fragment;
    }

    public BaseFragment getFragment() {
        return mFragment;
    }

    public void run() {
        mListener.onLoading();
    }

    public void setOnLoadingListener(OnLoadingListener listener) {
        mListener = listener;
    }

    public interface OnLoadingListener {
        void onLoading();
    }

}
