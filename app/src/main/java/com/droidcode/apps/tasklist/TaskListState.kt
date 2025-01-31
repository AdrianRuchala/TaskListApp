package com.droidcode.apps.tasklist

data class TaskListState(
    val tasks: List<Task> = emptyList(),
    val filter: TaskFilter = TaskFilter.ALL
)
