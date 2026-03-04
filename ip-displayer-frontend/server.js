const express = require('express');
const axios = require('axios');
const app = express();

app.set('trust proxy', true);

const PORT = process.env.PORT || 8080;
const BACKEND_URL = process.env.BACKEND_URL || 'http://ip-echo-api-service:8088';

app.set('view engine', 'ejs');

app.get('/', async (req, res) => {
    const traceId = Math.floor(Math.random() * 100000);
    const clientIp = req.ip;

    console.log(`[Trace-${traceId}] STAGE 1: Frontend received request from Client IP: ${clientIp}`);
    console.log(`[Trace-${traceId}] STAGE 2: Re-routing to Backend (${BACKEND_URL}) to validate...`);

    try {
        const startTime = Date.now();
        const response = await axios.get(BACKEND_URL, { 
            timeout: 3000,
            headers: {
                'X-Forwarded-For': clientIp,
                'X-Trace-ID': traceId // Custom header to pass the ID to Java
            }
        });
        const duration = Date.now() - startTime;

        console.log(`[Trace-${traceId}] STAGE 4: Backend validated IP as ${response.data.ip}. (Took ${duration}ms)`);
        console.log(`[Trace-${traceId}] STAGE 5: Sending final response back to User.`);
        
        res.render('index', { 
            ip: response.data.ip, 
            status: `Validated (Trace: ${traceId})`, 
            backend: BACKEND_URL,
            error: null 
        });
    } catch (err) {
        console.error(`[Trace-${traceId}] ERROR: Failed to reach Backend: ${err.message}`);
        res.render('index', { 
            ip: '0.0.0.0', 
            status: 'Disconnected', 
            backend: BACKEND_URL,
            error: `Re-route failed: ${err.message}` 
        });
    }
});

app.listen(PORT, '0.0.0.0', () => {
    console.log(`Frontend Visualizer active on port ${PORT}`);
    console.log(`Routing requests to: ${BACKEND_URL}`);
});
