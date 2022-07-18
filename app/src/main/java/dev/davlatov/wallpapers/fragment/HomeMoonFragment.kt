package dev.davlatov.wallpapers.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.RecyclerView
import dev.davlatov.wallpapers.adapter.RetrofitGetSearchAdapter
import dev.davlatov.wallpapers.models.SearchModels.SearchHome
import dev.davlatov.wallpapers.models.SearchModels.SearchResult
import dev.davlatov.wallpapers.networking.RetrofitHttp
import dev.davlatov.wallpapers.R
import dev.davlatov.wallpapers.databinding.FragmentHomeCarsBinding
import dev.davlatov.wallpapers.util.ApiKeyList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeMoonFragment : BaseFragment() {
    private var _binding: FragmentHomeCarsBinding? = null
    private val binding get() = _binding!!
    var photosList = ArrayList<SearchResult>()
    private var pageCount = 1
    private var perPageCount = 30
    private var searchName = "Moon"
    private lateinit var retrofitGetSearchAdapter: RetrofitGetSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_home_moon, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeCarsBinding.bind(view)
        recyclerViewManager()
        loadItemImagesAll(pageCount, perPageCount)
        recyclerItemClickManager()
        searchCardClickManager()
    }

    private fun searchCardClickManager() {
        binding.searchCardView.setOnClickListener {
            startSearchFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        motionManager()
    }

    private fun motionManager() {
        binding.apply {
            val transitionListener = object : MotionLayout.TransitionListener {
                override fun onTransitionStarted(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                ) {
                    binding.searchCardView.startAnimation(animation)
                }

                override fun onTransitionChange(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                    progress: Float,
                ) {
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {}

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float,
                ) {
                }
            }
            motionLayout.setTransitionListener(transitionListener)
        }
    }

    private fun refreshPageCountAdder() {
        loadItemImagesAll(pageCount++, perPageCount)
    }

    private fun refreshAllItemLoadAgain() {
        loadItemImagesAll(pageCount, perPageCount)
    }

    private fun recyclerItemClickManager() {
        retrofitGetSearchAdapter.itemCLick = {
            startDetailFragment()
        }
    }

    private fun loadItemImagesAll(page: Int, per_page: Int) {
        hideDialog()
        RetrofitHttp.posterService.searchPhotos(page, per_page, searchName)
            .enqueue(object : Callback<SearchHome> {
                override fun onResponse(
                    call: Call<SearchHome>,
                    response: Response<SearchHome>,
                ) {
                    if (response.body() != null) {
                        photosList.addAll(response.body()!!.results!!)
                        adapterNotify()
                    } else {
                        if (ApiKeyList.countGetInt != 3) {
                            when (ApiKeyList.countGetInt) {
                                0 -> {
                                    ApiKeyList.countGetInt = 1
                                    refreshAllItemLoadAgain()
                                }
                                1 -> {
                                    ApiKeyList.countGetInt = 2
                                    refreshAllItemLoadAgain()
                                }
                                2 -> {
                                    ApiKeyList.countGetInt = 3
                                    refreshAllItemLoadAgain()
                                }
                            }
                        } else {
                            connectFirebaseServer()
                        }
                    }
                }

                override fun onFailure(call: Call<SearchHome>, t: Throwable) {
                    toast(getString(R.string.server_not_response))
                    hideDialog()
                    connectFirebaseServer()
                }
            })
    }

    private fun connectFirebaseServer() {
        toast("All Server Not Response Please try later!")
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun adapterNotify() {
        retrofitGetSearchAdapter.notifyDataSetChanged()
    }

    private fun recyclerViewManager() {
        retrofitGetSearchAdapter = RetrofitGetSearchAdapter(requireContext(), photosList)
        binding.homeAllRecyclerView.adapter = retrofitGetSearchAdapter

        binding.homeAllRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    viewDialog.showDialog()
                    refreshPageCountAdder()
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}