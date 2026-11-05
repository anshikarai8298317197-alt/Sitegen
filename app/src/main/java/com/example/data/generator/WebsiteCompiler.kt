package com.example.data.generator

import com.example.data.model.SectionType
import com.example.data.model.WebsitePage
import com.example.data.model.WebsiteProject
import com.example.data.model.WebsiteSection

object WebsiteCompiler {

    fun compileToHtml(project: WebsiteProject, page: WebsitePage? = null): String {
        val targetPage = page ?: project.pages.firstOrNull { it.isHome } ?: project.pages.firstOrNull()
        val sections = targetPage?.sections ?: emptyList()
        val pageTitle = if (targetPage != null && !targetPage.isHome) "${targetPage.title} | ${project.name}" else project.name
        val primaryHex = project.primaryColorHex.ifBlank { "#6366F1" }
        val secondaryHex = project.secondaryColorHex.ifBlank { "#06B6D4" }

        val sb = StringBuilder()
        sb.append("<!DOCTYPE html>\n")
        sb.append("<html lang=\"en\">\n<head>\n")
        sb.append("  <meta charset=\"UTF-8\">\n")
        sb.append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n")
        sb.append("  <title>${escapeHtml(pageTitle)}</title>\n")
        sb.append("  <meta name=\"description\" content=\"${escapeHtml(project.seoDescription.ifBlank { project.description })}\">\n")
        sb.append("  <meta property=\"og:title\" content=\"${escapeHtml(pageTitle)}\">\n")
        sb.append("  <meta property=\"og:description\" content=\"${escapeHtml(project.seoDescription.ifBlank { project.description })}\">\n")
        sb.append("  <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n")
        sb.append("  <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n")
        sb.append("  <link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800&display=swap\" rel=\"stylesheet\">\n")

        sb.append("  <style>\n")
        sb.append(generateBaseCss(primaryHex, secondaryHex, project.themeStyle))
        if (project.customCss.isNotBlank()) {
            sb.append("\n  /* Custom User CSS */\n  ${project.customCss}\n")
        }
        sb.append("  </style>\n")
        sb.append("</head>\n<body>\n")

        // Render each section
        for (section in sections) {
            sb.append(renderSection(section, project))
        }

        // SITEGEN Badge footer if not removed
        sb.append("""
            <div class="sitegen-badge-bar">
                <span>Made with <a href="https://sitegen.page" target="_blank">SITEGEN</a> • One Prompt. Any Website.</span>
            </div>
        """.trimIndent())

        sb.append("\n  <script>\n")
        sb.append(generateInteractiveJs(project.id))
        if (project.customJs.isNotBlank()) {
            sb.append("\n  // Custom User JS\n  ${project.customJs}\n")
        }
        sb.append("  </script>\n")
        sb.append("</body>\n</html>")

        return sb.toString()
    }

    private fun renderSection(section: WebsiteSection, project: WebsiteProject): String {
        return when (section.type) {
            SectionType.NAVBAR -> renderNavbar(section, project)
            SectionType.HERO -> renderHero(section)
            SectionType.FEATURES -> renderFeatures(section)
            SectionType.SERVICES -> renderServices(section)
            SectionType.STATS -> renderStats(section)
            SectionType.PORTFOLIO_GALLERY -> renderGallery(section)
            SectionType.PRICING -> renderPricing(section)
            SectionType.TESTIMONIALS -> renderTestimonials(section)
            SectionType.FAQ -> renderFaq(section)
            SectionType.CONTACT_FORM -> renderContactForm(section)
            SectionType.BLOG_FEED -> renderBlogFeed(section)
            SectionType.NEWSLETTER -> renderNewsletter(section)
            SectionType.FOOTER -> renderFooter(section, project)
            SectionType.CUSTOM_CODE -> "<section class=\"section-block\">${section.rawHtml}</section>"
        }
    }

