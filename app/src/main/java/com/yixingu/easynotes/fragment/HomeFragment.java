package com.yixingu.easynotes.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yixingu.easynotes.R;

import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.DefaultNoAnimator;
import me.yokeyword.fragmentation.anim.DefaultVerticalAnimator;

/**
 * Created by likaisong on 17-4-24.
 */

public class HomeFragment extends BaseMainFragment implements Toolbar.OnMenuItemClickListener {

    private Toolbar mToolbar;
    private FloatingActionButton mFab;
    private RecyclerView mRecy;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab);
        mRecy = (RecyclerView) view.findViewById(R.id.recy);

        mToolbar.setTitle(R.string.notes);
        initToolbarNav(mToolbar, true);
        mToolbar.inflateMenu(R.menu.home);
        mToolbar.setOnMenuItemClickListener(this);

//        mAdapter = new HomeAdapter(_mActivity);
//        LinearLayoutManager manager = new LinearLayoutManager(_mActivity);
//        mRecy.setLayoutManager(manager);
//        mRecy.setAdapter(mAdapter);
//
//        mRecy.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 5) {
//                    mFab.hide();
//                } else if (dy < -5) {
//                    mFab.show();
//                }
//            }
//        });
//
//        mAdapter.setOnItemClickListener(new OnItemClickListener() {
//            @Override
//            public void onItemClick(int position, View view) {
//                start(DetailFragment.newInstance(mAdapter.getItem(position).getTitle()));
//            }
//        });
//
//        // Init Datas
//        List<Article> articleList = new ArrayList<>();
//        for (int i = 0; i < 15; i++) {
//            int index = (int) (Math.random() * 3);
//            Article article = new Article(mTitles[index], mContents[index]);
//            articleList.add(article);
//        }
//        mAdapter.setDatas(articleList);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * 类似于 Activity的 onNewIntent()
     */
    @Override
    protected void onNewBundle(Bundle args) {
        super.onNewBundle(args);

        Toast.makeText(_mActivity, args.getString("from"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_anim:
//                final PopupMenu popupMenu = new PopupMenu(_mActivity, mToolbar, GravityCompat.END);
//                popupMenu.inflate(R.menu.home_pop);
//                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.action_anim_veritical:
//                                _mActivity.setFragmentAnimator(new DefaultVerticalAnimator());
//                                Toast.makeText(_mActivity, "设置全局动画成功! 竖向", Toast.LENGTH_SHORT).show();
//                                break;
//                            case R.id.action_anim_horizontal:
//                                _mActivity.setFragmentAnimator(new DefaultHorizontalAnimator());
//                                Toast.makeText(_mActivity, "设置全局动画成功! 横向", Toast.LENGTH_SHORT).show();
//                                break;
//                            case R.id.action_anim_none:
//                                _mActivity.setFragmentAnimator(new DefaultNoAnimator());
//                                Toast.makeText(_mActivity, "设置全局动画成功! 无", Toast.LENGTH_SHORT).show();
//                                break;
//                        }
//                        popupMenu.dismiss();
//                        return true;
//                    }
//                });
//                popupMenu.show();
//                break;
//        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecy.setAdapter(null);
    }
}
