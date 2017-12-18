package com.owangwang.easymock;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.owangwang.easymock.bean.WoDeKuaiDi;

import java.util.List;

/**
 * Created by wangchao on 2017/12/14.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    List<WoDeKuaiDi> mList;


    public MyAdapter(List<WoDeKuaiDi> mList) {
        this.mList = mList;
    }



    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item,parent,false);

         final ViewHolder viewHolder=new ViewHolder(view);
         view.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent=new Intent(view.getContext(),DoMainQueryActivity.class);
                 int position=viewHolder.getAdapterPosition();
                 intent.putExtra("mtype",mList.get(position).getType());
                 intent.putExtra("mnumber",mList.get(position).getNumber());
                 view.getContext().startActivity(intent);
             }
         });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        Glide.with(holder.iv_touxiang.getContext()).load("http://upload.jianshu.io/users/upload_avatars/579463/a54a6aa2bcf9.jpeg?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240").into(holder.iv_touxiang);
        holder.tv_number.setText(mList.get(position).getNumber());
        holder.tv_name.setText(mList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_number;
        TextView tv_name;
//        ImageView iv_touxiang;

        public ViewHolder(View itemView) {

            super(itemView);
            tv_number=itemView.findViewById(R.id.tv_number);
            tv_name=itemView.findViewById(R.id.tv_name);
//            iv_touxiang=itemView.findViewById(R.id.iv_touxiang);
        }
    }
}
