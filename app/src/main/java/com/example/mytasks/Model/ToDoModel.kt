package com.example.mytasks.Model

class ToDoModel {
    private var id: Int = 0
    private var task: String = ""
    private var status: Int = 0
    private var description: String = ""
    private var priority: String = ""
    private var deadline: String = ""

    fun getId(): Int = id
    fun setId(id: Int) { this.id = id }

    fun getTask(): String = task
    fun setTask(task: String) { this.task = task }

    fun getStatus(): Int {
        return status
    }
    fun setStatus(status: Int) { this.status = status }

    fun getDescription(): String = description
    fun setDescription(description: String) { this.description = description }

    fun getPriority(): String = priority
    fun setPriority(priority: String) { this.priority = priority }

    fun getDeadline(): String = deadline
    fun setDeadline(deadline: String) { this.deadline = deadline }
}

