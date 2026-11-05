package com.example.data.generator

import com.example.data.model.ProjectStatus
import com.example.data.model.SectionItem
import com.example.data.model.SectionType
import com.example.data.model.WebsitePage
import com.example.data.model.WebsiteProject
import com.example.data.model.WebsiteSection

object TemplatesCatalog {

    val templates: List<WebsiteProject> by lazy {
        listOf(
            createAgencyTemplate(),
            createSaasTemplate(),
            createPortfolioTemplate(),
            createRestaurantTemplate(),
            createCoachingTemplate(),
            createEcommerceTemplate(),
            createRealEstateTemplate(),
            createTechBlogTemplate(),
            createEventTemplate(),
            createBioLinkTemplate()
        )
    }

    private fun createAgencyTemplate(): WebsiteProject {
        return WebsiteProject(
            id = "tpl_agency_delhi",
            name = "Apex Wave Digital",
            category = "Agency",
            description = "Full-service digital growth, paid ads, SEO, and creative studio for modern brands.",
            themeStyle = "Modern Cyber",
            primaryColorHex = "#6366F1",
            secondaryColorHex = "#06B6D4",
            subdomain = "apexwave.sitegen.page",
            status = ProjectStatus.PUBLISHED,
            seoTitle = "Apex Wave Digital | Growth & Marketing Agency",
            seoDescription = "We scale ambitious D2C and B2B brands with revenue-focused performance marketing and high-converting websites.",
            pages = listOf(
                WebsitePage(
                    id = "pg_agency_home",
                    title = "Home",
                    slug = "home",
                    isHome = true,
                    sections = listOf(
                        WebsiteSection(
                            id = "sec_ag_nav",
                            type = SectionType.NAVBAR,
                            title = "Apex Wave",
                            primaryButtonText = "Book Discovery Call",
                            primaryButtonUrl = "#contact"
                        ),
                        WebsiteSection(
                            id = "sec_ag_hero",
                            type = SectionType.HERO,
                            badgeText = "Voted #1 Growth Agency 2026",
                            title = "We Scale Brands With Performance Marketing & Design",
                            subtitle = "Delivering 4.8x average ROAS for enterprise brands across India, UAE, and North America with AI-driven growth systems.",
                            primaryButtonText = "View Case Studies",
                            primaryButtonUrl = "#services",
                            secondaryButtonText = "Get Free Audit",
                            secondaryButtonUrl = "#contact"
                        ),
                        WebsiteSection(
                            id = "sec_ag_stats",
                            type = SectionType.STATS,
                            items = listOf(
                                SectionItem("st1", "Client Revenue Generated", "Verified", badge = "₹120Cr+"),
                                SectionItem("st2", "Average Client ROAS", "Industry-leading", badge = "4.8x"),
                                SectionItem("st3", "Active Client Retention", "Year over year", badge = "94%"),
                                SectionItem("st4", "Global Brand Partners", "Across 14 countries", badge = "85+")
                            )
                        ),
                        WebsiteSection(
                            id = "sec_ag_services",
                            type = SectionType.SERVICES,
                            title = "Growth Capabilities",
                            subtitle = "Engineered to capture demand, convert traffic, and build compounding brand equity.",
                            items = listOf(
                                SectionItem("sv1", "Performance Paid Media", "Meta, Google, and TikTok Ads engineered with predictive creative optimization.", linkUrl = "#contact"),
                                SectionItem("sv2", "Conversion Rate Optimization", "Full-funnel landing page teardowns and split testing delivering +35% lift.", linkUrl = "#contact"),
                                SectionItem("sv3", "SEO & Content Authority", "Programmatic and editorial SEO frameworks that dominate high-intent keywords.", linkUrl = "#contact"),
                                SectionItem("sv4", "Creative Brand Design", "High-fidelity 3D motion assets, viral video hooks, and cohesive digital design systems.", linkUrl = "#contact")
                            )
                        ),
                        WebsiteSection(
                            id = "sec_ag_pricing",
                            type = SectionType.PRICING,
                            title = "Retainer Plans",
                            subtitle = "Transparent tiers tailored for ambitious scale.",
                            items = listOf(
                                SectionItem("pr1", "Growth Starter", "Up to ₹5L monthly ad spend. Dedicated campaign manager & weekly reporting.", price = "₹45,000", badge = "Starter"),
                                SectionItem("pr2", "Scale Engine", "Up to ₹20L monthly ad spend. Full creative studio, daily optimization, dedicated Slack.", price = "₹95,000", badge = "Most Popular"),
                                SectionItem("pr3", "Enterprise Scale", "Unlimited ad spend, omni-channel dominance, custom AI attribution models.", price = "₹1,80,000", badge = "Enterprise")
                            )
                        ),
                        WebsiteSection(
                            id = "sec_ag_testimonials",
                            type = SectionType.TESTIMONIALS,
                            title = "Client Success",
                            subtitle = "What founders and CMOs say about working with Apex Wave.",
                            items = listOf(
                                SectionItem("ts1", "Rohit Verma", "Apex Wave tripled our D2C monthly revenue in 90 days while slashing CAC by 42%.", badge = "Founder, UrbanPulse Lifestyle"),
                                SectionItem("ts2", "Sarah Jenkins", "Their technical SEO and high-converting landing pages made us #1 in our SaaS category.", badge = "VP Marketing, FlowStack US")
                            )
                        ),
                        WebsiteSection(
                            id = "sec_ag_contact",
                            type = SectionType.CONTACT_FORM,
                            title = "Ready To Multiply Your Revenue?",
                            subtitle = "Submit your brand details below and get a free 15-minute growth diagnostic within 24 hours."
                        ),
                        WebsiteSection(
                            id = "sec_ag_footer",
                            type = SectionType.FOOTER
                        )
                    )
                )
            )
        )
    }

