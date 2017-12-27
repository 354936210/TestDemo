package com.owangwang.easymock;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.owangwang.easymock.bean.WoDeKuaiDi;

import org.litepal.crud.DataSupport;

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
        //物流状态 1在途中 2派件中 3已签收 4派送失败(拒签等)

        if (mList.get(position).getStatus().equals("1")){
            holder.tv_touxiang.setText("在途中");
        }else if (mList.get(position).getStatus().equals("2")){
            holder.tv_touxiang.setText("派件中");
        }else if (mList.get(position).getStatus().equals("3")){
            holder.tv_touxiang.setText("已签收");
        }else if (mList.get(position).getStatus().equals("4")){
            holder.tv_touxiang.setText("失败");
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_number;
        TextView tv_name;
        ImageView iv_delete;
        TextView tv_touxiang;

        public ViewHolder(final View itemView) {

            super(itemView);
            tv_number=itemView.findViewById(R.id.tv_number);
            tv_name=itemView.findViewById(R.id.tv_name);
            tv_touxiang=itemView.findViewById(R.id.tv_touxiang);
            Typeface mtypeface= Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/FZSTK.TTF");
            tv_touxiang.setTypeface(mtypeface);
            //设置逆时针旋转30度
            tv_touxiang.setRotation(-30);
            iv_delete=itemView.findViewById(R.id.iv_delete);
            iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    int result= DataSupport.deleteAll(WoDeKuaiDi.class,"number=?",mList.get(position).getNumber());
                    if (result!=-1)
                    {
                        Toast.makeText(itemView.getContext(),"已删除!",Toast.LENGTH_SHORT).show();
                        mList.remove(position);
                        notifyDataSetChanged();
                    }else {
                        Toast.makeText(itemView.getContext(),"删除失败!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
