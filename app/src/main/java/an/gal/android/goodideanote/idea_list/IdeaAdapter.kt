package an.gal.android.goodideanote.idea_list

import an.gal.android.goodideanote.R
import an.gal.android.goodideanote.databinding.ViewHolderIdeaBinding
import an.gal.android.goodideanote.data.Idea
import an.gal.android.goodideanote.data.IdeaPurpose
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class IdeaAdapter(private val onItemClicked: (id: Int) -> Unit): ListAdapter<Idea, IdeaViewHolder>(IdeaDiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeaViewHolder =
        IdeaViewHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.view_holder_idea, parent, false)
        )


    override fun onBindViewHolder(holder: IdeaViewHolder, position: Int) {
        val idea = getItem(position)

        holder.bind(idea, onItemClicked)
    }
}

class IdeaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = ViewHolderIdeaBinding.bind(itemView)

    fun bind(idea: Idea, onItemClicked: (id: Int) -> Unit) {
        binding.title.text = idea.title
        binding.text.text = idea.text

        binding.icon.setImageResource( IdeaPurpose.getImageById(idea.purposeId) )

        // onClick
        itemView.setOnClickListener {
            idea.id?.let {
                onItemClicked.invoke(it)
            }
        }
    }
}

private class IdeaDiffCallback: DiffUtil.ItemCallback<Idea>() {
    override fun areItemsTheSame(oldItem: Idea, newItem: Idea): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Idea, newItem: Idea): Boolean {
        return oldItem == newItem
    }
}
