package com.zxzx74147.live.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.base.BaseFragment;
import com.zxzx74147.devlib.modules.busstation.LiveBusStation;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.devlib.widget.SpaceItemDecoration;
import com.zxzx74147.live.R;
import com.zxzx74147.live.data.HomeData;
import com.zxzx74147.live.data.Live;
import com.zxzx74147.live.databinding.FragmentLiveListBinding;
import com.zxzx74147.live.stroage.LiveStorage;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * A fragment representing a list of Items.
 */
public class LiveListFragment extends BaseFragment {

    private FragmentLiveListBinding mBinding = null;
    private LiveStorage mStorage = RetrofitClient.getClient().create(LiveStorage.class);

    private List<Live> mData = new LinkedList<>();
    private CommonRecyclerViewAdapter<Live> mAdapter = null;
    private boolean mChecked = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LiveListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LiveListFragment newInstance() {
        LiveListFragment fragment = new LiveListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_live_list, container, false);
        initView();
        return mBinding.getRoot();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initData();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void initView() {
        mAdapter = new CommonRecyclerViewAdapter<>(mData);
        mAdapter.setMultiTypeDelegate(new CommonMultiTypeDelegate());
        mBinding.list.setAdapter(mAdapter);
        mBinding.list.setLayoutManager(new LinearLayoutManager(getActivity()));
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.default_gap_24);
        mBinding.list.addItemDecoration(new SpaceItemDecoration(0, spacingInPixels));
        mAdapter.loadMoreComplete();
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            Live live = (Live) adapter.getItem(position);
            startLive(live);
        });
    }

    private void startLive(Live live) {
        LiveBusStation.startLive(getContext(),live);
//        BaseFragment fragment = LiveFragment.newInstance(live);
//        ((BaseActivity) getActivity()).loadRootFragment(R.id.root_fragment, fragment, true, false);
    }

    private void initData() {
        NetworkApi.ApiSubscribe(mStorage.roomGetList(), new Consumer<HomeData>() {
            @Override
            public void accept(HomeData homeData) throws Exception {
                if (homeData.hasError()) {
                    ToastUtil.showToast(getContext(), homeData.error.usermsg);
                    return;
                }
                if (homeData.liveList != null) {
                    mData.clear();
                    mData.addAll(homeData.liveList.live);
                    mAdapter.notifyDataSetChanged();
                    if(!mChecked){
                        mChecked = true;
                        for(Live live:homeData.liveList.live){
                            if(live.status==2){
                                LiveBusStation.startLive(getContext(),live);
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

}
