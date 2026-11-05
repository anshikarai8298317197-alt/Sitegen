package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ViewQuilt
import androidx.compose.material.icons.filled.Web
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
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

@Composable
fun AppNavigationDrawer(
    currentScreen: Screen,
    userProfile: UserProfile,
    creditBalance: Int,
    onNavigate: (Screen) -> Unit,
    onCloseDrawer: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(310.dp)
            .fillMaxHeight()
            .background(SlateSurface)
            .border(androidx.compose.foundation.BorderStroke(1.dp, SlateBorder))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // App Header in Drawer
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(IndigoPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text("S", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = "SITEGEN",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Text(
                    text = "One Prompt. Any Website.",
                    style = MaterialTheme.typography.labelSmall,
                    color = CyanAccent
                )
            }
        }

        // User / Plan Card
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = SlateSurfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(IndigoPrimary.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("A", color = CyanAccent, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(userProfile.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
                    Text(userProfile.email, style = MaterialTheme.typography.labelSmall, color = TextMuted)
                }
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = EmeraldSuccess.copy(alpha = 0.2f)
                ) {
                    Text(
                        "PRO",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = EmeraldSuccess,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        HorizontalDivider(color = SlateBorder, modifier = Modifier.padding(bottom = 12.dp))

        // SECTION: CORE APP
        DrawerSectionHeader("APPLICATION & BUILDER")
        DrawerItem("Dashboard", Icons.Default.Dashboard, currentScreen == Screen.DASHBOARD_OVERVIEW) {
            onNavigate(Screen.DASHBOARD_OVERVIEW); onCloseDrawer()
        }
        DrawerItem("My Websites", Icons.Default.Web, currentScreen == Screen.MY_WEBSITES) {
            onNavigate(Screen.MY_WEBSITES); onCloseDrawer()
        }
        DrawerItem("AI Website Generator", Icons.Default.AutoAwesome, currentScreen == Screen.CREATE_AI_WIZARD, badge = "10 Cr") {
            onNavigate(Screen.CREATE_AI_WIZARD); onCloseDrawer()
        }
        DrawerItem("Templates Catalog", Icons.Default.ViewQuilt, currentScreen == Screen.TEMPLATES_BROWSER) {
            onNavigate(Screen.TEMPLATES_BROWSER); onCloseDrawer()
        }
        DrawerItem("Visual Editor (Puck-style)", Icons.Default.Brush, currentScreen == Screen.VISUAL_BUILDER) {
            onNavigate(Screen.VISUAL_BUILDER); onCloseDrawer()
        }
        DrawerItem("Monaco Code Editor", Icons.Default.Code, currentScreen == Screen.CODE_EDITOR) {
            onNavigate(Screen.CODE_EDITOR); onCloseDrawer()
        }

        // SECTION: HOSTING & PUBLISHING
        Spacer(modifier = Modifier.height(8.dp))
        DrawerSectionHeader("PUBLISHING & CLOUD")
        DrawerItem("Publishing & Subdomains", Icons.Default.Publish, currentScreen == Screen.PUBLISH_HOSTING) {
            onNavigate(Screen.PUBLISH_HOSTING); onCloseDrawer()
        }
        DrawerItem("Custom Domains & SSL", Icons.Default.Domain, currentScreen == Screen.CUSTOM_DOMAINS) {
            onNavigate(Screen.CUSTOM_DOMAINS); onCloseDrawer()
        }

        // SECTION: BUSINESS & EXTENSIONS
        Spacer(modifier = Modifier.height(8.dp))
        DrawerSectionHeader("BUSINESS & LEADS")
        DrawerItem("Blog CMS", Icons.Default.Article, currentScreen == Screen.BLOG_CMS) {
            onNavigate(Screen.BLOG_CMS); onCloseDrawer()
        }
        DrawerItem("Forms & Captured Leads", Icons.Default.Email, currentScreen == Screen.FORMS_LEADS) {
            onNavigate(Screen.FORMS_LEADS); onCloseDrawer()
        }
        DrawerItem("SEO Optimization", Icons.Default.Search, currentScreen == Screen.SEO_TOOLS) {
            onNavigate(Screen.SEO_TOOLS); onCloseDrawer()
        }
        DrawerItem("Privacy Analytics", Icons.Default.Analytics, currentScreen == Screen.ANALYTICS) {
            onNavigate(Screen.ANALYTICS); onCloseDrawer()
        }
        DrawerItem("Ecommerce Storefront", Icons.Default.ShoppingBag, currentScreen == Screen.ECOMMERCE) {
            onNavigate(Screen.ECOMMERCE); onCloseDrawer()
        }

        // SECTION: BILLING & AI WALLET
        Spacer(modifier = Modifier.height(8.dp))
        DrawerSectionHeader("BILLING & CREDITS")
        DrawerItem("AI Credit Wallet", Icons.Default.AutoAwesome, currentScreen == Screen.AI_CREDITS, badge = "$creditBalance") {
            onNavigate(Screen.AI_CREDITS); onCloseDrawer()
        }
        DrawerItem("Plans & Cashfree Billing", Icons.Default.CreditCard, currentScreen == Screen.BILLING) {
            onNavigate(Screen.BILLING); onCloseDrawer()
        }

        // SECTION: ADMIN & SETTINGS
        Spacer(modifier = Modifier.height(8.dp))
        DrawerSectionHeader("SYSTEM & ADMIN")
        DrawerItem("Super Admin Dashboard", Icons.Default.AdminPanelSettings, currentScreen == Screen.ADMIN_PANEL, badge = "Admin") {
            onNavigate(Screen.ADMIN_PANEL); onCloseDrawer()
        }
        DrawerItem("Settings & Integrations", Icons.Default.Settings, currentScreen == Screen.SETTINGS_INTEGRATIONS) {
            onNavigate(Screen.SETTINGS_INTEGRATIONS); onCloseDrawer()
        }
        DrawerItem("Help & Documentation", Icons.Default.HelpOutline, currentScreen == Screen.HELP_SUPPORT) {
            onNavigate(Screen.HELP_SUPPORT); onCloseDrawer()
        }

        // SECTION: PUBLIC PAGES
        Spacer(modifier = Modifier.height(8.dp))
        DrawerSectionHeader("PUBLIC MARKETING")
        DrawerItem("Landing Home", Icons.Default.Public, currentScreen == Screen.PUBLIC_HOME) {
            onNavigate(Screen.PUBLIC_HOME); onCloseDrawer()
        }
        DrawerItem("Features Showcase", Icons.Default.Language, currentScreen == Screen.PUBLIC_FEATURES) {
            onNavigate(Screen.PUBLIC_FEATURES); onCloseDrawer()
        }
        DrawerItem("Public Pricing", Icons.Default.CreditCard, currentScreen == Screen.PUBLIC_PRICING) {
            onNavigate(Screen.PUBLIC_PRICING); onCloseDrawer()
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun DrawerSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
        color = TextMuted,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
    )
}

@Composable
private fun DrawerItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    badge: String? = null,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) IndigoPrimary.copy(alpha = 0.2f) else Color.Transparent,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clickable { onClick() }
            .testTag("drawer_item_${title.take(8)}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                icon,
                contentDescription = title,
                tint = if (isSelected) CyanAccent else TextSecondary,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                ),
                color = if (isSelected) TextPrimary else TextSecondary,
                modifier = Modifier.weight(1f)
            )
            if (badge != null) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (isSelected) IndigoPrimary else SlateSurfaceVariant
                ) {
                    Text(
                        badge,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
