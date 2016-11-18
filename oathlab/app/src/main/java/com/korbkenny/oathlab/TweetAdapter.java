package com.korbkenny.oathlab;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by KorbBookProReturns on 11/17/16.
 */

public class TweetAdapter extends RecyclerView.Adapter<TweetViewholder>{
    ArrayList<TweetText> mList;
//    TextView mText;
//    TextView mDate;

    public TweetAdapter(ArrayList<TweetText> list) {
        mList = list;
//        mText = text;
//        mDate = date;
    }

    @Override
    public TweetViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new TweetViewholder(layoutInflater.inflate(R.layout.tweet_viewholder,parent,false));
    }

    @Override
    public void onBindViewHolder(TweetViewholder holder, int position) {
        holder.mText.setText(mList.get(position).getText());
        holder.mDate.setText(mList.get(position).getCreated_at());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
