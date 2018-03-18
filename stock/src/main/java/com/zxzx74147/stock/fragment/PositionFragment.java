package com.zxzx74147.stock.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dinuscxj.refresh.MaterialDragDistanceConverter;
import com.dinuscxj.refresh.RecyclerRefreshLayout;
import com.jakewharton.rxbinding2.support.design.widget.RxTabLayout;
import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.devlib.BR;
import com.zxzx74147.devlib.DevLib;
import com.zxzx74147.devlib.base.BaseBindingViewHolder;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.data.DialogItem;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.fragment.CommonFragmentDialog;
import com.zxzx74147.devlib.interfaces.CommonListRequestCallback;
import com.zxzx74147.devlib.interfaces.IBaseListDataHolder;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.RecyclerViewUtil;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.devlib.widget.CommonRefreshView;
import com.zxzx74147.profile.data.UserUniData;
import com.zxzx74147.stock.R;
import com.zxzx74147.stock.data.MachPosition;
import com.zxzx74147.stock.data.MachPositionList;
import com.zxzx74147.stock.data.MachPositionListData;
import com.zxzx74147.stock.data.Position;
import com.zxzx74147.stock.data.PositionListData;
import com.zxzx74147.stock.databinding.FragmentPositionBinding;
import com.zxzx74147.stock.databinding.ItemMachpositionBinding;
import com.zxzx74147.stock.databinding.ItemMachpositionHeaderBinding;
import com.zxzx74147.stock.databinding.LayoutPositionHeaderBinding;
import com.zxzx74147.stock.storage.TradesStorage;
import com.zxzx74147.stock.util.FailDealUtil;

import java.util.LinkedList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 */
public class PositionFragment extends BaseDialogFragment {

    private FragmentPositionBinding mBinding = null;
    private UserViewModel mUserViewModel = null;

