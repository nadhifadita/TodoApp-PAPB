package com.example.todoapp.ui
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.todoapp.model.Todo

@Composable
fun TodoItem(
 todo: Todo,
 onToggle: () -> Unit,
 onDelete: () -> Unit,
 onEdit: () -> Unit
) {
 val bgColor = if (todo.isDone) Color(0xFFFFCDD2) else Color(0xFFB3E5FC)

 Card(
  modifier = Modifier
   .fillMaxWidth()
   .padding(vertical = 6.dp),
  colors = CardDefaults.cardColors(containerColor = bgColor),
  elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
  shape = MaterialTheme.shapes.medium
 ) {
  Row(
   modifier = Modifier
    .fillMaxWidth()
    .padding(12.dp),
   verticalAlignment = Alignment.CenterVertically
  ) {
   Checkbox(checked = todo.isDone, onCheckedChange = { onToggle() })
   Spacer(Modifier.width(8.dp))
   Text(
    text = "${todo.title}",
    modifier = Modifier.weight(1f),
    style = if (todo.isDone)
     TextStyle(textDecoration = TextDecoration.LineThrough, color = Color.DarkGray)
    else
     TextStyle(color = Color.Black)
   )

   if (!todo.isDone) {
    IconButton(onClick = onEdit) {
     Icon(
      Icons.Default.Edit,
      contentDescription = "Edit", tint = Color(0xFF6A1B9A)
     )
    }
   }

   IconButton(onClick = onDelete) {
    Icon(
     Icons.Default.Delete,
     contentDescription = "Delete", tint = Color(0xFFD32F2F)
    )
   }

  }
 }
}
