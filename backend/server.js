const app = require('./src/app');
const config = require('./src/config');

app.listen(config.port, '0.0.0.0', () => {
    console.log(
        `N-PuDo-N Backend API running on port ${config.port}`
    );
});
