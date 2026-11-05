package com.example.data.generator

import com.example.data.model.ProjectStatus
import com.example.data.model.SectionItem
import com.example.data.model.SectionType
import com.example.data.model.WebsitePage
import com.example.data.model.WebsiteProject
import com.example.data.model.WebsiteSection
import java.util.UUID

data class WebsiteGenerationParams(
    val name: String,
    val category: String,
    val description: String,
    val themeStyle: String = "Modern Cyber",
    val primaryColorHex: String = "#6366F1",
    val secondaryColorHex: String = "#06B6D4",
    val numPages: Int = 1,
    val features: List<String> = listOf("Hero", "Features", "Pricing", "Testimonials", "Contact Form")
)

object AiWebsiteEngine {

    fun generateWebsite(params: WebsiteGenerationParams): WebsiteProject {
        val cleanName = params.name.ifBlank { "NextGen Venture" }
        val slug = cleanName.lowercase().replace(Regex("[^a-z0-9]"), "").take(15).ifBlank { "mywebsite" }
        val projectId = "proj_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(6)}"
        val subdomain = "$slug.sitegen.page"

        // Build Sections based on category and selected features
        val sections = mutableListOf<WebsiteSection>()

        // 1. Navbar
        sections.add(
            WebsiteSection(
                id = "sec_${UUID.randomUUID().toString().take(6)}",
                type = SectionType.NAVBAR,
                title = cleanName,
                primaryButtonText = if (params.category.contains("Ecommerce", true)) "Shop Now" else "Get Started",
                primaryButtonUrl = "#contact"
            )
        )

        // 2. Hero Section
        val heroBadge = when {
            params.category.contains("Agency", true) -> "Next-Generation Digital Growth"
            params.category.contains("SaaS", true) -> "AI-Powered Platform"
            params.category.contains("Restaurant", true) -> "Fresh Ingredients & Culinary Heritage"
            params.category.contains("Portfolio", true) -> "Design & Engineering Portfolio"
            params.category.contains("Coaching", true) -> "Executive Leadership Accelerator"
            params.category.contains("Real Estate", true) -> "Prime Architectural Living"
            params.category.contains("Ecommerce", true) -> "Curated Modern Storefront"
            else -> "Premium Digital Experience"
        }

        val heroTitle = when {
            params.category.contains("Agency", true) -> "Scale Your Revenue With Performance & High-Impact Creative"
            params.category.contains("SaaS", true) -> "Automate Everything With Intelligent Cloud Workflows"
            params.category.contains("Restaurant", true) -> "Exceptional Flavors, Handcrafted Every Single Day"
            params.category.contains("Portfolio", true) -> "Crafting Digital Products That Shape Industries"
            params.category.contains("Coaching", true) -> "Unlock Peak Leadership & Scale Your Potential"
            params.category.contains("Real Estate", true) -> "Discover Bespoke Residences Built For Extraordinary Living"
            params.category.contains("Ecommerce", true) -> "Thoughtfully Designed Essentials For Everyday Life"
            else -> "Transforming Visions Into High-Performing Websites"
        }

        val heroSubtitle = params.description.ifBlank {
            "Delivering unmatched quality, modern design aesthetics, and measurable impact for our clients worldwide."
        }

        sections.add(
            WebsiteSection(
                id = "sec_${UUID.randomUUID().toString().take(6)}",
                type = SectionType.HERO,
                badgeText = heroBadge,
                title = heroTitle,
                subtitle = heroSubtitle,
                primaryButtonText = if (params.category.contains("Ecommerce", true)) "Explore Catalog" else "Get Started Free",
                primaryButtonUrl = "#pricing",
                secondaryButtonText = "Learn More",
                secondaryButtonUrl = "#features"
            )
        )

        // 3. Stats Section
        sections.add(
            WebsiteSection(
                id = "sec_${UUID.randomUUID().toString().take(6)}",
                type = SectionType.STATS,
                items = listOf(
                    SectionItem("st_1", "Global Clients Served", "", badge = "1,500+"),
                    SectionItem("st_2", "Client Satisfaction", "", badge = "99.4%"),
                    SectionItem("st_3", "Uptime & Reliability", "", badge = "99.99%"),
                    SectionItem("st_4", "Years of Excellence", "", badge = "10+")
                )
            )
        )

        // 4. Features or Services
        if (params.features.contains("Features") || params.features.contains("Services")) {
            val featureItems = generateFeatureItems(params.category)
            sections.add(
                WebsiteSection(
                    id = "sec_${UUID.randomUUID().toString().take(6)}",
                    type = SectionType.FEATURES,
                    badgeText = "Why Choose Us",
                    title = "Engineered For Peak Performance",
                    subtitle = "Everything you need to stand out, convert users, and accelerate growth.",
                    items = featureItems
                )
            )
        }

        // 5. Portfolio or Gallery if relevant
        if (params.features.contains("Gallery") || params.category.contains("Portfolio", true) || params.category.contains("Real Estate", true)) {
            sections.add(
                WebsiteSection(
                    id = "sec_${UUID.randomUUID().toString().take(6)}",
                    type = SectionType.PORTFOLIO_GALLERY,
                    title = "Selected Showcase",
                    subtitle = "Explore our latest projects and high-impact releases.",
                    items = listOf(
                        SectionItem("gl_1", "Project Alpha", "Award-winning responsive digital platform for FinTech leaders.", imageUrl = "https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=600&auto=format&fit=crop"),
                        SectionItem("gl_2", "Project Beta", "Full-stack mobile experience with 4.9 rating on App Store.", imageUrl = "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=600&auto=format&fit=crop"),
                        SectionItem("gl_3", "Project Gamma", "Brand transformation resulting in 2.5x higher conversions.", imageUrl = "https://images.unsplash.com/photo-1507238691740-187a5b1d37b8?w=600&auto=format&fit=crop")
                    )
                )
            )
        }

        // 6. Pricing if selected
        if (params.features.contains("Pricing") || params.category.contains("SaaS", true) || params.category.contains("Agency", true)) {
            sections.add(
                WebsiteSection(
                    id = "sec_${UUID.randomUUID().toString().take(6)}",
                    type = SectionType.PRICING,
                    title = "Transparent, Predictable Plans",
                    subtitle = "Choose the plan that fits your current stage and scale effortlessly.",
                    items = listOf(
                        SectionItem("p1", "Starter", "Ideal for individual creators and early stage experiments.", price = "₹1,999", badge = "Starter", linkUrl = "#contact"),
                        SectionItem("p2", "Professional", "Complete toolset with dedicated priority support and analytics.", price = "₹4,999", badge = "Most Popular", linkUrl = "#contact"),
                        SectionItem("p3", "Enterprise", "Custom integrations, unlimited capacity, and SLA guarantees.", price = "₹14,999", badge = "Enterprise", linkUrl = "#contact")
                    )
                )
            )
        }

        // 7. Testimonials
        if (params.features.contains("Testimonials")) {
            sections.add(
                WebsiteSection(
                    id = "sec_${UUID.randomUUID().toString().take(6)}",
                    type = SectionType.TESTIMONIALS,
                    title = "Trusted by Leaders Worldwide",
                    subtitle = "Here is what our partners and clients have to say.",
                    items = listOf(
                        SectionItem("t1", "Sameer Kapoor", "Switching to $cleanName was the single best decision for our digital presence. Flawless execution.", badge = "Founder, NextWave"),
                        SectionItem("t2", "Elena Rostova", "Unmatched design quality, lightning fast speed, and incredible attention to detail.", badge = "Product Lead, ApexGlobal")
                    )
                )
            )
        }

        // 8. FAQ
        if (params.features.contains("FAQ")) {
            sections.add(
                WebsiteSection(
                    id = "sec_${UUID.randomUUID().toString().take(6)}",
                    type = SectionType.FAQ,
                    title = "Got Questions? We Have Answers.",
                    subtitle = "Quick answers to common questions about our services and process.",
                    items = listOf(
                        SectionItem("q1", "How quickly can we get started?", "You can launch immediately! Our systems are built for fast onboarding within 24 to 48 hours."),
                        SectionItem("q2", "Can we customize and scale as we grow?", "Yes! You have full control over branding, content, custom domains, and integrations."),
                        SectionItem("q3", "What support is included?", "All plans include responsive email and live chat support, with dedicated managers on higher tiers.")
                    )
                )
            )
        }

        // 9. Contact Form
        if (params.features.contains("Contact Form") || params.features.contains("Contact")) {
            sections.add(
                WebsiteSection(
                    id = "sec_${UUID.randomUUID().toString().take(6)}",
                    type = SectionType.CONTACT_FORM,
                    title = "Let's Start a Conversation",
                    subtitle = "Have a question or looking to collaborate? Send us a message and our team will get back to you shortly."
                )
            )
        }

        // 10. Footer
        sections.add(
            WebsiteSection(
                id = "sec_${UUID.randomUUID().toString().take(6)}",
                type = SectionType.FOOTER
            )
        )

        val pages = mutableListOf<WebsitePage>()
        pages.add(
            WebsitePage(
                id = "pg_${UUID.randomUUID().toString().take(6)}",
                title = "Home",
                slug = "home",
                isHome = true,
                sections = sections
            )
        )

        // Add additional pages if requested
        if (params.numPages > 1) {
            pages.add(
                WebsitePage(
                    id = "pg_${UUID.randomUUID().toString().take(6)}",
                    title = "About Us",
                    slug = "about",
                    isHome = false,
                    sections = listOf(
                        WebsiteSection(id = "sec_${UUID.randomUUID().toString().take(6)}", type = SectionType.NAVBAR, title = cleanName),
                        WebsiteSection(
                            id = "sec_${UUID.randomUUID().toString().take(6)}",
                            type = SectionType.HERO,
                            badgeText = "Our Story",
                            title = "We Are On A Mission To Redefine Digital Excellence",
                            subtitle = "Founded with the ambition to combine cutting-edge technology with human-centric design."
                        ),
                        WebsiteSection(
                            id = "sec_${UUID.randomUUID().toString().take(6)}",
                            type = SectionType.FEATURES,
                            title = "Our Core Values",
                            subtitle = "Guiding principles that drive every solution we build.",
                            items = listOf(
                                SectionItem("v1", "Integrity & Transparency", "Open communication, honest timelines, and zero surprises.", iconName = "🤝"),
                                SectionItem("v2", "Relentless Quality", "Pixel-perfect engineering and verified performance benchmarks.", iconName = "⭐"),
                                SectionItem("v3", "Client-First Obsession", "We measure our success strictly by the compounding growth of our clients.", iconName = "🚀")
                            )
                        ),
                        WebsiteSection(id = "sec_${UUID.randomUUID().toString().take(6)}", type = SectionType.FOOTER)
                    )
                )
            )
        }

        return WebsiteProject(
            id = projectId,
            name = cleanName,
            category = params.category,
            description = params.description.ifBlank { "Modern web presence powered by SITEGEN AI." },
            themeStyle = params.themeStyle,
            primaryColorHex = params.primaryColorHex,
            secondaryColorHex = params.secondaryColorHex,
            subdomain = subdomain,
            status = ProjectStatus.DRAFT,
            seoTitle = "$cleanName | ${params.category} Official Website",
            seoDescription = params.description.ifBlank { "Discover $cleanName - premier ${params.category} solutions." },
            pages = pages
        )
    }

