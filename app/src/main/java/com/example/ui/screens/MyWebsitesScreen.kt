package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProjectStatus
import com.example.data.model.WebsiteProject
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.RoseError
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.Screen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MyWebsitesScreen(
    projects: List<WebsiteProject>,
    onSelectProject: (WebsiteProject) -> Unit,
    onDuplicateProject: (WebsiteProject) -> Unit,
    onDeleteProject: (String) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") } // "ALL", "PUBLISHED", "DRAFT"
    var projectToDelete by remember { mutableStateOf<WebsiteProject?>(null) }

    val filteredProjects = projects.filter { proj ->
        val matchesSearch = proj.name.contains(searchQuery, ignoreCase = true) ||
                proj.category.contains(searchQuery, ignoreCase = true) ||
                proj.subdomain.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            "PUBLISHED" -> proj.status == ProjectStatus.PUBLISHED
            "DRAFT" -> proj.status == ProjectStatus.DRAFT
            else -> true
        }
        matchesSearch && matchesFilter
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .padding(20.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("My Websites", style = MaterialTheme.typography.displayMedium, color = TextPrimary)
                Text("${projects.size} websites in your workspace", style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
            Button(
                onClick = { onNavigate(Screen.CREATE_AI_WIZARD) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                modifier = Modifier.testTag("create_new_website_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("New Site", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by name, category or subdomain...", color = TextMuted) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted) },
            modifier = Modifier.fillMaxWidth().testTag("search_projects_input"),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Chips
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip("All (${projects.size})", selectedFilter == "ALL") { selectedFilter = "ALL" }
            FilterChip("Published (${projects.count { it.status == ProjectStatus.PUBLISHED }})", selectedFilter == "PUBLISHED") { selectedFilter = "PUBLISHED" }
            FilterChip("Drafts (${projects.count { it.status == ProjectStatus.DRAFT }})", selectedFilter == "DRAFT") { selectedFilter = "DRAFT" }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Projects List
        if (filteredProjects.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No websites found matching criteria", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { searchQuery = ""; selectedFilter = "ALL" }) {
                        Text("Reset Filters")
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredProjects, key = { it.id }) { proj ->
                    WebsiteCardItem(
                        project = proj,
                        onOpenBuilder = {
                            onSelectProject(proj)
                            onNavigate(Screen.VISUAL_BUILDER)
                        },
                        onOpenCode = {
                            onSelectProject(proj)
                            onNavigate(Screen.CODE_EDITOR)
                        },
                        onOpenPublish = {
                            onSelectProject(proj)
                            onNavigate(Screen.PUBLISH_HOSTING)
                        },
                        onDuplicate = { onDuplicateProject(proj) },
                        onDelete = { projectToDelete = proj }
                    )
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (projectToDelete != null) {
        val proj = projectToDelete!!
        AlertDialog(
            onDismissRequest = { projectToDelete = null },
            title = { Text("Delete Website?", color = TextPrimary) },
            text = { Text("Are you sure you want to delete '${proj.name}'? This action cannot be undone.", color = TextMuted) },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteProject(proj.id)
                        projectToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RoseError)
                ) {
                    Text("Delete Site")
                }
            },
            dismissButton = {
                TextButton(onClick = { projectToDelete = null }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = SlateSurface
        )
    }
}

@Composable
private fun FilterChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) IndigoPrimary else SlateSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) IndigoPrimary else SlateBorder),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = if (isSelected) Color.White else TextSecondary,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun WebsiteCardItem(
    project: WebsiteProject,
    onOpenBuilder: () -> Unit,
    onOpenCode: () -> Unit,
    onOpenPublish: () -> Unit,
    onDuplicate: () -> Unit,
    onDelete: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }
    val isPublished = project.status == ProjectStatus.PUBLISHED
    val dateStr = SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(Date(project.lastModified))

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Category Badge Icon
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(IndigoPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(project.category.take(2).uppercase(), color = CyanAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(project.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                        Text(
                            project.customDomain ?: project.subdomain,
                            style = MaterialTheme.typography.labelSmall,
                            color = CyanAccent
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (isPublished) EmeraldSuccess.copy(alpha = 0.2f) else SlateSurfaceVariant
                    ) {
                        Text(
                            if (isPublished) "PUBLISHED" else "DRAFT",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = if (isPublished) EmeraldSuccess else TextMuted,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Box {
                        IconButton(onClick = { menuExpanded = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "Options", tint = TextMuted)
                        }
                        DropdownMenu(
                            expanded = menuExpanded,
                            onDismissRequest = { menuExpanded = false },
                            modifier = Modifier.background(SlateSurfaceVariant)
                        ) {
                            DropdownMenuItem(
                                text = { Text("Duplicate Project", color = TextPrimary) },
                                onClick = { menuExpanded = false; onDuplicate() },
                                leadingIcon = { Icon(Icons.Default.ContentCopy, contentDescription = null, tint = TextSecondary) }
                            )
                            DropdownMenuItem(
                                text = { Text("Publishing Settings", color = TextPrimary) },
                                onClick = { menuExpanded = false; onOpenPublish() },
                                leadingIcon = { Icon(Icons.Default.Publish, contentDescription = null, tint = CyanAccent) }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete Project", color = RoseError) },
                                onClick = { menuExpanded = false; onDelete() },
                                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = RoseError) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(project.description, style = MaterialTheme.typography.bodySmall, color = TextMuted)

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Version: v${project.deploymentVersion} • Modified: $dateStr", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                Text("${project.analyticsViews} visits", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
            }

            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onOpenBuilder,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    modifier = Modifier.weight(1f).height(38.dp).testTag("card_builder_${project.id.take(6)}")
                ) {
                    Icon(Icons.Default.Brush, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Visual Editor", style = MaterialTheme.typography.labelMedium)
                }

                OutlinedButton(
                    onClick = onOpenCode,
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                    modifier = Modifier.height(38.dp)
                ) {
                    Icon(Icons.Default.Code, contentDescription = "Code", tint = TextSecondary, modifier = Modifier.size(16.dp))
                }

                OutlinedButton(
                    onClick = onOpenPublish,
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                    modifier = Modifier.height(38.dp)
                ) {
                    Icon(Icons.Default.Publish, contentDescription = "Publish", tint = CyanAccent, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
