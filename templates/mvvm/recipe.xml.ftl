<?xml version="1.0"?>
<recipe>

   <instantiate from="res/layout/sample_view.xml.ftl"
              to="${escapeXmlAttribute(resOut)}/layout/${viewName?lower_case}_view.xml" />

   <instantiate from="res/menu/sample.xml.ftl"
              to="${escapeXmlAttribute(resOut)}/menu/${viewName?lower_case}.xml" />

    <instantiate from="src/app_package/ui/fragments/SampleViewFragment.java.ftl"
                       to="${escapeXmlAttribute(realSrcOut)}/ui/fragments/${viewName}ViewFragment.java" />

    <instantiate from="src/app_package/ui/scopes/SampleViewScope.java.ftl"
                       to="${escapeXmlAttribute(realSrcOut)}/ui/scopes/${viewName}ViewScope.java" />

    <instantiate from="src/app_package/ui/viewmodels/SampleViewModel.java.ftl"
                       to="${escapeXmlAttribute(realSrcOut)}/ui/viewmodels/${viewName}ViewModel.java" />

    <instantiate from="src/app_package/app/dagger/SampleViewComponent.java.ftl"
                       to="${escapeXmlAttribute(realSrcOut)}/app/dagger/${viewName}ViewComponent.java" />

    <instantiate from="src/app_package/app/dagger/SampleViewModule.java.ftl"
                       to="${escapeXmlAttribute(realSrcOut)}/app/dagger/${viewName}ViewModule.java" />


    <open file="${escapeXmlAttribute(realSrcOut)}/ui/fragments/${viewName}ViewModel.java" />
   
</recipe>
