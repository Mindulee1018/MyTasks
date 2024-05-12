package com.example.mytasks

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mytasks.Adapter.ToDoAdapter
import com.example.mytasks.Model.ToDoModel
import com.example.mytasks.Utils.DatabaseHandler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Collections

class MainActivity : AppCompatActivity(), DialogCloseListener {
    private lateinit var db: DatabaseHandler
    private lateinit var tasksRecyclerView: RecyclerView
    private lateinit var tasksAdapter: ToDoAdapter
    private lateinit var fab: FloatingActionButton
    private var taskList: List<ToDoModel> = mutableListOf()
    private lateinit var allTasksBtn: Button
    private lateinit var completedTasksBtn: Button
    private lateinit var uncompletedTasksBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()  // Hiding the action bar

        db = DatabaseHandler(this)
        db.openDatabase()

        tasksRecyclerView = findViewById(R.id.tasksRecyclerView)
        tasksRecyclerView.layoutManager = LinearLayoutManager(this)

        tasksAdapter = ToDoAdapter(db, this)
        tasksRecyclerView.adapter = tasksAdapter

        allTasksBtn = findViewById(R.id.allTasksBtn)
        completedTasksBtn = findViewById(R.id.completedTasksBtn)
        uncompletedTasksBtn = findViewById(R.id.uncompletedTasksBtn)

        val itemTouchHelper = ItemTouchHelper(RecyclerItemTouchHelper(tasksAdapter))
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView)

        fab = findViewById(R.id.fab)
        completedTasksBtn.setOnClickListener {
            loadCompletedTasks()
        }

        uncompletedTasksBtn.setOnClickListener {
            loadUncompletedTasks()
        }

        allTasksBtn.setOnClickListener {
            loadTasks()  // This will reload all tasks
        }

        loadTasks()

        fab.setOnClickListener {
            AddNewTask.newInstance().show(supportFragmentManager, AddNewTask.TAG)
        }
    }

    private fun loadTasks() {
        taskList = db.allTasks
        tasksAdapter.setTasks(taskList)
    }

    private fun loadCompletedTasks() {
        taskList = db.allTasks.filter { it.getStatus() == 1 } // Now using the public getter
        tasksAdapter.setTasks(taskList)
    }

    private fun loadUncompletedTasks() {
        taskList = db.allTasks.filter { it.getStatus() == 0 } // Now using the public getter
        tasksAdapter.setTasks(taskList)
    }


    override fun handleDialogClose(dialog: DialogInterface?) {
        loadTasks()
    }


}
