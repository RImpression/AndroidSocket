package com.example.andsocket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by RImpression on 2016/6/13 0013.
 */
public class ChatAdapter extends BaseAdapter {
    private static final int TYPE_INPUT = 0;
    private static final int TYPE_OUTPUT = 1;
    private static final int TYPE_GUIDE = 2;
    private ArrayList<ChatMessage> mDatas = null;
    private Context mContent;


    public ChatAdapter(Context context,ArrayList<ChatMessage> message) {
        this.mContent = context;
        this.mDatas = message;
    }


    @Override
    public int getCount() {
        return 0;
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
    public int getItemViewType(int position) {
        if (mDatas.get(position).getType().equals("INPUT")){
            return TYPE_INPUT;
        } else if (mDatas.get(position).getType().equals("OUTPUT")) {
            return TYPE_OUTPUT;
        } else {
            return TYPE_GUIDE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        ChatMessage chatMessage = mDatas.get(position);
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        ViewHolder3 holder3 = null;
        if (convertView == null) {
            switch (type) {
                case TYPE_INPUT:
                    holder1 = new ViewHolder1();
                    convertView = LayoutInflater.from(mContent).inflate(R.layout.item_left,parent,false);
                    holder1.tvFromDate = (TextView) convertView.findViewById(R.id.tvFromDate);
                    holder1.tvFromName = (TextView) convertView.findViewById(R.id.tvFromName);
                    holder1.tvFromContent = (TextView) convertView.findViewById(R.id.tvFromContent);
                    holder1.imgFrom = (ImageView) convertView.findViewById(R.id.imgFrom);
                    convertView.setTag(0,holder1);
                    break;
                case TYPE_OUTPUT:
                    holder2 = new ViewHolder2();
                    convertView = LayoutInflater.from(mContent).inflate(R.layout.item_right,parent,false);
                    holder2.tvToDate = (TextView) convertView.findViewById(R.id.tvToDate);
                    holder2.tvToName = (TextView) convertView.findViewById(R.id.tvToName);
                    holder2.tvToContent = (TextView) convertView.findViewById(R.id.tvToContent);
                    holder2.imgTo = (ImageView) convertView.findViewById(R.id.imgTo);
                    convertView.setTag(1,holder2);
                    break;
                case TYPE_GUIDE:
                    holder3 = new ViewHolder3();
                    convertView = LayoutInflater.from(mContent).inflate(R.layout.item_guide,parent,false);
                    holder3.tvGuideWord = (TextView) convertView.findViewById(R.id.tvGuideWord);
                    convertView.setTag(2,holder3);
                    break;
            }

        } else {
            switch (type) {
                case TYPE_INPUT:
                    holder1 = (ViewHolder1) convertView.getTag(0);
                    break;
                case TYPE_OUTPUT:
                    holder2 = (ViewHolder2) convertView.getTag(1);
                    break;
                case TYPE_GUIDE:
                    holder3 = (ViewHolder3) convertView.getTag(2);
                    break;
            }

        }


        switch (type) {
            case TYPE_INPUT:
                holder1.tvFromName.setText(chatMessage.getUserName());
                holder1.tvFromContent.setText(chatMessage.getMsg());
                break;
            case TYPE_OUTPUT:
                holder2.tvToName.setText(chatMessage.getUserName());
                holder2.tvToContent.setText(chatMessage.getMsg());
                break;
            case TYPE_GUIDE:
                holder3.tvGuideWord.setText(chatMessage.getMsg());
                break;
        }


        return convertView;
    }


    private static class ViewHolder1{
        private TextView tvFromDate,tvFromName,tvFromContent;
        private ImageView imgFrom;
    }

    private static class ViewHolder2{
        private TextView tvToDate,tvToName,tvToContent;
        private ImageView imgTo;
    }

    private static class ViewHolder3{
        private TextView tvGuideWord;
    }

}
