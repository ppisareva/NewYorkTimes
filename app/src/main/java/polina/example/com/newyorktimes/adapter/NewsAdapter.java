package polina.example.com.newyorktimes.adapter;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import polina.example.com.newyorktimes.BR;
import polina.example.com.newyorktimes.R;
import polina.example.com.newyorktimes.databinding.NewItemNoImageBinding;
import polina.example.com.newyorktimes.model.New;

/**
 * Created by polina on 9/21/17.
 */

public class NewsAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<New> news;
    Context context;
    public static final int FULL = 1;
    public static final int SHORT = 0;
    private int requestCode =1102123;

    public NewsAdapter(List<New> news, Context context) {
        this.news = news;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case FULL:
                View v = inflater.inflate(R.layout.new_item, parent, false);
                viewHolder = new ViewHolder(v);
                return viewHolder;
            case SHORT:
                View vNoImage = inflater.inflate(R.layout.new_item_no_image, parent, false);
                viewHolder = new ViewHolderNoImage(vNoImage);
                return viewHolder;
        }
      return null;

    }

    @Override
    public int getItemViewType(int position) {
        if (news.get(position).getImageURL()==null||news.get(position).getImageURL().isEmpty()) {
            return SHORT;
        } else if (news.get(position).getImageURL()instanceof String) {
            return FULL;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        New newItem = news.get(position);
        switch (viewHolder.getItemViewType()) {
            case FULL:
                ViewHolder holder = (ViewHolder) viewHolder;
                holder.setNew(newItem);
                holder.tvTitle.setText(newItem.getTitle());
                holder.tvDescription.setText(newItem.getDescription());
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.ivCover.getLayoutParams();
                int h = params.height;
                params.width = context.getResources().getDimensionPixelSize(R.dimen.image_view_width);
                params.height = params.width * newItem.getHeight() / newItem.getWidth();
                if (Math.abs(params.height - h) > 1) {
                    holder.ivCover.setLayoutParams(params);
                }
                Glide.with(context)
                    .load(Uri.parse(newItem.getImageURL()))
                    .into(holder.ivCover);
                break;
            case SHORT:
                ViewHolderNoImage holderNoImage = (ViewHolderNoImage) viewHolder;
                holderNoImage.setNew(newItem);
                holderNoImage.getBinding().setVariable(BR.newItem, newItem);
                holderNoImage.getBinding().executePendingBindings();
                break;
        }

    }


    @Override
    public int getItemCount() {
        return news.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivCover;
        public TextView tvTitle;
        public TextView tvDescription;
        New newItem;


       public ViewHolder(View itemView) {
           super(itemView);
           itemView.setOnClickListener(this);
           ivCover = (ImageView) itemView.findViewById(R.id.ivNew);
           tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
           tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);

       }

        @Override
        public void onClick(View view) {
            openChrome(newItem);
        }

        public void setNew(New aNew) {
            newItem = aNew;
        }


    }

    class ViewHolderNoImage extends RecyclerView.ViewHolder implements View.OnClickListener {

        New newItem;
        private NewItemNoImageBinding binding;


        public ViewHolderNoImage(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            binding.getRoot().setOnClickListener(this);

        }

        public ViewDataBinding getBinding() {
            return binding;
        }

        @Override
        public void onClick(View view) {
           openChrome(newItem);

        }

        public void setNew(New aNew) {
            newItem = aNew;
        }
    }

    private void openChrome(New newItem){
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_action_name);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, newItem.getWebUrl());
        PendingIntent pendingIntent = PendingIntent.getActivity(context,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setActionButton(bitmap, context.getString(R.string.share), pendingIntent, true);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(context, Uri.parse(newItem.getWebUrl()));
    }

}
