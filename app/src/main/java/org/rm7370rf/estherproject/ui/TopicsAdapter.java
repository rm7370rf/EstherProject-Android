package org.rm7370rf.estherproject.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;
import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.model.Topic;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

import static org.rm7370rf.estherproject.utils.Config.MAX_LIST_ITEM_TEXT_LENGTH;

public class TopicsAdapter extends RealmRecyclerViewAdapter<Topic, TopicsAdapter.ViewHolder> {
    public TopicsAdapter(OrderedRealmCollection<Topic> data) {
        super(data, true);
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.topics_recyclerview_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Topic topic = getItem(position);
        holder.itemNumberText.setText(String.valueOf(position + 1));
        holder.itemSubjectText.setText(StringUtils.abbreviate(topic.getSubject(), MAX_LIST_ITEM_TEXT_LENGTH));
        holder.itemMessageText.setText(StringUtils.abbreviate(topic.getMessage(), MAX_LIST_ITEM_TEXT_LENGTH));
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId().longValue();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNumberText;
        TextView itemSubjectText;
        TextView itemMessageText;

        ViewHolder(View view) {
            super(view);
            itemNumberText = view.findViewById(R.id.itemNumber);
            itemSubjectText = view.findViewById(R.id.itemSubject);
            itemMessageText = view.findViewById(R.id.itemMessage);
        }
    }
}
