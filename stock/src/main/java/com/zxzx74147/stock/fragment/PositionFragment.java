package com.zxzx74147.stock.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.profile.data.UserUniData;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.Position;
import com.zxzx74147.stock.databinding.FragmentPositionBinding;

import java.util.LinkedList;
import java.util.List;

/**
 */
public class PositionFragment extends BaseDialogFragment {

    private FragmentPositionBinding mBinding = null;
    private UserViewModel mUserViewModel = null;

    private List<Position> mData = new LinkedList<>();
    private CommonRecyclerViewAdapter<Position> mAdapter = null;

    public static PositionFragment newInstance() {
        PositionFragment fragment = new PositionFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_position, container, false);
        initView();
        return mBinding.getRoot();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        mUserViewModel = ViewModelProviders.of(ViewUtil.getFragmentActivity(getContext())).get(UserViewModel.class);
        mUserViewModel.getUserUniLiveData().observe(this, userUniData -> {

            refresh();
        });
    }

    private void refresh(){
        UserUniData userUniData = mUserViewModel.getUserUniLiveData().getValue();
        if(userUniData==null||userUniData.hasError()){
            return;
        }
        mBinding.setUserUniData(userUniData);
        mData.clear();
        if(userUniData.positionList!=null&& userUniData.positionList.position!=null) {
            mData.addAll(userUniData.positionList.position);
            mAdapter.notifyDataSetChanged();
        }
    }


    private void initView() {
        mAdapter = new CommonRecyclerViewAdapter<>(mData);
        mAdapter.setMultiTypeDelegate(new CommonMultiTypeDelegate());

        mBinding.list.setAdapter(mAdapter);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.list.setLayoutManager(lm);
    }

}
