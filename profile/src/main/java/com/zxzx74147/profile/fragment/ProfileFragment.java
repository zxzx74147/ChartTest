package com.zxzx74147.profile.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.devlib.widget.SpaceItemDecoration;
import com.zxzx74147.profile.R;
import com.zxzx74147.profile.databinding.LayoutProfileBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 */
public class ProfileFragment extends BaseDialogFragment {

    private LayoutProfileBinding mBinding = null;
    private CommonRecyclerViewAdapter mAdapter = null;
    private List<String> mData = new LinkedList<>();

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_profile, container, false);
        mBinding.setUser(AccountManager.sharedInstance().getUser());
        initView();
        return mBinding.getRoot();
    }

    private void initView(){
        mAdapter = new CommonRecyclerViewAdapter<>(mData);
        CommonMultiTypeDelegate temp = new CommonMultiTypeDelegate();
        temp.registViewType(String.class,R.layout.item_mine);
        mAdapter.setMultiTypeDelegate(temp);
        mBinding.list.setAdapter(mAdapter);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        mBinding.list.setLayoutManager(lm);
        String[] items=getResources().getStringArray(R.array.profile_list);
        mData.addAll(Arrays.asList(items));
    }


}
