package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.AiCreditWallet
import com.example.data.model.AiTransaction
import com.example.data.model.AnalyticsData
import com.example.data.model.BlogPost
import com.example.data.model.EcommerceProduct
import com.example.data.model.FormSubmission
import com.example.data.model.SubscriptionPlan
import com.example.data.model.SystemSettings
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 1. BLOG CMS SCREEN
@Composable
fun BlogCmsScreen(
    project: WebsiteProject?,
    posts: List<BlogPost>,
    onAddPost: (title: String, excerpt: String, content: String, category: String, tags: String) -> Unit,
    onDeletePost: (String) -> Unit
) {
    if (project == null) {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF090D16)), contentAlignment = Alignment.Center) {
            Text("No website selected", color = TextMuted)
        }
        return
    }

    var showCreateDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Blog CMS", style = MaterialTheme.typography.displayMedium, color = TextPrimary)
                Text("${project.name} • Articles & Editorial Posts", style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
            Button(
                onClick = { showCreateDialog = true },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                modifier = Modifier.testTag("create_blog_post_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("New Article")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (posts.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Article, contentDescription = null, tint = TextMuted, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No blog posts published yet", color = TextMuted)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(posts, key = { it.id }) { post ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SlateSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(shape = RoundedCornerShape(4.dp), color = IndigoPrimary.copy(alpha = 0.2f)) {
                                    Text(post.category, style = MaterialTheme.typography.labelSmall, color = CyanAccent, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                                IconButton(onClick = { onDeletePost(post.id) }, modifier = Modifier.size(28.dp)) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = RoseError, modifier = Modifier.size(16.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(post.title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(post.excerpt, style = MaterialTheme.typography.bodySmall, color = TextMuted)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Author: ${post.author} • Tags: ${post.tags}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateBlogDialog(
            onSave = { t, e, c, cat, tags ->
                onAddPost(t, e, c, cat, tags)
                showCreateDialog = false
            },
            onDismiss = { showCreateDialog = false }
        )
    }
}

@Composable
private fun CreateBlogDialog(
    onSave: (title: String, excerpt: String, content: String, category: String, tags: String) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var excerpt by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Tech & Design") }
    var tags by remember { mutableStateOf("startup, growth") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState())) {
                Text("Write New Article", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                Spacer(modifier = Modifier.height(14.dp))
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Article Title *") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(value = excerpt, onValueChange = { excerpt = it }, label = { Text("Short Excerpt *") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Article Body Content *") }, modifier = Modifier.fillMaxWidth().height(120.dp))
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { if (title.isNotBlank() && content.isNotBlank()) onSave(title, excerpt, content, category, tags) },
                    enabled = title.isNotBlank() && content.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().height(44.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                ) {
                    Text("Publish Article")
                }
            }
        }
    }
}

