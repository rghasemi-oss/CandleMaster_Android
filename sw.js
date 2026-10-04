const CACHE_NAME = 'ndn-pwa-v3';

const STATIC_ASSETS = [
    './',
    './index.html',
    './app_manifest.json',
    './icons/icon-192.png',
    './icons/icon-512.png'
];

self.addEventListener('install', event => {
    event.waitUntil(
        caches.open(CACHE_NAME)
            .then(cache => cache.addAll(STATIC_ASSETS))
            .then(() => self.skipWaiting())
    );
});

self.addEventListener('activate', event => {
    event.waitUntil(
        caches.keys().then(keys =>
            Promise.all(
                keys
                    .filter(key => key !== CACHE_NAME)
                    .map(key => caches.delete(key))
            )
        ).then(() => self.clients.claim())
    );
});

self.addEventListener('fetch', event => {

    const request = event.request;
    const url = new URL(request.url);

    // Never cache API/backend requests.
    if (
        url.hostname === 'candlemaster-android.onrender.com' ||
        request.method !== 'GET'
    ) {
        return;
    }

    // Cache only same-origin static PWA resources.
    if (url.origin !== self.location.origin) {
        return;
    }

    event.respondWith(
        caches.match(request).then(cachedResponse => {

            if (cachedResponse) {
                return cachedResponse;
            }

            return fetch(request).then(networkResponse => {

                if (
                    !networkResponse ||
                    networkResponse.status !== 200 ||
                    networkResponse.type !== 'basic'
                ) {
                    return networkResponse;
                }

                const responseClone = networkResponse.clone();

                caches.open(CACHE_NAME).then(cache => {
                    cache.put(request, responseClone);
                });

                return networkResponse;
            });
        })
    );
});
