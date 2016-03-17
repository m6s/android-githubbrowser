<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>

        <import type="android.view.View"/>

        <variable
            name="viewModel"
            type="${localPackageName}.ui.viewmodels.${viewName}ViewModel"/>
    </data>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <android.support.design.widget.AppBarLayout
            android:id="@+id/appBarLayout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <android.support.v7.widget.Toolbar
                android:id="@+id/toolbar"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:minHeight="?attr/actionBarSize"/>
        </android.support.design.widget.AppBarLayout>

        <FrameLayout
            android:layout_width="match_parent"
            android:layout_height="0px"
            android:layout_weight="1">

            <View
                android:layout_width="10dp"
                android:layout_height="10dp"/>
        </FrameLayout>
    </LinearLayout>
</layout>
