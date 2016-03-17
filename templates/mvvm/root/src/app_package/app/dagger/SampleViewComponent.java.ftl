package ${localPackageName}.app.dagger;

import dagger.Subcomponent;
import ${localPackageName}.ui.fragments.${viewName}ViewFragment;
import ${localPackageName}.ui.scopes.${viewName}ViewScope;

/**
 * @author Matthias Schmitt
 */
@${viewName}ViewScope
@Subcomponent(modules = {${viewName}ViewModule.class})
interface ${viewName}ViewComponent extends ${viewName}ViewFragment.Component {
}
