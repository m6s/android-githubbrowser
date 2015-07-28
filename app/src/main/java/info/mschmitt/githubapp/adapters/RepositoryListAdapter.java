package info.mschmitt.githubapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import info.mschmitt.githubapp.entities.Repository;

/**
 * @author Matthias Schmitt
 */
public class RepositoryListAdapter extends ArrayAdapter<Repository> {
    public RepositoryListAdapter(Context context, List<Repository> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RepositoryHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            holder = new RepositoryHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (RepositoryHolder) convertView.getTag();
        }
        Repository repository = getItem(position);
        holder.text1.setText(repository.getName());
        return convertView;
    }

    static class RepositoryHolder {
        TextView text1;

        public RepositoryHolder(View view) {
            text1 = (TextView) view.findViewById(android.R.id.text1);
        }
    }
}
