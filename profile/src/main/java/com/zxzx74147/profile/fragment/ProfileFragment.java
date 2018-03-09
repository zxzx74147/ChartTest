package com.zxzx74147.profile.fragment;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.devlib.modules.busstation.ProfileBusStation;
import com.zxzx74147.devlib.modules.sys.SysInitManager;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.WebviewUtil;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.devlib.widget.SpaceItemDecoration;
import com.zxzx74147.profile.R;
import com.zxzx74147.profile.data.UserUniData;
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
    private UserViewModel mUserModelView = null;

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_profile, container, false);
        mUserModelView = ViewModelProviders.of(getActivity()).get(UserViewModel.class);

        initView();
        initData();
        return mBinding.getRoot();
    }

    private void initData() {
        mBinding.setUserUni(mUserModelView.getUserUniLiveData().getValue());
        mUserModelView.getUserUniLiveData().observe(ProfileFragment.this, new Observer<UserUniData>() {
            @Override
            public void onChanged(@Nullable UserUniData userUniData) {
                if(userUniData.hasError()){
                    return;
                }

                mBinding.setUserUni(userUniData);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mUserModelView!=null) {
            mUserModelView.getUserUniLiveData().doRefresh();
        }
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

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position){
                    case 0:
                        break;
                    case 1:
                        WebviewUtil.showWebActivity(getActivity(), SysInitManager.sharedInstance().getSysInitData().config.userGuideUrl);
                        break;
                    case 2:
                        ProfileBusStation.startMessageCenter(getActivity());
                        break;
                    case 3:
                        break;
                }
            }
        });
    }


}
