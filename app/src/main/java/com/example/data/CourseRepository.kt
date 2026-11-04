package com.example.data

import com.example.model.CertificateData
import com.example.model.Course
import com.example.model.CourseCategory
import com.example.model.CourseLesson
import com.example.model.CourseModule
import com.example.model.EnrolledCourse
import com.example.model.OrderReceipt
import com.example.model.Testimonial
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

object CourseRepository {

    // Contact Information
    const val CONTACT_PHONE = "+917470738944"
    const val CONTACT_PHONE_FORMATTED = "+91 7470738944"
    const val CONTACT_WHATSAPP = "917470738944"
    const val WEBSITE_URL = "https://www.harshikasedits.in"
    const val INSTAGRAM_URL = "https://www.instagram.com/harshikasedits"
    const val INSTAGRAM_HANDLE = "@harshikasedits"
    const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.harshikas.edits"
    const val SUPPORT_EMAIL = "harshikas.edits@gmail.com"

    // Initial Testimonials
    private val initialTestimonials = listOf(
        Testimonial(
            id = "test-1",
            name = "Pooja Sharma",
            role = "Freelance Graphic Designer",
            location = "Delhi",
            courseName = "Canva, AI Tools & AI Graphic Designing",
            rating = 5.0f,
            comment = "I had zero design background before joining. Harshika Ma'am taught Canva and modern AI tools step-by-step. Within 3 weeks of completing the course, I landed an ongoing client on Upwork paying $300/month for social media graphics! The 1-Year Canva Pro free subscription alone recovered my course fee.",
            earningsOutcome = "Earns $300/mo on Upwork",
            date = "2 days ago"
        ),
        Testimonial(
            id = "test-2",
            name = "Rahul Verma",
            role = "Digital Marketing Executive",
            location = "Mumbai",
            courseName = "Professional Diploma Program",
            rating = 5.0f,
            comment = "The 3-Month Diploma was the best career investment. From website development to Meta ads and client pitching, everything was covered with live interactive classes. Their placement assistance helped me switch from low-paying tele-calling to a full-time Digital Marketing role at ₹35,000/month!",
            earningsOutcome = "Switched to ₹35k/mo career",
            date = "1 week ago"
        ),
        Testimonial(
            id = "test-3",
            name = "Ananya Gupta",
            role = "Homemaker to Creative Freelancer",
            location = "Indore",
            courseName = "Canva Mastery Course",
            rating = 5.0f,
            comment = "Being a homemaker, I wanted to earn independently from home. The 7-day Canva course made me proficient in Instagram posts, brand logos, and digital invites. Currently I earn ₹18,000 to ₹25,000 monthly working just 3 hours a day from my laptop!",
            earningsOutcome = "Earns ₹25,000/mo from home",
            date = "2 weeks ago"
        ),
        Testimonial(
            id = "test-4",
            name = "Vikramaditya Rathore",
            role = "Content Creator & Reel Editor",
            location = "Jaipur",
            courseName = "Video Editing Course",
            rating = 4.9f,
            comment = "The video editing course gave me deep insights into trending Instagram Reels, transitions, sound effects, and color grading. My client videos started going viral. I currently edit reels for 3 fitness influencers in India and charge ₹15,000 per brand!",
            earningsOutcome = "Charges ₹45,000/mo for editing",
            date = "3 weeks ago"
        ),
        Testimonial(
            id = "test-5",
            name = "Sneha Patel",
            role = "B.Tech Graduate / Job Seeker",
            location = "Ahmedabad",
            courseName = "Job Mastery Course",
            rating = 5.0f,
            comment = "Resume building, corporate communication, and LinkedIn optimization sessions were eye-openers. Recruiters started reaching out to me directly on LinkedIn. Harshika's team took mock interviews and guided me till my final offer letter!",
            earningsOutcome = "Got Placed at Top IT Firm",
            date = "1 month ago"
        ),
        Testimonial(
            id = "test-6",
            name = "Mohammad Farhan",
            role = "Freelance SEO & Growth Strategist",
            location = "Lucknow",
            courseName = "LinkedIn, SEO & Freelancing Course",
            rating = 4.9f,
            comment = "The client hunting and proposal writing templates provided in this course are pure gold. Closed my first domestic SEO client within 10 days of batch completion. 100% recommended for every aspiring freelancer!",
            earningsOutcome = "Closed 3 High-Ticket Clients",
            date = "1 month ago"
        )
    )

