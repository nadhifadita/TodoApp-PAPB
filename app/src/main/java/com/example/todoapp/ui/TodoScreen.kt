package com.example.todoapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.model.Todo
import com.example.todoapp.viewmodel.TodoViewModel

@Composable
fun TodoScreen(vm: TodoViewModel = viewModel()) {
    val todos by vm.todos.collectAsState()
    var text by rememberSaveable { mutableStateOf("") }
    var editingTodo by remember { mutableStateOf<Todo?>(null) }
    var editedText by remember { mutableStateOf("") }
    var filter by rememberSaveable { mutableStateOf("All") }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFF8E1), Color(0xFFFFE0B2))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.Start) {

            Text(
                text = "📝 To-do App",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5D4037)
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        label = { Text("Tambah tugas...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            if (text.isNotBlank()) {
                                vm.addTask(text.trim())
                                text = ""
                            }
                        },
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .align(Alignment.End)
                    ) {
                        Text("Tambah")
                    }
                }
            }
            
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Cari tugas...") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterButton(text = "Semua", selected = filter == "All") { filter = "All" }
                Spacer(modifier = Modifier.width(8.dp))
                FilterButton(text = "Selesai", selected = filter == "Completed") { filter = "Completed" }
                Spacer(modifier = Modifier.width(8.dp))
                FilterButton(text = "Belum Selesai", selected = filter == "Incomplete") { filter = "Incomplete" }
            }

            val completedCount = todos.count { it.isDone }
            val incompleteCount = todos.size - completedCount
            Text(
                text = "Selesai: $completedCount, Aktif: $incompleteCount",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp).align(Alignment.CenterHorizontally)
            )

            Text(
                text = "📌 Daftar Tugas",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color(0xFF6D4C41),
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Divider(color = Color(0xFF8D6E63))

            LazyColumn {
                val filteredTodos = when (filter) {
                    "Completed" -> todos.filter { it.isDone }
                    "Incomplete" -> todos.filter { !it.isDone }
                    else -> todos
                }.filter { it.title.contains(searchQuery, ignoreCase = true) }

                items(filteredTodos) { todo ->
                    TodoItem(
                        todo = todo,
                        onToggle = { vm.toggleTask(todo.id) },
                        onDelete = { vm.deleteTask(todo.id) },
                        onEdit = {
                            editingTodo = todo
                            editedText = todo.title
                        }
                    )
                }
            }

            if (editingTodo != null) {
                AlertDialog(
                    onDismissRequest = { editingTodo = null },
                    title = { Text("Edit Tugas") },
                    text = {
                        OutlinedTextField(
                            value = editedText,
                            onValueChange = { editedText = it },
                            label = { Text("Ubah teks tugas") }
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            vm.editTask(editingTodo!!.id, editedText)
                            editingTodo = null
                        }) { Text("Simpan") }
                    },
                    dismissButton = {
                        TextButton(onClick = { editingTodo = null }) { Text("Batal") }
                    }
                )
            }
        }
    }
}

@Composable
fun FilterButton(text: String, selected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text(text)
    }
}
