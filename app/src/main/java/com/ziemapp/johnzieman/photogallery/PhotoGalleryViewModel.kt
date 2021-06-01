package com.ziemapp.johnzieman.photogallery

import android.app.Application
import androidx.lifecycle.*
import com.ziemapp.johnzieman.photogallery.models.GalleryItem

class PhotoGalleryViewModel(private val app: Application) : AndroidViewModel(app) {
    val galleryItemLiveData: LiveData<List<GalleryItem>>

    private val flickrFetcher = FlickrFetcher()
    private val mutableSearchTerm = MutableLiveData<String>()

    val searchTerm: String
    get() = mutableSearchTerm.value ?: ""

    init {
        mutableSearchTerm.value = QueryPreferences.getStoredQuery(app)

        galleryItemLiveData = Transformations.switchMap(mutableSearchTerm) {
            if(it.isBlank()){
                flickrFetcher.fetchPhotos()
            } else {
                flickrFetcher.searchPhotos(it)
            }

        }
    }

    fun fetchPhotos(query: String = "") {
        QueryPreferences.setStoredQuery(app, query)
        mutableSearchTerm.value = query
    }
}