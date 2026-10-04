const express = require('express');
const supabase = require('../supabase');
const { requireAuth } = require('../middleware/auth');

const router = express.Router();

router.use(requireAuth);

router.get('/', async (req, res) => {
    try {
        const { data, error } = await supabase
            .from('hubs')
            .select('*')
            .order('name', { ascending: true });

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

module.exports = router;
