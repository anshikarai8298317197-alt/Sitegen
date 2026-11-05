package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.generator.WebsiteCompiler
import com.example.data.generator.WebsiteJsonUtils
import com.example.data.model.WebsiteProject
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.theme.SlateBorder
import com.example.ui.theme.SlateSurface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CodeEditorScreen(
    project: WebsiteProject?,
    onSaveCustomCode: (customCss: String, customJs: String) -> Unit,
    onShowToast: (String) -> Unit
) {
    if (project == null) {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF090D16)), contentAlignment = Alignment.Center) {
            Text("No website selected", color = TextMuted)
        }
        return
    }

    val context = LocalContext.current
    var codeTab by remember { mutableIntStateOf(0) } // 0: HTML (Compiled), 1: Custom CSS, 2: Custom JS, 3: JSON Schema
    var customCss by remember { mutableStateOf(project.customCss) }
    var customJs by remember { mutableStateOf(project.customJs) }

    val compiledHtml = remember(project, customCss, customJs) {
        WebsiteCompiler.compileToHtml(project.copy(customCss = customCss, customJs = customJs))
    }
    val jsonSchema = remember(project) {
        WebsiteJsonUtils.serializePages(project.pages)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
    ) {
        // Code Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SlateSurface)
                .border(1.dp, SlateBorder)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${project.name} • Code View",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Text(
                    text = "Monaco-style isolated syntax container",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        val toCopy = when (codeTab) {
                            0 -> compiledHtml
                            1 -> customCss
                            2 -> customJs
                            else -> jsonSchema
                        }
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        clipboard.setPrimaryClip(ClipData.newPlainText("SITEGEN Code", toCopy))
                        onShowToast("✓ Code copied to clipboard!")
                    },
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SlateBorder),
                    modifier = Modifier.height(34.dp).testTag("copy_code_button")
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(14.dp), tint = TextPrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copy", style = MaterialTheme.typography.labelSmall, color = TextPrimary)
                }

                Button(
                    onClick = {
                        onSaveCustomCode(customCss, customJs)
                    },
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    modifier = Modifier.height(34.dp).testTag("save_recompile_code_button")
                ) {
                    Icon(Icons.Default.Save, contentDescription = "Save", modifier = Modifier.size(14.dp), tint = Color.White)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Save & Recompile", style = MaterialTheme.typography.labelSmall, color = Color.White)
                }
            }
        }

        // Tabs
        TabRow(
            selectedTabIndex = codeTab,
            containerColor = SlateSurface,
            contentColor = TextPrimary,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[codeTab]),
                    color = IndigoPrimary
                )
            }
        ) {
            Tab(selected = codeTab == 0, onClick = { codeTab = 0 }, text = { Text("HTML (Compiled)", fontSize = 11.sp) })
            Tab(selected = codeTab == 1, onClick = { codeTab = 1 }, text = { Text("Custom CSS", fontSize = 11.sp) })
            Tab(selected = codeTab == 2, onClick = { codeTab = 2 }, text = { Text("Custom JS", fontSize = 11.sp) })
            Tab(selected = codeTab == 3, onClick = { codeTab = 3 }, text = { Text("JSON Schema", fontSize = 11.sp) })
        }

        // Code Editor View
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
        ) {
            when (codeTab) {
                0 -> {
                    // Read-only compiled HTML view with line numbers
                    CodeViewerBox(code = compiledHtml)
                }
                1 -> {
                    // Editable Custom CSS
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            "/* Injected into <style> tag of the website */",
                            style = TextStyle(fontFamily = JetBrainsMonoFontFamily, fontSize = 12.sp, color = CyanAccent),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = customCss,
                            onValueChange = { customCss = it },
                            placeholder = { Text("/* Write custom CSS overrides here...\n.hero-title { letter-spacing: -2px; } */", color = TextMuted) },
                            textStyle = TextStyle(fontFamily = JetBrainsMonoFontFamily, fontSize = 13.sp, color = Color(0xFFF8FAFC)),
                            modifier = Modifier.fillMaxSize().testTag("custom_css_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF070B14),
                                unfocusedContainerColor = Color(0xFF070B14),
                                focusedBorderColor = IndigoPrimary,
                                unfocusedBorderColor = SlateBorder
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
                2 -> {
                    // Editable Custom JS
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            "// Injected into <script> tag before </body>",
                            style = TextStyle(fontFamily = JetBrainsMonoFontFamily, fontSize = 12.sp, color = EmeraldSuccess),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = customJs,
                            onValueChange = { customJs = it },
                            placeholder = { Text("// Write custom JavaScript here...\nconsole.log('Site loaded!');", color = TextMuted) },
                            textStyle = TextStyle(fontFamily = JetBrainsMonoFontFamily, fontSize = 13.sp, color = Color(0xFFF8FAFC)),
                            modifier = Modifier.fillMaxSize().testTag("custom_js_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Color(0xFF070B14),
                                unfocusedContainerColor = Color(0xFF070B14),
                                focusedBorderColor = IndigoPrimary,
                                unfocusedBorderColor = SlateBorder
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
                3 -> {
                    // JSON Schema
                    CodeViewerBox(code = jsonSchema)
                }
            }
        }
    }
}

@Composable
private fun CodeViewerBox(code: String) {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF070B14)),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        shape = RoundedCornerShape(8.dp)
    ) {
        val lines = code.lines()
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Line numbers gutter
            Column(
                modifier = Modifier
                    .padding(end = 12.dp)
                    .width(36.dp),
                horizontalAlignment = Alignment.End
            ) {
                lines.forEachIndexed { i, _ ->
                    Text(
                        text = "${i + 1}",
                        style = TextStyle(fontFamily = JetBrainsMonoFontFamily, fontSize = 12.sp, color = TextMuted)
                    )
                }
            }

            // Code text
            Column(
                modifier = Modifier
                    .weight(1f)
                    .horizontalScroll(rememberScrollState())
            ) {
                lines.forEach { line ->
                    Text(
                        text = line,
                        style = TextStyle(fontFamily = JetBrainsMonoFontFamily, fontSize = 12.sp, color = Color(0xFFE2E8F0))
                    )
                }
            }
        }
    }
}
