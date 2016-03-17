package ${localPackageName}.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import ${localPackageName}.R;
import ${localPackageName}.android.FragmentUtils;
import ${localPackageName}.databinding.${viewName}ViewBinding;
import ${localPackageName}.ui.viewmodels.${viewName}ViewModel;

/**
 * @author Matthias Schmitt
 */
public class ${viewName}ViewFragment extends Fragment {
    @Inject ${viewName}ViewModel mViewModel;

    public static ${viewName}ViewFragment newInstance() {
        return new ${viewName}ViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentUtils.getHost(this, FragmentHost.class).${viewName?lower_case}ViewComponent(this).inject(this);
        mViewModel.onLoad(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ${viewName}ViewBinding binding = ${viewName}ViewBinding.inflate(inflater, container, false);
        binding.setViewModel(mViewModel);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mViewModel.onSave(outState);
    }

    @Override
    public void onPause() {
        mViewModel.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mViewModel = null;
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.${viewName?lower_case}, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public interface FragmentHost {
        Component ${viewName?lower_case}ViewComponent(${viewName}ViewFragment fragment);
    }

    public interface Component {
        void inject(${viewName}ViewFragment fragment);
    }
}
