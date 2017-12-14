package com.owangwang.easymock;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.owangwang.easymock.bean.KuaiDibean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangchao on 2017/12/14.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    List<KuaiDibean> mList;
    Context context;
    List<String> nameList;
    List<String> typeList;

    public MyAdapter(List<KuaiDibean> mList,Context context) {
        this.mList = mList;
        this.context=context;
        init(mList);
    }

    private void init(List<KuaiDibean> mList) {
        nameList=new ArrayList<>();
        typeList=new ArrayList<>();
        nameList.add("自动识别");
        typeList.add("auto");
        for (KuaiDibean kuaiDibean:
             mList) {
            nameList.add(kuaiDibean.getName());
            typeList.add(kuaiDibean.getType());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item,parent,false);

        final ViewHolder viewHolder=new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=viewHolder.getAdapterPosition();
                KuaiDibean kuaiDibean = mList.get(position);
                Intent intent=new Intent(context,DoMainQueryActivity.class);
                intent.putExtra("position",position+1);

                intent.putStringArrayListExtra("namelist", (ArrayList<String>) nameList);
                intent.putStringArrayListExtra("typelist", (ArrayList<String>) typeList);
                context.startActivity(intent);
                Toast.makeText(context,"选择了"+kuaiDibean.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        Glide.with(holder.iv_touxiang.getContext()).load("http://upload.jianshu.io/users/upload_avatars/579463/a54a6aa2bcf9.jpeg?imageMogr2/auto-orient/strip|imageView2/1/w/240/h/240").into(holder.iv_touxiang);
        holder.tv_id.setText(mList.get(position).getType());
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
