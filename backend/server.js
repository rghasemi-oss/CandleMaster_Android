const express = require('express');
const cors = require('cors');
const { createClient } = require('@supabase/supabase-js');
require('dotenv').config();

const app = express();
app.use(cors());
app.use(express.json());

const PORT = process.env.PORT || 3000;
const SUPABASE_URL = process.env.SUPABASE_URL;
const SUPABASE_SERVICE_ROLE_KEY = process.env.SUPABASE_SERVICE_ROLE_KEY || process.env.SUPABASE_ANON_KEY;

if (!SUPABASE_URL || !SUPABASE_SERVICE_ROLE_KEY) {
    console.error("❌ Error: SUPABASE_URL and SUPABASE_SERVICE_ROLE_KEY must be provided!");
}

const supabase = createClient(SUPABASE_URL, SUPABASE_SERVICE_ROLE_KEY);

// Health check endpoint
app.get('/', (req, res) => {
    res.json({ status: 'online', service: 'NDN Delivery Backend API', timestamp: new Date() });
});

// --- Deliveries Endpoints ---
app.get('/api/deliveries', async (req, res) => {
    try {
        const { data, error } = await supabase.from('deliveries').select('*').order('timestamp', { ascending: false });
        if (error) throw error;
        res.json(data);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

app.post('/api/deliveries', async (req, res) => {
    try {
        const { trackingId, sender, customer, hubName, status, weight, timestamp } = req.body;
        const { data, error } = await supabase.from('deliveries').upsert([
            { trackingId, sender, customer, hubName, status, weight, timestamp: timestamp || Date.now() }
        ]).select();
        if (error) throw error;
        res.json({ success: true, data });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

app.delete('/api/deliveries/:trackingId', async (req, res) => {
    try {
        const { trackingId } = req.params;
        const { error } = await supabase.from('deliveries').delete().eq('trackingId', trackingId);
        if (error) throw error;
        res.json({ success: true });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// --- Hubs Endpoints ---
app.get('/api/hubs', async (req, res) => {
    try {
        const { data, error } = await supabase.from('hubs').select('*').order('name', { ascending: true });
        if (error) throw error;
        res.json(data);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

app.listen(PORT, () => {
    console.log(`🚀 NDN Backend API running on port ${PORT}`);
});