    private fun createSaasTemplate(): WebsiteProject {
        return WebsiteProject(
            id = "tpl_saas_cloudpulse",
            name = "CloudPulse Analytics",
            category = "SaaS",
            description = "Real-time observability and anomaly detection for modern distributed cloud architectures.",
            themeStyle = "Modern Cyber",
            primaryColorHex = "#3B82F6",
            secondaryColorHex = "#10B981",
            subdomain = "cloudpulse.sitegen.page",
            status = ProjectStatus.PUBLISHED,
            seoTitle = "CloudPulse | Zero-Config Cloud Observability",
            seoDescription = "Monitor Kubernetes, serverless functions, and microservices in real time with sub-second latency.",
            pages = listOf(
                WebsitePage(
                    id = "pg_saas_home",
                    title = "Home",
                    slug = "home",
                    isHome = true,
                    sections = listOf(
                        WebsiteSection(
                            id = "sec_saas_nav",
                            type = SectionType.NAVBAR,
                            title = "CloudPulse",
                            primaryButtonText = "Start Free Trial",
                            primaryButtonUrl = "#pricing"
                        ),
                        WebsiteSection(
                            id = "sec_saas_hero",
                            type = SectionType.HERO,
                            badgeText = "Next-Gen Observability Engine",
                            title = "Detect Outages Before Your Customers Do",
                            subtitle = "Zero-overhead eBPF agents deliver deep distributed telemetry, instant root cause analysis, and automatic incident remediations in seconds.",
                            primaryButtonText = "Deploy in 5 Minutes",
                            primaryButtonUrl = "#pricing",
                            secondaryButtonText = "Interactive Sandbox",
                            secondaryButtonUrl = "#features"
                        ),
                        WebsiteSection(
                            id = "sec_saas_features",
                            type = SectionType.FEATURES,
                            badgeText = "Core Engine",
                            title = "Built For High-Velocity Engineering",
                            subtitle = "Eliminate blind spots with auto-instrumented tracing, metric aggregation, and AI anomaly triage.",
                            items = listOf(
                                SectionItem("f1", "Instant eBPF Tracing", "Zero code modification required. Run our one-line daemonset across any Linux or Kubernetes cluster.", iconName = "⚡"),
                                SectionItem("f2", "Automated Root Cause", "AI correlation engine identifies the offending commit, degraded DB query, or pod memory leak immediately.", iconName = "🎯"),
                                SectionItem("f3", "Real-Time Edge Alerts", "Route high-priority incidents directly to PagerDuty, Slack, OpsGenie, or Webhooks with sub-second delivery.", iconName = "🔔"),
                                SectionItem("f4", "SOC2 & HIPAA Compliant", "End-to-end encrypted telemetry with zero customer PII storage and customizable data retention windows.", iconName = "🔒")
                            )
                        ),
                        WebsiteSection(
                            id = "sec_saas_pricing",
                            type = SectionType.PRICING,
                            title = "Simple, Predictable Cloud Pricing",
                            subtitle = "No hidden host fees or ingest overage surprises.",
                            items = listOf(
                                SectionItem("sp1", "Developer", "Up to 5 nodes and 50GB ingestion. 7 days retention, community Discord support.", price = "$29", badge = "Free 14d Trial"),
                                SectionItem("sp2", "Scale Pro", "Up to 25 nodes and 500GB ingestion. 30 days retention, automated AI root cause, Slack alerts.", price = "$99", badge = "Recommended"),
                                SectionItem("sp3", "Enterprise Grid", "Unlimited nodes, custom VPC peering, SSO SAML, 99.99% SLA, dedicated solution architect.", price = "$299", badge = "Enterprise")
                            )
                        ),
                        WebsiteSection(
                            id = "sec_saas_faq",
                            type = SectionType.FAQ,
                            title = "Frequently Asked Questions",
                            subtitle = "Everything you need to know about CloudPulse deployment and security.",
                            items = listOf(
                                SectionItem("fq1", "Does CloudPulse slow down production services?", "No. Our lightweight eBPF kernel probes operate with less than 0.4% CPU overhead, verified at scale."),
                                SectionItem("fq2", "Can we deploy CloudPulse in our private cloud?", "Yes! Enterprise tiers include self-hosted on-premise Helm charts or AWS/GCP marketplace private VPC integrations.")
                            )
                        ),
                        WebsiteSection(
                            id = "sec_saas_newsletter",
                            type = SectionType.NEWSLETTER,
                            title = "Stay Ahead of Distributed Systems",
                            subtitle = "Join 45,000+ DevOps and SRE leads reading our weekly reliability engineering newsletter."
                        ),
                        WebsiteSection(
                            id = "sec_saas_footer",
                            type = SectionType.FOOTER
                        )
                    )
                )
            )
        )
    }

