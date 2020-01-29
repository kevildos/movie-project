# MovieFinder

An Android application that retrieves random movie information from the Movie Database (http://theMovieDb.org) and lets you store the data to a Flask (Python) server (http://github.com/kevildos/movie-project-api).

The application allows you to `View New Movies` or `View Favorite Movies`.
* `View New Movies`: Allows you to query theMovieDB and display the published information about 3 random movies. Start querying by clicking "Call". Long Clicking the displayed movies will add them to your Favorites.
* `View Favorite Movies`: Allows you to view the favorites that you have saved from "View New Movies." These are saved onto the Flask server on (http://github.com/kevildos/movie-project-api) which stores them persitently on a miniature SQL instance. Long clicking on the displayed movies will remove them from your Favorites.

## Quick Start
The API can be accessed at http://movie-api-stage.herokuapp.com/

If you want to run the development build, clone the Github repository in Android Studio, set up an Android phone(Minimum API 21: Android 5.0) or an emulator, and run the app. The application will be connected to the same API that is always running on herokuapp.

## Status
* I have plans to add authentication so that users can store their own movies without having others access it. 
* I also intend to introduce more specific querying features. The Movie DB has a "popularity" statistic that has potential to allow users to see only movies that fall past a certain threshold.
