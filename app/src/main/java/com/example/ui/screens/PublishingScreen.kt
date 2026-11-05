package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import com.example.data.model.DeploymentSnapshot
import com.example.data.model.ProjectStatus
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PublishingScreen(
    project: WebsiteProject?,
    snapshots: List<DeploymentSnapshot>,
    isLoading: Boolean,
    onPublish: (subdomain: String?, customDomain: String?) -> Unit,
    onVerifyDomain: (String) -> Unit,
    onRollback: (DeploymentSnapshot) -> Unit
) {
    if (project == null) {
        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF090D16)), contentAlignment = Alignment.Center) {
            Text("No website selected", color = TextMuted)
        }
        return
    }

    var slug by remember { mutableStateOf(project.subdomain.replace(".sitegen.page", "")) }
    var customDomainInput by remember { mutableStateOf(project.customDomain ?: "") }
    val isPublished = project.status == ProjectStatus.PUBLISHED

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Publishing & Hosting", style = MaterialTheme.typography.displayMedium, color = TextPrimary)
                Text("Shared Cloudflare Workers & R2 edge distribution", style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isPublished) EmeraldSuccess.copy(alpha = 0.2f) else SlateSurfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isPublished) EmeraldSuccess else SlateBorder)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (isPublished) EmeraldSuccess else TextMuted)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        if (isPublished) "LIVE (v${project.deploymentVersion})" else "UNPUBLISHED DRAFT",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (isPublished) EmeraldSuccess else TextMuted
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SECTION 1: Free SITEGEN Subdomain
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Public, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Free SITEGEN Subdomain", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text("Your website is served over high-speed Cloudflare Edge with automated HTTPS SSL.", style = MaterialTheme.typography.bodySmall, color = TextMuted)

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = slug,
                        onValueChange = { slug = it.lowercase().replace(Regex("[^a-z0-9-]"), "") },
                        modifier = Modifier.weight(1f).testTag("subdomain_slug_input"),
                        shape = RoundedCornerShape(8.dp),
                        prefix = { Text("https://", color = TextMuted) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(".sitegen.page", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = TextSecondary)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val fullSub = "$slug.sitegen.page"
                        onPublish(fullSub, null)
                    },
                    modifier = Modifier.fillMaxWidth().height(46.dp).testTag("publish_subdomain_button"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Default.Publish, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isPublished) "Republish Changes to Edge" else "Publish Website Live Now", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SECTION 2: Custom Domain & DNS
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Domain, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Custom Domain Connection", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                    }
                    if (project.customDomainVerified) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = EmeraldSuccess.copy(alpha = 0.2f)
                        ) {
                            Text("SSL ACTIVE", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = EmeraldSuccess, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text("Connect your own registered domain (e.g. www.mycompany.com) with Cloudflare SSL.", style = MaterialTheme.typography.bodySmall, color = TextMuted)

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = customDomainInput,
                    onValueChange = { customDomainInput = it.lowercase().trim() },
                    placeholder = { Text("e.g. www.delhigrowth.com", color = TextMuted) },
                    modifier = Modifier.fillMaxWidth().testTag("custom_domain_input"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // DNS Instructions Table
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SlateSurfaceVariant),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("DNS Records to Configure in your Registrar (GoDaddy/Cloudflare/Namecheap):", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Type: CNAME", style = MaterialTheme.typography.labelSmall, color = CyanAccent)
                            Text("Name: @ or www", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                            Text("Target: cname.sitegen.page", style = MaterialTheme.typography.labelSmall, color = EmeraldSuccess)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (customDomainInput.isNotBlank()) {
                            onVerifyDomain(customDomainInput)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(42.dp).testTag("verify_custom_domain_button"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (project.customDomainVerified) EmeraldSuccess else IndigoPrimary),
                    enabled = customDomainInput.isNotBlank() && !isLoading
                ) {
                    Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (project.customDomainVerified) "Domain Verified & SSL Active" else "Verify DNS & Provision HTTPS")
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SECTION 3: Version Management & Rollback
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.History, contentDescription = null, tint = IndigoPrimary, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Deployment History & Version Rollback", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (snapshots.isEmpty()) {
                    Text("No past deployments recorded yet. Deployments are automatically saved every time you publish.", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                } else {
                    snapshots.forEach { snap ->
                        val dateFormatted = SimpleDateFormat("MMM d, yyyy HH:mm", Locale.getDefault()).format(Date(snap.publishedAt))
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = SlateSurfaceVariant),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Version v${snap.versionNumber} • $dateFormatted", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                                    Text(snap.summary, style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                }
                                OutlinedButton(
                                    onClick = { onRollback(snap) },
                                    shape = RoundedCornerShape(6.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                                    modifier = Modifier.height(32.dp)
                                ) {
                                    Icon(Icons.Default.Restore, contentDescription = "Rollback", modifier = Modifier.size(14.dp), tint = CyanAccent)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Rollback", style = MaterialTheme.typography.labelSmall, color = CyanAccent)
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // SECTION 4: Cloudflare Workers & R2 Architecture Info
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurfaceVariant),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Cloud, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cloudflare Infrastructure Architecture", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "SITEGEN uses a shared multi-tenant Cloudflare Workers routing gateway connected to an R2 object storage bucket (sitegen-published-sites). Every subdomain maps dynamically to the compiled HTML/CSS/JS bundles without creating individual Cloudflare Pages projects for each user.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        }
    }
}
