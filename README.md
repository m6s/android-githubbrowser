# GitHub Browser

Example Android application showcasing the following frameworks and build tools:

* Dagger 2 for dependency injection
* The upcoming Android data binding framework and generator
* Retrofit 1 with Gson and OkHttp for network access
* RxJava to facilitate asynchronous operations
* Retrolambda for back-porting Java 8 lambdas
* Gradle Play Publisher for publishing to the Play Store

Built around the model–view–viewmodel pattern with a navigation manager. The views are implemented
through Android fragments, with one activity and one instance state retaining root fragment.

To keep this example short and (arguably) simple, a data layer has been excluded. Most
applications would want to go through a database when displaying network supplied data.

## TODO

* View for displaying third-party licenses
* Update to Retrofit 2 and maybe Jackson
* Replace `ListView` with `RecyclerView`
* Replace RxJava with Quasar should it ever become available on Android
