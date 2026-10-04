const express = require('express');
const cors = require('cors');
const { createClient } = require('@supabase/supabase-js');
require('dotenv').config();
const ws = require('ws'); 

const app = express();

// مدیریت تنظیمات CORS برای دسترسی امن اندروید و وب‌سایت PWA گیت‌هاب
app.use(cors({
    origin: '*',
    methods: ['GET', 'POST', 'PUT', 'DELETE'],
    allowedHeaders: ['Content-Type', 'Authorization']
}));

app.use(express.json());

const PORT = process.env.PORT || 3000;
const SUPABASE_URL = process.env.SUPABASE_URL;
const SUPABASE_KEY = process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.SUPABASE_ANON_KEY;

if (!SUPABASE_URL || !SUPABASE_KEY) {
    console.error("❌ Error: SUPABASE_URL and Supabase Key must be defined in environment variables!");
}

const supabase = createClient(SUPABASE_URL || '', SUPABASE_KEY || '', {
    auth: { persistSession: false },
    realtime: { transport: ws }
});

// ۱. اندپوینت خواندن آمار (GET)
app.get('/', async (req, res) => {
    try {
        const { error, count } = await supabase
            .from('deliveries')
            .select('*', { count: 'exact', head: true });

        if (error) {
            return res.json({ status: 'online', service: 'NDN API', supabase_status: '❌ Error: ' + error.message });
        }

        res.json({
            status: 'online',
            service: 'NDN Delivery Backend API',
            supabase_status: '✅ Successfully connected!',
            totalRecords: count || 0,
            timestamp: new Date()
        });
    } catch (err) {
        res.json({ status: 'online', supabase_status: '❌ Exception: ' + err.message });
    }
});

// ۲. اندپوینت جدید برای ثبت واقعی بارنامه در سوپابیس (POST)
app.post('/api/deliveries', async (req, res) => {
    try {
        const { sender, customer, status } = req.body;

        if (!sender || !customer) {
            return res.status(400).json({ success: false, message: 'نام فرستنده و گیرنده الزامی است.' });
        }

        // تولید یک کد رهگیری تصادفی و استاندارد برای مرسوله
        const trackingId = 'TRK-' + Math.floor(10000 + Math.random() * 90000);

        // درج مستقیم مستندات در جدول deliveries سوپابیس
        const { data, error } = await supabase
            .from('deliveries')
            .insert([
                { 
                    trackingId: trackingId, 
                    sender: sender, 
                    customer: customer, 
                    status: status || 'PENDING',
                    timestamp: new Date().toISOString()
                }
            ])
            .select();

        if (error) {
            return res.status(500).json({ success: false, message: 'خطا در ثبت سوپابیس', error: error.message });
        }

        res.json({
            success: true,
            message: '📦 بارنامه با موفقیت در دیتابیس سوپابیس ذخیره شد!',
            trackingId: trackingId,
            data: data[0]
        });

    } catch (err) {
        res.status(500).json({ success: false, message: 'خطای سرور رندر', error: err.message });
    }
});

app.listen(PORT, () => {
    console.log(`🚀 NDN Backend API running on port ${PORT}`);
});