    private fun createPortfolioTemplate(): WebsiteProject {
        return WebsiteProject(
            id = "tpl_portfolio_arjun",
            name = "Arjun Mehta Design",
            category = "Portfolio",
            description = "Principal Product Designer & Design Technologist specializing in fintech, AI interfaces, and design systems.",
            themeStyle = "Minimalist Light",
            primaryColorHex = "#18181B",
            secondaryColorHex = "#6366F1",
            subdomain = "arjunmehta.sitegen.page",
            status = ProjectStatus.PUBLISHED,
            seoTitle = "Arjun Mehta | Staff Product Designer",
            seoDescription = "Crafting category-defining product experiences for tech startups and enterprise leaders.",
            pages = listOf(
                WebsitePage(
                    id = "pg_pf_home",
                    title = "Home",
                    slug = "home",
                    isHome = true,
                    sections = listOf(
                        WebsiteSection(
                            id = "sec_pf_nav",
                            type = SectionType.NAVBAR,
                            title = "Arjun Mehta",
                            primaryButtonText = "Let's Talk",
                            primaryButtonUrl = "#contact"
                        ),
                        WebsiteSection(
                            id = "sec_pf_hero",
                            type = SectionType.HERO,
                            badgeText = "Available for Q1 2027 Advisory & Projects",
                            title = "Designing High-Impact Digital Products & Systems",
                            subtitle = "Over 10 years crafting intuitive user interfaces for 30M+ active users across fintech, AI productivity tools, and modern SaaS ecosystems.",
                            primaryButtonText = "Explore Selected Work",
                            primaryButtonUrl = "#gallery",
                            secondaryButtonText = "Read Design Essays",
                            secondaryButtonUrl = "#about"
                        ),
                        WebsiteSection(
                            id = "sec_pf_gallery",
                            type = SectionType.PORTFOLIO_GALLERY,
                            title = "Featured Case Studies",
                            subtitle = "A selection of end-to-end product designs and high-conversion systems.",
                            items = listOf(
                                SectionItem("cs1", "FinScale Mobile Trading", "Redesigned mobile trading flow resulting in 48% higher day-1 active retention.", imageUrl = "https://images.unsplash.com/photo-1551288049-bebda4e38f71?w=600&auto=format&fit=crop"),
                                SectionItem("cs2", "Nova AI Workspace", "Spatial canvas editor and design token generator for 200,000 design teams.", imageUrl = "https://images.unsplash.com/photo-1507238691740-187a5b1d37b8?w=600&auto=format&fit=crop"),
                                SectionItem("cs3", "Starlight Design System", "Unified multi-brand component library powering 14 global web applications.", imageUrl = "https://images.unsplash.com/photo-1581291518857-4e27b48ff24e?w=600&auto=format&fit=crop")
                            )
                        ),
                        WebsiteSection(
                            id = "sec_pf_contact",
                            type = SectionType.CONTACT_FORM,
                            title = "Let's Build Something Memorable",
                            subtitle = "Looking for product strategy, UX leadership, or design system consultation? Send a note below."
                        ),
                        WebsiteSection(
                            id = "sec_pf_footer",
                            type = SectionType.FOOTER
                        )
                    )
                )
            )
        )
    }

