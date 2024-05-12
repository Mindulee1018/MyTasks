package com.example.mytasks.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mytasks.AddNewTask
import com.example.mytasks.MainActivity
import com.example.mytasks.R
import com.example.mytasks.Model.ToDoModel
import com.example.mytasks.Utils.DatabaseHandler

class ToDoAdapter(private val db: DatabaseHandler, private val activity: MainActivity) :
    RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {

    private var todoList: MutableList<ToDoModel>? = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.task_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        todoList?.getOrNull(position)?.let { item ->
            holder.task.text = item.getTask()
            holder.task.isChecked = item.getStatus() != 0
            holder.task.setOnCheckedChangeListener(null) // Remove listener during initialization
            holder.task.setOnCheckedChangeListener { _, isChecked ->
                db.updateStatus(item.getId(), if (isChecked) 1 else 0)
            }

            // Ensure these are properly assigned:
            holder.description.text = item.getDescription()
            holder.priority.text = item.getPriority()
            holder.priority.setTextColor(getPriorityColor(item.getPriority(), holder.itemView.context))
            holder.deadline.text = item.getDeadline()

        }
    }

    private fun getPriorityColor(priority: String, context: Context): Int {
        return when (priority) {
            "High" -> ContextCompat.getColor(context, R.color.priority_high)
            "Medium" -> ContextCompat.getColor(context, R.color.priority_medium)
            "Low" -> ContextCompat.getColor(context, R.color.priority_low)
            else -> ContextCompat.getColor(context, android.R.color.black) // Default color
        }
    }

    override fun getItemCount(): Int {
        return todoList!!.size
    }

    private fun toBoolean(n: Int): Boolean {
        return n != 0
    }

    fun setTasks(todoList: List<ToDoModel> = emptyList()) {
        this.todoList = todoList.toMutableList()
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        val item: ToDoModel = todoList!![position]
        db.deleteTask(item.getId())
        todoList!!.removeAt(position)
        notifyItemRemoved(position)
    }

    fun editItem(position: Int) {
        val item: ToDoModel = todoList!![position]
        val bundle = Bundle().apply {
            putInt("id", item.getId())
            putString("task", item.getTask())
        }
        val fragment = AddNewTask().apply {
            arguments = bundle
        }
        fragment.show(activity.supportFragmentManager, AddNewTask.TAG)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var task: CheckBox = view.findViewById(R.id.todoCheckBox)
        var description: TextView = view.findViewById(R.id.taskDescription)
        var priority: TextView = view.findViewById(R.id.taskPriority)
        var deadline: TextView = view.findViewById(R.id.taskDeadline)
    }
}
