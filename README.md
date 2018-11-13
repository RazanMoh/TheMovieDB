# TheMovieDB
## Introduction
Android application for displaying movies using the themoviedb api.

## How to build on your environment
Add your API key in local.properties file.
```xml
tmdb_api_key=YOUR_API_KEY
```

## Specs & Open-source libraries
* Minimum SDK 18
* The Movie DB API
* [Volley](https://developer.android.com/training/volley/) for constructing the REST API
* [Glide](https://github.com/bumptech/glide) for loading images
* [PhotoView](https://github.com/bumptech/glide) for zoom in/out and move around the image
* [RecyclerView](https://developer.android.com/topic/libraries/support-library/packages) for implementing adapters and viewHolders
* [BottomNavigationViewEx](https://github.com/ittianyu/BottomNavigationViewEx) for enhancing BottomNavigationView

##  Features
* Show the top movies
* Search for movies shows by title
* Details about a work including the casts, the videos, the similar and recommended works
* Details about a cast including the list of movies casted
* Add a movie or remove it from favorites list