    // Master Course Catalog from Harshika's Edits Brochure
    private val allCourses = listOf(
        Course(
            id = "he-diploma-01",
            title = "Professional Diploma Program",
            subtitle = "3-Month Future-Ready Digital Skills & Freelancing Program with Job Guarantee",
            category = CourseCategory.CAREER_DIPLOMA,
            duration = "3 Months",
            mode = "Daily Live Classes (Mon-Fri) + Recordings Access",
            price = 4999,
            originalPrice = 14999,
            rating = 4.95f,
            reviewCount = 380,
            enrolledCount = 2850,
            tag = "Job Guarantee",
            overview = "This comprehensive 3-Month Professional Diploma Program is designed to prepare learners for the rapidly growing digital economy. The program combines practical digital skills, freelancing strategies, and portfolio development, and career-focused training to help learners build a successful future with high income capabilities.",
            whatYouWillLearn = listOf(
                "Full Stack Website Development using modern CMS and responsive tools",
                "Advanced Social Media Management & Brand Strategy",
                "High-ROI Google Ads & Meta Ads Paid Campaign Mastery",
                "Professional Graphic Designing & Visual Identity Creation",
                "AI Tools Mastery, Automated Workflows & Prompt Engineering",
                "SEO (Search Engine Optimization) & High-Conversion Content Writing",
                "LinkedIn Authority Growth & Personal Branding Strategy",
                "Commercial Video Editing for YouTube, Reels & Ads",
                "Freelancing Strategy, Client Acquisition & High-Ticket Pitching"
            ),
            topicsCovered = listOf(
                "Website Development", "Social Media Management", "Google & Meta Ads",
                "Graphic Designing", "AI Tools & Automation", "Content Writing",
                "SEO Optimization", "LinkedIn Growth Strategy", "Professional Video Editing",
                "Freelancing & Client Pitching"
            ),
            curriculumModules = listOf(
                CourseModule(
                    1, "Foundation & Professional Graphic Design",
                    "Canva advanced techniques, color psychology, typography, and commercial branding assets.",
                    listOf(
                        CourseLesson("l1", "Design Principles & Brand Identity", "45 mins", isFreePreview = true),
                        CourseLesson("l2", "Social Media Post & Ad Creative Design", "55 mins"),
                        CourseLesson("l3", "Print Graphics, Presentations & Brochures", "50 mins")
                    )
                ),
                CourseModule(
                    2, "AI Tools & Automation",
                    "Harnessing generative AI tools for writing, image generation, research, and workflow automation.",
                    listOf(
                        CourseLesson("l4", "Prompt Engineering Masterclass", "60 mins"),
                        CourseLesson("l5", "AI Image Generation & Commercial Edits", "55 mins"),
                        CourseLesson("l6", "Productivity Automation & Workflow Integration", "50 mins")
                    )
                ),
                CourseModule(
                    3, "Website Development & CMS",
                    "Building fast, modern, responsive websites without complex coding.",
                    listOf(
                        CourseLesson("l7", "Domain, Hosting & WordPress Setup", "60 mins"),
                        CourseLesson("l8", "Designing Responsive Business Landing Pages", "65 mins"),
                        CourseLesson("l9", "E-commerce & Lead Capture Funnels", "50 mins")
                    )
                ),
                CourseModule(
                    4, "Performance Marketing & Client Acquisition",
                    "Paid ads, SEO, LinkedIn client hunting, and freelancing business operations.",
                    listOf(
                        CourseLesson("l10", "Meta & Google Ads Campaign Setup", "70 mins"),
                        CourseLesson("l11", "Client Hunting & Proposal Writing on Upwork & LinkedIn", "60 mins"),
                        CourseLesson("l12", "Portfolio Review & Final Certification Call", "45 mins")
                    )
                )
            ),
            specialBenefits = listOf(
                "Daily Live Classes with expert faculty",
                "Lifetime Recordings Access",
                "Verified Course Completion Certificate",
                "Hands-on Practical Learning Projects",
                "Free 1:1 Mentor Call after completion",
                "100% Freelancing and Job Placement Guidance",
                "Portfolio Building Support & Client Acquisition Scripts"
            ),
            idealFor = listOf("Students", "Graduates", "Working Professionals", "Entrepreneurs", "Freelancers"),
            earningOpportunity = "₹5,000 – ₹20,000+ per month depending on skills & effort",
            iconKey = "diploma"
        ),
        Course(
            id = "he-canva-ai-02",
            title = "Canva, AI Tools & AI Graphic Designing Course",
            subtitle = "Master Trending AI Tools & Learn AI-Powered Graphic Designing",
            category = CourseCategory.AI_AND_DESIGN,
            duration = "10 Days",
            mode = "Live Online Classes + Recordings Available",
            price = 999,
            originalPrice = 3499,
            rating = 4.96f,
            reviewCount = 520,
            enrolledCount = 4200,
            tag = "Hot & Trending",
            overview = "Learn the latest AI tools and discover how artificial intelligence is transforming graphic designing, content creation, productivity, and digital work. This course is designed for students, freelancers, and working professionals who want to stay ahead in the digital era.",
            whatYouWillLearn = listOf(
                "Canva basics to advanced creative techniques",
                "10+ Trending AI Tools for content creation & productivity",
                "AI Graphic Designing & Automated Visuals",
                "AI Image Generation (Midjourney, DALL-E, Firefly)",
                "Prompt Engineering for professional design workflows",
                "Social Media Post Designing & Animated Videos",
                "Resume & Portfolio Design that stands out"
            ),
            topicsCovered = listOf(
                "Trending AI Tools", "AI-Powered Graphic Designing", "Content Creation with AI",
                "Productivity & Automation Tools", "Professional Design Workflows",
                "Canva AI Magic Features", "Animated Reels & Social Posts"
            ),
            curriculumModules = listOf(
                CourseModule(
                    1, "Canva Advanced & Magic AI Features",
                    "Mastering Canva's design tools, Magic Studio, and typography.",
                    listOf(
                        CourseLesson("l201", "Canva Pro Interface & Magic Studio", "45 mins", isFreePreview = true),
                        CourseLesson("l202", "Designing High-Converting Social Media Posts", "50 mins"),
                        CourseLesson("l203", "Brand Kit & Aesthetic Typography", "40 mins")
                    )
                ),
                CourseModule(
                    2, "AI Graphic Designing & Image Generation",
                    "Using generative AI models to craft photorealistic and artistic visuals.",
                    listOf(
                        CourseLesson("l204", "Prompt Engineering for Realistic Images", "55 mins"),
                        CourseLesson("l205", "Creating Vector Graphics & Logos with AI", "50 mins"),
                        CourseLesson("l206", "Combining Canva & AI for Commercial Projects", "60 mins")
                    )
                )
            ),
            specialBenefits = listOf(
                "1 Year FREE Canva Pro Subscription",
                "Daily Live Interactive Sessions",
                "Recorded Class Access Anytime",
                "Prompt Library & Resource Pack",
                "Course Completion Certificate"
            ),
            idealFor = listOf("Students", "Working Professionals", "Beginners", "Freelancers"),
            earningOpportunity = "₹10,000 – ₹30,000/mo designing for social media & brands",
            iconKey = "ai"
        ),
        Course(
            id = "he-canva-mastery-03",
            title = "Canva Mastery Course",
            subtitle = "Learn Canva from Beginner to Advanced & Start Your Freelancing Journey",
            category = CourseCategory.AI_AND_DESIGN,
            duration = "7 Days",
            mode = "Online Live Sessions + Class Recordings",
            price = 499,
            originalPrice = 1999,
            rating = 4.92f,
            reviewCount = 410,
            enrolledCount = 3800,
            tag = "Bestseller",
            overview = "Master Canva and learn how to create professional designs for social media, businesses, marketing campaigns, presentations, branding, and freelancing projects. Practical, step-by-step training with real-world projects to help you become a confident designer.",
            whatYouWillLearn = listOf(
                "Canva Basics to Advanced Tools & Features",
                "Social Media Design for Instagram, YouTube & Facebook",
                "Branding & Marketing Creatives (Flyers, Posters, Banners)",
                "Pitch Decks & High-Impact Presentation Design",
                "Business Graphics & Corporate Stationery",
                "Professional Design Techniques & Color Theory",
                "How to earn online through Canva freelancing gigs"
            ),
            topicsCovered = listOf(
                "Canva Interface", "Social Media Templates", "Branding & Logos",
                "Presentation Design", "Business Graphics", "Freelancing on Canva"
            ),
            curriculumModules = listOf(
                CourseModule(
                    1, "Canva Essentials to Mastery",
                    "From blank canvas to stunning graphics.",
                    listOf(
                        CourseLesson("l301", "Canva Fundamentals & Layouts", "40 mins", isFreePreview = true),
                        CourseLesson("l302", "Social Media Graphics & Stories", "45 mins"),
                        CourseLesson("l303", "Corporate Brochures & Pitch Decks", "50 mins"),
                        CourseLesson("l304", "Freelancing Opportunities with Canva", "45 mins")
                    )
                )
            ),
            specialBenefits = listOf(
                "1 Year Canva Pro Subscription FREE",
                "1 Year Freelancing Guidance",
                "100% Freelancing Support & Guidance",
                "Design Asset Bundle (Fonts, Palettes, Icons)"
            ),
            idealFor = listOf("Beginners", "Social Media Managers", "Homemakers", "Content Creators"),
            earningOpportunity = "₹15,000 – ₹25,000/mo offering design services",
            iconKey = "canva"
        ),
        Course(
            id = "he-video-editing-04",
            title = "Video Editing Course",
            subtitle = "Learn Professional Video Editing for Reels, YouTube & Viral Social Content",
            category = CourseCategory.VIDEO_EDITING,
            duration = "15 Days",
            mode = "Online Live Classes + Recording Available",
            price = 1299,
            originalPrice = 3999,
            rating = 4.94f,
            reviewCount = 290,
            enrolledCount = 2100,
            tag = "High Income Skill",
            overview = "Learn professional video editing techniques from basics to advanced level and create engaging videos for reels, YouTube, and all social platforms. Master pacing, transitions, text animations, and cinematic sound design.",
            whatYouWillLearn = listOf(
                "Reels & Shorts Editing with viral hooks & retention pacing",
                "Smooth Dynamic Transitions & Visual Effects",
                "Text Animation, Kinetic Typography & Auto-Captions",
                "Sound Design & Audio Editing for maximum immersion",
                "Cinematic Color Correction & LUTs application",
                "Export settings for crisp 4K & Instagram uploads",
                "Monetizing video editing skills with client outreach"
            ),
            topicsCovered = listOf(
                "Reels & Shorts Editing", "Transitions & Effects", "Text Animation",
                "Audio Editing", "Color Correction", "Viral Content Formula", "Client Pitching"
            ),
            curriculumModules = listOf(
                CourseModule(
                    1, "Short Form Video Editing (Reels & Shorts)",
                    "Master vertical storytelling and retention techniques.",
                    listOf(
                        CourseLesson("l401", "Introduction to Editing Software & Timeline", "45 mins", isFreePreview = true),
                        CourseLesson("l402", "Hooking Viewers: First 3 Seconds Mastery", "50 mins"),
                        CourseLesson("l403", "Dynamic Captions & Sound Effects Sync", "55 mins")
                    )
                ),
                CourseModule(
                    2, "Advanced Effects & Commercial Video",
                    "Color correction, masking, and brand video production.",
                    listOf(
                        CourseLesson("l404", "Transitions, Keyframing & Motion Effects", "60 mins"),
                        CourseLesson("l405", "Color Grading & Audio Enhancement", "50 mins"),
                        CourseLesson("l406", "Finding Clients as a Video Editor", "45 mins")
                    )
                )
            ),
            specialBenefits = listOf(
                "1 Month CapCut Pro Subscription FREE",
                "Sound Effects & B-Roll Pack (1,000+ Assets)",
                "Live Project Reviews with Feedback",
                "Course Completion Certificate"
            ),
            idealFor = listOf("Beginners", "Content Creators", "Aspiring Editors", "Freelancers"),
            earningOpportunity = "₹20,000 – ₹50,000/mo editing for creators & brands",
            iconKey = "video"
        ),
        Course(
            id = "he-digital-skills-05",
            title = "Digital Skills Course",
            subtitle = "A Complete Skill Development Program for Today's Online Economy",
            category = CourseCategory.CAREER_DIPLOMA,
            duration = "40 Days",
            mode = "Online Live Classes + Recordings Available",
            price = 1999,
            originalPrice = 5999,
            rating = 4.93f,
            reviewCount = 260,
            enrolledCount = 1950,
            tag = "Complete Package",
            overview = "A complete skill development program designed to help learners gain practical digital skills that are highly valuable in today's online economy. Covers Canva designing, AI tools & automation, video editing, practical projects, and productivity techniques.",
            whatYouWillLearn = listOf(
                "End-to-end Canva Designing from basic to advanced",
                "AI Tools & Smart Workflow Automation",
                "Video Editing for Social Media & Business",
                "Practical Real-World Projects & Portfolio Development",
                "Content Creation & Copywriting Fundamentals",
                "Digital Productivity & Online Work Techniques"
            ),
            topicsCovered = listOf(
                "Canva Designing", "AI Tools & Automation", "Video Editing",
                "Practical Projects", "Content Creation Skills", "Productivity Techniques"
            ),
            curriculumModules = listOf(
                CourseModule(
                    1, "Canva & Visual Assets",
                    "Foundational visual creation for modern businesses.",
                    listOf(
                        CourseLesson("l501", "Digital Visuals Overview", "45 mins", isFreePreview = true),
                        CourseLesson("l502", "Commercial Design & Branding Kits", "50 mins")
                    )
                ),
                CourseModule(
                    2, "AI & Video Editing",
                    "Combining modern AI automation with compelling video edits.",
                    listOf(
                        CourseLesson("l503", "AI for Content Creation & Automation", "55 mins"),
                        CourseLesson("l504", "Reels & Social Video Editing", "60 mins")
                    )
                )
            ),
            specialBenefits = listOf(
                "1-Year Canva Pro Subscription FREE",
                "1-Month CapCut Pro Subscription FREE",
                "Recorded Sessions Access",
                "Certificate of Completion",
                "Practical Learning Experience"
            ),
            idealFor = listOf("Students", "Beginners", "Freelancers", "Working Professionals"),
            earningOpportunity = "₹15,000 – ₹35,000/mo in multi-skill freelancing",
            iconKey = "digital"
        ),
        Course(
            id = "he-job-mastery-06",
            title = "Job Mastery Course",
            subtitle = "Become Job-Ready & Freelancing-Ready with High-Income Digital Skills",
            category = CourseCategory.CAREER_DIPLOMA,
            duration = "70 Days",
            mode = "Online Live Classes + Recording Available",
            price = 2999,
            originalPrice = 8999,
            rating = 4.95f,
            reviewCount = 310,
            enrolledCount = 2200,
            tag = "Placement Support",
            overview = "Develop professional skills, improve workplace readiness, and prepare for employment and career opportunities. Includes resume building, LinkedIn optimization, corporate communication, job search strategies, and placement guidance.",
            whatYouWillLearn = listOf(
                "ATS-Friendly Professional Resume & Cover Letter Building",
                "Complete LinkedIn Profile Optimization & Recruiter Outreach",
                "Corporate Communication & Workplace Etiquette",
                "Interview Preparation & Mock HR Rounds",
                "Job Search Strategies on Naukri, Indeed & LinkedIn",
                "Hands-on skills: AI tools, video editing, Canva, and social media",
                "Placement Guidance & Direct Referral Opportunities"
            ),
            topicsCovered = listOf(
                "AI Tools", "Video Editing", "Canva", "Reel Creation",
                "Digital Marketing", "Social Media Management", "Resume Building",
                "LinkedIn Optimization", "Corporate Skills", "Placement Guidance"
            ),
            curriculumModules = listOf(
                CourseModule(
                    1, "Digital Skills Mastery",
                    "Equipping you with Canva, AI tools, and video editing for corporate roles.",
                    listOf(
                        CourseLesson("l601", "Corporate Digital Skill Requirements", "45 mins", isFreePreview = true),
                        CourseLesson("l602", "Presentations, Reports & Digital Assets", "50 mins")
                    )
                ),
                CourseModule(
                    2, "Career Preparation & Placement",
                    "Resume crafting, LinkedIn networking, and interview success.",
                    listOf(
                        CourseLesson("l603", "ATS Resume & Portfolio Building", "60 mins"),
                        CourseLesson("l604", "LinkedIn Recruiter InMail Strategies", "55 mins"),
                        CourseLesson("l605", "Mock Interview Sessions & HR FAQs", "60 mins")
                    )
                )
            ),
            specialBenefits = listOf(
                "1 Year FREE Canva Pro Subscription",
                "1 Month FREE CapCut Pro Subscription",
                "Personalized Resume Review by Experts",
                "100% Placement Guidance & Referral Support"
            ),
            idealFor = listOf("College Graduates", "Job Seekers", "Career Changers", "Working Professionals"),
            earningOpportunity = "₹25,000 – ₹50,000/mo corporate starter salaries",
            iconKey = "job"
        ),
        Course(
            id = "he-linkedin-seo-07",
            title = "LinkedIn, SEO & Freelancing Course",
            subtitle = "A Practical & Career-Focused Program to Rank Websites & Land High-Paying Clients",
            category = CourseCategory.FREELANCING,
            duration = "20 Days",
            mode = "Online Live Classes + Recording Available",
            price = 1199,
            originalPrice = 3499,
            rating = 4.93f,
            reviewCount = 210,
            enrolledCount = 1600,
            tag = "Freelance Fast-Track",
            overview = "A practical and career-focused program designed to help you grow on LinkedIn, rank on search engines, and build a successful freelancing career. Learn personal branding, search engine optimization, client hunting, proposal writing, and portfolio building.",
            whatYouWillLearn = listOf(
                "LinkedIn Profile Optimization to attract inbound leads",
                "Content strategy to reach 100K+ impressions on LinkedIn",
                "On-Page, Off-Page & Technical SEO Fundamentals",
                "Keyword Research using free and premium tools",
                "Freelancing Platforms Mastery (Upwork, Fiverr, Freelancer)",
                "Cold Emailing & Direct Client Hunting on Social Media",
                "Winning Proposal Writing & Contract Negotiation"
            ),
            topicsCovered = listOf(
                "LinkedIn Growth", "SEO Fundamentals", "Keyword Research",
                "Freelancing Platforms", "Client Hunting", "Proposal Writing", "Portfolio Building"
            ),
            curriculumModules = listOf(
                CourseModule(
                    1, "LinkedIn Personal Branding & SEO",
                    "Positioning yourself as an authority online.",
                    listOf(
                        CourseLesson("l701", "Optimizing Your LinkedIn Profile for Inbound Leads", "50 mins", isFreePreview = true),
                        CourseLesson("l702", "Keyword Research & Ranking Content on Google", "55 mins")
                    )
                ),
                CourseModule(
                    2, "Client Hunting & Pitching",
                    "Finding clients, closing deals, and receiving international payments.",
                    listOf(
                        CourseLesson("l703", "Upwork & Fiverr Profile Setup for Top Rated Badge", "50 mins"),
                        CourseLesson("l704", "High-Converting Proposal Templates & Pricing", "55 mins")
                    )
                )
            ),
            specialBenefits = listOf(
                "Proven Upwork & LinkedIn Proposal Templates",
                "Contract & Invoicing Templates for Freelancers",
                "International Payment Setup Guide (PayPal, Stripe, Wise)",
                "Certificate of Completion"
            ),
            idealFor = listOf("Students", "Job Seekers", "Freelancers", "Entrepreneurs", "Working Professionals"),
            earningOpportunity = "₹20,000 – ₹60,000/mo in freelancing & consulting",
            iconKey = "seo"
        ),
        Course(
            id = "he-digital-marketing-08",
            title = "Digital Marketing Course",
            subtitle = "A Practical and Results-Driven Course to Grow Brands & Run Smart Campaigns",
            category = CourseCategory.DIGITAL_MARKETING,
            duration = "80 Days",
            mode = "Online Live Classes + Recording Available",
            price = 3499,
            originalPrice = 9999,
            rating = 4.96f,
            reviewCount = 340,
            enrolledCount = 2400,
            tag = "Agency Grade",
            overview = "A practical and results-driven Digital Marketing Course designed to equip you with essential online marketing skills to grow brands, generate leads, and boost business. Master social media marketing, Google Ads, Meta Ads, campaign management, and analytics.",
            whatYouWillLearn = listOf(
                "Social Media Marketing on Instagram, Facebook & LinkedIn",
                "Google Search, Display & Video Ads Campaign Setup",
                "Meta Ads Manager Mastery (Audience Targeting & Retargeting)",
                "High-Converting Lead Generation Funnels",
                "Campaign Analytics, ROI Tracking & Pixel Setup",
                "Email Marketing & WhatsApp Business Automation",
                "Agency Management & Scaling Client Accounts"
            ),
            topicsCovered = listOf(
                "Social Media Marketing", "Google Ads", "Meta Ads",
                "Lead Generation", "Campaign Management", "Analytics & Reporting", "Conversion Optimization"
            ),
            curriculumModules = listOf(
                CourseModule(
                    1, "Social Media Marketing & Brand Building",
                    "Building organic reach and community engagement.",
                    listOf(
                        CourseLesson("l801", "Social Media Algorithms & Growth Strategies", "55 mins", isFreePreview = true),
                        CourseLesson("l802", "Content Calendars & Viral Promotion", "50 mins")
                    )
                ),
                CourseModule(
                    2, "Paid Ads & Performance Marketing",
                    "Running profitable ads on Meta and Google.",
                    listOf(
                        CourseLesson("l803", "Meta Ads Manager: Setup, Audiences & Copy", "65 mins"),
                        CourseLesson("l804", "Google Search & Performance Max Campaigns", "60 mins"),
                        CourseLesson("l805", "Analytics, ROI Optimization & Scaling", "55 mins")
                    )
                )
            ),
            specialBenefits = listOf(
                "Live Campaign Budget for Hands-on Practice",
                "Case Studies of ₹10 Lakh+ Ad Spends",
                "Agency Pitch Decks & Client Reporting Templates",
                "Industry Recognized Certificate"
            ),
            idealFor = listOf("Students", "Entrepreneurs", "Freelancers", "Job Seekers", "Business Owners"),
            earningOpportunity = "₹30,000 – ₹70,000/mo as performance marketer or agency owner",
            iconKey = "marketing"
        ),
        Course(
            id = "he-web-dev-09",
            title = "Website Development Course",
            subtitle = "Build Modern, Responsive & Professional Websites from Scratch to Live",
            category = CourseCategory.WEB_DEV,
            duration = "10 Days",
            mode = "Online Live Classes + Recordings Available",
            price = 1499,
            originalPrice = 4499,
            rating = 4.91f,
            reviewCount = 180,
            enrolledCount = 1400,
            tag = "No Code Needed",
            overview = "A practical website development program designed to help you build modern, responsive, and professional websites — from scratch to live. Learn WordPress website creation, responsive design, plugins & tools, domain & hosting basics, and complete a live website project.",
            whatYouWillLearn = listOf(
                "Domain Names & Hosting Setup made simple",
                "WordPress Installation & Modern Block/Elementor Design",
                "Responsive Design for Mobile, Tablet & Desktop",
                "Must-Have Plugins for Security, Speed & SEO",
                "Contact Forms, WhatsApp Chat & Booking Integrations",
                "Building E-Commerce Stores & Payment Gateway Integration",
                "Deploying a Real Live Website Project for your Portfolio"
            ),
            topicsCovered = listOf(
                "WordPress Website Creation", "Responsive Design", "Plugins & Tools",
                "Domain & Hosting Basics", "Live Website Project", "Payment Gateway Setup"
            ),
            curriculumModules = listOf(
                CourseModule(
                    1, "WordPress Foundation & Architecture",
                    "Setting up servers and building pages.",
                    listOf(
                        CourseLesson("l901", "Domain, Hosting & WordPress Setup", "45 mins", isFreePreview = true),
                        CourseLesson("l902", "Customizing Themes & Modern Page Builders", "55 mins")
                    )
                ),
                CourseModule(
                    2, "Responsive Pages & Live Deployment",
                    "Creating complete functional client websites.",
                    listOf(
                        CourseLesson("l903", "Mobile Optimization & Essential Plugins", "50 mins"),
                        CourseLesson("l904", "Building and Launching Live Client Project", "60 mins")
                    )
                )
            ),
            specialBenefits = listOf(
                "Free Subdomain & Sandbox Server for Practice",
                "Premium Themes & Plugin Templates Included",
                "Deploy a Live Portfolio Website during the course",
                "Course Certificate"
            ),
            idealFor = listOf("Students", "Beginners", "Freelancers", "Aspiring Web Developers"),
            earningOpportunity = "₹10,000 – ₹25,000 per website design project",
            iconKey = "web"
        ),
        Course(
            id = "he-ai-tools-10",
            title = "AI Tools Mastery Course",
            subtitle = "Explore 20+ Modern AI Tools for Productivity, Content Creation, Research & Design",
            category = CourseCategory.AI_AND_DESIGN,
            duration = "14 Days",
            mode = "Online Live Classes + Recordings Available",
            price = 899,
            originalPrice = 2499,
            rating = 4.94f,
            reviewCount = 220,
            enrolledCount = 1850,
            tag = "Future Ready",
            overview = "Explore various AI tools and learn how to use them for productivity, content creation, research, designing, and professional tasks. Stay ahead in your career and automate routine digital tasks effortlessly.",
            whatYouWillLearn = listOf(
                "Mastery over 20+ top AI Tools for workplace & freelance",
                "Advanced Prompt Engineering for ChatGPT, Claude & Gemini",
                "AI for High-Speed Research, Data Analysis & Summaries",
                "Automating Content Creation: Blogs, Emails, Social Posts",
                "AI Video and Voice Generation Tools",
                "Building Custom AI Workflows for daily business tasks"
            ),
            topicsCovered = listOf(
                "Trending AI Tools", "Prompt Engineering", "Productivity Automation",
                "Content Generation", "AI Research", "Workflow Optimization"
            ),
            curriculumModules = listOf(
                CourseModule(
                    1, "Generative Text & Research AI",
                    "Unlocking modern LLMs for practical output.",
                    listOf(
                        CourseLesson("l1001", "Prompt Crafting for Precision & Speed", "45 mins", isFreePreview = true),
                        CourseLesson("l1002", "Automating Work Reports, Emails & Summaries", "50 mins")
                    )
                ),
                CourseModule(
                    2, "Creative & Multimodal AI Tools",
                    "Voice, video, and image tools.",
                    listOf(
                        CourseLesson("l1003", "AI Video & Voice Cloning for Creators", "55 mins"),
                        CourseLesson("l1004", "Building Automated Daily Productivity Stacks", "50 mins")
                    )
                )
            ),
            specialBenefits = listOf(
                "Curated 100+ Prompt Swipe File",
                "AI Tools Directory & Discount Codes",
                "Recorded Sessions Access",
                "Verified Course Certificate"
            ),
            idealFor = listOf("Students", "Working Professionals", "Entrepreneurs", "Freelancers"),
            earningOpportunity = "Save 15+ hours weekly & scale freelance output 3x",
            iconKey = "ai"
        )
    )

