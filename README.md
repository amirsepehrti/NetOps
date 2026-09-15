<div align="center">

<img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.webp" width="96" alt="NetOps Mobile icon">

# NetOps Mobile

**An all-in-one network diagnostic toolbox, device monitor and console for network engineers — on Android.**

[![Build & Publish Android Release APK](https://github.com/amirsepehrti/NetOps/actions/workflows/release.yml/badge.svg)](https://github.com/amirsepehrti/NetOps/actions/workflows/release.yml)
[![Latest release](https://img.shields.io/github/v/release/amirsepehrti/NetOps?label=release)](https://github.com/amirsepehrti/NetOps/releases/latest)
[![Android](https://img.shields.io/badge/Android-7.0%2B%20(API%2024)-3DDC84?logo=android&logoColor=white)](#requirements)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)

[**English**](#english) · [**فارسی**](#فارسی)

</div>

---

<a id="english"></a>

## English

### Overview

NetOps Mobile packs the diagnostics you normally reach for on a laptop — ping, port scanning, traceroute, DNS, subnetting, Wake-on-LAN, throughput testing — into a single Android app built entirely with Jetpack Compose.

It runs every test from the phone itself. There is no NetOps backend, no account and no telemetry: your targets, sites and results stay in a local database on the device.

### Features

**Diagnostic toolbox — 15 tools**

| Tool | What it does |
| --- | --- |
| ICMP / TCP Ping | Live latency stream with loss ratio and min/avg/max RTT |
| Port Scanner | TCP connect scan over common ports, a custom range or a single port |
| Subnet Calculator | Netmask, network, broadcast, usable range, host count, plus sub-splitting |
| DNS Lookup | Record lookups against the system resolver or a DNS server you choose |
| Wake-On-LAN | Magic packets to a MAC and broadcast address, with saved targets |
| Traceroute Visualizer | Streams the path hop by hop |
| WHOIS & IP Geolocation | Registration and location data for a domain or address |
| Network Speed Test | Throughput measurement with live progress |
| Traffic Generator | Generates load against a target for link testing |
| Bandwidth Client & Server | Point-to-point throughput between two devices |
| SNMP Discovery & Walk | Discovers devices and walks interface tables |
| WAN Killer Congestion Tester | Saturates a link to observe behaviour under congestion |
| Subnet MAC Scanner | Sweeps a CIDR range and resolves hosts with vendor lookup |
| Wi-Fi Signal & Sniffer | Signal strength, nearby access points and traffic panel |
| BTS Cell Tower Diagnostics | Cellular radar and RF diagnostics |

**The rest of the app**

- **Dashboard** — a configurable home screen. Choose single-list or compact HUD density and toggle each widget: matrix rain header, diagnostic stream, telemetry shortcuts, incident log, sniffer panel, antenna radar and discovered devices.
- **Inventory** — organise devices into sites, stored locally.
- **Alerts** — a unified incident and diagnostic alert log.
- **Terminal** — a console with saved command snippets and a scriptable prompt. Note that the remote session is a *simulator* for practising command flow; it is not a real SSH implementation and does not open a network connection to a host.
- **History** — recent runs are saved so you can revisit results, with backup and restore for your data.
- **12 themes** — Skeuomorphic Console, Skeuomorphic Aluminum, Nord Slate, Matrix Green, Cyberpunk Neo, Ocean Blue, Solarized Dark, Dracula, Monokai Pro, Retro Gold, Obsidian Stealth and Classic Light, with a top or bottom navigation dock.

### Install

Download the APK from the [latest release](https://github.com/amirsepehrti/NetOps/releases/latest) and open it on your device. You will need to allow installation from unknown sources.

> **On signing:** unless an upload keystore is configured for the repository (see [Releases](#releases)), the published APK is signed with a debug key. Android will warn about the installation source, and a debug-signed build cannot be installed over one signed with a different key — uninstall the old version first.

<a id="requirements"></a>
**Requirements:** Android 7.0 (API 24) or newer. Built against API 36.

### Permissions

| Permission | Why it is needed |
| --- | --- |
| `INTERNET`, `ACCESS_NETWORK_STATE` | Running the diagnostics and reading connection state |
| `ACCESS_WIFI_STATE`, `CHANGE_WIFI_STATE`, `NEARBY_WIFI_DEVICES` | Wi-Fi scanning and signal diagnostics |
| `ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION` | Android requires location permission to return Wi-Fi scan results |
| `READ_PHONE_STATE` | Cell tower and RF diagnostics |

Nothing is uploaded anywhere. Results live in an on-device Room database.

### Build from source

```bash
git clone https://github.com/amirsepehrti/NetOps.git
cd NetOps
./gradlew assembleDebug
```

The APK lands in `app/build/outputs/apk/debug/`. You need JDK 17 and an Android SDK with API 36; Gradle and the Android Gradle Plugin come from the wrapper and version catalog.

For a release build, `./gradlew assembleRelease` works without any setup — it falls back to a debug signing key and logs a warning. To sign properly, point `KEYSTORE_PATH` at your keystore and set `STORE_PASSWORD`, `KEY_ALIAS` and `KEY_PASSWORD` in the environment.

`.env.example` documents a `GEMINI_API_KEY` entry inherited from the project scaffold. No shipping code reads it, so you can ignore it.

<a id="releases"></a>
### Releases

`.github/workflows/release.yml` builds the APK and publishes a GitHub release. It runs on a push to `main`, on any `v*` tag, or on demand from the Actions tab.

The tag and release notes are derived from `versionName` in `app/build.gradle.kts`, so cutting a release means bumping `versionCode` and `versionName` there. A tag push uses its own tag name instead.

To publish properly signed builds, add these repository secrets:

| Secret | Contents |
| --- | --- |
| `KEYSTORE_BASE64` | Your upload keystore, base64 encoded |
| `STORE_PASSWORD` | Keystore password |
| `KEY_ALIAS` | Key alias (defaults to `upload`) |
| `KEY_PASSWORD` | Key password |

### Tech stack

Kotlin · Jetpack Compose with Material 3 · Room · OkHttp, Retrofit and Moshi · Kotlin coroutines and Flow · KSP · Robolectric and Roborazzi for tests · Gradle version catalog

```
app/src/main/java/com/example/
├── MainActivity.kt          navigation, dock and dashboard customization
├── data/
│   ├── NetworkEngine.kt     the diagnostic implementations
│   ├── Database.kt          Room entities and DAO
│   └── NetOpsRepository.kt
└── ui/
    ├── NetOpsViewModel.kt   app state
    ├── screens/             Dashboard, Toolbox, Devices, Alerts, Terminal, Settings
    ├── dialogs/             operator profile, backup and restore
    └── theme/               the 12 themes
```

### Contributing

Issues and pull requests are welcome. Please run `./gradlew test` before opening a pull request.

### License

No license has been chosen for this project yet. Until one is added, default copyright applies and others have no right to reuse the code — worth settling before the repository goes public.

---

<div dir="rtl" align="right">

<a id="فارسی"></a>

## فارسی

### معرفی

نت‌اپس موبایل همان ابزارهایی را که معمولاً روی لپ‌تاپ سراغشان می‌روید — پینگ، اسکن پورت، تریسروت، DNS، محاسبهٔ ساب‌نت، Wake-on-LAN و تست پهنای باند — در یک اپلیکیشن اندرویدی جمع کرده که تماماً با Jetpack Compose نوشته شده است.

همهٔ تست‌ها روی خودِ گوشی اجرا می‌شوند. هیچ سرور پشتیبانی، حسابِ کاربری یا ارسال آمار در کار نیست: هدف‌ها، سایت‌ها و نتایج شما در یک پایگاه دادهٔ محلی روی دستگاه می‌مانند.

### امکانات

**جعبه‌ابزار عیب‌یابی — ۱۵ ابزار**

| ابزار | کاری که انجام می‌دهد |
| --- | --- |
| پینگ ICMP / TCP | نمایش زندهٔ تأخیر همراه با نرخ اتلاف و کمینه/میانگین/بیشینهٔ RTT |
| اسکنر پورت | اسکن TCP Connect روی پورت‌های رایج، یک بازهٔ دلخواه یا یک پورت مشخص |
| ماشین‌حساب ساب‌نت | نت‌ماسک، آدرس شبکه، برادکست، بازهٔ قابل‌استفاده، تعداد هاست و تقسیم به زیرشبکه |
| جست‌وجوی DNS | واکشی رکوردها از resolver سیستم یا هر سرور DNS دلخواه |
| Wake-On-LAN | ارسال Magic Packet به MAC و آدرس برادکست، با امکان ذخیرهٔ هدف‌ها |
| نمایش تریسروت | نمایش مسیر به‌صورت گام‌به‌گام و زنده |
| WHOIS و موقعیت IP | اطلاعات ثبت و موقعیت جغرافیایی یک دامنه یا آدرس |
| تست سرعت شبکه | اندازه‌گیری پهنای باند با پیشرفت لحظه‌ای |
| مولد ترافیک | تولید بار روی یک هدف برای تست لینک |
| کلاینت و سرور پهنای باند | سنجش توان عبوری نقطه‌به‌نقطه میان دو دستگاه |
| کشف و پیمایش SNMP | شناسایی تجهیزات و پیمایش جدول اینترفیس‌ها |
| تست ازدحام WAN Killer | اشباع لینک برای مشاهدهٔ رفتار شبکه در شرایط ازدحام |
| اسکنر MAC ساب‌نت | جاروب یک بازهٔ CIDR و شناسایی هاست‌ها به‌همراه سازنده |
| سیگنال و اسنیفر وای‌فای | قدرت سیگنال، اکسس‌پوینت‌های اطراف و پنل ترافیک |
| عیب‌یابی دکل BTS | رادار سلولی و تشخیص وضعیت رادیویی |

**بقیهٔ اپلیکیشن**

- **داشبورد** — صفحهٔ خانهٔ قابل شخصی‌سازی. چگالی نمایش را بین فهرست ساده و HUD فشرده انتخاب کنید و هر ویجت را جداگانه روشن یا خاموش کنید: هدر Matrix Rain، جریان تشخیصی، میان‌برهای تله‌متری، گزارش رخدادها، پنل اسنیفر، رادار آنتن و فهرست دستگاه‌های کشف‌شده.
- **موجودی تجهیزات** — دسته‌بندی دستگاه‌ها ذیل سایت‌ها، با ذخیرهٔ محلی.
- **هشدارها** — گزارش یکپارچهٔ رخدادها و هشدارهای تشخیصی.
- **ترمینال** — کنسولی با قابلیت ذخیرهٔ قطعه‌دستورها. توجه کنید که نشست راه دور یک **شبیه‌ساز** برای تمرین جریان دستورهاست؛ پیاده‌سازی واقعی SSH نیست و اتصال شبکه‌ای به هاست برقرار نمی‌کند.
- **تاریخچه** — اجراهای اخیر ذخیره می‌شوند تا بتوانید نتایج را دوباره ببینید، به‌همراه پشتیبان‌گیری و بازیابی داده‌ها.
- **۱۲ پوستهٔ ظاهری** — Skeuomorphic Console، Skeuomorphic Aluminum، Nord Slate، Matrix Green، Cyberpunk Neo، Ocean Blue، Solarized Dark، Dracula، Monokai Pro، Retro Gold، Obsidian Stealth و Classic Light، با امکان قرار دادن نوار ناوبری در بالا یا پایین صفحه.

### نصب

فایل APK را از [آخرین ریلیز](https://github.com/amirsepehrti/NetOps/releases/latest) دانلود کنید و روی دستگاه باز کنید. لازم است اجازهٔ نصب از منابع ناشناس را فعال کنید.

> **دربارهٔ امضای برنامه:** تا زمانی که کلید امضای اختصاصی برای مخزن تنظیم نشده باشد (بخش [ریلیزها](#ریلیزها))، فایل منتشرشده با کلید دیباگ امضا می‌شود. اندروید دربارهٔ منبع نصب هشدار می‌دهد و چنین نسخه‌ای روی نسخه‌ای که با کلید دیگری امضا شده نصب نمی‌شود — ابتدا نسخهٔ قبلی را حذف کنید.

**پیش‌نیاز:** اندروید ۷٫۰ (API 24) یا بالاتر. ساخته‌شده روی API 36.

### دسترسی‌ها

| دسترسی | دلیل نیاز |
| --- | --- |
| `INTERNET` و `ACCESS_NETWORK_STATE` | اجرای تست‌ها و خواندن وضعیت اتصال |
| `ACCESS_WIFI_STATE`، `CHANGE_WIFI_STATE` و `NEARBY_WIFI_DEVICES` | اسکن وای‌فای و سنجش سیگنال |
| `ACCESS_FINE_LOCATION` و `ACCESS_COARSE_LOCATION` | اندروید برای برگرداندن نتایج اسکن وای‌فای دسترسی موقعیت مکانی را الزامی کرده است |
| `READ_PHONE_STATE` | عیب‌یابی دکل مخابراتی و وضعیت رادیویی |

هیچ داده‌ای جایی آپلود نمی‌شود؛ نتایج در یک پایگاه دادهٔ Room روی خود دستگاه ذخیره می‌شوند.

### ساخت از روی کد منبع

```bash
git clone https://github.com/amirsepehrti/NetOps.git
cd NetOps
./gradlew assembleDebug
```

خروجی در مسیر `app/build/outputs/apk/debug/` ساخته می‌شود. به JDK 17 و Android SDK با API 36 نیاز دارید؛ خودِ Gradle و افزونهٔ اندروید از طریق wrapper و version catalog تأمین می‌شوند.

برای ساخت نسخهٔ ریلیز، دستور `./gradlew assembleRelease` بدون هیچ تنظیم اضافه‌ای کار می‌کند و در نبودِ کلید، به کلید دیباگ برمی‌گردد و هشدار می‌دهد. برای امضای درست، مسیر کلید را در `KEYSTORE_PATH` و مقادیر `STORE_PASSWORD`، `KEY_ALIAS` و `KEY_PASSWORD` را در محیط تنظیم کنید.

در فایل `.env.example` کلیدی با نام `GEMINI_API_KEY` از قالب اولیهٔ پروژه باقی مانده است. هیچ بخشی از کد فعلی آن را نمی‌خواند و می‌توانید نادیده‌اش بگیرید.

<a id="ریلیزها"></a>
### ریلیزها

فایل `.github/workflows/release.yml` نسخهٔ APK را می‌سازد و یک ریلیز روی گیت‌هاب منتشر می‌کند. این روند با push روی شاخهٔ `main`، با هر تگِ `v*`، یا به‌صورت دستی از تب Actions اجرا می‌شود.

نام تگ و متن ریلیز از روی `versionName` در `app/build.gradle.kts` ساخته می‌شود؛ بنابراین برای انتشار نسخهٔ تازه کافی است `versionCode` و `versionName` را همان‌جا بالا ببرید. در push یک تگ، نام همان تگ استفاده می‌شود.

برای انتشار نسخه‌های امضاشده، این Secretها را به مخزن اضافه کنید:

| Secret | محتوا |
| --- | --- |
| `KEYSTORE_BASE64` | فایل keystore به‌صورت base64 |
| `STORE_PASSWORD` | گذرواژهٔ keystore |
| `KEY_ALIAS` | نام کلید (پیش‌فرض `upload`) |
| `KEY_PASSWORD` | گذرواژهٔ کلید |

### فناوری‌های به‌کاررفته

Kotlin · Jetpack Compose و Material 3 · Room · OkHttp و Retrofit و Moshi · کوروتین و Flow · KSP · Robolectric و Roborazzi برای تست‌ها · Gradle version catalog

```
app/src/main/java/com/example/
├── MainActivity.kt          ناوبری، نوار داک و شخصی‌سازی داشبورد
├── data/
│   ├── NetworkEngine.kt     پیاده‌سازی ابزارهای تشخیصی
│   ├── Database.kt          موجودیت‌ها و DAO مربوط به Room
│   └── NetOpsRepository.kt
└── ui/
    ├── NetOpsViewModel.kt   وضعیت اپلیکیشن
    ├── screens/             داشبورد، جعبه‌ابزار، تجهیزات، هشدارها، ترمینال، تنظیمات
    ├── dialogs/             پروفایل اپراتور، پشتیبان‌گیری و بازیابی
    └── theme/               دوازده پوستهٔ ظاهری
```

### مشارکت

ایشو و پول‌ریکوئست پذیرفته می‌شود. لطفاً پیش از باز کردن پول‌ریکوئست، `./gradlew test` را اجرا کنید.

### مجوز

هنوز مجوزی برای این پروژه انتخاب نشده است. تا وقتی مجوزی اضافه نشود، کپی‌رایت پیش‌فرض برقرار است و دیگران حق استفادهٔ مجدد از کد را ندارند — بهتر است پیش از عمومی‌کردن مخزن تکلیفش روشن شود.

</div>
