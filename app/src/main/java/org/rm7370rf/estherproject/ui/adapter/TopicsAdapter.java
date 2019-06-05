package org.rm7370rf.estherproject.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.lang3.StringUtils;
import org.rm7370rf.estherproject.R;
import org.rm7370rf.estherproject.model.Topic;

import java.math.BigInteger;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

import static org.rm7370rf.estherproject.utils.Config.MAX_LIST_ITEM_TEXT_LENGTH;

public class TopicsAdapter extends RealmRecyclerViewAdapter<Topic, TopicsAdapter.ViewHolder> {
    private OnItemClickListener listener;
    private int selectedPos = RecyclerView.NO_POSITION;

    public TopicsAdapter(OrderedRealmCollection<Topic> data) {
        super(data, true);
        setHasStableIds(true);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.topics_recyclerview_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
        holder.itemView.setSelected(selectedPos == position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId().longValue();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView numberText;
        TextView subjectText;
        TextView messageText;

        ViewHolder(View view) {
            super(view);
            numberText = view.findViewById(R.id.numberText);
            subjectText = view.findViewById(R.id.subjectText);
            messageText = view.findViewById(R.id.messageText);
        }

        public void bind(int position) {
            final Topic topic = getItem(position);
            numberText.setText(String.valueOf(topic.getId()));
            subjectText.setText(StringUtils.abbreviate(topic.getSubject(), MAX_LIST_ITEM_TEXT_LENGTH));
            messageText.setText(StringUtils.abbreviate(topic.getMessage(), MAX_LIST_ITEM_TEXT_LENGTH));
            itemView.setOnClickListener(v -> {
                selectedPos = getAdapterPosition();
                notifyItemChanged(getAdapterPosition());
                listener.onClick(topic.getId());
            });
        }
    }

    interface OnItemClickListener {
        void onClick(BigInteger topicId);
    }
}
