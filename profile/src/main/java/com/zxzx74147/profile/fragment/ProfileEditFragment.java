package com.zxzx74147.profile.fragment;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.yalantis.ucrop.UCrop;
import com.zxzx74147.devlib.base.BaseDialogFragment;
import com.zxzx74147.devlib.callback.CommonCallback;
import com.zxzx74147.devlib.data.DialogItem;
import com.zxzx74147.devlib.data.IntentData;
import com.zxzx74147.devlib.data.UpPicData;
import com.zxzx74147.devlib.fragment.CommonItemSelectorDialog;
import com.zxzx74147.devlib.modules.account.AccountManager;
import com.zxzx74147.devlib.modules.account.UserStorage;
import com.zxzx74147.devlib.modules.account.UserViewModel;
import com.zxzx74147.devlib.modules.busstation.ProfileBusStation;
import com.zxzx74147.devlib.network.NetworkApi;
import com.zxzx74147.devlib.network.RetrofitClient;
import com.zxzx74147.devlib.umeng.UmengAction;
import com.zxzx74147.devlib.umeng.UmengAgent;
import com.zxzx74147.devlib.utils.FileUtil;
import com.zxzx74147.devlib.utils.ImageUtil;
import com.zxzx74147.devlib.utils.ToastUtil;
import com.zxzx74147.devlib.utils.ViewUtil;
import com.zxzx74147.devlib.utils.ZXFragmentJumpHelper;
import com.zxzx74147.profile.R;
import com.zxzx74147.profile.data.UserUniData;
import com.zxzx74147.profile.databinding.LayoutProfileEditBinding;
import com.zxzx74147.profile.storage.SysStorage;

import java.io.File;

import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 */
public class ProfileEditFragment extends BaseDialogFragment {

    private static final String TAG = ProfileEditFragment.class.getSimpleName();
    private LayoutProfileEditBinding mBinding = null;
    private UserViewModel mUserModelView = null;
    private UserStorage mUserStroage = RetrofitClient.getClient().create(UserStorage.class);
    private SysStorage mSysStorage = RetrofitClient.getClient().create(SysStorage.class);
    private static int REQUEST_IMAGE_CAPTURE = 100;
    private static int REQUEST_IMAGE_GALLERY = 101;
    private static int REQUEST_IMAGE_CROP = 102;
    private Uri mCaptureUri = null;
    private UpPicData mUpPicData = null;

    public static ProfileEditFragment newInstance() {
        ProfileEditFragment fragment = new ProfileEditFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        UmengAgent.onEvent(UmengAction.ALUmengPageEditProfile);
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_profile_edit, container, false);
        mBinding.setUser(AccountManager.sharedInstance().getUser());
        mUserModelView = ViewModelProviders.of(getActivity()).get(UserViewModel.class);

        initView();
        initData();
        return mBinding.getRoot();
    }

    private void initData() {
        mBinding.setUserUni(mUserModelView.getUserUniLiveData().getValue());
        mUserModelView.getUserUniLiveData().observe(ProfileEditFragment.this, new Observer<UserUniData>() {
            @Override
            public void onChanged(@Nullable UserUniData userUniData) {
                if (userUniData.hasError()) {
                    return;
                }
                mBinding.setUserUni(userUniData);
            }
        });
    }

    private void initView() {
        RxView.focusChanges(mBinding.nickName).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                if (aBoolean) {
                    ViewUtil.locateEditCursor(mBinding.nickName);
                }
            }
        });
        RxTextView.textChanges(mBinding.nickName).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence charSequence) throws Exception {
                if (mUpPicData==null&&(charSequence.length() == 0||charSequence.toString().equals(mBinding.getUser().nickName))) {
                    mBinding.saveButton.setEnabled(false);
                }else{
                    mBinding.saveButton.setEnabled(true);
                }
            }
        });
        RxView.clicks(mBinding.saveButton).subscribe(v->{
            doEditName();
        });
