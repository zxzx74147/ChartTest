package com.zxzx74147.stock.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.support.design.widget.RxTabLayout;
import com.jakewharton.rxbinding2.support.design.widget.TabLayoutSelectionEvent;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.profile.data.UserUniData;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.MachPosition;
import com.zxzx74147.stock.data.Position;
import com.zxzx74147.stock.databinding.FragmentPositionBinding;

import java.util.LinkedList;

import io.reactivex.functions.Consumer;

/**
 */
public class PositionFragment extends BaseDialogFragment {

    private FragmentPositionBinding mBinding = null;
    private UserViewModel mUserViewModel = null;

    private CommonRecyclerViewAdapter<Position> mPositionAdapter = null;
    private CommonRecyclerViewAdapter<MachPosition> mMachAdapter = null;

//    private List<Position> mData = new LinkedList<>();
//    private CommonRecyclerViewAdapter<MachPosition> mMachAdapter = null;

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
            refresh(userUniData);
        });
    }

    private void refresh(UserUniData userdata) {
        TabLayout.Tab tab1 = mBinding.tabLayout2.getTabAt(0);
        tab1.setText(String.format(getString(R.string.format_my_position), userdata.positionList.num));
        TabLayout.Tab tab2 = mBinding.tabLayout2.getTabAt(0);
        tab2.setText(String.format(getString(R.string.format_my_machposition), userdata.machPositionList.num));


        UserUniData userUniData = mUserViewModel.getUserUniLiveData().getValue();
        if (userUniData == null || userUniData.hasError()) {
            return;
        }
        if (userUniData.positionList != null && userUniData.positionList.position != null) {
            mPositionAdapter.setNewData(userUniData.positionList.position);
        }
        if (userUniData.machPositionList != null && userUniData.machPositionList.machPosition != null) {
            mMachAdapter.setNewData(userUniData.machPositionList.machPosition);
        }
        mBinding.setUserUniData(userUniData);
    }


    private void initView() {
        RxTabLayout.selectionEvents(mBinding.tabLayout2).subscribe(new Consumer<TabLayoutSelectionEvent>() {
            @Override
            public void accept(TabLayoutSelectionEvent tabLayoutSelectionEvent) throws Exception {

            }
        });
        mPositionAdapter = new CommonRecyclerViewAdapter<>(new LinkedList<>());
        mMachAdapter = new CommonRecyclerViewAdapter<>(new LinkedList<>());
        CommonMultiTypeDelegate delegate = new CommonMultiTypeDelegate();
        mPositionAdapter.setMultiTypeDelegate(delegate);
        mMachAdapter.setMultiTypeDelegate(delegate);
        mBinding.list.setAdapter(mPositionAdapter);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.list.setLayoutManager(lm);
    }

}
