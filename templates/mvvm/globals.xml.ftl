<?xml version="1.0"?>
<globals>
    <global id="realSrcOut" value="app/src/main/java/${slashedPackageName(localPackageName)}" />
    <!--srcOut pretty much ignores what's in value, bug?-->
    <global id="srcOut" value="${srcDir}/${slashedPackageName(localPackageName)}" />
    <global id="resOut" value="${resDir}" />
</globals>
