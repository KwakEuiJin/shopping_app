package com.example.part5_chapter2.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.part5_chapter2.data.entity.product.ProductEntity
import com.example.part5_chapter2.databinding.ItemProductBinding
import com.example.part5_chapter2.extension.loadCenterCrop

class ProductListAdapter(private val onClicked : (ProductEntity) -> Unit) : ListAdapter<ProductEntity, ProductListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product:ProductEntity) = with(binding) {
            productNameTextView.text = product.productName
            productImageView.loadCenterCrop(product.productImage,8f)
            productPriceTextView.text = "${product.productPrice} 원"

            itemView.setOnClickListener {
                onClicked(product)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ProductEntity>() {
            override fun areItemsTheSame(
                oldItem: ProductEntity,
                newItem: ProductEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ProductEntity,
                newItem: ProductEntity
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}