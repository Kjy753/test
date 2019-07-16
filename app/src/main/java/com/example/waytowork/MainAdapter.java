package com.example.waytowork;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {

    private ArrayList<MainData> arrayList;//Main에 리스트 뷰를 담을 공간

    private LayoutInflater mInflate; // 추가
    private Context mContext; // 추가
    public MainAdapter(Context context, ArrayList<MainData> items){
        this.arrayList = items;
        this.mInflate = LayoutInflater.from(context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public MainAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//액티비티의 온크에이틀당 비슷한대 리스트뷰가 처음으로 생성될때 생성주기
        View view = mInflate.inflate(R.layout.item_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MainAdapter.CustomViewHolder holder, int position) {
    //실제 추가될때의 생명주기
        //holder.iv_pofile.setImageResource(arrayList.get(position).getIv_profile());
        holder.tv_start.setText(arrayList.get(position).tv_start_po);
        holder.tv_end.setText(arrayList.get(position).tv_end_po);
        holder.tv_context.setText(arrayList.get(position).tv_content);

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String curName= holder.tv_context.getText().toString();//현재내용?
                Toast.makeText(v.getContext(), curName, Toast.LENGTH_LONG).show();
            }
        });

//롱클릭했을때 삭제
//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                remove(holder.getAdapterPosition()); //파라메타 값을 넘겨줘야함
//                return true;
//            }
//        });
    }

    @Override
    public int getItemCount() {
        //return arrayList.size();
              return (null != arrayList ? arrayList.size() :0);

    }
//필터로 e1에 적은 글자 찾아서 검색기능
    public void filterList(ArrayList<MainData> filteredList) {
        arrayList = filteredList;
        notifyDataSetChanged();
    }

//위에 롱클릭 했을때 삭제 하려고 만들어 준건데 딱히 쓸모 없어서 일단 주석
//    public void remove(int position){
//        try {
//            arrayList.remove(position);
//            notifyItemRemoved(position); //새로고침이라는뜻 윗줄에list뷰가 한걸 새로고침
//        }catch (IndexOutOfBoundsException ex){
//            ex.printStackTrace();
//        }
//    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

       // protected ImageView iv_pofile;
        protected TextView tv_start;
        protected TextView tv_end;
        protected TextView tv_context;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
           // this.iv_pofile = (ImageView) itemView.findViewById(R.id.iv_profile);//find뷰랑 비슷함쓰는이유는 액티비티 형태의 클래스가 아니가 떄문에
            this.tv_start = (TextView) itemView.findViewById(R.id.tv_start);
            this.tv_end = (TextView) itemView.findViewById(R.id.tv_end);
            this.tv_context = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
}