    // Reactive State
    private val _testimonialsState = MutableStateFlow(initialTestimonials)
    val testimonials: StateFlow<List<Testimonial>> = _testimonialsState.asStateFlow()

    private val _enrolledCoursesState = MutableStateFlow<Map<String, EnrolledCourse>>(
        // Give the user one starter enrolled course so they can immediately test "My Learning" & lessons!
        mapOf(
            "he-canva-ai-02" to EnrolledCourse(
                courseId = "he-canva-ai-02",
                enrollmentDate = "Today",
                orderId = "HE-ENR-84920",
                selectedBatch = "Evening Batch (7:00 PM - 8:30 PM)",
                progressPercent = 35,
                completedLessons = setOf("l201"),
                isCompleted = false
            )
        )
    )
    val enrolledCourses: StateFlow<Map<String, EnrolledCourse>> = _enrolledCoursesState.asStateFlow()

    private val _recentOrdersState = MutableStateFlow<List<OrderReceipt>>(emptyList())
    val recentOrders: StateFlow<List<OrderReceipt>> = _recentOrdersState.asStateFlow()

    fun getAllCourses(): List<Course> = allCourses

    fun getCourseById(id: String): Course? = allCourses.find { it.id == id }

    fun isCourseEnrolled(courseId: String): Boolean = _enrolledCoursesState.value.containsKey(courseId)