    private fun generateFeatureItems(category: String): List<SectionItem> {
        return when {
            category.contains("Agency", true) -> listOf(
                SectionItem("af1", "Strategic Growth Audits", "Data-backed teardowns to unlock hidden revenue opportunities in your funnel.", iconName = "📊"),
                SectionItem("af2", "High-Converting Creative", "Video hooks, interactive landing pages, and cohesive visual branding.", iconName = "🎨"),
                SectionItem("af3", "Multi-Channel Media Buying", "Scalable ad operations across Meta, Google, and LinkedIn with predictive attribution.", iconName = "📈"),
                SectionItem("af4", "Retention & Email CRM", "Automated customer journeys that maximize lifetime value and repeat sales.", iconName = "💌")
            )
            category.contains("SaaS", true) -> listOf(
                SectionItem("sf1", "Sub-Second Response", "Built on globally distributed edge networks for lightning-fast speed.", iconName = "⚡"),
                SectionItem("sf2", "Deep API Integrations", "Connect directly with 100+ tools including Slack, Zapier, and GitHub.", iconName = "🔗"),
                SectionItem("sf3", "Enterprise Compliance", "SOC2 Type II certified and GDPR ready out of the box.", iconName = "🛡️"),
                SectionItem("sf4", "Predictive AI Engine", "Automated insights and anomaly alerts sent directly to your team.", iconName = "🧠")
            )
            category.contains("Restaurant", true) -> listOf(
                SectionItem("rf1", "Farm-to-Table Ingredients", "Sourced daily from certified local organic growers.", iconName = "🌿"),
                SectionItem("rf2", "Authentic Heritage Recipes", "Master culinary techniques passed down through generations.", iconName = "🍳"),
                SectionItem("rf3", "Craft Cocktail Bar", "Bespoke botanical infusions and natural biodynamic wines.", iconName = "🍸"),
                SectionItem("rf4", "Private Dining & Events", "Intimate ambiance curated for celebrations and corporate dinners.", iconName = "✨")
            )
            else -> listOf(
                SectionItem("gf1", "Modern Responsive Design", "Flawless rendering on mobile, tablet, and desktop screens.", iconName = "📱"),
                SectionItem("gf2", "Search Engine Optimized", "Pre-configured meta tags, schema markup, and sitemaps for top rankings.", iconName = "🔍"),
                SectionItem("gf3", "Blazing Fast Delivery", "Optimized assets and clean semantic code for 99+ PageSpeed scores.", iconName = "🚀"),
                SectionItem("gf4", "24/7 Dedicated Support", "Our expert engineering team is always on hand to assist you.", iconName = "💬")
            )
        }
    }