    private fun createRestaurantTemplate(): WebsiteProject {
        return WebsiteProject(
            id = "tpl_rest_bellatavola",
            name = "Bella Tavola Bistro",
            category = "Restaurant",
            description = "Artisanal wood-fired cuisine, handcrafted fresh pastas, and organic natural wines in New Delhi.",
            themeStyle = "Warm Organic",
            primaryColorHex = "#EA580C",
            secondaryColorHex = "#D97706",
            subdomain = "bellatavola.sitegen.page",
            status = ProjectStatus.PUBLISHED,
            seoTitle = "Bella Tavola | Artisanal Italian Bistro & Wine Bar",
            seoDescription = "Experience slow-cooked authentic Italian gastronomy with farm-fresh organic ingredients.",
            pages = listOf(
                WebsitePage(
                    id = "pg_res_home",
                    title = "Home",
                    slug = "home",
                    isHome = true,
                    sections = listOf(
                        WebsiteSection(
                            id = "sec_res_nav",
                            type = SectionType.NAVBAR,
                            title = "Bella Tavola",
                            primaryButtonText = "Reserve a Table",
                            primaryButtonUrl = "#contact"
                        ),
                        WebsiteSection(
                            id = "sec_res_hero",
                            type = SectionType.HERO,
                            badgeText = "Michelin Guide Recommended 2026",
                            title = "Handmade Pastas & Wood-Fired Heritage",
                            subtitle = "Celebrating timeless Italian traditions with locally sourced organic produce, sourdough bread, and biodynamic natural wines.",
                            primaryButtonText = "View Seasonal Menu",
                            primaryButtonUrl = "#features",
                            secondaryButtonText = "Private Dining",
                            secondaryButtonUrl = "#contact"
                        ),
                        WebsiteSection(
                            id = "sec_res_features",
                            type = SectionType.FEATURES,
                            title = "The Experience",
                            subtitle = "Every plate reflects our obsession with artisanal technique.",
                            items = listOf(
                                SectionItem("rf1", "Daily Hand-Rolled Pastas", "Prepared each morning using Italian durum semolina and pasture-raised eggs.", iconName = "🍝"),
                                SectionItem("rf2", "Wood-Fired Neapolitan Oven", "48-hour fermented sourdough fired at 450°C for airy, blistered crusts.", iconName = "🍕"),
                                SectionItem("rf3", "Curated Natural Cellar", "Over 120 biodynamic wines handpicked from boutique European vineyards.", iconName = "🍷")
                            )
                        ),
                        WebsiteSection(
                            id = "sec_res_contact",
                            type = SectionType.CONTACT_FORM,
                            title = "Book Your Evening",
                            subtitle = "Reservations recommended for dinner service. For private events over 10 guests, please specify in the note."
                        ),
                        WebsiteSection(
                            id = "sec_res_footer",
                            type = SectionType.FOOTER
                        )
                    )
                )
            )
        )
    }