    fun getEnrolledCourse(courseId: String): EnrolledCourse? = _enrolledCoursesState.value[courseId]

    // Enroll & Buy Course
    fun enrollInCourse(
        course: Course,
        selectedBatch: String,
        amountPaid: Int,
        discountApplied: Int,
        couponCode: String?,
        paymentMethod: String,
        learnerName: String,
        learnerPhone: String
    ): OrderReceipt {
        val orderId = "HE-" + (10000..99999).random()
        val txId = "TXN" + UUID.randomUUID().toString().take(10).uppercase()
        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        val timestamp = dateFormat.format(Date())

        val receipt = OrderReceipt(
            orderId = orderId,
            courseId = course.id,
            courseTitle = course.title,
            selectedBatch = selectedBatch,
            amountPaid = amountPaid,
            originalPrice = course.originalPrice,
            discountApplied = discountApplied,
            couponCode = couponCode,
            paymentMethod = paymentMethod,
            transactionId = txId,
            timestamp = timestamp,
            learnerName = learnerName.ifBlank { "Learner" },
            learnerPhone = learnerPhone.ifBlank { CONTACT_PHONE_FORMATTED }
        )

        val updatedMap = _enrolledCoursesState.value.toMutableMap()
        updatedMap[course.id] = EnrolledCourse(
            courseId = course.id,
            enrollmentDate = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date()),
            orderId = orderId,
            selectedBatch = selectedBatch,
            progressPercent = 0,
            completedLessons = emptySet(),
            isCompleted = false
        )
        _enrolledCoursesState.value = updatedMap

