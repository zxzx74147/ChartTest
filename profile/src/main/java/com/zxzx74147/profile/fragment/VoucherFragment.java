package com.zxzx74147.profile.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.fragment.CommonInfoDialog;
import com.zxzx74147.devlib.interfaces.CommonListRequestCallback;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.RecyclerViewUtil;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.profile.R;
import com.zxzx74147.profile.data.VocherUserListData;
import com.zxzx74147.profile.data.Voucher;
import com.zxzx74147.profile.databinding.FragmentVocherBinding;
import com.zxzx74147.profile.storage.EventStorage;

import io.reactivex.Observable;

/**
 */
public class VoucherFragment extends BaseDialogFragment {


    private FragmentVocherBinding mBinding = null;
    private EventStorage mEventStorage = RetrofitClient.getClient().create(EventStorage.class);
    //    private UserViewModel mUserViewModel = null;
//
    private CommonRecyclerViewAdapter<Voucher> mVoucherAdapter = null;

    //
//
    public static VoucherFragment newInstance() {
        VoucherFragment fragment = new VoucherFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, intnetData);
//        fragment.setArguments(args);
        return fragment;
    }

    //
//    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_vocher, container, false);
//        mUserViewModel = ViewModelProviders.of(ViewUtil.getFragmentActivity(getContext())).get(UserViewModel.class);
//        mUserViewModel.getUserUniLiveData().observe(this, userUniData -> {
//            refresh(userUniData);
//        });
        initView();
        return mBinding.getRoot();
    }

    //
//    @Override
//    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
//
//    }
//
//    private void refresh(UserUniData userdata) {
//        ViewUtil.changeTabs(((ViewGroup)mBinding.tabLayout2.getChildAt(0)).getChildAt(0),String.format(getString(R.string.format_my_position), userdata.positionList.num));
//        ViewUtil.changeTabs(((ViewGroup)mBinding.tabLayout2.getChildAt(0)).getChildAt(1),String.format(getString(R.string.format_my_machposition), userdata.machPositionList.num));
//
//    }
//
//
    private void initView() {
        mVoucherAdapter = new CommonRecyclerViewAdapter<>(null);
        CommonMultiTypeDelegate delegate = new CommonMultiTypeDelegate();
        mVoucherAdapter.setMultiTypeDelegate(delegate);
        mVoucherAdapter.setEmptyView(DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.no_voucher,null,false).getRoot());

        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.list.setLayoutManager(lm);

        RecyclerViewUtil.setupRecyclerView(mBinding.refreshLayout, mBinding.list, mVoucherAdapter, new CommonListRequestCallback<Voucher>() {
            @Override
            public Observable<VocherUserListData> getObserverble(BaseListData listdata) {
                return mEventStorage.eventsVoucherMyList(listdata == null ? 0 : listdata.nextPage);
            }
        });

        RxView.clicks(mBinding.textView16).subscribe(v->{
            CommonInfoDialog dialog = CommonInfoDialog.newInstance(new IntentData<Integer>(R.layout.info_voucher));
            ZXFragmentJumpHelper.startFragment(getContext(), dialog,null);
        });

    }

}
