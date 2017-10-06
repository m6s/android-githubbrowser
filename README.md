# DEPRECATED

# GitHub Browser

Android example application showcasing the following frameworks and build-tools:

* Dagger 2 for dependency injection
* The upcoming Android data binding framework
* RxJava for asynchronous operations and event propagation
* AutoValue for reducing boilerplate code in value classes
* Retrofit 2 with Gson and OkHttp for network access
* Retrolambda for back-porting Java 8 lambdas
* Gradle Play Publisher for publishing to the Play Store

Built around the MVVM pattern with a navigation manager. The views are implemented by way of Android
fragments. The application uses one activity and one instance state retaining root fragment, into
which the top level view fragments are placed. Sibling views communicate via Rx subjects, which are
created higher up the containment hierarchy. Every fragment constitutes a dependency injection
scope.

To keep this example short, a data layer has been excluded.

## License

Released into the public domain. May contain third-party, copyrighted code and resources.
