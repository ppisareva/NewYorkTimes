package polina.example.com.newyorktimes.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import polina.example.com.newyorktimes.R;
import polina.example.com.newyorktimes.activities.WebViewerActivity;
import polina.example.com.newyorktimes.model.New;

import static android.os.Build.VERSION_CODES.N;
import static java.security.AccessController.getContext;

/**
 * Created by polina on 9/21/17.
 */

public class NewsAdapter  extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    List<New> news;
    Context context;

    public NewsAdapter(List<New> news, Context context) {
        this.news = news;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View bookView = inflater.inflate(R.layout.new_item, parent, false);
        NewsAdapter.ViewHolder viewHolder = new NewsAdapter.ViewHolder(bookView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        New newItem = news.get(position);
        holder.setNew(newItem);
        holder.tvTitle.setText(newItem.getTitle());
        holder.tvDescription.setText(newItem.getDescription());
        if(newItem.getDesk()!=null&&!newItem.getDesk().equals("None")) {
            holder.tvDesk.setVisibility(View.VISIBLE);
            holder.tvDesk.setText(newItem.getDesk());
        } else {
            holder.tvDesk.setVisibility(View.GONE);
        }
        Glide.with(context)
                .load(Uri.parse(newItem.getImageURL()))
                .placeholder(android.R.drawable.alert_dark_frame)
                .into(holder.ivCover);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvDescription;
        private TextView tvDesk;
        New newItem;


       public ViewHolder(View itemView) {
           super(itemView);
           itemView.setOnClickListener(this);
           ivCover = (ImageView) itemView.findViewById(R.id.ivNew);
           tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
           tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
           tvDesk = (TextView) itemView.findViewById(R.id.tvDesk);

       }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, WebViewerActivity.class);
            intent.putExtra("url", newItem.getWebUrl());
            context.startActivity(intent);

        }

        public void setNew(New aNew) {
            newItem = aNew;
        }
    }
}
