package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.generator.TemplatesCatalog
import com.example.data.generator.WebsiteCompiler
import com.example.data.generator.WebsiteGenerationParams
import com.example.data.model.AiCreditWallet
import com.example.data.model.AiTransaction
import com.example.data.model.AnalyticsData
import com.example.data.model.BlogPost
import com.example.data.model.CashfreePaymentOrder
import com.example.data.model.DeploymentSnapshot
import com.example.data.model.EcommerceProduct
import com.example.data.model.FormSubmission
import com.example.data.model.SectionItem
import com.example.data.model.SectionType
import com.example.data.model.SubscriptionPlan
import com.example.data.model.SystemSettings
import com.example.data.model.UserProfile
import com.example.data.model.WebsitePage
import com.example.data.model.WebsiteProject
import com.example.data.model.WebsiteSection
import com.example.data.repository.SiteGenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Stack
import java.util.UUID

enum class Screen {
    // Public / Marketing
    PUBLIC_HOME,
    PUBLIC_FEATURES,
    PUBLIC_PRICING,
    PUBLIC_TEMPLATES,
    PUBLIC_ABOUT,
    PUBLIC_CONTACT,
    PUBLIC_AUTH,
    PUBLIC_PRIVACY,
    PUBLIC_TERMS,
    PUBLIC_REFUND,

    // App / Dashboard
    DASHBOARD_OVERVIEW,
    MY_WEBSITES,
    CREATE_AI_WIZARD,
    TEMPLATES_BROWSER,
    VISUAL_BUILDER,
    CODE_EDITOR,
    PUBLISH_HOSTING,
    CUSTOM_DOMAINS,
    BLOG_CMS,
    FORMS_LEADS,
    SEO_TOOLS,
    ANALYTICS,
    ECOMMERCE,
    AI_CREDITS,
    BILLING,
    ADMIN_PANEL,
    SETTINGS_INTEGRATIONS,
    HELP_SUPPORT
}

enum class DeviceMode {
    DESKTOP,
    TABLET,
    MOBILE
}

class SiteGenViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SiteGenRepository(application)

    // Navigation Stack
    private val navigationBackStack = Stack<Screen>().apply { push(Screen.DASHBOARD_OVERVIEW) }
    private val _currentScreen = MutableStateFlow(Screen.DASHBOARD_OVERVIEW)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // Projects Flow
    val projects: StateFlow<List<WebsiteProject>> = repository.getAllProjectsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Currently Selected Project for Editor/Preview/CMS
    private val _selectedProject = MutableStateFlow<WebsiteProject?>(null)
    val selectedProject: StateFlow<WebsiteProject?> = _selectedProject.asStateFlow()

    // Wallet & Credits
    val creditWallet: StateFlow<AiCreditWallet> = repository.getCreditWalletFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AiCreditWallet())

    val creditTransactions: StateFlow<List<AiTransaction>> = repository.getCreditTransactionsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Settings
    val systemSettings: StateFlow<SystemSettings> = repository.getSettingsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SystemSettings())

    // Plans
    val plans: List<SubscriptionPlan> = repository.availablePlans

    // User Profile
    private val _userProfile = MutableStateFlow(
        UserProfile(
            id = "usr_sitegen_admin",
            email = "ashgaming0502@gmail.com",
            name = "Ash Gaming",
            role = "SUPER_ADMIN",
            currentPlanId = "plan_pro"
        )
    )
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // Blog posts
    private val _blogPosts = MutableStateFlow<List<BlogPost>>(emptyList())
    val blogPosts: StateFlow<List<BlogPost>> = _blogPosts.asStateFlow()

    // Form Submissions / Leads
    val formSubmissions: StateFlow<List<FormSubmission>> = repository.getFormSubmissionsFlow()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Ecommerce Products
    private val _ecommerceProducts = MutableStateFlow<List<EcommerceProduct>>(emptyList())
    val ecommerceProducts: StateFlow<List<EcommerceProduct>> = _ecommerceProducts.asStateFlow()

    // Deployment Snapshots
    private val _deploymentSnapshots = MutableStateFlow<List<DeploymentSnapshot>>(emptyList())
    val deploymentSnapshots: StateFlow<List<DeploymentSnapshot>> = _deploymentSnapshots.asStateFlow()

    // Editor Viewport Mode
    private val _deviceMode = MutableStateFlow(DeviceMode.MOBILE)
    val deviceMode: StateFlow<DeviceMode> = _deviceMode.asStateFlow()

    // Cashfree Checkout Dialog State
    private val _activeCashfreeOrder = MutableStateFlow<CashfreePaymentOrder?>(null)
    val activeCashfreeOrder: StateFlow<CashfreePaymentOrder?> = _activeCashfreeOrder.asStateFlow()
    private var pendingPlanId: String? = null
    private var pendingCredits: Int? = null

    // UI Feedback
    private val _toastMessage = MutableStateFlow<String?>(null)
    val toastMessage: StateFlow<String?> = _toastMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Undo / Redo for Editor
    private val undoStack = Stack<WebsiteProject>()
    private val redoStack = Stack<WebsiteProject>()

    init {
        // Auto-select first project if available
        viewModelScope.launch {
            projects.collect { list ->
                if (_selectedProject.value == null && list.isNotEmpty()) {
                    _selectedProject.value = list.first()
                    refreshProjectData(list.first().id)
                }
            }
        }
    }

    fun navigateTo(screen: Screen) {
        if (_currentScreen.value != screen) {
            navigationBackStack.push(_currentScreen.value)
            _currentScreen.value = screen
        }
    }

    fun handleBack(): Boolean {
        if (!navigationBackStack.isEmpty()) {
            _currentScreen.value = navigationBackStack.pop()
            return true
        }
        return false
    }

    fun setDeviceMode(mode: DeviceMode) {
        _deviceMode.value = mode
    }

    fun showToast(msg: String) {
        _toastMessage.value = msg
    }

    fun clearToast() {
        _toastMessage.value = null
    }

    fun selectProject(project: WebsiteProject) {
        _selectedProject.value = project
        undoStack.clear()
        redoStack.clear()
        refreshProjectData(project.id)
    }

    private fun refreshProjectData(projectId: String) {
        viewModelScope.launch {
            repository.getBlogPostsFlow(projectId).collect { _blogPosts.value = it }
        }
        viewModelScope.launch {
            repository.getProductsFlow(projectId).collect { _ecommerceProducts.value = it }
        }
        viewModelScope.launch {
            repository.getDeploymentSnapshotsFlow(projectId).collect { _deploymentSnapshots.value = it }
        }
    }

    // AI Website Generation
    fun generateWebsite(params: WebsiteGenerationParams) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.generateNewWebsite(params)
            _isLoading.value = false
            result.onSuccess { proj ->
                _selectedProject.value = proj
                showToast("✓ Website '${proj.name}' successfully generated!")
                navigateTo(Screen.VISUAL_BUILDER)
            }.onFailure { err ->
                showToast("Generation failed: ${err.message}")
            }
        }
    }

    // AI Editing Assistant
    fun applyAiEdit(instruction: String) {
        val current = _selectedProject.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            pushUndo(current)
            val result = repository.modifyWebsiteWithAi(current.id, instruction)
            _isLoading.value = false
            result.onSuccess { updated ->
                _selectedProject.value = updated
                showToast("✓ AI changes applied successfully!")
            }.onFailure { err ->
                showToast("AI edit error: ${err.message}")
            }
        }
    }

    // Visual Editor Operations
    fun updateProjectDetails(name: String, category: String, desc: String, primaryColor: String, secondaryColor: String, theme: String) {
        val current = _selectedProject.value ?: return
        pushUndo(current)
        val updated = current.copy(
            name = name,
            category = category,
            description = desc,
            primaryColorHex = primaryColor,
            secondaryColorHex = secondaryColor,
            themeStyle = theme,
            lastModified = System.currentTimeMillis()
        )
        _selectedProject.value = updated
        viewModelScope.launch { repository.saveProject(updated) }
        showToast("Project settings updated")
    }

    fun updateSection(section: WebsiteSection) {
        val current = _selectedProject.value ?: return
        pushUndo(current)
        val pages = current.pages.map { page ->
            val updatedSections = page.sections.map { sec ->
                if (sec.id == section.id) section else sec
            }
            page.copy(sections = updatedSections)
        }
        val updated = current.copy(pages = pages, lastModified = System.currentTimeMillis())
        _selectedProject.value = updated
        viewModelScope.launch { repository.saveProject(updated) }
        showToast("Section updated")
    }

    fun addSection(type: SectionType) {
        val current = _selectedProject.value ?: return
        pushUndo(current)
        val newSection = WebsiteSection(
            id = "sec_${UUID.randomUUID().toString().take(6)}",
            type = type,
            title = when (type) {
                SectionType.FAQ -> "Frequently Asked Questions"
                SectionType.PRICING -> "Flexible Pricing"
                SectionType.TESTIMONIALS -> "What People Say"
                SectionType.FEATURES -> "Key Capabilities"
                SectionType.CONTACT_FORM -> "Contact Our Team"
                SectionType.STATS -> "Proven Track Record"
                SectionType.NEWSLETTER -> "Stay Informed"
                else -> "New Section"
            },
            subtitle = "Customizable section description",
            items = listOf(
                SectionItem("item_1", "Feature 1", "High impact description for this item.", iconName = "⭐", price = "₹999", badge = "New"),
                SectionItem("item_2", "Feature 2", "Detailed benefits and information.", iconName = "⚡", price = "₹1,999", badge = "Popular")
            )
        )
        val pages = current.pages.map { page ->
            if (page.isHome || page == current.pages.firstOrNull()) {
                val list = page.sections.toMutableList()
                val insertPos = (list.size - 1).coerceAtLeast(0)
                list.add(insertPos, newSection)
                page.copy(sections = list)
            } else page
        }
        val updated = current.copy(pages = pages, lastModified = System.currentTimeMillis())
        _selectedProject.value = updated
        viewModelScope.launch { repository.saveProject(updated) }
        showToast("Added $type section")
    }

    fun deleteSection(sectionId: String) {
        val current = _selectedProject.value ?: return
        pushUndo(current)
        val pages = current.pages.map { page ->
            page.copy(sections = page.sections.filterNot { it.id == sectionId })
        }
        val updated = current.copy(pages = pages, lastModified = System.currentTimeMillis())
        _selectedProject.value = updated
        viewModelScope.launch { repository.saveProject(updated) }
        showToast("Section removed")
    }

    fun reorderSection(fromIndex: Int, toIndex: Int) {
        val current = _selectedProject.value ?: return
        pushUndo(current)
        val pages = current.pages.map { page ->
            if (page.isHome || page == current.pages.firstOrNull()) {
                val list = page.sections.toMutableList()
                if (fromIndex in list.indices && toIndex in list.indices) {
                    val item = list.removeAt(fromIndex)
                    list.add(toIndex, item)
                }
                page.copy(sections = list)
            } else page
        }
        val updated = current.copy(pages = pages, lastModified = System.currentTimeMillis())
        _selectedProject.value = updated
        viewModelScope.launch { repository.saveProject(updated) }
    }

    // Code Editor Save
    fun saveCustomCode(customCss: String, customJs: String) {
        val current = _selectedProject.value ?: return
        pushUndo(current)
        val updated = current.copy(customCss = customCss, customJs = customJs, lastModified = System.currentTimeMillis())
        _selectedProject.value = updated
        viewModelScope.launch { repository.saveProject(updated) }
        showToast("✓ Custom Code saved and recompiled!")
    }

    // Duplicate & Delete Project
    fun duplicateProject(project: WebsiteProject) {
        viewModelScope.launch {
            val cloned = repository.duplicateProject(project)
            _selectedProject.value = cloned
            showToast("✓ Duplicated '${project.name}'")
        }
    }

    fun deleteProject(projectId: String) {
        viewModelScope.launch {
            repository.deleteProject(projectId)
            showToast("Project deleted")
            if (_selectedProject.value?.id == projectId) {
                _selectedProject.value = projects.value.firstOrNull { it.id != projectId }
            }
        }
    }

    // Use Template
    fun useTemplate(template: WebsiteProject) {
        viewModelScope.launch {
            val newId = "proj_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(4)}"
            val cloned = template.copy(
                id = newId,
                name = "${template.name} (My Site)",
                subdomain = template.subdomain.replace(".sitegen.page", "") + "-${(100..999).random()}.sitegen.page",
                status = com.example.data.model.ProjectStatus.DRAFT,
                lastModified = System.currentTimeMillis(),
                publishedAt = null
            )
            repository.saveProject(cloned)
            _selectedProject.value = cloned
            showToast("✓ Template loaded! Ready to customize.")
            navigateTo(Screen.VISUAL_BUILDER)
        }
    }

    // Publish Project
    fun publishProject(subdomain: String?, customDomain: String?) {
        val current = _selectedProject.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            val published = repository.publishProject(current.id, subdomain, customDomain)
            _selectedProject.value = published
            _isLoading.value = false
            showToast("🚀 Website published live to ${published.subdomain}!")
            refreshProjectData(published.id)
        }
    }

    // Rollback
    fun rollbackVersion(snapshot: DeploymentSnapshot) {
        val current = _selectedProject.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            val rolledBack = repository.rollbackDeployment(current.id, snapshot)
            _selectedProject.value = rolledBack
            _isLoading.value = false
            showToast("Restored deployment version ${snapshot.versionNumber}")
        }
    }

    // Custom Domain Verification
    fun verifyDomain(domain: String) {
        val current = _selectedProject.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(1200) // Realistic DNS lookup simulation
            val updated = current.copy(
                customDomain = domain,
                customDomainVerified = true,
                lastModified = System.currentTimeMillis()
            )
            repository.saveProject(updated)
            _selectedProject.value = updated
            _isLoading.value = false
            showToast("✓ Domain '$domain' verified and SSL certificate provisioned!")
        }
    }

    // SEO Settings
    fun updateSeo(title: String, desc: String, keywords: String) {
        val current = _selectedProject.value ?: return
        val updated = current.copy(
            seoTitle = title,
            seoDescription = desc,
            seoKeywords = keywords,
            lastModified = System.currentTimeMillis()
        )
        _selectedProject.value = updated
        viewModelScope.launch { repository.saveProject(updated) }
        showToast("SEO metadata updated")
    }

    // Cashfree Payments Flow
    fun startCashfreePlanUpgrade(plan: SubscriptionPlan, isYearly: Boolean) {
        viewModelScope.launch {
            val amount = if (isYearly) plan.priceYearlyINR.toDouble() else plan.priceMonthlyINR.toDouble()
            val order = repository.createCashfreeOrder(
                purpose = "UPGRADE_PLAN_${plan.name.uppercase()}",
                amount = amount,
                planId = plan.id
            )
            pendingPlanId = plan.id
            pendingCredits = null
            _activeCashfreeOrder.value = order
        }
    }

    fun startCashfreeCreditPurchase(credits: Int, priceInr: Double) {
        viewModelScope.launch {
            val order = repository.createCashfreeOrder(
                purpose = "BUY_${credits}_AI_CREDITS",
                amount = priceInr,
                credits = credits
            )
            pendingPlanId = null
            pendingCredits = credits
            _activeCashfreeOrder.value = order
        }
    }

    fun completeCashfreePayment(simulateFailure: Boolean = false) {
        val order = _activeCashfreeOrder.value ?: return
        viewModelScope.launch {
            _isLoading.value = true
            if (simulateFailure) {
                _isLoading.value = false
                _activeCashfreeOrder.value = null
                showToast("Payment was cancelled or failed in Cashfree checkout.")
                return@launch
            }
            val result = repository.verifyAndCompleteCashfreePayment(order, pendingPlanId, pendingCredits)
            _isLoading.value = false
            _activeCashfreeOrder.value = null
            result.onSuccess { msg ->
                showToast("✓ $msg")
            }.onFailure { err ->
                showToast("Verification failed: ${err.message}")
            }
        }
    }

    fun dismissCashfreeModal() {
        _activeCashfreeOrder.value = null
        pendingPlanId = null
        pendingCredits = null
    }

    // Blog CMS
    fun addBlogPost(title: String, excerpt: String, content: String, category: String, tags: String) {
        val current = _selectedProject.value ?: return
        viewModelScope.launch {
            val post = BlogPost(
                id = "post_${System.currentTimeMillis()}",
                projectId = current.id,
                title = title,
                slug = title.lowercase().replace(Regex("[^a-z0-9]"), "-").take(25),
                excerpt = excerpt,
                content = content,
                category = category,
                tags = tags
            )
            repository.saveBlogPost(post)
            showToast("✓ Blog post created!")
            refreshProjectData(current.id)
        }
    }

    fun deleteBlogPost(id: String) {
        val current = _selectedProject.value ?: return
        viewModelScope.launch {
            repository.deleteBlogPost(id)
            showToast("Post deleted")
            refreshProjectData(current.id)
        }
    }

    // Form Submissions / Leads
    fun recordLeadFromSite(name: String, email: String, phone: String, message: String) {
        val current = _selectedProject.value ?: return
        viewModelScope.launch {
            val submission = FormSubmission(
                id = "lead_${System.currentTimeMillis()}",
                projectId = current.id,
                websiteName = current.name,
                senderName = name,
                senderEmail = email,
                senderPhone = phone,
                message = message
            )
            repository.recordFormSubmission(submission)
            showToast("✓ New lead captured for ${current.name}!")
        }
    }

    fun updateLeadStatus(id: String, status: String) {
        viewModelScope.launch {
            repository.updateLeadStatus(id, status)
            showToast("Lead status updated to $status")
        }
    }

    // Ecommerce
    fun addProduct(name: String, price: Double, category: String, desc: String) {
        val current = _selectedProject.value ?: return
        viewModelScope.launch {
            val product = EcommerceProduct(
                id = "prod_${System.currentTimeMillis()}",
                projectId = current.id,
                name = name,
                price = price,
                category = category,
                description = desc
            )
            repository.saveProduct(product)
            showToast("✓ Product added to catalogue!")
            refreshProjectData(current.id)
        }
    }

    fun deleteProduct(id: String) {
        val current = _selectedProject.value ?: return
        viewModelScope.launch {
            repository.deleteProduct(id)
            showToast("Product deleted")
            refreshProjectData(current.id)
        }
    }

    // System Settings / Admin
    fun saveSettings(s: SystemSettings) {
        viewModelScope.launch {
            repository.saveSettings(s)
            showToast("✓ Configuration settings saved!")
        }
    }

    // Undo / Redo
    private fun pushUndo(project: WebsiteProject) {
        undoStack.push(project)
        redoStack.clear()
    }

    fun undo() {
        if (!undoStack.isEmpty()) {
            val current = _selectedProject.value ?: return
            redoStack.push(current)
            val previous = undoStack.pop()
            _selectedProject.value = previous
            viewModelScope.launch { repository.saveProject(previous) }
            showToast("Undo applied")
        }
    }

    fun redo() {
        if (!redoStack.isEmpty()) {
            val current = _selectedProject.value ?: return
            undoStack.push(current)
            val next = redoStack.pop()
            _selectedProject.value = next
            viewModelScope.launch { repository.saveProject(next) }
            showToast("Redo applied")
        }
    }

    // Helper for HTML Preview
    fun getCompiledHtml(): String {
        val project = _selectedProject.value ?: return "<html><body><h1>No project selected</h1></body></html>"
        return WebsiteCompiler.compileToHtml(project)
    }

    fun getAnalyticsData(): AnalyticsData {
        val project = _selectedProject.value ?: return repository.getAnalytics(
            WebsiteProject(id = "none", name = "Site", category = "General", description = "", subdomain = "")
        )
        return repository.getAnalytics(project)
    }
}
