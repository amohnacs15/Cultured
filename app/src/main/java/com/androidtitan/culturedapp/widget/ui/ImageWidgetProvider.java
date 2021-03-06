package com.androidtitan.culturedapp.widget.ui;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.androidtitan.culturedapp.R;
import com.androidtitan.culturedapp.main.CulturedApp;
import com.androidtitan.culturedapp.main.newsfeed.ui.NewsDetailActivity;
import com.androidtitan.culturedapp.main.toparticle.TopArticleMvp;
import com.androidtitan.culturedapp.main.toparticle.TopArticleProvider;
import com.androidtitan.culturedapp.model.newyorktimes.Article;
import com.androidtitan.culturedapp.model.newyorktimes.Facet;
import com.androidtitan.culturedapp.model.newyorktimes.FacetType;
import com.androidtitan.culturedapp.model.newyorktimes.Multimedium;
import com.androidtitan.culturedapp.widget.AlarmBroadcastReceiver;
import com.androidtitan.culturedapp.widget.AppWidgetProviderConfigureActivity;
import com.androidtitan.culturedapp.widget.WidgetSharedUpdater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.androidtitan.culturedapp.common.Constants.ARTICLE_EXTRA;
import static com.androidtitan.culturedapp.common.Constants.ARTICLE_GEO_FACETS;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link AppWidgetProviderConfigureActivity AppWidgetProviderConfigureActivity}
 */
public class ImageWidgetProvider extends AppWidgetProvider implements TopArticleMvp.Provider.CallbackListener {
    private final static String TAG = ImageWidgetProvider.class.getCanonicalName();

    public static final String ALARM_BROADCAST_TITLE = "ImageWidgetProvider.alarmBroadcastTitle";
    public static final String ALARM_BROADCAST_GEO_FACET = "ImageWidgetProvider.alarmBroadcastGeoFacet";
    public static final String ALARM_BROADCAST_WIDTH = "ImageWidgetProvider.width";
    public static final String ALARM_BROADCAST_HEIGHT = "ImageWidgetProvider.height";
    public static final String ALARM_BROADCAST_URL = "ImageWidgetProvider.url";

    private static final int ALARM_INTERVAL = 5000; //300000;

    private Context context;
    private AppWidgetManager appWidgetManager;
    private int[] appWidgetIds;

    private TopArticleProvider provider;
    private Article providerArticle;

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    //Enter relevant functionality for when the first widget is created
    //Think of this as your onCreate()
    @Override
    public void onEnabled(Context context) {

        super.onEnabled(context);
        this.context = context;

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e(TAG, "onUpdate()");
        provider = TopArticleProvider.getInstance(CulturedApp.getAppContext());
        if(providerArticle == null) {
            provider.fetchArticles(this);
        }

        this.appWidgetManager = appWidgetManager;
        this.appWidgetIds = appWidgetIds;

        RemoteViews views = new RemoteViews(CulturedApp.getAppContext().getPackageName(), R.layout.image_widget_provider);

        if (appWidgetIds != null && appWidgetIds.length > 0) {
            // There may be multiple widgets active, so update all of them
            for (int appWidgetId : appWidgetIds) {
                WidgetSharedUpdater.updateAppWidget(views, appWidgetManager, appWidgetId, providerArticle);
            }
        }

        if(providerArticle != null) {
            updateRepeatingAlarm(context);

            PendingIntent detailArticlePendingIntent = buildOnClickPendingIntent();
            views.setOnClickPendingIntent(R.id.articleImageView, detailArticlePendingIntent);
        }

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Log.e(TAG, "onDeleted()");

        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            //AppWidgetProviderConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onDisabled(Context context) {
        Log.e(TAG, "onDisabled()");

        AlarmManager timeCop = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        PendingIntent broadcastPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        timeCop.cancel(broadcastPendingIntent);
    }


    private PendingIntent buildOnClickPendingIntent() {
        Intent intent = new Intent(CulturedApp.getAppContext(), NewsDetailActivity.class);
        intent.putExtra(ARTICLE_EXTRA, providerArticle);
        intent.putStringArrayListExtra(ARTICLE_GEO_FACETS, getGeoFacetArrayList(providerArticle));

        return PendingIntent.getActivity(CulturedApp.getAppContext(), 200, intent, 0);
    }

    @Override
    public void onArticleConstructionComplete(ArrayList<Article> articleArrayList) {

        if (articleArrayList.size() > 0) {
            providerArticle = articleArrayList.get(0);
            for(Article article : articleArrayList) {
                if(!providerArticle.getMultimedia().isEmpty()) {
                    providerArticle = article;

                    Log.e(TAG, "onArticleConstruction Complete");

                    this.onUpdate(context, appWidgetManager, appWidgetIds);
                    return;
                }
            }
        }
    }

    @Override
    public void onFacetConstructionComplete(HashMap<FacetType, HashMap<Integer, List<Facet>>> facetArrayList) {
        //
    }

    @Override
    public void cursorDataNotAvailable() {
        //no-op
    }

    @Override
    public void cursorDataEmpty() {
        //no-op
    }

    /**
     * AlarmManager is one way to periodically update your widget.
     * android:updatePeriodMillis in the xml resource does the same
     *
     * @param context
     */
    private void updateRepeatingAlarm(Context context) {

        if(context != null) {
            AlarmManager timeCop = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmBroadcastReceiver.class);

            Facet workingFacet = providerArticle.getGeoFacet().get(0);
            Multimedium workingMultimedium = providerArticle.getMultimedia().get(0);

            intent.putExtra(ALARM_BROADCAST_TITLE, providerArticle.getTitle());
            intent.putExtra(ALARM_BROADCAST_GEO_FACET, workingFacet.getFacetText());
            intent.putExtra(ALARM_BROADCAST_URL, workingMultimedium.getUrl());
            intent.putExtra(ALARM_BROADCAST_WIDTH, workingMultimedium.getWidth());
            intent.putExtra(ALARM_BROADCAST_HEIGHT, workingMultimedium.getHeight());

            PendingIntent broadcastPendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            timeCop.setRepeating(AlarmManager.RTC_WAKEUP, ALARM_INTERVAL, ALARM_INTERVAL, broadcastPendingIntent);
        }
    }

    private ArrayList<String> getGeoFacetArrayList(@NonNull Article article) {
        ArrayList<String> facets = new ArrayList<>();
        for (Facet facet : article.getGeoFacet()) {
            facets.add(facet.getFacetText());
        }
        return facets;
    }
}

