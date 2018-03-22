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
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.data.WheelSelectorData;
import com.zxzx74147.devlib.fragment.CommonWheelSelectorDialog;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.FormatUtil;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.profile.data.UserUniData;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.data.MachPosition;
import com.zxzx74147.stock.data.MachPositionData;
import com.zxzx74147.stock.data.Position;
import com.zxzx74147.stock.data.PositionData;
import com.zxzx74147.stock.databinding.FragmentPositionBinding;
import com.zxzx74147.stock.databinding.FragmentPositionModifyBinding;
import com.zxzx74147.stock.storage.TradesStorage;
import com.zxzx74147.stock.util.FailDealUtil;

import java.util.LinkedList;

import io.reactivex.functions.Consumer;

/**
 */
public class PositionModifyFragment extends BaseDialogFragment {

    private FragmentPositionModifyBinding mBinding = null;
    private TradesStorage mTradeStorage = RetrofitClient.getClient().create(TradesStorage.class);


//    private List<Position> mData = new LinkedList<>();
//    private CommonRecyclerViewAdapter<MachPosition> mMachAdapter = null;

    public static PositionModifyFragment newInstance(IntentData<Position> intentData) {
        PositionModifyFragment fragment = new PositionModifyFragment();
        Bundle args = new Bundle();
        args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, intentData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_position_modify, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        initView();

    }






    private void initView() {

        RxView.clicks(mBinding.cancel).subscribe(o->{
            dismiss();
        });

        RxView.clicks(mBinding.buyStopValue).subscribe(a -> {
            if(mBinding.getData().isVoucher!=0){
                return;
            }
            int offset = FormatUtil.getPureNum(mBinding.buyStopValue.getText().toString());
            WheelSelectorData data = new WheelSelectorData();
            data.items = FormatUtil.POINT_LIST;
            data.offset = offset;
            CommonWheelSelectorDialog dialog = CommonWheelSelectorDialog.newInstance(new IntentData<>(data));
            ZXFragmentJumpHelper.startFragment(getContext(), dialog, new CommonCallback() {
                @Override
                public void callback(Object item) {
                    if(item==null){
                        return;
                    }
                    mBinding.buyStopValue.setText(data.items.get((Integer) item));
                }
            });
        });

        RxView.clicks(mBinding.buyLimitValue).subscribe(a -> {
            if(mBinding.getData().isVoucher!=0){
                return;
            }
            int offset = FormatUtil.getPureNum(mBinding.buyLimitValue.getText().toString());
            WheelSelectorData data = new WheelSelectorData();
            data.items = FormatUtil.POINT_LIST;
            data.offset = offset;
            CommonWheelSelectorDialog dialog = CommonWheelSelectorDialog.newInstance(new IntentData<>(data));
            ZXFragmentJumpHelper.startFragment(getContext(), dialog, new CommonCallback() {
                @Override
                public void callback(Object item) {
                    if(item==null){
                        return;
                    }
                    mBinding.buyLimitValue.setText(data.items.get((Integer) item));
                }
            });
        });


        Bundle bundle = getArguments();
        IntentData intentData = (IntentData<Position>) bundle.getSerializable(ZXActivityJumpHelper.INTENT_DATA);
        mBinding.setData((Position) intentData.data);

        RxView.clicks(mBinding.ok).subscribe(o -> {
            submit();
        });
        if(mBinding.getData().isVoucher!=0){
            RxCompoundButton.checkedChanges(mBinding.checkDeffer).subscribe(v->{
                mBinding.checkDeffer.setChecked(false);
            });
        }
    }

    public void submit(){
        int stop = FormatUtil.getPureNum(mBinding.buyStopValue.getText().toString());
        int limit = FormatUtil.getPureNum(mBinding.buyLimitValue.getText().toString());
        String stopStr = stop==0? "":String.valueOf(stop);
        String limitStr = limit==0? "":String.valueOf(limit);
        NetworkApi.ApiSubscribe(getActivity(),mTradeStorage.positionModify(mBinding.getData().positionId, limitStr, stopStr, mBinding.checkDeffer.isChecked() ? 1 : 0),true, new Consumer<PositionData>() {
            @Override
            public void accept(PositionData machPositionData) throws Exception {
                if(machPositionData.hasError()){
                    if(FailDealUtil.dealFail(getActivity(),machPositionData.failed)){
                        return;
                    }
                    ToastUtil.showToast(getActivity(),machPositionData.error.usermsg);
                    return;
                }
                ToastUtil.showToast(getActivity(),getResources().getString(R.string.succ));
                dismiss();
                if(mCallback!=null){
                    mCallback.callback(mBinding.getData());
                }
            }
        },PositionData.class);
    }

}
