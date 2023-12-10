package com.example.githubapisub.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubapisub.databinding.FragmentDetailBinding
import com.example.githubapisub.ui.viewmodel.DetailFragViewModel


class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lateinit var viewModel: DetailFragViewModel
        viewModel = ViewModelProvider(this)[DetailFragViewModel::class.java]
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.rvFollow
        val adapter = ListUserAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username: String? = arguments?.getString(ARG_USERNAME)

        if (username != null) {
            viewModel.getFollowing(username)
            viewModel.getFollowers(username)
        }

        if (index == 1) {
            viewModel.userFollowers.observe(viewLifecycleOwner) { followersList ->
                adapter.submitList(followersList)
            }
        } else {
            viewModel.userFollowing.observe(requireActivity()) {followingList  ->
                adapter.submitList(followingList)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val ARG_USERNAME = "username"
    }

}
