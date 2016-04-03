package barqsoft.footballscores.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.TodayWidgetProvider;
import barqsoft.footballscores.ui.widget.WidgetItem;

public class ListViewService extends RemoteViewsService {

    private boolean putDate = true;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{
        private int mCount;
        private List<WidgetItem> mWidgetItems = new ArrayList<>();
        private Context context;
        private int mAppWidgetId;

        public ListViewRemoteViewsFactory(Context context, Intent intent){
           // Log.v("ListViewService", "ListViewRemoteViewsFactory");
            this.context = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        }

        private String getTodayDate(){
            return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }

        public void onCreate(){
           // Log.v("ListViewService", "onCreate");


            Cursor data = context.getContentResolver().query(
                    DatabaseContract.BASE_CONTENT_URI,
                    null,
                    DatabaseContract.scores_table.DATE_COL + " >= ? ",
                    new String[]{getTodayDate()},
                    //null,
                    //null,
                    DatabaseContract.scores_table.DATE_COL
            );

            if (data == null) {
               // Log.v("LOG_TAG", "data is null");
                return;
            }
            if (!data.moveToFirst()) {
              //  Log.v("LOG_TAG", "NO DATA");
                data.close();
                return;
            }
            this.mCount = data.getCount();

            String team1Text, team2Text, timeText, dateText;

            data.moveToFirst();
            for (int i = 0; i < mCount; i++){

            //while(data.moveToNext()) {
                team1Text = data.getString(data.getColumnIndex(DatabaseContract.scores_table.HOME_COL));
                team2Text = data.getString(data.getColumnIndex(DatabaseContract.scores_table.AWAY_COL));
                timeText = data.getString(data.getColumnIndex(DatabaseContract.scores_table.TIME_COL));
                dateText = data.getString(data.getColumnIndex(DatabaseContract.scores_table.DATE_COL));

                mWidgetItems.add(new WidgetItem(team1Text, team2Text, timeText, dateText));
                data.moveToNext();
           // }data.moveToFirst();
                //getViewAt(i);
            }
           // for(int a = 0; a < mWidgetItems.size(); a++){
               // Log.v("ITEMS IN THE LIST", mWidgetItems.get(a).toString());
           // }
            List<WidgetItem> copyItems = new ArrayList<>();

            for(int i = 0; i < mCount; i++){
                if((i != 0) && (mWidgetItems.get(i-1).getDate().equals(mWidgetItems.get(i).getDate()))){
                    copyItems.add(new WidgetItem(mWidgetItems.get(i).getTeam1(), mWidgetItems.get(i).getTeam2(), mWidgetItems.get(i).getTime(), ""));
                }else {
                    copyItems.add(new WidgetItem(mWidgetItems.get(i).getTeam1(), mWidgetItems.get(i).getTeam2(), mWidgetItems.get(i).getTime(), mWidgetItems.get(i).getDate()));
                }
            }

            this.mWidgetItems = copyItems;

        }



        public RemoteViews getViewAt(int position) {

            // Construct a RemoteViews item based on the app widget item XML file, and set the
            // text based on the position.
            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);

            rv.setTextViewText(R.id.team1, mWidgetItems.get(position).getTeam1());
            rv.setTextViewText(R.id.team2, mWidgetItems.get(position).getTeam2());
            rv.setTextViewText(R.id.time, mWidgetItems.get(position).getTime());
            try {
                String finalDate = getMatchDate(mWidgetItems.get(position).getDate());
                rv.setTextViewText(R.id.date, finalDate);
                if(!"".equals(finalDate)) {
                    rv.setTextViewText(R.id.day_week, getDayOfWeekNow(mWidgetItems.get(position).getDate()));
                }else{
                    rv.setTextViewText(R.id.day_week, "");
                }
            }catch (Exception e){
                e.printStackTrace();
            }

            // Next, set a fill-intent, which will be used to fill in the pending intent template
            // that is set on the collection view in StackWidgetProvider.
            Bundle extras = new Bundle();
            extras.putInt(TodayWidgetProvider.EXTRA_ITEM, position);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            // Make it possible to distinguish the individual on-click
            // action of a given item
            rv.setOnClickFillInIntent(R.id.widget_list_view, fillInIntent);

            // Return the RemoteViews object.
            return rv;
        }

        @Override
        public int getCount() {
            //must return the number of items, because if 0 by default, you will always end up with an empty list
            return this.mCount;
        }

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        private String getMatchDate(String date)throws Exception{
            if("".equals(date)){
                return "";
            }
            StringBuilder formattedDate = new StringBuilder();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date convertedDate = sdf.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(convertedDate);
            return String.valueOf(calendar.get(Calendar.DATE));
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

        private String getDayOfWeekNow(String passedDate) throws Exception{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date convertedDate = sdf.parse(passedDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(convertedDate);
            int dayOfWeekNumeric = calendar.get(Calendar.DAY_OF_WEEK);
            String dayOfWeek;

            switch (dayOfWeekNumeric){
                case 1: dayOfWeek = "Sun"; break;
                case 2: dayOfWeek = "Mon"; break;
                case 3: dayOfWeek = "Tue"; break;
                case 4: dayOfWeek = "Wed"; break;
                case 5: dayOfWeek = "Thu"; break;
                case 6: dayOfWeek = "Fri"; break;
                case 7: dayOfWeek = "Sat"; break;
                default:
                    dayOfWeek = "Invalid";
            }
            return dayOfWeek;
        }



    }
}
