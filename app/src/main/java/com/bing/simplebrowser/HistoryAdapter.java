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

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Context context;
    int listPosition;

    private List<History> myHistoryList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        TextView urlText;
        View historyView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            historyView = itemView;//点击的地方
            titleText = itemView.findViewById(R.id.title);
            urlText = itemView.findViewById(R.id.url);
        }
    }

    public HistoryAdapter(List<History> list, Context context) {
        this.context = context;
        myHistoryList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.history_list, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.historyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 listPosition = holder.getAdapterPosition();
                History history = myHistoryList.get(listPosition);
                Intent intent = new Intent(context, Main2Activity.class);
                String url = history.url;
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
                    String url = myHistoryList.get(listPosition).url;
                    myHistoryList.remove(listPosition);
                    notifyDataSetChanged();
                    delete(url);
                }
                if (position == 1) {
                    myHistoryList.clear();
                    notifyDataSetChanged();
                    deleteAll();

                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        History history = myHistoryList.get(i);
        viewHolder.titleText.setText(history.title);
        viewHolder.urlText.setText(history.url);
    }

    @Override
    public int getItemCount() {
        return myHistoryList.size();
    }

    public void delete(String url) {
        HistoryDataBaseHelper historyDataBaseHelper = new HistoryDataBaseHelper(context, "history.db", null, 2);
        SQLiteDatabase sqLiteDatabase = historyDataBaseHelper.getWritableDatabase();
        sqLiteDatabase.delete("history", "url = ?", new String[]{url});
    }

    public void deleteAll() {
        HistoryDataBaseHelper historyDataBaseHelper = new HistoryDataBaseHelper(context, "history.db", null, 2);
        SQLiteDatabase sqLiteDatabase = historyDataBaseHelper.getWritableDatabase();
        sqLiteDatabase.delete("history", "title != ?", new String[]{""});

    }
}
