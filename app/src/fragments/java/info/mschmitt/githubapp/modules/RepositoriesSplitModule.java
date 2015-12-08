//package info.mschmitt.githubapp.modules;
//
//import javax.inject.Singleton;
//
//import dagger.Module;
//import dagger.Provides;
//import info.mschmitt.githubapp.AnalyticsManager;
//import info.mschmitt.githubapp.presenters.RepositoriesSplitViewPresenter;
//
///**
// * @author Matthias Schmitt
// */
//@Module
//public class RepositoriesSplitModule {
//    private RepositoriesSplitViewPresenter.RepositoriesSplitView mView;
//
//    public RepositoriesSplitModule(RepositoriesSplitViewPresenter.RepositoriesSplitView view) {
//        mView = view;
//    }
//
//    @Provides
//    @Singleton
//    public RepositoriesSplitViewPresenter providePresenter(AnalyticsManager analyticsManager) {
//        return new RepositoriesSplitViewPresenter(mView, analyticsManager);
//    }
//}