        val updatedOrders = _recentOrdersState.value.toMutableList()
        updatedOrders.add(0, receipt)
        _recentOrdersState.value = updatedOrders

        return receipt
    }

    // Toggle Lesson Complete
    fun toggleLessonCompletion(courseId: String, lessonId: String) {
        val current = _enrolledCoursesState.value[courseId] ?: return
        val course = getCourseById(courseId) ?: return
        val totalLessons = course.curriculumModules.sumOf { it.lessons.size }.coerceAtLeast(1)

        val updatedLessons = current.completedLessons.toMutableSet()
        if (updatedLessons.contains(lessonId)) {
            updatedLessons.remove(lessonId)
        } else {
            updatedLessons.add(lessonId)
        }

        val progress = ((updatedLessons.size.toFloat() / totalLessons) * 100).toInt().coerceIn(0, 100)
        val isDone = progress == 100

        val updated = current.copy(
            completedLessons = updatedLessons,
            progressPercent = progress,
            isCompleted = isDone,
            certificateId = if (isDone) "HE-CERT-${course.id.takeLast(4)}-${(1000..9999).random()}" else current.certificateId
        )

        val updatedMap = _enrolledCoursesState.value.toMutableMap()
        updatedMap[courseId] = updated
        _enrolledCoursesState.value = updatedMap
    }

    // Add user feedback testimonial
    fun addTestimonial(
        name: String,
        role: String,
        location: String,
        courseName: String,
        rating: Float,
        comment: String,
        earningsOutcome: String
    ) {
        val newTestimonial = Testimonial(
            id = "test-" + UUID.randomUUID().toString().take(6),
            name = name.ifBlank { "Anonymous Student" },
            role = role.ifBlank { "Learner" },
            location = location.ifBlank { "India" },
            courseName = courseName,
            rating = rating,
            comment = comment,
            earningsOutcome = earningsOutcome.ifBlank { "Gained Career Confidence" },
            verifiedBuyer = true,
            date = "Just now"
        )
        val updated = _testimonialsState.value.toMutableList()
        updated.add(0, newTestimonial)
        _testimonialsState.value = updated
    }

    // Validate Coupon
    fun applyCoupon(code: String, coursePrice: Int): Int {
        val trimmed = code.trim().uppercase()
        return when (trimmed) {
            "HARSHIKA10" -> (coursePrice * 0.10).toInt()
            "SKILLS500" -> if (coursePrice >= 1500) 500 else 150
            "EARLYBIRD" -> 200
            "WELCOME" -> 150
            else -> 0
        }
    }
}
