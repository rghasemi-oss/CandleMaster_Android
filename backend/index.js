const express = require('express');
const cors = require('cors');
const { createClient } = require('@supabase/supabase-js');
require('dotenv').config();
const ws = require('ws'); 

const app = express();
app.use(cors());
app.use(express.json());

const PORT = process.env.PORT || 3000;
const SUPABASE_URL = process.env.SUPABASE_URL;
const SUPABASE_KEY = process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.SUPABASE_ANON_KEY;

if (!SUPABASE_URL || !SUPABASE_KEY) {
    console.error("❌ Error: SUPABASE_URL and Supabase Key must be defined in environment variables!");
}

const supabase = createClient(SUPABASE_URL || '', SUPABASE_KEY || '', {
    auth: {
        persistSession: false 
    },
    realtime: {
        transport: ws 
    }
});

// صفحه اصلی تغییر یافته برای تست همزمان سرور و دیتابیس
app.get('/', async (req, res) => {
    try {
        if (!SUPABASE_URL || !SUPABASE_KEY) {
            return res.json({
                status: 'online',
                service: 'NDN Delivery Backend API',
                supabase_status: '❌ Error: Environment variables are missing on Render!'
            });
        }

        // تست اتصال به تیبل دیتابیس
        const { error, count } = await supabase
            .from('deliveries')
            .select('*', { count: 'exact', head: true });

        if (error) {
            return res.json({
                status: 'online',
                service: 'NDN Delivery Backend API',
                supabase_status: '❌ Failed to connect to Supabase: ' + error.message
            });
        }

        res.json({
            status: 'online',
            service: 'NDN Delivery Backend API',
            supabase_status: '✅ Successfully connected to Supabase database!',
            totalRecords: count || 0,
            timestamp: new Date()
        });

    } catch (err) {
        res.json({
            status: 'online',
            service: 'NDN Delivery Backend API',
            supabase_status: '❌ Exception: ' + err.message
        });
    }
});

app.listen(PORT, () => {
    console.log(`🚀 NDN Backend API running on port ${PORT}`);
});
