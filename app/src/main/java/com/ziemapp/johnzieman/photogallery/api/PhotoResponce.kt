package com.ziemapp.johnzieman.photogallery.api


import com.google.gson.annotations.SerializedName
import com.ziemapp.johnzieman.photogallery.models.GalleryItem

class PhotoResponce {
    @SerializedName("photo")
    lateinit var galleryItems: List<GalleryItem>
}