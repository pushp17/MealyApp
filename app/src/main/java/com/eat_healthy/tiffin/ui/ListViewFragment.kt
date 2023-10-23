package com.eat_healthy.tiffin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.eat_healthy.tiffin.R
import com.eat_healthy.tiffin.genericFiles.ListItem
import com.eat_healthy.tiffin.utils.RecyclerviewItemClicklistener
import javax.inject.Inject

abstract class ListViewFragment<A : RecyclerView.Adapter<RecyclerView.ViewHolder>, VDB : ViewDataBinding> :
    BaseFragment() , RecyclerviewItemClicklistener<ListItem> {
    @get:LayoutRes
    abstract val fragmentLayoutResId: Int
    private var _binding: VDB? = null
    protected val binding get() = _binding!!
    var recyclerView: RecyclerView? = null
    var navigationController:NavController?=null
    @Inject
    lateinit var adapter: A
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater,
            fragmentLayoutResId,
            container,
            false/*, bindingComponent*/
        )
        recyclerView=binding.root.findViewById(R.id.rv_parent)
        navigationController=findNavController()
        return binding.root
    }

    override fun onClickItem(position: Int, item: ListItem?) {
    }
    override fun onClickItem(position: Int, item: ListItem?,id: String?) {}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.let {
            it.lifecycleOwner = this
        }
        recyclerView?.adapter=adapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
