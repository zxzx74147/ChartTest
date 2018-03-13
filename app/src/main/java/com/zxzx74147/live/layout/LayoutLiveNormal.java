package com.zxzx74147.live.layout;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewEditorActionEvent;
import com.zxzx74147.charttest.R;
import com.zxzx74147.charttest.databinding.LayoutLiveNormalBinding;
import com.zxzx74147.devlib.base.BaseActivity;
import com.zxzx74147.devlib.data.UniApiData;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.utils.KeyboardStatusDetector;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.widget.CommonMultiTypeDelegate;
import com.zxzx74147.devlib.widget.CommonRecyclerViewAdapter;
import com.zxzx74147.live.data.LiveMsgListData;
import com.zxzx74147.live.data.Msg;
import com.zxzx74147.live.data.MsgData;
import com.zxzx74147.live.stroage.LiveStorage;
import com.zxzx74147.live.viewmodel.LiveMsgViewModel;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by zhengxin on 2018/3/10.
 */

public class LayoutLiveNormal extends FrameLayout {


    private LayoutLiveNormalBinding mBingding = null;
    private LiveMsgViewModel mMsgViewModel = null;
    private CommonRecyclerViewAdapter<Msg> mAdapter = null;
    private LiveStorage mLiveStorage = RetrofitClient.getClient().create(LiveStorage.class);
    private boolean mIsTouchint = false;
    private KeyboardStatusDetector mDetector = new KeyboardStatusDetector();



    public LayoutLiveNormal(Context context) {
        super(context);
        init();
    }

    public LayoutLiveNormal(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LayoutLiveNormal(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mMsgViewModel = ViewModelProviders.of((FragmentActivity) getContext()).get(LiveMsgViewModel.class);
        mBingding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.layout_live_normal, this, true);

        mAdapter = new CommonRecyclerViewAdapter<>(null);
        mBingding.list.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter.setMultiTypeDelegate(new CommonMultiTypeDelegate());
        mBingding.list.setAdapter(mAdapter);
        mBingding.goodList.setProvider(ViewModelProviders.of((FragmentActivity) getContext()));
        mBingding.goodList.setLifeCircle((FragmentActivity) getContext());
        mBingding.bubble.setDefaultDrawableList();


//        RxView.touches(mBingding.list).subscribe(new Consumer<MotionEvent>() {
//            @Override
//            public void accept(MotionEvent motionEvent) throws Exception {
//                switch (motionEvent.getAction()) {
//                    case MotionEvent.ACTION_MOVE:
//                    case MotionEvent.ACTION_DOWN:
//                        mIsTouchint = true;
//                        break;
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_OUTSIDE:
//                    case MotionEvent.ACTION_CANCEL:
//                        mIsTouchint = false;
//                        break;
//
//                }
//            }
//        });

        mMsgViewModel.getLiveMsgListLiveData().observe((BaseActivity) getContext(), liveMsgListData -> {
            if (liveMsgListData.hasError()) {
                return;
            }
            mBingding.setLiveMsg(liveMsgListData);
            if (mIsTouchint) {
                return;
            }
            mAdapter.setNewData(liveMsgListData.msgList.msg);

            mBingding.bubble.startAnimation( mBingding.bubble.getWidth()/2,  mBingding.bubble.getHeight()-getResources().getDimensionPixelSize(R.dimen.default_gap_100),2);
            if(mBingding.profitLayout.rootView.getVisibility()==View.GONE) {
                Msg msg = mMsgViewModel.getLiveMsgListLiveData().popBullet();
                showProfit(msg);
            }
        });


        RxTextView.editorActionEvents(mBingding.commentEdit).subscribe(new Consumer<TextViewEditorActionEvent>() {
            @Override
            public void accept(TextViewEditorActionEvent textViewEditorActionEvent) throws Exception {
                if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_SEND) {
                    doSendComment();
                }
            }
        });
        RxView.clicks(mBingding.comment).subscribe(v->{
            showComment();
        });
        mDetector.registerActivity((Activity) getContext());
        mDetector.setmVisibilityListener(new KeyboardStatusDetector.KeyboardVisibilityListener() {
            @Override
            public void onVisibilityChanged(boolean keyboardVisible) {
                if(!keyboardVisible){
                    ViewUtil.hideSoftPad(mBingding.commentEdit);
                    mBingding.commentEdit.setVisibility(View.GONE);
                }
            }
        });

        RxView.clicks(mBingding.like).subscribe(v->{
            doLike();
        });

    }

    public void showComment(){
        mBingding.commentEdit.setVisibility(View.VISIBLE);
        ViewUtil.showSoftPad( mBingding.commentEdit);
    }

    private void doSendComment() {
        String comment = mBingding.commentEdit.getText().toString();
        if(TextUtils.isEmpty(comment)){
            ToastUtil.showToast(getContext(),"请输入");
            return;
        }

        NetworkApi.ApiSubscribe(((BaseActivity) getContext()).getLifecycle(), mLiveStorage.msgSend(mMsgViewModel.getLive().liveId, comment), new Consumer<MsgData>() {
            @Override
            public void accept(MsgData msgData) throws Exception {
                if(msgData.hasError()){
                    ToastUtil.showToast(getContext(),msgData.error.usermsg);
                    return;
                }
                ViewUtil.hideSoftPad(mBingding.commentEdit);
                mBingding.commentEdit.setText("");
                mBingding.commentEdit.setVisibility(View.GONE);

            }
        },MsgData.class);

    }

    private void showProfit(Msg msg){
        if(msg==null){
            return;
        }
        mBingding.profitLayout.setData(msg);
        mBingding.profitLayout.rootView.setVisibility(View.VISIBLE);
        Animator mFirstAnimator = getAnimator(mBingding.profitLayout.rootView);
        mFirstAnimator.start();
        mFirstAnimator.addListener(mAnimListener);
    }

    private Animator.AnimatorListener mAnimListener = new Animator.AnimatorListener() {

        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            mBingding.getRoot().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBingding.profitLayout.rootView.setVisibility(View.GONE);
                }
            },2000);

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };



    private static Animator getAnimator(View view) {
        ObjectAnimator result;
        int SCREEN_WIDTH=1080;
        int length = SCREEN_WIDTH + view.getRootView().getMeasuredWidth();
        result = ObjectAnimator
                .ofFloat(view, "translationX", SCREEN_WIDTH, 0)//
                .setDuration(3000);
            result.setInterpolator(new LinearInterpolator());

        return result;
    }

    private void doLike(){
        LiveMsgListData liveMsgListLiveData=  mMsgViewModel.getLiveMsgListLiveData().getValue();
        Observable<UniApiData> obs = null;
        if(liveMsgListLiveData.liveDynamic.isLove>0){
            obs = mLiveStorage.roomHate(mMsgViewModel.getLive().liveId);
        }else{
            obs = mLiveStorage.roomLove(mMsgViewModel.getLive().liveId);
        }
        NetworkApi.ApiSubscribe(((BaseActivity) getContext()).getLifecycle(), obs, new Consumer<UniApiData>() {
            @Override
            public void accept(UniApiData uniApiData) throws Exception {

            }
        },UniApiData.class);
    }


}
