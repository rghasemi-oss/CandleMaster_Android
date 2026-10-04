const supabase = require('../supabase');

async function requireAuth(req, res, next) {
    const authorization = req.headers.authorization || '';

    if (!authorization.startsWith('Bearer ')) {
        return res.status(401).json({
            success: false,
            error: 'Missing Bearer token'
        });
    }

    const token = authorization.substring('Bearer '.length).trim();

    if (!token) {
        return res.status(401).json({
            success: false,
            error: 'Empty Bearer token'
        });
    }

    try {
        const { data, error } = await supabase.auth.getUser(token);

        if (error || !data || !data.user) {
            return res.status(401).json({
                success: false,
                error: 'Invalid authentication token'
            });
        }

        req.user = data.user;
        next();
    } catch (error) {
        return res.status(401).json({
            success: false,
            error: 'Authentication failed'
        });
    }
}

module.exports = {
    requireAuth
};
