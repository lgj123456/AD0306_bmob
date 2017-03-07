package com.example.yhdj.ad0306bmob;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yhdj on 2017/3/7.
 */

public class Title extends LinearLayout{
    private List<Person> mPersons = new ArrayList<>();
    public Title(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title,this);


    }

}
