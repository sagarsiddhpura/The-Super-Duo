package barqsoft.footballscores.ui.widget;

public class WidgetItem {
    private String team1;
    private String team2;
    private String time;
    private String date;

    public WidgetItem(String team1, String team2, String time, String date) {
        this.team1 = team1;
        this.team2 = team2;
        this.time = time;
        this.date = date;
    }

    @Override
    public String toString() {
        return "WidgetItem{" +
                "team1='" + team1 + '\'' +
                ", team2='" + team2 + '\'' +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