    private fun renderNavbar(section: WebsiteSection, project: WebsiteProject): String {
        val navLinks = project.pages.joinToString("") { page ->
            """<a href="#${page.slug}" class="nav-link">${escapeHtml(page.title)}</a>"""
        }
        return """
        <header class="site-header">
            <div class="container nav-container">
                <a href="#" class="brand-logo">
                    <span class="logo-dot"></span>
                    ${escapeHtml(project.name)}
                </a>
                <nav class="nav-links" id="desktopNav">
                    $navLinks
                </nav>
                <div class="nav-actions">
                    ${if (section.primaryButtonText.isNotBlank()) """<a href="${section.primaryButtonUrl}" class="btn btn-primary btn-sm">${escapeHtml(section.primaryButtonText)}</a>""" else ""}
                    <button class="mobile-toggle" id="menuToggle" aria-label="Toggle navigation">☰</button>
                </div>
            </div>
            <div class="mobile-menu" id="mobileMenu">
                $navLinks
                ${if (section.primaryButtonText.isNotBlank()) """<a href="${section.primaryButtonUrl}" class="btn btn-primary" style="margin-top:12px;">${escapeHtml(section.primaryButtonText)}</a>""" else ""}
            </div>
        </header>
        """.trimIndent()
    }

    private fun renderHero(section: WebsiteSection): String {
        return """
        <section class="hero-section ${section.backgroundStyle}">
            <div class="container hero-content">
                ${if (section.badgeText.isNotBlank()) """<span class="badge hero-badge">✨ ${escapeHtml(section.badgeText)}</span>""" else ""}
                <h1 class="hero-title">${escapeHtml(section.title)}</h1>
                <p class="hero-subtitle">${escapeHtml(section.subtitle)}</p>
                <div class="hero-cta-group">
                    ${if (section.primaryButtonText.isNotBlank()) """<a href="${section.primaryButtonUrl}" class="btn btn-primary">${escapeHtml(section.primaryButtonText)}</a>""" else ""}
                    ${if (section.secondaryButtonText.isNotBlank()) """<a href="${section.secondaryButtonUrl}" class="btn btn-outline">${escapeHtml(section.secondaryButtonText)}</a>""" else ""}
                </div>
                ${if (section.imageUrl.isNotBlank()) """
                <div class="hero-media-wrapper">
                    <img src="${section.imageUrl}" alt="${escapeHtml(section.title)}" class="hero-img" loading="lazy" />
                </div>
                """ else ""}
            </div>
        </section>
        """.trimIndent()
    }

    private fun renderFeatures(section: WebsiteSection): String {
        val itemsHtml = section.items.joinToString("") { item ->
            """
            <div class="feature-card">
                <div class="feature-icon">${item.iconName}</div>
                <h3 class="feature-title">${escapeHtml(item.title)}</h3>
                <p class="feature-desc">${escapeHtml(item.description)}</p>
            </div>
            """.trimIndent()
        }
        return """
        <section class="section-block ${section.backgroundStyle}">
            <div class="container">
                <div class="section-header">
                    ${if (section.badgeText.isNotBlank()) """<span class="badge">🔥 ${escapeHtml(section.badgeText)}</span>""" else ""}
                    <h2 class="section-title">${escapeHtml(section.title)}</h2>
                    <p class="section-subtitle">${escapeHtml(section.subtitle)}</p>
                </div>
                <div class="features-grid">
                    $itemsHtml
                </div>
            </div>
        </section>
        """.trimIndent()
    }

    private fun renderServices(section: WebsiteSection): String {
        val itemsHtml = section.items.joinToString("") { item ->
            """
            <div class="service-card">
                <h3 class="service-title">${escapeHtml(item.title)}</h3>
                <p class="service-desc">${escapeHtml(item.description)}</p>
                ${if (item.linkUrl.isNotBlank()) """<a href="${item.linkUrl}" class="service-link">Learn More →</a>""" else ""}
            </div>
            """.trimIndent()
        }
        return """
        <section class="section-block ${section.backgroundStyle}">
            <div class="container">
                <div class="section-header">
                    <h2 class="section-title">${escapeHtml(section.title)}</h2>
                    <p class="section-subtitle">${escapeHtml(section.subtitle)}</p>
                </div>
                <div class="services-grid">
                    $itemsHtml
                </div>
            </div>
        </section>
        """.trimIndent()
    }

