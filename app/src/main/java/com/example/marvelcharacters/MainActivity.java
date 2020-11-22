package com.example.marvelcharacters;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marvelcharacters.Adapters.CharactersAdapter;
import com.example.marvelcharacters.Adapters.DialogListAdapter;
import com.example.marvelcharacters.Controllers.MainController;
import com.example.marvelcharacters.DataContract.CharacterData;
import com.example.marvelcharacters.Database.DatabaseHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public Dialog listDialog;
    public SearchView searchView;
    public TextView noDataTextDialog;
    TextView noSearchDataTextDialog;
    RecyclerView recyclerViewForList;
    List<CharacterData> canBeAddedCharactersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initContent();
        initAddCharacterView();
    }

    private void initContent() {
        MainController mainController=new MainController();
        List<CharacterData> initialCharacterDataList = mainController.getMyCharacterData(this);
        if (initialCharacterDataList != null && initialCharacterDataList.size() > 0) {
            RecyclerView my_character_list = findViewById(R.id.my_character_list);
            TextView no_character_text = findViewById(R.id.no_character_text);
            no_character_text.setVisibility(View.GONE);
            my_character_list.setVisibility(View.VISIBLE);

            my_character_list.setLayoutManager(new GridLayoutManager(this, 1));
            CharactersAdapter mAdapter = new CharactersAdapter(initialCharacterDataList, this);
            my_character_list.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initAddCharacterView() {
        ImageView add_character = findViewById(R.id.add_character);
        add_character.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAddCharacterListDialogData();
            }
        });
    }

    private void filterList(String filterString, List<CharacterData> list) {
        try {
            if (list != null && list.size() > 0) {
                List<CharacterData> filteredList = new ArrayList<>();
                for (CharacterData filterValue : list) {
                    if (filterValue.getCharacterName().toLowerCase().contains(filterString.toLowerCase())) {
                        filteredList.add(filterValue);
                    }
                }
                noSearchDataTextDialog.setVisibility(View.GONE);
                DialogListAdapter tagAdapterDealer = new DialogListAdapter(filteredList, this);
                recyclerViewForList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                recyclerViewForList.setItemAnimator(new DefaultItemAnimator());
                recyclerViewForList.setAdapter(tagAdapterDealer);
                tagAdapterDealer.notifyDataSetChanged();
                if (filteredList.size() == 0) {
                    noSearchDataTextDialog.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchViewOnClick(View v) {
        try {
            searchView.setIconified(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setAddCharacterListDialogData() {
        try {
            if (listDialog == null || !listDialog.isShowing()) {
                View dialogView = View.inflate(this, R.layout.add_character_dialog, null);
                listDialog = new Dialog(this);
                listDialog.setContentView(dialogView);
                listDialog.show();

                Window window = listDialog.getWindow();
                WindowManager.LayoutParams windowParameter = window.getAttributes();
                windowParameter.gravity = Gravity.TOP;
                window.setAttributes(windowParameter);
                windowParameter.alpha = 4;

                searchView = dialogView.findViewById(R.id.searchView);
                noDataTextDialog = dialogView.findViewById(R.id.noDataTextDialog);

                canBeAddedCharactersList = new ArrayList<CharacterData>();
                DatabaseHelper db = new DatabaseHelper(this);
                if (db != null) {
                    canBeAddedCharactersList = db.getAllCharacters("canbeaddedcharacters");
                }

                recyclerViewForList = dialogView.findViewById(R.id.recycle_tag);
                noSearchDataTextDialog=dialogView.findViewById(R.id.noSearchDataTextDialog);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String filterString) {
                        filterList(filterString, canBeAddedCharactersList);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String filterString) {
                        filterList(filterString, canBeAddedCharactersList);
                        return true;
                    }
                });

                if (canBeAddedCharactersList.size() > 0) {
                    recyclerViewForList.setVisibility(View.VISIBLE);
                    noDataTextDialog.setVisibility(View.GONE);
                    DialogListAdapter tag_adapter = new DialogListAdapter(canBeAddedCharactersList, this);
                    recyclerViewForList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                    recyclerViewForList.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewForList.setAdapter(tag_adapter);
                    tag_adapter.notifyDataSetChanged();
                } else {
                    recyclerViewForList.setVisibility(View.GONE);
                    noDataTextDialog.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshListafterAddition(CharacterData data) {
        DatabaseHelper db = new DatabaseHelper(this);
        if (db != null) {
            db.addCharacter(data, "addedcharacters");
            db.deleteCharacter(data, "canbeaddedcharacters");

            List<CharacterData> addedCharactersList = db.getAllCharacters("addedcharacters");

            RecyclerView my_character_list = findViewById(R.id.my_character_list);
            TextView no_character_text = findViewById(R.id.no_character_text);
            no_character_text.setVisibility(View.GONE);
            my_character_list.setVisibility(View.VISIBLE);

            my_character_list.setLayoutManager(new GridLayoutManager(this, 1));
            CharactersAdapter mAdapter = new CharactersAdapter(addedCharactersList, this);
            my_character_list.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void refreshAfterDelete() {
        DatabaseHelper db = new DatabaseHelper(this);
        if (db != null) {
            List<CharacterData> addedCharactersList = new ArrayList<CharacterData>();
            addedCharactersList = db.getAllCharacters("addedcharacters");
            if (addedCharactersList.size() == 0) {
                RecyclerView my_character_list = findViewById(R.id.my_character_list);
                TextView no_character_text = findViewById(R.id.no_character_text);
                no_character_text.setVisibility(View.VISIBLE);
                my_character_list.setVisibility(View.GONE);
            }
            else {
                RecyclerView my_character_list = findViewById(R.id.my_character_list);
                my_character_list.setLayoutManager(new GridLayoutManager(this, 1));
                CharactersAdapter mAdapter = new CharactersAdapter(addedCharactersList, this);
                my_character_list.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}