package com.example.onlineshoping.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.onlineshoping.ModelClasses.CommentsClass;
import com.example.onlineshoping.ModelClasses.CustomVolleyRequest;
import com.example.onlineshoping.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentAdapterViewHolder>{

    private List<CommentsClass> commentsClasses;
    private Context context;

    private ImageLoader imageLoader;

    public CommentAdapter(List<CommentsClass> commentsClasses1
            , Context ctx) {

        commentsClasses = commentsClasses1;
        context = ctx;
    }


    @Override
    public CommentAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

       /* LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.comment_list, null);
        return new CommentAdapterViewHolder(view);*/
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list, parent, false);
        CommentAdapterViewHolder viewHolder = new CommentAdapterViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(CommentAdapterViewHolder holder, int position) {
       // cQuestions cquestions = questions.get(position);

        CommentsClass commentsClass = commentsClasses.get(position);

        imageLoader = CustomVolleyRequest.getInstance(context).getImageLoader();
        imageLoader.get(commentsClass.getProfile_pic(), ImageLoader.getImageListener(holder.networkImageView, R.drawable.ic_launcher_background, android.R.drawable.ic_dialog_alert));
        holder.networkImageView.setImageUrl(commentsClass.getProfile_pic(), imageLoader);

        holder.textViewName.setText(commentsClass.getName());
        holder.textViewDes.setText(commentsClass.getComment());
        holder.ratingBar.setRating((float) commentsClass.getRating());
        holder.textViewDate.setText(commentsClass.getDate());



    }

    @Override
    public int getItemCount() {

        //  Toast.makeText(QuestionListAdapter.this.context,"selected" + c,Toast.LENGTH_SHORT).show();
        return commentsClasses.size();
    }



    class CommentAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName, textViewDes, textViewDate;
        NetworkImageView networkImageView;
        RatingBar ratingBar;
        public CommentAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            networkImageView = itemView.findViewById(R.id.networkImageViewCmntList);
            textViewName = itemView.findViewById(R.id.textViewNameCmntList);
            textViewDes = itemView.findViewById(R.id.textViewDesCmntList);
            ratingBar = itemView.findViewById(R.id.ratingBarCmntList);
            textViewDate = itemView.findViewById(R.id.textViewDateCmntList);
            //int c = 0 ;



        }


    }
}