//        RxTextView.editorActionEvents(mBinding.nickName).subscribe(new Consumer<TextViewEditorActionEvent>() {
//            @Override
//            public void accept(TextViewEditorActionEvent textViewEditorActionEvent) throws Exception {
//                if (textViewEditorActionEvent.actionId() == EditorInfo.IME_ACTION_SEND) {
//                    doEditName();
//                }
//            }
//        });

        RxView.clicks(mBinding.logout).subscribe(v -> {
            ProfileBusStation.startProfileLogout(getContext());
        });


        RxView.clicks(mBinding.portrait).subscribe(v -> {
            if(!FileUtil.isExternalStorageWritable()||!FileUtil.isCameraAvaliable()){
                FileUtil.verifyStoragePermissions(getActivity());
            }else{
                startEditPortrait();
            }

        });
    }


    private void startEditPortrait(){
        DialogItem dialogItem = new DialogItem();
        dialogItem.title = getString(R.string.select_portrait);
        dialogItem.items = new String[]{getString(R.string.take_photo), getString(R.string.gallery)};
        CommonItemSelectorDialog dialog = CommonItemSelectorDialog.newInstance(new IntentData(dialogItem));
        ZXFragmentJumpHelper.startFragment(getContext(), dialog, new CommonCallback() {
            @Override
            public void callback(Object item) {
                if (item == null) {
                    return;
                }
                int id = (int) item;
                switch (id) {
                    case 0:
                        mCaptureUri = ImageUtil.takeCamera(ProfileEditFragment.this, REQUEST_IMAGE_CAPTURE);
                        break;
                    case 1:
                        ImageUtil.selectImage(ProfileEditFragment.this, REQUEST_IMAGE_GALLERY);
                        break;
                }
            }
        });
    }


    private void doEditName() {

        String name = mBinding.nickName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showToast(getActivity(), "昵称不能为空");
            return;
        }

        if (name.equals(mBinding.getUser().nickName)&&mUpPicData==null) {
            return;
        }

        NetworkApi.ApiSubscribe(getActivity(), mUserStroage.accountUpdate(name, mUpPicData==null? null:mUpPicData.picKey), true, new Consumer<UserUniData>() {
            @Override
            public void accept(UserUniData userUniData) throws Exception {
                if (userUniData.hasError()) {
                    ToastUtil.showToast(getActivity(), userUniData.error.usermsg);
                    return;
                }
                if (mUserModelView.getUserUniLiveData().getValue() != null) {
                    mUserModelView.getUserUniLiveData().getValue().user = userUniData.user;
                    mUserModelView.getUserUniLiveData().setValue(mUserModelView.getUserUniLiveData().getValue());
                }
                mUpPicData = null;
                mBinding.saveButton.setEnabled(false);
                AccountManager.sharedInstance().doRefresh();
                mBinding.setUser(userUniData.user);
                ViewUtil.hideSoftPad(mBinding.nickName);
                ToastUtil.showToast(getActivity(),R.string.nickname_edit_done);
            }
        }, UserUniData.class);
    }

    private void doEditPortrait(Uri resultUri) {

        File output = new File(resultUri.getPath());
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), output);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", "file", reqFile);
        NetworkApi.ApiSubscribe(mSysStorage.uploadPic(body), new io.reactivex.Observer<UpPicData>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(UpPicData upPicData) {
                if (upPicData.hasError()) {
                    ToastUtil.showToast(getActivity(), upPicData.error.usermsg);
                    return;
                }
                mUpPicData = upPicData;
                mBinding.getUser().portraitUrl = upPicData.picUrl;
                mBinding.setUser(mBinding.getUser());
                mBinding.saveButton.setEnabled(true);

//                NetworkApi.ApiSubscribe(mUserStroage.accountUpdate(null, upPicData.picKey), new Consumer<UserUniData>() {
//                    @Override
//                    public void accept(UserUniData userUniData) throws Exception {
//                        if (userUniData.hasError()) {
//                            ToastUtil.showToast(getActivity(), userUniData.error.usermsg);
//                            return;
//                        }
//                        if (mUserModelView.getUserUniLiveData().getValue() != null) {
//                            mUserModelView.getUserUniLiveData().getValue().user = userUniData.user;
//                            mUserModelView.getUserUniLiveData().setValue(mUserModelView.getUserUniLiveData().getValue());
//                        }
//                        AccountManager.sharedInstance().doRefresh();
//                        mBinding.setUser(userUniData.user);
//                        ViewUtil.hideSoftPad(mBinding.nickName);
//                    }
//                });
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.showToast(getActivity(), e.getMessage());
            }

            @Override
            public void onComplete() {

            }


        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Log.d(TAG, "canceled or other exception!");
            return;
        }

        switch (resultCode) {
            case Activity.RESULT_OK:
                if (requestCode == REQUEST_IMAGE_CAPTURE) {

                    File file = ImageUtil.getImageFile();
                    Uri imageUri = Uri.fromFile(file);//The Uri to store the big bitmap
                    UCrop.of(mCaptureUri, imageUri)
                            .withAspectRatio(1, 1)
                            .withMaxResultSize(600, 600)
                            .start(getActivity(), ProfileEditFragment.this, REQUEST_IMAGE_CROP);
                } else if (requestCode == REQUEST_IMAGE_GALLERY) {
                    Uri image = data.getData();
                    File file = ImageUtil.getImageFile();
                    Uri imageUri = Uri.fromFile(file);//The Uri to store the big bitmap
                    UCrop.of(image, imageUri)
                            .withAspectRatio(1, 1)
                            .withMaxResultSize(500, 500)
                            .start(getActivity(), ProfileEditFragment.this, REQUEST_IMAGE_CROP);
                } else if (requestCode == REQUEST_IMAGE_CROP) {
                    final Uri resultUri = UCrop.getOutput(data);
                    doEditPortrait(resultUri);

                }

                break;

        }

//        Log.d(TAG, "REQUEST_IMAGE_CAPTURE");
//        Bitmap bitmap;
//        try {
//            //"data"这个居然没用常量定义,也是醉了,我们可以发现它直接把bitmap序列化到intent里面了。
//            bitmap = data.getExtras().getParcelable("data");
//            //TODO:do something with bitmap, Do NOT forget call Bitmap.recycler();
//            mCameraImageview.setImageBitmap(bitmap);
//        } catch (ClassCastException e) {
//            //do something with exceptions
//            e.printStackTrace();
//        }


    }


}
