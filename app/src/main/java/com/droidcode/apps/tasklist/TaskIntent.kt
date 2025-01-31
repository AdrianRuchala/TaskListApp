package com.droidcode.apps.tasklist

sealed class TaskIntent {
    data class AddTask(val content: String): TaskIntent()
    data class ChangeFilter(val filter: TaskFilter): TaskIntent()
    data class ChangeTaskState(val taskId: Int): TaskIntent()
}
