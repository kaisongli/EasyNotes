package com.yixingu.easynotes.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yixingu.easynotes.Adapter.MaterialSimpleListAdapter;
import com.yixingu.easynotes.R;
import com.yixingu.easynotes.model.ShareListItem;
import com.yixingu.easynotes.utils.BuildConfig;
import com.yixingu.easynotes.utils.SnackbarUtils;
import com.yixingu.easynotes.utils.WXUtils;

/**
 * Created by likaisong on 17-4-24.
 */

public class AboutFragment extends BaseMainFragment implements Toolbar.OnMenuItemClickListener{
    private android.support.v7.widget.Toolbar toolbar;
    private TextView versionText;
    private Button blogBtn;
    private Button projectBtn;
    private final static String WEIBO_PACKAGENAME = "com.sina.weibo";

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        versionText = (TextView)view.findViewById(R.id.version_text);
        blogBtn = (Button)view.findViewById(R.id.blog_btn);
        projectBtn = (Button)view.findViewById(R.id.project_home_btn);

        toolbar.setTitle(R.string.about);
        initToolbarNav(toolbar);

        versionText.setText("v "+getVersion(getActivity()));

        blogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startViewAction(BuildConfig.BLOG_URL);
            }
        });

        projectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startViewAction(BuildConfig.PROJECT_URL);
            }
        });
    }


    private void startViewAction(String uriStr) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uriStr));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getVersion(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1.0.0";
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share:
                showShareDialog();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void showShareDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.ShareDialog);
        builder.setTitle(getString(R.string.share));
        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(getActivity());
        String[] array = getResources().getStringArray(R.array.share_dialog_text);
        adapter.add(new ShareListItem.Builder(getActivity())
                .content(array[0])
                .icon(R.drawable.ic_wx_logo)
                .build());
        adapter.add(new ShareListItem.Builder(getActivity())
                .content(array[1])
                .icon(R.drawable.ic_wx_moments)
                .build());
        adapter.add(new ShareListItem.Builder(getActivity())
                .content(array[2])
                .icon(R.drawable.ic_wx_collect)
                .build());
        adapter.add(new ShareListItem.Builder(getActivity())
                .content(array[3])
                .icon(R.drawable.ic_sina_logo)
                .build());
        adapter.add(new ShareListItem.Builder(getActivity())
                .content(array[4])
                .icon(R.drawable.ic_share_more)
                .build());
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        shareToWeChatSession();
                        break;
                    case 1:
                        shareToWeChatTimeline();
                        break;
                    case 2:
                        shareToWeChatFavorite();
                        break;
                    case 3:
                        shareToWeibo();
                        break;
                    default:
                        share("", null);
                }
            }
        });
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = window.getAttributes();
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point out = new Point();
        display.getSize(out);
        lp.width = out.x;
        window.setAttributes(lp);
        final View decorView = window.getDecorView();
        decorView.setBackgroundColor(getResources().getColor(R.color.window_background));
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Animator animator = ObjectAnimator.ofFloat(decorView, "translationY", decorView.getMeasuredHeight() / 1.5F, 0);
                animator.setDuration(200);
                animator.start();
            }
        });
        dialog.show();
    }

    private void shareToWeChatTimeline(){
        shareToWeChat(SendMessageToWX.Req.WXSceneTimeline);
    }

    private void shareToWeChatSession(){
        shareToWeChat(SendMessageToWX.Req.WXSceneSession);
    }

    private void shareToWeChatFavorite(){
        shareToWeChat(SendMessageToWX.Req.WXSceneFavorite);
    }

    private void shareToWeibo(){
        if (isInstallApplication(WEIBO_PACKAGENAME)){
            share(WEIBO_PACKAGENAME, null);
        }else {
            SnackbarUtils.show(getActivity(), R.string.not_install_app);
        }
    }

    private void shareToWeChat(int scene){
        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), BuildConfig.WECHAT_ID, true);
        if (!api.isWXAppInstalled()){
            SnackbarUtils.show(getActivity(), R.string.not_install_app);
        }
        api.registerApp(BuildConfig.WECHAT_ID);
        WXWebpageObject object = new WXWebpageObject();
        object.webpageUrl = "http://www.wandoujia.com/apps/com.yixingu.easynotes";
        WXMediaMessage msg = new WXMediaMessage(object);
        msg.mediaObject = object;
        msg.thumbData = getLogoBitmapArray();
        msg.title = getString(R.string.app_desc);
        msg.description = getString(R.string.share_text, "", "");
        SendMessageToWX.Req request = new SendMessageToWX.Req();
        request.message = msg;
        request.scene = scene;
        api.sendReq(request);
        api.unregisterApp();
    }


    private byte[] getLogoBitmapArray(){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        return WXUtils.bmpToByteArray(bitmap, false);
    }

    private boolean isInstallApplication(String packageName){
        try {
            PackageManager pm = getActivity().getPackageManager();
            pm.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void share(String packages, Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (uri != null) {
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        } else {
            intent.setType("text/plain");
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share));
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text, getString(R.string.download_url), BuildConfig.APP_DOWNLOAD_URL));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!TextUtils.isEmpty(packages))
            intent.setPackage(packages);
        startActivity(Intent.createChooser(intent, getString(R.string.share)));
    }
}
