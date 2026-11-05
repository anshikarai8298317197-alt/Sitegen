package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AppNavigationDrawer
import com.example.ui.components.CashfreeCheckoutDialog
import com.example.ui.components.SiteGenTopAppBar
import com.example.ui.screens.AiCreditsScreen
import com.example.ui.screens.AiGeneratorScreen
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.BlogCmsScreen
import com.example.ui.screens.CodeEditorScreen
import com.example.ui.screens.DashboardOverviewScreen
import com.example.ui.screens.EcommerceScreen
import com.example.ui.screens.FormsLeadsScreen
import com.example.ui.screens.HelpSupportScreen
import com.example.ui.screens.LegalPolicyScreen
import com.example.ui.screens.MyWebsitesScreen
import com.example.ui.screens.PublicContactScreen
import com.example.ui.screens.PublicHomeScreen
import com.example.ui.screens.PublicPricingScreen
import com.example.ui.screens.PublishingScreen
import com.example.ui.screens.SuperAdminScreen
import com.example.ui.screens.TemplatesBrowserScreen
import com.example.ui.screens.VisualEditorScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.Screen
import com.example.ui.viewmodel.SiteGenViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SiteGenApp()
            }
        }
    }
}

@Composable
fun SiteGenApp(viewModel: SiteGenViewModel = viewModel()) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val projects by viewModel.projects.collectAsState()
    val selectedProject by viewModel.selectedProject.collectAsState()
    val creditWallet by viewModel.creditWallet.collectAsState()
    val creditTransactions by viewModel.creditTransactions.collectAsState()
    val userProfile by viewModel.userProfile.collectAsState()
    val blogPosts by viewModel.blogPosts.collectAsState()
    val formSubmissions by viewModel.formSubmissions.collectAsState()
    val ecommerceProducts by viewModel.ecommerceProducts.collectAsState()
    val deploymentSnapshots by viewModel.deploymentSnapshots.collectAsState()
    val deviceMode by viewModel.deviceMode.collectAsState()
    val activeCashfreeOrder by viewModel.activeCashfreeOrder.collectAsState()
    val toastMessage by viewModel.toastMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val systemSettings by viewModel.systemSettings.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Intercept back presses
    BackHandler(enabled = currentScreen != Screen.DASHBOARD_OVERVIEW) {
        if (drawerState.isOpen) {
            coroutineScope.launch { drawerState.close() }
        } else {
            val handled = viewModel.handleBack()
            if (!handled && currentScreen != Screen.DASHBOARD_OVERVIEW) {
                viewModel.navigateTo(Screen.DASHBOARD_OVERVIEW)
            }
        }
    }

    // Display SnackBar on feedback messages
    LaunchedEffect(toastMessage) {
        toastMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg)
            viewModel.clearToast()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppNavigationDrawer(
                currentScreen = currentScreen,
                userProfile = userProfile,
                creditBalance = creditWallet.balance,
                onNavigate = { screen -> viewModel.navigateTo(screen) },
                onCloseDrawer = { coroutineScope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                SiteGenTopAppBar(
                    currentScreen = currentScreen,
                    creditBalance = creditWallet.balance,
                    onMenuClick = { coroutineScope.launch { drawerState.open() } },
                    onBackClick = { viewModel.handleBack() },
                    onCreditClick = { viewModel.navigateTo(Screen.AI_CREDITS) },
                    onAiWizardClick = { viewModel.navigateTo(Screen.CREATE_AI_WIZARD) }
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFF090D16))
            ) {
                when (currentScreen) {
                    Screen.PUBLIC_HOME, Screen.PUBLIC_FEATURES -> {
                        PublicHomeScreen(
                            onStartPrompt = { prompt ->
                                viewModel.navigateTo(Screen.CREATE_AI_WIZARD)
                            },
                            onNavigate = { viewModel.navigateTo(it) },
                            onUseTemplate = { tpl ->
                                viewModel.useTemplate(tpl)
                            }
                        )
                    }
                    Screen.PUBLIC_PRICING, Screen.BILLING -> {
                        PublicPricingScreen(
                            plans = viewModel.plans,
                            onSelectPlan = { plan, isYearly ->
                                viewModel.startCashfreePlanUpgrade(plan, isYearly)
                            },
                            onBuyCredits = { credits, priceInr ->
                                viewModel.startCashfreeCreditPurchase(credits, priceInr)
                            }
                        )
                    }
                    Screen.PUBLIC_ABOUT, Screen.PUBLIC_CONTACT -> {
                        PublicContactScreen(
                            onSubmitLead = { name, email, phone, msg ->
                                viewModel.recordLeadFromSite(name, email, phone, msg)
                            }
                        )
                    }
                    Screen.PUBLIC_PRIVACY -> {
                        LegalPolicyScreen(
                            title = "Privacy Policy",
                            content = "At SITEGEN, we respect your privacy. All websites generated on our platform belong entirely to you. We do not sell or monetize customer data. Published websites are served through Cloudflare edge nodes with strict SSL encryption. Server-side secrets including Gemini API keys and Cashfree credentials are never exposed to browser runtimes."
                        )
                    }
                    Screen.PUBLIC_TERMS -> {
                        LegalPolicyScreen(
                            title = "Terms of Service",
                            content = "By using SITEGEN, you agree to our acceptable use guidelines. You retain 100% intellectual property ownership of any website design, copy, or code generated. Websites hosted on the SITEGEN shared Cloudflare network must comply with standard telecommunications and internet safety regulations."
                        )
                    }
                    Screen.PUBLIC_REFUND -> {
                        LegalPolicyScreen(
                            title = "Refund Policy",
                            content = "We offer a 7-day money-back guarantee on all paid plans processed via Cashfree Payments if you are unsatisfied with the website builder capabilities. Unused AI credits from one-time packs are eligible for a full refund within 14 days of purchase upon contacting support."
                        )
                    }
                    Screen.PUBLIC_AUTH -> {
                        PublicHomeScreen(
                            onStartPrompt = { viewModel.navigateTo(Screen.CREATE_AI_WIZARD) },
                            onNavigate = { viewModel.navigateTo(it) },
                            onUseTemplate = { viewModel.useTemplate(it) }
                        )
                    }
                    Screen.DASHBOARD_OVERVIEW -> {
                        DashboardOverviewScreen(
                            userProfile = userProfile,
                            projects = projects,
                            creditWallet = creditWallet,
                            leads = formSubmissions,
                            onSelectProject = { viewModel.selectProject(it) },
                            onNavigate = { viewModel.navigateTo(it) }
                        )
                    }
                    Screen.MY_WEBSITES -> {
                        MyWebsitesScreen(
                            projects = projects,
                            onSelectProject = { viewModel.selectProject(it) },
                            onDuplicateProject = { viewModel.duplicateProject(it) },
                            onDeleteProject = { viewModel.deleteProject(it) },
                            onNavigate = { viewModel.navigateTo(it) }
                        )
                    }
                    Screen.CREATE_AI_WIZARD -> {
                        AiGeneratorScreen(
                            creditBalance = creditWallet.balance,
                            onGenerate = { params ->
                                viewModel.generateWebsite(params)
                            }
                        )
                    }
                    Screen.TEMPLATES_BROWSER, Screen.PUBLIC_TEMPLATES -> {
                        TemplatesBrowserScreen(
                            onUseTemplate = { tpl ->
                                viewModel.useTemplate(tpl)
                            }
                        )
                    }
                    Screen.VISUAL_BUILDER, Screen.SEO_TOOLS -> {
                        VisualEditorScreen(
                            project = selectedProject,
                            deviceMode = deviceMode,
                            onDeviceModeChange = { viewModel.setDeviceMode(it) },
                            onUpdateSection = { viewModel.updateSection(it) },
                            onAddSection = { viewModel.addSection(it) },
                            onDeleteSection = { viewModel.deleteSection(it) },
                            onReorderSection = { from, to -> viewModel.reorderSection(from, to) },
                            onUpdateProjectDetails = { n, c, d, p, s, t -> viewModel.updateProjectDetails(n, c, d, p, s, t) },
                            onUpdateSeo = { t, d, k -> viewModel.updateSeo(t, d, k) },
                            onApplyAiPrompt = { viewModel.applyAiEdit(it) },
                            onUndo = { viewModel.undo() },
                            onRedo = { viewModel.redo() },
                            onNavigate = { viewModel.navigateTo(it) },
                            onLeadCaptured = { name, email, phone, msg ->
                                viewModel.recordLeadFromSite(name, email, phone, msg)
                            }
                        )
                    }
                    Screen.CODE_EDITOR -> {
                        CodeEditorScreen(
                            project = selectedProject,
                            onSaveCustomCode = { css, js ->
                                viewModel.saveCustomCode(css, js)
                            },
                            onShowToast = { viewModel.showToast(it) }
                        )
                    }
                    Screen.PUBLISH_HOSTING, Screen.CUSTOM_DOMAINS -> {
                        PublishingScreen(
                            project = selectedProject,
                            snapshots = deploymentSnapshots,
                            isLoading = isLoading,
                            onPublish = { sub, cust ->
                                viewModel.publishProject(sub, cust)
                            },
                            onVerifyDomain = { domain ->
                                viewModel.verifyDomain(domain)
                            },
                            onRollback = { snap ->
                                viewModel.rollbackVersion(snap)
                            }
                        )
                    }
                    Screen.BLOG_CMS -> {
                        BlogCmsScreen(
                            project = selectedProject,
                            posts = blogPosts,
                            onAddPost = { t, e, c, cat, tags ->
                                viewModel.addBlogPost(t, e, c, cat, tags)
                            },
                            onDeletePost = { viewModel.deleteBlogPost(it) }
                        )
                    }
                    Screen.FORMS_LEADS -> {
                        FormsLeadsScreen(
                            leads = formSubmissions,
                            onUpdateStatus = { id, st -> viewModel.updateLeadStatus(id, st) },
                            onDeleteLead = { viewModel.deleteBlogPost(it) },
                            onShowToast = { viewModel.showToast(it) }
                        )
                    }
                    Screen.ANALYTICS -> {
                        val analytics = viewModel.getAnalyticsData()
                        AnalyticsScreen(analytics = analytics)
                    }
                    Screen.ECOMMERCE -> {
                        EcommerceScreen(
                            project = selectedProject,
                            products = ecommerceProducts,
                            onAddProduct = { n, p, c, d -> viewModel.addProduct(n, p, c, d) },
                            onDeleteProduct = { viewModel.deleteProduct(it) }
                        )
                    }
                    Screen.AI_CREDITS -> {
                        AiCreditsScreen(
                            wallet = creditWallet,
                            transactions = creditTransactions,
                            onBuyCredits = { credits, priceInr ->
                                viewModel.startCashfreeCreditPurchase(credits, priceInr)
                            }
                        )
                    }
                    Screen.ADMIN_PANEL, Screen.SETTINGS_INTEGRATIONS -> {
                        SuperAdminScreen(
                            projects = projects,
                            settings = systemSettings,
                            onSaveSettings = { viewModel.saveSettings(it) }
                        )
                    }
                    Screen.HELP_SUPPORT -> {
                        HelpSupportScreen()
                    }
                }

                // Cashfree Hosted Checkout Modal
                activeCashfreeOrder?.let { order ->
                    CashfreeCheckoutDialog(
                        order = order,
                        onSuccess = { viewModel.completeCashfreePayment(simulateFailure = false) },
                        onDismiss = { viewModel.dismissCashfreeModal() }
                    )
                }
            }
        }
    }
}
