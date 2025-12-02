package com.kkwieer.erpwebapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kkwieer.erpwebapp.data.LinkRepository
import com.kkwieer.erpwebapp.data.PortalLink

private val PrimaryBlue = Color(0xFF1976D2)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onNavigateBack: () -> Unit) {
    val context = LocalContext.current
    val repository = remember { LinkRepository.getInstance(context) }
    var links by remember { mutableStateOf(repository.getLinks()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingLink by remember { mutableStateOf<PortalLink?>(null) }
    var showResetDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<PortalLink?>(null) }

    fun refreshLinks() {
        links = repository.getLinks()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Manage Portal Links",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showResetDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Reset to defaults"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PrimaryBlue,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Link")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (links.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No links added",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Tap + to add a new link",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(links, key = { _, link -> link.id }) { index, link ->
                        LinkCard(
                            link = link,
                            index = index,
                            totalCount = links.size,
                            onEdit = { editingLink = link },
                            onDelete = { showDeleteDialog = link },
                            onMoveUp = {
                                if (index > 0) {
                                    repository.moveLink(index, index - 1)
                                    refreshLinks()
                                }
                            },
                            onMoveDown = {
                                if (index < links.size - 1) {
                                    repository.moveLink(index, index + 1)
                                    refreshLinks()
                                }
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(72.dp))
                    }
                }
            }
        }
    }

    // Add/Edit Dialog
    if (showAddDialog || editingLink != null) {
        LinkDialog(
            link = editingLink,
            onDismiss = {
                showAddDialog = false
                editingLink = null
            },
            onSave = { name, url ->
                if (editingLink != null) {
                    repository.updateLink(editingLink!!.copy(name = name, url = url))
                } else {
                    repository.addLink(PortalLink(name = name, url = url))
                }
                refreshLinks()
                showAddDialog = false
                editingLink = null
            }
        )
    }

    // Reset Confirmation Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset to Defaults?") },
            text = { Text("This will remove all custom links and restore the original portal links.") },
            confirmButton = {
                Button(
                    onClick = {
                        repository.resetToDefaults()
                        refreshLinks()
                        showResetDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // Delete Confirmation Dialog
    showDeleteDialog?.let { link ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text("Delete Link?") },
            text = { Text("Are you sure you want to delete \"${link.name}\"?") },
            confirmButton = {
                Button(
                    onClick = {
                        repository.deleteLink(link.id)
                        refreshLinks()
                        showDeleteDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun LinkCard(
    link: PortalLink,
    index: Int,
    totalCount: Int,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = link.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (link.isDefault) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = PrimaryBlue.copy(alpha = 0.1f)
                                ),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "Default",
                                    fontSize = 10.sp,
                                    color = PrimaryBlue,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = link.url,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Reorder buttons
                Column {
                    IconButton(
                        onClick = onMoveUp,
                        enabled = index > 0,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowUp,
                            contentDescription = "Move Up",
                            tint = if (index > 0) PrimaryBlue else Color.Gray.copy(alpha = 0.3f)
                        )
                    }
                    IconButton(
                        onClick = onMoveDown,
                        enabled = index < totalCount - 1,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Move Down",
                            tint = if (index < totalCount - 1) PrimaryBlue else Color.Gray.copy(
                                alpha = 0.3f
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete")
                }
            }
        }
    }
}

@Composable
private fun LinkDialog(
    link: PortalLink?,
    onDismiss: () -> Unit,
    onSave: (name: String, url: String) -> Unit
) {
    var name by remember { mutableStateOf(link?.name ?: "") }
    var url by remember { mutableStateOf(link?.url ?: "") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var urlError by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        var isValid = true
        if (name.isBlank()) {
            nameError = "Name is required"
            isValid = false
        } else {
            nameError = null
        }
        if (url.isBlank()) {
            urlError = "URL is required"
            isValid = false
        } else if (!url.startsWith("http://") && !url.startsWith("https://")) {
            urlError = "URL must start with http:// or https://"
            isValid = false
        } else {
            urlError = null
        }
        return isValid
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(if (link == null) "Add New Link" else "Edit Link")
        },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = null
                    },
                    label = { Text("Link Name") },
                    placeholder = { Text("e.g., My Portal") },
                    isError = nameError != null,
                    supportingText = nameError?.let { { Text(it) } },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = url,
                    onValueChange = {
                        url = it
                        urlError = null
                    },
                    label = { Text("URL") },
                    placeholder = { Text("https://example.com") },
                    isError = urlError != null,
                    supportingText = urlError?.let { { Text(it) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (validate()) {
                        onSave(name.trim(), url.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
