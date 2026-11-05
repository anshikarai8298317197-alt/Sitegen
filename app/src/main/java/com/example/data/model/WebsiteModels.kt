package com.example.data.model

enum class ProjectStatus {
    DRAFT,
    PUBLISHED,
    UPDATING,
    ARCHIVED
}

enum class SectionType {
    NAVBAR,
    HERO,
    FEATURES,
    SERVICES,
    STATS,
    PORTFOLIO_GALLERY,
    PRICING,
    TESTIMONIALS,
    FAQ,
    CONTACT_FORM,
    BLOG_FEED,
    NEWSLETTER,
    FOOTER,
    CUSTOM_CODE
}

data class SectionItem(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String = "Sparkles",
    val price: String = "",
    val badge: String = "",
    val imageUrl: String = "",
    val linkUrl: String = ""
)

data class WebsiteSection(
    val id: String,
    val type: SectionType,
    val title: String = "",
    val subtitle: String = "",
    val badgeText: String = "",
    val primaryButtonText: String = "",
    val primaryButtonUrl: String = "#",
    val secondaryButtonText: String = "",
    val secondaryButtonUrl: String = "#",
    val imageUrl: String = "",
    val backgroundStyle: String = "surface", // "default", "surface", "primary_subtle", "dark", "gradient"
    val paddingY: Int = 48,
    val items: List<SectionItem> = emptyList(),
    val rawHtml: String = ""
)

data class WebsitePage(
    val id: String,
    val title: String,
    val slug: String,
    val isHome: Boolean = false,
    val sections: List<WebsiteSection> = emptyList()
)

data class WebsiteProject(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val themeStyle: String = "Modern Cyber",
    val primaryColorHex: String = "#6366F1",
    val secondaryColorHex: String = "#06B6D4",
    val subdomain: String,
    val customDomain: String? = null,
    val customDomainVerified: Boolean = false,
    val status: ProjectStatus = ProjectStatus.DRAFT,
    val lastModified: Long = System.currentTimeMillis(),
    val publishedAt: Long? = null,
    val deploymentVersion: Int = 1,
    val pages: List<WebsitePage> = emptyList(),
    val seoTitle: String = "",
    val seoDescription: String = "",
    val seoKeywords: String = "",
    val customCss: String = "",
    val customJs: String = "",
    val analyticsViews: Int = 1420,
    val analyticsUniqueVisitors: Int = 890
)

data class DeploymentSnapshot(
    val id: String,
    val projectId: String,
    val versionNumber: Int,
    val publishedAt: Long,
    val summary: String,
    val url: String,
    val pagesJson: String
)

data class BlogPost(
    val id: String,
    val projectId: String,
    val title: String,
    val slug: String,
    val excerpt: String,
    val content: String,
    val author: String = "Admin",
    val category: String = "General",
    val tags: String = "web, design",
    val imageUrl: String = "",
    val status: String = "Published", // "Draft", "Published"
    val createdAt: Long = System.currentTimeMillis()
)

data class FormSubmission(
    val id: String,
    val projectId: String,
    val websiteName: String,
    val formType: String = "Contact Form",
    val senderName: String,
    val senderEmail: String,
    val senderPhone: String = "",
    val message: String,
    val status: String = "New", // "New", "Contacted", "Converted", "Archived"
    val submittedAt: Long = System.currentTimeMillis()
)

data class EcommerceProduct(
    val id: String,
    val projectId: String,
    val name: String,
    val price: Double,
    val currency: String = "INR",
    val category: String,
    val description: String,
    val imageUrl: String = "",
    val inStock: Boolean = true,
    val inventoryCount: Int = 25
)

data class AiCreditWallet(
    val balance: Int = 60,
    val totalPurchased: Int = 60,
    val totalUsed: Int = 0
)

data class AiTransaction(
    val id: String,
    val amount: Int,
    val type: String, // "WELCOME_BONUS", "PURCHASE", "GENERATION", "AI_EDIT"
    val description: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class SubscriptionPlan(
    val id: String,
    val name: String,
    val tagline: String,
    val priceMonthlyINR: Int,
    val priceYearlyINR: Int,
    val websitesLimit: Int,
    val aiCreditsPerMonth: Int,
    val subdomainAllowed: Boolean = true,
    val customDomainAllowed: Boolean = true,
    val codeExportAllowed: Boolean = true,
    val removeBranding: Boolean = true,
    val analyticsAllowed: Boolean = true,
    val ecommerceAllowed: Boolean = false,
    val isPopular: Boolean = false
)

data class CashfreePaymentOrder(
    val orderId: String,
    val amount: Double,
    val currency: String = "INR",
    val customerId: String,
    val customerEmail: String,
    val customerPhone: String,
    val purpose: String, // "SUBSCRIPTION_PRO", "AI_CREDITS_150", etc.
    val status: String = "CREATED", // "CREATED", "SUCCESS", "FAILED"
    val cfPaymentId: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

data class UserProfile(
    val id: String = "usr_sitegen_admin",
    val email: String = "ashgaming0502@gmail.com",
    val name: String = "Ash Gaming",
    val role: String = "SUPER_ADMIN", // "USER", "SUPER_ADMIN"
    val currentPlanId: String = "plan_pro",
    val planExpiry: Long = System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000,
    val avatarUrl: String = ""
)

data class SystemSettings(
    val id: Int = 1,
    val geminiApiKey: String = "",
    val cashfreeAppId: String = "TEST_10423985cf90",
    val cashfreeSecretKey: String = "TEST_sec_993847291039",
    val cashfreeApiVersion: String = "2023-08-01",
    val cashfreeWebhookSecret: String = "wh_sec_sitegen_live",
    val cashfreeEnvironment: String = "SANDBOX", // "SANDBOX", "PRODUCTION"
    val cloudflareAccountId: String = "cf_acc_8729184",
    val cloudflareApiToken: String = "cf_tok_sitegen_prod",
    val cloudflareZoneId: String = "cf_zn_493820",
    val cloudflareR2Bucket: String = "sitegen-published-sites",
    val baseDomain: String = "sitegen.page",
    val enableRegistration: Boolean = true,
    val maintenanceMode: Boolean = false
)

data class AnalyticsData(
    val totalPageviews: Int,
    val uniqueVisitors: Int,
    val bounceRatePercent: Int,
    val avgSessionSecs: Int,
    val topPages: List<Pair<String, Int>>,
    val trafficSources: List<Pair<String, Int>>,
    val devices: List<Pair<String, Int>>,
    val dailyViews: List<Pair<String, Int>>
)
