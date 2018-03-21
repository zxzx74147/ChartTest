package com.zxzx74147.stock.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.data.WheelSelectorData;
import com.zxzx74147.devlib.fragment.CommonWheelSelectorDialog;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.FormatUtil;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.Fail;
import com.zxzx74147.stock.data.GoodType;
import com.zxzx74147.stock.data.MachPositionData;
import com.zxzx74147.stock.data.Position;
import com.zxzx74147.stock.data.PositionData;
import com.zxzx74147.stock.databinding.FragmentPositionCloseBinding;
import com.zxzx74147.stock.databinding.FragmentPositionModifyBinding;
import com.zxzx74147.stock.storage.TradesStorage;
import com.zxzx74147.stock.util.FailDealUtil;

import io.reactivex.functions.Consumer;

/**
 */
public class PositionCloseFragment extends BaseDialogFragment {

    private FragmentPositionCloseBinding mBinding = null;
    private TradesStorage mTradeStorage = RetrofitClient.getClient().create(TradesStorage.class);


//    private List<Position> mData = new LinkedList<>();
//    private CommonRecyclerViewAdapter<MachPosition> mMachAdapter = null;

    public static PositionCloseFragment newInstance(IntentData<Position> intentData) {
        PositionCloseFragment fragment = new PositionCloseFragment();
        Bundle args = new Bundle();
        args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, intentData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_position_close, container, false);
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




        Bundle bundle = getArguments();
        IntentData intentData = (IntentData<Position>) bundle.getSerializable(ZXActivityJumpHelper.INTENT_DATA);

        Position data = (Position) intentData.data;
        if(AccountManager.sharedInstance().getUserUni()!=null&&AccountManager.sharedInstance().getUserUni().goodsTypeList!=null){
            for(GoodType goodType:AccountManager.sharedInstance().getUserUni().goodsTypeList.goodType) {
                if(goodType.goodsType.equals(data.goodsType)) {
                    data.closePrice =goodType.price.curPrice;
                    break;
                }
            }
        }
        mBinding.setData(data);

        RxView.clicks(mBinding.ok).subscribe(o -> {
            submit();
        });
    }

    public void submit(){
        NetworkApi.ApiSubscribe(mTradeStorage.positionClose(mBinding.getData().positionId, mBinding.getData().closePrice), new Consumer<PositionData>() {
            @Override
            public void accept(PositionData machPositionData) throws Exception {
                if(machPositionData.hasError()){
                    if(FailDealUtil.dealFail(getActivity(),machPositionData.failed)){
                        return;
                    }
                    ToastUtil.showToast(getActivity(),machPositionData.error.usermsg);
                    return;
                }
                if(mCallback!=null){
                    mCallback.callback(machPositionData);
                }
                ToastUtil.showToast(getActivity(),getResources().getString(R.string.succ));
                dismiss();
            }
        });
    }

}
