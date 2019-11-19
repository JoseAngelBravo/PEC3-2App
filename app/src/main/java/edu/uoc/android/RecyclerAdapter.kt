package edu.uoc.android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.uoc.android.rest.models.Element
import edu.uoc.android.rest.models.Museums
import kotlinx.android.synthetic.main.museum_list_item.view.*




class RecyclerAdapter(myDataset: List<Element>? ) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    //private var items: List<Element> = ArrayList()
   private var items: List<Element>? = myDataset


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DataViewHolder(
           // LayoutInflater.from(parent.context).inflate(R.layout.museum_list_item, parent, false)
            LayoutInflater.from(parent.context).inflate(R.layout.museum_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {

            is DataViewHolder -> {
                holder.bind(items!!.get(position))
            }

        }
    }

    override fun getItemCount(): Int {
        return items!!.size
    }

    fun submitList(element: List<Element>) {
        items = element

    }

    class DataViewHolder
    constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView){

        val foto = itemView.imgView
        val texto = itemView.tvText


 fun bind(Element: Element){

     /* val uestOptions = RequestOptions()
          .placeholder(R.drawable.ic_launcher_background)
          .error(R.drawable.ic_launcher_background)

     Glide.with(itemView.context)
          .applyDefaultRequestOptions(requestOptions)
          .load(Museum.elements.getImage)
          .into(image)*/

    texto.setText(Element.adrecaNom)

     /*Picasso.get().load(Element.imatge).into(foto)*/

    val url =Element.imatge.toString().replace("[","").replace("]","")
     Glide.with(itemView.context)
         .load(url)
         .into(foto)
}

}

}