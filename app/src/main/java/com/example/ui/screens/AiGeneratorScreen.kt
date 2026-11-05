package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.data.generator.WebsiteGenerationParams
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
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AiGeneratorScreen(
    initialPrompt: String = "",
    creditBalance: Int,
    onGenerate: (WebsiteGenerationParams) -> Unit
) {
    var websiteName by remember { mutableStateOf("Delhi Growth Studio") }
    var category by remember { mutableStateOf("Agency") }
    var description by remember {
        mutableStateOf(
            if (initialPrompt.isNotBlank()) initialPrompt
            else "A modern, high-converting digital marketing and growth agency in Delhi serving ambitious D2C brands."
        )
    }
    var themeStyle by remember { mutableStateOf("Modern Cyber") }
    var primaryColorHex by remember { mutableStateOf("#6366F1") }
    var secondaryColorHex by remember { mutableStateOf("#06B6D4") }
    var numPages by remember { mutableIntStateOf(1) }
    var selectedFeatures by remember {
        mutableStateOf(setOf("Hero", "Features", "Pricing", "Testimonials", "Contact Form", "FAQ"))
    }

    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var isGenerating by remember { mutableStateOf(false) }
    var generationStep by remember { mutableStateOf(0) }

    val categories = listOf(
        "Agency", "SaaS", "Portfolio", "Restaurant", "Coaching",
        "Ecommerce", "Real Estate", "Blog", "Event", "Personal",
        "Educational", "Product Catalogue", "Documentation"
    )

    val colorPresets = listOf(
        Pair("#6366F1", "#06B6D4") to "Cyber Indigo",
        Pair("#3B82F6", "#10B981") to "Cloud Tech",
        Pair("#059669", "#0D9488") to "Emerald Luxury",
        Pair("#EA580C", "#D97706") to "Warm Artisan",
        Pair("#EC4899", "#8B5CF6") to "Vibrant Pink",
        Pair("#18181B", "#6366F1") to "Minimal Monochrome"
    )

    val generationSteps = listOf(
        "Analyzing prompt and business intent...",
        "Selecting optimal component structure & navigation...",
        "Generating conversion-focused copy & testimonials...",
        "Assembling responsive HTML5 & CSS design tokens...",
        "Finalizing SEO tags, clean URLs & interactive JS..."
    )

    LaunchedEffect(isGenerating) {
        if (isGenerating) {
            for (step in generationSteps.indices) {
                generationStep = step
                delay(800)
            }
            onGenerate(
                WebsiteGenerationParams(
                    name = websiteName,
                    category = category,
                    description = description,
                    themeStyle = themeStyle,
                    primaryColorHex = primaryColorHex,
                    secondaryColorHex = secondaryColorHex,
                    numPages = numPages,
                    features = selectedFeatures.toList()
                )
            )
            isGenerating = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Top Banner
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("AI Website Generator", style = MaterialTheme.typography.displayMedium, color = TextPrimary)
                Text("Generate complete websites with one prompt", style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = SlateSurfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("$creditBalance Credits", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (isGenerating) {
            // Live Generation Animation Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, IndigoPrimary)
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = CyanAccent,
                        strokeWidth = 3.dp
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Generating Website...", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(generationSteps[generationStep], style = MaterialTheme.typography.bodySmall, color = CyanAccent)

                    Spacer(modifier = Modifier.height(20.dp))
                    generationSteps.forEachIndexed { idx, txt ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            if (idx < generationStep) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                            } else if (idx == generationStep) {
                                CircularProgressIndicator(modifier = Modifier.size(14.dp), strokeWidth = 2.dp, color = CyanAccent)
                            } else {
                                Box(modifier = Modifier.size(14.dp).clip(CircleShape).background(SlateBorder))
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(txt, style = MaterialTheme.typography.labelSmall, color = if (idx <= generationStep) TextPrimary else TextMuted)
                        }
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    // Website Name
                    Text("Website / Business Name *", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = websiteName,
                        onValueChange = { websiteName = it },
                        modifier = Modifier.fillMaxWidth().testTag("ai_name_input"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Website Category
                    Text("Website Category *", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))

                    ExposedDropdownMenuBox(
                        expanded = categoryDropdownExpanded,
                        onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = category,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = categoryDropdownExpanded,
                            onDismissRequest = { categoryDropdownExpanded = false },
                            modifier = Modifier.background(SlateSurfaceVariant)
                        ) {
                            categories.forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat, color = TextPrimary) },
                                    onClick = {
                                        category = cat
                                        categoryDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Business Prompt
                    Text("Prompt / Business Description *", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        placeholder = { Text("e.g. Create a sleek modern website for an enterprise cloud security startup...", color = TextMuted) },
                        modifier = Modifier.fillMaxWidth().height(100.dp).testTag("ai_prompt_textarea"),
                        shape = RoundedCornerShape(8.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Theme Presets
                    Text("Color & Style Presets", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        colorPresets.forEach { (colors, name) ->
                            val isSelected = primaryColorHex == colors.first
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) IndigoPrimary.copy(alpha = 0.2f) else SlateSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) IndigoPrimary else SlateBorder),
                                modifier = Modifier.clickable {
                                    primaryColorHex = colors.first
                                    secondaryColorHex = colors.second
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .clip(CircleShape)
                                            .background(Color(android.graphics.Color.parseColor(colors.first)))
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(name, style = MaterialTheme.typography.labelSmall, color = TextPrimary)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Pages Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Number of Pages", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
                        Text("$numPages Page(s)", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = CyanAccent)
                    }
                    Slider(
                        value = numPages.toFloat(),
                        onValueChange = { numPages = it.toInt() },
                        valueRange = 1f..4f,
                        steps = 2,
                        colors = SliderDefaults.colors(thumbColor = IndigoPrimary, activeTrackColor = IndigoPrimary)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Features Checklist
                    Text("Sections to Include", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
                    Spacer(modifier = Modifier.height(8.dp))

                    val allFeatures = listOf("Hero", "Features", "Pricing", "Testimonials", "FAQ", "Gallery", "Contact Form", "Newsletter")
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        allFeatures.forEach { feat ->
                            val isChecked = selectedFeatures.contains(feat)
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isChecked) IndigoPrimary.copy(alpha = 0.2f) else SlateSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isChecked) IndigoPrimary else SlateBorder),
                                modifier = Modifier.clickable {
                                    selectedFeatures = if (isChecked) selectedFeatures - feat else selectedFeatures + feat
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (isChecked) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                    }
                                    Text(feat, style = MaterialTheme.typography.labelSmall, color = if (isChecked) TextPrimary else TextMuted)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Credit Cost Warning
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SlateSurfaceVariant),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Generating this website will deduct 10 AI Credits from your balance (${creditBalance} available).",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Generate Button
                    Button(
                        onClick = {
                            if (websiteName.isNotBlank() && description.isNotBlank()) {
                                isGenerating = true
                            }
                        },
                        enabled = websiteName.isNotBlank() && description.isNotBlank() && creditBalance >= 10,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("ai_generate_now_button"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate Website with AI (10 Credits)", fontWeight = FontWeight.Bold)
                    }

                    if (creditBalance < 10) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Insufficient credits. Please top up your AI credit wallet in Billing.",
                            style = MaterialTheme.typography.labelSmall,
                            color = RoseError,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
