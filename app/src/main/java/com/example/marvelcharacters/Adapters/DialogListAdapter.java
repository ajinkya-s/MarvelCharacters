package com.example.marvelcharacters.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.marvelcharacters.DataContract.CharacterData;
import com.example.marvelcharacters.MainActivity;
import com.example.marvelcharacters.R;

import java.util.List;

public class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.MyViewHolder> {


    private List<CharacterData> dialogList;
    private Context mContext;

    public DialogListAdapter(List<CharacterData> dialogList, Context context) {
        this.dialogList = dialogList;
        this.mContext = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView dialogName;
        View childView;
        LinearLayout dialogListLinerLayout;
        CheckBox checkboxDialog;

        public MyViewHolder(View itemView) {
            super(itemView);
            dialogName = itemView.findViewById(R.id.dialogName);
            childView = itemView;
            dialogListLinerLayout = itemView.findViewById(R.id.dialogListLinerLayout);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dialog_list, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        try {
            final int dataPosition = position;

            myViewHolder.dialogName.setText(dialogList.get(position).getCharacterName());
            myViewHolder.dialogListLinerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) mContext).refreshListafterAddition(dialogList.get(dataPosition));
                    ((MainActivity) mContext).listDialog.cancel();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public int getItemCount() {
        if (dialogList != null)
            return dialogList.size();
        else
            return 0;
    }

}