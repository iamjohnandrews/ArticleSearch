package Model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by andrj148 on 7/29/16.
 */
public class Article implements Parcelable {
    String webURL;
    String headline;
    String thumbnail;
    String snippet;
    String date;
    private static final String nyTimesURL = "http://www.nytimes.com/";

    public String getWebURL() {
        return webURL;
    }

    public String getHeadline() {
        return headline;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getSnippet() {
        return snippet;
    }

    public String getDate() {
        return date;
    }

    public Article(JSONObject jsonObject) {

        try {
            this.webURL = jsonObject.getString("web_url");
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            this.snippet = jsonObject.getString("snippet");
            this.date = convertIntoReadableDateFormat(jsonObject.getString("pub_date"));

            JSONArray multimedia = jsonObject.getJSONArray("multimedia");
            if (multimedia.length() > 0) {
                JSONObject multimediaJSON = multimedia.getJSONObject(0);
                this.thumbnail = nyTimesURL + multimediaJSON.getString("url");
            } else {
                this.thumbnail = "";
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String convertIntoReadableDateFormat(String date) {
        String readableDate = "n/a";

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date setDate = dateFormatter.parse(date);
            dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            readableDate = dateFormatter.format(setDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return readableDate;
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array) {
        ArrayList<Article> results = new ArrayList<>();

        for (int i = 0; array.length() > i; i++ ) {
            try {
                results.add(new Article(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.webURL);
        dest.writeString(this.headline);
        dest.writeString(this.thumbnail);
        dest.writeString(this.snippet);
        dest.writeString(this.date);
    }

    protected Article(Parcel in) {
        this.webURL = in.readString();
        this.headline = in.readString();
        this.thumbnail = in.readString();
        this.snippet = in.readString();
        this.date = in.readString();
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>() {
        @Override
        public Article createFromParcel(Parcel source) {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size) {
            return new Article[size];
        }
    };
}
