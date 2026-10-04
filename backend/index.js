/*
 * Compatibility entrypoint.
 *
 * Existing Render/Docker configurations may still call:
 *     node index.js
 *
 * Keep this file so existing deployment configuration does not
 * need to be changed immediately.
 */

require('./server');
