package com.zxzx74147.profile.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.interfaces.CommonListRequestCallback;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.RecyclerViewUtil;
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
public class VocherFragment extends BaseDialogFragment {


    private FragmentVocherBinding mBinding = null;
    private EventStorage mEventStorage = RetrofitClient.getClient().create(EventStorage.class);
    //    private UserViewModel mUserViewModel = null;
//
    private CommonRecyclerViewAdapter<Voucher> mVoucherAdapter = null;

    //
//
    public static VocherFragment newInstance() {
        VocherFragment fragment = new VocherFragment();
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
        mVoucherAdapter.setEmptyView(R.layout.no_voucher);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        mBinding.list.setLayoutManager(lm);

        RecyclerViewUtil.setupRecyclerView(mBinding.refreshLayout, mBinding.list, mVoucherAdapter, new CommonListRequestCallback<Voucher>() {
            @Override
            public Observable<VocherUserListData> getObserverble(BaseListData listdata) {
                return mEventStorage.eventsVoucherMyList(listdata == null ? 0 : listdata.nextPage);
            }
        });

    }

}
