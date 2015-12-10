package info.mschmitt.githubapp.adapters;

import android.content.Context;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import info.mschmitt.githubapp.android.presentation.BaseAdapterOnListChangedCallback;
import info.mschmitt.githubapp.entities.Repository;

/**
 * @author Matthias Schmitt
 */
public class RepositoryListAdapter extends ArrayAdapter<Repository> {
    private final ObservableList<Repository> mRepositories;
    private final BaseAdapterOnListChangedCallback<Repository> mCallback =
            new BaseAdapterOnListChangedCallback<>(this);

    public RepositoryListAdapter(Context context, ObservableList<Repository> repositories) {
        super(context, 0, repositories);
        mRepositories = repositories;
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

    public void onCreateView(Bundle savedInstanceState) {
        mRepositories.addOnListChangedCallback(mCallback);
    }

    public void onDestroyView() {
        mRepositories.removeOnListChangedCallback(mCallback);
    }

    static class RepositoryHolder {
        TextView text1;

        public RepositoryHolder(View view) {
            text1 = (TextView) view.findViewById(android.R.id.text1);
        }
    }
}
