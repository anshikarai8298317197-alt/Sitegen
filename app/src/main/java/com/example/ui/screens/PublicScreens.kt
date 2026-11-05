package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.generator.TemplatesCatalog
import com.example.data.model.SubscriptionPlan
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

@Composable
fun PublicHomeScreen(
    onStartPrompt: (String) -> Unit,
    onNavigate: (Screen) -> Unit,
    onUseTemplate: (WebsiteProject) -> Unit
) {
    var homePrompt by remember { mutableStateOf("Create a modern website for a Delhi-based digital marketing agency") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 32.dp)
    ) {
        // Hero Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.sitegen_hero),
                contentDescription = "SITEGEN Hero",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Brush.verticalGradient(
                            listOf(Color.Transparent, Color(0xFF090D16))
                        )
                    )
            )
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            // Tagline & Badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = IndigoPrimary.copy(alpha = 0.2f),
                border = androidx.compose.foundation.BorderStroke(1.dp, IndigoPrimary.copy(alpha = 0.5f)),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("AI Website Builder SaaS Platform", style = MaterialTheme.typography.labelSmall, color = CyanAccent)
                }
            }

            Text(
                text = "One Prompt.\nAny Website.",
                style = MaterialTheme.typography.displayLarge.copy(lineHeight = 38.sp),
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Generate, visually edit, customize with code, and publish production-grade websites in seconds with Gemini AI.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Interactive Prompt Bar
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SlateSurface),
                border = androidx.compose.foundation.BorderStroke(1.dp, IndigoPrimary)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    OutlinedTextField(
                        value = homePrompt,
                        onValueChange = { homePrompt = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("home_prompt_input"),
                        shape = RoundedCornerShape(8.dp),
                        placeholder = { Text("Describe the website you want to generate...", color = TextMuted) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { onStartPrompt(homePrompt) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("home_generate_button"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate Website with AI", fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Quick Stats Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(SlateSurface)
                    .border(1.dp, SlateBorder, RoundedCornerShape(12.dp))
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("100K+", "Sites Built")
                StatItem("< 30s", "Gen Speed")
                StatItem("99.99%", "Uptime")
                StatItem("Cashfree", "Payments")
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Feature Highlights
            Text(
                text = "Why Modern Teams Choose SITEGEN",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(14.dp))

            FeatureCard("Prompt-to-Website Engine", "Transform plain English business descriptions into responsive multi-page websites with SEO, forms, and custom color tokens.", Icons.Default.AutoAwesome)
            FeatureCard("Visual Puck-Style Canvas", "Drag, drop, add, remove, and restyle sections with instant live preview in Desktop, Tablet, and Mobile frames.", Icons.Default.RocketLaunch)
            FeatureCard("Monaco Code Editor", "Edit HTML, CSS, JavaScript, and JSON with syntax highlighting in an isolated security sandbox.", Icons.Default.Speed)
            FeatureCard("Cloudflare Hosting & Domains", "Publish instantly to free .sitegen.page subdomains or connect custom domains with automated SSL certificates.", Icons.Default.Language)
            FeatureCard("Cashfree Payments Architecture", "Official Indian payments integration with UPI, Cards, Netbanking, sandbox testing, and server-side verification.", Icons.Default.CreditCard)

            Spacer(modifier = Modifier.height(32.dp))

            // Templates Teaser
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Starter Templates",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
                Text(
                    text = "View All →",
                    style = MaterialTheme.typography.labelLarge,
                    color = CyanAccent,
                    modifier = Modifier.clickable { onNavigate(Screen.TEMPLATES_BROWSER) }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            TemplatesCatalog.templates.take(3).forEach { tpl ->
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
                            Text(tpl.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = IndigoPrimary.copy(alpha = 0.2f)
                            ) {
                                Text(tpl.category, style = MaterialTheme.typography.labelSmall, color = CyanAccent, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(tpl.description, style = MaterialTheme.typography.bodySmall, color = TextMuted)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = { onUseTemplate(tpl) },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                                modifier = Modifier.testTag("use_tpl_${tpl.id.take(8)}")
                            ) {
                                Text("Use Template", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer Links
            HorizontalDivider(color = SlateBorder)
            Spacer(modifier = Modifier.height(16.dp))

            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FooterLink("Features") { onNavigate(Screen.PUBLIC_FEATURES) }
                FooterLink("Pricing") { onNavigate(Screen.PUBLIC_PRICING) }
                FooterLink("Templates") { onNavigate(Screen.TEMPLATES_BROWSER) }
                FooterLink("About") { onNavigate(Screen.PUBLIC_ABOUT) }
                FooterLink("Contact") { onNavigate(Screen.PUBLIC_CONTACT) }
                FooterLink("Privacy Policy") { onNavigate(Screen.PUBLIC_PRIVACY) }
                FooterLink("Terms") { onNavigate(Screen.PUBLIC_TERMS) }
                FooterLink("Refund Policy") { onNavigate(Screen.PUBLIC_REFUND) }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "© 2026 SITEGEN Inc. All rights reserved. • One Prompt. Any Website.",
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
    }
}

@Composable
private fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = CyanAccent)
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
    }
}

@Composable
private fun FeatureCard(title: String, desc: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(IndigoPrimary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = CyanAccent, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
                Spacer(modifier = Modifier.height(4.dp))
                Text(desc, style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
        }
    }
}

@Composable
private fun FooterLink(text: String, onClick: () -> Unit) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium,
        color = TextSecondary,
        modifier = Modifier.clickable { onClick() }
    )
}

@Composable
fun PublicPricingScreen(
    plans: List<SubscriptionPlan>,
    onSelectPlan: (SubscriptionPlan, Boolean) -> Unit,
    onBuyCredits: (Int, Double) -> Unit
) {
    var isYearly by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(
            text = "Flexible, Transparent Pricing",
            style = MaterialTheme.typography.displayMedium,
            color = TextPrimary
        )
        Text(
            text = "Start free. Upgrade as your websites and client portfolio scale.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Monthly / Yearly Switch
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(SlateSurface)
                .border(1.dp, SlateBorder, RoundedCornerShape(20.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text(
                "Monthly",
                style = MaterialTheme.typography.labelMedium,
                color = if (!isYearly) TextPrimary else TextMuted
            )
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = isYearly,
                onCheckedChange = { isYearly = it },
                colors = SwitchDefaults.colors(checkedThumbColor = IndigoPrimary)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Yearly (Save 30%)",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = if (isYearly) EmeraldSuccess else TextMuted
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Plans
        plans.forEach { plan ->
            val price = if (isYearly) plan.priceYearlyINR else plan.priceMonthlyINR
            val period = if (isYearly) "/yr" else "/mo"
            val isHighlighted = plan.isPopular

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isHighlighted) SlateSurfaceVariant else SlateSurface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    if (isHighlighted) 2.dp else 1.dp,
                    if (isHighlighted) IndigoPrimary else SlateBorder
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(plan.name, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                        if (isHighlighted) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = IndigoPrimary
                            ) {
                                Text("MOST POPULAR", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(plan.tagline, style = MaterialTheme.typography.bodySmall, color = TextMuted)

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = if (price == 0) "Free" else "₹$price",
                            style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        if (price > 0) {
                            Text(period, style = MaterialTheme.typography.bodyMedium, color = TextMuted, modifier = Modifier.padding(bottom = 6.dp, start = 4.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    PlanFeatureItem("${plan.websitesLimit} Active Websites")
                    PlanFeatureItem("${plan.aiCreditsPerMonth} Monthly AI Credits")
                    PlanFeatureItem("Free SITEGEN Subdomain", enabled = plan.subdomainAllowed)
                    PlanFeatureItem("Custom Domain + Automated SSL", enabled = plan.customDomainAllowed)
                    PlanFeatureItem("Remove SITEGEN Branding", enabled = plan.removeBranding)
                    PlanFeatureItem("HTML / CSS / JS Code Export", enabled = plan.codeExportAllowed)
                    PlanFeatureItem("Ecommerce Storefront Module", enabled = plan.ecommerceAllowed)

                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = { onSelectPlan(plan, isYearly) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("plan_select_${plan.id}"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isHighlighted) IndigoPrimary else SlateSurfaceVariant
                        ),
                        border = if (!isHighlighted) androidx.compose.foundation.BorderStroke(1.dp, SlateBorder) else null
                    ) {
                        Text(
                            text = if (plan.id == "plan_free") "Current Free Tier" else "Subscribe with Cashfree",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // AI Credit Packs
        Text(
            text = "One-Time AI Credit Packs",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = TextPrimary
        )
        Text(
            text = "Need extra generation capacity without changing your plan?",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CreditPackCard("50 Credits", "₹299", Modifier.weight(1f)) { onBuyCredits(50, 299.0) }
            CreditPackCard("150 Credits", "₹699", Modifier.weight(1f), badge = "Popular") { onBuyCredits(150, 699.0) }
            CreditPackCard("500 Credits", "₹1,999", Modifier.weight(1f)) { onBuyCredits(500, 1999.0) }
        }
    }
}

@Composable
private fun PlanFeatureItem(text: String, enabled: Boolean = true) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 3.dp)
    ) {
        Icon(
            if (enabled) Icons.Default.Check else Icons.Default.Close,
            contentDescription = null,
            tint = if (enabled) EmeraldSuccess else TextMuted,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = if (enabled) TextSecondary else TextMuted
        )
    }
}

@Composable
fun CreditPackCard(title: String, price: String, modifier: Modifier = Modifier, badge: String? = null, onBuy: () -> Unit) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SlateSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (badge != null) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = CyanAccent.copy(alpha = 0.2f),
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(badge, style = MaterialTheme.typography.labelSmall, color = CyanAccent, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                }
            }
            Text(title, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
            Text(price, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = EmeraldSuccess)
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onBuy,
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                modifier = Modifier.fillMaxWidth().height(32.dp)
            ) {
                Text("Buy", style = MaterialTheme.typography.labelSmall)
            }
        }
    }
}

@Composable
fun PublicContactScreen(
    onSubmitLead: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isSubmitted by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text("Contact Support & Enterprise", style = MaterialTheme.typography.displayMedium, color = TextPrimary)
        Text("Have questions about SITEGEN, custom deployment, or enterprise agreements? We are here to help.", style = MaterialTheme.typography.bodyMedium, color = TextMuted)

        Spacer(modifier = Modifier.height(24.dp))

        if (isSubmitted) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = EmeraldSuccess.copy(alpha = 0.15f)),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess)
            ) {
                Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("✓ Thank You!", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold), color = EmeraldSuccess)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("Your inquiry has been logged into our support ticketing and lead system. A technical account lead will reach out within 24 hours.", style = MaterialTheme.typography.bodySmall, color = TextPrimary, textAlign = TextAlign.Center)
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
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Your Name *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Work Email *") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Message / Project Requirements *") },
                        modifier = Modifier.fillMaxWidth().height(120.dp)
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank() && email.isNotBlank()) {
                                onSubmitLead(name, email, phone, message)
                                isSubmitted = true
                            }
                        },
                        enabled = name.isNotBlank() && email.isNotBlank(),
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                    ) {
                        Text("Send Inquiry", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun LegalPolicyScreen(title: String, content: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF090D16))
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Text(title, style = MaterialTheme.typography.displayMedium, color = TextPrimary)
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp),
                color = TextSecondary,
                modifier = Modifier.padding(20.dp)
            )
        }
    }
}
