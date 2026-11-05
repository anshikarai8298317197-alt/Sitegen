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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.window.Dialog
import com.example.data.generator.WebsiteCompiler
import com.example.data.model.SectionType
import com.example.data.model.WebsiteProject
import com.example.data.model.WebsiteSection
import com.example.ui.components.AiAssistantDialog
import com.example.ui.components.SandboxedWebsiteWebView
import com.example.ui.components.ViewportSwitcherBar
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
import com.example.ui.viewmodel.DeviceMode
import com.example.ui.viewmodel.Screen

@Composable
fun VisualEditorScreen(
    project: WebsiteProject?,
    deviceMode: DeviceMode,
    onDeviceModeChange: (DeviceMode) -> Unit,
    onUpdateSection: (WebsiteSection) -> Unit,
    onAddSection: (SectionType) -> Unit,
    onDeleteSection: (String) -> Unit,
    onReorderSection: (Int, Int) -> Unit,
    onUpdateProjectDetails: (name: String, category: String, desc: String, primaryColor: String, secondaryColor: String, theme: String) -> Unit,
    onUpdateSeo: (String, String, String) -> Unit,
    onApplyAiPrompt: (String) -> Unit,
    onUndo: () -> Unit,
    onRedo: () -> Unit,
    onNavigate: (Screen) -> Unit,
    onLeadCaptured: (name: String, email: String, phone: String, msg: String) -> Unit
) {
    if (project == null) {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF090D16)), contentAlignment = Alignment.Center) {
            Text("No project selected. Please pick a website from My Websites.", color = TextMuted)
        }
        return
    }

    var activeTab by remember { mutableIntStateOf(0) } // 0: Canvas, 1: Sections, 2: Theme/Styles, 3: SEO
    var sectionToEdit by remember { mutableStateOf<WebsiteSection?>(null) }
    var showAddSectionDialog by remember { mutableStateOf(false) }
    var showAiAssistantDialog by remember { mutableStateOf(false) }

    val homePage = project.pages.firstOrNull { it.isHome } ?: project.pages.firstOrNull()
    val sections = homePage?.sections ?: emptyList()
    val compiledHtml = remember(project) { WebsiteCompiler.compileToHtml(project) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
    ) {
        // Editor Control Header
        Surface(
            color = SlateSurface,
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                project.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = IndigoPrimary.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    project.subdomain,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = CyanAccent,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text("Visual Block Editor • Autosaved", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                    }

                    // Viewport Switcher
                    ViewportSwitcherBar(
                        currentMode = deviceMode,
                        onModeChange = onDeviceModeChange
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Toolbar Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        IconButton(onClick = onUndo, modifier = Modifier.size(32.dp).testTag("undo_button")) {
                            Icon(Icons.AutoMirrored.Filled.Undo, contentDescription = "Undo", tint = TextSecondary, modifier = Modifier.size(16.dp))
                        }
                        IconButton(onClick = onRedo, modifier = Modifier.size(32.dp).testTag("redo_button")) {
                            Icon(Icons.AutoMirrored.Filled.Redo, contentDescription = "Redo", tint = TextSecondary, modifier = Modifier.size(16.dp))
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = { showAiAssistantDialog = true },
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = CyanAccent),
                            modifier = Modifier.height(32.dp).testTag("ai_assistant_trigger")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFF0F172A), modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("AI Edit", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color(0xFF0F172A))
                        }

                        OutlinedButton(
                            onClick = { onNavigate(Screen.CODE_EDITOR) },
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                            modifier = Modifier.height(32.dp).testTag("to_code_editor_button")
                        ) {
                            Icon(Icons.Default.Code, contentDescription = null, tint = TextPrimary, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Code", style = MaterialTheme.typography.labelSmall, color = TextPrimary)
                        }

                        Button(
                            onClick = { onNavigate(Screen.PUBLISH_HOSTING) },
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                            modifier = Modifier.height(32.dp).testTag("to_publish_button")
                        ) {
                            Icon(Icons.Default.Publish, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Publish", style = MaterialTheme.typography.labelSmall, color = Color.White)
                        }
                    }
                }
            }
        }

        // Sub Tabs: [Canvas Preview], [Sections List], [Styles & Colors], [SEO Meta]
        TabRow(
            selectedTabIndex = activeTab,
            containerColor = SlateSurface,
            contentColor = TextPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                    color = IndigoPrimary
                )
            }
        ) {
            Tab(
                selected = activeTab == 0,
                onClick = { activeTab = 0 },
                text = { Text("Live Canvas", fontSize = 12.sp, fontWeight = if (activeTab == 0) FontWeight.Bold else FontWeight.Normal) }
            )
            Tab(
                selected = activeTab == 1,
                onClick = { activeTab = 1 },
                text = { Text("Sections (${sections.size})", fontSize = 12.sp, fontWeight = if (activeTab == 1) FontWeight.Bold else FontWeight.Normal) }
            )
            Tab(
                selected = activeTab == 2,
                onClick = { activeTab = 2 },
                text = { Text("Design & Colors", fontSize = 12.sp, fontWeight = if (activeTab == 2) FontWeight.Bold else FontWeight.Normal) }
            )
            Tab(
                selected = activeTab == 3,
                onClick = { activeTab = 3 },
                text = { Text("SEO Meta", fontSize = 12.sp, fontWeight = if (activeTab == 3) FontWeight.Bold else FontWeight.Normal) }
            )
        }

        // Tab Content
        Box(modifier = Modifier.fillMaxSize()) {
            when (activeTab) {
                0 -> {
                    // Live Canvas
                    SandboxedWebsiteWebView(
                        htmlContent = compiledHtml,
                        deviceMode = deviceMode,
                        onLeadCaptured = onLeadCaptured
                    )
                }
                1 -> {
                    // Sections Reorder & Edit
                    SectionsManagerTab(
                        sections = sections,
                        onEditSection = { sectionToEdit = it },
                        onDeleteSection = onDeleteSection,
                        onMoveUp = { idx -> if (idx > 0) onReorderSection(idx, idx - 1) },
                        onMoveDown = { idx -> if (idx < sections.size - 1) onReorderSection(idx, idx + 1) },
                        onAddSectionClick = { showAddSectionDialog = true }
                    )
                }
                2 -> {
                    // Design & Styles
                    DesignStylesTab(
                        project = project,
                        onSaveStyles = onUpdateProjectDetails
                    )
                }
                3 -> {
                    // SEO
                    SeoMetaTab(
                        project = project,
                        onSaveSeo = onUpdateSeo
                    )
                }
            }
        }
    }

    // Section Properties Inspector Modal
    if (sectionToEdit != null) {
        SectionEditDialog(
            section = sectionToEdit!!,
            onSave = { updated ->
                onUpdateSection(updated)
                sectionToEdit = null
            },
            onDismiss = { sectionToEdit = null }
        )
    }

    // Add Section Dialog
    if (showAddSectionDialog) {
        AddSectionDialog(
            onSelectType = { type ->
                onAddSection(type)
                showAddSectionDialog = false
            },
            onDismiss = { showAddSectionDialog = false }
        )
    }

    // AI Assistant Dialog
    AiAssistantDialog(
        isOpen = showAiAssistantDialog,
        onDismiss = { showAiAssistantDialog = false },
        onApplyPrompt = onApplyAiPrompt
    )
}

