این پروژهٔ نمونهٔ اندروید برای بسته‌بندی دارایی‌های `CandleMaster` ساخته شده است.

چکیدهٔ گام‌ها برای ساخت APK محلی:

1. نصب پیش‌نیازها:
   - JDK 11 یا بالاتر
   - Android SDK (platform-tools, build-tools و پلتفرم API 33)

2. باز کردن پروژه در Android Studio: وارد پوشهٔ `CandleMaster_Android` شوید و آن را به عنوان پروژهٔ اندروید باز کنید. Android Studio وابستگی‌ها و Gradle wrapper را تولید/همگام‌سازی خواهد کرد.

3. اضافه کردن دارایی‌ها:
   - فایل‌ها و پوشه‌های `assets/`, `images/`, `sounds/`, `libs/` که ممکن است در ریشهٔ repository یا در شاخهٔ بالاتر workspace قرار داشته باشند را به `app/src/main/assets/` و `app/src/main/res/` منتقل یا کپی کنید.
   - برای راحتی یک اسکریپت خودکار اضافه شده است: `scripts/sync_assets.sh` — اجرای این اسکریپت تمام دارایی‌های قابل‌تشخیص را به پوشه‌های مناسب داخل `app` کپی می‌کند.

   - مثال اجرا:
```
bash scripts/sync_assets.sh
```

4. ساخت APK (با Gradle wrapper یا از داخل Android Studio):
   - با wrapper (پس از همگام‌سازی در Android Studio یا اجرای `gradle wrapper`):
```
./gradlew assembleDebug
```
   - خروجی در `app/build/outputs/apk/debug/` قرار می‌گیرد.

5. امضای release و انتشار: برای release از یک keystore استفاده کنید و `./gradlew assembleRelease` را اجرا کرده و سپس APK را امضا کنید.

CI (GitHub Actions): می‌توانم یک workflow اضافه کنم که روی runner اندروید SDK و Gradle را نصب کرده و APK را بسازد و به عنوان artifact آپلود کند — اگر مایل باشید انجام می‌دهم.
