const express = require('express');
const axios = require('axios');
const app = express();

const PORT = process.env.PORT || 8080;
const BACKEND_URL = process.env.BACKEND_URL || 'http://ip-echo-service:8088';

app.set('view engine', 'ejs');

app.get('/', async (req, res) => {
    try {
        const clientIp = req.headers['x-forwarded-for'] || req.socket.remoteAddress;

        const response = await axios.get(BACKEND_URL, { 
            timeout: 3000,
            headers: {
                'X-Forwarded-For': clientIp
            }
        });
        
        res.render('index', { 
            ip: response.data.ip, 
            status: 'Connected', 
            backend: BACKEND_URL,
            error: null 
        });
    } catch (err) {
        console.error('SRC Error:', err.message);
        res.render('index', { 
            ip: '0.0.0.0', 
            status: 'Disconnected', 
            backend: BACKEND_URL,
            error: 'Cannot reach the Java Backend Service' 
        });
    }
});

app.listen(PORT, '0.0.0.0', () => {
    console.log(`Frontend Visualizer active on port ${PORT}`);
    console.log(`Routing SRC requests to: ${BACKEND_URL}`);
});