    fun applyAiModification(project: WebsiteProject, instruction: String): WebsiteProject {
        val lower = instruction.lowercase()
        var updated = project

        // Color updates
        if (lower.contains("purple") || lower.contains("violet")) {
            updated = updated.copy(primaryColorHex = "#8B5CF6", secondaryColorHex = "#C084FC")
        } else if (lower.contains("emerald") || lower.contains("green")) {
            updated = updated.copy(primaryColorHex = "#10B981", secondaryColorHex = "#34D399")
        } else if (lower.contains("amber") || lower.contains("gold") || lower.contains("yellow")) {
            updated = updated.copy(primaryColorHex = "#F59E0B", secondaryColorHex = "#FBBF24")
        } else if (lower.contains("crimson") || lower.contains("rose") || lower.contains("red")) {
            updated = updated.copy(primaryColorHex = "#F43F5E", secondaryColorHex = "#FB7185")
        } else if (lower.contains("blue") || lower.contains("cyan")) {
            updated = updated.copy(primaryColorHex = "#0EA5E9", secondaryColorHex = "#06B6D4")
        }

        // Theme updates
        if (lower.contains("dark") || lower.contains("cyber")) {
            updated = updated.copy(themeStyle = "Modern Cyber")
        } else if (lower.contains("light") || lower.contains("minimal")) {
            updated = updated.copy(themeStyle = "Minimalist Light")
        }

        // Update pages
        val updatedPages = updated.pages.map { page ->
            val updatedSections = page.sections.toMutableList()

            // If instruction says add FAQ
            if (lower.contains("add faq") && updatedSections.none { it.type == SectionType.FAQ }) {
                val insertIndex = (updatedSections.size - 2).coerceAtLeast(1)
                updatedSections.add(
                    insertIndex,
                    WebsiteSection(
                        id = "sec_faq_${UUID.randomUUID().toString().take(6)}",
                        type = SectionType.FAQ,
                        title = "Frequently Asked Questions",
                        subtitle = "Common inquiries answered by our team.",
                        items = listOf(
                            SectionItem("fq_ai_1", "What is the onboarding process?", "You can get started in less than 5 minutes with our guided wizard."),
                            SectionItem("fq_ai_2", "Are there any long-term lock-in contracts?", "No, you can upgrade, downgrade, or cancel anytime with zero penalty.")
                        )
                    )
                )
            }

            // If instruction says add pricing
            if (lower.contains("add pricing") && updatedSections.none { it.type == SectionType.PRICING }) {
                val insertIndex = (updatedSections.size - 2).coerceAtLeast(1)
                updatedSections.add(
                    insertIndex,
                    WebsiteSection(
                        id = "sec_pr_${UUID.randomUUID().toString().take(6)}",
                        type = SectionType.PRICING,
                        title = "Straightforward Pricing",
                        subtitle = "Select your tier and unlock complete capabilities.",
                        items = listOf(
                            SectionItem("pr_ai_1", "Base", "Essential features to get online.", price = "₹1,499", badge = "Starter"),
                            SectionItem("pr_ai_2", "Scale", "Advanced power, custom domain & analytics.", price = "₹3,999", badge = "Popular")
                        )
                    )
                )
            }

            // If instruction mentions rewriting hero
            if (lower.contains("rewrite hero") || lower.contains("headline") || lower.contains("hero")) {
                val heroIdx = updatedSections.indexOfFirst { it.type == SectionType.HERO }
                if (heroIdx != -1) {
                    val existingHero = updatedSections[heroIdx]
                    val newHeadline = if (lower.contains("b2b") || lower.contains("enterprise")) {
                        "Enterprise-Grade Digital Solutions That Drive Compounding Value"
                    } else if (lower.contains("ai") || lower.contains("intelligent")) {
                        "Empowering The Future With Autonomous AI Workflows"
                    } else {
                        "Next-Generation Performance & High-Impact Results"
                    }
                    updatedSections[heroIdx] = existingHero.copy(
                        title = newHeadline,
                        subtitle = "Engineered with precision to elevate your brand authority and maximize growth."
                    )
                }
            }

            // If instruction mentions SEO
            if (lower.contains("seo")) {
                updated = updated.copy(
                    seoTitle = "${updated.name} | Official Website - High Performance & Growth",
                    seoDescription = "Official website of ${updated.name}. Explore premier solutions, client reviews, and transparent pricing."
                )
            }

            page.copy(sections = updatedSections)
        }

        return updated.copy(
            pages = updatedPages,
            lastModified = System.currentTimeMillis()
        )
    }
}
