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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.ViewQuilt
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AiCreditWallet
import com.example.data.model.FormSubmission
import com.example.data.model.ProjectStatus
import com.example.data.model.UserProfile
import com.example.data.model.WebsiteProject
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.IndigoPrimary
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
fun DashboardOverviewScreen(
    userProfile: UserProfile,
    projects: List<WebsiteProject>,
    creditWallet: AiCreditWallet,
    leads: List<FormSubmission>,
    onSelectProject: (WebsiteProject) -> Unit,
    onNavigate: (Screen) -> Unit
) {
    val totalSites = projects.size
    val publishedSites = projects.count { it.status == ProjectStatus.PUBLISHED }
    val totalVisitors = projects.sumOf { it.analyticsViews }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // User Greeting
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Welcome back, ${userProfile.name}",
                    style = MaterialTheme.typography.displayMedium,
                    color = TextPrimary
                )
                Text(
                    text = "Build, customize and publish websites with AI",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = EmeraldSuccess.copy(alpha = 0.15f),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.4f))
            ) {
                Text(
                    "PRO PLAN",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = EmeraldSuccess,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Metrics Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            MetricCard("My Websites", "$totalSites", Icons.Default.Web, IndigoPrimary, Modifier.weight(1f))
            MetricCard("Published", "$publishedSites", Icons.Default.Publish, EmeraldSuccess, Modifier.weight(1f))
            MetricCard("Total Visits", "$totalVisitors", Icons.Default.Analytics, CyanAccent, Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(14.dp))

        // AI Credit Balance Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, IndigoPrimary.copy(alpha = 0.6f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(IndigoPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CyanAccent)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("AI Credits Wallet", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                        Text("${creditWallet.balance} credits remaining (10/site gen)", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    }
                }
                Button(
                    onClick = { onNavigate(Screen.AI_CREDITS) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    modifier = Modifier.testTag("buy_more_credits_button")
                ) {
                    Text("+ Top Up", style = MaterialTheme.typography.labelMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quick Actions Row
        Text("Quick Actions", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            QuickActionButton("AI Generator", Icons.Default.AutoAwesome, IndigoPrimary, Modifier.weight(1f)) {
                onNavigate(Screen.CREATE_AI_WIZARD)
            }
            QuickActionButton("Templates", Icons.Default.ViewQuilt, CyanAccent, Modifier.weight(1f)) {
                onNavigate(Screen.TEMPLATES_BROWSER)
            }
            QuickActionButton("Leads (${leads.size})", Icons.Default.Email, EmeraldSuccess, Modifier.weight(1f)) {
                onNavigate(Screen.FORMS_LEADS)
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Recent Websites Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Recent Websites",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
            Text(
                "Manage All →",
                style = MaterialTheme.typography.labelLarge,
                color = CyanAccent,
                modifier = Modifier.clickable { onNavigate(Screen.MY_WEBSITES) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (projects.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No websites generated yet", style = MaterialTheme.typography.bodyMedium, color = TextMuted)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { onNavigate(Screen.CREATE_AI_WIZARD) },
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                    ) {
                        Text("Create Your First Website with AI")
                    }
                }
            }
        } else {
            projects.take(4).forEach { proj ->
                DashboardWebsiteItem(
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
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Recent Leads Ticker
        if (leads.isNotEmpty()) {
            Text("Recent Leads Captured", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
            Spacer(modifier = Modifier.height(10.dp))

            leads.take(3).forEach { lead ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = SlateSurface),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(lead.senderName, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                            Text(lead.senderEmail, style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Text("\"${lead.message.take(60)}...\"", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = CyanAccent.copy(alpha = 0.15f)
                        ) {
                            Text(lead.websiteName, style = MaterialTheme.typography.labelSmall, color = CyanAccent, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
            Text(title, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        }
    }
}

@Composable
private fun QuickActionButton(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accent: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = SlateSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = label, tint = accent, modifier = Modifier.size(22.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(label, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
        }
    }
}

@Composable
private fun DashboardWebsiteItem(
    project: WebsiteProject,
    onOpenBuilder: () -> Unit,
    onOpenCode: () -> Unit,
    onOpenPublish: () -> Unit
) {
    val isPublished = project.status == ProjectStatus.PUBLISHED
    val dateStr = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault()).format(Date(project.lastModified))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(project.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                    Text(
                        project.customDomain ?: project.subdomain,
                        style = MaterialTheme.typography.labelSmall,
                        color = CyanAccent
                    )
                }
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (isPublished) EmeraldSuccess.copy(alpha = 0.2f) else SlateSurfaceVariant
                ) {
                    Text(
                        if (isPublished) "PUBLISHED" else "DRAFT",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (isPublished) EmeraldSuccess else TextMuted,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text("Last edited: $dateStr • ${project.pages.size} page(s) • ${project.themeStyle}", style = MaterialTheme.typography.labelSmall, color = TextMuted)

            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onOpenBuilder,
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    modifier = Modifier.weight(1f).height(36.dp).testTag("open_builder_${project.id.take(6)}")
                ) {
                    Icon(Icons.Default.Brush, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Visual Editor", style = MaterialTheme.typography.labelSmall)
                }

                OutlinedButton(
                    onClick = onOpenCode,
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                    modifier = Modifier.height(36.dp).testTag("open_code_${project.id.take(6)}")
                ) {
                    Icon(Icons.Default.Code, contentDescription = null, modifier = Modifier.size(14.dp), tint = TextSecondary)
                }

                OutlinedButton(
                    onClick = onOpenPublish,
                    shape = RoundedCornerShape(6.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                    modifier = Modifier.height(36.dp).testTag("open_publish_${project.id.take(6)}")
                ) {
                    Icon(Icons.Default.Publish, contentDescription = null, modifier = Modifier.size(14.dp), tint = CyanAccent)
                }
            }
        }
    }
}