    private CommonRecyclerViewAdapter<Position> mPositionAdapter = null;
    private CommonRecyclerViewAdapter<Object> mMachAdapter = null;
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
        ViewUtil.changeTabs(((ViewGroup) mBinding.tabLayout2.getChildAt(0)).getChildAt(0), String.format(getString(R.string.format_my_position), userdata.positionList.num));
        ViewUtil.changeTabs(((ViewGroup) mBinding.tabLayout2.getChildAt(0)).getChildAt(1), String.format(getString(R.string.format_my_machposition), userdata.machPositionList.num));

    }


    private void initView() {
        mPositionAdapter = new CommonRecyclerViewAdapter<>(new LinkedList<>());
        mMachAdapter = new CommonRecyclerViewAdapter(new LinkedList<>()) {
            @Override
            protected void convert(BaseBindingViewHolder helper, Object item) {
                ViewDataBinding itemCommonBinding = helper.mBinding;
                itemCommonBinding.setVariable(BR.data, item);
                if (itemCommonBinding instanceof ItemMachpositionBinding) {
                    ItemMachpositionBinding binding = (ItemMachpositionBinding) itemCommonBinding;
                    binding.setDeleteCallback(mDeleteCallback);
                    binding.setMotifyCallback(mMotifyCallback);
                }
            }
        };
        CommonMultiTypeDelegate delegate = new CommonMultiTypeDelegate() {
            @Override
            protected int getItemType(Object o) {
                if (o instanceof MachPosition) {
                    MachPosition mach = (MachPosition) o;
                    if (mach.status == 0) {
                        return R.layout.item_machposition;
                    }
                    return R.layout.item_machposition_his;
                } else if (o instanceof String) {
                    return R.layout.item_his_title;
                }
                return super.getItemType(o);
            }
        };
        delegate.registerItemType(R.layout.item_his_title, R.layout.item_his_title);
        delegate.registerItemType(R.layout.item_machposition, R.layout.item_machposition);
        delegate.registerItemType(R.layout.item_machposition_his, R.layout.item_machposition_his);
        mPositionAdapter.setMultiTypeDelegate(delegate);
        mMachAdapter.setMultiTypeDelegate(delegate);

        mPositionAdapter.setCommonEmptyView(getActivity(), R.string.no_position);
        mMachAdapter.setCommonEmptyView(getActivity(), R.string.no_machposition);

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
                setupRecyclerViewMachPosition(mBinding.refreshLayout, mBinding.list, mMachAdapter, new CommonListRequestCallback<MachPosition>() {
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

        RxView.clicks(mMachpositionHeaderBinding.expand).subscribe(o -> {
            if (mMachpositionHeaderBinding.content.getMaxLines() == 2) {
                mMachpositionHeaderBinding.content.setMaxLines(100);
                mMachpositionHeaderBinding.expand.setText(R.string.pack_up);
            } else {
                mMachpositionHeaderBinding.content.setMaxLines(2);
                mMachpositionHeaderBinding.expand.setText(R.string.expanc);
            }

        });


        Bundle bundle = getArguments();
        IntentData goodIntent = (IntentData) bundle.getSerializable(ZXActivityJumpHelper.INTENT_DATA);
        mBinding.tabLayout2.getTabAt(goodIntent.type).select();
    }

    private CommonCallback<MachPosition> mMotifyCallback = new CommonCallback<MachPosition>() {
        @Override
        public void callback(MachPosition item) {

            startMotify(item);
        }
    };

    private CommonCallback<MachPosition> mDeleteCallback = new CommonCallback<MachPosition>() {
        @Override
        public void callback(MachPosition item) {
            startDelete(item);
        }
    };

    private void startMotify(MachPosition machPosition) {
        TradeFragment fragment = TradeFragment.newInstance(machPosition);
        ZXFragmentJumpHelper.startFragment(getActivity(), fragment, new CommonCallback() {
            @Override
            public void callback(Object item) {
                setupRecyclerViewMachPosition(mBinding.refreshLayout, mBinding.list, mMachAdapter, new CommonListRequestCallback<MachPosition>() {
                    @Override
                    public Observable<MachPositionListData> getObserverble(BaseListData listdata) {
//                        return mTradeStorage.machpositionGetList(listdata == null ? 0 : listdata.nextPage);
                        return mTradeStorage.machpositionGetHisList(listdata == null ? 0 : listdata.nextPage);
                    }
                });
            }
        });

    }

    private void startDelete(MachPosition machPosition) {

        DialogItem item = new DialogItem();
        item.title = getResources().getString(R.string.delete_machposition);
        item.obj = machPosition;
        CommonFragmentDialog dialog = CommonFragmentDialog.newInstance(new IntentData<>(item));
        ZXFragmentJumpHelper.startFragment(getActivity(), dialog, new CommonCallback() {
            @Override
            public void callback(Object item) {
                if (item != null) {
                    NetworkApi.ApiSubscribe(mTradeStorage.machpositionCancel(machPosition.machPositionId), o -> {
                        if (o.hasError()) {
                            if(FailDealUtil.dealFail(getActivity(),o.failed)){
                                return;
                            }
                            ToastUtil.showToast(getActivity(), o.error.usermsg);
                            return;
                        }
                        setupRecyclerViewMachPosition(mBinding.refreshLayout, mBinding.list, mMachAdapter, new CommonListRequestCallback<MachPosition>() {
                            @Override
                            public Observable<MachPositionListData> getObserverble(BaseListData listdata) {
//                        return mTradeStorage.machpositionGetList(listdata == null ? 0 : listdata.nextPage);
                                return mTradeStorage.machpositionGetHisList(listdata == null ? 0 : listdata.nextPage);
                            }
                        });

                    });
                }

            }
        });


    }


    public void setupRecyclerViewMachPosition(RecyclerRefreshLayout mRecyclerRefreshLayout, RecyclerView mRecyclerView, CommonRecyclerViewAdapter adapter, CommonListRequestCallback callback) {
        final Disposable[] refreshDisposable = {null};
        final Disposable[] loadMoreDisposable = {null};
        final BaseListData[] lastData = {null};
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                DevLib.getApp().getResources().getDimensionPixelSize(com.zxzx74147.devlib.R.dimen.default_gap_50), DevLib.getApp().getResources().getDimensionPixelSize(com.zxzx74147.devlib.R.dimen.default_gap_50));
        mRecyclerRefreshLayout.setRefreshView(new CommonRefreshView(mRecyclerRefreshLayout.getContext()), layoutParams);
        mRecyclerRefreshLayout.setDragDistanceConverter(new MaterialDragDistanceConverter());
//        mRecyclerView.post(new Runnable() {
//            @Override
//            public void run() {
//                mRecyclerView.setAdapter(adapter);
//            }
//        });
        mRecyclerView.setAdapter(adapter);
        if (mRecyclerRefreshLayout != null) {
            RecyclerRefreshLayout.OnRefreshListener listener = () -> {
                NetworkApi.ApiSubscribe(callback.getObserverble(null), new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        refreshDisposable[0] = d;
                        if (loadMoreDisposable[0] != null) {
                            loadMoreDisposable[0].dispose();
                            loadMoreDisposable[0] = null;
                        }
                    }

                    @Override
                    public void onNext(Object o) {
                        if (((UniApiData) o).hasError()) {
                            ToastUtil.showToast(mRecyclerView.getContext(), ((UniApiData) o).error.usermsg);
                            return;
                        }
                        mRecyclerRefreshLayout.setRefreshing(false);
                        IBaseListDataHolder iBaseListDataHolder = (IBaseListDataHolder) o;
                        if (iBaseListDataHolder.getListData() == null) {
                            return;
                        }
                        MachPositionList list = AccountManager.sharedInstance().getUserUni().machPositionList;
                        if (list != null && list.num > 0 && iBaseListDataHolder.getListData() != null) {
                            iBaseListDataHolder.getListData().getListItems().addAll(0, list.getListItems());

                        }
                        if (iBaseListDataHolder.getListData().num > 0) {
                            iBaseListDataHolder.getListData().getListItems().add(list.num, "");
                        }
                        lastData[0] = iBaseListDataHolder.getListData();
                        adapter.setNewData(iBaseListDataHolder.getListData().getListItems());
                        adapter.notifyDataSetChanged();
                        RecyclerViewUtil.dealLoadMore(adapter, iBaseListDataHolder.getListData());
                        if (loadMoreDisposable[0] != null) {
                            loadMoreDisposable[0].dispose();
                            loadMoreDisposable[0] = null;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();

                        mRecyclerRefreshLayout.setRefreshing(false);
                        loadMoreDisposable[0] = null;
                    }

                    @Override
                    public void onComplete() {

                    }
                });

            };
            mRecyclerRefreshLayout.setOnRefreshListener(listener);
            mRecyclerRefreshLayout.setRefreshing(true);
            listener.onRefresh();
        }

        adapter.setOnLoadMoreListener(() -> {
            if (lastData[0] == null) {
                return;
            }
            NetworkApi.ApiSubscribe(ViewUtil.getLivecircle(mRecyclerView), callback.getObserverble(lastData[0]), new Observer<UniApiData>() {
                @Override
                public void onSubscribe(Disposable d) {
                    refreshDisposable[0] = d;
                    if (loadMoreDisposable[0] != null) {
                        loadMoreDisposable[0].dispose();
                        loadMoreDisposable[0] = null;
                    }
                }

                @Override
                public void onNext(UniApiData o) {
                    if (o.hasError()) {
                        ToastUtil.showToast(mRecyclerView.getContext(), ((UniApiData) o).error.usermsg);
                        return;
                    }
                    IBaseListDataHolder iBaseListDataHolder = (IBaseListDataHolder) o;
                    if (iBaseListDataHolder.getListData() == null) {
                        return;
                    }
                    lastData[0] = iBaseListDataHolder.getListData();
                    adapter.addData(iBaseListDataHolder.getListData().getListItems());
                    mRecyclerRefreshLayout.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                    RecyclerViewUtil.dealLoadMore(adapter, iBaseListDataHolder.getListData());
                    if (loadMoreDisposable[0] != null) {
                        loadMoreDisposable[0].dispose();
                        loadMoreDisposable[0] = null;
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        }, mRecyclerView);


    }

}