    private fun renderStats(section: WebsiteSection): String {
        val itemsHtml = section.items.joinToString("") { item ->
            """
            <div class="stat-card">
                <div class="stat-number">${escapeHtml(item.badge.ifBlank { item.price.ifBlank { "99%" } })}</div>
                <div class="stat-label">${escapeHtml(item.title)}</div>
            </div>
            """.trimIndent()
        }
        return """
        <section class="stats-section ${section.backgroundStyle}">
            <div class="container stats-grid">
                $itemsHtml
            </div>
        </section>
        """.trimIndent()
    }

    private fun renderGallery(section: WebsiteSection): String {
        val itemsHtml = section.items.joinToString("") { item ->
            """
            <div class="gallery-card">
                <img src="${item.imageUrl.ifBlank { "https://images.unsplash.com/photo-1460925895917-afdab827c52f?w=600&auto=format&fit=crop" }}" alt="${escapeHtml(item.title)}" class="gallery-img" loading="lazy" />
                <div class="gallery-info">
                    <h4>${escapeHtml(item.title)}</h4>
                    <p>${escapeHtml(item.description)}</p>
                </div>
            </div>
            """.trimIndent()
        }
        return """
        <section class="section-block ${section.backgroundStyle}">
            <div class="container">
                <div class="section-header">
                    <h2 class="section-title">${escapeHtml(section.title)}</h2>
                    <p class="section-subtitle">${escapeHtml(section.subtitle)}</p>
                </div>
                <div class="gallery-grid">
                    $itemsHtml
                </div>
            </div>
        </section>
        """.trimIndent()
    }

    private fun renderPricing(section: WebsiteSection): String {
        val itemsHtml = section.items.joinToString("") { item ->
            val isHighlighted = item.badge.contains("Popular", ignoreCase = true) || item.badge.contains("Best", ignoreCase = true)
            """
            <div class="pricing-card ${if (isHighlighted) "highlighted" else ""}">
                ${if (item.badge.isNotBlank()) """<span class="pricing-badge">${escapeHtml(item.badge)}</span>""" else ""}
                <h3 class="plan-name">${escapeHtml(item.title)}</h3>
                <div class="plan-price">${escapeHtml(item.price)}<span class="period">/mo</span></div>
                <p class="plan-desc">${escapeHtml(item.description)}</p>
                <a href="${item.linkUrl.ifBlank { "#contact" }}" class="btn ${if (isHighlighted) "btn-primary" else "btn-outline"} btn-full">Choose Plan</a>
            </div>
            """.trimIndent()
        }
        return """
        <section class="section-block ${section.backgroundStyle}" id="pricing">
            <div class="container">
                <div class="section-header">
                    <h2 class="section-title">${escapeHtml(section.title)}</h2>
                    <p class="section-subtitle">${escapeHtml(section.subtitle)}</p>
                </div>
                <div class="pricing-grid">
                    $itemsHtml
                </div>
            </div>
        </section>
        """.trimIndent()
    }

    private fun renderTestimonials(section: WebsiteSection): String {
        val itemsHtml = section.items.joinToString("") { item ->
            """
            <div class="testimonial-card">
                <div class="stars">★★★★★</div>
                <p class="quote">“${escapeHtml(item.description)}”</p>
                <div class="author-block">
                    <div class="author-name">${escapeHtml(item.title)}</div>
                    <div class="author-role">${escapeHtml(item.badge)}</div>
                </div>
            </div>
            """.trimIndent()
        }
        return """
        <section class="section-block ${section.backgroundStyle}">
            <div class="container">
                <div class="section-header">
                    <h2 class="section-title">${escapeHtml(section.title)}</h2>
                    <p class="section-subtitle">${escapeHtml(section.subtitle)}</p>
                </div>
                <div class="testimonials-grid">
                    $itemsHtml
                </div>
            </div>
        </section>
        """.trimIndent()
    }

