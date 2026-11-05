package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.generator.TemplatesCatalog
import com.example.data.generator.WebsiteJsonUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProjectEntity::class,
        BlogPostEntity::class,
        FormSubmissionEntity::class,
        EcommerceProductEntity::class,
        CreditTransactionEntity::class,
        SystemSettingsEntity::class,
        DeploymentSnapshotEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao
    abstract fun blogDao(): BlogDao
    abstract fun formDao(): FormDao
    abstract fun productDao(): ProductDao
    abstract fun creditDao(): CreditDao
    abstract fun settingsDao(): SettingsDao
    abstract fun deploymentDao(): DeploymentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sitegen_database.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate starter data
                            CoroutineScope(Dispatchers.IO).launch {
                                populateInitialData(getInstance(context))
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateInitialData(database: AppDatabase) {
            val projectDao = database.projectDao()
            val creditDao = database.creditDao()
            val settingsDao = database.settingsDao()
            val blogDao = database.blogDao()
            val formDao = database.formDao()
            val productDao = database.productDao()

            // Seed initial settings
            settingsDao.saveSettings(
                SystemSettingsEntity(
                    id = 1,
                    geminiApiKey = "",
                    cashfreeAppId = "TEST_10423985cf90",
                    cashfreeSecretKey = "TEST_sec_993847291039",
                    cashfreeApiVersion = "2023-08-01",
                    cashfreeWebhookSecret = "wh_sec_sitegen_live",
                    cashfreeEnvironment = "SANDBOX",
                    cloudflareAccountId = "cf_acc_8729184",
                    cloudflareApiToken = "cf_tok_sitegen_prod",
                    cloudflareZoneId = "cf_zn_493820",
                    cloudflareR2Bucket = "sitegen-published-sites",
                    baseDomain = "sitegen.page",
                    walletBalance = 60,
                    currentPlanId = "plan_pro"
                )
            )

            // Seed initial credit transaction
            creditDao.insertTransaction(
                CreditTransactionEntity(
                    id = "tx_welcome_bonus",
                    amount = 60,
                    type = "WELCOME_BONUS",
                    description = "Welcome bonus AI credits to generate your first websites",
                    timestamp = System.currentTimeMillis()
                )
            )

            // Seed default projects from templates
            val starterTemplates = TemplatesCatalog.templates.take(3)
            for (tpl in starterTemplates) {
                projectDao.insertProject(
                    ProjectEntity(
                        id = tpl.id,
                        name = tpl.name,
                        category = tpl.category,
                        description = tpl.description,
                        themeStyle = tpl.themeStyle,
                        primaryColorHex = tpl.primaryColorHex,
                        secondaryColorHex = tpl.secondaryColorHex,
                        subdomain = tpl.subdomain,
                        customDomain = tpl.customDomain,
                        customDomainVerified = tpl.customDomainVerified,
                        status = tpl.status.name,
                        lastModified = tpl.lastModified,
                        publishedAt = tpl.publishedAt ?: System.currentTimeMillis(),
                        deploymentVersion = tpl.deploymentVersion,
                        pagesJson = WebsiteJsonUtils.serializePages(tpl.pages),
                        seoTitle = tpl.seoTitle,
                        seoDescription = tpl.seoDescription,
                        seoKeywords = tpl.seoKeywords,
                        customCss = tpl.customCss,
                        customJs = tpl.customJs,
                        analyticsViews = tpl.analyticsViews,
                        analyticsUniqueVisitors = tpl.analyticsUniqueVisitors
                    )
                )
            }

            // Seed initial blog posts
            blogDao.insertPost(
                BlogPostEntity(
                    id = "post_1",
                    projectId = starterTemplates[0].id,
                    title = "How We Scale Digital Performance Marketing in 2026",
                    slug = "scaling-performance-marketing",
                    excerpt = "A breakdown of creative strategy, conversion optimization, and automated bidding algorithms.",
                    content = "In today's hyper-competitive digital landscape, relying on basic keyword targeting is no longer sufficient...",
                    author = "Apex Wave Growth Team",
                    category = "Marketing",
                    tags = "growth, roas, marketing",
                    imageUrl = "https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=600&auto=format&fit=crop",
                    status = "Published",
                    createdAt = System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000L
                )
            )

            // Seed initial form submission
            formDao.insertSubmission(
                FormSubmissionEntity(
                    id = "sub_1",
                    projectId = starterTemplates[0].id,
                    websiteName = starterTemplates[0].name,
                    formType = "Contact Form",
                    senderName = "Vikram Aditya",
                    senderEmail = "vikram@d2cbrand.in",
                    senderPhone = "+91 98112 34567",
                    message = "Hi team, we are scaling our apparel label to ₹2Cr monthly GMV and looking for full performance marketing management.",
                    status = "New",
                    submittedAt = System.currentTimeMillis() - 4 * 60 * 60 * 1000L
                )
            )

            // Seed initial ecommerce product
            productDao.insertProduct(
                EcommerceProductEntity(
                    id = "prod_1",
                    projectId = starterTemplates[0].id,
                    name = "Full Funnel Growth Audit",
                    price = 14999.0,
                    currency = "INR",
                    category = "Services",
                    description = "Comprehensive 40-point technical audit covering ads, conversion rate, and retention.",
                    imageUrl = "",
                    inStock = true,
                    inventoryCount = 10
                )
            )
        }
    }
}
