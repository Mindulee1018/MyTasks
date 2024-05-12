package com.example.mytasks


import com.example.mytasks.R
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mytasks.Adapter.ToDoAdapter

class RecyclerItemTouchHelper(private val adapter: ToDoAdapter) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val context = viewHolder.itemView.context
        val position = viewHolder.adapterPosition  // It's better to use adapterPosition
        if (direction == ItemTouchHelper.LEFT) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Delete Task")
            builder.setMessage("Are you sure you want to delete this Task?")
            builder.setPositiveButton("Confirm",
                DialogInterface.OnClickListener { dialog, which -> adapter.deleteItem(position) })
            builder.setNegativeButton(android.R.string.cancel, DialogInterface.OnClickListener { dialog, which ->
                adapter.notifyItemChanged(position)  // Using position captured above
            })
            val dialog = builder.create()
            dialog.show()
        } else {
            adapter.editItem(position)
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val context = viewHolder.itemView.context
        val icon: Drawable?
        val background: ColorDrawable
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20

        if (dX > 0) {
            // Ensure this icon exists in your drawable folder
            icon = ContextCompat.getDrawable(context, R.drawable.baseline_create_24)
            // Change R.color.colorPrimaryDark to a color that exists in your colors.xml or use a default one
            background = ColorDrawable(Color.WHITE)
        } else {
            icon = ContextCompat.getDrawable(context, R.drawable.baseline_delete_24)
            background = ColorDrawable(Color.WHITE)
        }

        // Ensure the icon is not null
        icon?.let {
            val iconMargin = (itemView.height - icon.intrinsicHeight) / 2
            val iconTop = itemView.top + iconMargin
            val iconBottom = iconTop + icon.intrinsicHeight

            if (dX > 0) { // Swiping to the right
                val iconLeft = itemView.left + iconMargin
                val iconRight = itemView.left + iconMargin + icon.intrinsicWidth
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background.setBounds(
                    itemView.left, itemView.top,
                    itemView.left + (dX.toInt()) + backgroundCornerOffset, itemView.bottom
                )
            } else if (dX < 0) { // Swiping to the left
                val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                val iconRight = itemView.right - iconMargin
                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                background.setBounds(
                    itemView.right + (dX.toInt()) - backgroundCornerOffset,
                    itemView.top, itemView.right, itemView.bottom
                )
            }
        }

        background.draw(c)
        icon?.draw(c)
    }

}
