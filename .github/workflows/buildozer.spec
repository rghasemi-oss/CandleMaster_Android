[app]
# ===============================
# اطلاعات پایه برنامه
# ===============================
title = CandleMaster Pro
package.name = candlemasterpro
package.domain = ir.candlemaster
version = 1.0.0

# ===============================
# سورس پروژه
# ===============================
source.dir = .
source.include_exts = py,png,ttf
source.exclude_dirs = tests,__pycache__

main = main.py

# ===============================
# وابستگی‌های پایتون (اندروید امن)
# ===============================
requirements = python3,kivy==2.1.0,requests,numpy,plyer

# ===============================
# تنظیمات رابط
# ===============================
orientation = portrait
fullscreen = 0
window.softinput_mode = resize

# ===============================
# آیکون و اسپلش
# ===============================
icon.filename = assets/icon.png
presplash.filename = assets/splash.png
presplash.color = #4CAF50

# ===============================
# تنظیمات اندروید (پایدار)
# ===============================
android.api = 31
android.minapi = 21
android.ndk = 25b
android.ndk_api = 21

android.arch = armeabi-v7a,arm64-v8a

android.permissions = INTERNET,WAKE_LOCK
android.allow_backup = true
android.private_storage = true

# ===============================
# AndroidX
# ===============================
android.enable_androidx = true
android.enable_jetifier = true

# ===============================
# لاگ
# ===============================
log_level = 2

# ===============================
# امضا (دیباگ)
# ===============================
android.debug_signkey = debug.keystore
android.debug_signkey_storepass = android
android.debug_signkey_keypass = android

# ===============================
# انتشار (برای مرحله بعدی Google Play)
# ===============================
# برای ساخت AAB در مرحله ریلیز این را فعال کن:
# android.release_artifact = aab