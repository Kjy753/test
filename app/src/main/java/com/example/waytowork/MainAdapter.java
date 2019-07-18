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

        holder.tv_start.setText(arrayList.get(position).getTv_start_po());
        holder.tv_end.setText(arrayList.get(position).getTv_end_po());
        holder.tv_content.setText(arrayList.get(position).getTv_content());
        if(arrayList.get(position).getIv_item_kat().equals("문서")) {
            holder.iv_kat.setImageResource(R.drawable.ic_marker); // 사진바꿔야함
        }else if(arrayList.get(position).getIv_item_kat().equals("음식")) {
            holder.iv_kat.setImageResource(R.drawable.ic_start); // 사진바꿔야함
        }

       /* holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //누르면 팝업이 나와서 보여줘야 함..
                String curName= holder.tv_context.getText().toString();//현재내용?
                Toast.makeText(v.getContext(), curName, Toast.LENGTH_LONG).show();
            }
        });*/

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

        public ImageView iv_kat;
        public TextView tv_start;
        public TextView tv_end;
        public TextView tv_content;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_kat = (ImageView) itemView.findViewById(R.id.iv_kat);//find뷰랑 비슷함쓰는이유는 액티비티 형태의 클래스가 아니가 떄문에
            this.tv_start = (TextView) itemView.findViewById(R.id.tv_start);
            this.tv_end = (TextView) itemView.findViewById(R.id.tv_end);
            this.tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }
}
