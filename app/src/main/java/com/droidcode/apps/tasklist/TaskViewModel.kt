package com.droidcode.apps.tasklist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class TaskViewModel: ViewModel(){
    val state = mutableStateOf(TaskListState())

    fun onAction(action: TaskIntent){
        when(action){
            is TaskIntent.AddTask -> addTask(action.content)
            is TaskIntent.ChangeFilter -> filterTaskList(action.filter)
        }
    }

    private fun addTask(content: String){
        val newTask = Task(generateTaskId(), content, false)
        state.value = state.value.copy(state.value.tasks + newTask)
    }

    private fun filterTaskList(filter: TaskFilter){
        state.value = state.value.copy(filter = filter)
    }


    private fun generateTaskId(): Int {
        if (state.value.tasks.isEmpty()){
            return 0;
        }
        else {
            val lastIndex = state.value.tasks.lastIndex
            return lastIndex + 1
        }
    }
}
