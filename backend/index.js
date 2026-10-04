const express = require('express');
const cors = require('cors');
const { createClient } = require('@supabase/supabase-js');
require('dotenv').config();
const ws = require('ws'); // ماژول وب‌ساکت برای رفع ارور رندر

const app = express();
app.use(cors());
app.use(express.json());

const PORT = process.env.PORT || 3000;
const SUPABASE_URL = process.env.SUPABASE_URL;
const SUPABASE_KEY = process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.SUPABASE_ANON_KEY;

if (!SUPABASE_URL || !SUPABASE_KEY) {
    console.error("❌ Error: SUPABASE_URL and Supabase Key must be defined in environment variables!");
}

// ساخت کلاینت سوپابیس به همراه پیکربندی وب‌ساکت و غیرفعال‌سازی سشن سروری
const supabase = createClient(SUPABASE_URL || '', SUPABASE_KEY || '', {
    auth: {
        persistSession: false // بهینه‌سازی شده برای محیط‌های سروری مثل رندر
    },
    realtime: {
        transport: ws // رفع خطای نبود نیتو وب‌ساکت در نود ۲۰
    }
});

// Root endpoint
app.get('/', (req, res) => {
    res.json({ status: 'online', service: 'NDN Delivery Backend API', timestamp: new Date() });
});

// Test Supabase Database Connection Endpoint
app.get('/api/test-db', async (req, res) => {
    try {
        if (!SUPABASE_URL || !SUPABASE_KEY) {
            return res.status(500).json({
                success: false,
                message: 'Supabase environment variables are missing on the server.'
            });
        }

        // Query the deliveries table (or a lightweight test query)
        const { data, error, count } = await supabase
            .from('deliveries')
            .select('*', { count: 'exact', head: true });

        if (error) {
            return res.status(500).json({
                success: false,
                message: 'Failed to connect to Supabase database',
                error: error.message
            });
        }

        res.json({
            success: true,
            message: 'Successfully connected to Supabase database!',
            table: 'deliveries',
            totalRecords: count || 0,
            timestamp: new Date()
        });
    } catch (err) {
        res.status(500).json({
            success: false,
            message: 'Exception occurred while testing database connection',
            error: err.message
        });
    }
});

app.listen(PORT, () => {
    console.log(`🚀 NDN Backend API running on port ${PORT}`);
});
