package com.example.anew

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.anew.models.BoardSize
import kotlin.math.min

class imageSelectAdapter (
    private val context: Context,
    private val imageUri: List<Uri>,
    private val boardSize: BoardSize,
    private val imageClickListener : ImageClickListener

    ):
    RecyclerView.Adapter<imageSelectAdapter.ViewHolder>() {


    interface ImageClickListener{
        fun onPlaceHolderCLicked() //this shows that the user has tapped a single card

    }


    //same as memorybaord adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
     val view =  LayoutInflater.from(context).inflate(R.layout.card_image,parent,false)

    //setting height and with dynamically

    val cardWidth = parent.width /boardSize.getWidth()
    val cardHeight = parent.height /boardSize.getHeight()
    val cardSideLength = min(cardWidth, cardHeight)

    val layoutParams =  view.findViewById<ImageView>(R.id.customImage).layoutParams

    layoutParams.height = cardSideLength
    layoutParams.width = cardSideLength



    return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //when user has chosen an image
        if (position < imageUri.size ){
            holder.bind(imageUri[position])
        }else{
            //user hasnt selected an image
            holder.bind()

        }

    }

    override fun getItemCount() = boardSize.getPairs()


    inner class  ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        //reference to the image view
        private val customImage = itemView.findViewById<ImageView>(R.id.customImage)

        //2 bind function  1 with uri parameters the other empty
        fun bind(uri: Uri) {
            customImage.setImageURI(uri)

            //null click listener so as not to respond to clicks on the imageview
            customImage.setOnClickListener(null)

        }
        fun bind() {
            //listen to user tapping to the imageview = want ot choose image
            customImage.setOnClickListener{
                imageClickListener.onPlaceHolderCLicked()

            }

        }
    }

}