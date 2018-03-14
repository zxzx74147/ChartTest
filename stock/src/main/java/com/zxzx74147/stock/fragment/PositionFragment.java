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
import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.interfaces.CommonListRequestCallback;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.RecyclerViewUtil;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.profile.data.UserUniData;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.MachPosition;
import com.zxzx74147.stock.data.MachPositionListData;
import com.zxzx74147.stock.data.Position;
import com.zxzx74147.stock.data.PositionListData;
import com.zxzx74147.stock.databinding.FragmentPositionBinding;
import com.zxzx74147.stock.databinding.ItemMachpositionHeaderBinding;
import com.zxzx74147.stock.databinding.LayoutPositionHeaderBinding;
import com.zxzx74147.stock.storage.TradesStorage;

import java.util.LinkedList;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 */
public class PositionFragment extends BaseDialogFragment {

    private FragmentPositionBinding mBinding = null;
    private UserViewModel mUserViewModel = null;

    private CommonRecyclerViewAdapter<Position> mPositionAdapter = null;
    private CommonRecyclerViewAdapter<MachPosition> mMachAdapter = null;
    private TradesStorage mTradeStorage = RetrofitClient.getClient().create(TradesStorage.class);


    public static PositionFragment newInstance(IntentData intnetData) {
        PositionFragment fragment = new PositionFragment();
        Bundle args = new Bundle();
        args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, intnetData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_position, container, false);
        mUserViewModel = ViewModelProviders.of(ViewUtil.getFragmentActivity(getContext())).get(UserViewModel.class);
        mUserViewModel.getUserUniLiveData().observe(this, userUniData -> {
            refresh(userUniData);
        });
        initView();
        return mBinding.getRoot();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {

    }

    private void refresh(UserUniData userdata) {
        ViewUtil.changeTabs(((ViewGroup)mBinding.tabLayout2.getChildAt(0)).getChildAt(0),String.format(getString(R.string.format_my_position), userdata.positionList.num));
        ViewUtil.changeTabs(((ViewGroup)mBinding.tabLayout2.getChildAt(0)).getChildAt(1),String.format(getString(R.string.format_my_machposition), userdata.machPositionList.num));

    }


    private void initView() {
        mPositionAdapter = new CommonRecyclerViewAdapter<>(new LinkedList<>());
        mMachAdapter = new CommonRecyclerViewAdapter<>(new LinkedList<>());
        CommonMultiTypeDelegate delegate = new CommonMultiTypeDelegate(){
            @Override
            protected int getItemType(Object o) {
                if(o instanceof MachPosition){
                    MachPosition mach = (MachPosition) o;
                    if(mach.status==0){
                        return R.layout.item_machposition;
                    }
                    return R.layout.item_machposition_his;
                }
                return super.getItemType(o);
            }
        };
        delegate.registerItemType(R.layout.item_machposition,R.layout.item_machposition);
        delegate.registerItemType(R.layout.item_machposition_his,R.layout.item_machposition_his);
        mPositionAdapter.setMultiTypeDelegate(delegate);
        mMachAdapter.setMultiTypeDelegate(delegate);

        mPositionAdapter.setCommonEmptyView(getActivity(),R.string.no_position);
        mMachAdapter.setCommonEmptyView(getActivity(),R.string.no_machposition);

        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.list.setLayoutManager(lm);
        mBinding.list.setAdapter(mPositionAdapter);

        RxTabLayout.selectionEvents(mBinding.tabLayout2).subscribe(tabLayoutSelectionEvent -> {
            if (tabLayoutSelectionEvent.tab().getPosition() == 0) {
                mPositionAdapter.loadMoreComplete();
                RecyclerViewUtil.setupRecyclerView(mBinding.refreshLayout, mBinding.list, mPositionAdapter, new CommonListRequestCallback<Position>() {
                    @Override
                    public Observable<PositionListData> getObserverble(BaseListData listdata) {
                        return mTradeStorage.positionGetList(listdata == null ? 0 : listdata.nextPage);
                    }
                });
            } else {
                mMachAdapter.loadMoreComplete();
                RecyclerViewUtil.setupRecyclerView(mBinding.refreshLayout, mBinding.list, mMachAdapter, new CommonListRequestCallback<MachPosition>() {
                    @Override
                    public Observable<MachPositionListData> getObserverble(BaseListData listdata) {
//                        return mTradeStorage.machpositionGetList(listdata == null ? 0 : listdata.nextPage);
                        return mTradeStorage.machpositionGetHisList(listdata == null ? 0 : listdata.nextPage);
                    }
                });
            }
        });


        ItemMachpositionHeaderBinding mMachpositionHeaderBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.item_machposition_header, null, false);
        mMachAdapter.addHeaderView(mMachpositionHeaderBinding.getRoot());

        LayoutPositionHeaderBinding mLayoutPositionHeaderBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_position_header, null, false);
        mPositionAdapter.addHeaderView(mLayoutPositionHeaderBinding.getRoot());
        mLayoutPositionHeaderBinding.setUserUniData(AccountManager.sharedInstance().getUserUni());

        RxView.clicks(mMachpositionHeaderBinding.expand).subscribe(o->{
            if(mMachpositionHeaderBinding.content.getMaxLines()==2){
                mMachpositionHeaderBinding.content.setMaxLines(100);
                mMachpositionHeaderBinding.expand.setText(R.string.pack_up);
            }else{
                mMachpositionHeaderBinding.content.setMaxLines(2);
                mMachpositionHeaderBinding.expand.setText(R.string.expanc);
            }

        });


        Bundle bundle = getArguments();
        IntentData goodIntent = (IntentData) bundle.getSerializable(ZXActivityJumpHelper.INTENT_DATA);
        mBinding.tabLayout2.setScrollPosition(goodIntent.type, 0, false);
    }

}
