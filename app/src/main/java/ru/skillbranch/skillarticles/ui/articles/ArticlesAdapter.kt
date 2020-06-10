package ru.skillbranch.skillarticles.ui.articles

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlinx.android.synthetic.*
import ru.skillbranch.skillarticles.R
import ru.skillbranch.skillarticles.data.ArticleItemData
import ru.skillbranch.skillarticles.extensions.dpToIntPx

class ArticlesAdapter(private val listener: (ArticleItemData) -> Unit) :
    ListAdapter<ArticleItemData, ArticleVH>(ArticleDIffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleVH {
        val containerView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleVh(containerView)
    }

    override fun onBindViewHolder(holder: ArticleVH, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

class ArticleDiffCAllback : DiffUtil.ItemCallback<ArticleItemData>() {
    override fun areItemsTheSame(oldItem: ArticleItemData, newItem: ArticleItemData): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ArticleItemData, newItem: ArticleItemData): Boolean =
        oldItem == newItem
}

class ArticleVH(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer{
    fun bind(
        item: ArticleItemData,
        listener: (ArticleItemData)-> Unit
    ){
        val posterSize = containerView.context.dpToIntPx(64)
        val cornerRadius = containerView.context.dpToIntPx(8)
        val categorySize = containerView.context.dpToIntPx(40)

        Glide.with(containerView.context)
            .load(item.poster)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .override(posterSize)
            .into(iv_poster)

        Glide.with(containerView.context)
            .load(item.categoryIcon)
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .override(categorySize)
            .into(iv_category)

        tv_date.text = item.date.format()
        tv_author.text = item.author
        tv_title.text = item.title
        tv_description.text = "${item.likeCount}"
        tv_comments_count.text = "${item.commentCount}"
        tv_read_duration.text = "${item.readDuration} min read"

        itemView.setOnClickListener{listener(item)}
    }
}