    private fun createCoachingTemplate(): WebsiteProject {
        return WebsiteProject(
            id = "tpl_coach_ascend",
            name = "Ascend Executive Mastery",
            category = "Coaching",
            description = "High-performance leadership coaching for founders, C-suite executives, and venture partners.",
            themeStyle = "Emerald Luxury",
            primaryColorHex = "#059669",
            secondaryColorHex = "#0D9488",
            subdomain = "ascendcoaching.sitegen.page",
            status = ProjectStatus.PUBLISHED,
            seoTitle = "Ascend Coaching | Executive & Founder Coaching",
            seoDescription = "Master high-stakes decision making, team scaling, and sustainable executive energy.",
            pages = listOf(
                WebsitePage(
                    id = "pg_coach_home",
                    title = "Home",
                    slug = "home",
                    isHome = true,
                    sections = listOf(
                        WebsiteSection(
                            id = "sec_co_nav",
                            type = SectionType.NAVBAR,
                            title = "Ascend Mastery",
                            primaryButtonText = "Apply For Coaching",
                            primaryButtonUrl = "#contact"
                        ),
                        WebsiteSection(
                            id = "sec_co_hero",
                            type = SectionType.HERO,
                            badgeText = "Confidential Advisory for Top 1% Leaders",
                            title = "Master The Pressure of Scaling From $10M to $100M",
                            subtitle = "Transform your leadership psychology, executive presence, and strategic focus with 1-on-1 sparring designed for venture-backed founders.",
                            primaryButtonText = "Schedule Confidential Call",
                            primaryButtonUrl = "#contact",
                            secondaryButtonText = "Read Methodology",
                            secondaryButtonUrl = "#services"
                        ),
                        WebsiteSection(
                            id = "sec_co_services",
                            type = SectionType.SERVICES,
                            title = "Coaching Tracks",
                            subtitle = "Tailored engagements for different inflection points in your career.",
                            items = listOf(
                                SectionItem("ct1", "The Scale Founder Track", "Navigating Series B+ organizational scaling, board dynamics, and delegation systems."),
                                SectionItem("ct2", "C-Suite Executive Presence", "Command the room with strategic clarity, high-stakes communication, and executive poise."),
                                SectionItem("ct3", "Mental Resilience & Longevity", "Combat burnout, align cognitive bandwidth, and achieve sustained high performance.")
                            )
                        ),
                        WebsiteSection(
                            id = "sec_co_testimonials",
                            type = SectionType.TESTIMONIALS,
                            title = "Client Endorsements",
                            subtitle = "Trusted by founders backed by Sequoia, Lightspeed, and Y Combinator.",
                            items = listOf(
                                SectionItem("c_t1", "Ananya Singhal", "Working with Ascend gave me the exact mental clarity needed to scale our team from 40 to 300.", badge = "CEO, Fintech Unicorn"),
                                SectionItem("c_t2", "Marcus Vance", "The single highest ROI investment I've made in my 12 years as an operator.", badge = "Managing Director, Global VC")
                            )
                        ),
                        WebsiteSection(
                            id = "sec_co_contact",
                            type = SectionType.CONTACT_FORM,
                            title = "Inquire For Private Mentorship",
                            subtitle = "We accept 5 new executives per quarter to ensure deep personal engagement."
                        ),
                        WebsiteSection(
                            id = "sec_co_footer",
                            type = SectionType.FOOTER
                        )
                    )
                )
            )
        )
    }

