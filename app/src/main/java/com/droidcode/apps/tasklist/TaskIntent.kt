package com.droidcode.apps.tasklist

sealed class TaskIntent {
    data class AddTask(val content: String): TaskIntent()
//    data class CompleteTask: TaskIntent
//    data class DeleteTask: TaskIntent
    data class ChangeFilter(val filter: TaskFilter): TaskIntent()
}
