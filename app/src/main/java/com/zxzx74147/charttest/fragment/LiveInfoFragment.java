package com.zxzx74147.charttest.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxzx74147.charttest.R;
import com.zxzx74147.charttest.databinding.FragmentLiveInfoBinding;
import com.zxzx74147.devlib.base.BaseFragment;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.live.data.Live;

/**
 * A fragment representing a list of Items.
 */
public class LiveInfoFragment extends BaseFragment {


    private FragmentLiveInfoBinding mBinding = null;
    private Live mLive = null;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LiveInfoFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LiveInfoFragment newInstance(Live live) {
        LiveInfoFragment fragment = new LiveInfoFragment();
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_live_info,container,false);
        return mBinding.getRoot();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState){
        if (getArguments() != null) {
            mLive = (Live) getArguments().getSerializable(ZXActivityJumpHelper.INTENT_DATA);
        }
    }




}
