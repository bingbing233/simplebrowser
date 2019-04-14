package com.bing.simplebrowser;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {
    Context context ;
    int listPosition;
   private List<Bookmark>list;

   public BookmarkAdapter(List<Bookmark>list,Context context){
       this.list = list;
       this.context = context;
   }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bookmark_list, viewGroup, false);
        final BookmarkAdapter.ViewHolder holder = new BookmarkAdapter.ViewHolder(view);
        holder.bookmarkView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPosition = holder.getAdapterPosition();
                Bookmark bookmark = list.get(listPosition);
                Intent intent = new Intent(context, Main2Activity.class);
                String url = bookmark.url;
                intent.putExtra("url", url);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        List<String> popupMenuItemList = new ArrayList<>();
        popupMenuItemList.add("删除");
        popupMenuItemList.add("全部删除");
        PopupList popupList = new PopupList(context);
        popupList.setTextSize(40);
        popupList.bind(view, popupMenuItemList, new PopupList.PopupListListener() {
            @Override
            public boolean showPopupList(View adapterView, View contextView, int contextPosition) {
                return true;
            }

            @Override
            public void onPopupListClick(View contextView, int contextPosition, int position) {
                if (position == 0) {
                    String url = list.get(listPosition).url;
                    list.remove(listPosition);
                    notifyDataSetChanged();
                    delete(url);
                }
                if (position == 1) {
                    list.clear();
                    notifyDataSetChanged();
                    deleteAll();

                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Bookmark bookmark = list.get(i);
        viewHolder.titleText.setText(bookmark.title);
        viewHolder.urlText.setText(bookmark.url);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView urlText;
        TextView titleText;
        View bookmarkView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bookmarkView = itemView;
            urlText = itemView.findViewById(R.id.bookmarkUrlText);
            titleText = itemView.findViewById(R.id.bookTitleText);
        }
    }
    public void delete(String url) {
       BookmarkDataBaseHelper bookmarkDataBaseHelper= new BookmarkDataBaseHelper(context, "bookmark.db", null, 2);
        SQLiteDatabase sqLiteDatabase = bookmarkDataBaseHelper.getWritableDatabase();
        sqLiteDatabase.delete("bookmark", "url = ?", new String[]{url});
    }

    public void deleteAll() {
        BookmarkDataBaseHelper bookmarkDataBaseHelper= new BookmarkDataBaseHelper(context, "bookmark", null, 2);
        SQLiteDatabase sqLiteDatabase = bookmarkDataBaseHelper.getWritableDatabase();
        sqLiteDatabase.delete("bookmark", "title != ?", new String[]{""});

    }
}
