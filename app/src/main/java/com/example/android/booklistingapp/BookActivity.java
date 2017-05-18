package com.example.android.booklistingapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

public class BookActivity extends AppCompatActivity {

    /**
     * URL for book data from the Google API
     */
    private static final String GOOGLE_BOOKS_API_BASE = "https://www.googleapis.com/books/v1/volumes?maxResults=20&q=";


    /**
     * Adapter for the list of books and authors
     */
    Button search;
    private EditText findText;
    private BookAdapter mAdapter;
    private String text;
    public static final String LOG_TAG = BookActivity.class.getName();
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);


        search = (Button) findViewById(R.id.search_button);

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView =  (ListView) findViewById(R.id.list);;

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of books as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());


        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        final EditText textField = (EditText) findViewById(R.id.enter_text_field);

        search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if (textField.getText().toString().trim().equals("")) {
                    textField.setError(getString(R.string.no_text_entered));
                } else {
                    findText = (EditText) findViewById(R.id.enter_text_field);
                    text = findText.getText().toString().replace(" ", "+");


                    BookAsyncTask task = new BookAsyncTask();
                    // Update the information displayed to the user.


                    ConnectivityManager connMgr = (ConnectivityManager)
                            getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                    if (textField.getText().toString().trim().equals("")) {
                        textField.setError(getString(R.string.no_text_entered));
                    }

                    // If there is a network connection, fetch data
                    if (networkInfo != null && networkInfo.isConnected()) {
                        task.execute(GOOGLE_BOOKS_API_BASE + text);
                    } else {

                        // Update empty state with no connection error message
                        mEmptyStateTextView.setText(R.string.no_internet_connection);
                    }
                }
            }
        });
    }

    private class BookAsyncTask extends AsyncTask<String, Void, List<Book>> {
        List<Book> books = new ArrayList<>();

        @Override
        protected List<Book> doInBackground(String... urls) {

            // Don't perform the request if there are no URLs, or the first URL is null
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Book> books = QueryUtils.fetchBookData(urls[0]);
            return books;
        }


        @Override
        protected void onPostExecute(List<Book> books) {
            // Clear the adapter of previous earthquake data
            mAdapter.clear();

            // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
            // data set. This will trigger the ListView to update.
            if (books != null && !books.isEmpty()) {
                mAdapter.addAll(books);

            }

        }
    }
}



