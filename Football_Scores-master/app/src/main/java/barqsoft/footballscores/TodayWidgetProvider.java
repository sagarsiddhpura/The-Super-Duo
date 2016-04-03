package barqsoft.footballscores;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import barqsoft.footballscores.service.ListViewService;
import barqsoft.footballscores.ui.activities.MainActivity;

public class TodayWidgetProvider extends AppWidgetProvider {

    public static final String TOAST_ACTION = "com.example.android.barqsoft.footballscores.TOAST_ACTION";
    public static final String EXTRA_ITEM = "com.example.android.barqsoft.footballscores.EXTRA_ITEM";

    private static final String LOG_TAG = "TodayWidgetProvider";
    private ListView listView;

    public void onReceive(Context context, Intent intent) {
        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(TOAST_ACTION)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int viewIndex = intent.getIntExtra(EXTRA_ITEM, 0);
            Toast.makeText(context, "Touched view " + viewIndex, Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

       // Log.v("LAUNCH WIDGET",  "BEGIN");




        for (int appWidgetId : appWidgetIds) {

            Intent intent = new Intent(context, ListViewService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_small);
            //remoteViews.setTextViewText(R.id.team1, "1");
            remoteViews.setTextViewText(R.id.day_of_the_week, getDayOfWeekNow(null));
            remoteViews.setTextViewText(R.id.date_today, getDayMonthNow());

            remoteViews.setRemoteAdapter(R.id.widget_list_view, intent);

            remoteViews.setEmptyView(R.id.widget_list_view, R.id.empty_view);


            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);


            Intent lauchAppIntent =  new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,lauchAppIntent, 0);
            remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

         //   Log.v("LAUNCH WIDGET", "end");

        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private String getDayOfWeekNow(Date passedDate){
        Calendar calendar = Calendar.getInstance();
        if(passedDate != null){
            calendar.setTime(passedDate);
        }
        int dayOfWeekNumeric = calendar.get(Calendar.DAY_OF_WEEK);
        String dayOfWeek;

        switch (dayOfWeekNumeric){
            case 1: dayOfWeek = "Sun"; break;
            case 2: dayOfWeek = "Mon"; break;
            case 3: dayOfWeek = "Tue"; break;
            case 4: dayOfWeek = "Wed"; break;
            case 5: dayOfWeek = "Thru"; break;
            case 6: dayOfWeek = "Fri"; break;
            case 7: dayOfWeek = "Sat"; break;
            default:
                dayOfWeek = "Invalid";
        }
        return dayOfWeek;
    }

    private String getDayMonthNow() {
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DATE);
        int monthNumeric = calendar.get(Calendar.MONTH);
        String month;
        switch (monthNumeric){
            case 0: month = "January"; break;
            case 1: month = "February"; break;
            case 2: month = "March"; break;
            case 3: month = "April"; break;
            case 4: month = "May"; break;
            case 5: month = "June"; break;
            case 6: month = "July"; break;
            case 7: month = "August"; break;
            case 8: month = "September"; break;
            case 9: month = "October"; break;
            case 10: month = "November"; break;
            case 11: month = "December"; break;
            default:
                month = "Error";
        }

        return (month + " " + date);
    }

}

