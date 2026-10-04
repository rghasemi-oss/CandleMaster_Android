require('dotenv').config();

const config = {
    port: Number(process.env.PORT || 3000),
    corsOrigins: process.env.CORS_ORIGINS || '*',
    supabaseUrl: process.env.SUPABASE_URL || null,
    supabaseServiceRoleKey: process.env.SUPABASE_SERVICE_ROLE_KEY || null,
};

module.exports = config;
