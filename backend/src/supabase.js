const { createClient } = require('@supabase/supabase-js');
const config = require('./config');

let supabase = null;

if (config.supabaseUrl && config.supabaseServiceRoleKey) {
    supabase = createClient(
        config.supabaseUrl,
        config.supabaseServiceRoleKey,
        {
            auth: {
                persistSession: false,
                autoRefreshToken: false,
            },
        }
    );
}

function getSupabase() {
    if (!supabase) {
        const error = new Error(
            'Supabase is not configured. Set SUPABASE_URL and SUPABASE_SERVICE_ROLE_KEY.'
        );
        error.code = 'SUPABASE_NOT_CONFIGURED';
        throw error;
    }

    return supabase;
}

module.exports = {
    supabase,
    getSupabase,
};
