package com.zxzx74147.profile.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.interfaces.CommonListRequestCallback;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.RecyclerViewUtil;
import com.zxzx74147.devlib.utils.RspDealUtil;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.profile.R;
import com.zxzx74147.profile.data.VocherListData;
import com.zxzx74147.profile.data.VocherUserListData;
import com.zxzx74147.profile.data.Voucher;
import com.zxzx74147.profile.databinding.FragmentVocherUngetBinding;
import com.zxzx74147.profile.storage.EventStorage;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 */
public class VocherUnGetFragment extends BaseDialogFragment {


    private FragmentVocherUngetBinding mBinding = null;
    private EventStorage mEventStorage = RetrofitClient.getClient().create(EventStorage.class);
    //    private UserViewModel mUserViewModel = null;
//
    private CommonRecyclerViewAdapter<Voucher> mVoucherAdapter = null;

    //
//
    public static VocherUnGetFragment newInstance() {
        VocherUnGetFragment fragment = new VocherUnGetFragment();
//        Bundle args = new Bundle();
//        args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, intnetData);
//        fragment.setArguments(args);
        return fragment;
    }

    //
//    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_vocher_unget, container, false);
        initView();
        return mBinding.getRoot();
    }

    private void initView() {
        mVoucherAdapter = new CommonRecyclerViewAdapter<>(null);
        mVoucherAdapter.setEmptyView(DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.no_voucher,null,false).getRoot());

        CommonMultiTypeDelegate delegate = new CommonMultiTypeDelegate();
        mVoucherAdapter.setMultiTypeDelegate(delegate);
//        mVoucherAdapter.setEmptyView(R.layout.no_voucher);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.list.setLayoutManager(lm);

        RecyclerViewUtil.setupRecyclerView(mBinding.refreshLayout, mBinding.list, mVoucherAdapter, new CommonListRequestCallback<Voucher>() {
            @Override
            public Observable<VocherListData> getObserverble(BaseListData listdata) {
                return mEventStorage.eventsVoucherList();
            }
        });

        mVoucherAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                voucherPay((Voucher) adapter.getItem(position));
            }
        });

    }

    private void voucherPay(Voucher vocher){
        NetworkApi.ApiSubscribe(mEventStorage.eventsVoucherPay(vocher.voucherId), new Consumer<UniApiData>() {
            @Override
            public void accept(UniApiData uniApiData) throws Exception {
                if(RspDealUtil.deanRsp(getActivity(),uniApiData)){
                    return;
                }
                ToastUtil.showToast(getActivity(),R.string.vocher_get_succ);
            }
        });
    }

}
