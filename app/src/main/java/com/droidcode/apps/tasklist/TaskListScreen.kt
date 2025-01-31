package com.droidcode.apps.tasklist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun TaskListScreen(modifier: Modifier, viewModel: TaskViewModel) {
    val isAddNewTaskAlertDialogOpen = remember { mutableStateOf(false) }
    val isFilterAlertDialogOpen = remember { mutableStateOf(false) }

    if (isAddNewTaskAlertDialogOpen.value) {
        AddTaskAlertDialog(Modifier, isAddNewTaskAlertDialogOpen, viewModel)
    }

    if (isFilterAlertDialogOpen.value) {
        FilterAlertDialog(Modifier, isFilterAlertDialogOpen, viewModel)
    }

    Scaffold(
        floatingActionButton = { AddTaskButton(isAddNewTaskAlertDialogOpen) },
        topBar = { TopBar(modifier, isFilterAlertDialogOpen) }) { padding ->

        LazyColumn(
            modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(viewModel.state.value.tasks) { task ->
                Row(
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .clickable { viewModel.onAction(TaskIntent.ChangeTaskState(task.id)) }
                ) {
                    if (task.isDone) {
                        Image(
                            painter = painterResource(R.drawable.baseline_check_box_24),
                            contentDescription = null
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.baseline_check_box_outline_blank_24),
                            contentDescription = null
                        )
                    }
                    Text(task.content)
                }
            }
        }
    }

}

@Composable
fun TopBar(modifier: Modifier, isAlertDialogOpen: MutableState<Boolean>) {
    Column(Modifier) {
        Row(
            modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.size(48.dp))
            Text(stringResource(R.string.app_name))

            Row(Modifier.clickable {
                isAlertDialogOpen.value = true
            }) {
                Text(stringResource(R.string.filter), style = MaterialTheme.typography.titleMedium)
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }

        HorizontalDivider(Modifier)
    }
}

@Composable
fun AddTaskButton(isAlertDialogOpen: MutableState<Boolean>) {
    FloatingActionButton(onClick = { isAlertDialogOpen.value = true }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }
}

@Composable
fun AddTaskAlertDialog(
    modifier: Modifier,
    isAlertDialogOpen: MutableState<Boolean>,
    viewModel: TaskViewModel
) {
    var taskContent by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = { isAlertDialogOpen.value = false },
        title = { Text(stringResource(R.string.add_task)) },
        text = {

            TextField(
                value = taskContent,
                onValueChange = { taskContent = it },
                modifier
                    .fillMaxWidth(),
                label = {
                    Text(stringResource(R.string.add_task))
                },
                isError = taskContent.isEmpty()
            )

        },
        confirmButton = {
            Button(onClick = {
                if (taskContent.isNotEmpty()) {
                    viewModel.onAction(TaskIntent.AddTask(taskContent))
                    isAlertDialogOpen.value = false
                }
            })
            {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            Button(onClick = { isAlertDialogOpen.value = false }) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
fun FilterAlertDialog(
    modifier: Modifier,
    isAlertDialogOpen: MutableState<Boolean>,
    viewModel: TaskViewModel
) {
    val filterTypes = arrayOf(
        "All",
        "Active",
        "Completed"
    )

    AlertDialog(
        onDismissRequest = { isAlertDialogOpen.value = false },
        title = { Text(stringResource(R.string.filter_list)) },
        text = {
            Column(modifier.fillMaxWidth()) {
                filterTypes.forEach { filterType ->
                    Text(
                        text = filterType,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                when (filterType) {
                                    filterTypes[0] -> {
                                        viewModel.onAction(TaskIntent.ChangeFilter(TaskFilter.ALL))
                                        isAlertDialogOpen.value = false
                                    }

                                    filterTypes[1] -> {
                                        viewModel.onAction(TaskIntent.ChangeFilter(TaskFilter.ACTIVE))
                                        isAlertDialogOpen.value = false
                                    }

                                    filterTypes[2] -> {
                                        viewModel.onAction(TaskIntent.ChangeFilter(TaskFilter.COMPLETED))
                                        isAlertDialogOpen.value = false
                                    }
                                }
                            }
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { isAlertDialogOpen.value = false }) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
