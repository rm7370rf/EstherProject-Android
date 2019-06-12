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
import org.rm7370rf.estherproject.other.Keys;
import org.rm7370rf.estherproject.util.Utils;

import java.math.BigInteger;

import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.Sort;

import static org.rm7370rf.estherproject.other.Config.MAX_LIST_ITEM_TEXT_LENGTH;

public class TopicsAdapter extends RealmRecyclerViewAdapter<Topic, TopicsAdapter.ViewHolder> {
    private OnItemClickListener listener;
    private int selectedPos = RecyclerView.NO_POSITION;

    public TopicsAdapter() {
        super(
                Realm.getDefaultInstance().where(Topic.class).findAll().sort(Keys.Db.ID, Sort.DESCENDING),
                true
        );
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
        TextView dateText;
        TextView subjectText;
        TextView messageText;

        ViewHolder(View view) {
            super(view);
            dateText = view.findViewById(R.id.dateText);
            subjectText = view.findViewById(R.id.subjectText);
            messageText = view.findViewById(R.id.messageText);
        }

        public void bind(int position) {
            final Topic topic = getItem(position);
            dateText.setText(Utils.timestampToDate(topic.getTimestamp()));
            subjectText.setText(StringUtils.abbreviate(topic.getSubject(), MAX_LIST_ITEM_TEXT_LENGTH));
            messageText.setText(StringUtils.abbreviate(topic.getMessage(), MAX_LIST_ITEM_TEXT_LENGTH));
            itemView.setOnClickListener(v -> {
                selectedPos = getAdapterPosition();
                notifyItemChanged(getAdapterPosition());
                listener.onClick(topic.getId());
            });
        }
    }

    public interface OnItemClickListener {
        void onClick(BigInteger topicId);
    }
}
