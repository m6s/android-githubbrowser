package info.mschmitt.githubapp.app;

import android.content.Context;
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

import info.mschmitt.githubapp.R;
import info.mschmitt.githubapp.android.presentation.FragmentUtils;
import info.mschmitt.githubapp.databinding.UsernameViewBinding;
import info.mschmitt.githubapp.modules.UsernameViewModule;
import info.mschmitt.githubapp.presenters.UsernameViewModel;


public class UsernameViewFragment extends Fragment {
    private FragmentHost mHost;
    private UsernameViewModel mPresenter;
    private NavigationManager mNavigationManager;

    public static UsernameViewFragment newInstance() {
        return new UsernameViewFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHost = FragmentUtils.getParent(this, FragmentHost.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHost.getSuperComponent(this).plus(new UsernameViewModule()).inject(this);
        mNavigationManager.onCreate(this);
        mPresenter.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        UsernameViewBinding binding = UsernameViewBinding.inflate(inflater, container, false);
        binding.setPresenter(mPresenter);
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolbar);
        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPresenter.onSave(outState);
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        mNavigationManager.onDestroy(this);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        mHost = null;
        super.onDetach();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_about:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Inject
    public void setPresenter(UsernameViewModel presenter) {
        mPresenter = presenter;
    }

    @Inject
    public void setNavigationManager(NavigationManager navigationManager) {
        mNavigationManager = navigationManager;
    }

    public interface Component {
        void inject(UsernameViewFragment fragment);
    }

    public interface SuperComponent {
        Component plus(UsernameViewModule module);
    }

    public interface FragmentHost {
        SuperComponent getSuperComponent(UsernameViewFragment fragment);
    }
}