    private fun renderFaq(section: WebsiteSection): String {
        val itemsHtml = section.items.mapIndexed { idx, item ->
            """
            <div class="faq-item" onclick="toggleFaq($idx)">
                <div class="faq-question">
                    <span>${escapeHtml(item.title)}</span>
                    <span class="faq-icon" id="faq-icon-$idx">+</span>
                </div>
                <div class="faq-answer" id="faq-ans-$idx">
                    <p>${escapeHtml(item.description)}</p>
                </div>
            </div>
            """.trimIndent()
        }.joinToString("")
        return """
        <section class="section-block ${section.backgroundStyle}" id="faq">
            <div class="container max-w-700">
                <div class="section-header">
                    <h2 class="section-title">${escapeHtml(section.title)}</h2>
                    <p class="section-subtitle">${escapeHtml(section.subtitle)}</p>
                </div>
                <div class="faq-list">
                    $itemsHtml
                </div>
            </div>
        </section>
        """.trimIndent()
    }

    private fun renderContactForm(section: WebsiteSection): String {
        return """
        <section class="section-block ${section.backgroundStyle}" id="contact">
            <div class="container max-w-600">
                <div class="section-header">
                    <h2 class="section-title">${escapeHtml(section.title)}</h2>
                    <p class="section-subtitle">${escapeHtml(section.subtitle)}</p>
                </div>
                <form class="contact-form" id="leadForm" onsubmit="handleFormSubmit(event)">
                    <div class="form-group">
                        <label>Your Name *</label>
                        <input type="text" id="senderName" required placeholder="John Doe" class="form-input">
                    </div>
                    <div class="form-group">
                        <label>Email Address *</label>
                        <input type="email" id="senderEmail" required placeholder="john@company.com" class="form-input">
                    </div>
                    <div class="form-group">
                        <label>Phone Number (Optional)</label>
                        <input type="tel" id="senderPhone" placeholder="+91 98765 43210" class="form-input">
                    </div>
                    <div class="form-group">
                        <label>Project Details / Message *</label>
                        <textarea id="senderMessage" rows="4" required placeholder="Tell us about your requirements..." class="form-input"></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary btn-full" id="submitBtn">Send Message</button>
                    <div class="form-status" id="formStatus"></div>
                </form>
            </div>
        </section>
        """.trimIndent()
    }

    private fun renderBlogFeed(section: WebsiteSection): String {
        val itemsHtml = section.items.joinToString("") { item ->
            """
            <article class="blog-card">
                <div class="blog-meta">${escapeHtml(item.badge)}</div>
                <h3 class="blog-title">${escapeHtml(item.title)}</h3>
                <p class="blog-desc">${escapeHtml(item.description)}</p>
                <a href="${item.linkUrl.ifBlank { "#" }}" class="read-more">Read Article →</a>
            </article>
            """.trimIndent()
        }
        return """
        <section class="section-block ${section.backgroundStyle}" id="blog">
            <div class="container">
                <div class="section-header">
                    <h2 class="section-title">${escapeHtml(section.title)}</h2>
                    <p class="section-subtitle">${escapeHtml(section.subtitle)}</p>
                </div>
                <div class="blog-grid">
                    $itemsHtml
                </div>
            </div>
        </section>
        """.trimIndent()
    }

    private fun renderNewsletter(section: WebsiteSection): String {
        return """
        <section class="section-block newsletter-box ${section.backgroundStyle}">
            <div class="container max-w-600 text-center">
                <h2 class="section-title">${escapeHtml(section.title)}</h2>
                <p class="section-subtitle">${escapeHtml(section.subtitle)}</p>
                <form class="newsletter-form" onsubmit="handleNewsletter(event)">
                    <input type="email" id="newsEmail" required placeholder="Enter your work email" class="form-input">
                    <button type="submit" class="btn btn-primary">Subscribe</button>
                </form>
                <div id="newsStatus" class="form-status"></div>
            </div>
        </section>
        """.trimIndent()
    }

