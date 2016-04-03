package barqsoft.footballscores;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.widget.SimpleCursorAdapter;

public class WidgetScoresAdapter extends SimpleCursorAdapter {

    private static final String LOG_TAG = "WidgetScoresAdapter";
    LayoutInflater mInflater;


    public WidgetScoresAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

}
