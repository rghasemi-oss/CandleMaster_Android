const express = require('express');
const { getSupabase } = require('../supabase');
const { requireAuth } = require('../middleware/auth');

const router = express.Router();

router.use(requireAuth);

router.get('/', async (req, res) => {
    try {
        const supabase = getSupabase();

        const { data, error } = await supabase
            .from('deliveries')
            .select('*')
            .order('timestamp', { ascending: false });

        if (error) {
            return res.status(500).json({
                success: false,
                error: error.message
            });
        }

        return res.json({
            success: true,
            data: data || []
        });
    } catch (error) {
        return res.status(500).json({
            success: false,
            error: error.message
        });
    }
});

router.post('/', async (req, res) => {
    try {
        const supabase = getSupabase();

        const {
            trackingId,
            sender,
            customer,
            hubName,
            status,
            weight,
            timestamp
        } = req.body || {};

        if (!sender || !customer) {
            return res.status(400).json({
                success: false,
                error: 'sender and customer are required'
            });
        }

        const finalTrackingId =
            trackingId ||
            `TRK-${Math.floor(10000 + Math.random() * 90000)}`;

        const record = {
            trackingId: finalTrackingId,
            sender,
            customer,
            hubName: hubName || null,
            status: status || 'PENDING',
            weight: weight || null,
            timestamp: timestamp || new Date().toISOString()
        };

        const { data, error } = await supabase
            .from('deliveries')
            .insert([record])
            .select();

        if (error) {
            return res.status(500).json({
                success: false,
                error: error.message
            });
        }

        return res.status(201).json({
            success: true,
            data: data?.[0] || record
        });
    } catch (error) {
        return res.status(500).json({
            success: false,
            error: error.message
        });
    }
});

router.delete('/:trackingId', async (req, res) => {
    try {
        const supabase = getSupabase();
        const { trackingId } = req.params;

        const { error } = await supabase
            .from('deliveries')
            .delete()
            .eq('trackingId', trackingId);

        if (error) {
            return res.status(500).json({
                success: false,
                error: error.message
            });
        }

        return res.json({
            success: true,
            trackingId
        });
    } catch (error) {
        return res.status(500).json({
            success: false,
            error: error.message
        });
    }
});

module.exports = router;
