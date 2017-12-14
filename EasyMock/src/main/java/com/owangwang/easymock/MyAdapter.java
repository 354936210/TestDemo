package com.owangwang.easymock;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.owangwang.easymock.bean.Usersbean;

import java.util.List;

/**
 * Created by wangchao on 2017/12/14.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    List<Usersbean> mList;

    public MyAdapter(List<Usersbean> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(holder.iv_touxiang.getContext()).load(mList.get(position).getUrl()).into(holder.iv_touxiang);
        holder.tv_id.setText(mList.get(position).getId());
        holder.tv_name.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_id;
        TextView tv_name;
        ImageView iv_touxiang;

        public ViewHolder(View itemView) {

            super(itemView);
            tv_id=itemView.findViewById(R.id.tv_id);
            tv_name=itemView.findViewById(R.id.tv_name);
            iv_touxiang=itemView.findViewById(R.id.iv_touxiang);
        }
    }
}
