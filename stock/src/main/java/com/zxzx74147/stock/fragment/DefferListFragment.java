package com.zxzx74147.stock.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.Position;
import com.zxzx74147.stock.databinding.FragmentDefferlistBinding;

/**
 */
public class DefferListFragment extends BaseDialogFragment {

    private FragmentDefferlistBinding mBinding = null;


//    private List<Position> mData = new LinkedList<>();
//    private CommonRecyclerViewAdapter<MachPosition> mMachAdapter = null;

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
//        mBinding.setData((Position) intentData.data);


    }


}