    private fun createEcommerceTemplate(): WebsiteProject {
        return WebsiteProject(
            id = "tpl_ecom_nordic",
            name = "Nordic Haven Essentials",
            category = "Ecommerce",
            description = "Minimalist Scandinavian home decor, sustainable ceramics, and organic linen textiles.",
            themeStyle = "Clean Minimal",
            primaryColorHex = "#475569",
            secondaryColorHex = "#0EA5E9",
            subdomain = "nordichaven.sitegen.page",
            status = ProjectStatus.PUBLISHED,
            seoTitle = "Nordic Haven | Sustainable Scandinavian Living",
            seoDescription = "Thoughtfully crafted ceramics, lighting, and textiles designed for calm spaces.",
            pages = listOf(
                WebsitePage(
                    id = "pg_ec_home",
                    title = "Home",
                    slug = "home",
                    isHome = true,
                    sections = listOf(
                        WebsiteSection(
                            id = "sec_ec_nav",
                            type = SectionType.NAVBAR,
                            title = "Nordic Haven",
                            primaryButtonText = "Shop Autumn Drop",
                            primaryButtonUrl = "#gallery"
                        ),
                        WebsiteSection(
                            id = "sec_ec_hero",
                            type = SectionType.HERO,
                            badgeText = "Autumn Drop 2026 Live",
                            title = "Minimalist Objects for Mindful Living Spaces",
                            subtitle = "Sculptural forms, natural earth pigments, and ethically crafted materials that bring tranquil harmony to your home.",
                            primaryButtonText = "Browse Collection",
                            primaryButtonUrl = "#gallery",
                            secondaryButtonText = "Our Story",
                            secondaryButtonUrl = "#features"
                        ),
                        WebsiteSection(
                            id = "sec_ec_gallery",
                            type = SectionType.PORTFOLIO_GALLERY,
                            title = "Curated Collection",
                            subtitle = "Small-batch releases crafted by independent master potters and weavers.",
                            items = listOf(
                                SectionItem("g1", "Terra Sand Ceramic Vase", "Hand-thrown stoneware with raw volcanic matte finish.", price = "₹3,400", imageUrl = "https://images.unsplash.com/photo-1612196808214-b8e1d6145a8c?w=600&auto=format&fit=crop"),
                                SectionItem("g2", "Koto Flax Linen Throw", "100% organic stonewashed European flax in natural oat hue.", price = "₹5,200", imageUrl = "https://images.unsplash.com/photo-1584100936595-c0654b55a2e2?w=600&auto=format&fit=crop"),
                                SectionItem("g3", "Aura Minimalist Table Lamp", "Brushed cast brass and frosted opaline glass sphere.", price = "₹8,900", imageUrl = "https://images.unsplash.com/photo-1507473885765-e6ed057f782c?w=600&auto=format&fit=crop")
                            )
                        ),
                        WebsiteSection(
                            id = "sec_ec_newsletter",
                            type = SectionType.NEWSLETTER,
                            title = "Receive Private Pre-Launch Access",
                            subtitle = "Subscribers receive 48-hour advance access to limited artisan drops and 15% off first order."
                        ),
                        WebsiteSection(
                            id = "sec_ec_footer",
                            type = SectionType.FOOTER
                        )
                    )
                )
            )
        )
    }

    private fun createRealEstateTemplate(): WebsiteProject {
        return WebsiteProject(
            id = "tpl_realestate_skyline",
            name = "Skyline Urban Estates",
            category = "Real Estate",
            description = "Exclusive luxury residences, penthouses, and bespoke architectural villas in prime metropolitan areas.",
            themeStyle = "Cyber Dark",
            primaryColorHex = "#D97706",
            secondaryColorHex = "#F59E0B",
            subdomain = "skylineestates.sitegen.page",
            status = ProjectStatus.PUBLISHED,
            seoTitle = "Skyline Estates | Prime Architectural Residences",
            seoDescription = "Discover ultra-luxury private penthouses and high-yield real estate investments.",
            pages = listOf(
                WebsitePage(
                    id = "pg_re_home",
                    title = "Home",
                    slug = "home",
                    isHome = true,
                    sections = listOf(
                        WebsiteSection(
                            id = "sec_re_nav",
                            type = SectionType.NAVBAR,
                            title = "Skyline Estates",
                            primaryButtonText = "Private Viewing",
                            primaryButtonUrl = "#contact"
                        ),
                        WebsiteSection(
                            id = "sec_re_hero",
                            type = SectionType.HERO,
                            badgeText = "Private Residences Collection",
                            title = "Unrivaled Architectural Luxury & City Panoramas",
                            subtitle = "Step into a sanctuary of custom Italian marble, floor-to-ceiling glass, private infinity pools, and panoramic skyline vistas.",
                            primaryButtonText = "Explore Residences",
                            primaryButtonUrl = "#gallery",
                            secondaryButtonText = "Download Brochure",
                            secondaryButtonUrl = "#contact"
                        ),
                        WebsiteSection(
                            id = "sec_re_stats",
                            type = SectionType.STATS,
                            items = listOf(
                                SectionItem("re_s1", "Prime Projects Delivered", "Over 20 years", badge = "48+"),
                                SectionItem("re_s2", "Average Capital Appreciation", "Over 5-year cycle", badge = "18% p.a."),
                                SectionItem("re_s3", "Private Club Amenities", "Curated for residents", badge = "35+"),
                                SectionItem("re_s4", "Acres of Green Canopy", "Private gated sanctuaries", badge = "120")
                            )
                        ),
                        WebsiteSection(
                            id = "sec_re_contact",
                            type = SectionType.CONTACT_FORM,
                            title = "Request a Private Tour",
                            subtitle = "Connect with our senior private wealth property director for confidential portfolio discussions."
                        ),
                        WebsiteSection(
                            id = "sec_re_footer",
                            type = SectionType.FOOTER
                        )
                    )
                )
            )
        )
    }

