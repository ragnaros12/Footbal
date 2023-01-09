package com.example.footbal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Map;

public class AdapterResults extends RecyclerView.Adapter<AdapterResults.ResultViewHolder> {//класс который работает со списками
    List<Map.Entry<String, Double>> results;//список результатов
    Context context;//контекст, нужен для инита layoutInflater
    LayoutInflater layoutInflater;//создает элмементы

    public AdapterResults(List<Map.Entry<String, Double>> results, Context context) {
        this.results = results;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {// вызывается когда нужно создать элемент
        return new ResultViewHolder(layoutInflater.inflate(R.layout.item_result, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {//вызывается когда нужно чем то заполнить элемент
        Map.Entry<String, Double> current = results.get(position);
        holder.textView.setText("Команда: " + current.getKey() + "\nРезультат: " + current.getValue() + "\nМесто: " + position);
    }

    @Override
    public int getItemCount() {//размер списка
        return results.size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder{//класс который отвечает за элемент
        TextView textView;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.data_item);
        }
    }
}
