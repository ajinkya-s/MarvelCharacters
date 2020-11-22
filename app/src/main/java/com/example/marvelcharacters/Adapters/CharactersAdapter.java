package com.example.marvelcharacters.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.marvelcharacters.DataContract.CharacterData;
import com.example.marvelcharacters.Database.DatabaseHelper;
import com.example.marvelcharacters.MainActivity;
import com.example.marvelcharacters.R;

import java.util.List;

public class CharactersAdapter extends RecyclerView.Adapter<CharactersAdapter.MyViewHolder> {

    private List<CharacterData> mData;
    private MainActivity mainActivity;

    public CharactersAdapter(List<CharacterData> data, MainActivity mainActivity) {
        this.mData = data;
        this.mainActivity=mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.character_card, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        final CharacterData characterData = mData.get(i);
        final int position=i;
        myViewHolder.character_name.setText(characterData.getCharacterName());
        myViewHolder.character_description.setText(characterData.getCharacterDescription());
        Glide.with(myViewHolder.character_image.getContext()).load(characterData.getCharacterImage()).into(myViewHolder.character_image);

        myViewHolder.remove_character.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(v.getContext());
                if (db != null) {
                    db.deleteCharacter(characterData,"addedcharacters");
                    db.addCharacter(characterData,"canbeaddedcharacters");
                    mData.remove(position);
                    notifyItemRemoved(position);
                    mainActivity.refreshAfterDelete();
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView character_name, character_description;
        public ImageView character_image,remove_character;

        public MyViewHolder(View view) {
            super(view);
            character_name = view.findViewById(R.id.character_name);
            character_description = view.findViewById(R.id.character_description);
            character_image = view.findViewById(R.id.character_image);
            remove_character = view.findViewById(R.id.remove_character);
        }
    }

}