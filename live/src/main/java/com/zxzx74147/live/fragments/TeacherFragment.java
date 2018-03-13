package com.zxzx74147.live.fragments;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.live.R;
import com.zxzx74147.live.data.Teacher;
import com.zxzx74147.live.data.TeacherLive;
import com.zxzx74147.live.data.TeacherLiveListData;
import com.zxzx74147.live.databinding.FragmentTeacherBinding;
import com.zxzx74147.live.stroage.LiveStorage;

import java.util.LinkedList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * A fragment representing a list of Items.
 */
public class TeacherFragment extends BaseDialogFragment {


    private FragmentTeacherBinding mBinding = null;
    private Teacher mTeacher = null;
    private CommonRecyclerViewAdapter mAdapter = null;
    private List<TeacherLive> mData = new LinkedList<>();


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TeacherFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static TeacherFragment newInstance(Teacher teacher) {
        TeacherFragment fragment = new TeacherFragment();
        Bundle args = new Bundle();
        args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, teacher);
        fragment.setArguments(args);
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_teacher, container, false);
        mAdapter = new CommonRecyclerViewAdapter<>(mData);
        CommonMultiTypeDelegate temp = new CommonMultiTypeDelegate();
        mAdapter.setMultiTypeDelegate(temp);
        mBinding.list.setAdapter(mAdapter);
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        mBinding.list.setLayoutManager(lm);
        ViewDataBinding mHeader = DataBindingUtil.inflate(inflater, R.layout.item_live_time_header, null, false);
        mAdapter.addHeaderView(mHeader.getRoot());
        RxView.clicks(mBinding.close).subscribe(o -> {
            this.dismiss();
        });
        RxView.clicks(mBinding.like).subscribe(o -> {
            this.like();
        });
        return mBinding.getRoot();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            mTeacher = (Teacher) getArguments().getSerializable(ZXActivityJumpHelper.INTENT_DATA);

            initData();
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    private void initData() {
        mBinding.setTeacher(mTeacher);
        LiveStorage liveStorage = RetrofitClient.getClient().create(LiveStorage.class);
        NetworkApi.ApiSubscribe(liveStorage.teacherLiveList(mTeacher.teacherId), new Consumer<TeacherLiveListData>() {
            @Override
            public void accept(TeacherLiveListData teacherLiveListData) throws Exception {
                if (teacherLiveListData.hasError()) {
                    return;
                }
                mData.addAll(teacherLiveListData.teacherLiveList.teacherLive);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void like(){
        LiveStorage liveStorage = RetrofitClient.getClient().create(LiveStorage.class);
        NetworkApi.ApiSubscribe(liveStorage.teacherLove(mTeacher.teacherId), new Consumer<UniApiData>() {
            @Override
            public void accept(UniApiData uniApiData) throws Exception {
                if(uniApiData.hasError()){
                    ToastUtil.showToast(getActivity(),uniApiData.error.usermsg);
                    return;
                }
                mTeacher.loveNum++;
                mBinding.setTeacher(mTeacher);
            }
        });
    }

}