@Composable
private fun SectionsManagerTab(
    sections: List<WebsiteSection>,
    onEditSection: (WebsiteSection) -> Unit,
    onDeleteSection: (String) -> Unit,
    onMoveUp: (Int) -> Unit,
    onMoveDown: (Int) -> Unit,
    onAddSectionClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Page Sections", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                Text("Reorder, edit copy, or add new blocks to this page", style = MaterialTheme.typography.labelSmall, color = TextMuted)
            }
            Button(
                onClick = onAddSectionClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                modifier = Modifier.height(36.dp).testTag("add_section_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Block", style = MaterialTheme.typography.labelSmall)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(sections, key = { _, sec -> sec.id }) { index, sec ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = IndigoPrimary.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    "${index + 1}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = CyanAccent,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(sec.type.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
                                Text(
                                    sec.title.ifBlank { sec.subtitle.ifBlank { "Section block" } },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted,
                                    maxLines = 1
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { onMoveUp(index) }, enabled = index > 0, modifier = Modifier.size(30.dp)) {
                                Icon(Icons.Default.ArrowUpward, contentDescription = "Move Up", tint = if (index > 0) TextSecondary else TextMuted, modifier = Modifier.size(16.dp))
                            }
                            IconButton(onClick = { onMoveDown(index) }, enabled = index < sections.size - 1, modifier = Modifier.size(30.dp)) {
                                Icon(Icons.Default.ArrowDownward, contentDescription = "Move Down", tint = if (index < sections.size - 1) TextSecondary else TextMuted, modifier = Modifier.size(16.dp))
                            }
                            IconButton(onClick = { onEditSection(sec) }, modifier = Modifier.size(30.dp)) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit Properties", tint = CyanAccent, modifier = Modifier.size(16.dp))
                            }
                            if (sec.type != SectionType.NAVBAR && sec.type != SectionType.FOOTER) {
                                IconButton(onClick = { onDeleteSection(sec.id) }, modifier = Modifier.size(30.dp)) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = RoseError, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionEditDialog(
    section: WebsiteSection,
    onSave: (WebsiteSection) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(section.title) }
    var subtitle by remember { mutableStateOf(section.subtitle) }
    var badgeText by remember { mutableStateOf(section.badgeText) }
    var primaryBtnText by remember { mutableStateOf(section.primaryButtonText) }
    var primaryBtnUrl by remember { mutableStateOf(section.primaryButtonUrl) }
    var backgroundStyle by remember { mutableStateOf(section.backgroundStyle) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text("Edit ${section.type.name} Section", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Headline / Title") },
                    modifier = Modifier.fillMaxWidth().testTag("edit_section_title")
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = subtitle,
                    onValueChange = { subtitle = it },
                    label = { Text("Subtitle / Description") },
                    modifier = Modifier.fillMaxWidth().height(90.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = badgeText,
                    onValueChange = { badgeText = it },
                    label = { Text("Badge Label") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = primaryBtnText,
                        onValueChange = { primaryBtnText = it },
                        label = { Text("Button Text") },
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = primaryBtnUrl,
                        onValueChange = { primaryBtnUrl = it },
                        label = { Text("Button URL") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                Text("Background Style:", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                Spacer(modifier = Modifier.height(6.dp))

                val styles = listOf("surface", "dark", "gradient", "default")
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    styles.forEach { st ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (backgroundStyle == st) IndigoPrimary else SlateSurfaceVariant,
                            modifier = Modifier.clickable { backgroundStyle = st }
                        ) {
                            Text(
                                st.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.labelSmall,
                                color = if (backgroundStyle == st) Color.White else TextMuted,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    OutlinedButton(onClick = onDismiss) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            onSave(
                                section.copy(
                                    title = title,
                                    subtitle = subtitle,
                                    badgeText = badgeText,
                                    primaryButtonText = primaryBtnText,
                                    primaryButtonUrl = primaryBtnUrl,
                                    backgroundStyle = backgroundStyle
                                )
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                        modifier = Modifier.testTag("save_section_properties_button")
                    ) {
                        Text("Save Changes")
                    }
                }
            }
        }
    }
}

@Composable
private fun AddSectionDialog(
    onSelectType: (SectionType) -> Unit,
    onDismiss: () -> Unit
) {
    val blockTypes = listOf(
        SectionType.FEATURES to "Feature Cards Grid",
        SectionType.PRICING to "Pricing Tiers Table",
        SectionType.TESTIMONIALS to "Client Reviews",
        SectionType.FAQ to "Expandable FAQ Accordion",
        SectionType.CONTACT_FORM to "Interactive Lead Form",
        SectionType.STATS to "Metrics & Counter Strip",
        SectionType.PORTFOLIO_GALLERY to "Image Showcase Grid",
        SectionType.NEWSLETTER to "Email Subscribe Box"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Add New Component Block", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                Text("Select a section block to append to your webpage", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                Spacer(modifier = Modifier.height(14.dp))

                blockTypes.forEach { (type, desc) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onSelectType(type) }
                            .testTag("add_type_${type.name}"),
                        colors = CardDefaults.cardColors(containerColor = SlateSurfaceVariant),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(type.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = CyanAccent)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("• $desc", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DesignStylesTab(
    project: WebsiteProject,
    onSaveStyles: (name: String, category: String, desc: String, primaryColor: String, secondaryColor: String, theme: String) -> Unit
) {
    var name by remember { mutableStateOf(project.name) }
    var desc by remember { mutableStateOf(project.description) }
    var primaryColor by remember { mutableStateOf(project.primaryColorHex) }
    var secondaryColor by remember { mutableStateOf(project.secondaryColorHex) }
    var theme by remember { mutableStateOf(project.themeStyle) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Design System & Global Tokens", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Brand / Website Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = desc,
            onValueChange = { desc = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = primaryColor,
                onValueChange = { primaryColor = it },
                label = { Text("Primary Hex (e.g. #6366F1)") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = secondaryColor,
                onValueChange = { secondaryColor = it },
                label = { Text("Secondary Hex") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Theme Aesthetics", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
        Spacer(modifier = Modifier.height(6.dp))

        val themeOptions = listOf("Modern Cyber", "Minimalist Light", "Warm Organic", "Emerald Luxury")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            themeOptions.forEach { th ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (theme == th) IndigoPrimary else SlateSurfaceVariant,
                    modifier = Modifier.clickable { theme = th }
                ) {
                    Text(
                        th,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (theme == th) Color.White else TextMuted,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onSaveStyles(name, project.category, desc, primaryColor, secondaryColor, theme) },
            modifier = Modifier.fillMaxWidth().height(44.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
        ) {
            Text("Save Global Styles & Recompile", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SeoMetaTab(
    project: WebsiteProject,
    onSaveSeo: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf(project.seoTitle.ifBlank { "${project.name} | Official Website" }) }
    var desc by remember { mutableStateOf(project.seoDescription.ifBlank { project.description }) }
    var keywords by remember { mutableStateOf(project.seoKeywords.ifBlank { "${project.category}, web, modern" }) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Search Engine Optimization (SEO)", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
        Text("Metadata injected into <head> tags for Google, Open Graph, and Twitter Cards", style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("SEO Meta Title (<title>)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = desc,
            onValueChange = { desc = it },
            label = { Text("Meta Description") },
            modifier = Modifier.fillMaxWidth().height(90.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = keywords,
            onValueChange = { keywords = it },
            label = { Text("Meta Keywords (comma-separated)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Google Search Result Preview Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = SlateSurfaceVariant),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Google Search Result Preview", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                Spacer(modifier = Modifier.height(6.dp))
                Text("https://${project.subdomain}", style = MaterialTheme.typography.labelSmall, color = EmeraldSuccess)
                Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = Color(0xFF60A5FA))
                Text(desc, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onSaveSeo(title, desc, keywords) },
            modifier = Modifier.fillMaxWidth().height(44.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
        ) {
            Text("Save SEO Settings", fontWeight = FontWeight.Bold)
        }
    }
}
