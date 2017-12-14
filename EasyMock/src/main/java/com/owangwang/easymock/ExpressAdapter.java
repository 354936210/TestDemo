package com.owangwang.easymock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.owangwang.easymock.bean.MyExpress;

import java.util.List;

/**
 * Created by wangchao on 2017/12/14.
 */

public class ExpressAdapter extends RecyclerView.Adapter<ExpressAdapter.ViewHolder>{
    List<MyExpress> mList;
    Context context;


    public ExpressAdapter(List<MyExpress> mList, Context context) {
        this.mList = mList;
        this.context=context;

    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.express_item,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        Glide.with(holder.iv_touxiang.getContext()).load("http://upload.jianshu.io/users/upload_avatars/579463/a54a6aa2bcf9.jpeg?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240").into(holder.iv_touxiang);
        holder.tv_time.setText(mList.get(position).getTime());
        holder.tv_status.setText(mList.get(position).getStatus());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_time;
        TextView tv_status;


        public ViewHolder(View itemView) {

            super(itemView);
            tv_time=itemView.findViewById(R.id.tv_ex_time);
            tv_status=itemView.findViewById(R.id.tv_ex_status);

        }
    }
}
