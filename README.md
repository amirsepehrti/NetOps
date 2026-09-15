# NetOps Mobile 🌐⚡

<div align="center">

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Database](https://img.shields.io/badge/Storage-Room%20(SQLite)-00599C?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)

**All-in-one offline network toolbox, telemetry monitor, and terminal console for network engineers, sysadmins, and DevOps.**

🌐 **Language / زبان:** [فارسی (Persian)](#-درباره-برنامه-about) • [English (README)](#-netops-mobile---english-version)

</div>

---

## 📖 درباره برنامه (About)
**NetOps Mobile** یک دستیار جامع، مهندسی‌شده و آفلاین برای پایش، عیب‌یابی و مدیریت تجهیزات شبکه در پلتفرم اندروید است. این اپلیکیشن با استفاده از آخرین استانداردهای **Jetpack Compose** و معماری **Material 3** همراه با طراحی نوستالژیک و مدرن **Skeuomorphic Rack Console** توسعه یافته تا بالاترین کارایی را در بررسی میدانی رک‌ها، سوییچ‌ها، روترها و سرورها فراهم آورد.

---

## ✨ ویژگی‌های کلیدی (Key Features)

### 1. 🛠️ جعبه‌ابزار مهندسی شبکه (Network Toolbox)
مجموعه‌ای از ۱۶ ابزار اختصاصی برای تست، ارزیابی و آنالیز بدون وابستگی به اینترنت خارجی:
* **ICMP & TCP Ping:** پینگ با امکان تنظیم Timeout، پکت‌سایز و نمایش نمودار زنده لتنسی (Jitter / Latency).
* **Multi-Threaded Port Scanner:** اسکن سریع پورت‌های مشهور (Well-known) یا بازه دلخواه (Range) با تشخیص وضعیت Open/Filtered/Closed.
* **Subnet & CIDR Calculator:** محاسبه ماسک، Broadcast، آدرس‌های قابل تخصیص و تحلیل VLSM به همراه نمایش باینری.
* **DNS Lookup:** واکشی رکوردهای DNS (A, AAAA, MX, TXT, CNAME, NS, SOA) با امکان تغییر DNS سرور هدف.
* **Wake-on-LAN (WoL):** ارسال بسته‌های جادویی (Magic Packet) به آدرس‌های MAC هدف به همراه لیست ذخیره‌شده دائمی.
* **Visual Traceroute:** ردیابی مسیر گره‌های بین‌مبدا و مقصد با محاسبه زمان رفت و برگشت هر هاپ.
* **WHOIS Lookup:** دریافت شناسنامه کامل دامنه‌ها و بلوک‌های آی‌پی.
* **Speed Test & Bandwidth Analyzer:** سنجش نرخ انتقال زنده دانلود و آپلود بر روی کارت‌های شبکه فعال.
* **Traffic Generator & WAN Killer:** شبیه‌ساز ترافیک جهت تست استرس پهنای باند و مقاومت فایروال‌ها.
* **SNMP Discovery:** پویش دیوایس‌ها بر اساس پاسخ‌های SNMP v1/v2c.
* **MAC / ARP Scanner:** کشف آدرس مک و Vendor سازنده کارت شبکه دیوایس‌های موجود در شبکه محلی.
* **WiFi & Cell Tower Diagnostics:** مانیتورینگ دقیق قدرت سیگنال (RSSI/dBm)، فرکانس، کانال، SSID و اطلاعات دکل‌های مخابراتی.

### 2. 📊 داشبورد و مانیتورینگ زنده (Live Telemetry Dashboard)
* نمایش مشخصات کامل اینترفیس‌های شبکه فعال (Wi-Fi، دیتای سیم‌کارت، VPN، اترنت).
* نمودار زنده ترافیک Rx / Tx بر حسب کیلوبایت و مگابایت بر ثانیه.
* تفکیک وضعیت دسترسی به Gateway و اینترنت خارجی با نشانگرهای نئونی وضعیت.

### 3. 🏢 مدیریت موجودی و سایت‌ها (Devices & Sites Inventory)
* سازمان‌دهی دیوایس‌ها در قالب سایت‌ها/شعبات مختلف (Data Center, HQ, Branch, etc.).
* رصد وضعیت سلامت دستگاه‌ها (Online, Degraded, Offline).
* ثبت پورت‌های مدیریتی، آدرس‌های IP و توضیحات مربوط به هر تجهیز.

### 4. 🚨 نگهبان اعلان‌ها و هشدارها (Guardian Alert Center)
* ثبت بلادرنگ رخدادها و خطاهای قطعی سرورها یا سرویس‌ها.
* فیلتربندی بر اساس سطح حساسیت (Critical, Warning, Info).
* امکان بررسی (Acknowledge) و پاک‌سازی هشدارهای بررسی‌شده.

### 5. 💻 کنسول ترمینال و اسکریپت‌ها (Terminal & Snippets)
* محیط شبیه‌ساز شل اختصاصی جهت اجرای دستورات تشخیصی.
* ذخیره و دسته‌بندی قطعه‌کدهای پرکاربرد (SSH Snippets) با قابلیت فراخوانی با یک کلیک.

### 6. 🎨 رابط کاربری اسکیومورفیک و تم‌های چندگانه
* پوسته **Skeuomorphic Rack Console** (شبیه به پنل سوییچ‌ها و تجهیزات فیزیکی سیسکو و سرورهای رک‌مونت).
* تم فلزی صنعتی **Brushed Aluminum**، تم تیره مدرن **Cyber Black** و تم روشن اختصاصی.
* پشتیبانی از جابه‌جایی داک ناوبری (منوی بالا یا منوی پایین) با انیمیشن‌های ۶۰ فریم بر ثانیه.

### 7. 🔒 امنیت، حریم خصوصی و پشتیبان‌گیری
* **کاملاً آفلاین:** بدون ارسال داده‌ها به سرور شخص ثالث.
* **پایگاه‌داده محلی SQLite / Room:** نگهداری دائمی اطلاعات سایت‌ها، دیوایس‌ها و اسنیپت‌ها.
* **Backup & Restore:** قابلیت خروجی‌گرفتن کامل از دیتابیس به صورت فایل خوانای JSON و بازیابی مجدد آن.

---

## 🏗️ معماری و فناوری‌ها (Tech Stack)

| لایه | تکنولوژی / کتابخانه |
| :--- | :--- |
| **زبان برنامه‌نویسی** | Kotlin (100%) |
| **طراحی رابط کاربری** | Jetpack Compose + Material Design 3 |
| **معماری** | MVVM (Model-View-ViewModel) + Clean Data Architecture |
| **مدیریت وضعیت و همروندی** | StateFlow, SharedFlow, Kotlin Coroutines |
| **پایگاه داده محلی** | Room Database (SQLite Engine) + KSP |
| **فریم‌ورک بیلد** | Gradle Kotlin DSL (`build.gradle.kts`) |
| **پایپ‌لاین CI/CD** | GitHub Actions (خودکارسازی ساخت و انتشار APK) |

---

## 🚀 راهنمای بیلد و نصب (Build & Installation)

### ۱. نصب مستقیم فایل APK آماده
برای نصب آخرین نسخه نیازی به کامپایل کدها ندارید:
1. به سربرگ [Releases](https://github.com) در همین مخزن بروید.
2. فایل `NetOps-Mobile.apk` را دانلود و روی گوشی اندرویدی خود نصب کنید.

### ۲. بیلد از طریق سورس‌کد
اگر مایل به کامپایل سورس‌کد در سیستم محلی خود هستید:

```bash
# کلون کردن مخزن
git clone https://github.com/USERNAME/NetOps.git
cd NetOps

# ساخت نسخه دیباگ با استفاده از Gradle Wrapper
./gradlew assembleDebug

# فایل خروجی در مسیر زیر قرار خواهد گرفت:
# app/build/outputs/apk/debug/app-debug.apk
```

---

## ⚙️ یکپارچه‌سازی با گیت‌هاب (CI/CD Pipeline)
این مخزن مجهز به فایل گردش‌کار هوشمند در مسیر `.github/workflows/release.yml` است.
* با هر `push` به شاخه‌های اصلی (`main` / `master`) یا ارسال تگ نسخه (مانند `v1.0.0`)، گیت‌هاب اکشن به صورت اتوماتیک:
  1. محیط Java 17 و ابزارهای بیلد اندروید را آماده می‌سازد.
  2. امضای امنیتی برنامه‌نویسی را بازسازی می‌کند.
  3. فایل نصبی `NetOps-Mobile.apk` را خروجی می‌گیرد.
  4. یک **Release رسمی جدید** ساخته و APK را برای دانلود مستقیم عموم پیوست می‌کند.

---

## 📄 مجوز (License)
این پروژه تحت مجوز MIT منتشر شده است. برای اطلاعات بیشتر فایل `LICENSE` را مطالعه فرمایید.

---
---

# 🌐 NetOps Mobile - English Version

<div align="center">

**All-in-one offline network diagnostics toolbox, real-time telemetry monitor, and terminal console for network engineers, sysadmins, and DevOps professionals.**

[Download Latest APK](#-direct-apk-installation) • [Key Features](#-key-features) • [Tech Stack](#-tech-stack) • [Build & Installation](#-build--installation)

</div>

---

## 📖 About
**NetOps Mobile** is a robust, precision-engineered Android network utility designed for field technicians, system administrators, and network engineers. Built with modern **Jetpack Compose** and **Material Design 3**, it combines high-performance raw socket diagnostics with an authentic, tactile **Skeuomorphic Rack Console** interface reminiscent of enterprise Cisco gear and server racks.

The app operates **100% offline** without any telemetry tracking, cloud dependencies, or third-party data collection.

---

## ✨ Key Features

### 1. 🛠️ Network Engineering Toolbox
A comprehensive suite of 16 built-in diagnostic and forensic tools:
* **ICMP & TCP Ping:** Configurable timeouts, packet payload sizes, and real-time latency jitter charts.
* **Multi-Threaded Port Scanner:** Fast concurrent scanning for common well-known services or custom target ranges with Open/Filtered/Closed state determination.
* **Subnet & CIDR Calculator:** Instant netmask calculations, broadcast determination, usable host ranges, and full VLSM breakdown with binary representations.
* **DNS Lookup:** Detailed record queries (A, AAAA, MX, TXT, CNAME, NS, SOA) with custom DNS resolver support.
* **Wake-on-LAN (WoL):** Broadcast UDP magic packets to target MAC addresses with persistent target inventory.
* **Visual Traceroute:** Hop-by-hop packet route visualization with individual RTT timing.
* **WHOIS Domain & IP Lookup:** Query registrar and registry information for public IP blocks and domains.
* **Speed Test & Bandwidth Analyzer:** Direct local interface throughput benchmark measuring download and upload bitrates.
* **Traffic Generator & WAN Killer:** Controlled packet generation to stress-test bandwidth capacity, switch buffers, and firewall throughput.
* **SNMP Discovery:** Scan subnets for SNMP-responsive nodes (SNMP v1/v2c MIB queries).
* **MAC / ARP Scanner:** Discover live network neighbors with automated OUI vendor identification.
* **WiFi & Cell Tower Diagnostics:** Inspect Wi-Fi RSSI (dBm), channel frequency, SSID details, link speed, and cellular network telemetry.

### 2. 📊 Live Telemetry Dashboard
* Active interface detection (Wi-Fi, Mobile Data, VPN, Ethernet).
* High-frequency live Rx / Tx bandwidth throughput graphs (KB/s & MB/s).
* Instant connectivity status indicators with LED indicators for Gateway and Internet reachability.

### 3. 🏢 Devices & Multi-Site Inventory
* Organize servers, switches, APs, and endpoints across distinct locations (e.g. Data Center, HQ, Branches).
* Live status tracking (Online, Degraded, Offline).
* Store management ports, IP assignments, MAC addresses, and operational notes.

### 4. 🚨 Guardian Alert Center
* Real-time automated logging of ping spikes, port drops, and connectivity loss.
* Severity triage filtering (Critical, Warning, Info).
* One-click alert acknowledgment, status flagging, and audit log clearance.

### 5. 💻 Terminal Console & Command Snippets
* Integrated shell emulator for executing network commands and scripts.
* Quick-access categorized SSH / Shell command snippet library with instant parameter interpolation.

### 6. 🎨 Skeuomorphic Rack UI & Custom Themes
* **Skeuomorphic Rack Console:** Realistic server rack aesthetic featuring knurled hardware screws, rack ears, status LEDs, and brushed finishes.
* **Theme Options:** Industrial Brushed Aluminum, Cyber Stealth Black, and Clean High-Contrast Light.
* **Dynamic Dock Position:** Switch between Top-Rack Console and Bottom Dock navigation with smooth 60 FPS transitions.

### 7. 🔒 Privacy & Data Persistence
* **100% Offline-First:** No accounts, no telemetry, no background analytics.
* **SQLite / Room Engine:** Reliable local persistence for all your sites, nodes, and terminal snippets.
* **Backup & Restore:** Full database export and import as human-readable JSON files for backup across devices.

---

## 🏗️ Tech Stack

| Component | Technology |
| :--- | :--- |
| **Language** | Kotlin (100%) |
| **UI Framework** | Jetpack Compose + Material Design 3 |
| **Architecture** | MVVM (Model-View-ViewModel) + Repository Pattern |
| **State & Concurrency** | StateFlow, SharedFlow, Kotlin Coroutines |
| **Local Persistence** | Android Room Database (SQLite Engine) + KSP |
| **Build Tooling** | Gradle Kotlin DSL (`build.gradle.kts`) |
| **Automation & CI/CD** | GitHub Actions Workflow (`.github/workflows/release.yml`) |

---

## 🚀 Build & Installation

### 1. Direct APK Installation
Pre-built APK binaries are ready to install without compiling:
1. Navigate to the [Releases](../../releases) tab of this repository.
2. Download `NetOps-Mobile.apk` and install it on your Android device (Android 8.0+ / API 26+ supported).

### 2. Building from Source
Prerequisites: JDK 17+ and Android SDK.

```bash
# Clone the repository
git clone https://github.com/USERNAME/NetOps.git
cd NetOps

# Build debug APK using Gradle Wrapper
./gradlew assembleDebug

# Output APK location:
# app/build/outputs/apk/debug/app-debug.apk
```

---

## ⚙️ Automated CI/CD Pipeline (GitHub Actions)
This repository includes an automated build workflow (`.github/workflows/release.yml`).
* On every `push` to `main` / `master` or release tag (e.g. `v1.1.0`), GitHub Actions:
  1. Sets up the Temurin OpenJDK 17 environment.
  2. Generates the necessary keystore certificates.
  3. Executes `./gradlew assembleDebug --no-daemon`.
  4. Automatically publishes a formal **GitHub Release** with `NetOps-Mobile.apk` attached as a downloadable binary.

---

## 📄 License
Distributed under the MIT License. See `LICENSE` for more information.

