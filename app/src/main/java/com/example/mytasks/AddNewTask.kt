package com.example.mytasks

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.mytasks.Model.ToDoModel
import com.example.mytasks.Utils.DatabaseHandler
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.Calendar
import java.util.Locale
import android.widget.Spinner

class AddNewTask : BottomSheetDialogFragment() {
    private lateinit var newTaskText: EditText
    private lateinit var newTaskDescription: EditText
    private lateinit var newTaskPriority: Spinner
    private lateinit var newTaskSaveButton: Button
    private lateinit var newTaskDeadline: EditText
    private var db: DatabaseHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.new_task, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newTaskText = view.findViewById(R.id.newTaskText)
        newTaskDescription = view.findViewById(R.id.newTaskDescription)
        newTaskPriority = view.findViewById(R.id.newTaskPriority)
        newTaskSaveButton = view.findViewById(R.id.newTaskButton)
        newTaskDeadline = view.findViewById(R.id.newTaskDeadline)
        newTaskDeadline.setOnClickListener { showDatePickerDialog() }

        var isUpdate = false
        val bundle = arguments
        if (bundle != null) {
            isUpdate = true
            val task = bundle.getString("task")
            newTaskText.setText(task)
            task?.let {
                if (it.isNotEmpty()) {
                    newTaskSaveButton.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
                    )
                }
            }
        }

        val context = context
        if (context != null) {
            db = DatabaseHandler(context)
            db?.openDatabase()
        }

        newTaskText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().isEmpty()) {
                    newTaskSaveButton.isEnabled = false
                    newTaskSaveButton.setTextColor(Color.GRAY)
                } else {
                    newTaskSaveButton.isEnabled = true
                    newTaskSaveButton.setTextColor(
                        ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark)
                    )
                }
            }
            override fun afterTextChanged(s: Editable) {}
        })

        newTaskSaveButton.setOnClickListener {
            val text = newTaskText.text.toString()
            val description = newTaskDescription.text.toString()
            val priority = newTaskPriority.selectedItem.toString()
            val deadline = newTaskDeadline.text.toString()

            if (isUpdate) {
                bundle?.getInt("id")?.let { taskId ->
                    val updatedTask = ToDoModel().apply {
                        setId(taskId)
                        setTask(text)
                        setDescription(description)
                        setPriority(priority)
                        setDeadline(deadline)
                    }
                    db?.updateTask(updatedTask)
                }
            } else {
                val newTask = ToDoModel().apply {
                    setTask(text)
                    setStatus(0) // assuming 0 is the default status for new tasks
                    setDescription(description)
                    setPriority(priority)
                    setDeadline(deadline)
                }
                db?.insertTask(newTask)
            }
            dismiss()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
            val selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth)
            newTaskDeadline?.setText(selectedDate)
        }, year, month, day)

        datePicker.show()
    }

    override fun onDismiss(dialog: DialogInterface) {
        val activity = activity
        if (activity is DialogCloseListener) {
            (activity as DialogCloseListener).handleDialogClose(dialog)
        }
    }

    companion object {
        const val TAG = "ActionBottomDialog"
        @JvmStatic
        fun newInstance(): AddNewTask {
            return AddNewTask()
        }
    }
}
