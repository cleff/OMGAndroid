package com.mvmdata.chris.omgandroid.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by chris on 4/8/14.
 */
public class JSONAdapter extends BaseAdapter {
    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;

    public JSONAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        mJsonArray = new JSONArray();


    }


    @Override
    public int getCount() {

        return mJsonArray.length();
    }

    @Override
    public JSONObject getItem(int position) {

        return mJsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        // Your particular dataset uses String IDs
        // but you have to put something in this method
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {
            // inflate the custom layout from your xml
            convertView = mInflater.inflate(R.layout.row_book, null);
            // create a new "holder" with subviews
            holder = new ViewHolder();
            holder.thumbnailImageView = (ImageView)
                    convertView.findViewById(R.id.img_thumbnail);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.text_title);
            holder.authorTextView = (TextView) convertView.findViewById(R.id.text_author);
            // hang onto this holder for future recyclage
            convertView.setTag(holder);

        } else {
            // skip all expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();

        }
        // more code after this

        // get the current book's data in JSON form
        JSONObject jsonObject = (JSONObject) getItem(position);

        // see if there is a cover ID in the Object
        if (jsonObject.has("cover_i")) {

            // if so, grab the cover ID out from the Object
            String imageID = jsonObject.optString("cover_i");

            // construct the image URL (specific to API)
            String imageURL = IMAGE_URL_BASE + imageID + "-S.jpg";

            // use Picasso to load the image
            // temporarily have a placeholder in case it's slow to load
            Picasso.with(mContext).load(imageURL).placeholder(R.drawable.ic_books).into(holder.thumbnailImageView);
        } else {

            // if there is no cover ID in the Object, use a placeholder
            holder.thumbnailImageView.setImageResource(R.drawable.ic_books);
        }

        // Grab the title and author from the JSON
        String bookTitle = "";
        String authorName = "";

        if (jsonObject.has("title")) {
            bookTitle = jsonObject.optString("title");
        }

        if (jsonObject.has("author_name")) {
            authorName = jsonObject.optJSONArray("author_name").optString(0);
        }

        // Send these strings to TextViews for display
        holder.titleTextView.setText(bookTitle);
        holder.authorTextView.setText(authorName);

        return convertView;
    }
    // this is used so you only ever have to do
    // inflation and finding by ID once ever per view
    private static class ViewHolder {
        public ImageView thumbnailImageView;
        public TextView titleTextView;
        public TextView authorTextView;
    }
    public void updateData(JSONArray jsonArray) {
        // update the adapter's dataset
        mJsonArray = jsonArray;
        notifyDataSetChanged();
    }
}
