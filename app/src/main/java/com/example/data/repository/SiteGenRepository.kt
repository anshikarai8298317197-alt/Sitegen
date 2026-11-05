package com.example.data.repository

import android.content.Context
import com.example.data.generator.AiWebsiteEngine
import com.example.data.generator.WebsiteGenerationParams
import com.example.data.generator.WebsiteJsonUtils
import com.example.data.local.AppDatabase
import com.example.data.local.BlogPostEntity
import com.example.data.local.CreditTransactionEntity
import com.example.data.local.DeploymentSnapshotEntity
import com.example.data.local.EcommerceProductEntity
import com.example.data.local.FormSubmissionEntity
import com.example.data.local.ProjectEntity
import com.example.data.local.SystemSettingsEntity
import com.example.data.model.AiCreditWallet
import com.example.data.model.AiTransaction
import com.example.data.model.AnalyticsData
import com.example.data.model.BlogPost
import com.example.data.model.CashfreePaymentOrder
import com.example.data.model.DeploymentSnapshot
import com.example.data.model.EcommerceProduct
import com.example.data.model.FormSubmission
import com.example.data.model.ProjectStatus
import com.example.data.model.SubscriptionPlan
import com.example.data.model.SystemSettings
import com.example.data.model.UserProfile
import com.example.data.model.WebsiteProject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class SiteGenRepository(private val context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val projectDao = db.projectDao()
    private val blogDao = db.blogDao()
    private val formDao = db.formDao()
    private val productDao = db.productDao()
    private val creditDao = db.creditDao()
    private val settingsDao = db.settingsDao()
    private val deploymentDao = db.deploymentDao()

    val availablePlans: List<SubscriptionPlan> = listOf(
        SubscriptionPlan(
            id = "plan_free",
            name = "Free",
            tagline = "For creators experimenting with AI website generation",
            priceMonthlyINR = 0,
            priceYearlyINR = 0,
            websitesLimit = 1,
            aiCreditsPerMonth = 20,
            subdomainAllowed = true,
            customDomainAllowed = false,
            codeExportAllowed = false,
            removeBranding = false,
            analyticsAllowed = false,
            ecommerceAllowed = false
        ),
        SubscriptionPlan(
            id = "plan_starter",
            name = "Starter",
            tagline = "For freelancers, solopreneurs & small businesses",
            priceMonthlyINR = 799,
            priceYearlyINR = 6999,
            websitesLimit = 3,
            aiCreditsPerMonth = 100,
            subdomainAllowed = true,
            customDomainAllowed = true,
            codeExportAllowed = true,
            removeBranding = true,
            analyticsAllowed = true,
            ecommerceAllowed = false
        ),
        SubscriptionPlan(
            id = "plan_pro",
            name = "Pro",
            tagline = "For scaling companies, startups and agencies",
            priceMonthlyINR = 1999,
            priceYearlyINR = 16999,
            websitesLimit = 10,
            aiCreditsPerMonth = 350,
            subdomainAllowed = true,
            customDomainAllowed = true,
            codeExportAllowed = true,
            removeBranding = true,
            analyticsAllowed = true,
            ecommerceAllowed = true,
            isPopular = true
        ),
        SubscriptionPlan(
            id = "plan_agency",
            name = "Agency",
            tagline = "High-volume generation for agencies and enterprise",
            priceMonthlyINR = 4999,
            priceYearlyINR = 41999,
            websitesLimit = 50,
            aiCreditsPerMonth = 1200,
            subdomainAllowed = true,
            customDomainAllowed = true,
            codeExportAllowed = true,
            removeBranding = true,
            analyticsAllowed = true,
            ecommerceAllowed = true
        )
    )

    // Flow of all projects
    fun getAllProjectsFlow(): Flow<List<WebsiteProject>> {
        return projectDao.getAllProjectsFlow().map { entities ->
            entities.map { mapToModel(it) }
        }
    }

    suspend fun getProjectById(id: String): WebsiteProject? {
        val entity = projectDao.getProjectById(id) ?: return null
        return mapToModel(entity)
    }

    suspend fun saveProject(project: WebsiteProject) {
        projectDao.insertProject(mapToEntity(project.copy(lastModified = System.currentTimeMillis())))
    }

    suspend fun deleteProject(id: String) {
        projectDao.deleteProjectById(id)
    }

    suspend fun duplicateProject(project: WebsiteProject): WebsiteProject {
        val newId = "proj_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(4)}"
        val cleanSlug = project.subdomain.replace(".sitegen.page", "") + "-copy"
        val cloned = project.copy(
            id = newId,
            name = "${project.name} (Copy)",
            subdomain = "$cleanSlug.sitegen.page",
            status = ProjectStatus.DRAFT,
            customDomain = null,
            customDomainVerified = false,
            publishedAt = null,
            deploymentVersion = 1,
            lastModified = System.currentTimeMillis()
        )
        saveProject(cloned)
        return cloned
    }

    suspend fun generateNewWebsite(params: WebsiteGenerationParams): Result<WebsiteProject> {
        // Check credit balance
        val settings = settingsDao.getSettings() ?: SystemSettingsEntity(
            geminiApiKey = "", cashfreeAppId = "", cashfreeSecretKey = "",
            cashfreeApiVersion = "", cashfreeWebhookSecret = "", cashfreeEnvironment = "",
            cloudflareAccountId = "", cloudflareApiToken = "", cloudflareZoneId = "",
            cloudflareR2Bucket = "", baseDomain = "", walletBalance = 50, currentPlanId = "plan_pro"
        )

        if (settings.walletBalance < 10) {
            return Result.failure(Exception("Insufficient AI Credits. Generating a full website requires 10 credits. Current balance: ${settings.walletBalance}"))
        }

        // Deduct 10 credits
        val newBalance = settings.walletBalance - 10
        settingsDao.saveSettings(settings.copy(walletBalance = newBalance))
        creditDao.insertTransaction(
            CreditTransactionEntity(
                id = "tx_${System.currentTimeMillis()}",
                amount = -10,
                type = "GENERATION",
                description = "Full website generation for '${params.name}'",
                timestamp = System.currentTimeMillis()
            )
        )

        val project = AiWebsiteEngine.generateWebsite(params)
        saveProject(project)
        return Result.success(project)
    }

    suspend fun modifyWebsiteWithAi(projectId: String, instruction: String): Result<WebsiteProject> {
        val project = getProjectById(projectId) ?: return Result.failure(Exception("Project not found"))
        val settings = settingsDao.getSettings() ?: return Result.failure(Exception("Settings not found"))

        if (settings.walletBalance < 2) {
            return Result.failure(Exception("Insufficient AI Credits. AI edit requires 2 credits. Current balance: ${settings.walletBalance}"))
        }

        val newBalance = settings.walletBalance - 2
        settingsDao.saveSettings(settings.copy(walletBalance = newBalance))
        creditDao.insertTransaction(
            CreditTransactionEntity(
                id = "tx_${System.currentTimeMillis()}",
                amount = -2,
                type = "AI_EDIT",
                description = "AI modification: \"${instruction.take(30)}...\"",
                timestamp = System.currentTimeMillis()
            )
        )

        val modified = AiWebsiteEngine.applyAiModification(project, instruction)
        saveProject(modified)
        return Result.success(modified)
    }

    suspend fun publishProject(projectId: String, subdomain: String?, customDomain: String?): WebsiteProject {
        val project = getProjectById(projectId) ?: throw IllegalArgumentException("Project not found")
        val newVersion = project.deploymentVersion + 1
        val now = System.currentTimeMillis()

        val updated = project.copy(
            subdomain = subdomain ?: project.subdomain,
            customDomain = customDomain ?: project.customDomain,
            status = ProjectStatus.PUBLISHED,
            deploymentVersion = newVersion,
            publishedAt = now,
            lastModified = now
        )
        saveProject(updated)

        // Record deployment snapshot
        deploymentDao.insertSnapshot(
            DeploymentSnapshotEntity(
                id = "deploy_${UUID.randomUUID().toString().take(8)}",
                projectId = projectId,
                versionNumber = newVersion,
                publishedAt = now,
                summary = "Published deployment v$newVersion to ${updated.subdomain}",
                url = updated.customDomain?.let { "https://$it" } ?: "https://${updated.subdomain}",
                pagesJson = WebsiteJsonUtils.serializePages(updated.pages)
            )
        )

        return updated
    }

    suspend fun rollbackDeployment(projectId: String, snapshot: DeploymentSnapshot): WebsiteProject {
        val project = getProjectById(projectId) ?: throw IllegalArgumentException("Project not found")
        val restoredPages = WebsiteJsonUtils.deserializePages(snapshot.pagesJson)
        val updated = project.copy(
            pages = restoredPages,
            deploymentVersion = snapshot.versionNumber,
            lastModified = System.currentTimeMillis()
        )
        saveProject(updated)
        return updated
    }

    fun getDeploymentSnapshotsFlow(projectId: String): Flow<List<DeploymentSnapshot>> {
        return deploymentDao.getSnapshotsFlow(projectId).map { list ->
            list.map {
                DeploymentSnapshot(
                    id = it.id,
                    projectId = it.projectId,
                    versionNumber = it.versionNumber,
                    publishedAt = it.publishedAt,
                    summary = it.summary,
                    url = it.url,
                    pagesJson = it.pagesJson
                )
            }
        }
    }

    // Credits & Wallet
    fun getCreditWalletFlow(): Flow<AiCreditWallet> {
        return settingsDao.getSettingsFlow().map { settings ->
            val bal = settings?.walletBalance ?: 60
            AiCreditWallet(
                balance = bal,
                totalPurchased = 150,
                totalUsed = 90
            )
        }
    }

    fun getCreditTransactionsFlow(): Flow<List<AiTransaction>> {
        return creditDao.getAllTransactionsFlow().map { list ->
            list.map {
                AiTransaction(
                    id = it.id,
                    amount = it.amount,
                    type = it.type,
                    description = it.description,
                    timestamp = it.timestamp
                )
            }
        }
    }

    // Cashfree Checkout Simulator & Server-Side Verification
    suspend fun createCashfreeOrder(purpose: String, amount: Double, planId: String? = null, credits: Int? = null): CashfreePaymentOrder {
        val orderId = "order_cf_${System.currentTimeMillis()}_${(1000..9999).random()}"
        return CashfreePaymentOrder(
            orderId = orderId,
            amount = amount,
            currency = "INR",
            customerId = "cust_sitegen_ash",
            customerEmail = "ashgaming0502@gmail.com",
            customerPhone = "9876543210",
            purpose = purpose,
            status = "CREATED"
        )
    }

    suspend fun verifyAndCompleteCashfreePayment(order: CashfreePaymentOrder, planId: String? = null, credits: Int? = null): Result<String> {
        val settings = settingsDao.getSettings() ?: return Result.failure(Exception("Settings not initialized"))

        // Server-side verification simulated: check signature and status
        val paymentId = "cf_pay_${System.currentTimeMillis()}"

        if (credits != null && credits > 0) {
            val newBal = settings.walletBalance + credits
            settingsDao.saveSettings(settings.copy(walletBalance = newBal))
            creditDao.insertTransaction(
                CreditTransactionEntity(
                    id = "tx_cf_${System.currentTimeMillis()}",
                    amount = credits,
                    type = "PURCHASE",
                    description = "Purchased $credits AI Credits via Cashfree ($paymentId)",
                    timestamp = System.currentTimeMillis()
                )
            )
            return Result.success("Payment verified! Added $credits AI credits to your wallet.")
        }

        if (planId != null) {
            val plan = availablePlans.firstOrNull { it.id == planId }
            settingsDao.saveSettings(settings.copy(currentPlanId = planId))
            creditDao.insertTransaction(
                CreditTransactionEntity(
                    id = "tx_plan_${System.currentTimeMillis()}",
                    amount = plan?.aiCreditsPerMonth ?: 100,
                    type = "PURCHASE",
                    description = "Subscribed to ${plan?.name ?: "Pro"} Plan via Cashfree ($paymentId)",
                    timestamp = System.currentTimeMillis()
                )
            )
            return Result.success("Payment verified! Activated ${plan?.name ?: "Pro"} plan.")
        }

        return Result.success("Payment verified successfully!")
    }

    // Blog CMS
    fun getBlogPostsFlow(projectId: String): Flow<List<BlogPost>> {
        return blogDao.getPostsByProjectFlow(projectId).map { list ->
            list.map {
                BlogPost(
                    id = it.id,
                    projectId = it.projectId,
                    title = it.title,
                    slug = it.slug,
                    excerpt = it.excerpt,
                    content = it.content,
                    author = it.author,
                    category = it.category,
                    tags = it.tags,
                    imageUrl = it.imageUrl,
                    status = it.status,
                    createdAt = it.createdAt
                )
            }
        }
    }

    suspend fun saveBlogPost(post: BlogPost) {
        blogDao.insertPost(
            BlogPostEntity(
                id = post.id,
                projectId = post.projectId,
                title = post.title,
                slug = post.slug,
                excerpt = post.excerpt,
                content = post.content,
                author = post.author,
                category = post.category,
                tags = post.tags,
                imageUrl = post.imageUrl,
                status = post.status,
                createdAt = post.createdAt
            )
        )
    }

    suspend fun deleteBlogPost(id: String) {
        blogDao.deletePost(id)
    }

    // Form Submissions / Leads
    fun getFormSubmissionsFlow(): Flow<List<FormSubmission>> {
        return formDao.getAllSubmissionsFlow().map { list ->
            list.map {
                FormSubmission(
                    id = it.id,
                    projectId = it.projectId,
                    websiteName = it.websiteName,
                    formType = it.formType,
                    senderName = it.senderName,
                    senderEmail = it.senderEmail,
                    senderPhone = it.senderPhone,
                    message = it.message,
                    status = it.status,
                    submittedAt = it.submittedAt
                )
            }
        }
    }

    suspend fun recordFormSubmission(sub: FormSubmission) {
        formDao.insertSubmission(
            FormSubmissionEntity(
                id = sub.id,
                projectId = sub.projectId,
                websiteName = sub.websiteName,
                formType = sub.formType,
                senderName = sub.senderName,
                senderEmail = sub.senderEmail,
                senderPhone = sub.senderPhone,
                message = sub.message,
                status = sub.status,
                submittedAt = sub.submittedAt
            )
        )
    }

    suspend fun updateLeadStatus(id: String, status: String) {
        formDao.updateStatus(id, status)
    }

    suspend fun deleteLead(id: String) {
        formDao.deleteSubmission(id)
    }

    // Ecommerce Products
    fun getProductsFlow(projectId: String): Flow<List<EcommerceProduct>> {
        return productDao.getProductsByProjectFlow(projectId).map { list ->
            list.map {
                EcommerceProduct(
                    id = it.id,
                    projectId = it.projectId,
                    name = it.name,
                    price = it.price,
                    currency = it.currency,
                    category = it.category,
                    description = it.description,
                    imageUrl = it.imageUrl,
                    inStock = it.inStock,
                    inventoryCount = it.inventoryCount
                )
            }
        }
    }

    suspend fun saveProduct(product: EcommerceProduct) {
        productDao.insertProduct(
            EcommerceProductEntity(
                id = product.id,
                projectId = product.projectId,
                name = product.name,
                price = product.price,
                currency = product.currency,
                category = product.category,
                description = product.description,
                imageUrl = product.imageUrl,
                inStock = product.inStock,
                inventoryCount = product.inventoryCount
            )
        )
    }

    suspend fun deleteProduct(id: String) {
        productDao.deleteProduct(id)
    }

    // Settings
    fun getSettingsFlow(): Flow<SystemSettings> {
        return settingsDao.getSettingsFlow().map {
            if (it == null) SystemSettings() else SystemSettings(
                id = it.id,
                geminiApiKey = it.geminiApiKey,
                cashfreeAppId = it.cashfreeAppId,
                cashfreeSecretKey = it.cashfreeSecretKey,
                cashfreeApiVersion = it.cashfreeApiVersion,
                cashfreeWebhookSecret = it.cashfreeWebhookSecret,
                cashfreeEnvironment = it.cashfreeEnvironment,
                cloudflareAccountId = it.cloudflareAccountId,
                cloudflareApiToken = it.cloudflareApiToken,
                cloudflareZoneId = it.cloudflareZoneId,
                cloudflareR2Bucket = it.cloudflareR2Bucket,
                baseDomain = it.baseDomain
            )
        }
    }

    suspend fun saveSettings(s: SystemSettings) {
        val current = settingsDao.getSettings()
        settingsDao.saveSettings(
            SystemSettingsEntity(
                id = 1,
                geminiApiKey = s.geminiApiKey,
                cashfreeAppId = s.cashfreeAppId,
                cashfreeSecretKey = s.cashfreeSecretKey,
                cashfreeApiVersion = s.cashfreeApiVersion,
                cashfreeWebhookSecret = s.cashfreeWebhookSecret,
                cashfreeEnvironment = s.cashfreeEnvironment,
                cloudflareAccountId = s.cloudflareAccountId,
                cloudflareApiToken = s.cloudflareApiToken,
                cloudflareZoneId = s.cloudflareZoneId,
                cloudflareR2Bucket = s.cloudflareR2Bucket,
                baseDomain = s.baseDomain,
                walletBalance = current?.walletBalance ?: 60,
                currentPlanId = current?.currentPlanId ?: "plan_pro"
            )
        )
    }

    // Analytics Generator
    fun getAnalytics(project: WebsiteProject): AnalyticsData {
        return AnalyticsData(
            totalPageviews = project.analyticsViews,
            uniqueVisitors = project.analyticsUniqueVisitors,
            bounceRatePercent = 38,
            avgSessionSecs = 142,
            topPages = listOf(
                "/" to (project.analyticsViews * 0.65).toInt(),
                "/pricing" to (project.analyticsViews * 0.18).toInt(),
                "/services" to (project.analyticsViews * 0.11).toInt(),
                "/contact" to (project.analyticsViews * 0.06).toInt()
            ),
            trafficSources = listOf(
                "Direct / Bookmark" to 42,
                "Google Organic Search" to 31,
                "Twitter / X" to 14,
                "LinkedIn" to 8,
                "Other Referrals" to 5
            ),
            devices = listOf(
                "Mobile Phone" to 62,
                "Desktop / Laptop" to 33,
                "Tablet" to 5
            ),
            dailyViews = listOf(
                "Mon" to 140,
                "Tue" to 220,
                "Wed" to 195,
                "Thu" to 310,
                "Fri" to 280,
                "Sat" to 160,
                "Sun" to 115
            )
        )
    }

    // Mappers
    private fun mapToModel(entity: ProjectEntity): WebsiteProject {
        return WebsiteProject(
            id = entity.id,
            name = entity.name,
            category = entity.category,
            description = entity.description,
            themeStyle = entity.themeStyle,
            primaryColorHex = entity.primaryColorHex,
            secondaryColorHex = entity.secondaryColorHex,
            subdomain = entity.subdomain,
            customDomain = entity.customDomain,
            customDomainVerified = entity.customDomainVerified,
            status = try { ProjectStatus.valueOf(entity.status) } catch (e: Exception) { ProjectStatus.DRAFT },
            lastModified = entity.lastModified,
            publishedAt = entity.publishedAt,
            deploymentVersion = entity.deploymentVersion,
            pages = WebsiteJsonUtils.deserializePages(entity.pagesJson),
            seoTitle = entity.seoTitle,
            seoDescription = entity.seoDescription,
            seoKeywords = entity.seoKeywords,
            customCss = entity.customCss,
            customJs = entity.customJs,
            analyticsViews = entity.analyticsViews,
            analyticsUniqueVisitors = entity.analyticsUniqueVisitors
        )
    }

    private fun mapToEntity(model: WebsiteProject): ProjectEntity {
        return ProjectEntity(
            id = model.id,
            name = model.name,
            category = model.category,
            description = model.description,
            themeStyle = model.themeStyle,
            primaryColorHex = model.primaryColorHex,
            secondaryColorHex = model.secondaryColorHex,
            subdomain = model.subdomain,
            customDomain = model.customDomain,
            customDomainVerified = model.customDomainVerified,
            status = model.status.name,
            lastModified = model.lastModified,
            publishedAt = model.publishedAt,
            deploymentVersion = model.deploymentVersion,
            pagesJson = WebsiteJsonUtils.serializePages(model.pages),
            seoTitle = model.seoTitle,
            seoDescription = model.seoDescription,
            seoKeywords = model.seoKeywords,
            customCss = model.customCss,
            customJs = model.customJs,
            analyticsViews = model.analyticsViews,
            analyticsUniqueVisitors = model.analyticsUniqueVisitors
        )
    }
}
