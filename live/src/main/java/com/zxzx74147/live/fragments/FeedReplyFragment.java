package com.zxzx74147.live.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;

import com.jakewharton.rxbinding2.support.design.widget.RxTextInputLayout;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewEditorActionEvent;
import com.zxzx74147.devlib.BR;
import com.zxzx74147.devlib.base.BaseBindingViewHolder;
import com.zxzx74147.devlib.base.BaseFragment;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.BaseListData;
import com.zxzx74147.devlib.data.DialogItem;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.fragment.CommonFragmentDialog;
import com.zxzx74147.devlib.interfaces.CommonListRequestCallback;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.RecyclerViewUtil;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXActivityJumpHelper;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.devlib.widget.RecycleViewDivider;
import com.zxzx74147.live.R;
import com.zxzx74147.live.data.Reply;
import com.zxzx74147.live.data.ReplyData;
import com.zxzx74147.live.data.ReplyListData;
import com.zxzx74147.live.data.ReplyNumData;
import com.zxzx74147.live.data.Text;
import com.zxzx74147.live.databinding.FragmentReplyBinding;
import com.zxzx74147.live.databinding.HeadTextBinding;
import com.zxzx74147.live.databinding.ItemReplyBinding;
import com.zxzx74147.live.stroage.FeedStorage;

import java.util.LinkedList;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * A fragment representing a list of Items.
 */
public class FeedReplyFragment extends BaseFragment {


    private FragmentReplyBinding mBinding = null;
    private HeadTextBinding mHeadBinding = null;
    private CommonRecyclerViewAdapter<Reply> mAdapter = null;
    private FeedStorage mFeedStorage = RetrofitClient.getClient().create(FeedStorage.class);
    private Text mData = null;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FeedReplyFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FeedReplyFragment newInstance(IntentData<Text> intent) {
        FeedReplyFragment fragment = new FeedReplyFragment();
        Bundle args = new Bundle();
        args.putSerializable(ZXActivityJumpHelper.INTENT_DATA, intent);
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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_reply, container, false);
        mHeadBinding = DataBindingUtil.inflate(inflater, R.layout.head_text, container, false);
        return mBinding.getRoot();
    }

    public void setData(Text data) {
        mData = data;
//        initData();
    }

    private void initView() {
        mBinding.list.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayout.HORIZONTAL,
                1, getResources().getColor(R.color.div_default)));
        mAdapter = new CommonRecyclerViewAdapter<Reply>(new LinkedList<>()){
            @Override
            protected void convert(BaseBindingViewHolder helper, Reply item) {
                super.convert(helper, item);
                ItemReplyBinding replyBinding = (ItemReplyBinding) helper.mBinding;
                replyBinding.setDeleteCallback(new CommonCallback<Reply>() {
                    @Override
                    public void callback(Reply reply) {
                        DialogItem item = new DialogItem();
                        item.title = getResources().getString(R.string.delete_verify);

                        item.obj = reply;
                        CommonFragmentDialog dialog = CommonFragmentDialog.newInstance(new IntentData<>(item));
                        ZXFragmentJumpHelper.startFragment(getActivity(), dialog, new CommonCallback() {
                            @Override
                            public void callback(Object item) {
                                if(item!=null) {
                                    doDelete(reply);
                                }
                                return;
                            }
                        });

                    }


                });
            }
        };
        RxView.clicks(mHeadBinding.write).subscribe(v->{
            ViewUtil.showSoftPad(mBinding.replyEidt);
        });
        mAdapter.addHeaderView(mHeadBinding.getRoot());
        mBinding.list.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.setMultiTypeDelegate(new CommonMultiTypeDelegate());

        RxTextView.editorActionEvents(mBinding.replyEidt).subscribe(new Consumer<TextViewEditorActionEvent>() {
            @Override
            public void accept(TextViewEditorActionEvent textViewEditorActionEvent) throws Exception {
                if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_SEND) {
                    doSend();
                }
            }
        });

        RecyclerViewUtil.setupRecyclerView(mBinding.refreshLayout, mBinding.list, mAdapter, new CommonListRequestCallback<Reply>() {
            @Override
            public Observable<ReplyListData> getObserverble(BaseListData listdata) {
                if (listdata == null) {
                    return mFeedStorage.replyGetList(mData.textId, 0);
                } else {
                    return mFeedStorage.replyGetList(mData.textId, listdata.nextPage);
                }
            }
        });
        mBinding.refreshLayout.setRefreshing(false);
        mAdapter.setCommonEmptyView(getActivity(),R.string.no_reply);
        mAdapter.setHeaderFooterEmpty(true,false);

    }

    private void doDelete(Reply reply){
        NetworkApi.ApiSubscribe(mFeedStorage.replyDel(reply.replyId), new Consumer<ReplyNumData>() {
            @Override
            public void accept(ReplyNumData replyData) throws Exception {
                if (replyData.hasError()) {
                    ToastUtil.showToast(getActivity(), replyData.error.usermsg);
                    return;
                }
                mData.replyNum = replyData.replyNum;
                mAdapter.remove(reply);
                mHeadBinding.setData(mData);
            }
        });
    }

    private void doSend() {
        String content = mBinding.replyEidt.getText().toString();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        NetworkApi.ApiSubscribe(mFeedStorage.replyAdd(mData.textId, content), new Consumer<ReplyData>() {
            @Override
            public void accept(ReplyData replyData) throws Exception {
                if (replyData.hasError()) {
                    ToastUtil.showToast(getActivity(), replyData.error.usermsg);
                    return;
                }
                ToastUtil.showToast(getActivity(), "回复成功");
                mAdapter.addData(0,replyData.reply);
                mBinding.replyEidt.setText("");
//                mBinding.list.scrollToButtom();
                ViewUtil.hideSoftPad(mBinding.replyEidt);

            }
        });
    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        initView();
        initData();
    }

    private void initData() {
        if (mData == null) {
            return;
        }
        mHeadBinding.setData(mData);
        NetworkApi.ApiSubscribe(mFeedStorage.feedRead(mData.textId), readData -> {
            if (readData.hasError()) {
                return;
            }
            mData.readNum = readData.readNum;
            mHeadBinding.setData(mData);
        });

    }

}
