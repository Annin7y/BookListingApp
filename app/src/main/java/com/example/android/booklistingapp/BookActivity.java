package com.example.android.booklistingapp;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>>
{
    /**
     * URL for book data from the Google API
     */
    private static final String GOOGLE_BOOKS_API_BASE = "https://www.googleapis.com/books/v1/volumes?maxResults=20&q=";

    //Constant value for the earthquake loader ID
    private static final int BOOK_LOADER_ID = 1;

    public static final String LOG_TAG = BookActivity.class.getName();

    Button search;
    private EditText findText;
    private BookAdapter mAdapter;
    private String text;
    private String findUrl;
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);

        search = (Button) findViewById(R.id.search_button);

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        bookListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of books as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        final EditText textField = (EditText) findViewById(R.id.enter_text_field);

        //hide the keyboard after rotation
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        android.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, null, this);

        search.setOnClickListener(new View.OnClickListener()

                                  {
                                      @Override
                                      public void onClick(View v)
                                      {
                                          if (textField.getText().toString().trim().equals(""))
                                          {
                                              textField.setError(getString(R.string.no_text_entered));
                                          } else {
                                              findText = (EditText) findViewById(R.id.enter_text_field);
                                              text = findText.getText().toString().replace(" ", "+");
                                              findUrl = GOOGLE_BOOKS_API_BASE + text;

                                              ConnectivityManager connMgr = (ConnectivityManager)
                                                      getSystemService(Context.CONNECTIVITY_SERVICE);

                                              NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                                              if (textField.getText().toString().trim().equals(""))
                                              {
                                                  textField.setError(getString(R.string.no_text_entered));
                                              }

                                              // If there is a network connection, fetch data
                                              if (networkInfo != null && networkInfo.isConnected())
                                              {
                                                  android.app.LoaderManager loaderManager = getLoaderManager();
                                                  loaderManager.restartLoader(BOOK_LOADER_ID, null, BookActivity.this);
                                                  View loadingIndicator = findViewById(R.id.loading_indicator);
                                                  loadingIndicator.setVisibility(View.VISIBLE);

                                              } else
                                              {
                                                  View loadingIndicator = findViewById(R.id.loading_indicator);
                                                  loadingIndicator.setVisibility(View.GONE);
                                                  // Update empty state with no connection error message
                                                  mEmptyStateTextView.setText(R.string.no_internet_connection);
                                              }
                                          }
                                      }
                                  }
        );
    }

    @Override
    public android.content.Loader<List<Book>> onCreateLoader(int i, Bundle bundle)
    {
        return new BookLoader(this, findUrl);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Book>> loader, List<Book> books)
    {
        mAdapter.clear();

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        // Set empty state text to display "No books available"
        mEmptyStateTextView.setText(R.string.no_books_available);

        if (books != null && !books.isEmpty())
        {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Book>> loader)
    {
        mAdapter.clear();
    }

}