    private fun renderFooter(section: WebsiteSection, project: WebsiteProject): String {
        return """
        <footer class="site-footer">
            <div class="container footer-container">
                <div class="footer-brand">
                    <h3>${escapeHtml(project.name)}</h3>
                    <p>${escapeHtml(project.description)}</p>
                </div>
                <div class="footer-links">
                    <h4>Navigation</h4>
                    ${project.pages.joinToString("") { """<a href="#${it.slug}">${escapeHtml(it.title)}</a>""" }}
                </div>
                <div class="footer-links">
                    <h4>Connect</h4>
                    <a href="#contact">Contact Support</a>
                    <a href="#">Privacy Policy</a>
                    <a href="#">Terms of Service</a>
                </div>
            </div>
            <div class="footer-bottom">
                <p>© ${java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)} ${escapeHtml(project.name)}. All rights reserved.</p>
            </div>
        </footer>
        """.trimIndent()
    }

    private fun generateBaseCss(primaryHex: String, secondaryHex: String, theme: String): String {
        val isDarkTheme = theme.contains("Dark", ignoreCase = true) || theme.contains("Cyber", ignoreCase = true)
        val bgHex = if (isDarkTheme) "#0B0F19" else "#FAFAFA"
        val surfaceHex = if (isDarkTheme) "#131B2E" else "#FFFFFF"
        val textHex = if (isDarkTheme) "#F1F5F9" else "#0F172A"
        val textMutedHex = if (isDarkTheme) "#94A3B8" else "#64748B"
        val borderHex = if (isDarkTheme) "#24324D" else "#E2E8F0"

        return """
        :root {
            --primary: $primaryHex;
            --primary-hover: ${primaryHex}E6;
            --secondary: $secondaryHex;
            --bg: $bgHex;
            --surface: $surfaceHex;
            --text: $textHex;
            --text-muted: $textMutedHex;
            --border: $borderHex;
            --radius: 12px;
            --font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: var(--font-family);
            background-color: var(--bg);
            color: var(--text);
            line-height: 1.6;
            -webkit-font-smoothing: antialiased;
            overflow-x: hidden;
        }

        .container {
            width: 100%;
            max-width: 1140px;
            margin: 0 auto;
            padding: 0 24px;
        }

        .max-w-600 { max-width: 600px; margin: 0 auto; }
        .max-w-700 { max-width: 700px; margin: 0 auto; }
        .text-center { text-align: center; }

        /* Navigation */
        .site-header {
            position: sticky;
            top: 0;
            z-index: 100;
            background: ${if (isDarkTheme) "rgba(11, 15, 25, 0.85)" else "rgba(255, 255, 255, 0.9)"};
            backdrop-filter: blur(12px);
            border-bottom: 1px solid var(--border);
        }

        .nav-container {
            display: flex;
            align-items: center;
            justify-content: space-between;
            height: 70px;
        }

        .brand-logo {
            font-size: 20px;
            font-weight: 700;
            color: var(--text);
            text-decoration: none;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .logo-dot {
            width: 10px;
            height: 10px;
            background: var(--primary);
            border-radius: 50%;
            box-shadow: 0 0 10px var(--primary);
        }

        .nav-links {
            display: flex;
            gap: 28px;
        }

        .nav-link {
            color: var(--text-muted);
            text-decoration: none;
            font-size: 14px;
            font-weight: 500;
            transition: color 0.2s;
        }

        .nav-link:hover {
            color: var(--primary);
        }

        .mobile-toggle {
            display: none;
            background: none;
            border: none;
            font-size: 24px;
            color: var(--text);
            cursor: pointer;
        }

        .mobile-menu {
            display: none;
            flex-direction: column;
            gap: 16px;
            padding: 20px 24px;
            background: var(--surface);
            border-bottom: 1px solid var(--border);
        }

        .mobile-menu.open {
            display: flex;
        }

        /* Buttons */
        .btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            padding: 12px 24px;
            border-radius: var(--radius);
            font-size: 15px;
            font-weight: 600;
            text-decoration: none;
            cursor: pointer;
            transition: all 0.2s ease;
            border: 1px solid transparent;
        }

        .btn-primary {
            background-color: var(--primary);
            color: #FFFFFF;
            box-shadow: 0 4px 14px ${primaryHex}4D;
        }

        .btn-primary:hover {
            background-color: var(--primary-hover);
            transform: translateY(-1px);
        }

        .btn-outline {
            background-color: transparent;
            border-color: var(--border);
            color: var(--text);
        }

        .btn-outline:hover {
            border-color: var(--primary);
            color: var(--primary);
        }

        .btn-sm {
            padding: 8px 16px;
            font-size: 13px;
        }

        .btn-full {
            width: 100%;
        }

        .badge {
            display: inline-block;
            padding: 6px 14px;
            font-size: 12px;
            font-weight: 600;
            border-radius: 999px;
            background: ${primaryHex}1A;
            color: var(--primary);
            border: 1px solid ${primaryHex}33;
            margin-bottom: 14px;
        }

        /* Hero */
        .hero-section {
            padding: 90px 0 60px;
            text-align: center;
            position: relative;
        }

        .hero-title {
            font-size: 44px;
            font-weight: 800;
            line-height: 1.15;
            margin-bottom: 20px;
            letter-spacing: -0.02em;
        }

        .hero-subtitle {
            font-size: 18px;
            color: var(--text-muted);
            max-width: 680px;
            margin: 0 auto 32px;
        }

        .hero-cta-group {
            display: flex;
            gap: 16px;
            justify-content: center;
            margin-bottom: 40px;
        }

        .hero-media-wrapper {
            margin-top: 24px;
            border-radius: 16px;
            overflow: hidden;
            border: 1px solid var(--border);
            box-shadow: 0 20px 40px rgba(0,0,0,0.25);
        }

        .hero-img {
            width: 100%;
            height: auto;
            display: block;
        }

        /* Section Blocks */
        .section-block {
            padding: 70px 0;
        }

        .section-header {
            text-align: center;
            margin-bottom: 48px;
        }

        .section-title {
            font-size: 32px;
            font-weight: 700;
            margin-bottom: 12px;
            letter-spacing: -0.01em;
        }

        .section-subtitle {
            font-size: 16px;
            color: var(--text-muted);
            max-width: 600px;
            margin: 0 auto;
        }

        /* Features & Services Grid */
        .features-grid, .services-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 24px;
        }

        .feature-card, .service-card {
            background: var(--surface);
            border: 1px solid var(--border);
            padding: 32px 24px;
            border-radius: var(--radius);
            transition: transform 0.2s, border-color 0.2s;
        }

        .feature-card:hover, .service-card:hover {
            transform: translateY(-4px);
            border-color: var(--primary);
        }

        .feature-icon {
            font-size: 28px;
            margin-bottom: 16px;
            width: 50px;
            height: 50px;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 12px;
            background: ${primaryHex}15;
            color: var(--primary);
        }

        .feature-title, .service-title {
            font-size: 18px;
            font-weight: 600;
            margin-bottom: 10px;
        }

        .feature-desc, .service-desc {
            font-size: 14px;
            color: var(--text-muted);
            line-height: 1.5;
        }

        /* Stats */
        .stats-section {
            padding: 50px 0;
            background: var(--surface);
            border-top: 1px solid var(--border);
            border-bottom: 1px solid var(--border);
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 24px;
            text-align: center;
        }

        .stat-number {
            font-size: 38px;
            font-weight: 800;
            color: var(--primary);
            margin-bottom: 6px;
        }

        .stat-label {
            font-size: 14px;
            color: var(--text-muted);
            font-weight: 500;
        }

        /* Gallery */
        .gallery-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 24px;
        }

        .gallery-card {
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: var(--radius);
            overflow: hidden;
        }

        .gallery-img {
            width: 100%;
            height: 200px;
            object-fit: cover;
            display: block;
        }

        .gallery-info {
            padding: 20px;
        }

        .gallery-info h4 {
            font-size: 16px;
            font-weight: 600;
            margin-bottom: 6px;
        }

        .gallery-info p {
            font-size: 13px;
            color: var(--text-muted);
        }

        /* Pricing */
        .pricing-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 24px;
            align-items: stretch;
        }

        .pricing-card {
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: 16px;
            padding: 36px 28px;
            display: flex;
            flex-direction: column;
            position: relative;
        }

        .pricing-card.highlighted {
            border: 2px solid var(--primary);
            box-shadow: 0 8px 30px ${primaryHex}26;
        }

        .pricing-badge {
            position: absolute;
            top: -12px;
            left: 50%;
            transform: translateX(-50%);
            background: var(--primary);
            color: #FFF;
            font-size: 11px;
            font-weight: 700;
            padding: 4px 12px;
            border-radius: 999px;
            text-transform: uppercase;
        }

        .plan-price {
            font-size: 36px;
            font-weight: 800;
            margin: 16px 0 12px;
        }

        .plan-price .period {
            font-size: 14px;
            font-weight: 400;
            color: var(--text-muted);
        }

        .plan-desc {
            font-size: 14px;
            color: var(--text-muted);
            margin-bottom: 24px;
            flex-grow: 1;
        }

        /* Testimonials */
        .testimonials-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 24px;
        }

        .testimonial-card {
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: var(--radius);
            padding: 28px;
        }

        .stars {
            color: #F59E0B;
            font-size: 16px;
            margin-bottom: 12px;
        }

        .quote {
            font-size: 14px;
            font-style: italic;
            color: var(--text);
            margin-bottom: 20px;
            line-height: 1.6;
        }

        .author-name {
            font-weight: 600;
            font-size: 14px;
        }

        .author-role {
            font-size: 12px;
            color: var(--text-muted);
        }

        /* FAQ */
        .faq-list {
            display: flex;
            flex-direction: column;
            gap: 12px;
        }

        .faq-item {
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: var(--radius);
            padding: 20px 24px;
            cursor: pointer;
            transition: border-color 0.2s;
        }

        .faq-item:hover {
            border-color: var(--primary);
        }

        .faq-question {
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-weight: 600;
            font-size: 15px;
        }

        .faq-icon {
            font-size: 20px;
            font-weight: 400;
            color: var(--primary);
            transition: transform 0.2s;
        }

        .faq-answer {
            display: none;
            margin-top: 12px;
            padding-top: 12px;
            border-top: 1px solid var(--border);
            color: var(--text-muted);
            font-size: 14px;
            line-height: 1.5;
        }

        .faq-answer.show {
            display: block;
        }

        /* Forms */
        .contact-form {
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: 16px;
            padding: 32px 28px;
        }

        .form-group {
            margin-bottom: 18px;
        }

        .form-group label {
            display: block;
            font-size: 13px;
            font-weight: 600;
            margin-bottom: 6px;
            color: var(--text);
        }

        .form-input {
            width: 100%;
            padding: 12px 14px;
            border-radius: 8px;
            border: 1px solid var(--border);
            background: ${if (isDarkTheme) "#0B0F19" else "#F8FAFC"};
            color: var(--text);
            font-family: inherit;
            font-size: 14px;
            outline: none;
            transition: border-color 0.2s;
        }

        .form-input:focus {
            border-color: var(--primary);
        }

        .form-status {
            margin-top: 14px;
            padding: 10px;
            border-radius: 8px;
            font-size: 13px;
            display: none;
            text-align: center;
        }

        .form-status.success {
            display: block;
            background: #10B98126;
            color: #10B981;
            border: 1px solid #10B9814D;
        }

        .newsletter-box {
            background: var(--surface);
            border-top: 1px solid var(--border);
            border-bottom: 1px solid var(--border);
        }

        .newsletter-form {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }

        /* Blog */
        .blog-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 24px;
        }

        .blog-card {
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: var(--radius);
            padding: 28px;
        }

        .blog-meta {
            font-size: 12px;
            color: var(--primary);
            font-weight: 600;
            margin-bottom: 8px;
            text-transform: uppercase;
        }

        .blog-title {
            font-size: 18px;
            font-weight: 700;
            margin-bottom: 10px;
        }

        .blog-desc {
            font-size: 14px;
            color: var(--text-muted);
            margin-bottom: 16px;
        }

        .read-more {
            color: var(--primary);
            text-decoration: none;
            font-size: 13px;
            font-weight: 600;
        }

        /* Footer */
        .site-footer {
            background: ${if (isDarkTheme) "#080B12" else "#F1F5F9"};
            border-top: 1px solid var(--border);
            padding: 60px 0 30px;
        }

        .footer-container {
            display: grid;
            grid-template-columns: 2fr 1fr 1fr;
            gap: 40px;
            margin-bottom: 40px;
        }

        .footer-brand h3 {
            font-size: 18px;
            font-weight: 700;
            margin-bottom: 12px;
        }

        .footer-brand p {
            font-size: 14px;
            color: var(--text-muted);
            max-width: 320px;
        }

        .footer-links h4 {
            font-size: 14px;
            font-weight: 600;
            margin-bottom: 16px;
        }

        .footer-links a {
            display: block;
            color: var(--text-muted);
            text-decoration: none;
            font-size: 13px;
            margin-bottom: 8px;
            transition: color 0.2s;
        }

        .footer-links a:hover {
            color: var(--primary);
        }

        .footer-bottom {
            text-align: center;
            padding-top: 24px;
            border-top: 1px solid var(--border);
            font-size: 13px;
            color: var(--text-muted);
        }

        /* SITEGEN Platform Badge */
        .sitegen-badge-bar {
            text-align: center;
            padding: 12px;
            background: #090D16;
            color: #94A3B8;
            font-size: 12px;
            border-top: 1px solid #1E293B;
        }

        .sitegen-badge-bar a {
            color: #6366F1;
            text-decoration: none;
            font-weight: 600;
        }

        @media (max-width: 768px) {
            .nav-links { display: none; }
            .mobile-toggle { display: block; }
            .hero-title { font-size: 32px; }
            .hero-cta-group { flex-direction: column; }
            .newsletter-form { flex-direction: column; }
            .footer-container { grid-template-columns: 1fr; }
        }
        """.trimIndent()
    }

