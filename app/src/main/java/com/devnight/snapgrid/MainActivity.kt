package com.devnight.snapgrid

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devnight.snapgrid.adapter.PhotoAdapter
import com.devnight.snapgrid.databinding.ActivityMainBinding
import com.devnight.snapgrid.viewmodel.PhotoViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: PhotoViewModel by viewModels()

    private lateinit var adapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()
        observePhotos()
    }

    private fun setupRecyclerView() {
        val rcy = binding.recyclerViewGallery
        val layoutManager = GridLayoutManager(this, 3)
        layoutManager.initialPrefetchItemCount = 6 // her satır için ekstra 2 satır önceden yükle
        rcy.layoutManager = layoutManager
        rcy.setHasFixedSize(true)
        rcy.itemAnimator = null
        rcy.setItemViewCacheSize(20)
        adapter = PhotoAdapter(this)
        rcy.adapter = adapter

        // Scroll pozisyonu takip etmek için
        var lastFirstVisible = 0

        rcy.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                val firstVisible =
                    (rv.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()

                if (firstVisible < lastFirstVisible) {
                    // Yukarı scroll → görünmeyecek itemları iptal et
                    (rv.adapter as? PhotoAdapter)?.cancelInvisibleJobs(rv)
                }

                lastFirstVisible = firstVisible
            }
        })
    }

    private fun observePhotos() {
        lifecycleScope.launch {
            viewModel.photos.collectLatest { pagingData ->
                adapter.submitData(pagingData)
            }
        }
    }
}
