package com.random.widget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.widget.RemoteViews;

import java.util.Random;

public class RandomWidgetProvider extends AppWidgetProvider {

    private static final String ACTION_UPDATE_RANDOM = "com.random.widget.UPDATE_RANDOM";
    private static final int REQUEST_CODE = 1001;

    private void updateAllWidgets(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName component = new ComponentName(context, RandomWidgetProvider.class);
        int[] ids = manager.getAppWidgetIds(component);

        for (int id : ids) {
            updateAppWidget(context, manager, id);
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager manager, int widgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_random);

        int random = new Random().nextInt(1000);
        views.setTextViewText(R.id.textRandom, String.valueOf(random));

        manager.updateAppWidget(widgetId, views);
    }

    private void scheduleUpdates(Context context) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, RandomWidgetProvider.class);
        intent.setAction(ACTION_UPDATE_RANDOM);

        PendingIntent pending = PendingIntent.getBroadcast(
                context, REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        long interval = 60_000L;
        long trigger = SystemClock.elapsedRealtime() + interval;

        alarm.setInexactRepeating(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                trigger,
                interval,
                pending
        );
    }

    private void cancelUpdates(Context context) {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, RandomWidgetProvider.class);
        intent.setAction(ACTION_UPDATE_RANDOM);

        PendingIntent pending = PendingIntent.getBroadcast(
                context, REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        alarm.cancel(pending);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] ids) {
        for (int id : ids) {
            updateAppWidget(context, manager, id);
        }
        scheduleUpdates(context);
    }

    @Override
    public void onEnabled(Context context) {
        scheduleUpdates(context);
    }

    @Override
    public void onDisabled(Context context) {
        cancelUpdates(context);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (ACTION_UPDATE_RANDOM.equals(intent.getAction())) {
            updateAllWidgets(context);
        }
    }
}