    private fun createTechBlogTemplate(): WebsiteProject {
        return WebsiteProject(
            id = "tpl_blog_kernel",
            name = "Kernel & Cloud Journal",
            category = "Blog",
            description = "Deep technical essays on distributed databases, WebAssembly, compiler design, and Rust engineering.",
            themeStyle = "Cyber Dark",
            primaryColorHex = "#8B5CF6",
            secondaryColorHex = "#06B6D4",
            subdomain = "kernelcode.sitegen.page",
            status = ProjectStatus.PUBLISHED,
            seoTitle = "Kernel & Cloud | Systems Architecture & Tech Journal",
            seoDescription = "Technical essays and benchmarks for senior software engineers building high-scale backends.",
            pages = listOf(
                WebsitePage(
                    id = "pg_bl_home",
                    title = "Home",
                    slug = "home",
                    isHome = true,
                    sections = listOf(
                        WebsiteSection(
                            id = "sec_bl_nav",
                            type = SectionType.NAVBAR,
                            title = "Kernel & Cloud",
                            primaryButtonText = "Subscribe RSS",
                            primaryButtonUrl = "#newsletter"
                        ),
                        WebsiteSection(
                            id = "sec_bl_hero",
                            type = SectionType.HERO,
                            badgeText = "Engineering Deep Dives",
                            title = "Deconstructing High-Scale Distributed Systems",
                            subtitle = "Practical benchmarks, lock-free concurrency algorithms, and production post-mortems for principal architects.",
                            primaryButtonText = "Read Latest Posts",
                            primaryButtonUrl = "#blog",
                            secondaryButtonText = "All Topics",
                            secondaryButtonUrl = "#blog"
                        ),
                        WebsiteSection(
                            id = "sec_bl_feed",
                            type = SectionType.BLOG_FEED,
                            title = "Recent Technical Papers",
                            subtitle = "Published bi-weekly with reproducible benchmarks.",
                            items = listOf(
                                SectionItem("bp1", "Why We Migrated From RocksDB To Custom NVMe B-Trees", "Achieving 4x IOPS throughput with zero garbage collection spikes in high-volume ingestion.", badge = "Databases"),
                                SectionItem("bp2", "Building Sub-Millisecond Event Loops in Rust", "A deep dive into io_uring, epoll nuances, and cache-friendly data structures.", badge = "Rust & OS"),
                                SectionItem("bp3", "The Hidden Latency of Distributed Consensus", "Evaluating Raft vs Paxos variations across geo-replicated multi-region clusters.", badge = "Architecture")
                            )
                        ),
                        WebsiteSection(
                            id = "sec_bl_newsletter",
                            type = SectionType.NEWSLETTER,
                            title = "Join 38,000+ Senior Engineers",
                            subtitle = "Get our unfiltered technical essays delivered to your inbox every alternate Tuesday."
                        ),
                        WebsiteSection(
                            id = "sec_bl_footer",
                            type = SectionType.FOOTER
                        )
                    )
                )
            )
        )
    }

