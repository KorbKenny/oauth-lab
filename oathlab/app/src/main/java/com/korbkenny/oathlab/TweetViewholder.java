package com.korbkenny.oathlab;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by KorbBookProReturns on 11/17/16.
 */

public class TweetViewholder extends RecyclerView.ViewHolder{
    TextView mText;
    TextView mDate;

    public TweetViewholder(View itemView) {
        super(itemView);
        mText = (TextView) itemView.findViewById(R.id.mainText);
        mDate = (TextView) itemView.findViewById(R.id.dateText);
    }
}
