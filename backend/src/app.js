const express = require('express');
const cors = require('cors');

const config = require('./config');

const deliveriesRouter = require('./routes/deliveries');
const hubsRouter = require('./routes/hubs');

const app = express();

app.use(cors({
    origin: config.corsOrigins === '*' ? true : config.corsOrigins,
}));

app.use(express.json());

app.get('/', (req, res) => {
    res.json({
        success: true,
        service: 'N-PuDo-N Backend API',
        version: 'v1',
        status: 'ok',
    });
});

app.get('/api/v1/health', (req, res) => {
    res.json({
        success: true,
        service: 'N-PuDo-N Backend API',
        version: 'v1',
        status: 'ok',
        timestamp: new Date().toISOString(),
    });
});

app.use('/api/v1/deliveries', deliveriesRouter);
app.use('/api/v1/hubs', hubsRouter);

app.use((req, res) => {
    res.status(404).json({
        success: false,
        error: 'Route not found',
    });
});

app.use((error, req, res, next) => {
    console.error(error);

    res.status(500).json({
        success: false,
        error: 'Internal server error',
    });
});

module.exports = app;