// 2. FORMS & LEADS SCREEN
@Composable
fun FormsLeadsScreen(
    leads: List<FormSubmission>,
    onUpdateStatus: (id: String, status: String) -> Unit,
    onDeleteLead: (id: String) -> Unit,
    onShowToast: (String) -> Unit
) {
    val context = LocalContext.current
    var showCsvModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Forms & Captured Leads", style = MaterialTheme.typography.displayMedium, color = TextPrimary)
                Text("${leads.size} customer inquiries received", style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
            Button(
                onClick = { showCsvModal = true },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SlateSurfaceVariant),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                modifier = Modifier.testTag("export_leads_csv_button")
            ) {
                Text("Export CSV", style = MaterialTheme.typography.labelSmall, color = TextPrimary)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (leads.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Email, contentDescription = null, tint = TextMuted, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No form submissions yet. Form leads submitted on published sites appear here.", color = TextMuted, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(leads, key = { it.id }) { lead ->
                    val dateFormatted = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()).format(Date(lead.submittedAt))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SlateSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(lead.senderName, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                                Surface(shape = RoundedCornerShape(4.dp), color = IndigoPrimary.copy(alpha = 0.2f)) {
                                    Text(lead.websiteName, style = MaterialTheme.typography.labelSmall, color = CyanAccent, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("${lead.senderEmail} ${if (lead.senderPhone.isNotBlank()) "• ${lead.senderPhone}" else ""}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = SlateSurfaceVariant),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    "\"${lead.message}\"",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextPrimary,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(dateFormatted, style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    val statuses = listOf("New", "Contacted", "Converted")
                                    statuses.forEach { st ->
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = if (lead.status == st) EmeraldSuccess else SlateSurfaceVariant,
                                            modifier = Modifier.clickable { onUpdateStatus(lead.id, st) }
                                        ) {
                                            Text(
                                                st,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = if (lead.status == st) Color.White else TextMuted,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCsvModal) {
        val csvText = buildString {
            append("ID,Website,Name,Email,Phone,Message,Status,SubmittedAt\n")
            leads.forEach { l ->
                append("\"${l.id}\",\"${l.websiteName}\",\"${l.senderName}\",\"${l.senderEmail}\",\"${l.senderPhone}\",\"${l.message.replace("\"", "'")}\",\"${l.status}\",\"${l.submittedAt}\"\n")
            }
        }
        Dialog(onDismissRequest = { showCsvModal = false }) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Export Leads (CSV Format)", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                    Spacer(modifier = Modifier.height(10.dp))
                    Card(modifier = Modifier.fillMaxWidth().height(160.dp), colors = CardDefaults.cardColors(containerColor = SlateSurfaceVariant)) {
                        Text(csvText, style = MaterialTheme.typography.labelSmall, color = TextSecondary, modifier = Modifier.padding(8.dp).verticalScroll(rememberScrollState()))
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = {
                            val clip = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clip.setPrimaryClip(ClipData.newPlainText("Leads CSV", csvText))
                            showCsvModal = false
                            onShowToast("✓ CSV copied to clipboard!")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                    ) {
                        Text("Copy CSV Data")
                    }
                }
            }
        }
    }
}

// 3. PRIVACY ANALYTICS SCREEN
@Composable
fun AnalyticsScreen(analytics: AnalyticsData) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("Privacy-Conscious Analytics", style = MaterialTheme.typography.displayMedium, color = TextPrimary)
        Text("No cookies, GDPR compliant, zero third-party telemetry", style = MaterialTheme.typography.bodySmall, color = TextMuted)

        Spacer(modifier = Modifier.height(20.dp))

        // Metrics Grid
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AnalyticsMetricCard("Page Views", "${analytics.totalPageviews}", CyanAccent, Modifier.weight(1f))
            AnalyticsMetricCard("Visitors", "${analytics.uniqueVisitors}", IndigoPrimary, Modifier.weight(1f))
            AnalyticsMetricCard("Bounce Rate", "${analytics.bounceRatePercent}%", EmeraldSuccess, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Daily Views Visual Bar Chart
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Daily Views (Last 7 Days)", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                Spacer(modifier = Modifier.height(16.dp))

                val maxVal = analytics.dailyViews.maxOfOrNull { it.second } ?: 300
                Row(
                    modifier = Modifier.fillMaxWidth().height(140.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    analytics.dailyViews.forEach { (day, count) ->
                        val barHeightRatio = (count.toFloat() / maxVal).coerceIn(0.1f, 1f)
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("$count", style = MaterialTheme.typography.labelSmall, color = CyanAccent)
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .width(22.dp)
                                    .height((100 * barHeightRatio).dp)
                                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                    .background(IndigoPrimary)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(day, style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Traffic Sources Breakdown
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Top Traffic Sources", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                Spacer(modifier = Modifier.height(12.dp))
                analytics.trafficSources.forEach { (src, pct) ->
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(src, style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                            Text("$pct%", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = CyanAccent)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)).background(SlateBorder)) {
                            Box(modifier = Modifier.fillMaxWidth(pct / 100f).height(6.dp).clip(RoundedCornerShape(3.dp)).background(IndigoPrimary))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AnalyticsMetricCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = color)
            Text(title, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        }
    }
}

// 4. ECOMMERCE CATALOG SCREEN
@Composable
fun EcommerceScreen(
    project: WebsiteProject?,
    products: List<EcommerceProduct>,
    onAddProduct: (name: String, price: Double, category: String, desc: String) -> Unit,
    onDeleteProduct: (String) -> Unit
) {
    if (project == null) {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF090D16)), contentAlignment = Alignment.Center) {
            Text("No website selected", color = TextMuted)
        }
        return
    }

    var showAddDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Ecommerce Storefront", style = MaterialTheme.typography.displayMedium, color = TextPrimary)
                Text("${products.size} products in catalogue", style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
            Button(
                onClick = { showAddDialog = true },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                modifier = Modifier.testTag("add_product_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Product")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (products.isEmpty()) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = SlateSurface), shape = RoundedCornerShape(12.dp)) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.ShoppingBag, contentDescription = null, tint = TextMuted, modifier = Modifier.size(40.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("No products added yet", color = TextMuted)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(products, key = { it.id }) { prod ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = SlateSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(prod.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                                Text("₹${prod.price.toInt()} • In Stock (${prod.inventoryCount})", style = MaterialTheme.typography.bodySmall, color = EmeraldSuccess)
                                Text(prod.description, style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            }
                            IconButton(onClick = { onDeleteProduct(prod.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = RoseError)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var pName by remember { mutableStateOf("") }
        var pPrice by remember { mutableStateOf("") }
        var pCategory by remember { mutableStateOf("Digital") }
        var pDesc by remember { mutableStateOf("") }

        Dialog(onDismissRequest = { showAddDialog = false }) {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Add Product to Store", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(value = pName, onValueChange = { pName = it }, label = { Text("Product Name *") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(value = pPrice, onValueChange = { pPrice = it }, label = { Text("Price in INR (₹) *") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(value = pDesc, onValueChange = { pDesc = it }, label = { Text("Short Description") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            val priceVal = pPrice.toDoubleOrNull() ?: 999.0
                            if (pName.isNotBlank()) {
                                onAddProduct(pName, priceVal, pCategory, pDesc)
                                showAddDialog = false
                            }
                        },
                        enabled = pName.isNotBlank(),
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                    ) {
                        Text("Add Product")
                    }
                }
            }
        }
    }
}

// 5. AI CREDITS & WALLET SCREEN
@Composable
fun AiCreditsScreen(
    wallet: AiCreditWallet,
    transactions: List<AiTransaction>,
    onBuyCredits: (credits: Int, priceInr: Double) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("AI Credits & Wallet", style = MaterialTheme.typography.displayMedium, color = TextPrimary)
        Text("Manage credits for generating websites and AI modifications", style = MaterialTheme.typography.bodySmall, color = TextMuted)

        Spacer(modifier = Modifier.height(20.dp))

        // Balance Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, IndigoPrimary)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Available AI Credits", style = MaterialTheme.typography.labelMedium, color = TextMuted)
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("${wallet.balance}", style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold), color = CyanAccent)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Credits", style = MaterialTheme.typography.titleMedium, color = TextSecondary)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text("• 10 Credits = 1 Full Website Generation\n• 2 Credits = 1 In-Editor AI Modification", style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Buy Packs
        Text("Purchase Credit Packs (Cashfree Gateway)", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CreditPackCard("50 Credits", "₹299", Modifier.weight(1f)) { onBuyCredits(50, 299.0) }
            CreditPackCard("150 Credits", "₹699", Modifier.weight(1f), badge = "Popular") { onBuyCredits(150, 699.0) }
            CreditPackCard("500 Credits", "₹1,999", Modifier.weight(1f)) { onBuyCredits(500, 1999.0) }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Transactions History
        Text("Credits Ledger & History", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
        Spacer(modifier = Modifier.height(10.dp))

        if (transactions.isEmpty()) {
            Text("No credit transactions recorded yet.", color = TextMuted)
        } else {
            transactions.forEach { tx ->
                val dateStr = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()).format(Date(tx.timestamp))
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateSurface),
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(tx.description, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
                            Text(dateStr, style = MaterialTheme.typography.labelSmall, color = TextMuted)
                        }
                        Text(
                            text = if (tx.amount > 0) "+${tx.amount}" else "${tx.amount}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (tx.amount > 0) EmeraldSuccess else RoseError
                        )
                    }
                }
            }
        }
    }
}

