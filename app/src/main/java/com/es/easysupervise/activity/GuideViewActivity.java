package com.es.easysupervise.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.es.easysupervise.MainActivity_;
import com.es.easysupervise.R;
import com.es.easysupervise.uil.Util;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

@EActivity(R.layout.activity_guide_view)
public class GuideViewActivity extends AppCompatActivity {


    @ViewById
    ViewPager vpGuide;

    ArrayList<View> views = new ArrayList<View>();
    View view = null;
    LayoutInflater mInflater;
    private static final int[] pics = {
            R.drawable.guide1,R.drawable.guide2,R.drawable.guide3,R.drawable.guide4
    };
    @AfterViews
    void init()
    {
        mInflater = getLayoutInflater();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        //初始化引导图片列表
        for(int i=0; i<pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setImageResource(pics[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            views.add(iv);
        }

        GuideViewAdapter adapter = new GuideViewAdapter(views,this);
        vpGuide.setAdapter(adapter);

    }

    public class GuideViewAdapter extends PagerAdapter {
        private ArrayList<View> mListViews;
        private Activity activity;

        public GuideViewAdapter(ArrayList<View> mListViews,Activity activity) {
            this.mListViews = mListViews;
            this.activity = activity;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)   {
            container.removeView(mListViews.get(position));
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {  //这个方法用来实例化页卡

            container.addView(mListViews.get(position), 0);


            if(position == mListViews.size() - 1)
            {
                ImageView imgLast = (ImageView)mListViews.get(mListViews.size() - 1);
                imgLast.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SharedPreferences.Editor edit = activity.getSharedPreferences(Util.SHAREDPREFERENCENAME,MODE_PRIVATE).edit();
                        edit.putBoolean("isFirstIn", false);
                        edit.commit();
                        Intent intent = new Intent(activity, MainActivity_.class);
                        startActivity(intent);
                        activity.finish();
                    }
                });
            }
            return mListViews.get(position);
        }

        @Override
        public int getCount() {

            if(mListViews != null)
                return mListViews.size();
            return  0;//返回页卡的数量
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;//官方提示这样写
        }
    }


}
