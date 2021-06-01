package com.ziemapp.johnzieman.photogallery

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ziemapp.johnzieman.photogallery.api.FlickrApi
import com.ziemapp.johnzieman.photogallery.models.GalleryItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

private const val TAG = "PhotoGalleryFragment"
class PhotoGalleryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    private val photoGalleryViewModel: PhotoGalleryViewModel by lazy {
        ViewModelProvider(this).get(PhotoGalleryViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val item = inflater.inflate(R.layout.fragment_photo_gallery, container, false)

        recyclerView = item.findViewById(R.id.photo_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        recyclerView.adapter = PhotoAdaper(emptyList())
        return item

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoGalleryViewModel.galleryItemLiveData.observe(
            viewLifecycleOwner,
            Observer { galleryItems ->
                recyclerView.adapter = PhotoAdaper(galleryItems)
            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_gallery, menu)
        val searchItem: MenuItem = menu.findItem(R.id.menu_item_search)
        val searchView = searchItem.actionView as SearchView

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    Log.d(TAG, "QueryTextSubmit: $query")
                    if (query != null) {
                        photoGalleryViewModel.fetchPhotos(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.d(TAG, "QueryTextChange: $query")
                    return false
                }
            })

            setOnClickListener {
                searchView.setQuery(photoGalleryViewModel.searchTerm, false)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){

        R.id.menu_item_clear -> {
            photoGalleryViewModel.fetchPhotos("")
            true
        }

        else -> super.onOptionsItemSelected(item)
        }
    }

    private inner class PhotoAdaper(private val galleryItems: List<GalleryItem>): RecyclerView.Adapter<PhotoHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
            val view = LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false)
            return PhotoHolder(view)
        }

        override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
            val galleryItem = galleryItems[position]
            holder.bind(galleryItem)
        }

        override fun getItemCount() = galleryItems.size
    }


    private inner class PhotoHolder(view: View): RecyclerView.ViewHolder(view) {
        val pic = view.findViewById(R.id.pic) as ImageView

        fun bind(galleryItem: GalleryItem){
            Glide.with(view!!).load(galleryItem.url).into(pic)
        }
    }


}
