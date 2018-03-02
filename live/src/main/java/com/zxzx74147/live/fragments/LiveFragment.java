package com.zxzx74147.live.fragments;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxzx74147.devlib.base.BaseFragment;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.live.R;
import com.zxzx74147.live.data.Live;
import com.zxzx74147.live.databinding.FragmentLiveBinding;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * A fragment representing a list of Items.
 */
public class LiveFragment extends BaseFragment {


    private FragmentLiveBinding mBinding = null;
    private Live mLive = null;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LiveFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LiveFragment newInstance(Live live) {
        LiveFragment fragment = new LiveFragment();
        Bundle args = new Bundle();
        args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, live);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_live, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mLive = (Live) getArguments().getSerializable(ZXActivityJumpHelper.INTENT_DATA);
            initData();
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBinding.ijkVideoView.stopPlayback();
        mBinding.ijkVideoView.release(true);
    }

    private void initData() {
        IjkMediaPlayer.loadLibrariesOnce(null);
        mBinding.ijkVideoView.setVideoPath(mLive.rtmpList.rtmp.get(0).url.trim());
        mBinding.ijkVideoView.start();

    }

}
