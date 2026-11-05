package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.window.Dialog
import com.example.data.generator.TemplatesCatalog
import com.example.data.generator.WebsiteCompiler
import com.example.data.model.WebsiteProject
import com.example.ui.components.SandboxedWebsiteWebView
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.SlateSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.DeviceMode

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TemplatesBrowserScreen(
    onUseTemplate: (WebsiteProject) -> Unit
) {
    var selectedCategory by remember { mutableStateOf("All") }
    var previewProject by remember { mutableStateOf<WebsiteProject?>(null) }

    val categories = listOf("All", "Agency", "SaaS", "Portfolio", "Restaurant", "Coaching", "Ecommerce", "Real Estate", "Blog", "Event", "Personal")
    val filtered = if (selectedCategory == "All") TemplatesCatalog.templates else TemplatesCatalog.templates.filter { it.category.equals(selectedCategory, true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .padding(20.dp)
    ) {
        Column {
            Text("Starter Templates", style = MaterialTheme.typography.displayMedium, color = TextPrimary)
            Text("10 original production-grade templates with responsive layouts & content", style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Category Filter Chips
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { cat ->
                val isSelected = selectedCategory == cat
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) IndigoPrimary else SlateSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) IndigoPrimary else SlateBorder),
                    modifier = Modifier.clickable { selectedCategory = cat }
                ) {
                    Text(
                        cat,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = if (isSelected) Color.White else TextSecondary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            items(filtered, key = { it.id }) { tpl ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(tpl.name, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                                Text(tpl.description, style = MaterialTheme.typography.bodySmall, color = TextMuted)
                            }
                            Surface(shape = RoundedCornerShape(4.dp), color = IndigoPrimary.copy(alpha = 0.2f)) {
                                Text(tpl.category, style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = CyanAccent, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Features & Color Dots
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(Color(android.graphics.Color.parseColor(tpl.primaryColorHex)))
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(Color(android.graphics.Color.parseColor(tpl.secondaryColorHex)))
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text("${tpl.themeStyle} • ${tpl.pages.firstOrNull()?.sections?.size ?: 6} sections", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { onUseTemplate(tpl) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                                modifier = Modifier.weight(1f).height(40.dp).testTag("use_template_btn_${tpl.id.take(8)}")
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Use Template", fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { previewProject = tpl },
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                                modifier = Modifier.height(40.dp).testTag("preview_tpl_btn_${tpl.id.take(8)}")
                            ) {
                                Icon(Icons.Default.Visibility, contentDescription = "Preview", tint = CyanAccent, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Live Demo", color = CyanAccent)
                            }
                        }
                    }
                }
            }
        }
    }

    // Fullscreen Live Demo Preview Dialog
    if (previewProject != null) {
        val proj = previewProject!!
        val html = remember(proj) { WebsiteCompiler.compileToHtml(proj) }

        Dialog(onDismissRequest = { previewProject = null }) {
            Card(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(proj.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                            Text("Interactive Live Demo • ${proj.category}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        }
                        IconButton(onClick = { previewProject = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                        }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        SandboxedWebsiteWebView(htmlContent = html, deviceMode = DeviceMode.MOBILE)
                    }
                    Button(
                        onClick = {
                            onUseTemplate(proj)
                            previewProject = null
                        },
                        modifier = Modifier.fillMaxWidth().padding(14.dp).height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                    ) {
                        Text("Use This Template to Build My Site", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
