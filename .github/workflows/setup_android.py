import os
import sys
import subprocess
import shutil
from pathlib import Path

#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
اسکریپت راه‌اندازی CandleMaster برای اندروید
"""


def check_prerequisites():
    """بررسی پیش‌نیازها"""
    print("🔍 بررسی پیش‌نیازها...")
    
    # بررسی Python
    try:
        subprocess.run([sys.executable, "--version"], check=True, capture_output=True)
        print("✓ پایتون شناسایی شد")
    except:
        print("❌ پایتون یافت نشد")
        return False
    
    # بررسی Buildozer
    try:
        subprocess.run(["buildozer", "--version"], check=True, capture_output=True)
        print("✓ Buildozer شناسایی شد")
    except:
        print("❌ Buildozer یافت نشد")
        print("📥 در حال نصب Buildozer...")
        try:
            subprocess.run([sys.executable, "-m", "pip", "install", "buildozer"], check=True)
            print("✓ Buildozer نصب شد")
        except:
            print("❌ خطا در نصب Buildozer")
            return False
    
    # بررسی وابستگی‌های سیستمی برای لینوکس
    if sys.platform == "linux":
        print("🔧 بررسی وابستگی‌های لینوکس...")
        try:
            subprocess.run(["dpkg", "--version"], check=True, capture_output=True)
            
            # لیست بسته‌های مورد نیاز
            packages = [
                "build-essential",
                "ccache",
                "git",
                "autoconf",
                "libtool",
                "pkg-config",
                "zlib1g-dev",
                "libncurses5-dev",
                "libncursesw5-dev",
                "libtinfo5",
                "cmake",
                "libffi-dev",
                "libssl-dev",
                "python3-dev",
                "python3-venv"
            ]
            
            # نصب بسته‌ها (در اوبونتو/دبیان)
            for package in packages:
                print(f"  بررسی {package}...")
                result = subprocess.run(
                    ["dpkg", "-s", package],
                    capture_output=True,
                    text=True
                )
                if result.returncode != 0:
                    print(f"  📦 نصب {package}...")
                    subprocess.run(
                        ["sudo", "apt-get", "install", "-y", package],
                        check=True
                    )
            
            print("✓ وابستگی‌های لینوکس نصب شدند")
        except Exception as e:
            print(f"⚠️ خطا در بررسی وابستگی‌ها: {e}")
    
    return True

def setup_project_structure():
    """ایجاد ساختار پروژه اندروید"""
    print("\n📁 ایجاد ساختار پروژه اندروید...")
    
    # پوشه‌های مورد نیاز
    folders = [
        "assets",
        "data",
        "libs",
        "sounds",
        "fonts",
        "images"
    ]
    
    for folder in folders:
        os.makedirs(folder, exist_ok=True)
        print(f"  ✓ پوشه {folder} ایجاد شد")
    
    # فایل‌های نمونه
    sample_files = {
        "assets/icon.png": "آیکون برنامه (512x512)",
        "assets/splash.png": "صفحه اسپلش (1920x1080)",
        "data/symbols.json": "لیست نمادها",
        "fonts/Vazir.ttf": "فونت فارسی وزیر"
    }
    
    for file_path, description in sample_files.items():
        if not os.path.exists(file_path):
            print(f"  ⚠️ فایل {file_path} یافت نشد ({description})")
    
    return True

def install_dependencies():
    """نصب وابستگی‌های پایتون"""
    print("\n📦 نصب وابستگی‌های پایتون...")
    
    # لیست کتابخانه‌ها
    dependencies = [
        "kivy==2.1.0",
        "kivymd==1.1.1",
        "buildozer",
        "requests",
        "pandas",
        "numpy",
        "matplotlib",
        "yfinance"
    ]
    
    for dep in dependencies:
        print(f"  📥 نصب {dep}...")
        try:
            subprocess.run(
                [sys.executable, "-m", "pip", "install", dep],
                check=True,
                capture_output=True
            )
            print(f"    ✓ {dep} نصب شد")
        except Exception as e:
            print(f"    ❌ خطا در نصب {dep}: {e}")
    
    return True

def create_sample_assets():
    """ایجاد فایل‌های نمونه اگر وجود ندارند"""
    print("\n🎨 ایجاد فایل‌های نمونه...")
    
    # فونت فارسی نمونه (اگر وزیر وجود ندارد)
    if not os.path.exists("fonts/Vazir.ttf"):
        print("  ⚠️ فونت فارسی یافت نشد")
        print("  💡 پیشنهاد: فونت Vazir را از fonts.google.com دانلود کنید")
    
    # آیکون نمونه
    if not os.path.exists("assets/icon.png"):
        print("  ⚠️ آیکون یافت نشد")
        print("  💡 پیشنهاد: آیکون 512x512 با پسزمینه شفاف ایجاد کنید")
    
    # اسپلش اسکرین
    if not os.path.exists("assets/splash.png"):
        print("  ⚠️ اسپلش اسکرین یافت نشد")
        print("  💡 پیشنهاد: تصویر 1920x1080 با لوگو برنامه ایجاد کنید")
    
    return True

def build_android_apk():
    """ساخت فایل APK"""
    print("\n🏗️ شروع ساخت APK برای اندروید...")
    
    try:
        # ساخت APK دیباگ
        print("🔨 ساخت APK (ممکن است 10-30 دقیقه طول بکشد)...")
        
        process = subprocess.Popen(
            ["buildozer", "android", "debug"],
            stdout=subprocess.PIPE,
            stderr=subprocess.STDOUT,
            universal_newlines=True
        )
        
        # نمایش خروجی واقعی
        for line in process.stdout:
            print(line.strip())
        
        process.wait()
        
        if process.returncode == 0:
            print("\n🎉 ساخت APK با موفقیت کامل شد!")
            
            # پیدا کردن فایل APK
            apk_files = list(Path("bin").glob("*.apk"))
            if apk_files:
                print(f"\n📦 فایل APK ساخته شده:")
                for apk in apk_files:
                    size_mb = apk.stat().st_size / (1024 * 1024)
                    print(f"  📄 {apk.name} ({size_mb:.1f} MB)")
                
                print("\n🚀 برای نصب روی گوشی:")
                print("  1. فایل APK را به گوشی انتقال دهید")
                print("  2. در گوشی، فایل را باز کنید")
                print("  3. اجازه نصب از منابع ناشناس را بدهید")
                print("  4. برنامه را نصب و اجرا کنید")
            else:
                print("⚠️ فایل APK یافت نشد")
        
        return True
        
    except Exception as e:
        print(f"❌ خطا در ساخت APK: {e}")
        return False

def build_for_windows():
    """راهنمای ساخت در ویندوز"""
    print("\n🖥️ راهنمای ساخت در ویندوز:")
    print("=" * 50)
    print("\nبرای ساخت APK در ویندوز:")
    print("\n1. WSL2 نصب کنید (ویندوز ساب‌سیستم لینوکس):")
    print("   - PowerShell را با دسترسی Admin باز کنید")
    print("   - دستور زیر را اجرا کنید:")
    print("     wsl --install -d Ubuntu")
    print("\n2. در WSL:")
    print("   - پروژه را کپی کنید")
    print("   - این اسکریپت را اجرا کنید")
    print("\n3. یا از خدمات ابری استفاده کنید:")
    print("   - GitHub Actions")
    print("   - Google Colab")
    print("   - خدمات ساخت APK آنلاین")
    print("\n4. روش جایگزین: استفاده از PyCharm + Kivy plugin")

def main():
    """تابع اصلی"""
    print("=" * 60)
    print("     CandleMaster Pro - ساخت اپلیکیشن اندروید")
    print("=" * 60)
    
    # بررسی پلتفرم
    current_platform = sys.platform
    print(f"\n🎯 پلتفرم فعلی: {current_platform}")
    
    if current_platform.startswith("win"):
        print("\n⚠️ توجه: ساخت APK در ویندوز مستقیم ممکن نیست")
        build_for_windows()
        return
    
    # مرحله 1: بررسی پیش‌نیازها
    if not check_prerequisites():
        print("\n❌ پیش‌نیازها تکمیل نیستند")
        return
    
    # مرحله 2: ساختار پروژه
    setup_project_structure()
    
    # مرحله 3: نصب وابستگی‌ها
    install_dependencies()
    
    # مرحله 4: ایجاد فایل‌های نمونه
    create_sample_assets()
    
    print("\n" + "=" * 60)
    print("✅ پروژه آماده ساخت است")
    print("\n📋 فایل‌های ایجاد شده:")
    print("  📄 main.py - برنامه اصلی")
    print("  📄 buildozer.spec - تنظیمات ساخت")
    print("  📄 requirements.txt - کتابخانه‌ها")
    print("  📁 assets/ - تصاویر و فونت‌ها")
    print("  📁 data/ - داده‌ها")
    
    # سوال برای ساخت
    print("\n" + "=" * 60)
    choice = input("\nآیا می‌خواهید APK بسازید؟ (y/N): ")
    
    if choice.lower() == 'y':
        build_android_apk()
    else:
        print("\n📌 دستورالعمل دستی:")
        print("برای ساخت APK دستی:")
        print("1. buildozer init")
        print("2. buildozer.spec را تنظیم کنید")
        print("3. buildozer android debug deploy run")
        print("\nبرای تست روی کامپیوتر:")
        print("python main.py")

if __name__ == "__main__":
    main()