    private fun createEventTemplate(): WebsiteProject {
        return WebsiteProject(
            id = "tpl_event_summit",
            name = "NextGen AI Summit 2026",
            category = "Event",
            description = "The premier international gathering of AI researchers, foundation model architects, and technology executives.",
            themeStyle = "Modern Cyber",
            primaryColorHex = "#EC4899",
            secondaryColorHex = "#8B5CF6",
            subdomain = "nextgenai.sitegen.page",
            status = ProjectStatus.PUBLISHED,
            seoTitle = "NextGen AI Summit 2026 | Tokyo & Virtual",
            seoDescription = "3 Days, 80+ Keynotes, 5,000+ Engineers exploring the frontier of multimodal intelligence.",
            pages = listOf(
                WebsitePage(
                    id = "pg_ev_home",
                    title = "Home",
                    slug = "home",
                    isHome = true,
                    sections = listOf(
                        WebsiteSection(
                            id = "sec_ev_nav",
                            type = SectionType.NAVBAR,
                            title = "NextGen AI '26",
                            primaryButtonText = "Register Pass",
                            primaryButtonUrl = "#pricing"
                        ),
                        WebsiteSection(
                            id = "sec_ev_hero",
                            type = SectionType.HERO,
                            badgeText = "November 14-16, 2026 • Tokyo & Live Stream",
                            title = "Where The Global AI Community Defines The Future",
                            subtitle = "Join 5,000+ engineers, researchers, and venture leaders discussing next-generation reasoning models, autonomous agents, and robotics.",
                            primaryButtonText = "Secure Early Bird Pass",
                            primaryButtonUrl = "#pricing",
                            secondaryButtonText = "View Speaker Lineup",
                            secondaryButtonUrl = "#features"
                        ),
                        WebsiteSection(
                            id = "sec_ev_pricing",
                            type = SectionType.PRICING,
                            title = "Conference Passes",
                            subtitle = "Choose in-person Tokyo pass or global virtual live-stream access.",
                            items = listOf(
                                SectionItem("ep1", "Virtual Access", "Full 4K live stream access to all 3 tracks, replay recordings, and speaker Q&A chat.", price = "$199", badge = "Global"),
                                SectionItem("ep2", "Full Conference", "In-person access in Tokyo, workshop labs, networking dinners, and VIP speaker lounge.", price = "$699", badge = "Most Popular"),
                                SectionItem("ep3", "Executive VIP", "Includes private closed-door roundtables, 1-on-1 speaker intros, and concierge lounge.", price = "$1,499", badge = "Limited (100 seats)")
                            )
                        ),
                        WebsiteSection(
                            id = "sec_ev_footer",
                            type = SectionType.FOOTER
                        )
                    )
                )
            )
        )
    }

    private fun createBioLinkTemplate(): WebsiteProject {
        return WebsiteProject(
            id = "tpl_bio_mayalin",
            name = "Maya Lin Bio Link",
            category = "Personal",
            description = "Creative director, podcaster, and author sharing insights on aesthetics, design, and culture.",
            themeStyle = "Minimalist Light",
            primaryColorHex = "#0F172A",
            secondaryColorHex = "#6366F1",
            subdomain = "mayalin.sitegen.page",
            status = ProjectStatus.PUBLISHED,
            seoTitle = "Maya Lin | Official Bio & Links",
            seoDescription = "Books, weekly podcast episodes, photography prints, and creative advisory.",
            pages = listOf(
                WebsitePage(
                    id = "pg_bio_home",
                    title = "Home",
                    slug = "home",
                    isHome = true,
                    sections = listOf(
                        WebsiteSection(
                            id = "sec_bio_hero",
                            type = SectionType.HERO,
                            badgeText = "Creator & Author",
                            title = "Maya Lin",
                            subtitle = "Exploring design, technology philosophy, and aesthetic culture. Host of The Creative Horizon Podcast (2M+ listens).",
                            primaryButtonText = "Listen To Podcast",
                            primaryButtonUrl = "#contact",
                            secondaryButtonText = "Order My New Book",
                            secondaryButtonUrl = "#pricing"
                        ),
                        WebsiteSection(
                            id = "sec_bio_services",
                            type = SectionType.SERVICES,
                            title = "Official Links & Projects",
                            subtitle = "Everything happening across my ecosystem.",
                            items = listOf(
                                SectionItem("b1", "🎙️ The Creative Horizon Podcast", "Weekly deep dives with authors, architects, and founders."),
                                SectionItem("b2", "📖 Book: The Design of Belonging", "National bestseller on intentional community and physical spaces."),
                                SectionItem("b3", "💌 Sunday Reflections Newsletter", "5-minute essay read by 75,000 creatives every weekend."),
                                SectionItem("b4", "☕ Support on BuyMeACoffee", "Fuel the ongoing research and studio production.")
                            )
                        ),
                        WebsiteSection(
                            id = "sec_bio_contact",
                            type = SectionType.CONTACT_FORM,
                            title = "Speaking & Advisory Inquiries",
                            subtitle = "For keynote inquiries, press interviews, or creative partnerships."
                        ),
                        WebsiteSection(
                            id = "sec_bio_footer",
                            type = SectionType.FOOTER
                        )
                    )
                )
            )
        )
    }
}