// 6. SUPER ADMIN DASHBOARD
@Composable
fun SuperAdminScreen(
    projects: List<WebsiteProject>,
    settings: SystemSettings,
    onSaveSettings: (SystemSettings) -> Unit
) {
    var geminiKey by remember { mutableStateOf(settings.geminiApiKey) }
    var cfAppId by remember { mutableStateOf(settings.cashfreeAppId) }
    var cfSecKey by remember { mutableStateOf(settings.cashfreeSecretKey) }
    var baseDomain by remember { mutableStateOf(settings.baseDomain) }
    var cfEnv by remember { mutableStateOf(settings.cashfreeEnvironment) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = RoseError, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text("Super Admin Dashboard", style = MaterialTheme.typography.displayMedium, color = TextPrimary)
                Text("Platform telemetry, Cashfree & Cloudflare management", style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // System Metrics
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            AnalyticsMetricCard("Total Users", "1,480", IndigoPrimary, Modifier.weight(1f))
            AnalyticsMetricCard("Sites Built", "${projects.size + 3120}", CyanAccent, Modifier.weight(1f))
            AnalyticsMetricCard("Platform GMV", "₹14.8L", EmeraldSuccess, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Payment & Cashfree API Settings
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("Cashfree Gateway Configuration", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                Text("Keep Cashfree secret keys server-side only as per specifications", style = MaterialTheme.typography.labelSmall, color = TextMuted)

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(value = cfAppId, onValueChange = { cfAppId = it }, label = { Text("CASHFREE_APP_ID") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(value = cfSecKey, onValueChange = { cfSecKey = it }, label = { Text("CASHFREE_SECRET_KEY") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(value = baseDomain, onValueChange = { baseDomain = it }, label = { Text("SITEGEN_BASE_DOMAIN") }, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(14.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Environment: $cfEnv", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = { cfEnv = if (cfEnv == "SANDBOX") "PRODUCTION" else "SANDBOX" },
                        colors = ButtonDefaults.buttonColors(containerColor = SlateSurfaceVariant)
                    ) {
                        Text("Toggle to ${if (cfEnv == "SANDBOX") "PROD" else "SANDBOX"}", style = MaterialTheme.typography.labelSmall, color = CyanAccent)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        onSaveSettings(
                            settings.copy(
                                cashfreeAppId = cfAppId,
                                cashfreeSecretKey = cfSecKey,
                                baseDomain = baseDomain,
                                cashfreeEnvironment = cfEnv
                            )
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(42.dp).testTag("save_admin_settings_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                ) {
                    Text("Save System Settings")
                }
            }
        }
    }
}

// 7. HELP & DOCUMENTATION SCREEN
@Composable
fun HelpSupportScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("Help & Technical Documentation", style = MaterialTheme.typography.displayMedium, color = TextPrimary)
        Text("Architecture specs, Supabase, Cashfree, and Cloudflare setup guides", style = MaterialTheme.typography.bodySmall, color = TextMuted)

        Spacer(modifier = Modifier.height(20.dp))

        DocSection("1. Cashfree Payments Integration", """
            • SITEGEN strictly integrates Cashfree Payments (official documentation: https://www.cashfree.com/docs/payments/overview).
            • Order creation is handled server-side before presenting the Cashfree hosted checkout modal.
            • We never trust frontend success callbacks alone; server-side signature verification is enforced.
            • Environment Variables:
              - CASHFREE_APP_ID
              - CASHFREE_SECRET_KEY
              - CASHFREE_API_VERSION (2023-08-01)
              - CASHFREE_WEBHOOK_SECRET
        """.trimIndent())

        DocSection("2. Cloudflare Edge Architecture", """
            • Shared publishing architecture using Cloudflare Workers and Cloudflare R2 bucket (sitegen-published-sites).
            • Rather than spinning up a separate Cloudflare Pages project for every website, a high-performance Workers edge gateway routes requests based on subdomain or custom host headers.
            • Automated SSL provisioning via Cloudflare for all custom CNAME records.
        """.trimIndent())

        DocSection("3. Supabase & PostgreSQL Schema", """
            • Tables: profiles, projects, website_pages, website_versions, templates, assets, subscriptions, payments, ai_credit_wallets, form_submissions, custom_domains.
            • Row Level Security (RLS) is enabled so users can only view and modify their own projects, leads, and assets.
        """.trimIndent())

        DocSection("4. Google Gemini API Engine", """
            • Uses Gemini API through server-side JSON schema generation.
            • Generates structured website representation using approved components and blocks.
            • Output sanitization ensures no arbitrary unverified scripts are executed.
        """.trimIndent())
    }
}

@Composable
private fun DocSection(title: String, body: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = CyanAccent)
            Spacer(modifier = Modifier.height(8.dp))
            Text(body, style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp), color = TextSecondary)
        }
    }
}
