# NDN Backend API (Node.js & Express)

این پکیج شامل کدهای بک‌اند اپلیکیشن **NDN** است که به پایگاه داده **Supabase** متصل می‌شود و آمادهٔ استقرار روی **Render.com** است.

## راهنمای استقرار روی Render.com:

1. **آپلود به گیت‌هاب**:
   - محتویات پوشه `backend` را در یک ریپازیتوری جداگانه در گیت‌هاب (GitHub) آپلود کنید.

2. **ساخت وب‌سرویس در Render**:
   - وارد [Render Dashboard](https://dashboard.render.com/) شوید.
   - روی گزینه **New +** کلیک کرده و **Web Service** را انتخاب کنید.
   - ریپازیتوری گیت‌هاب خود را متصل کنید.

3. **تنظیمات بیلد و اجرا**:
   - **Name**: `ndn-backend-api`
   - **Environment**: `Node`
   - **Build Command**: `npm install`
   - **Start Command**: `npm start`

4. **تنظیم Environment Variables در Render**:
   - متغیرهای زیر را در بخش Environment تنظیم کنید:
     - `SUPABASE_URL`: آدرس پروژه Supabase شما
     - `SUPABASE_SERVICE_ROLE_KEY`: کلید امنیتی سرویس (Service Role Key) از تنظیمات API پایگاه داده Supabase

5. **دریافت آدرس (`RENDER_BACKEND_URL`)**:
   - پس از کلیک روی **Create Web Service**، رندر پس از چند ثانیه لینک اختصاصی سرور شما را (مانند `https://ndn-backend-api.onrender.com`) تولید می‌کند. همان لینک را در اپلیکیشن اندروید وارد کنید.
