package com.example.onlineshoping.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshoping.Activities.ByCategoryListShow;
import com.example.onlineshoping.R;

import java.util.ArrayList;

//public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

/*    private Context context;
    private LayoutInflater inflater;
    List<SearchData> data = Collections.emptyList();
    SearchData current;

    // create constructor to initialize context and data sent from MainActivity
    public SearchAdapter(Context context, List<SearchData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    // Inflate the layout when ViewHolder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // View view = inflater.inflate(R.layout.search_list, parent, false);
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list, parent, false);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        // Get current position of item in RecyclerView to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        SearchData current = data.get(position);
        myHolder.textViewName.setText(current.Name);


    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }






    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewName;
        ConstraintLayout constraintLayout;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewSearchName);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.containerLayoutSearch);
            itemView.setOnClickListener(this);
        }*/


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    int count = 10;
    private ArrayList<String> names;
    Context context;
    public SearchAdapter(ArrayList<String> names,Context context) {
        this.names = names;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textViewName.setText(names.get(position));

        holder.textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ByCategoryListShow.class);
                intent.putExtra("searchItem", holder.textViewName.getText().toString());
                intent.putExtra("category", holder.textViewName.getText().toString());
                context.startActivity(intent);
                context.fileList();
                Toast.makeText(context, "You clicked an item", Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public int getItemCount() {

        if(names.size() > count){
            return count;
        }
        else
        {
            return names.size();
        }

       // return names.size();
    }

    public void filterList(ArrayList<String> filterdNames) {
        this.names = filterdNames;

        notifyDataSetChanged();
    }

    public ArrayList<String> getFilter() {
        return names;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;

        ViewHolder(View itemView) {
            super(itemView);

            textViewName = (TextView) itemView.findViewById(R.id.textViewSearchName);


        }
    }




        // Click event for all items
        /*@Override
        public void onClick(View v) {

            Toast.makeText(context, "You clicked an item", Toast.LENGTH_SHORT).show();

        }
*/

}


