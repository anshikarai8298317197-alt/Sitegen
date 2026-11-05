package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class ProjectEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val description: String,
    val themeStyle: String,
    val primaryColorHex: String,
    val secondaryColorHex: String,
    val subdomain: String,
    val customDomain: String?,
    val customDomainVerified: Boolean,
    val status: String,
    val lastModified: Long,
    val publishedAt: Long?,
    val deploymentVersion: Int,
    val pagesJson: String,
    val seoTitle: String,
    val seoDescription: String,
    val seoKeywords: String,
    val customCss: String,
    val customJs: String,
    val analyticsViews: Int,
    val analyticsUniqueVisitors: Int
)

@Entity(tableName = "blog_posts")
data class BlogPostEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val title: String,
    val slug: String,
    val excerpt: String,
    val content: String,
    val author: String,
    val category: String,
    val tags: String,
    val imageUrl: String,
    val status: String,
    val createdAt: Long
)

@Entity(tableName = "form_submissions")
data class FormSubmissionEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val websiteName: String,
    val formType: String,
    val senderName: String,
    val senderEmail: String,
    val senderPhone: String,
    val message: String,
    val status: String,
    val submittedAt: Long
)

@Entity(tableName = "ecommerce_products")
data class EcommerceProductEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val name: String,
    val price: Double,
    val currency: String,
    val category: String,
    val description: String,
    val imageUrl: String,
    val inStock: Boolean,
    val inventoryCount: Int
)

@Entity(tableName = "credit_transactions")
data class CreditTransactionEntity(
    @PrimaryKey val id: String,
    val amount: Int,
    val type: String,
    val description: String,
    val timestamp: Long
)

@Entity(tableName = "system_settings")
data class SystemSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val geminiApiKey: String,
    val cashfreeAppId: String,
    val cashfreeSecretKey: String,
    val cashfreeApiVersion: String,
    val cashfreeWebhookSecret: String,
    val cashfreeEnvironment: String,
    val cloudflareAccountId: String,
    val cloudflareApiToken: String,
    val cloudflareZoneId: String,
    val cloudflareR2Bucket: String,
    val baseDomain: String,
    val walletBalance: Int,
    val currentPlanId: String
)

@Entity(tableName = "deployment_snapshots")
data class DeploymentSnapshotEntity(
    @PrimaryKey val id: String,
    val projectId: String,
    val versionNumber: Int,
    val publishedAt: Long,
    val summary: String,
    val url: String,
    val pagesJson: String
)
