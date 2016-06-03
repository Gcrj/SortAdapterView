package com.gcrj.sortadapterview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gcrj.library.SortAdapter;
import com.gcrj.library.SortListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SortListView listView = (SortListView) findViewById(R.id.lv);
        listView.setAdapter(new MyAdapter(this, new ArrayList(Arrays.asList(getResources().getStringArray(R.array.aa)))));
    }

    class MyAdapter extends BaseAdapter implements SortAdapter<String> {

        private Context mContext;
        private List<String> mList;

        public MyAdapter(Context context, List list) {
            mContext = context;
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new TextView(mContext);
                convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 100));
                ((TextView) convertView).setTextSize(20);
            }
            ((TextView) convertView).setText(mList.get(position));
            return convertView;
        }

        @Override
        public List<String> getList() {
            return mList;
        }
    }

}
