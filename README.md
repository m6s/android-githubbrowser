# GitHub Browser

Android example application showcasing the following frameworks and build tools:

* Dagger 2 for dependency injection
* The upcoming Android data binding framework and generator
* RxJava to control asynchronous operations and for event propagation
* AutoValue to reduce boilerplate when writing value classes
* Retrofit 2 with Gson and OkHttp for network access
* Retrolambda for back-porting Java 8 lambdas
* Gradle Play Publisher for publishing to the Play Store

Built around the MVVM pattern with a navigation manager. The views are implemented by way of Android
fragments. The application uses one activity and one instance state retaining root fragment, into
which the top level view fragments are placed. Sibling views communicate via Rx subjects, which are
created higher up the containment hierarchy. Every fragment is associated with a dependency
injection scope.

To keep this example short, a data layer has been excluded. I would favor a DAO approach, maybe
modelled after JDBI.

## TODO

* Display copyright information
* Use a `RecyclerView` instead of a `ListView`
* Incorporate latest changes from the data binding framework
* Find a solution to remove Android dependencies from view-models
* Evaluate https://github.com/bluelinelabs/LoganSquare for JSON parsing
* Use Quasar, should it ever make its way to Android
