package org.rm7370rf.estherproject.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.model.Post;
import org.rm7370rf.estherproject.utils.Utils;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class TopicAdapter extends RealmRecyclerViewAdapter<Post, TopicAdapter.ViewHolder> {
    public TopicAdapter(OrderedRealmCollection<Post> data) {
        super(data, true);
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.topic_recyclerview_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = getItem(position);
        String userName = post.getUserName();
        holder.messageText.setText(post.getMessage());

        if(userName.equals("N/A")) {
            holder.userNameText.setVisibility(View.GONE);
        }
        else {
            holder.userNameText.setText(userName);
        }

        holder.dateText.setText(Utils.timestampToDate(post.getTimestamp()));
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId().longValue();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView userNameText;
        TextView messageText;
        TextView dateText;

        ViewHolder(View view) {
            super(view);
            userNameText = view.findViewById(R.id.userNameText);
            messageText = view.findViewById(R.id.messageText);
            dateText = view.findViewById(R.id.dateText);
        }
    }
}
