package com.glh.fragmentlazy.lazyFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * <pre>
 *     author : 高磊华
 *     e-mail : 984992087@qq.com
 *     time   :2016/09/16
 *     desc   : 赖加载情况下fragment的基类(用于分析过程所有.)
 *              集成到真实项目中,涉及  isFirstLoad 的不需要.具体见BaseLazyFragment1
 * </pre>
 */


public abstract class BaseLazyFragment extends Fragment {

    private static String TAG = "赖加载基类========>";

    private boolean isVisible   = false;//当前Fragment是否可见
    private boolean isInitView  = false;//是否与View建立起映射关系
    private boolean isFirstLoad = true;//是否是第一次加载数据

    private View              convertView;
    private SparseArray<View> mViews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        convertView = inflater.inflate(getLayoutId(), container, false);
        mViews = new SparseArray<>();
        initView();
        isInitView = true;
        lazyLoadData();
        return convertView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.e(TAG, "setUserVisibleHint: " + isVisibleToUser + ".............." + this.getClass()
                .getSimpleName());
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoadData();

        } else {
            isVisible = false;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    private void lazyLoadData() {
        if (isFirstLoad) {
            Log.e(TAG, "第一次加载 " + " isInitView:" + isInitView + ".............." + "isVisible:" + isVisible + ".............." + this.getClass().getSimpleName());
        } else {
            Log.e(TAG, "不是第一次加载 " + " isInitView:" + isInitView
                    + ".............." + "isVisible:" + isVisible + ".............." + this.getClass().getSimpleName());
        }
        if (!isFirstLoad || !isVisible || !isInitView) {
            Log.e(TAG, "不加载" + ".............." + this.getClass().getSimpleName());
            return;
        }

        Log.e(TAG, "完成数据第一次加载" + ".............." + this.getClass().getSimpleName());
        initData();
        isFirstLoad = false;
    }

    /**
     * 加载页面布局文件
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 让布局中的view与fragment中的变量建立起映射
     */
    protected abstract void initView();

    /**
     * 加载要显示的数据
     */
    protected abstract void initData();

    /**
     * fragment中可以通过这个方法直接找到需要的view，而不需要进行类型强转
     *
     * @param viewId
     * @param <E>
     * @return
     */
    protected <E extends View> E findView(int viewId) {
        if (convertView != null) {
            E view = (E) mViews.get(viewId);
            if (view == null) {
                view = (E) convertView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return view;
        }
        return null;
        }

}