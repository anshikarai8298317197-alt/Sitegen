package com.example.ui.components

import android.annotation.SuppressLint
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Publish
import androidx.compose.material.icons.filled.Tablet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import com.example.data.model.CashfreePaymentOrder
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SiteGenTopAppBar(
    currentScreen: Screen,
    creditBalance: Int,
    onMenuClick: () -> Unit,
    onBackClick: () -> Unit,
    onCreditClick: () -> Unit,
    onAiWizardClick: () -> Unit,
    canGoBack: Boolean = true
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(IndigoPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("S", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "SITEGEN",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        ),
                        color = TextPrimary
                    )
                    Text(
                        text = "One Prompt. Any Website.",
                        style = MaterialTheme.typography.labelSmall,
                        color = CyanAccent
                    )
                }
            }
        },
        navigationIcon = {
            if (canGoBack && currentScreen != Screen.DASHBOARD_OVERVIEW && currentScreen != Screen.PUBLIC_HOME) {
                IconButton(onClick = onBackClick, modifier = Modifier.testTag("nav_back_button")) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            } else {
                IconButton(onClick = onMenuClick, modifier = Modifier.testTag("nav_drawer_button")) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextPrimary)
                }
            }
        },
        actions = {
            // AI Credits Badge
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = SlateSurfaceVariant,
                border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { onCreditClick() }
                    .testTag("credit_badge")
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Icon(
                        Icons.Default.AutoAwesome,
                        contentDescription = "Credits",
                        tint = CyanAccent,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$creditBalance",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                }
            }

            // Quick AI Create Button
            IconButton(
                onClick = onAiWizardClick,
                modifier = Modifier
                    .padding(end = 4.dp)
                    .clip(CircleShape)
                    .background(IndigoPrimary)
                    .size(36.dp)
                    .testTag("quick_ai_create_button")
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "New AI Website",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SlateSurface
        )
    )
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SandboxedWebsiteWebView(
    htmlContent: String,
    deviceMode: DeviceMode,
    modifier: Modifier = Modifier,
    onLeadCaptured: ((name: String, email: String, phone: String, msg: String) -> Unit)? = null
) {
    val targetWidth = when (deviceMode) {
        DeviceMode.DESKTOP -> Modifier.fillMaxWidth()
        DeviceMode.TABLET -> Modifier.widthIn(max = 768.dp).fillMaxWidth(0.92f)
        DeviceMode.MOBILE -> Modifier.widthIn(max = 380.dp).fillMaxWidth(0.88f)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF070B14))
            .padding(8.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = targetWidth
                .fillMaxSize()
                .border(1.dp, SlateBorder, RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            AndroidView(
                factory = { ctx ->
                    WebView(ctx).apply {
                        settings.apply {
                            javaScriptEnabled = true
                            domStorageEnabled = true
                            useWideViewPort = true
                            loadWithOverviewMode = true
                            setSupportZoom(true)
                            builtInZoomControls = true
                            displayZoomControls = false
                        }
                        webChromeClient = WebChromeClient()
                        webViewClient = object : WebViewClient() {}

                        // Add JS Bridge to catch form leads
                        addJavascriptInterface(object {
                            @JavascriptInterface
                            fun submitLead(projectId: String, name: String, email: String, phone: String, message: String) {
                                onLeadCaptured?.invoke(name, email, phone, message)
                            }
                        }, "SiteGenApp")

                        loadDataWithBaseURL("https://sitegen.page", htmlContent, "text/html", "UTF-8", null)
                    }
                },
                update = { webView ->
                    webView.loadDataWithBaseURL("https://sitegen.page", htmlContent, "text/html", "UTF-8", null)
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun ViewportSwitcherBar(
    currentMode: DeviceMode,
    onModeChange: (DeviceMode) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(SlateSurfaceVariant)
            .border(1.dp, SlateBorder, RoundedCornerShape(24.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        IconButton(
            onClick = { onModeChange(DeviceMode.DESKTOP) },
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (currentMode == DeviceMode.DESKTOP) IndigoPrimary else Color.Transparent)
                .testTag("viewport_desktop")
        ) {
            Icon(
                Icons.Default.Devices,
                contentDescription = "Desktop View",
                tint = if (currentMode == DeviceMode.DESKTOP) Color.White else TextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
        IconButton(
            onClick = { onModeChange(DeviceMode.TABLET) },
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (currentMode == DeviceMode.TABLET) IndigoPrimary else Color.Transparent)
                .testTag("viewport_tablet")
        ) {
            Icon(
                Icons.Default.Tablet,
                contentDescription = "Tablet View",
                tint = if (currentMode == DeviceMode.TABLET) Color.White else TextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
        IconButton(
            onClick = { onModeChange(DeviceMode.MOBILE) },
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (currentMode == DeviceMode.MOBILE) IndigoPrimary else Color.Transparent)
                .testTag("viewport_mobile")
        ) {
            Icon(
                Icons.Default.PhoneAndroid,
                contentDescription = "Mobile View",
                tint = if (currentMode == DeviceMode.MOBILE) Color.White else TextMuted,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun CashfreeCheckoutDialog(
    order: CashfreePaymentOrder,
    onSuccess: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedMethod by remember { mutableStateOf("UPI") }
    var isSimulating by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { if (!isSimulating) onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = SlateSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, SlateBorder)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldSuccess)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "Cashfree Payments",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                        }
                        Text(
                            "Secure Hosted Checkout • Sandbox",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }
                    IconButton(onClick = onDismiss, enabled = !isSimulating) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = SlateBorder)

                // Order Summary
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SlateSurfaceVariant),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Order ID", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Text(order.orderId.take(18) + "...", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Purpose", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Text(order.purpose, style = MaterialTheme.typography.labelSmall, color = CyanAccent)
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Total Payable", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                            Text(
                                "₹${order.amount.toInt()}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldSuccess
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Select Payment Method:", style = MaterialTheme.typography.labelMedium, color = TextSecondary)
                Spacer(modifier = Modifier.height(8.dp))

                // Payment Options
                val options = listOf(
                    "UPI" to "GPay, PhonePe, Paytm, BHIM",
                    "Card" to "Credit / Debit Cards (Visa, MC, RuPay)",
                    "Netbanking" to "HDFC, ICICI, SBI, Axis & 50+ Banks"
                )

                options.forEach { (name, sub) ->
                    val isSelected = selectedMethod == name
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { selectedMethod = name }
                            .testTag("pay_opt_$name"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) IndigoPrimary.copy(alpha = 0.15f) else SlateSurfaceVariant
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) IndigoPrimary else SlateBorder
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, if (isSelected) IndigoPrimary else TextMuted, CircleShape)
                                    .background(if (isSelected) IndigoPrimary else Color.Transparent)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold), color = TextPrimary)
                                Text(sub, style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Pay Action Button
                if (isSimulating) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = IndigoPrimary, strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Verifying with Cashfree server APIs...", style = MaterialTheme.typography.bodySmall, color = CyanAccent)
                    }
                } else {
                    Button(
                        onClick = {
                            isSimulating = true
                            // Simulate fast server-side verification
                            onSuccess()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("cashfree_pay_now_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess)
                    ) {
                        Text(
                            "Pay ₹${order.amount.toInt()} Securely",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AiAssistantDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onApplyPrompt: (String) -> Unit
) {
    if (!isOpen) return

    var promptText by remember { mutableStateOf("") }
    val quickPrompts = listOf(
        "Change color palette to dark emerald & cyan",
        "Rewrite hero headline for tech B2B startup",
        "Add a 3-tier pricing section with popular badge",
        "Add a 4-item FAQ section",
        "Optimize SEO title and meta description"
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = "AI", tint = CyanAccent)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "AI Editing Assistant",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Text(
                    "Tell the AI how to enhance this website. Costs 2 AI Credits.",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = promptText,
                    onValueChange = { promptText = it },
                    placeholder = { Text("e.g. Rewrite the hero section for an Indian SaaS brand...", color = TextMuted) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .testTag("ai_assistant_input"),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Quick Suggestions:", style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    quickPrompts.take(3).forEach { qp ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = SlateSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { promptText = qp }
                        ) {
                            Text(
                                "✨ $qp",
                                style = MaterialTheme.typography.labelSmall,
                                color = CyanAccent,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = {
                        if (promptText.isNotBlank()) {
                            onApplyPrompt(promptText)
                            onDismiss()
                        }
                    },
                    enabled = promptText.isNotBlank(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("apply_ai_edit_button"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary)
                ) {
                    Text("Apply AI Changes (2 Credits)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