    private fun generateInteractiveJs(projectId: String): String {
        return """
        // Mobile menu toggle
        const toggleBtn = document.getElementById('menuToggle');
        const mobileMenu = document.getElementById('mobileMenu');
        if (toggleBtn && mobileMenu) {
            toggleBtn.addEventListener('click', () => {
                mobileMenu.classList.toggle('open');
            });
        }

        // FAQ Toggle
        function toggleFaq(index) {
            const ans = document.getElementById('faq-ans-' + index);
            const icon = document.getElementById('faq-icon-' + index);
            if (ans) {
                const isOpen = ans.classList.contains('show');
                ans.classList.toggle('show');
                if (icon) icon.textContent = isOpen ? '+' : '−';
            }
        }

        // Contact form submission
        function handleFormSubmit(e) {
            e.preventDefault();
            const name = document.getElementById('senderName')?.value || '';
            const email = document.getElementById('senderEmail')?.value || '';
            const phone = document.getElementById('senderPhone')?.value || '';
            const msg = document.getElementById('senderMessage')?.value || '';
            const statusBox = document.getElementById('formStatus');
            const submitBtn = document.getElementById('submitBtn');

            if (submitBtn) {
                submitBtn.disabled = true;
                submitBtn.textContent = 'Submitting...';
            }

            // If running inside Android WebView bridge
            if (window.SiteGenApp && typeof window.SiteGenApp.submitLead === 'function') {
                window.SiteGenApp.submitLead('$projectId', name, email, phone, msg);
            }

            setTimeout(() => {
                if (statusBox) {
                    statusBox.className = 'form-status success';
                    statusBox.textContent = '✓ Thank you! Your message has been sent successfully.';
                }
                if (submitBtn) {
                    submitBtn.textContent = 'Message Sent';
                }
                document.getElementById('leadForm')?.reset();
            }, 600);
        }

        // Newsletter form
        function handleNewsletter(e) {
            e.preventDefault();
            const email = document.getElementById('newsEmail')?.value || '';
            const statusBox = document.getElementById('newsStatus');
            if (statusBox) {
                statusBox.className = 'form-status success';
                statusBox.textContent = '✓ You are subscribed to updates!';
            }
        }
        """.trimIndent()
    }

    private fun escapeHtml(text: String): String {
        return text.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
    }
}
