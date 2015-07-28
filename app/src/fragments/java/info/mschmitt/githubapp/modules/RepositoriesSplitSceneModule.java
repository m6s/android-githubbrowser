package info.mschmitt.githubapp.modules;

import java.util.List;

import dagger.Module;
import dagger.Provides;
import info.mschmitt.githubapp.entities.Repository;
import rx.Observable;

/**
 * @author Matthias Schmitt
 */
@Module
public class RepositoriesSplitSceneModule {
    private Observable<List<Repository>> mRepositories;

    public RepositoriesSplitSceneModule(Observable<List<Repository>> repositories) {
        mRepositories = repositories;
    }

    @Provides
    Observable<List<Repository>> provideRepositories() {
        return mRepositories;
    }
}
