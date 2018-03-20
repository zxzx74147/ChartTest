package com.zxzx74147.stock.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.devlib.widget.RecycleViewDivider;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.Deffer;
import com.zxzx74147.stock.data.Position;
import com.zxzx74147.stock.databinding.FragmentDefferlistBinding;

import java.util.LinkedList;
import java.util.List;

/**
 */
public class DefferListFragment extends BaseDialogFragment {

    private FragmentDefferlistBinding mBinding = null;


    private List<Deffer> mData = new LinkedList<>();
    private CommonRecyclerViewAdapter<Deffer> mDefferAdapter = null;

    public static DefferListFragment newInstance(IntentData<Position> intentData) {
        DefferListFragment fragment = new DefferListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, intentData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_defferlist, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        initView();

    }


    private void initView() {


        Bundle bundle = getArguments();
        IntentData intentData = (IntentData<Position>) bundle.getSerializable(ZXActivityJumpHelper.INTENT_DATA);
        Position position= (Position) intentData.data;
        if(position.deferredList==null||position.deferredList.deferred==null){
            return;
        }
        mData.addAll(position.deferredList.deferred);
//        mBinding.setData((Position) intentData.data);
        mDefferAdapter.setNewData(mData);
        mBinding.list.setAdapter(mDefferAdapter);
        mBinding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        mBinding.list.addItemDecoration(new RecycleViewDivider(
                getContext(), LinearLayoutManager.HORIZONTAL, getResources().getDimensionPixelOffset(R.dimen.default_gap_1), getResources().getColor(R.color.div_default)));


    }


}
