package com.example.footbal;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FootballAdapter extends BaseAdapter {
    List<String> items;//то же самое что и в AdapterResults
    Context context;
    LayoutInflater layoutInflater;
    List<FootballScore> scores;

    public FootballAdapter(List<String> items, Context context) {
        this.items = items;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        scores = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return (items.size() + 1) * (items.size() + 1);
    }// размер сетки будет кол-во команд на себя же, и + 1 для их обьявления

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {// получаем тип элемента. Если это первый, то это просто текст, если это 1 колонка то там все элементы будут текстом обьяления команд, такой же прикол и с колонками. Если все оставшееся то это либо
        //мяч(если команды совпадают), либо текстовое поле
        return i == 0 ? 0
                : i < items.size() + 1 ? 1
                : i % (items.size() + 1) == 0 ? 2
                : i / (items.size() + 1) == i % (items.size() + 1) ? 3 :
                4;
    }

    public List<FootballScore> getScores() {
        return scores;
    }

    @Override
    public View getView(int i, View view1, ViewGroup parent) {
        View view = null;
        switch ((int) getItemId(i)){// смотрим какой элемент
            case 0:
                view = layoutInflater.inflate(R.layout.command_name, parent, false);
                ((TextView)view.findViewById(R.id.football_command_name)).setText("старт");
                break;
            case 1:
                view = layoutInflater.inflate(R.layout.command_name, parent, false);
                ((TextView)view.findViewById(R.id.football_command_name)).setText(items.get(i - 1));
                break;
            case 2:
                view = layoutInflater.inflate(R.layout.command_name, parent, false);
                ((TextView)view.findViewById(R.id.football_command_name)).setText(items.get((i - 1) % items.size()));
                break;
            case 3:
                view = layoutInflater.inflate(R.layout.football_command_equels, parent, false);
                break;
            case 4:
                view = layoutInflater.inflate(R.layout.edit_cell, parent, false);
                String firstCommand = items.get(i / (items.size() + 1) - 1);
                String secondCommand = items.get(
                        i % (items.size() + 1) - 1
                );//получаем имя 1 и 2 комамнды
                FootballScore footballScore = new FootballScore(0, 0, firstCommand, secondCommand);// создаем для них счет
                EditText editText = view.findViewById(R.id.edit_score);
                editText.setText("0/0");
                editText.addTextChangedListener(new TextWatcher() {//при изменении текста будем что-то делать
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {//когда текст меняется пытаемся забрать значение, проверить его, и если все ок, то пишем в счет команды
                        String[] values = editable.toString().split("/");
                        if(values.length == 2){
                            if(!values[0].matches("-?\\d+(\\.\\d+)?") || !values[1].matches("-?\\d+(\\.\\d+)?"))
                                return;
                            footballScore.setFirstScore(Integer.parseInt(values[0]));
                            footballScore.setSecondScore(Integer.parseInt(values[1]));
                        }
                    }
                });


                if (scores.stream().noneMatch(u -> u.getFirstCommand().equals(firstCommand) && u.getSecondCommand().equals(secondCommand))) {// и тут мы должны либо добавить команду, если такой нет, но!! Матч может быть и в обратную сторону, поэтому нужно проверить что так не будет. По
                    //поэтому если обратный матч уже существует, то не надо его добавлять
                    if(scores.stream().anyMatch(u -> u.getFirstCommand().equals(secondCommand) && u.getSecondCommand().equals(firstCommand)))
                        editText.setEnabled(false);
                    else
                        scores.add(footballScore);
                }


                break;
        }
        return view;
    }
}
