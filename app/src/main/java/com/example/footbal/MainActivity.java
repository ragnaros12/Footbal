package com.example.footbal;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_UP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        }
        else{
            buildView();
        }
    }

    public void buildView(){
        List<String> commands = null;

        try {
            BufferedReader br = new BufferedReader(new FileReader(getApplicationContext().getFilesDir().getPath()+ "/test.txt"));
            commands = Arrays.stream(br.readLine().split(",")).collect(Collectors.toList());
        }
        catch (IOException e) {
            commands = Arrays.asList("Команда 1", "Команда 2", "Команда 3", "Команда 4");
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        FootballAdapter footballAdapter = new FootballAdapter(commands, getApplicationContext());

        GridView gridView = findViewById(R.id.grid);
        gridView.setNumColumns(commands.size() + 1);
        gridView.setAdapter(footballAdapter);


        Context context = this;
        findViewById(R.id.start).setOnTouchListener(new View.OnTouchListener() {//при нажатии создаем окно, считаем данныее и выводим
            Dialog fragment;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        fragment = new Dialog(context);
                        fragment.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        fragment.setContentView(R.layout.list_results);
                        fragment.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        fragment.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        fragment.show();

                        HashMap<String, Double> commandsRate = new HashMap<>();
                        for (FootballScore score: footballAdapter.getScores()) {
                            if (!commandsRate.containsKey(score.getFirstCommand()))
                                commandsRate.put(score.getFirstCommand(), (double) 0);
                            if (!commandsRate.containsKey(score.getSecondCommand()))
                                commandsRate.put(score.getSecondCommand(), (double) 0);
                            if (score.getFirstScore() > score.getSecondScore()) {
                                commandsRate.put(score.getFirstCommand(), commandsRate.get(score.getFirstCommand()) + 1);
                            } else if (score.getFirstScore() < score.getSecondScore()) {
                                commandsRate.put(score.getSecondCommand(), commandsRate.get(score.getSecondCommand()) + 1);
                            } else {
                                commandsRate.put(score.getFirstCommand(), commandsRate.get(score.getFirstCommand()) + 0.5);
                                commandsRate.put(score.getSecondCommand(), commandsRate.get(score.getSecondCommand()) + 0.5);
                            }
                            List<Map.Entry<String, Double>> data = new ArrayList<>(commandsRate.entrySet());
                            data.sort((e1, e) -> Double.compare(e.getValue(), e1.getValue()));

                            ((RecyclerView) fragment.findViewById(R.id.list_results)).setAdapter(new AdapterResults(data, getApplicationContext()));
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if(fragment != null){
                            fragment.cancel();
                            fragment = null;
                        }
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            buildView();
        }
    }
}