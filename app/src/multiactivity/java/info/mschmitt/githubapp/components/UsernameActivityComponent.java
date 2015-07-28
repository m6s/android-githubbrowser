package info.mschmitt.githubapp.components;

import dagger.Subcomponent;
import info.mschmitt.githubapp.UsernameActivity;
import info.mschmitt.githubapp.modules.UsernameActivityModule;
import info.mschmitt.githubapp.android.scopes.ActivityScope;

/**
 * @author Matthias Schmitt
 */
@ActivityScope
@Subcomponent(modules = {UsernameActivityModule.class})
public interface UsernameActivityComponent {
    void inject(UsernameActivity activity);
}
